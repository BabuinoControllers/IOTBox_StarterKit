package TestCases;

import com.sdk.*;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import com.testlog.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;


@SuppressWarnings("unused")
public class TestAccessPolicy002 {

//    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestAccessPolicy002";
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

                // it reset the PIN of Super A
                superA.resetPin(superA);
            }
        } catch (TestException | DiscoveryException | IOException | ObjectException | CommandErrorException e) {
            Logger.detail("TEST FAILURE ----->" + j);
            thisUnit.testCompleted(false, "failure in test case "+j);
            Assertions.fail("TEST FAILURE ----->" + j);

            //return false;
        }
        thisUnit.testCompleted(true, "complete!");
        Logger.detail("OK");
    }

    /*----------------------------------------------------------------------------
testCase01
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: It is possible open a door if it is in the policy list and policy allows

Security Level:

------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);
        AccessPolicy ap;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 01";
        ArrayList<PolicyLink> policyList;



        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);
tc.setCaseTitle(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);

            // set the policy for admin
            user.setDefaultAccessPolicy(Device.NULL_OBJECT_ID);
            user.update(superA);
            user.syncroFields(admin);

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);
                    
/*################################################################################################
                It is possible open a door if it is in the policy list and policy allows User             
  ################################################################################################*/

            Logger.detail("------------ Trigger the door always Policy ------------");

            ap.setAlwaysWeeklyPolicy();
            ap.update(superA);

            user.syncroPolicyList(user);
            policyList = user.getPolicyList();

            for (int i =0; i<policyList.size(); i++)
            {
                policyList.get(i).doorId = door.getObjectId();
                policyList.get(i).policyId = ap.getObjectId();

                user.updatePolicyList(admin);
                user.syncroPolicyList(user);

                door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

                // delay dummy
                ap.update(superA);

                door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }
/*################################################################################################
                It is possible open a door if it is in the policy list and policy allows Administrator             
  ################################################################################################*/

            Logger.detail("------------ Trigger the door always Policy ------------");

            ap.setAlwaysWeeklyPolicy();
            ap.update(superA);

            admin.syncroPolicyList(admin);
            policyList = admin.getPolicyList();

            for (int i =0; i<policyList.size(); i++)
            {
                policyList.get(i).doorId = door.getObjectId();
                policyList.get(i).policyId = ap.getObjectId();

                admin.updatePolicyList(superA);
                admin.syncroPolicyList(admin);

                door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

                // delay dummy
                ap.update(superA);

                door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }
                    
  /*################################################################################################
                Delete                
  ################################################################################################*/

            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false,"failure");
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

DESCRIPTION: It is not possible open a door if it is in the policy list and policy do not allow

Security Level:

------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command c = new Command();
        AccessPolicy ap;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 02";
        ArrayList<PolicyLink> policyList;
        Apdu apdu;
        String expectedRes, a;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);
tc.setCaseTitle(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);

            // set the policy for admin
            user.setDefaultAccessPolicy(Device.NULL_OBJECT_ID);
            user.update(superA);
            user.syncroFields(admin);

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);
                    
/*################################################################################################
                It is not possible open a door if it is in the policy list and policy allows Admin             
  ################################################################################################*/

            Logger.detail("------------ Trigger the door never Policy ------------");

            ap.setNeverWeeklyPolicy();
            ap.update(superA);

            admin.syncroPolicyList(admin);
            policyList = user.getPolicyList();

            for (int i =0; i<policyList.size(); i++)
            {
                policyList.get(i).doorId = door.getObjectId();
                policyList.get(i).policyId = ap.getObjectId();

                admin.updatePolicyList(superA);
                admin.syncroPolicyList(admin);

                // create the apdu object
                apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());
                apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

                // set the expected result and send the command;
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                        String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                        String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                        String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

                // send command
                c.description = "Trigger Door ";
                c.requester = admin;
                a = apdu.toString();
                c.execute(a, expectedRes);

                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }
/*################################################################################################
                It is not possible open a door if it is in the policy list and policy allows Admin             
  ################################################################################################*/

            Logger.detail("------------ Trigger the door never Policy ------------");

            ap.setNeverWeeklyPolicy();
            ap.update(superA);

            user.syncroPolicyList(user);
            policyList = user.getPolicyList();

            for (int i =0; i<policyList.size(); i++)
            {
                policyList.get(i).doorId = door.getObjectId();
                policyList.get(i).policyId = ap.getObjectId();

                user.updatePolicyList(admin);
                user.syncroPolicyList(user);

                // create the apdu object
                apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());
                apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

                // set the expected result and send the command;
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                        String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                        String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                        String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

                // send command
                c.description = "Trigger Door ";
                c.requester = user;
                a = apdu.toString();
                c.execute(a, expectedRes);

                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }
  /*################################################################################################
                Delete                
  ################################################################################################*/

            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false,"failed");
            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true,"success");
    }
}

