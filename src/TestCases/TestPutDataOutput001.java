package com.sdk;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.testlog.*;

@SuppressWarnings("unused")
public class TestPutDataOutput001 {

//    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestPutDataOutput001";
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

DESCRIPTION: Super Administrator Trigger the output on, off and pulse

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        String pin;
        String testCase = testBatch + "/" + "Test Case 01";
        tc.setCaseTitle(testCase);

        String a;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // ping
            thisDevice.ping();
            pin = "01020304";

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            //#
            //# SuperA turns output on
            //#

            for (int i = 0; i < Device.GPIO_NUMBER; i++) {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);

                Logger.detail(" ON Output " + i);
                superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_ON, 0);

                a = s1.getMeasure(superA);
                if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                    throw new TestException();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
            //#
            //# SuperA turns output off
            //#
            for (int i = 0; i < Device.GPIO_NUMBER; i++) {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);

                Logger.detail(" OFF Output " + i);
                superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_OFF, 0);

                a = s1.getMeasure(superA);
                if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                    throw new TestException();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
            //#
            //# SuperA pulse output
            //#
            for (int i = 0; i < Device.GPIO_NUMBER; i++) {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);
                Logger.detail(" PULSE Output " + i);
                superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_PULSE, 1000);

                a = s1.getMeasure(superA);
                if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                    throw new TestException();
                try {
                    Thread.sleep(1700);
                } catch (InterruptedException ignored) {
                }

                a = s1.getMeasure(superA);
                if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                    throw new TestException();
            }

            //#
            //# Object deletion
            //#
            s1.delete(superA);
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

DESCRIPTION: Administrators and users can not change the output via put data

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 02";
        tc.setCaseTitle(testCase);


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
            Apdu apdu = new Apdu(Apdu.PUT_DATA_OUTPUT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);
            apdu.addTlv(Apdu.DATA_TAG_GPIO, "00");
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, SuperA.SE_OUTPUT_ON);
            apdu.addTlv(Apdu.DATA_TAG_DOOR_TIME, "00000000");

            // set the expected result and send the command;
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Put Data Output";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not perform a system reset
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.PUT_DATA_OUTPUT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);
            apdu.addTlv(Apdu.DATA_TAG_GPIO, "00");
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, SuperA.SE_OUTPUT_ON);
            apdu.addTlv(Apdu.DATA_TAG_DOOR_TIME, "00000000");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Put Data Output";
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
testCase03
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: SuperA, Administrators users can not perform a put data output if PIN is wrong

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 03";

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
            Apdu apdu = new Apdu(Apdu.PUT_DATA_OUTPUT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "1235");
            apdu.addTlv(Apdu.DATA_TAG_GPIO, "00");
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, SuperA.SE_OUTPUT_ON);
            apdu.addTlv(Apdu.DATA_TAG_DOOR_TIME, "00000000");

            // set the expected result and send the command;
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Put Data Output";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not perform a system reset with a wrong PIN
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.PUT_DATA_OUTPUT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "1235");
            apdu.addTlv(Apdu.DATA_TAG_GPIO, "00");
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, SuperA.SE_OUTPUT_ON);
            apdu.addTlv(Apdu.DATA_TAG_DOOR_TIME, "00000000");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Put Data Output";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# SuperA can not perform a system reset with a wrong PIN
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.PUT_DATA_OUTPUT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "1235");
            apdu.addTlv(Apdu.DATA_TAG_GPIO, "00");
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, SuperA.SE_OUTPUT_ON);
            apdu.addTlv(Apdu.DATA_TAG_DOOR_TIME, "00000000");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Put Data Output";
            command.requester = superA;
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

DESCRIPTION: SuperA, Administrators users can not perform a put data output if PIN is missing

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 04";

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
            Apdu apdu = new Apdu(Apdu.PUT_DATA_OUTPUT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_GPIO, "00");
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, SuperA.SE_OUTPUT_ON);
            apdu.addTlv(Apdu.DATA_TAG_DOOR_TIME, "00000000");

            // set the expected result and send the command;
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Put Data Output";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not perform a system reset with a wrong PIN
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.PUT_DATA_OUTPUT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_GPIO, "00");
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, SuperA.SE_OUTPUT_ON);
            apdu.addTlv(Apdu.DATA_TAG_DOOR_TIME, "00000000");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Put Data Output";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# SuperA can not perform a system reset with a wrong PIN
            //#

            // create the apdu object
            apdu = new Apdu(Apdu.PUT_DATA_OUTPUT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_GPIO, "00");
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, SuperA.SE_OUTPUT_ON);
            apdu.addTlv(Apdu.DATA_TAG_DOOR_TIME, "00000000");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6A88_DATA_NOT_FOUND_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Put Data Output";
            command.requester = superA;
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
testCase05
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Put data output return the status of the output

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        String pin;
        String testCase = testBatch + "/" + "Test Case 05";
        String a;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // ping
            thisDevice.ping();
            pin = "01020304";

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            //#
            //# SuperA turns output on. Put data return the output value.
            //#

            for (int i = 0; i < 8; i++) {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);

                Logger.detail(" ON Output " + i);
                a = superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_ON, 0);

                if (0 != a.compareTo(Door.SE_DOOR_STATUS_HIGH))
                    throw new TestException();

                a = s1.getMeasure(superA);
                if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                    throw new TestException();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
            //#
            //# SuperA turns output off. Put data returns the output value;
            //#
            for (int i = 0; i < Device.GPIO_NUMBER; i++) {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);

                Logger.detail(" OFF Output " + i);
                a = superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_OFF, 0);

                if (0 != a.compareTo(Door.SE_DOOR_STATUS_LOW))
                    throw new TestException();

                a = s1.getMeasure(superA);
                if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                    throw new TestException();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
            //#
            //# SuperA pulse output
            //#
            for (int i = 0; i < Device.GPIO_NUMBER; i++) {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);
                Logger.detail(" PULSE Output " + i);
                a = superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_PULSE, 1000);

                if (0 != a.compareTo(Door.SE_DOOR_STATUS_HIGH))
                    throw new TestException();

                a = s1.getMeasure(superA);
                if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                    throw new TestException();
                try {
                    Thread.sleep(1700);
                } catch (InterruptedException ignored) {
                }

                a = s1.getMeasure(superA);
                if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                    throw new TestException();
            }

            //#
            //# Object deletion
            //#
            s1.delete(superA);
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

