package TestCases;
import com.sdk.*;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.*;
import com.testlog.*;

@SuppressWarnings("unused")
public class TestUser002 {
    
    /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestUser002";

    /********************************
    Public Methods
    ********************************/
    public static final String deviceId =  TestMain.deviceId;
       
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
	public void run()
    {
        thisUnit.setTestTitle(testBatch);

        int j;
       // ---------------------- Code -------------------------------        
        
        Command.onError = Command.ALT_ON_ERROR;
        
        j = 1;
        try {
            thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3,2000);
            thisUnit.setDevice(thisDevice);
            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice); 

            testCase01();
            j++;

            testCase02();
            j++;

            testCase04();
            j++;            
        }
        catch (TestException | DiscoveryException e){
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

        DESCRIPTION: Policy table for Administrator and user is initialized as expected.

        Security Level: None

        ------------------------------------------------------------------------------*/
    public static boolean testCase01() throws TestException
    {
        boolean result;
        ArrayList<PolicyLink> policyList;
        int policyListSize, i;

        String testCode = testBatch+"/"+"Test Case 01";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        result = true;
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

                // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);
            superA.syncroPolicyList(superA);

                // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "newAdministrator");
                // object personalized
            admin.updateKey("newAdministratorKeys");
            admin.syncroFields(admin);
            admin.syncroPolicyList(admin);
            
                // object created
            User user = new User(superA, User.USER_ROLE_USER, "newAdministrator");
                // object personalized
            user.updateKey("newAdministratorKeys");
            user.syncroFields(admin);
            user.syncroPolicyList(admin);            
            
//#
//# Get the policy List of an administrator and check that the default values are null object Id;
//# 

            policyList = admin.getPolicyList();

            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){
                
                if (( 0 != policyList.get(i).doorId.compareTo(Device.NULL_OBJECT_ID)) ||
                    ( 0 != policyList.get(i).policyId.compareTo(Device.NULL_OBJECT_ID))){
                    
                    throw new TestException();
                }
            }
//#
//# Get the policy List of a SuperA and check that the default values are null ObjectId;
//#
            policyList = user.getPolicyList();

            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){
                
                if (( 0 != policyList.get(i).doorId.compareTo(Device.NULL_OBJECT_ID)) ||
                    ( 0 != policyList.get(i).policyId.compareTo(Device.NULL_OBJECT_ID))){
                    
                    throw new TestException();
                }
            }            
 //#
//# Get the policy List of a superA. It is null;
//#
            policyList = superA.getPolicyList();

            if ( null != policyList){

                throw new TestException();
            }           
//#
//# Object deletion
//#
            admin.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                         

        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
                //result = false;
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCode);
        Logger.testResult(result);

        return result;
    }
    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check the update policy List

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase02() throws TestException
    {
        boolean result;
        ArrayList<PolicyLink> policyList, policyList1;
        int policyListSize, i, j;
        AccessPolicy policy;
        Door door;
        String doorId, policyId;

        String testCode = testBatch+"/"+"Test Case 02";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        result = true;
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

                // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);
            superA.syncroPolicyList(superA);

                // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "newAdministrator");
                // object personalized
            admin.updateKey("newAdministratorKeys");
            admin.syncroFields(admin);
            admin.syncroPolicyList(admin);
            
                // object created
            User user = new User(superA, User.USER_ROLE_USER, "newAdministrator");
                // object personalized
            user.updateKey("newAdministratorKeys");
            user.syncroFields(admin);
            user.syncroPolicyList(admin);
            
                // create and initialize a door object
            door = new Door(superA);                        
            door.setGpioId("05");
            door.setInitialOutputValue( Door.SE_DOOR_STATUS_LOW);          
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.update(superA);
            door.syncroFields(superA);            
            
                // create and initialize an access policy object
            policy = new AccessPolicy(superA);
            policy.setAlwaysWeeklyPolicy();
//#            
//# SuperA can update policy List of an admin
//# 
            
            doorId = door.getObjectId();
            policyId =  policy.getObjectId();
            
            policyList = admin.getPolicyList();
            
            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){

                Logger.detail("####################### ------------ Iterator i ------------> "+i);                                           
                
                policyList.get(i).doorId = doorId;
                policyList.get(i).policyId = policyId;

                admin.updatePolicyList(superA);
                admin.syncroPolicyList(superA);
                
                policyList1 = admin.getPolicyList();
                for (j = 0; j< policyListSize; j++){
                    
                    Logger.detail("------------ Iterator j ------------> "+j);
                    if (( 0 != policyList.get(j).doorId.compareTo(policyList1.get(j).doorId)) ||
                        ( 0 != policyList.get(j).policyId.compareTo(policyList1.get(j).policyId))){

                        throw new TestException();
                    }
                }                                
            }
