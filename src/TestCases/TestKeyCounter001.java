package com.sdk;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import com.testlog.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

@SuppressWarnings("unused")
public class TestKeyCounter001 {

//    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestKeyCounter001";
    public static final String deviceId = TestMain.deviceId;

//    /********************************
//     PUBLIC Methods
//     ********************************/
    public static User superA;
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
        superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);

        try {
            thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

            thisUnit.setDevice(thisDevice);

            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);

            for (int u = 0; u < 1; u++) {
                testCase01();
                j++;

                testCase02();
                j++;

                //thisDevice = Device.discover(deviceId, Device.WIFI);
                //IoStream.setActiveDevice(thisDevice);
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

DESCRIPTION: Check the transaction sequence. Onlty one User

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        RemoteAuthenticator a;
        int x, y;
        byte[] z;

        String testCase = testBatch + "/" + "Test Case 01";
        tc.setCaseTitle(testCase);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            a = superA.getRemoteAuthenticatorObject();

            //#
            //# Check Counter Sequence
            //# One User

            Command.isStopOnSynchroError = true;

            z = a.getKeyCounter();
            y = ((z[5] & 0xFF) << 16) + ((z[6] & 0xFF) << 8) + (z[7] & 0xFF);
            // object created
            for (int i = 0; i < 300; i++) {
                Logger.testCase("Attempt #" + i);
                superA.syncroFields(superA);

                z = a.getKeyCounter();
                x = ((z[5] & 0xFF) << 16) + ((z[6] & 0xFF) << 8) + (z[7] & 0xFF);
                y++;

                if (x != y)
                    throw new TestException();

            }
            Command.isStopOnSynchroError = false;
            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");

    }

    /*----------------------------------------------------------------------------
testCase03
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Check the transaction sequence. Two Users

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        RemoteAuthenticator raoSuperA;
        int sx, ax, sy, ay;
        byte[] superATransactionCounter, adminTransactionCounter;

        String testCase = testBatch + "/" + "Test Case 02";
        tc.setCaseTitle(testCase);



        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            raoSuperA = superA.getRemoteAuthenticatorObject();

            User admin = new User(superA, User.USER_ROLE_ADMIN, "initialAdmin");
            // object personalized
            //for(int i=0; i<1000; i++)
            admin.updateKey("admin");

            //#
            //# Check Counter Sequence
            //# One User

            Command.isStopOnSynchroError = true;

            superATransactionCounter = raoSuperA.getKeyCounter();
            adminTransactionCounter = raoSuperA.getKeyCounter();

            sy = ((superATransactionCounter[5] & 0xFF) << 16) + ((superATransactionCounter[6] & 0xFF) << 8) + (superATransactionCounter[7] & 0xFF);
            ay = ((adminTransactionCounter[5] & 0xFF) << 16) + ((adminTransactionCounter[6] & 0xFF) << 8) + (adminTransactionCounter[7] & 0xFF);
            // object created
            for (int i = 0; i < 300; i++) {
                Logger.testCase("Attempt #" + i);

                superA.syncroFields(superA);

                superATransactionCounter = raoSuperA.getKeyCounter();
                adminTransactionCounter = raoSuperA.getKeyCounter();

                sx = ((superATransactionCounter[5] & 0xFF) << 16) + ((superATransactionCounter[6] & 0xFF) << 8) + (superATransactionCounter[7] & 0xFF);

                sy++;
                if (sx != sy)
                    throw new TestException();

                admin.syncroFields(admin);
                ax = ((adminTransactionCounter[5] & 0xFF) << 16) + ((adminTransactionCounter[6] & 0xFF) << 8) + (adminTransactionCounter[7] & 0xFF);

                ay++;

                if (ax != ay)
                    throw new TestException();
            }
            Command.isStopOnSynchroError = false;
            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }
}

