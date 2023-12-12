package TestCases;

import com.sdk.*;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.testlog.*;


@SuppressWarnings("deprecation")
public class TestFiniteStateMachine001 {

    //    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestFiniteStateMachine001";
    public static final String deviceId = MainTest.TestMain.deviceId;

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
            thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);
            thisUnit.setDevice(thisDevice);
            superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);

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

            testCase10();
            j++;

            testCase11();
            j++;

            testCase12();
            j++;
            superA.resetPin(superA);
        } catch (TestException | DiscoveryException | CommandErrorException | IOException | ObjectException e) {

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

    DESCRIPTION: SuperA creates a Finite State Machine object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        String a, name;
        FiniteStateMachine fsm;
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
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            //#
            //# Super A creates a Finite State Machine object. Initial values are according to specs.
            //#
            // Create Finite State Machine
            Logger.detail("------------ Create Finite State Machine  ------------");
            fsm = new FiniteStateMachine(superA);
            fsm.syncroFields(superA);

            // Check SD ID
            Logger.detail("------------ Check Security Domain ID ------------");
            a = fsm.getSecurityDomain();
            if (0 != a.compareTo(superA.getSecurityDomain()))
                throw new ObjectException();

            // Check Status
            Logger.detail("------------ Check Status ------------");
            a = fsm.getStatus();
            if (0 != a.compareTo(FiniteStateMachine.SE_FSM_STATUS_ACTIVE))
                throw new ObjectException();

            // The Name is the initial name
            Logger.detail("------------ Check that name is the default one ------------");
            a = fsm.getName();
            name = fsm.getObjectId();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            // input list
            Logger.detail("------------ Check input list ------------");
            for (int i = 0; i < FiniteStateMachine.SE_FSM_INPUT_NUMBER; i++) {
                a = fsm.getInputId(i);
                if (0 != a.compareTo("FFFFFFFE"))
                    throw new ObjectException();
            }

            Logger.detail("------------ Check input list index ------------");
            for (int i = 0; i < FiniteStateMachine.SE_FSM_INPUT_NUMBER; i++) {
                a = fsm.getInputWire(i);
                if (0 != a.compareTo("00"))
                    throw new ObjectException();
            }
            // output list
            Logger.detail("------------ Check output list ------------");
            for (int i = 0; i < FiniteStateMachine.SE_FSM_OUTPUT_NUMBER; i++) {
                a = fsm.getGpio(i);
                if (0 != a.compareTo("FF"))
                    throw new ObjectException();
            }
            //#
            //# Object deletion
            //#
            fsm.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

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
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Only SuperA can create Finite State Machine Object

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
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // instantiate a local User object for the SUPER-A
            User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");

            // object personalized
            admin.updateKey("panda");
            admin.syncroFields(admin);

            // instantiate a local User object for the SUPER-A
            User user = new User(superA, User.USER_ROLE_USER, "zaffata");

            // object personalized
            user.updateKey("penda");
            user.syncroFields(user);
//#
//# User can not create Finite State Machine
//# 
            Logger.detail("------------ User can not create Finite State Machine ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to create a door
            apduObject = new Apdu(Apdu.CREATE_SE_FINITE_STATE_MACHINE_APDU);

            // send command
            c.description = "Create Finite State Machine";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
//#
//# Administrator can not create Finite State Machine
//#
            Logger.detail("------------ Administrator can not create Finite State Machine ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to create a door
            apduObject = new Apdu(Apdu.CREATE_SE_FINITE_STATE_MACHINE_APDU);


            // send command
            c.description = "Create digital function block";
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
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase03
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Readeable Fields can be read by all user object despite their role

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        String a;
        FiniteStateMachine fsm;
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
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // instantiate a local User object for Admin
            User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
            admin.updateKey("panda");
            admin.syncroFields(admin);

            // instantiate a local User object for Admin
            User admin1 = new User(superA, User.USER_ROLE_ADMIN, "zebra");
            admin1.updateKey("panda");
            admin1.syncroFields(admin1);

            // instantiate a local User
            User user = new User(superA, User.USER_ROLE_USER, "pinco");
            user.updateKey("pallino");
            user.syncroFields(user);

            // instantiate a local User
            User user1 = new User(superA, User.USER_ROLE_USER, "pinco");
            user1.updateKey("pallino");
            user1.syncroFields(user1);

            // Create Door
            Logger.detail("------------ Create Finite State Machine ------------");
            fsm = new FiniteStateMachine(superA);
            fsm.syncroFields(superA);
            //#
            //# Super A can read the status field of a Finite State Machine
            //#
            Logger.detail("-- Super A can read the status field  --");

            // Check Status
            Logger.detail("------------ Check Status ------------");
            fsm.syncroFields(superA);
            a = fsm.getStatus();
            if (0 != a.compareTo(FiniteStateMachine.SE_FSM_STATUS_ACTIVE))
                throw new ObjectException();

            //#
            //# Administrator can read the status field of a Finite State Machine
            //#
            Logger.detail("-- Administrator can read the status field  --");

            // Check Status
            Logger.detail("------------ Check Status ------------");
            fsm.syncroFields(admin);
            a = fsm.getStatus();
            if (0 != a.compareTo(FiniteStateMachine.SE_FSM_STATUS_ACTIVE))
                throw new ObjectException();
            //#
            //# User can read the status field of a Finite Stae Machine
            //#
            Logger.detail("-- User can read the status field  --");

            // Check Status
            Logger.detail("------------ Check Status ------------");
            fsm.syncroFields(user);
            a = fsm.getStatus();
            if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_STATUS_ACTIVE))
                throw new ObjectException();
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            admin1.delete(superA);
            user.delete(superA);
            fsm.delete(superA);
            user1.delete(superA);

            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
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
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can updated the fileds of a Door object. User and Admin not.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
        String a, expectedRes;
        FiniteStateMachine fsm;
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
            admin.syncroFields(admin);
            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

            // Create Door
            Logger.detail("------------ Create Finite State Machine Objcect ------------");
            fsm = new FiniteStateMachine(superA);
            fsm.syncroFields(superA);

//#
//# Super A can updated the field of a Finite State Machine Object
//#                                
            // Status
            fsm.setStatus(FiniteStateMachine.SE_FSM_STATUS_INACTIVE);
            fsm.update(superA);
            fsm.syncroFields(superA);

            Logger.detail("------------ Check Status ------------");
            a = fsm.getStatus();
            if (0 != a.compareTo(FiniteStateMachine.SE_FSM_STATUS_INACTIVE))
                throw new ObjectException();

            // Output Object Id
            for (int i = 0; i < FiniteStateMachine.SE_FSM_OUTPUT_NUMBER; i++) {
                fsm.setGpio(i, "0" + i);
                fsm.update(superA);
                fsm.syncroFields(superA);

                Logger.detail("------------ Check Gpio ------------");
                a = fsm.getGpio(i);
                if (0 != a.compareTo("0" + i))
                    throw new ObjectException();
            }
            // Input Wire
            for (int i = 0; i < FiniteStateMachine.SE_FSM_INPUT_NUMBER; i++) {
                fsm.setInputWire("0" + i, i);
                fsm.update(superA);
                fsm.syncroFields(superA);

                Logger.detail("------------ Check Input Wire ------------");
                a = fsm.getInputWire(i);
                if (0 != a.compareTo("0" + i))
                    throw new ObjectException();
            }

            // Input
            FiniteStateMachine[] fsmArray = new FiniteStateMachine[FiniteStateMachine.SE_FSM_INPUT_NUMBER];
            for (int i = 0; i < FiniteStateMachine.SE_FSM_INPUT_NUMBER; i++) {
                fsmArray[i] = new FiniteStateMachine(superA);
                fsm.setInputId(fsmArray[i].getObjectId(), i);
            }
            fsm.update(superA);
            fsm.syncroFields(superA);

            Logger.detail("------------ Check Input Id ------------");
            for (int i = 0; i < FiniteStateMachine.SE_FSM_INPUT_NUMBER; i++) {
                a = fsm.getInputId(i);
                if (0 != a.compareTo(fsmArray[i].getObjectId()))
                    throw new ObjectException();
            }

            // name
            fsm.setName("0123456789ABCDEF0123456789ABCDEF");
            fsm.update(superA);
            fsm.syncroFields(superA);

            Logger.detail("------------ Check Name ------------");
            a = fsm.getName();
            if (0 != a.compareTo("0123456789ABCDEF0123456789ABCDEF"))
                throw new CommandErrorException();
//#
//# Administrator can not update Finite State Machine object
//#
            Logger.detail("------------ Administrator can not update Finite State Machine object ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the FSM
            Apdu apdu = new Apdu(Apdu.UPDATE_FINITE_STATE_MACHINE_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, fsm.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_PULSE, "00000001");
            // send command
            c.description = "Update FSM";
            c.requester = admin;
            c.execute(apdu.toString(), expectedRes);

//#
//# user can not update Finite State Machine
//#
            Logger.detail("------------ user can not update Finite State MAchine Object ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the door
            apdu = new Apdu(Apdu.UPDATE_FINITE_STATE_MACHINE_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, fsm.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_PULSE, "00000001");

            // send command
            c.description = "Update Finite State Machine";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            fsm.delete(superA);
            for (FiniteStateMachine finiteStateMachine : fsmArray) {
                finiteStateMachine.delete(superA);

            }
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase05
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can delete a door. User and administrators can not delete a digital function block.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
        String expectedRes;
        FiniteStateMachine fsm;
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
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

//#
//# Super A can delete a Finite State Machine object
//#
            Logger.detail("------------ Super A can delete a Finite State Machine Object ------------");

            // Create Door
            Logger.detail("------------ Create Door ------------");
            fsm = new FiniteStateMachine(superA);

            // Update Initial Configuration. Door exist/
            fsm.setStatus(FiniteStateMachine.SE_FSM_STATUS_INACTIVE);

            String objId = fsm.getObjectId();

            fsm.delete(superA);

            // check that the door has been deleted
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update digital function block
            Apdu apdu = new Apdu(Apdu.UPDATE_FINITE_STATE_MACHINE_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, objId);
            apdu.addTlv(Atlv.DATA_TAG_ON_DELAY, "12340000");
            // send command
            c.description = "Update Digital Funciton Blocl";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);

//#
//# Administrator can not delete a door
//#
            fsm = new FiniteStateMachine(superA);

            Logger.detail("------------ Administrator can not delete a Finite State Machine block ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the door
            apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, fsm.getObjectId());
            // send command
            c.description = "Delete Finite State Machine Block";
            c.requester = admin;
            c.execute(apdu.toString(), expectedRes);

//#
//# user can not delete a Finite state Machine
//#

            Logger.detail("------------ user can not delete a digital Finite State Machine block ------------");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // try to update the door
            apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, fsm.getObjectId());

            // send command
            c.description = "Delete Digital function block";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            fsm.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase06
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test the get and set of the state transition function;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException {
        String pin, buffer, a;
        String testCode = testBatch + "/" + "Test Case 06";
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
            superA.setPin(superA, pin);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

//#
//# Test the set and the get of state transition function
//#
            Logger.detail("------------ Test the set and the get of the state transition function ------------");

            FiniteStateMachine fsm = new FiniteStateMachine(superA);

            // set the state transition function
            for (int i = 0; i < FiniteStateMachine.SE_NUMBER_OF_STATE_TRANSITION_FUNCTION_LINE; i++) {
                buffer = "";
                for (int j = 0; j < FiniteStateMachine.SE_STATE_TRANSITION_FUNCTION_LINE_SIZE; j++) {
                    buffer = buffer.concat(String.format("%02X", i));
                }
                fsm.setStateTransitionFunction(superA, buffer, i);
            }
            // get the state transition function
            for (int i = 0; i < FiniteStateMachine.SE_NUMBER_OF_STATE_TRANSITION_FUNCTION_LINE; i++) {
                buffer = "";
                for (int j = 0; j < FiniteStateMachine.SE_STATE_TRANSITION_FUNCTION_LINE_SIZE; j++) {
                    String newPiece = String.format("%02X", i);
                    buffer = buffer.concat(newPiece);
                }
                a = fsm.getStateTransitionFunction(superA, i);

                if (0 != a.compareTo(buffer))
                    throw new ObjectException();
            }

//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            fsm.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase07
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Update the output function

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException {
        String pin, buffer, a;
        String testCode = testBatch + "/" + "Test Case 07";
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
            superA.setPin(superA, pin);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

//#
//# Test the set and the get of state transition function
//#
            Logger.detail("------------ Test the set and the get of the output function ------------");

            FiniteStateMachine fsm = new FiniteStateMachine(superA);

            // set the state transition function
            for (int i = 0; i < FiniteStateMachine.SE_NUMBER_OF_OUTPUT_FUNCTION_LINE; i++) {
                buffer = "";
                for (int j = 0; j < FiniteStateMachine.SE_OUTPUT_FUNCTION_LINE_SIZE; j++) {
                    String newPiece = String.format("%02X", i);
                    buffer = buffer.concat(newPiece);
                }
                fsm.setOutputFunction(superA, buffer, i);
            }
            // get the state transition function
            for (int i = 0; i < FiniteStateMachine.SE_NUMBER_OF_OUTPUT_FUNCTION_LINE; i++) {
                buffer = "";
                for (int j = 0; j < FiniteStateMachine.SE_OUTPUT_FUNCTION_LINE_SIZE; j++) {
                    String newPiece = String.format("%02X", i);
                    buffer = buffer.concat(newPiece);
                }
                a = fsm.getOutputFunction(superA, i);

                if (0 != a.compareTo(buffer))
                    throw new ObjectException();
            }

//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            fsm.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase08
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test Finite State Automa Flip Flop Set Reset. I0 = set; I1 = reset;
                 It tests also the get output and getLastOutput;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase08() throws TestException {
        String pin, buffer, a;
        Door door, door1;
        String testCode = testBatch + "/" + "Test Case 08";
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
            superA.setPin(superA, pin);

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
                Thread.sleep(4000);
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
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
    testCase09
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check that Create, update and delete Digital Function Block updates the log.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase09() throws TestException {
        FiniteStateMachine fsm;
        int index;
        LogEntry le;
        String doorId, requestorId;
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
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

//#
//# Log is updated when a Finite State Machine object is created
//#
            Logger.detail("------------ Log is updated when a Finite State Machine object is created ------------");

            fsm = new FiniteStateMachine(superA);
            fsm.syncroFields(superA);

            index = DeviceLogger.getLastEntryIndex(superA);

            requestorId = superA.getObjectId();
            doorId = fsm.getObjectId();

            le = DeviceLogger.getEntry(superA, index);

            // check log entry
            if ((0 != requestorId.compareTo(le.requesterId)) ||
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_CREATE_EVENT.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result))) {
                throw new TestException();
            }
//#
//# Log is updated when a Finite State MAchine object is deleted;
//#
            Logger.detail("------------ Log is updated when a finite State Machine Object is deleted; ------------");

            fsm.delete(superA);

            index = (index + 1) & 0xFFFF;

            le = DeviceLogger.getEntry(superA, index);

            // check log entry
            if ((0 != requestorId.compareTo(le.requesterId)) ||
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_DELETE_EVENT.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result))) {
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
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
   testCase10
   --------------------------------------------------------------------------
   AUTHOR:	PDI

   DESCRIPTION: Test Finite State Automa SImulate a counter.

   Security Level: None

   ------------------------------------------------------------------------------*/
    private final static String[] s = {"01", "21", "23", "43", "45", "65", "67", "87", "89", "A9", "AB", "CB", "CD", "ED", "EF", "0F"};

    public static void testCase10() throws TestException {
        String pin, buffer, a;
        Door door, door1;
        String testCode = testBatch + "/" + "Test Case 10";
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
            superA.setPin(superA, pin);

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

            Sensor s2 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s2.setGpioId("01");
            s2.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s2.update(superA);

            Sensor s3 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s3.setGpioId("02");
            s3.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s3.update(superA);

            FiniteStateMachine fsm = new FiniteStateMachine(superA);

            // set the state transition function
            for (int i = 0; i < FiniteStateMachine.SE_NUMBER_OF_STATE_TRANSITION_FUNCTION_LINE; i++) {
                buffer = "";
                for (int j = 0; j < FiniteStateMachine.SE_STATE_TRANSITION_FUNCTION_LINE_SIZE; j++) {

                    String newPiece = s[i];
                    buffer = buffer.concat(newPiece);
                }
                fsm.setStateTransitionFunction(superA, buffer, i);

                buffer = "";
                for (int j = 0; j < FiniteStateMachine.SE_STATE_TRANSITION_FUNCTION_LINE_SIZE; j++) {
                    String newPiece = String.format("%02X", (((i + 1) & 0x0F) >> 1) + 16 * (((i + 1) & 0x0F) >> 1));
                    buffer = buffer.concat(newPiece);
                }
                fsm.setOutputFunction(superA, buffer, i);
            }

            fsm.setGpio(0, "00");
            fsm.setGpio(1, "01");
            fsm.setGpio(2, "02");
            fsm.setGpio(3, "03");

            fsm.setInputId(door.getObjectId(), 0);
            fsm.update(superA);

            thisDevice.systemReset(pin, superA, true);
            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

//#
//# Test of the counter
//#                
            Logger.detail("------------ Test of the pulse counter ------------");

            for (int i = 0; i < 50; i++) {
                // Set
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                // check LSB
                a = s1.getMeasure(superA);
                if (0 == (i & 0x01)) {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                } else {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                }
                // check MSB
                a = s3.getMeasure(superA);
                if (0 == ((i + 1) & 0x04)) {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                } else {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                }
                // check Middle bit
                a = s2.getMeasure(superA);
                if (0 == ((i + 1) & 0x02)) {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                } else {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                }

                // wait;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                // Set
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

                a = s1.getMeasure(superA);
                if (0 == (i & 0x01)) {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                } else {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                }
                // check MSB
                a = s3.getMeasure(superA);
                if (0 == ((i + 1) & 0x04)) {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                } else {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                }
                // check Middle bit
                a = s2.getMeasure(superA);
                if (0 == ((i + 1) & 0x02)) {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                } else {
                    if (0 != a.compareTo(Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                }

                // wait;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            fsm.delete(superA);
            s1.delete(superA);
            s2.delete(superA);
            s3.delete(superA);
            door.delete(superA);
            door1.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
     testCase11
     --------------------------------------------------------------------------
     AUTHOR:	PDI

     DESCRIPTION: It is possible to read and update the state waiting time table

     Security Level: None

     ------------------------------------------------------------------------------*/
    public static void testCase11() throws TestException {
        Door door;
        String pin, policyId, a;
        AccessPolicy defaultPolicy;
        String testCode = testBatch + "/" + "Test Case 11";
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
            superA.setPin(superA, pin);

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
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);

            FiniteStateMachine fsm = new FiniteStateMachine(superA);

//#
//# Initial value in state waiting table are the default 
//#
            Logger.detail("------------ Initial value of state waiting table  ------------");

            fsm.syncroStateWaitingTable(superA);
            for (int i = 0; i < FiniteStateMachine.SE_FSM_STATUS_NUMBER; i++) {
                a = fsm.getNextStatusInTimeWaitingTable(i);
                if (0 != a.compareTo("00"))
                    throw new TestException();

                a = fsm.getWaitingTimeInTimeWaitingTable(i);
                if (0 != a.compareTo("00000000"))
                    throw new TestException();

            }
//#
//# SuperA can update the next status time waiting table. Super A can read the content. The content is correct.
//#
            Logger.detail("------------ Read and update of status waiting table  ------------");

            fsm.syncroStateWaitingTable(superA);
            for (int i = 0; i < FiniteStateMachine.SE_FSM_STATUS_NUMBER; i++) {
                fsm.setTimeWaitingTable((i + 1) & 0x0F, 1000 + i, i);
            }
            fsm.updateStateWaitingTable(superA);

            fsm.syncroStateWaitingTable(superA);
            for (int i = 0; i < FiniteStateMachine.SE_FSM_STATUS_NUMBER; i++) {
                a = fsm.getNextStatusInTimeWaitingTable(i);
                if (0 != a.compareTo(String.format("%02X", (i + 1) & 0x0F)))
                    throw new TestException();

                a = fsm.getWaitingTimeInTimeWaitingTable(i);
                if (0 != a.compareTo(String.format("%08X", 1000 + i)))
                    throw new TestException();

            }

//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            fsm.delete(superA);

        } catch (CommandErrorException | ObjectException | IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
   testCase12
   --------------------------------------------------------------------------
   AUTHOR:	PDI

   DESCRIPTION: Test Finite State Automa Simulate a counter.

   Security Level: None

   ------------------------------------------------------------------------------*/
    //private final static String[] s1 = {"01", "21", "23", "43", "45", "65", "67", "87", "89", "A9", "AB", "CB", "CD", "ED", "EF", "0F"};
    public static void testCase12() throws TestException {
        String pin, buffer;
        Door door, door1;
        String testCode = testBatch + "/" + "Test Case 12";
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
            superA.setPin(superA, pin);

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

            Sensor s2 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s2.setGpioId("01");
            s2.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s2.update(superA);

            Sensor s3 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s3.setGpioId("02");
            s3.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s3.update(superA);

            FiniteStateMachine fsm = new FiniteStateMachine(superA);

            // set the state transition function
            for (int i = 0; i < FiniteStateMachine.SE_NUMBER_OF_STATE_TRANSITION_FUNCTION_LINE; i++) {
                buffer = "";
                for (int j = 0; j < FiniteStateMachine.SE_STATE_TRANSITION_FUNCTION_LINE_SIZE; j++) {

                    String newPiece = s[i];
                    buffer = buffer.concat(newPiece);
                }
                fsm.setStateTransitionFunction(superA, buffer, i);

                buffer = "";
                for (int j = 0; j < FiniteStateMachine.SE_STATE_TRANSITION_FUNCTION_LINE_SIZE; j++) {
                    String newPiece = String.format("%02X", (((i + 1) & 0x0F) >> 1) + 16 * (((i + 1) & 0x0F) >> 1));
                    buffer = buffer.concat(newPiece);
                }
                fsm.setOutputFunction(superA, buffer, i);
            }

            fsm.setGpio(0, "00");
            fsm.setGpio(1, "01");
            fsm.setGpio(2, "02");
            fsm.setGpio(3, "03");

            fsm.setInputId(door.getObjectId(), 0);
            fsm.update(superA);


            for (int i = 0; i < FiniteStateMachine.SE_FSM_STATUS_NUMBER; i++) {
                fsm.setTimeWaitingTable((i + 1) & 0x0F, 20000, i);
            }
            fsm.updateStateWaitingTable(superA);

            thisDevice.systemReset(pin, superA, true);

            // wait;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {
            }

//#
//# Test of the counter
//#           

            try {
                Thread.sleep(20000);
            } catch (InterruptedException ignored) {
            }
/*                Logger.detail("------------ Test of the pulse counter ------------");    

                for (int i =0; i<50; i++)
                {
                    // Set
                    door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                    
                        // check LSB
                    a = s1.getMeasure(superA);
                    if (0 == (i&0x01))
                    {                        
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                               throw new TestException();                
                    }
                    else
                    {
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                               throw new TestException();                
                    }
                    // check MSB
                    a = s3.getMeasure(superA);
                    if (0 == ((i+1)&0x04))
                    {                        
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                               throw new TestException();                
                    }
                    else
                    {
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                               throw new TestException();                
                    } 
                    // check Middle bit
                    a = s2.getMeasure(superA);
                    if (0 == ((i+1)&0x02))
                    {                        
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                               throw new TestException();                
                    }
                    else
                    {
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                               throw new TestException();                
                    }                     
                        
                        // wait;
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) 
                    {
                    }
                    // Set
                    door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

                    a = s1.getMeasure(superA);
                    if (0 == (i&0x01))
                    {                        
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                               throw new TestException();                
                    }
                    else
                    {
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                               throw new TestException();                
                    }
                    // check MSB
                    a = s3.getMeasure(superA);
                    if (0 == ((i+1)&0x04))
                    {                        
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                               throw new TestException();                
                    }
                    else
                    {
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                               throw new TestException();                
                    }
                    // check Middle bit
                    a = s2.getMeasure(superA);
                    if (0 == ((i+1)&0x02))
                    {                        
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                               throw new TestException();                
                    }
                    else
                    {
                        if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                               throw new TestException();                
                    }                     
                    
                        // wait;
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) 
                    {
                    }                    
                }*/
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            fsm.delete(superA);
            s1.delete(superA);
            s2.delete(superA);
            s3.delete(superA);
            door.delete(superA);
            door1.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }
}
