package TestCases;
import com.sdk.*;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

@SuppressWarnings({"deprecation", "unused"})
public class TestUserMqtt001 {

//    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestUserMQTT001";
    public static final String deviceId = MainTest.TestMain.deviceId;
    public static final String clientId = "3333333333";

    //public static final String hostName =  "54.76.137.235"; //mqtt cloud
    // public static final int port =  18223; // mqtt cloud

    public static final String HOSTNAME_TESTING = "80.211.32.249"; //mqtt cloud
    public static final String HOSTNAME_STAGING = "80.211.35.177"; //mqtt cloud
    public static final String hostName = HOSTNAME_STAGING; //mqtt cloud
    public static final int port = 1883;

    public static final int RESET_DELAY = 40000;

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
        try {

            ConnectionDetailsMqtt connectionDetails = new ConnectionDetailsMqtt();

            connectionDetails.setBearer(ConnectionDetailsMqtt.BEARER_MQTT_OVER_ETHERNET);

            connectionDetails.setClientId(clientId);

            connectionDetails.setHostName(hostName);
            connectionDetails.setPort(port);

            connectionDetails.setKeepAliveTime(60000);

            connectionDetails.setPassword("111");
            connectionDetails.setUserName(clientId);

            connectionDetails.setDeviceId(deviceId);

            thisDevice = new Device();
            thisDevice.setConnectionDetails(connectionDetails);
            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);

            thisDevice.startCommandSession();

            thisUnit.setDevice(thisDevice);

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

