package com.sdk;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import com.testlog.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

@SuppressWarnings("unused")
public class TestMemoryUnlock001 {

//    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestMemoryUnlock001";
    public static final String deviceId = TestMain.deviceId;

//    /********************************
//     PUBLIC Methods
//     ********************************/
    public static SuperA superA;
    public static Device thisDevice;

    private static final TestUnit thisUnit = new TestUnit();

    /*----------------------------------------------------------------------------
    run
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test run method

    Security Level: None

    ------------------------------------------------------------------------------*/
    @Test
    public void run() {
        thisUnit.setTestTitle(testBatch);

        int j;
        // ---------------------- Code -------------------------------

        Command.onError = Command.ALT_ON_ERROR;

        j = 1;
        try {
            thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

            thisUnit.setDevice(thisDevice);

            superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);

            for (int u = 0; u < 1; u++) {
                testCase01();
                j++;
            }
        } catch (TestException | DiscoveryException e) {
            thisUnit.testCompleted(false, "failure at test case " + j);
            Logger.detail("TEST FAILURE ----->" + j);
            Assertions.fail("TEST FAILURE ----->" + j);

            //return false;
        }
        thisUnit.testCompleted(true, "success!");
        Logger.detail("OK");

    }

    /*----------------------------------------------------------------------------
testCase01
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Memory Unlock

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);
        String pin;
        String testCase = testBatch + "/" + "Test Case 01";
        tc.setCaseTitle(testCase);

        Apdu apdu;


        Command c = new Command();
        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // ping
            thisDevice.ping();
            pin = "01020304";

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            //#
            //# Memory Unlock Success
            //#
            apdu = new Apdu("ACB50300");
            apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin);
            apdu.addTlv((byte) 0xEB, "3132333435363738");

            // send command
            c.description = "Memory Unlock";
            c.requester = superA;
            c.execute(apdu.toString(), Command.COMMAND_EXPECTED_SUCCESSFUL_RESPONSE);
            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
 testCase01
 --------------------------------------------------------------------------
 AUTHOR:	PDI

 DESCRIPTION: Memory Unlock

 Security Level: None

 ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        String pin;
        String testCase = testBatch + "/" + "Test Case 02";
        tc.setCaseTitle(testCase);

        Apdu apdu;


        Command c = new Command();
        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // ping
            thisDevice.ping();
            pin = "01020304";

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            //#
            //# Memory Unlock Success
            //#
            apdu = new Apdu("ACB50300");
            apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin);
            apdu.addTlv((byte) 0xEB, "3232333435363738");

            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            // send command
            c.description = "Memory Unlock Wrong UKey";
            c.requester = superA;
            c.execute(apdu.toString(), expectedRes);
            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }
}

