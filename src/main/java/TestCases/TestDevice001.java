package TestCases;
import com.sdk.*;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.testlog.*;

@SuppressWarnings("unused")
public class TestDevice001 {

    //    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestDevice01";
    public static final String deviceId = MainTest.TestMain.deviceId;

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

                testCase07();
                j++;

                testCase08();
                j++;

                testCase09();
                j++;


            }

        } catch (TestException | DiscoveryException e) {
            Logger.detail("TEST FAILURE ----->" + j);
            thisUnit.testCompleted(false, "failure at test case " + j);

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

    DESCRIPTION: Super Administrator can recover temperature and voltage

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        String pin;
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

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            //#
            //# SuperA can recover voltage of battery;
            //#
            int batteryVoltage = thisDevice.getBatteryVoltage(superA);
            Logger.detail(" Battery Voltage ==============================> " + batteryVoltage);

            //#
            //# SuperA can recover voltage of battery;
            //#
            double temperature = thisDevice.getMicroTemperature(superA);
            Logger.detail(" DeviceTemperature ==============================> " + temperature);

            //#
            //# SuperA can recover voltage of Vcc;
            //#
            double vcc = thisDevice.getVcc(superA);
            Logger.detail(" vcc ==============================> " + vcc);

            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException e)//| TestException e)
        {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Administrators and users can not get battery level and chip temperature

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 02";
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
            //# Admin can not get Battery Level
            //#
            // create the apdu object
            Apdu apdu = new Apdu(Apdu.GET_DATA_SENSOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_SENSOR_TYPE, Sensor.SENSOR_VBATT_TYPE);

            // set the expected result and send the command;              
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Get Battery";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not get Battery Level
            //#
            // create the apdu object
            apdu = new Apdu(Apdu.GET_DATA_SENSOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_SENSOR_TYPE, Sensor.SENSOR_VBATT_TYPE);

            // set the expected result and send the command;              
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Get Battery";
            command.requester = user;
            command.execute(apdu.toString(), expectedRes);

            //#
            //# Admin can not get Chip Temperature
            //#
            // create the apdu object
            apdu = new Apdu(Apdu.GET_DATA_SENSOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_SENSOR_TYPE, Sensor.SENSOR_CHIP_TEMPERATURE_TYPE);

            // set the expected result and send the command;              
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Get Temperature";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not get Chip Temperature
            //#
            // create the apdu object
            apdu = new Apdu(Apdu.GET_DATA_SENSOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_SENSOR_TYPE, Sensor.SENSOR_CHIP_TEMPERATURE_TYPE);

            // set the expected result and send the command;              
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Get Temperature";
            command.requester = user;
            command.execute(apdu.toString(), expectedRes);

            //#
            //# Object deletion
            //#
            user.delete(superA);
            admin.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
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

    DESCRIPTION: Get Battery data

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
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

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            //#
            //# SuperA can check the battery data;
            //#
            thisDevice.syncronizeSystemParameters(superA);

            String a = thisDevice.getBatteryRechargeActivity();
            if ((0 != a.compareTo(Device.BATTERY_ACTIVITY_RECHARGE_ONGOING)) && (0 != a.compareTo(Device.BATTERY_ACTIVITY_RECHARGE_ON_HOLD))) {
                throw new TestException();
            }

            a = thisDevice.getBatteryRechargeStatus();
            if (0 != a.compareTo(Device.BATTERY_RECHARGE_STATUS_ENABLED)) {
                throw new TestException();
            }

            a = thisDevice.getBatteryType();
            if (0 != a.compareTo(Device.BATTERY_TYPE_RECHARGABLE)) {
                throw new TestException();
            }
            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Administrators and users can not syncronyze device data

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
            //# Admin can not syncronyze Device data
            //#
            // create the apdu object
            Apdu apdu = new Apdu(Apdu.GET_DATA_SYNCHRONIZE_SYSTEM_DATA_APDU);

            // set the expected result and send the command;              
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Get Device Data";
            command.requester = admin;
            command.execute(apdu.toString(), expectedRes);
            //#
            //# User can not get Battery Level
            //#
            // create the apdu object
            apdu = new Apdu(Apdu.GET_DATA_SYNCHRONIZE_SYSTEM_DATA_APDU);

            // set the expected result and send the command;              
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "Get Device Data";
            command.requester = user;
            command.execute(apdu.toString(), expectedRes);


            //#
            //# Object deletion
            //#
            user.delete(superA);
            admin.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
     testCase05
     --------------------------------------------------------------------------
     AUTHOR:	PDI

     DESCRIPTION: Get MQTT data

     Security Level: None

     ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
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

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            //#
            //# SuperA can check the battery data;
            //#
            thisDevice.syncronizeSystemParameters(superA);

            String a = thisDevice.getMqttStatus();
            if (0 != a.compareTo(Device.MQTT_STATUS_ENABLED)) {
                throw new TestException();
            }
            
        /*    a = thisDevice.getMqttConnectionStatus();
            if (0 != a.compareTo(Device.MQTT_CONNECTION_STATUS_SUBSCRIBED)){
                throw new TestException();
            }*/

            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase06
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Enable/Disable Battery Recharge

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException {
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

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            //#
            //# SuperA can disable battery recharge
            //#
            thisDevice.syncronizeSystemParameters(superA);

            String a = thisDevice.getBatteryRechargeStatus();
            if (0 != a.compareTo(Device.BATTERY_RECHARGE_STATUS_ENABLED)) {
                throw new TestException();
            }

            thisDevice.setBatteryRechargeStatus(superA, Device.BATTERY_RECHARGE_STATUS_DISABLED, pin);

            thisDevice.syncronizeSystemParameters(superA);

            a = thisDevice.getBatteryRechargeStatus();
            if (0 != a.compareTo(Device.BATTERY_RECHARGE_STATUS_DISABLED)) {
                throw new TestException();
            }
            //#
            //# SuperA can enable battery recharge
            //#

            thisDevice.setBatteryRechargeStatus(superA, Device.BATTERY_RECHARGE_STATUS_ENABLED, pin);

            thisDevice.syncronizeSystemParameters(superA);

            a = thisDevice.getBatteryRechargeStatus();
            if (0 != a.compareTo(Device.BATTERY_RECHARGE_STATUS_ENABLED)) {
                throw new TestException();
            }

            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase07
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Enable/Disable MQTT

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException {
        String pin;
        String testCase = testBatch + "/" + "Test Case 07";
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

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            //#
            //# SuperA can disable MQTT
            //#
            thisDevice.syncronizeSystemParameters(superA);

            String a = thisDevice.getMqttStatus();
            if (0 != a.compareTo(Device.MQTT_STATUS_ENABLED)) {
                throw new TestException();
            }

            thisDevice.setMqttStatus(superA, Device.MQTT_STATUS_DISABLED, pin);

            thisDevice.syncronizeSystemParameters(superA);

            a = thisDevice.getMqttStatus();
            if (0 != a.compareTo(Device.MQTT_STATUS_DISABLED)) {
                throw new TestException();
            }
            //#
            //# SuperA can enable MQTT
            //#

            thisDevice.setMqttStatus(superA, Device.MQTT_STATUS_ENABLED, pin);

            thisDevice.syncronizeSystemParameters(superA);

            a = thisDevice.getMqttStatus();
            if (0 != a.compareTo(Device.MQTT_STATUS_ENABLED)) {
                throw new TestException();
            }

            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase08
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: it is not possible to update system data in case of wrong PIN.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase08() throws TestException {
        Command command = new Command();
        String pin;
        String testCase = testBatch + "/" + "Test Case 08";
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
            //# It is not possible update battery recharge status in case of Wrong PIN
            //#
            // create the apdu object
            Apdu apdu = new Apdu(Apdu.PUT_DATA_SET_SYSTEM_DATA_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01010101");
            apdu.addTlv(Apdu.DATA_BATTERY_DATA, Device.BATTERY_RECHARGE_STATUS_DISABLED);

            // set the expected result and send the command;              
            String expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "update Battery recharge status";
            command.requester = superA;
            command.execute(apdu.toString(), expectedRes);

            thisDevice.syncronizeSystemParameters(superA);

            String a = thisDevice.getBatteryRechargeStatus();
            if (0 != a.compareTo(Device.BATTERY_RECHARGE_STATUS_ENABLED)) {
                throw new TestException();
            }

            //#
            //# It is not possible to change the MQTT status in case of wrong PN
            //#
            // create the apdu object
            apdu = new Apdu(Apdu.PUT_DATA_SET_SYSTEM_DATA_APDU);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01010101");
            apdu.addTlv(Apdu.DATA_MQTT_STATUS, Device.MQTT_STATUS_DISABLED);

            // set the expected result and send the command;              
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            command.description = "update MQTT Status";
            command.requester = superA;
            command.execute(apdu.toString(), expectedRes);

            thisDevice.syncronizeSystemParameters(superA);

            a = thisDevice.getMqttStatus();
            if (0 != a.compareTo(Device.MQTT_STATUS_ENABLED)) {
                throw new TestException();
            }


            //#
            //# Object deletion
            //#
            user.delete(superA);
            admin.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
     testCase08
     --------------------------------------------------------------------------
     AUTHOR:	PDI

     DESCRIPTION: Change Device Nick Name

     Security Level: None

     ------------------------------------------------------------------------------*/
    public static void testCase09() throws TestException {
        String pin;
        String testCase = testBatch + "/" + "Test Case 09";
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

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            //#
            //# It is possible to change device Nickname
            //#
            thisDevice.ping();

            thisDevice.setName(superA, "Babuino", pin);

            thisDevice.ping();

            if (0 != "Babuino".compareTo(thisDevice.getDeviceName())) {
                throw new TestException();
            }

            //#
            //# Object deletion
            //#
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }
}