                //thisDevice.completeCommandSession();
            }


        } catch (TestException | IOException e) {
            Logger.detail("TEST FAILURE ----->" + j);
            thisUnit.testCompleted(false, "failure at test case " + j);

            Assertions.fail("TEST FAILURE ----->" + j);
            //return false;
        } finally {
            try {
                thisDevice.completeCommandSession();
            } catch (IOException ignored) {

            }
        }
        thisUnit.testCompleted(true, "success!");

        Logger.detail("OK");
    }

    /*----------------------------------------------------------------------------
testCase01
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Super-A creates an administrator object. The initial fields are set as expected;

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        String a, name;
        String testCase = testBatch + "/" + "Test Case 01";

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
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            //#
            //# Creation of an Administrator by the Super A
            //# Result: OK
            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "initialAdmin");
            // object personalized
            //for(int i=0; i<1000; i++)
            admin.updateKey("admin");

            admin.syncroFields(admin);

            // Role is administrator
            Logger.detail("------------ Check Role ------------");
            a = admin.getRole();
            if (0 != a.compareTo(User.USER_ROLE_ADMIN))
                throw new ObjectException();

            // Status is active
            Logger.detail("------------ Check Status ------------");
            a = admin.getStatus();
            if (0 != a.compareTo(User.STATUS_ACTIVE))
                throw new ObjectException();

            // Security Domain is the initial security domain
            Logger.detail("------------ Check Security Domain ------------");
            a = admin.getSecurityDomain();
            if (0 != a.compareTo(SecurityDomain.INITIAL_SD_ID))
                throw new ObjectException();

            // Remote authentication object has been created
            Logger.detail("------------ Check that remote authenticator is created ------------");
            RemoteAuthenticator rAuth = admin.getRemoteAuthenticatorObject();
            rAuth.syncroFields(admin);
            a = rAuth.getOwnerID();
            if (0 != a.compareTo(admin.getObjectId()))
                throw new ObjectException();

            // Local Authentication object has been created
            Logger.detail("------------ Check that local authenticator is created ------------");
            LocalAuthenticator lAuth = admin.getLocalAuthenticatorObject();
            lAuth.syncroFields(admin);
            a = lAuth.getOwnerID();
            if (0 != a.compareTo(admin.getObjectId()))
                throw new ObjectException();

            // AC Policy is the default policy
            Logger.detail("------------ Check that access policy is the default one ------------");
            a = admin.getAcPolicy();
            if (0 != a.compareTo(AccessPolicy.DEFAULT_ACCESS_POLICY_ID))
                throw new ObjectException();

            // Notification Policy is the default policy
            Logger.detail("------------ Check that notification policy is the default one ------------");
            a = admin.getNotificationPolicy();
            if (0 != a.compareTo(NotificationPolicy.INITIAL_NOTIFICATION_OBJECT_ID))
                throw new CommandErrorException();

            // The Name is the initial name
            Logger.detail("------------ Check that name is the default one ------------");
            a = admin.getName();
            name = admin.getObjectId();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCase);
        tc.testCompleted(true, "success");
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check sensors from 0 to 7

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        Sensor sensor;
        Door door;
        String telemetry;
        String testCode = testBatch + "/" + "Test Case 02";

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
            for (int i = 0; i < Device.GPIO_NUMBER; i++) {
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
            for (int i = 0; i < Device.GPIO_NUMBER; i++) {
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
            tc.testCompleted(false, "fail");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCode);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
     testCase03
     --------------------------------------------------------------------------
     AUTHOR:	PDI

     DESCRIPTION: SuperA, administrator and user launch a command to a door.
                 Triggering mandate PIN verification

     Security Level: None

     ------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        Door door;
        int index;
        LogEntry le;
        String doorId, requestorId, gpioValue, status, policyId;
        AccessPolicy defaultPolicy;
        LocalAuthenticator superALocalAuthetnicator;
        String testCode = testBatch + "/" + "Test Case 03";
        String pin;

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            pin = "01020304";

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superALocalAuthetnicator = superA.getLocalAuthenticatorObject();
            superALocalAuthetnicator.syncroFields(superA);
            status = superALocalAuthetnicator.getStatus();

            superA.resetPin(superA);

            if (0 == status.compareTo(LocalAuthenticator.AUTHOBJ_PIN_READY))
                superA.changePin(superA, pin, pin);
            else
                superA.setPin(superA, pin);


            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.setPin(admin, pin);
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.setPin(user, pin);
            user.syncroFields(user);

            policyId = admin.getAcPolicy();
            defaultPolicy = new AccessPolicy(admin, policyId);
            defaultPolicy.setAlwaysWeeklyPolicy();
            defaultPolicy.update(superA);

            // set the door
            door = new Door(superA);
            door.setGpioId("00");
            door.setSecurity(Door.SE_DOOR_SECURITY_PIN);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);

            doorId = door.getObjectId();

//#
//# Super A can trigger a door
//#
            Logger.detail("------------ Super A can trigger a door ------------");

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON, pin);
            gpioValue = door.getOutput(superA);
            if ((0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_HIGH))) {
                throw new TestException();
            }

            requestorId = superA.getObjectId();

            // get the last log entry
            index = DeviceLogger.getLastEntryIndex(superA);
            le = DeviceLogger.getEntry(superA, index);

            // check log entry
            if ((0 != requestorId.compareTo(le.requesterId)) ||
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result))) {
                throw new TestException();
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF, pin);
            gpioValue = door.getOutput(superA);
            if (!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW))) {
                throw new TestException();
            }

//#
//# Administrator can trigger a Door
//#
            Logger.detail("------------ Admin can trigger a door ------------");
            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON, pin);
            gpioValue = door.getOutput(admin);
            if (!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH))) {
                throw new TestException();
            }

            requestorId = admin.getObjectId();

            // get the last log entry
            index = DeviceLogger.getLastEntryIndex(superA);
            le = DeviceLogger.getEntry(superA, index);

            // check log entry
            if ((0 != requestorId.compareTo(le.requesterId)) ||
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result))) {
                throw new TestException();
            }
            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF, pin);
            gpioValue = door.getOutput(admin);
            if (!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW))) {
                throw new TestException();
            }
//#
//# User can trigger a Door
//#

            Logger.detail("------------ user can not update door ------------");
            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON, pin);
            gpioValue = door.getOutput(user);
            if (!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH))) {
                throw new TestException();
            }

            requestorId = user.getObjectId();

            // get the last log entry
            index = DeviceLogger.getLastEntryIndex(superA);
            le = DeviceLogger.getEntry(superA, index);

            // check log entry
            if ((0 != requestorId.compareTo(le.requesterId)) ||
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result))) {
                throw new TestException();
            }
            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF, pin);
            gpioValue = door.getOutput(user);
            if (!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW))) {
                throw new TestException();
            }
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | TestException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCode);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Digital Function Block over MQTT;
                Test the logical and with two input I1 and I2. The two input are from a door;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
        Door door, door1;
        String pin, policyId;
        AccessPolicy defaultPolicy;
        String testCode = testBatch + "/" + "Test Case 04";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            pin = "01020304";
            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.changePin(superA, pin, pin);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.syncroFields(admin);
            policyId = admin.getAcPolicy();

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

            defaultPolicy = new AccessPolicy(admin, policyId);
            defaultPolicy.setAlwaysWeeklyPolicy();
            defaultPolicy.update(superA);

            // create the door. Configure it.
            door = new Door(superA);
            door.setGpioId("03");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);

            // create the door. Configure it.
            door1 = new Door(superA);
            door1.setGpioId("02");
            door1.setStatus(Door.SE_DOOR_ENABLED);
            door1.update(superA);
            door1.syncroFields(superA);

            Sensor s = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s.setGpioId("03");
            s.update(superA);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);
//#
//# Test of the AND Digital function block (2 input)
//#
            Logger.detail("------------ Test of the AND Digital function block (2 input) ------------");
            Logger.detail("------------ Input 1 is a sensor; Input 2 is a door ------------");


            DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
            dfb.setInputId(s.getObjectId(), 0);
            dfb.setInputId(door1.getObjectId(), 1);
            dfb.setGpio("00");
            dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_AND_2_INPUT);
            dfb.update(superA);

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(RESET_DELAY);
            } catch (InterruptedException ignored) {
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            String a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                throw new TestException();
            // wait;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

            door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

            a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                throw new TestException();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                throw new TestException();
            // wait;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                throw new TestException();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            door1.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            dfb.delete(superA);
            s.delete(superA);
            s1.delete(superA);

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(RESET_DELAY);
            } catch (InterruptedException ignored) {
            }

        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        tc.testCompleted(true, "success");

        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
    testCase08
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: MQTT Test. Finiste state Machine;
                    Test Finite State Automa Flip Flop Set Reset. I0 = set; I1 = reset;
                 It tests also the get output and getLastOutput;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
        String pin, buffer, a;
        Door door, door1;
        String testCode = testBatch + "/" + "Test Case 05";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            pin = "01020304";
            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.changePin(superA, pin, pin);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

            // create the door. Configure it.
            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);

            door1 = new Door(superA);
            door1.setGpioId("04");
            door1.setStatus(Door.SE_DOOR_ENABLED);
            door1.update(superA);
            door1.syncroFields(superA);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);

            FiniteStateMachine fsm = new FiniteStateMachine(superA);


            buffer = "0010000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000";
            fsm.setStateTransitionFunction(superA, buffer, 0);


            buffer = "1011000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000";
            fsm.setStateTransitionFunction(superA, buffer, 1);

            buffer = "0010000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000";

            fsm.setOutputFunction(superA, buffer, 0);

            buffer = "1111000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000000";

            fsm.setOutputFunction(superA, buffer, 1);

            fsm.setGpio(0, "00");

            fsm.setInputId(door1.getObjectId(), 0);
            fsm.setInputId(door.getObjectId(), 1);
            fsm.update(superA);

            thisDevice.systemReset(pin, superA, true);
            // wait;
            try {
                Thread.sleep(RESET_DELAY);
            } catch (InterruptedException ignored) {
            }

//#
//# Test of the Set Reset Flip Flop. it test also the get Output
//#                
            Logger.detail("------------ Test SR Flip Flop ------------");
            // Set
            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                throw new TestException();

            a = fsm.getOutput(superA);
            if (0 != a.compareTo(FiniteStateMachine.SE_FINITE_STATE_MACHINE_HIGH))
                throw new TestException();
            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }
            // Memory 11
            door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                throw new TestException();
            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }
            // Memory 00
            door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                throw new TestException();
            // wait;
            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }
            // Reset
            door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
               /* a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                    throw new TestException();  */

            fsm.syncroFields(superA);
            a = fsm.getLastOutput();
            if (0 != a.compareTo(FiniteStateMachine.SE_FINITE_STATE_MACHINE_OUT_LOW))
                throw new TestException();

            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }
            // Memory 11
            door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                throw new TestException();
            // wait;

            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }
            // Memory 00
            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            a = s1.getMeasure(superA);
            if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                throw new TestException();
            // wait;
            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            fsm.delete(superA);
            s1.delete(superA);
            door.delete(superA);
            door1.delete(superA);

            thisDevice.systemReset(pin, superA, true);
            // wait;
            try {
                Thread.sleep(RESET_DELAY);
            } catch (InterruptedException ignored) {
            }
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
}

