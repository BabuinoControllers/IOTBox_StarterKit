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
public class TestLog001 {

//    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestLog001";
    public static final String deviceId = MainTest.TestMain.deviceId;

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

        superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);

        j = 1;
        try {
            thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

            thisUnit.setDevice(thisDevice);

            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);

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
        } catch (TestException | DiscoveryException | CommandErrorException |IOException|ObjectException e) {
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

    DESCRIPTION: It test that after a power up the log is updated

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        String pin;
        int index, index1;
        LogEntry le;

        String testCode = testBatch + "/" + "Test Case 01";

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            index = DeviceLogger.getLogCount(superA);
            //#
            //# Power on is logged into the system
            //#

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

            index1 = DeviceLogger.getLogCount(superA);

            // only one power on shall be logged
            if ((index1 - index) != 1) {
                throw new TestException();
            }
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1 - 1);
            if ((0 != le.requesterId.compareTo("00000000")) ||
                    (0 != le.objectId.compareTo("00000000")) ||
                    (0 != DeviceLogger.SE_LOG_POWER_ON.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }


            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1);
            if (!("FFFFFFFF".equals(le.requesterId)) ||
                    (!"FFFFFFFF".equals(le.objectId)) ||
                    (!"FFFF".equals(le.event)) ||
                    (!"FFFF".equals(le.result)) ||
                    (!"FFFFFFFF".equals(le.timestamp))) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCode);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: It test tests THREAD LOOP

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        String expectedRes, pin;
        Apdu apdu;
        int index, index1;
        LogEntry le;

        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 02";

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            index = DeviceLogger.getLogCount(superA);
            //#
            //# Global Error Task looping is logged
            //#

            // put data global error tesst Task loop
            apdu = new Apdu("ACB50301");
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);

            // set the expected result and send the command;
            expectedRes = Command.COMMAND_EXPECTED_SUCCESSFUL_RESPONSE;

            // Test Global Error
            c.description = "Global Error Thread loop";
            c.requester = superA;
            String a = apdu.toString();

            c.execute(a, expectedRes);

            // wait;
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ignored) {
            }

            index1 = DeviceLogger.getLogCount(superA);

            // Two new entries in the log: Global Error and Power On
            if ((index1 - index) != 2) {
                throw new TestException();
            }

            le = DeviceLogger.getEntry(superA, index1 - 2);
            if ((0 != DeviceLogger.SE_LOG_GLOBAL_ERROR.compareTo(le.event)) ||
                    (0 != Apdu.SW_6A01_GLOBAL_ERROR_THREAD_LOOP.compareTo(le.result))) {
                throw new TestException();
            }

            le = DeviceLogger.getEntry(superA, index1 - 1);
            if ((0 != le.requesterId.compareTo("00000000")) ||
                    (0 != le.objectId.compareTo("00000000")) ||
                    (0 != DeviceLogger.SE_LOG_POWER_ON.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }
            // increase the index
            index1 = (index1 + 1) & (0x0FFF);
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1);
            if (!("FFFFFFFF".equals(le.requesterId)) ||
                    (!"FFFFFFFF".equals(le.objectId)) ||
                    (!"FFFF".equals(le.event)) ||
                    (!"FFFF".equals(le.result)) ||
                    (!"FFFFFFFF".equals(le.timestamp))) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
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

    DESCRIPTION: It test tests EXCEPTION

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        String expectedRes, pin;
        Apdu apdu;
        int index, index1;
        LogEntry le;

        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 03";

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            index = DeviceLogger.getLogCount(superA);
            //#
            //# Global Error Hardware Exception is logged
            //#

            // put data global error tesst Task loop
            apdu = new Apdu("ACB50302");
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);

            // set the expected result and send the command;
            expectedRes = Command.COMMAND_EXPECTED_SUCCESSFUL_RESPONSE;

            // Test Global Error
            c.description = "Global Error Exception";
            c.requester = superA;
            String a = apdu.toString();

            c.execute(a, expectedRes);

            // wait;
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ignored) {
            }

            index1 = DeviceLogger.getLogCount(superA);

            // Two new entries in the log: Global Error and Power On
            if ((index1 - index) != 2) {
                throw new TestException();
            }

            le = DeviceLogger.getEntry(superA, index1 - 2);
            if ((0 != DeviceLogger.SE_LOG_GLOBAL_ERROR.compareTo(le.event)) ||
                    (0 != Apdu.SW_6A01_GLOBAL_ERROR_EXCEPTION.compareTo(le.result))) {
                throw new TestException();
            }

            le = DeviceLogger.getEntry(superA, index1 - 1);
            if ((0 != le.requesterId.compareTo("00000000")) ||
                    (0 != le.objectId.compareTo("00000000")) ||
                    (0 != DeviceLogger.SE_LOG_POWER_ON.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }
            // increase the index
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1);
            if (!("FFFFFFFF".equals(le.requesterId)) ||
                    (!"FFFFFFFF".equals(le.objectId)) ||
                    (!"FFFF".equals(le.event)) ||
                    (!"FFFF".equals(le.result)) ||
                    (!"FFFFFFFF".equals(le.timestamp))) {
                throw new TestException();
            }

            //#
            //# Power on is logged into the system and just one more item is logged
            //#

            index = DeviceLogger.getLogCount(superA);

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

            index1 = DeviceLogger.getLogCount(superA);

            // only one power on shall be logged
            if ((index1 - index) != 1) {
                throw new TestException();
            }
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1 - 1);
            if ((0 != le.requesterId.compareTo("00000000")) ||
                    (0 != le.objectId.compareTo("00000000")) ||
                    (0 != DeviceLogger.SE_LOG_POWER_ON.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }

            // increase the index
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1);
            if (!("FFFFFFFF".equals(le.requesterId)) ||
                    (!"FFFFFFFF".equals(le.objectId)) ||
                    (!"FFFF".equals(le.event)) ||
                    (!"FFFF".equals(le.result)) ||
                    (!"FFFFFFFF".equals(le.timestamp))) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
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

    DESCRIPTION: It test tests WWDG

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
        String expectedRes, pin;
        Apdu apdu;
        int index, index1;
        LogEntry le;

        Command c = new Command();

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            index = DeviceLogger.getLogCount(superA);
            //#
            //# Global Error WWDG is logged
            //#

            // put data global error tesst Task loop
            apdu = new Apdu("ACB50303");
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, pin);

            // set the expected result and send the command;
            expectedRes = Command.COMMAND_EXPECTED_SUCCESSFUL_RESPONSE;

            // Test Global Error
            c.description = "Global WWDG Global Error";
            c.requester = superA;
            String a = apdu.toString();

            c.execute(a, expectedRes);

            // wait;
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ignored) {
            }

            index1 = DeviceLogger.getLogCount(superA);

            // Two new entries in the log: Global Error and Power On
            if ((index1 - index) != 2) {
                throw new TestException();
            }

            le = DeviceLogger.getEntry(superA, index1 - 2);
            if ((0 != DeviceLogger.SE_LOG_GLOBAL_ERROR.compareTo(le.event)) ||
                    (0 != Apdu.SW_6A01_GLOBAL_HW_WWDG.compareTo(le.result))) {
                throw new TestException();
            }

            le = DeviceLogger.getEntry(superA, index1 - 1);
            if ((0 != le.requesterId.compareTo("00000000")) ||
                    (0 != le.objectId.compareTo("00000000")) ||
                    (0 != DeviceLogger.SE_LOG_POWER_ON.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }
            // increase the index
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1);
            if (!("FFFFFFFF".equals(le.requesterId)) ||
                    (!"FFFFFFFF".equals(le.objectId)) ||
                    (!"FFFF".equals(le.event)) ||
                    (!"FFFF".equals(le.result)) ||
                    (!"FFFFFFFF".equals(le.timestamp))) {
                throw new TestException();
            }

            //#
            //# Power on is logged into the system and just one more item is logged
            //#

            index = DeviceLogger.getLogCount(superA);

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

            index1 = DeviceLogger.getLogCount(superA);

            // only one power on shall be logged
            if ((index1 - index) != 1) {
                throw new TestException();
            }
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1 - 1);
            if ((0 != le.requesterId.compareTo("00000000")) ||
                    (0 != le.objectId.compareTo("00000000")) ||
                    (0 != DeviceLogger.SE_LOG_POWER_ON.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }

            // increase the index
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index1);
            if (!("FFFFFFFF".equals(le.requesterId)) ||
                    (!"FFFFFFFF".equals(le.objectId)) ||
                    (!"FFFF".equals(le.event)) ||
                    (!"FFFF".equals(le.result)) ||
                    (!"FFFFFFFF".equals(le.timestamp))) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCode);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
    testCase05
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: It test that the log is working if updated more than log memory size

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
        String pin;
        int index, index1;
        LogEntry le;

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            // Create Door
            Door door = new Door(superA);
            door.setGpioId(Door.SE_VIRTUAL_GPIO_0);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);

            //#
            //# Power on is logged into the system
            //#
            String requestorId = superA.getObjectId();
            String doorId = door.getObjectId();

            String command = Door.DOOR_COMMAND_SWITCH_ON;
            index1 = DeviceLogger.getLogCount(superA);
            while (index1 < 0x800) {

                Logger.detail("Log Entry ------------>" + index1);

                index = DeviceLogger.getLogCount(superA);

                door.setOutput(superA, command);

                index1 = DeviceLogger.getLogCount(superA);

                // only one power on shall be logged
                if ((index1 - index) != 1) {
                    throw new TestException();
                }

                le = DeviceLogger.getEntry(superA, index1 - 1);

                if (0 == command.compareTo(Door.DOOR_COMMAND_SWITCH_ON)) {
                    // check log entry
                    if ((0 != requestorId.compareTo(le.requesterId)) ||
                            (0 != doorId.compareTo(le.objectId)) ||
                            (0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                            (0 != "9000".compareTo(le.result))) {
                        throw new TestException();
                    }

                    command = Door.DOOR_COMMAND_SWITCH_OFF;
                } else {
                    // check log entry
                    if ((0 != requestorId.compareTo(le.requesterId)) ||
                            (0 != doorId.compareTo(le.objectId)) ||
                            (0 != DeviceLogger.SE_LOG_ACTUATOR_OFF.compareTo(le.event)) ||
                            (0 != "9000".compareTo(le.result))) {
                        throw new TestException();
                    }
                    command = Door.DOOR_COMMAND_SWITCH_ON;
                }
            }

            index = DeviceLogger.getLogCount(superA);

            // logging this command logger page will be flushed
            door.setOutput(superA, command);

            index1 = DeviceLogger.getLogCount(superA);

            Logger.detail("Log Entry ------------>" + index1);

            // only one power on shall be logged
            if ((index - index1) != 0xFE) {
                throw new TestException();
            }

            le = DeviceLogger.getEntry(superA, index1 - 1);

            if (0 == command.compareTo(Door.DOOR_COMMAND_SWITCH_ON)) {
                // check log entry
                if ((0 != requestorId.compareTo(le.requesterId)) ||
                        (0 != doorId.compareTo(le.objectId)) ||
                        (0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                        (0 != "9000".compareTo(le.result))) {
                    throw new TestException();
                }

            } else {
                // check log entry
                if ((0 != requestorId.compareTo(le.requesterId)) ||
                        (0 != doorId.compareTo(le.objectId)) ||
                        (0 != DeviceLogger.SE_LOG_ACTUATOR_OFF.compareTo(le.event)) ||
                        (0 != "9000".compareTo(le.result))) {
                    throw new TestException();
                }
            }
            //#
            //# Check if last index is correct after reset
            //#
            thisDevice.systemReset(pin, superA, true);
            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }
            index = DeviceLogger.getLogCount(superA);

            // only one power on shall be logged after the reset
            if ((index - index1) != 0x01) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            door.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCode);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
       testCase06
       --------------------------------------------------------------------------
       AUTHOR:	PDI

       DESCRIPTION: Test Restore Last Output Value after logwrap around

       Security Level: None

       ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException {
        Door door;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;
        String pin;
        String testCode = testBatch + "/" + "Test Case 06";

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

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(admin);

            // set the policy at always
            policyId = admin.getAcPolicy();
            defaultPolicy = new AccessPolicy(admin, policyId);
            defaultPolicy.setAlwaysWeeklyPolicy();
            defaultPolicy.update(superA);
            // set the door
            door = new Door(superA);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setGpioId(0);
            door.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
            door.update(superA);
            door.syncroFields(superA);

//#
//# Door high is restored after reset
//#
            Logger.detail("------------ Door high is restored after reset ------------");

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

            // introduce some extra information into the log
            door.update(superA);
            user.update(superA);

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            // introduce some extra information into the log
            door.update(superA);
            user.update(superA);
            door.update(superA);
            door.update(superA);
            user.update(superA);

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            door.update(superA);
            user.update(superA);

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

            gpioValue = door.getOutput(superA);
            if (!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH))) {
                throw new TestException();
            }
//#
//# Door low is restored after reset
//#
            Logger.detail("------------ Door low is restored after reset ------------");

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

            // introduce some extra information into the log
            door.update(superA);
            user.update(superA);

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            // introduce some extra information into the log
            door.update(superA);
            user.update(superA);
            door.update(superA);
            door.update(superA);
            user.update(superA);

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

            gpioValue = door.getOutput(superA);
            if (!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW))) {
                throw new TestException();
            }
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
}

