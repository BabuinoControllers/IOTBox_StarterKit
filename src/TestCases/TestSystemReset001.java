package com.sdk;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.testlog.*;

@SuppressWarnings("unused")
public class TestSystemReset001 {

    /********************************
     PUBLIC Fields
     ********************************/
    public static final String testBatch = "TestSystemReset001";
    public static final String deviceId = TestMain.deviceId;

    /********************************
     Public Methods
     ********************************/
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

                testCase02();
                j++;

                testCase03();
                j++;

                testCase04();
                j++;

                testCase05();
                j++;

                testCase06();
                j++;
            }
            superA.resetPin(superA);
        } catch (TestException | DiscoveryException | CommandErrorException | ObjectException | IOException e) {
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

DESCRIPTION: Super Administrator perform a system reset

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        String pin;
        Door door;
        String gpioValue;
        String testCase = testBatch + "/" + "Test Case 01";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

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

            // set the door
            door = new Door(superA);
            door.setGpioId("05");
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);
            //#
            //# SuperA performs a system reset
            //#

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

            thisDevice.systemReset(pin, superA, true);


            superA.syncroFields(superA);
            gpioValue = door.getOutput(superA);
            if (!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW))) {
                throw new TestException();
            }
            //#
            //# Object deletion
            //#
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase02
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Super Administrator perform a factory reset

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        Command command = new Command();
        String pin;
        Door door;
        String testCase = testBatch + "/" + "Test Case 01";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);


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

            // set the door
            door = new Door(superA);
            door.setGpioId("05");
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);
            //#
            //# SuperA performs a system reset
            //#

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

            thisDevice.systemFactoryReset(pin, superA, true);

            Apdu apdu = new Apdu(Apdu.UPDATE_USER_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, User.SUPER_ADM_ID);

            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            Logger.detail("------------ SuperA is in preperso state ------------");

            // send command
            command.description = "SuperA is in perso state";
            command.requester = superA;
            command.execute(apdu.toString(), expectedRes);

            //#
            //# Object deletion
            //#
            // dorr already deleted
            //door.delete(superA);
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
testCase03
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Administrators and users can not perform a system reset

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 03";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

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

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.updateKey("adminx");
            admin.syncroFields(admin);
            admin.setPin(superA, pin);

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.updateKey("newAdministratorKeysSutta");
            user.syncroFields(user);
            user.setPin(superA, pin);

            //#
            //# Admin can not perform a system reset
            //#

            // create the apdu object
            Apdu apdu = new Apdu(Apdu.SYSTEM_RESET_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);

            // set the expected result and send the command;
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "System Reset";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not perform a system reset
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.SYSTEM_RESET_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "System Reset";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);

            //#
            //# Object deletion
            //#
            user.delete(superA);
            admin.delete(superA);
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
testCase04
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Administrators and users can not perform a factlry reset

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 04";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

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

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.updateKey("adminx");
            admin.syncroFields(admin);
            admin.setPin(superA, pin);

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.updateKey("newAdministratorKeysSutta");
            user.syncroFields(user);
            user.setPin(superA, pin);

            //#
            //# Admin can not perform a system reset
            //#

            // create the apdu object
            Apdu apdu = new Apdu(Apdu.SYSTEM_FACTORY_RESET_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);

            // set the expected result and send the command;
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "System Reset";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not perform a system reset
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.SYSTEM_FACTORY_RESET_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "System Reset";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);

            //#
            //# Object deletion
            //#
            user.delete(superA);
            admin.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            tc.testCompleted(false, "fail");

            Logger.testResult(false);
            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase05
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Administrators and users can not perform a system reset if PIN is wrong

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 05";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

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

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.updateKey("adminx");
            admin.syncroFields(admin);
            admin.setPin(superA, pin);

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.updateKey("newAdministratorKeysSutta");
            user.syncroFields(user);
            user.setPin(superA, pin);

            //#
            //# Admin can not perform a system reset withotu a PIN
            //#

            // create the apdu object
            Apdu apdu = new Apdu(Apdu.SYSTEM_RESET_APDU);

            // set the expected result and send the command;
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "System Reset";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not perform a system reset with a wrong PIN
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.SYSTEM_RESET_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "1235");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "System Reset";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);

            //#
            //# Object deletion
            //#
            user.delete(superA);
            admin.delete(superA);
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
testCase06
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Administrators and users can not perform a system reset if PIN is wrong

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException {
        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 06";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

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

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.updateKey("adminx");
            admin.syncroFields(admin);
            admin.setPin(superA, pin);

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.updateKey("newAdministratorKeysSutta");
            user.syncroFields(user);
            user.setPin(superA, pin);

            //#
            //# Admin can not perform a system reset withotu a PIN
            //#

            // create the apdu object
            Apdu apdu = new Apdu(Apdu.SYSTEM_FACTORY_RESET_APDU);

            // set the expected result and send the command;
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "System Reset";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not perform a system reset with a wrong PIN
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.SYSTEM_FACTORY_RESET_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "1235");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "System Reset";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);

            //#
            //# Object deletion
            //#
            user.delete(superA);
            admin.delete(superA);
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