//#            
//# SuperA can update policy List of an user
//# 

            policyList = user.getPolicyList();
            
            doorId = door.getObjectId();
            //policyId =  policy.getObjectId();

            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){
                
                Logger.detail("####################### ------------ Iterator i ------------> "+i);
                
                policyList.get(i).doorId = doorId;
                policyList.get(i).policyId = policy.getObjectId();

                user.updatePolicyList(superA);
                user.syncroPolicyList(superA);
                
                policyList1 = user.getPolicyList();
                for (j = 0; j< policyListSize; j++){

                    Logger.detail("------------ Iterator j ------------> "+j);
                    if (( 0 != policyList.get(j).doorId.compareTo(policyList1.get(j).doorId)) ||
                        ( 0 != policyList.get(j).policyId.compareTo(policyList1.get(j).policyId))){

                        throw new TestException();
                    }
                }                                
            }
                // celar user Policy List
            for (i = 0; i< policyListSize; i++){
                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }
//#            
//# Admin can update policy List of an user
//# 

            policyList = user.getPolicyList();
            
            doorId = door.getObjectId();
            //policyId =  policy.getObjectId();

            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){

                Logger.detail("####################### ------------ Iterator i ------------> "+i);
                policyList = user.getPolicyList();
                policyList.get(i).doorId = doorId;
                policyList.get(i).policyId = policy.getObjectId();

                user.updatePolicyList(admin);                
                admin.syncroPolicyList(superA);
                
                policyList1 = user.getPolicyList();
                for (j = 0; j< policyListSize; j++){
                    
                    Logger.detail("------------ Iterator i ------------> "+j);
                    
                    if (( 0 != policyList.get(j).doorId.compareTo(policyList1.get(j).doorId)) ||
                        ( 0 != policyList.get(j).policyId.compareTo(policyList1.get(j).policyId))){

                        throw new TestException();
                    }
                }                                
            }            
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            policy.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                         

        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
                //result = false;
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCode);
        Logger.testResult(result);

        return result;
    }
  /*----------------------------------------------------------------------------
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check the update policy List

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase04() throws TestException
    {
        boolean result;
        Command c = new Command();
        ArrayList<PolicyLink> policyList;
        int i, j;
        AccessPolicy policy;
        Door door;
        String  expectedRes, a, userId, policyId, doorId;
        int policyListSize;
        
        String testCode = testBatch+"/"+"Test Case 04";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        result = true;
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

                // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);
            superA.syncroPolicyList(superA);

                // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "newAdministrator");
                // object personalized
            admin.updateKey("newAdministratorKeys");
            admin.syncroFields(admin);
            admin.syncroPolicyList(admin);
            
                            // object created
            User admin1 = new User(superA, User.USER_ROLE_ADMIN, "newAdministrator");
                // object personalized
            admin1.updateKey("newAdministratorKeys");
            admin1.syncroFields(admin);
            admin1.syncroPolicyList(admin);
            
                // object created
            User user = new User(superA, User.USER_ROLE_USER, "newAdministrator");
                // object personalized
            user.updateKey("newAdministratorKeys");
            user.syncroFields(admin);
            user.syncroPolicyList(admin);
            
                // create and initialize a door object
            door = new Door(superA);                        
            door.setGpioId("05");
            door.setInitialOutputValue( Door.SE_DOOR_STATUS_LOW);          
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.update(superA);
            door.syncroFields(superA);            
            
                // create and initialize an access policy object
            policy = new AccessPolicy(superA);
            policy.setAlwaysWeeklyPolicy();
//#            
//# It is not possible update the policy list with an object ID that is not of a door
//#                        
            Logger.detail("It is not possible update the policy list with an object ID that is not of a door.");
                
            policyList = user.getPolicyList();
            
            userId = user.getObjectId();
            policyId =  policy.getObjectId();

            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){

                Logger.detail("####################### ------------ Iterator i ------------> "+i);
                policyList = user.getPolicyList();
                policyList.get(i).doorId = userId;
                policyList.get(i).policyId = policyId;
                
                    // create the apdu object
                 Apdu apdu = new Apdu(Apdu.UPDATE_USER_APDU);
                 apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin1.getObjectId());

                     // convert policy List into a string
                 a = "";
                 for (j=0; j<policyList.size(); j++){
                     a = a.concat(policyList.get(j).toString());
                 }
                
                apdu.addTlv(Atlv.DATA_TAG_USER_POLICY_LIST, a);
            
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                        String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                        String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                            Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                        String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            
                c.description = "Update User Object";
                c.requester = superA;
                c.execute(apdu.toString(), expectedRes);
                     
                user.syncroPolicyList(superA);
                
                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }
//#            
//# It is not possible update the policy list with an object ID that is not of a policy
//#
            Logger.detail("It is not possible update the policy list with an object ID that is not of a policy.");
                
            policyList = user.getPolicyList();
            
            doorId = door.getObjectId();
            userId =  user.getObjectId();

            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){

                Logger.detail("####################### ------------ Iterator i ------------> "+i);
                policyList = user.getPolicyList();
                policyList.get(i).doorId = doorId;
                policyList.get(i).policyId = userId;
                
                    // create the apdu object
                 Apdu apdu = new Apdu(Apdu.UPDATE_USER_APDU);
                 apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin1.getObjectId());

                     // convert policy List into a string
                 a = "";
                 for (j=0; j<policyList.size(); j++){
                     a = a.concat(policyList.get(j).toString());
                 }
                
                apdu.addTlv(Atlv.DATA_TAG_USER_POLICY_LIST, a);
            
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                        String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                        String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                            Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                        String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            
                c.description = "Update User Object";
                c.requester = superA;
                c.execute(apdu.toString(), expectedRes);
                     
                user.syncroPolicyList(superA);
                
                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }               
//#            
//# It is not possible update the policy list with an object ID that did not exist
//#
            Logger.detail("It is not possible update the policy list with an object ID that did not exist");
                
            policyList = user.getPolicyList();
            
            doorId = door.getObjectId();
            policyId =  "FFFFFFFC";

            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){

                Logger.detail("####################### ------------ Iterator i ------------> "+i);
                policyList = user.getPolicyList();
                policyList.get(i).doorId = doorId;
                policyList.get(i).policyId = policyId;
                
                    // create the apdu object
                 Apdu apdu = new Apdu(Apdu.UPDATE_USER_APDU);
                 apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin1.getObjectId());

                     // convert policy List into a string
                 a = "";
                 for (j=0; j<policyList.size(); j++){
                     a = a.concat(policyList.get(j).toString());
                 }
                
                apdu.addTlv(Atlv.DATA_TAG_USER_POLICY_LIST, a);
            
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                        String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                        String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                            Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                        String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            
                c.description = "Update User Object";
                c.requester = superA;
                c.execute(apdu.toString(), expectedRes);
                     
                user.syncroPolicyList(superA);
                
                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }                                 
//#            
//# It is not possible update the policy list with an object ID that did not exist
//#
            Logger.detail("It is not possible update the policy list with an object ID that did not exist");
                
            policyList = user.getPolicyList();
            
            doorId = "FFFFFFFC";
            policyId =  policy.getObjectId();

            policyListSize = policyList.size();
            for (i = 0; i< policyListSize; i++){

                Logger.detail("####################### ------------ Iterator i ------------> "+i);
                policyList = user.getPolicyList();
                policyList.get(i).doorId = doorId;
                policyList.get(i).policyId = policyId;
                
                    // create the apdu object
                 Apdu apdu = new Apdu(Apdu.UPDATE_USER_APDU);
                 apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin1.getObjectId());

                     // convert policy List into a string
                 a = "";
                 for (j=0; j<policyList.size(); j++){
                     a = a.concat(policyList.get(j).toString());
                 }
                
                apdu.addTlv(Atlv.DATA_TAG_USER_POLICY_LIST, a);
            
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                        String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                        String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                            Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                        String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            
                c.description = "Update User Object";
                c.requester = superA;
                c.execute(apdu.toString(), expectedRes);
                     
                user.syncroPolicyList(superA);
                
                policyList.get(i).doorId = Device.NULL_OBJECT_ID;
                policyList.get(i).policyId = Device.NULL_OBJECT_ID;
            }                       
//#
//# Object deletion
//#
            admin.delete(superA);
            admin1.delete(superA);
            user.delete(superA);
            door.delete(superA);
            policy.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                         

        }
        catch (CommandErrorException | ObjectException | IOException e)
        {
                //result = false;
            tc.testCompleted(false, "fail");

            throw new TestException();

        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCode);
        Logger.testResult(result);

        return result;
    }
    
}

