package com.sdk;
///********************************************************************************
//
// *							PDI COPYRIGHT 2015
// * HEADER
// * $Workfile: Test0003.java $
// * $Date: 11/09/2015 13:21 $
// * $Author:  $
// * $Revision:  $
// * DESCRIPTION:	Test Sensors
// * Project: ACCESS CONTROL
// ********************************************************************************/


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.testlog.*;


@SuppressWarnings({"deprecation", "unused"})
public class TestSensors001 {
    //    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestSensors001";
    public static final String deviceId = TestMain.deviceId;

    //    /********************************
//     PUBLIC Methods
//     ********************************/
    public static User superA;
    public static Device thisDevice;

    private static final TestUnit thisUnit = new TestUnit();


    /*----------------------------------------------------------------------------
    testCase01
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: It Test the create command.

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
            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);

            // test list
            for (int u = 0; u < 1; u++) {

                // test list
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

                // testCase08(); // Only if Temperature and humidity sensor is on the board
                j++;

                testCase09();
                j++;

                //   testCase10(); Only if relay are connected with the digital input
                j++;

                testCase11();
                j++;

                testCase12();
                j++;

                testCase13();
                j++;

                testCase14(); //Need oscilloscope
                j++;

                //testCase15(); // It needs input set at right voltage;
                j++;

                testCase01();
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

    DESCRIPTION: SuperA creates a sensor object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        String a, name;
        Sensor sensor;
        String testCase = testBatch + " /" + "Test Case 01";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // object personalized
            //superA.updateKey("admin");
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);
            //#
            //# Super A creates a door object. Initial values are according to specs.
            //#
            // Create Sensor
            Logger.detail("------------ Create Sensor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);

            sensor.syncroFields(superA);

            // Check SD ID
            Logger.detail("------------ Check Security Domain ID ------------");
            a = sensor.getSecurityDomain();
            if (0 != a.compareTo(superA.getSecurityDomain()))
                throw new ObjectException();

            // Check Status
            Logger.detail("------------ Check Status ------------");
            a = sensor.getStatus();
            if (0 != a.compareTo(Sensor.SENSOR_STATUS_DISABLED)) {
                throw new ObjectException();
            }

            Logger.detail("------------ Check that name is the default one ------------");
            a = sensor.getName();
            name = sensor.getObjectId();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            // GPIOiD
            Logger.detail("------------ Check Initial gpioID ------------");
            a = sensor.getGpioId();
            if (0 != a.compareTo(Sensor.SE_SENSOR_VOID_GPIOID))
                throw new ObjectException();

            // Check Event
            Logger.detail("------------ Event ------------");
            a = sensor.getEvent();
            if (0 != a.compareTo("00"))
                throw new ObjectException();

            // type
            Logger.detail("------------ Check type Value ------------");
            a = sensor.getType();
            if (0 != a.compareTo(Sensor.SENSOR_TYPE_GPIO))
                throw new ObjectException();

            // lowTreshold
            Logger.detail("------------ Check Initial LowThreshold------------");
            a = sensor.getLowThreshold();
            if (0 != a.compareTo("00000000"))
                throw new ObjectException();

            // highTreshold
            Logger.detail("------------ highThreshold------------");
            a = sensor.getHighThreshold();
            if (0 != a.compareTo("00000000")) {
                throw new ObjectException();
            }

            // Device Extension Object Id
            Logger.detail("------------ DeviceExtensionId------------");
            a = sensor.getDeviceExtensionId();
            if (0 != a.compareTo(DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                throw new ObjectException();
            //#
            //# Object deletion
            //#
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();

        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Only SuperA can create a Sensor.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        String expectedRes;
        Apdu apduObject;
        Command c = new Command();


        String testCase = testBatch + " /" + "Test Case 02";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // object personalized
            superA.updateKey("admin");
            // instantiate a local User object for the SUPER-A
            User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");

            // object personalized
            admin.updateKey("panda");

            // instantiate a local User object for the SUPER-A
            User user = new User(superA, User.USER_ROLE_USER, "zaffata");

            // object personalized
            user.updateKey("penda");
            //#
            //# User can not create a Sensor
            //#
            Logger.detail("------------ User can not create a Sensor ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to create a sensor
            apduObject = new Apdu(Apdu.CREATE_SE_SENSOR_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_SENSOR_TYPE, Sensor.SENSOR_TYPE_GPIO);

            // send command
            c.description = "Create Sensor";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
            //#
            //# Administrator can not create a sensor
            //#
            Logger.detail("------------ Administrator can not create a sensor ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to create a sensor
            apduObject = new Apdu(Apdu.CREATE_SE_SENSOR_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_SENSOR_TYPE, Sensor.SENSOR_TYPE_GPIO);

            // send command
            c.description = "Create Door";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");


    }

    /*----------------------------------------------------------------------------
testCase03
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Read Policy of sensor object.
               Readeable Fields can be read by all user object despite their role

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        String a;
        Sensor sensor;
        String testCase = testBatch + " /" + "Test Case 03";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey("admin");

            // instantiate a local User object for Admin
            User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
            admin.updateKey("panda");

            // instantiate a local User object for Admin
            User admin1 = new User(superA, User.USER_ROLE_ADMIN, "zebra");
            admin1.updateKey("panda");

            // instantiate a local User
            User user = new User(superA, User.USER_ROLE_USER, "pinco");
            user.updateKey("pallino");

            // instantiate a local User
            User user1 = new User(superA, User.USER_ROLE_USER, "pinco");
            user1.updateKey("pallino");

            // Create Sensor
            Logger.detail("------------ Create Sensor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);

            //#
            //# Super A can read the status field of a Sensor
            //#
            Logger.detail("-- Super A can read the status field of Sensor  --");

            sensor.syncroFields(superA);
            // Check Status
            Logger.detail("------------ Check Status ------------");
            a = sensor.getStatus();
            if (0 != a.compareTo(Sensor.SENSOR_STATUS_DISABLED)) {
                throw new ObjectException();
            }

            //#
            //# Administrator can read the status field of a Sensor
            //#
            Logger.detail("-- Administrator can read the status field of a Sensor  --");

            sensor.syncroFields(admin);

            // Check Status
            Logger.detail("------------ Check Status ------------");
            a = sensor.getStatus();
            if (0 != a.compareTo(Sensor.SENSOR_STATUS_DISABLED)) {
                throw new ObjectException();
            }
            //#
            //# User can read the status field of a Door
            //#
            Logger.detail("-- User can read the status field of a Sensor  --");

            sensor.syncroFields(user1);
            // Check Status
            Logger.detail("------------ Check Status ------------");
            a = sensor.getStatus();
            if (0 != a.compareTo(Sensor.SENSOR_STATUS_DISABLED)) {
                throw new ObjectException();
            }
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            admin1.delete(superA);
            user.delete(superA);
            user1.delete(superA);
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can updated the fileds of a Sensor object. User and Admin not.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
        String a, expectedRes;
        Sensor sensor;
        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 04";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");

            // Create Sensor
            Logger.detail("------------ Create Sensor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);

            //#
            //# Super A can updated the field of a Door Object
            //#
            // Status
            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.update(superA);
            sensor.syncroFields(superA);

            Logger.detail("------------ Check Status ------------");
            a = sensor.getStatus();
            if (0 != a.compareTo(Sensor.SENSOR_STATUS_ENABLED)) {
                throw new ObjectException();
            }

            // type
            sensor.setType(Sensor.SENSOR_TYPE_TEMPERATURE_DS3231_TYPE);
            sensor.update(superA);
            sensor.syncroFields(superA);


            Logger.detail("------------ Check Type ------------");
            a = sensor.getType();
            if (0 != a.compareTo(Sensor.SENSOR_TYPE_TEMPERATURE_DS3231_TYPE)) {
                throw new ObjectException();
            }

            // GPIO
            sensor.setGpioId("09");
            sensor.update(superA);
            sensor.syncroFields(superA);

            Logger.detail("------------ Check GPIO ------------");
            a = sensor.getGpioId();
            if (0 != a.compareTo("09")) {
                throw new ObjectException();
            }

            // Event
            sensor.setEvent("01");
            sensor.update(superA);
            sensor.syncroFields(superA);


            Logger.detail("------------ Check Event ------------");
            a = sensor.getEvent();
            if (0 != a.compareTo("01")) {
                throw new ObjectException();
            }

            // low Threshold
            sensor.setLowThreshold("01020304");
            sensor.update(superA);
            sensor.syncroFields(superA);


            Logger.detail("------------ Check LowThreshold ------------");
            a = sensor.getLowThreshold();
            if (0 != a.compareTo("01020304")) {
                throw new ObjectException();
            }

            // high Treshold
            sensor.setHighThreshold("01020304");
            sensor.update(superA);
            sensor.syncroFields(superA);


            Logger.detail("------------ Check HighThreshold ------------");
            a = sensor.getHighThreshold();
            if (0 != a.compareTo("01020304")) {
                throw new ObjectException();
            }

            // name
            sensor.setName("313233343436373839303A3B3C3D3E3F");
            sensor.update(superA);
            sensor.syncroFields(superA);

            Logger.detail("------------ Check Name ------------");
            a = sensor.getName();
            if (0 != a.compareTo("313233343436373839303A3B3C3D3E3F")) {
                throw new CommandErrorException();
            }


            // Device Extension Identifier
            superA.syncroFields(superA);
            RemoteAuthenticator r = superA.getRemoteAuthenticatorObject();
            r.syncroFields(superA);
            DeviceExtender deviceEstender = new DeviceExtender(superA, "pepe");

            sensor.setDeviceExtensionObjectId(deviceEstender.getObjectId());
            sensor.update(superA);
            sensor.syncroFields(superA);

            // Check Device Extension Identifier
            Logger.detail("------------ Device Extension Identifier ------------");
            a = sensor.getDeviceExtensionId();
            if (0 != a.compareTo(deviceEstender.getObjectId()))
                throw new ObjectException();

            //#
            //# Administrator can not update sensor
            //#
            Logger.detail("------------ Administrator can not update sensor ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the door
            Apdu apdu = new Apdu(Apdu.UPDATE_SENSOR_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, sensor.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_GPIO, "02");
            // send command
            c.description = "Update Sensor";
            c.requester = admin;
            c.execute(apdu.toString(), expectedRes);

            //#
            //# user can not update door
            //#

            Logger.detail("------------ user can not update Sensor ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the door
            apdu = new Apdu(Apdu.UPDATE_SENSOR_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, sensor.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_GPIO, "02");
            // send command
            c.description = "Update Sensor";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            user.delete(superA);
            sensor.delete(superA);
            deviceEstender.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase05
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can delete a Sensor. User and administrators can not delete a Sensor.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
        String expectedRes;
        Sensor sensor;
        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 05";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");

            //#
            //# Super A can delete a sensor
            //#
            Logger.detail("------------ Super A can delete a Sensor ------------");

            // Create sensor
            Logger.detail("------------ Create Semsor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);

            // Update Initial Configuration.
            sensor.setGpioId("03");

            String objId = sensor.getObjectId();

            sensor.delete(superA);

            // check that the Sensor has been deleted
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the door
            Apdu apdu = new Apdu(Apdu.UPDATE_SENSOR_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, objId);
            apdu.addTlv(Atlv.DATA_TAG_GPIO, "02");
            // send command
            c.description = "Update Sensor";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);

            //#
            //# Administrator can not delete a door
            //#
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);

            Logger.detail("------------ Administrator can not delete a Sensor ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the door
            apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, sensor.getObjectId());
            // send command
            c.description = "Delete Sensor";
            c.requester = admin;
            c.execute(apdu.toString(), expectedRes);

            //#
            //# user can not delete a sensor
            //#

            Logger.detail("------------ user can not delete a sensor ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the door
            apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, sensor.getObjectId());

            // send command
            c.description = "Delete sensor";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            user.delete(superA);
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase06
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check sensors from 0 to 7

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException {
        Sensor sensor;
        Door door;
        String telemetry;
        String testCode = testBatch + "/" + "Test Case 06";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");

            // Create a sensor and assign it to GPIO 0
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.setGpioId("00");
            sensor.update(superA);

            // Create a door and assign it to GPIO 5
            door = new Door(superA);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setGpioId("00");
            door.update(superA);
            //#
            //# Super A can get a measure from a sensor at GPIO 0
            //#
            for (int i = 0; i < 5; i++) {
                door.setGpioId(i);
                door.update(superA);

                sensor.setGpioId(i);
                sensor.update(superA);

                Logger.detail("------------ Super A can get a measure from the sensor ------------");

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                telemetry = sensor.getMeasure(superA);

                // check telemetry
                if (!(telemetry.equals(Sensor.SENSOR_VALUE_ON))) {
                    throw new TestException();
                }
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                telemetry = sensor.getMeasure(superA);

                // check telemetry
                if (!(telemetry.equals(Sensor.SENSOR_VALUE_OFF))) {
                    throw new TestException();
                }
            }
            //#
            //# Super A can get a measure from a sensor after syncronization;
            //#
            for (int i = 0; i < 5; i++) {
                door.setGpioId(i);
                door.update(superA);

                sensor.setGpioId(i);
                sensor.update(superA);

                Logger.detail("------------ Super A can get a measure from the sensor ------------");

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                sensor.syncroFields(superA);
                telemetry = sensor.getLastMeasure();

                // check telemetry
                if (!(telemetry.equals(Sensor.SENSOR_VALUE_ON))) {
                    throw new TestException();
                }
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                sensor.syncroFields(superA);
                telemetry = sensor.getLastMeasure();

                // check telemetry
                if (!(telemetry.equals(Sensor.SENSOR_VALUE_OFF))) {
                    throw new TestException();
                }
            }
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | TestException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase07
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check that Create and delete sensor updates the log.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException {
        Sensor sensor;
        int index;
        LogEntry le;
        String sensorId, requestorId;
        String testCode = testBatch + "/" + "Test Case 07";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");

            //#
            //# Log is updated when a sensor is created
            //#
            Logger.detail("------------ Log is updated when a sensor is created ------------");

            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor.syncroFields(superA);

            index = DeviceLogger.getLastEntryIndex(superA);

            requestorId = superA.getObjectId();
            sensorId = sensor.getObjectId();

            le = DeviceLogger.getEntry(superA, index);

            // check log entry
            if (!(requestorId.equals(le.requesterId)) ||
                    (!sensorId.equals(le.objectId)) ||
                    (!DeviceLogger.SE_LOG_APDU_CREATE_EVENT.equals(le.event)) ||
                    (!"9000".equals(le.result))) {
                throw new TestException();
            }
            //#
            //# Log is updated when a door is deleted;
            //#
            Logger.detail("------------ Log is updated when a door is deleted; ------------");

            sensor.delete(superA);

            index = (index + 1) & 0xFFFF;

            le = DeviceLogger.getEntry(superA, index);

            // check log entry
            if (!(requestorId.equals(le.requesterId)) ||
                    (!sensorId.equals(le.objectId)) ||
                    (!DeviceLogger.SE_LOG_APDU_DELETE_EVENT.equals(le.event)) ||
                    (!"9000".equals(le.result))) {
                throw new TestException();
            }
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | TestException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase08
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: get temperature and humidity;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase08() throws TestException {
        Sensor sensor, sensor1, sensor2;
        String telemetry;
        String testCode = testBatch + "/" + "Test Case 08";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");

            // Create a sensor
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor.syncroFields(superA);
            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.setType(Sensor.SENSOR_TYPE_TEMPERATURE_DS3231_TYPE);
            sensor.update(superA);

            // Create a sensor
            sensor1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor1.syncroFields(superA);
            sensor1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor1.setType(Sensor.SENSOR_TYPE_TEMPERATURE_DS3231_TYPE);
            sensor1.update(superA);

            // Create a sensor
            sensor2 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor2.syncroFields(superA);
            sensor2.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor2.setType(Sensor.SENSOR_TYPE_TEMPERATURE_DS3231_TYPE);
            sensor2.update(superA);

            //#
            //# Super A can get a measure from a Temperature sensor based on HTS221
            //#
            Logger.detail("------------ Super A can get the temperature from the sensor HTS221------------");

            sensor1.setType(Sensor.SENSOR_TEMPERATURE_HTS221_TYPE);
            sensor1.update(superA);

            telemetry = sensor1.getMeasure(superA);

            System.out.println(">>>>>>> Value: " + telemetry);


            //#
            //# Super A can get a measure from a Humidity sensor based on HTS221
            //#
            Logger.detail("------------ Super A can get the Humidity from the sensor HTS221------------");

            sensor2.setType(Sensor.SENSOR_HUMIDITY_HTS221_TYPE);
            sensor2.update(superA);
            telemetry = sensor2.getMeasure(superA);

            System.out.println(">>>>>>>> Value: " + telemetry);

            //#
            //# Object deletion
            //#
            admin.delete(superA);
            user.delete(superA);
            sensor.delete(superA);
            sensor1.delete(superA);
            sensor2.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase09
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: If sensor is disabled it is not possible to perform telemetry.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase09() throws TestException {
        String expectedRes, telemetry;
        Sensor sensor;
        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 09";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");

            // Create a sensor and assign it to GPIO 9
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor.syncroFields(superA);
            sensor.setType(Sensor.SENSOR_TEMPERATURE_HTS221_TYPE);
            sensor.update(superA);

            //#
            //# Sensor is disabled. It is not possible to receive data.
            //#
            Logger.detail("------------ Sensor is disabled. It is not possible to receive data. ------------");

            // check that the Sensor has been deleted
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to telemetry
            Apdu apdu = new Apdu(Apdu.TELEMETRY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, sensor.getObjectId());
            // send command
            c.description = "Telemetry";
            c.requester = superA;
            c.execute(apdu.toString(), expectedRes);

            Logger.detail("------------ Sensor is enabled. Telemetry is working. ------------");
            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.update(superA);
            telemetry = sensor.getMeasure(superA);

            System.out.println("Value: " + telemetry);

            //#
            //# Object deletion
            //#
            admin.delete(superA);
            user.delete(superA);
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase10
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: It test input 1,2,3,4,5. Input shall be connected to relevant output.
        In 0 connected to out 0; an so on.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase10() throws TestException {
        String a;
        Sensor sensor;
        Door door;

        String testCode = testBatch + "/" + "Test Case 10";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");

            door = new Door(superA);
            door.syncroFields(superA);
            door.setGpioId(0);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            // Create a sensor and assign it to GPIO 9
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor.syncroFields(superA);
            sensor.setType(Sensor.SENSOR_TYPE_GPIO);
            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.setGpioId(0);
            sensor.update(superA);

            //#
            //# Door 0 triggered; Sensor 0 on;
            //#

            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_OFF))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_ON))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_OFF))) {
                throw new TestException();
            }

            //#
            //# Door 1 triggered; Sensor 1 on;
            //#

            door.setGpioId(1);
            door.update(superA);

            sensor.setGpioId(10);
            sensor.update(superA);

            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_OFF))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_ON))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_OFF))) {
                throw new TestException();
            }

            //#
            //# Door 2 triggered; Sensor 2 on;
            //#

            door.setGpioId(2);
            door.update(superA);

            sensor.setGpioId(11);
            sensor.update(superA);

            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_OFF))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_ON))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_OFF))) {
                throw new TestException();
            }

            //#
            //# Door 3 triggered; Sensor 3 on;
            //#

            door.setGpioId(3);
            door.update(superA);

            sensor.setGpioId(12);
            sensor.update(superA);

            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_OFF))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_ON))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

            a = sensor.getMeasure(superA);
            // check telemetry
            if (!(a.equals(Sensor.SENSOR_VALUE_OFF))) {
                throw new TestException();
            }

            //#
            //# Check ADC1 input @ 5 Volt
            //#

            door.setGpioId(3);
            door.update(superA);

            sensor.setType(Sensor.SENSOR_ANALOG_TYPE);
            sensor.update(superA);
            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            a = sensor.getMeasure(superA);

            System.out.println(".............................> Vin Voltage [mV] " + ((float) (Integer.parseInt(a, 16)) / 1000));

            //#
            //# Device Temperature
            //#

            door.setGpioId(3);
            door.update(superA);

            sensor.setType(Sensor.SENSOR_CHIP_TEMPERATURE_TYPE);
            sensor.update(superA);

            a = sensor.getMeasure(superA);

            System.out.println(".............................> Temperature [Celsius]" + ((float) (Integer.parseInt(a, 16)) / 1000));


            //#
            //# Check Vbatt
            //#

            door.setGpioId(3);
            door.update(superA);

            sensor.setType(Sensor.SENSOR_VBATT_TYPE);
            sensor.update(superA);

            a = sensor.getMeasure(superA);

            System.out.println(".............................> V battery [mv] " + ((float) (Integer.parseInt(a, 16)) / 1000));


            //#
            //# Object deletion
            //#
            admin.delete(superA);
            user.delete(superA);
            sensor.delete(superA);
            door.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase11
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA creates an Analog sensor object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase11() throws TestException {
        String a, name;
        Sensor sensor;
        String testCase = testBatch + " /" + "Test Case 11";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // object personalized
            //superA.updateKey("admin");
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);
            //#
            //# Super A creates a door object. Initial values are according to specs.
            //#
            // Create Sensor
            Logger.detail("------------ Create Sensor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);

            sensor.syncroFields(superA);

            // Check SD ID
            Logger.detail("------------ Check Security Domain ID ------------");
            a = sensor.getSecurityDomain();
            if (0 != a.compareTo(superA.getSecurityDomain()))
                throw new ObjectException();

            // Check Status
            Logger.detail("------------ Check Status ------------");
            a = sensor.getStatus();
            if (0 != a.compareTo(Sensor.SENSOR_STATUS_DISABLED)) {
                throw new ObjectException();
            }

            Logger.detail("------------ Check that name is the default one ------------");
            a = sensor.getName();
            name = sensor.getObjectId();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            // GPIOiD
            Logger.detail("------------ Check Initial gpioID ------------");
            a = sensor.getGpioId();
            if (0 != a.compareTo(Sensor.SE_SENSOR_VOID_GPIOID))
                throw new ObjectException();

            // Check Event
            Logger.detail("------------ Event ------------");
            a = sensor.getEvent();
            if (0 != a.compareTo("00"))
                throw new ObjectException();

            // type
            Logger.detail("------------ Check type Value ------------");
            a = sensor.getType();
            if (0 != a.compareTo(Sensor.SENSOR_ANALOG_TYPE))
                throw new ObjectException();

            // lowTreshold
            Logger.detail("------------ Check Initial LowThreshold------------");
            a = sensor.getLowThreshold();
            if (0 != a.compareTo("00000000"))
                throw new ObjectException();

            // highTreshold
            Logger.detail("------------ highThreshold------------");
            a = sensor.getHighThreshold();
            if (0 != a.compareTo("00000000")) {
                throw new ObjectException();
            }

            // Device Extension Object Id
            Logger.detail("------------ DeviceExtensionId------------");
            a = sensor.getDeviceExtensionId();
            if (0 != a.compareTo(DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                throw new ObjectException();
            //#
            //# Object deletion
            //#
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");


    }

    /*----------------------------------------------------------------------------
    testCase12
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA creates an VBatt object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase12() throws TestException {
        String a, name;
        Sensor sensor;
        String testCase = testBatch + " /" + "Test Case 12";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // object personalized
            //superA.updateKey("admin");
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);
            //#
            //# Super A creates a door object. Initial values are according to specs.
            //#
            // Create Sensor
            Logger.detail("------------ Create Sensor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_VBATT_TYPE, true);

            sensor.syncroFields(superA);

            // Check SD ID
            Logger.detail("------------ Check Security Domain ID ------------");
            a = sensor.getSecurityDomain();
            if (0 != a.compareTo(superA.getSecurityDomain()))
                throw new ObjectException();

            // Check Status
            Logger.detail("------------ Check Status ------------");
            a = sensor.getStatus();
            if (0 != a.compareTo(Sensor.SENSOR_STATUS_DISABLED)) {
                throw new ObjectException();
            }

            Logger.detail("------------ Check that name is the default one ------------");
            a = sensor.getName();
            name = sensor.getObjectId();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            // GPIOiD
            Logger.detail("------------ Check Initial gpioID ------------");
            a = sensor.getGpioId();
            if (0 != a.compareTo(Sensor.SE_SENSOR_VOID_GPIOID))
                throw new ObjectException();

            // Check Event
            Logger.detail("------------ Event ------------");
            a = sensor.getEvent();
            if (0 != a.compareTo("00"))
                throw new ObjectException();

            // type
            Logger.detail("------------ Check type Value ------------");
            a = sensor.getType();
            if (0 != a.compareTo(Sensor.SENSOR_VBATT_TYPE))
                throw new ObjectException();

            // lowTreshold
            Logger.detail("------------ Check Initial LowThreshold------------");
            a = sensor.getLowThreshold();
            if (0 != a.compareTo("00000000"))
                throw new ObjectException();

            // highTreshold
            Logger.detail("------------ highThreshold------------");
            a = sensor.getHighThreshold();
            if (0 != a.compareTo("00000000")) {
                throw new ObjectException();
            }

            // Device Extension Object Id
            Logger.detail("------------ DeviceExtensionId------------");
            a = sensor.getDeviceExtensionId();
            if (0 != a.compareTo(DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                throw new ObjectException();
            //#
            //# Object deletion
            //#
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase13
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA creates an Device Temperature object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase13() throws TestException {
        String a, name;
        Sensor sensor;
        String testCase = testBatch + " /" + "Test Case 13";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------

        try {
            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // object personalized
            //superA.updateKey("admin");
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);
            //#
            //# Super A creates a door object. Initial values are according to specs.
            //#
            // Create Sensor
            Logger.detail("------------ Create Sensor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_CHIP_TEMPERATURE_TYPE, true);

            sensor.syncroFields(superA);

            // Check SD ID
            Logger.detail("------------ Check Security Domain ID ------------");
            a = sensor.getSecurityDomain();
            if (0 != a.compareTo(superA.getSecurityDomain()))
                throw new ObjectException();

            // Check Status
            Logger.detail("------------ Check Status ------------");
            a = sensor.getStatus();
            if (0 != a.compareTo(Sensor.SENSOR_STATUS_DISABLED)) {
                throw new ObjectException();
            }

            Logger.detail("------------ Check that name is the default one ------------");
            a = sensor.getName();
            name = sensor.getObjectId();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            // GPIOiD
            Logger.detail("------------ Check Initial gpioID ------------");
            a = sensor.getGpioId();
            if (0 != a.compareTo(Sensor.SE_SENSOR_VOID_GPIOID))
                throw new ObjectException();

            // Check Event
            Logger.detail("------------ Event ------------");
            a = sensor.getEvent();
            if (0 != a.compareTo("00"))
                throw new ObjectException();

            // type
            Logger.detail("------------ Check type Value ------------");
            a = sensor.getType();
            if (0 != a.compareTo(Sensor.SENSOR_CHIP_TEMPERATURE_TYPE))
                throw new ObjectException();

            // lowTreshold
            Logger.detail("------------ Check Initial LowThreshold------------");
            a = sensor.getLowThreshold();
            if (0 != a.compareTo("00000000"))
                throw new ObjectException();

            // highTreshold
            Logger.detail("------------ highThreshold------------");
            a = sensor.getHighThreshold();
            if (0 != a.compareTo("00000000")) {
                throw new ObjectException();
            }

            // Device Extension Object Id
            Logger.detail("------------ DeviceExtensionId------------");
            a = sensor.getDeviceExtensionId();
            if (0 != a.compareTo(DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                throw new ObjectException();
            //#
            //# Object deletion
            //#
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase11
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test of non inverting Shmidt Trigger. Test need oscilloscope

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase14() throws TestException {
        String pin;
        Sensor sensor;
        String testCase = testBatch + " /" + "Test Case 14";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            pin = "01020304";
            // launch a ping
            thisDevice.ping();

            // object personalized
            //superA.updateKey("admin");
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);
            superA.syncroFields(superA);

            // Create Sensor
            Logger.detail("------------ Create Sensor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);
            sensor.setHighThreshold("00001388");    // 5V
            sensor.setLowThreshold("000003E8");     // 1V
            sensor.update(superA);
            sensor.syncroFields(superA);

            DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
            dfb.setInputId(sensor.getObjectId(), 0);
            dfb.setGpio("02");
            dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_IDENTITY);
            dfb.update(superA);

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }
            //#
            //# Schmitt trigger sensor works properly. Check on the oscilloscope
            //#

            // Create Sensor
            Logger.detail("------------ Set Trhesholds ------------");
            sensor.setHighThreshold(8000);
            sensor.setLowThreshold(6000);
            sensor.update(superA);
            // wait;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
            // Create Sensor
            Logger.detail("------------ Set Trhesholds ------------");
            sensor.setHighThreshold(4000);
            sensor.setLowThreshold(4000);
            sensor.update(superA);
            // wait;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }


            //#
            //# Object deletion
            //#
            sensor.delete(superA);
            dfb.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase15
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Input 0,1,2 are set at 0
                     Input 2 is open
                     Input 3 and 4 are set to VCC.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase15() throws TestException {
        String a;
        Sensor sensor;
        String testCase = testBatch + " /" + "Test Case 15";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // object personalized
            //superA.updateKey("admin");
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);

            // Create Sensors analog with threshold
            Logger.detail("------------ Create Sensor ------------");
            sensor = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);

            sensor.syncroFields(superA);

            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.setLowThreshold(500);
            sensor.setHighThreshold(3800);
            sensor.update(superA);

            // Create Sensors analog
            Logger.detail("------------ Create Sensor Voltage------------");
            Sensor sensorVoltage = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);

            sensorVoltage.syncroFields(superA);
            sensorVoltage.setGpioId(0);
            sensorVoltage.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensorVoltage.update(superA);


//#
//# Input 0 have a resistor of 3300 ohm; V measured is around 1.24V
//#

            // Get the sensor input
            Logger.detail("------------ Get the sensor input------------");

            sensorVoltage.syncroFields(superA);
            a = sensorVoltage.getMeasure(superA);

            int v = Integer.parseInt(a, 16);
            Logger.detail("Measured voltage in mv is: " + v);
            // resistence in input of
            if ((v < 1040) || (v > 1440)) {
                throw new ObjectException();
            }

//#
//# Input 0 have a resistor of 3300 ohm
//#

            // Get the sensor input
            Logger.detail("------------ Get the sensor input------------");

            sensorVoltage.setType(Sensor.SENSOR_OHM_TYPE);
            sensorVoltage.update(superA);

            sensorVoltage.syncroFields(superA);
            a = sensorVoltage.getMeasure(superA);

            v = Integer.parseInt(a, 16);
            Logger.detail("Measured Resistence in ohm: " + v);

            // resistence in input of
            if ((v > 3700) || (v < 2900)) {
                throw new ObjectException();
            }


//#
//# Input 1, and 2 are set at 0
//#

            // Get the sensor input
            Logger.detail("------------ Get the sensor input------------");

            for (int i = 1; i < 3; i++) {
                sensor.setGpioId(i);

                sensor.update(superA);

                a = sensor.getMeasure(superA);

                if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF)) {
                    throw new ObjectException();
                }
            }
//#
//# Input 3, 4, and 5 are set at 12V
//#

            // Get the sensor input
            Logger.detail("------------ Get the sensor input------------");

            for (int i = 3; i < 6; i++) {
                sensor.setGpioId(i);

                sensor.update(superA);

                a = sensor.getMeasure(superA);

                if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON)) {
                    throw new ObjectException();
                }
            }
            //#
            //# Object deletion
            //#
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }
}

