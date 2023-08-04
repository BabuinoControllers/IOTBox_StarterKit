package TestCases;
import com.sdk.*;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.testlog.*;

@SuppressWarnings({"unused", "deprecation"})
public class TestUser001 {

    /********************************
     PUBLIC Fields
     ********************************/
    public static final String testBatch = "TestUser001";
    public static final String deviceId = MainTest.TestMain.deviceId;

    /********************************
     Public Methods
     ********************************/
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

                testCase13();
                j++;

                testCase14();
                j++;

                testCase15();
                j++;

                testCase16();
                j++;

                testCase17();
                j++;

                testCase18();
                j++;

                testCase19();
                j++;

                testCase01();
                j++;

//                if (u == 0) {
//                    //    Logger.detail("<-------------------------------->");
//                    //    Logger.detail(" WIFI ");
//                    //    Logger.detail("<-------------------------------->");
//                    //   d = Device.discover(deviceId, Device.WIFI, 2 , 1000);
//                    //    IoStream.setActiveDevice(d);
//                    //    j=1;
//                }
            }
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
        tc.testCompleted(true, "success");

        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Super-A creates User objects. The initial fields are set as expected;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        String a, name;
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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            //#
            //# Creation of an User by an Admin
            //# Result: OK

            // object created
            User user = new User(superA, User.USER_ROLE_USER, "newUser");
            // object personalized
            user.updateKey("newUserKeys");
            user.syncroFields(user);

            // Role is user
            Logger.detail("------------ Check Role ------------");
            a = user.getRole();
            if (0 != a.compareTo(User.USER_ROLE_USER))
                throw new ObjectException();

            // Status is active
            Logger.detail("------------ Check Status ------------");
            a = user.getStatus();
            if (0 != a.compareTo(User.STATUS_ACTIVE))
                throw new ObjectException();

            // Security Domain is the initial security domain
            Logger.detail("------------ Check Security Domain ------------");
            a = user.getSecurityDomain();
            if (0 != a.compareTo(SecurityDomain.INITIAL_SD_ID))
                throw new ObjectException();

            // Remote authentication object has been created
            Logger.detail("------------ Check that remote authenticator is created ------------");
            RemoteAuthenticator rAuth = user.getRemoteAuthenticatorObject();
            rAuth.syncroFields(user);
            a = rAuth.getOwnerID();
            if (0 != a.compareTo(user.getObjectId()))
                throw new ObjectException();

            // Local Authentication object has been created
            Logger.detail("------------ Check that local authenticator is created ------------");
            LocalAuthenticator lAuth = user.getLocalAuthenticatorObject();
            lAuth.syncroFields(user);
            a = lAuth.getOwnerID();
            if (0 != a.compareTo(user.getObjectId()))
                throw new ObjectException();

            // AC Policy is the default policy
            Logger.detail("------------ Check that access policy is the default one ------------");
            a = user.getAcPolicy();
            if (0 != a.compareTo(AccessPolicy.DEFAULT_ACCESS_POLICY_ID))
                throw new ObjectException();

            // Notification Policy is the default policy
            Logger.detail("------------ Check that notification policy is the default one ------------");
            a = user.getNotificationPolicy();
            if (0 != a.compareTo(NotificationPolicy.INITIAL_NOTIFICATION_OBJECT_ID))
                throw new CommandErrorException();

            // The Name is the initial name
            Logger.detail("------------ Check that name is the default one ------------");
            a = user.getName();
            name = user.getObjectId();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();
            //#
            //# Object deletion
            //#
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
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

    DESCRIPTION: Administrator creates User objects. The initial fields are set as expected;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        String a, name;

        String testCode = testBatch + "/" + "Test Case 03";

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "newAdministrator");
            // object personalized
            admin.updateKey("newAdministratorKeys");
            admin.syncroFields(admin);

//#
//# Creation of an User by an administrator
//# Result: OK

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "newUser");
            // object personalized
            user.updateKey("newUserKeys");

            // Role is user
            Logger.detail("------------ Check Role ------------");
            a = user.getRole();
            if (0 != a.compareTo(User.USER_ROLE_USER))
                throw new ObjectException();

            // Status is active
            Logger.detail("------------ Check Status ------------");
            a = user.getStatus();
            if (0 != a.compareTo(User.STATUS_ACTIVE))
                throw new ObjectException();

            // Security Domain is the initial security domain
            Logger.detail("------------ Check Security Domain ------------");
            a = user.getSecurityDomain();
            if (0 != a.compareTo(SecurityDomain.INITIAL_SD_ID))
                throw new ObjectException();

            // Remote authentication object has been created
            Logger.detail("------------ Check that remote authenticator is created ------------");
            RemoteAuthenticator rAuth = user.getRemoteAuthenticatorObject();
            rAuth.syncroFields(user);
            a = rAuth.getOwnerID();
            if (0 != a.compareTo(user.getObjectId()))
                throw new ObjectException();

            // Local Authentication object has been created
            Logger.detail("------------ Check that local authenticator is created ------------");
            LocalAuthenticator lAuth = user.getLocalAuthenticatorObject();
            lAuth.syncroFields(user);
            a = lAuth.getOwnerID();
            if (0 != a.compareTo(user.getObjectId()))
                throw new ObjectException();

            // AC Policy is the default policy
            Logger.detail("------------ Check that access policy is the default one ------------");
            a = user.getAcPolicy();
            if (0 != a.compareTo(AccessPolicy.DEFAULT_ACCESS_POLICY_ID))
                throw new ObjectException();

            // Notification Policy is the default policy
            Logger.detail("------------ Check that notification policy is the default one ------------");
            a = user.getNotificationPolicy();
            if (0 != a.compareTo(NotificationPolicy.INITIAL_NOTIFICATION_OBJECT_ID))
                throw new ObjectException();

            // The Name is the initial name
            Logger.detail("------------ Check that name is the default one ------------");
            a = user.getName();
            name = user.getObjectId();
            if (0 != a.compareTo(name))
                throw new ObjectException();
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
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

    DESCRIPTION: User can not create administrator object or super A;
                Administrator can not create Super A
                                Administrator can not create Administrators
                User can not create Administrators
                User can not create Users

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
        String expectedRes;
        Apdu apduObject;
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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "newAdmin");
            // object personalized
            admin.updateKey("newAdministratorKeys");
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "newUser");
            // object personalized
            user.updateKey("newUserkk");

            user.syncroFields(user);
            // User can not create Super-A
            Logger.detail("------------ User Can not create Super-A ------------");

            apduObject = new Apdu(Apdu.CREATE_SE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_SUPERADMIN);
            apduObject.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apduObject.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            c.requester = user;
            c.description = "User Can not create Super-A";
            c.execute(apduObject.toString(), expectedRes);

            // Administrator can not create Super-A
            Logger.detail("------------ Administrator Can not create Super-A ------------");
            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFF_INCORRECT_ROLE_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            c.requester = admin;
            c.description = "Administrator Can not create Super-A";
            c.execute(apduObject.toString(), expectedRes);

            // Administrator can not create Administrators
            Logger.detail("------------ Administrator Can not create Administrators ------------");

            apduObject = new Apdu(Apdu.CREATE_SE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_ADMIN);
            apduObject.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apduObject.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            c.requester = admin;
            c.description = "Administrator Can not create Administrators";
            c.execute(apduObject.toString(), expectedRes);

            // User can not create Administrators
            Logger.detail("------------ User Can not create Administrators ------------");
            apduObject = new Apdu(Apdu.CREATE_SE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_ADMIN);
            apduObject.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apduObject.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            c.requester = user;
            c.description = "User Can not create Administrators";
            c.execute(apduObject.toString(), expectedRes);

            // User can not create Users
            Logger.detail("------------ User Can not create users ------------");
            apduObject = new Apdu(Apdu.CREATE_SE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_USER);
            apduObject.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apduObject.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            c.requester = user;
            c.description = "User Can not create users";
            c.execute(apduObject.toString(), expectedRes);

//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
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

    DESCRIPTION: Wrong requestor lenghts

    Security Level: None

    ------------------------------------------------------------------------------*/
/*	public static boolean testCase05()
	{
		boolean result;
		String expectedRes;
		
		Command c = new Command();
                
                String testCode = testBatch+"/"+"Test Case 05";
		
		// ---------------------- Code -------------------------------
		result = true;
		try
		{
			
			Logger.testCase(testCode);
			
				// launch a ping
			c.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                            // object personalized
			superA.updateKey("admin");                        
                            
                            // object created
			User admin = new User(superA, User.USER_ROLE_ADMIN, "pish");
                            // object personalized
			admin.updateKey("newAdministratorKeysAlfa");
                        
                            // object created
			User user = new User(admin, User.USER_ROLE_USER, "puck");
                        
                        user.updateKey("newUserAlfa");
			
				// Wrong Requestor Length = 3
			Logger.detail("------------ Wrong Requestor Length ------------");
			//apdu = Apdu.CREATE_SUPER_ADM_APDU;
			expectedRes = TLV.initTLVBuffer(Atlv.DATA_TAG_APDU_RESPONSE, Apdu.SW_6AFE_INCORRECT_TLV_TLV_STRING);
			c.requestorID = "000003";
			c.description = "Wrong Requestor Length";
			c.execute("", expectedRes);
			
				// Wrong Requestor Length = 2
			Logger.detail("------------ Wrong Requestor Length ------------");
			//apdu = Apdu.CREATE_SUPER_ADM_APDU;
			expectedRes = TLV.initTLVBuffer(Atlv.DATA_TAG_APDU_RESPONSE, Apdu.SW_6AFE_INCORRECT_TLV_TLV_STRING);
			c.requestorID = "0003";
			c.description = "Wrong Requestor Length";
			c.execute("", expectedRes);
			
				// Wrong Requestor Length = 5
			Logger.detail("------------ Wrong Requestor Length ------------");
			//apdu = Apdu.CREATE_SUPER_ADM_APDU;
			expectedRes = TLV.initTLVBuffer(Atlv.DATA_TAG_APDU_RESPONSE, Apdu.SW_6AFE_INCORRECT_TLV_TLV_STRING);
			c.requestorID = "0000000003";
			c.description = "Wrong Requestor Length";
			c.execute("", expectedRes);
        //#
	//# Object deletion
	//#
                        admin.delete(superA);
                        user.delete(superA);
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                         
	
		}
		catch (CommandErrorException | ObjectException e)
		{
			result = false;
		}	
		
		Logger.testCase(testCode);
		Logger.testResult(result);
            
            return result;
	}*/
	/*----------------------------------------------------------------------------
	testCase06
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: It test that after create user command the log is updated.
	
	Security Level: None

	------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException {
        String expectedRes;
        Apdu apdu;
        int index;
        LogEntry le;

        Command c = new Command();

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            index = DeviceLogger.getLastEntryIndex(superA);

            // increase the index
            index = (index + 1) & (0x0FFF);
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index);
            if (!("FFFFFFFF".equals(le.requesterId)) ||
                    (!"FFFFFFFF".equals(le.objectId)) ||
                    (!"FFFF".equals(le.event)) ||
                    (!"FFFF".equals(le.result)) ||
                    (!"FFFFFFFF".equals(le.timestamp))) {
                throw new TestException();
            }

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.syncroFields(superA);

            // Check that the event is logged
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != le.requesterId.compareTo(superA.getObjectId())) ||
                    (0 != admin.getObjectId().compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_CREATE_EVENT.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }

            // object personalized
            admin.updateKey("newAdministratorKeysSutta");

            index = DeviceLogger.getLastEntryIndex(superA);

            // increase the index
            index = (index + 1) & (0x0FFF);

            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != "FFFFFFFF".compareTo(le.requesterId)) ||
                    (0 != "FFFFFFFF".compareTo(le.objectId)) ||
                    (0 != "FFFF".compareTo(le.event)) ||
                    (0 != "FFFF".compareTo(le.result)) ||
                    (0 != "FFFFFFFF".compareTo(le.timestamp))) {
                throw new TestException();
            }

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.syncroFields(superA);

            // Check that the event is logged
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != admin.getObjectId().compareTo(le.requesterId)) ||
                    (0 != user.getObjectId().compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_CREATE_EVENT.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }

            // object personalized
            user.updateKey("newAdministratorKeysSatta");

            index = DeviceLogger.getLastEntryIndex(superA);

            // increase the index
            index = (index + 1) & (0x0FFF);

            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != "FFFFFFFF".compareTo(le.requesterId)) ||
                    (0 != "FFFFFFFF".compareTo(le.objectId)) ||
                    (0 != "FFFF".compareTo(le.event)) ||
                    (0 != "FFFF".compareTo(le.result)) ||
                    (0 != "FFFFFFFF".compareTo(le.timestamp))) {
                throw new TestException();
            }

            // create the apdu object
            apdu = new Apdu(Apdu.CREATE_SE_USER_APDU);
            apdu.addTlv(Apdu.DATA_TAG_USER_ROLE, User.USER_ROLE_SUPERADMIN);
            apdu.addTlv(Atlv.DATA_TAG_RAO_KIC, "313233343536373839303A3B3C3D3E3F");
            apdu.addTlv(Atlv.DATA_TAG_RAO_KID, "313233343536373839303A3B3C3D3E3F");

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFF_INCORRECT_ROLE_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Admin Can not create Super-A";
            c.requester = admin;
            c.execute(apdu.toString(), expectedRes);

            // Check that the event is logged
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != admin.getObjectId().compareTo(le.requesterId)) ||
                    (0 != "FFFFFFFE".compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_CREATE_EVENT.compareTo(le.event)) ||
                    (0 != "6AFF".compareTo(le.result))) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        tc.testCompleted(true, "success");

        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
    testCase07
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test of set Role

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException {
        String expectedRes;
        String r;

        Command c = new Command();
        Apdu apduObject;

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "lucy");
            // object personalized
            admin.updateKey("newAdministratorKeysCiccia");
            admin.syncroFields(admin);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            // object personalized
            user.updateKey("newSutta");
            user.syncroFields(user);

            // Super A can change the role of an user
            // User can read its role
            Logger.detail("------------ Super A can change the role of an user ------------");
            user.setRole(User.USER_ROLE_ADMIN);
            user.update(superA);
            user.syncroFields(user);
            r = user.getRole();
            if (0 != r.compareTo(User.USER_ROLE_ADMIN)) {
                throw new TestException();
            }

            // Revert the role to user
            user.setRole(User.USER_ROLE_USER);
            user.update(superA);
            user.syncroFields(user);

            // Super-A can change the role of an administrato to user;
            // SuperA can read the role of an user
            Logger.detail("------------ Super-A can change the role of an administrato to user; ------------");
            admin.setRole(User.USER_ROLE_USER);
            admin.update(superA);
            admin.syncroFields(admin);
            r = admin.getRole();
            if (0 != r.compareTo(User.USER_ROLE_USER)) {
                throw new TestException();
            }

            // Revert the role to Administrator
            admin.setRole(User.USER_ROLE_ADMIN);
            admin.update(superA);
            admin.syncroFields(superA);

            // User can not change its role role to Administrator
            // create the TLV with the role
            Logger.detail("------------ User can not change its role to a Administrator ------------");
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_ADMIN);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User can not change its role role to a Administrator";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);


            // User can not change its role role to a Super-A
            // create the TLV with the role
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_SUPERADMIN);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User can not change its role role to a Super-A";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);

            // Administrator can not change his role to Super-A
            // create the TLV with the role
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_SUPERADMIN);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Administrator can not change his role to Super-A";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);

            // Administrator can not change user role
            // create the TLV with the role
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_SUPERADMIN);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Administrator can not change his role to Super-A";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);

            // Super A can not change his role to Administrator
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_ADMIN);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Super A can not change his role to Administrator";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);

            // Invalid Role Value are not accepted
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ROLE, "35");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFF_INCORRECT_ROLE_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Invalid Role Value are not accepted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

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

    DESCRIPTION: It is not possible change the remote authentication object 
                            and local authentication object

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase08() throws TestException {
        String expectedRes;
        String authenticator;
        Apdu apduObject;

        Command c = new Command();

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "regnoDeiCattivi");
            // object personalized
            admin.updateKey("giiancattivo");
            admin.syncroFields(superA);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "porello");
            // object personalized
            user.updateKey("giiancattivino");
            user.syncroFields(user);

            // It is not possible modifying the RemoteAuthenticator Id of an User simulating the super A;
            // it is possible read the super administrator Local Authenticator by Super-A
            authenticator = superA.getRemoteAuthenticatorId();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_REAUTHID, authenticator);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "It is not possible modifying the RemoteAuthenticator Id of an User by an User";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
            if (0 == authenticator.compareTo(user.getRemoteAuthenticatorId())) {
                throw new TestException();
            }

            // It is not possible modifying the RemotelAuthenticator Id of Administrator
            // so to impersonate a super A
            // it is possible read the super administrator Local Authenticator by and User
            authenticator = superA.getRemoteAuthenticatorId();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_REAUTHID, authenticator);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "It is not possible modifying the RemoteAuthenticator Id of an admin by an admin";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);
            if (0 == authenticator.compareTo(admin.getRemoteAuthenticatorId())) {
                throw new TestException();
            }

            authenticator = user.getRemoteAuthenticatorId();

            // It is not possible modifying the Remote authenticator Id of the Super-A by the Super-A itself
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_REAUTHID, authenticator);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_9000_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "It is not possible modifying the LocalAuthenticator Id of the Super-A by the Super-A itself";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);
            if (0 == authenticator.compareTo(superA.getRemoteAuthenticatorId())) {
                throw new TestException();
            }

            // It is not possible modifying the local Authenticator Id of an admin
            // so to impersonate the Super A
            // User can read the remoteAuthenticator Id of the Super-A
            authenticator = superA.getLocalAuthenticatorId();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ACAUTHID, authenticator);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "It is not possible modifying the Local Authenticator Id of tan admin by an admin";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);
            if (0 == authenticator.compareTo(admin.getRemoteAuthenticatorId())) {
                throw new TestException();
            }

            // It is not possible modifying the local Authenticator Id of an user
            // so to impersonate the Super A
            // Admin can read the localAuthenticator Id of the Super-A
            authenticator = superA.getLocalAuthenticatorId();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ACAUTHID, authenticator);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "It is not possible modifying the Local Authenticator Id of user by an user";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
            if (0 == authenticator.compareTo(user.getRemoteAuthenticatorId())) {
                throw new TestException();
            }

            // It is not possible modifying the LocalAuthenticator Id of the Super-A by the super A
            authenticator = user.getLocalAuthenticatorId();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_USER_ACAUTHID, authenticator);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_9000_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "It is not possible modifying the Local Authenticator Id of the Super-A by the super A";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);
            if (0 == authenticator.compareTo(superA.getRemoteAuthenticatorId())) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

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

    DESCRIPTION: Test of setStatus

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase09() throws TestException {
        String expectedRes;
        String r;

        Command c = new Command();

        Apdu apduObject;

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
            User admin = new User(superA, User.USER_ROLE_ADMIN, "farinacci");
            // object personalized
            admin.updateKey("giiancattivo");
            admin.syncroFields(admin);

            // object created
            User admin1 = new User(superA, User.USER_ROLE_ADMIN, "ciano");
            // object personalized
            admin1.updateKey("galeazzo");
            admin1.syncroFields(admin1);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "balbo");
            // object personalized
            user.updateKey("cesare");
            user.syncroFields(user);

            User user1 = new User(admin, User.USER_ROLE_USER, "croce");
            // object personalized
            user1.updateKey("benedetto");
            user1.syncroFields(user1);

            // Super A can change the Status of an user. User can read its status.
            Logger.detail("------------ Super A can change the Status of an user ------------");
            user.setStatus(User.STATUS_INACTIVE);
            user.update(superA);
            user.syncroFields(user);
            r = user.getStatus();
            if (0 != r.compareTo(User.STATUS_INACTIVE)) {
                throw new TestException();
            }

            // Revert the Status to user
            user.setStatus(User.STATUS_ACTIVE);
            user.update(superA);
            user.syncroFields(user);

            // Administrator can change the Status of an user and can raed it
            Logger.detail("------------ Administrator can change the Status of an user ------------");
            user.setStatus(User.STATUS_INACTIVE);
            user.update(admin);
            user.syncroFields(user);
            r = user.getStatus();
            if (0 != r.compareTo(User.STATUS_INACTIVE)) {
                throw new TestException();
            }

            // Revert the Status to user
            user.setStatus(User.STATUS_ACTIVE);
            user.update(admin);
            user.syncroFields(admin);


            // Super-A can change the Status of an administrator and can read it
            admin.setStatus(User.STATUS_INACTIVE);
            admin.update(superA);
            admin.syncroFields(superA);
            Logger.detail("------------ Super-A can change the Status of an administrator and can read it  ------------");
            r = admin.getStatus();
            if (0 != r.compareTo(User.STATUS_INACTIVE)) {
                throw new TestException();
            }
            // Revert the Status to Administrator
            admin.setStatus(User.STATUS_ACTIVE);
            admin.update(superA);
            admin.syncroFields(superA);


            // User can not change Status of a Administrator
            Logger.detail("------------ User can not change Status of a Administrator ------------");
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_STATUS, User.STATUS_INACTIVE);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User can not change its Status to a Administrator";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
            admin.syncroFields(admin);
            r = admin.getStatus();
            if (0 != r.compareTo(User.STATUS_ACTIVE)) {
                throw new TestException();
            }

            // User can not change Status of a SuperA
            Logger.detail("------------ User can not change Status of a superA ------------");
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_STATUS, User.STATUS_INACTIVE);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User can not change Status of a SuperA";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
            superA.syncroFields(superA);
            r = superA.getStatus();
            if (0 != r.compareTo(User.STATUS_ACTIVE)) {
                throw new TestException();
            }


            // Admin can not change Status of a SuperA
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_STATUS, User.STATUS_INACTIVE);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User can not change its Status to a Administrator";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);
            superA.syncroFields(superA);
            r = superA.getStatus();
            if (0 != r.compareTo(User.STATUS_ACTIVE)) {
                throw new TestException();
            }
            // Admin can not change Status of another Admin
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_STATUS, User.STATUS_INACTIVE);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            Logger.detail("------------ Admin can not change Status of another Admin ------------");

            // send command
            c.description = "User can not change its Status to a Administrator";
            c.requester = admin1;
            c.execute(apduObject.toString(), expectedRes);
            superA.syncroFields(superA);
            r = admin.getStatus();
            if (0 != r.compareTo(User.STATUS_ACTIVE)) {
                throw new TestException();
            }


            // Admin can not change its Status
            Logger.detail("------------ Admin can not change its Status  ------------");
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_STATUS, User.STATUS_INACTIVE);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Admin can not change its Status";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);
            admin.syncroFields(admin);
            r = admin.getStatus();
            if (0 != r.compareTo(User.STATUS_ACTIVE)) {
                throw new TestException();
            }

            // User can not change its Status
            Logger.detail("------------ User can not change its Status ------------");
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_STATUS, User.STATUS_INACTIVE);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User can not change its Status";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
            user.syncroFields(admin);
            r = user.getStatus();
            if (0 != r.compareTo(User.STATUS_ACTIVE)) {
                throw new TestException();
            }

            // User can not change the status of another user
            Logger.detail("------------ User can not change its Status ------------");
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_STATUS, User.STATUS_INACTIVE);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User can not change its Status";
            c.requester = user1;
            c.execute(apduObject.toString(), expectedRes);
            user.syncroFields(admin);
            r = user.getStatus();
            if (0 != r.compareTo(User.STATUS_ACTIVE)) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

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

    DESCRIPTION:
        •	Super A can change User Name; User can read its name; Super A can read user name.
        • 	Admin can change User Name; Admin can read user name; User can read its name;
        •	Admin can not change Admin Name; Admin can read admin Name; Super A can read Admin Name;
        •	Super-A can change Admin Name; Admin can read its name;
        •	Admin can change Its Name.
        •	User Can not change administrator Name
        •	User Can not change Super-A Name
        •	Administrator can not change Super-A Name

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase10() throws TestException {
        String expectedRes;
        String r, name, prename;

        Command c = new Command();

        Apdu apduObject;

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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "regnoDeiCattivi");
            // object personalized
            admin.updateKey("giiancattivo");
            admin.syncroFields(admin);

            // object created
            User admin1 = new User(superA, User.USER_ROLE_ADMIN, "reso");
            // object personalized
            admin1.updateKey("gatto");
            admin1.syncroFields(admin1);

            // object created
            User user = new User(admin, User.USER_ROLE_USER, "regnodelterrore");
            // object personalized
            user.updateKey("getto");
            user.syncroFields(user);

            // object created
            User user1 = new User(admin, User.USER_ROLE_USER, "regnodelterrone");
            // object personalized
            user1.updateKey("metto");
            user1.syncroFields(user1);

            // Super A can change User Name; User can read its name; Super A can read user name.
            Logger.detail("------------ Super A can change User Name; User can read its name; Super A can read user name. ------------");
            name = "0123456789ABCDEF0123456789ABCDEF"; // name shall be 32 Char
            prename = user.getName();

            user.setName(name);
            user.update(superA);

            r = user.getName();
            if (0 != r.compareTo(name)) {
                throw new TestException();
            }
            user.setName(prename);
            user.update(superA);

            // Admin can change User Name; Admin can read user name; User can read its name;
            Logger.detail("------------ SAdmin can change User Name; Admin can read user name; User can read its name; ------------");
            name = "FEDCBA9876543210FEDCBA9876543210"; // name shall be 32 Char
            prename = user.getName();
            user.setName(name);
            user.update(admin);

            r = user.getName();
            if (0 != r.compareTo(name)) {
                throw new TestException();
            }
            user.setName(prename);
            user.update(admin);

            // User Can not change administrator Name
            Logger.detail("------------ User Can not change administrator Name  ------------");
            // create the TLV with the Status
            name = "3030303030303030";
            prename = admin.getName();
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User Can not change administrator Name";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
            admin.syncroFields(admin);
            r = admin.getName();
            if (0 != r.compareTo(prename)) {
                throw new TestException();
            }

            // User Can not change Super-A Name
            Logger.detail("------------ User Can not change Super-A Name  ------------");
            // create the TLV with the Status
            name = "31"; // name shall be 32 Char
            prename = superA.getName();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "User Can not change Super-A Name";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);
            superA.syncroFields(superA);
            r = superA.getName();
            if (0 != r.compareTo(prename)) {
                throw new TestException();
            }

            // Admin Can not change Super-A Name
            Logger.detail("------------ Admin Can not change Super-A Name  ------------");
            // create the TLV with the Status
            name = "31333234"; // name shall be 32 Char
            prename = superA.getName();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Admin Can not change Super-A Name";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);
            superA.syncroFields(superA);
            r = superA.getName();
            if (0 != r.compareTo(prename)) {
                throw new TestException();
            }

            // Admin Can not change Admin Name
            Logger.detail("------------ Admin Can not change Admin Name  ------------");
            // create the TLV with the Status
            name = "1234567890"; // name shall be 32 Char
            prename = admin.getName();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Admin Can not change Admin Name";
            c.requester = admin1;
            c.execute(apduObject.toString(), expectedRes);
            admin.syncroFields(admin);
            r = admin.getName();
            if (0 != r.compareTo(prename)) {
                throw new TestException();
            }

            // Admin Can not change its Name
            Logger.detail("------------ Admin Can not change its Name  ------------");
            // create the TLV with the Status
            name = "12345678"; // name shall be 32 Char
            prename = admin.getName();

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Admin Can not change its Name";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);
            admin.syncroFields(admin);
            r = admin.getName();
            if (0 != r.compareTo(prename)) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            admin1.delete(superA);
            user1.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

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

     DESCRIPTION: Test of update keys command invalid cases;

     Security Level: None

     ------------------------------------------------------------------------------*/
    public static void testCase11() throws TestException {
        String expectedRes, name;
        Apdu apduObject;

        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 11";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            name = "3031323334353637383930313233343536373839303132333435363738393031";

//#
//# Super A can not do any other action after creation then updating the keys
//# Test cancelled
 /*               Logger.detail("-- SuperA can not do any other action after creation then updating the keys --");
                // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

                apduObject = new Apdu(Apdu.UPDATE_USER_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
                apduObject.addTlv(Atlv.DATA_TAG_NAME, name);    

                    // send command
                c.description = "No action possible if not update keys";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);

                    // object personalized
                superA.updateKey("admin");*/

//#
//# Super A can reperso the keys
//#
            Logger.detail("-- SuperA can reporso the keys--");
            superA.updateKey("adminScaltro");
//#
//# Administrator can not do any other command after creation then updating the keys
//#
            Logger.detail("-- Administrator can not do any other action after creation then updating the keys --");
            // instantiate a local User object for the SUPER-A
            User admin = new User(superA, User.USER_ROLE_ADMIN, "administrator");

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            admin.updateKey("rocco");
//#
//# Admin can reperso the keys
//#
            Logger.detail("-- Admin can reporso the keys--");
            admin.updateKey("riccasecca");

//#
//# User can not do any other command after creation then updating the keys
//#
            Logger.detail("-- Administrator can not do any other action after creation then updating the keys --");
            // instantiate a local User object for the SUPER-A
            User user = new User(superA, User.USER_ROLE_USER, "riccitto");

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            user.updateKey("Barocco");
//#
//#  User can reperso the keys
//#                       
            Logger.detail("-- User can reporso the keys--");
            user.updateKey("rapido");
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase12
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test of update and reset keys command; Valid cases.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase12() throws TestException {
        String expectedRes, name;
        Apdu apduObject;

        Command c = new Command();
        String testCode = testBatch + "/" + "Test Case 12";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);


        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            name = "3031323334353637383930313233343536373839303132333435363738393031";

//#
//# Super A can reset the keys of an user; User RAO object in Perso state. User can reinitialize the keys.
//#
            Logger.detail("-- Super A can reset the keys of an user; User RAO object in Perso state. User can reinitialize the keys. --");
            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            User user = new User(superA, User.USER_ROLE_USER, "riccitto");
            // object personalized
            user.updateKey("Barocco");

            user.resetKey(superA, "ribrezza");

            // User can not issue any command until key is personalized.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            user.updateKey("Barocco");

            // user can get his name after key personalization
            user.syncroFields(user);

            name = user.getName();

            Logger.detail("User Name: " + name + " ----");
//#
//# Super A can reset the keys of an user; User RAO object in Create state
//#
            Logger.detail("-- Super A can reset the keys of an user; User RAO object in Create state --");

            User user1 = new User(superA, User.USER_ROLE_USER, "riccitto");

            user1.resetKey(superA, "ribrezza");

            // User can not issue any command until key is personalized.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user1.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = user1;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            user1.updateKey("Barocco");

            // user can get his name after key personalization
            user1.syncroFields(user1);
            name = user1.getName();

            Logger.detail("User Name: " + name + " ----");

//#
//# Super A can reset the keys of an administrator; User RAO object in Create state                       
//#
            // object personalized
            Logger.detail("-- Super A can reset the keys of an Admin; Admin RAO object in Create state --");

            User admin = new User(superA, User.USER_ROLE_ADMIN, "riccitto");

            admin.resetKey(superA, "ribrezza");

            // User can not issue any command until key is personalized.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            admin.updateKey("Barocco");

            // user can get his name after key personalization
            admin.syncroFields(admin);
            name = admin.getName();

            Logger.detail("User Name: " + name + " ----");

//#
//# Super A can ping the keys of an administrator; User RAO object in Perso state
//# 
            // object personalized
            Logger.detail("-- Super A can reset the keys of an Admin; Admin RAO object in Perso state --");

            admin.resetKey(superA, "ribrezza");

            // User can not issue any command until key is personalized.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            admin.updateKey("Barocco");

            // user can get his name after key personalization
            admin.syncroFields(admin);
            name = admin.getName();

            Logger.detail("User Name: " + name + " ----");

//#
//# Administrator can reset the keys of an user; User RAO object in Create state
//#
            Logger.detail("-- Admin can reset the keys of an user; User RAO object in Create state --");

            User user2 = new User(superA, User.USER_ROLE_USER, "riccitto");

            user2.resetKey(admin, "ribrezza");

            // User can not issue any command until key is personalized.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user2.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = user2;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            user2.updateKey("Barocco");

            // user can get his name after key personalization
            user2.syncroFields(user2);
            name = user2.getName();

            Logger.detail("User Name: " + name + " ----");

//#
//# Administrator can reset the keys of an user; User RAO object in Create state
//#
            Logger.detail("-- Admin can reset the keys of an user; User RAO object in Create state --");

            user2.resetKey(admin, "ribrezza");

            // User can not issue any command until key is personalized.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user2.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = user2;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            user2.updateKey("Barocco");

            // user can get his name after key personalization
            user2.syncroFields(user2);
            name = user2.getName();

            Logger.detail("User Name: " + name + " ----");
//#
//# Administrator can ping the keys of an Administrator; Admin RAO object in Create state
//#
            Logger.detail("-- Admin can ping the keys of an user; User RAO object in Create state --");

            User admin2 = new User(superA, User.USER_ROLE_USER, "riccitto");

            admin2.resetKey(admin, "ribrezza");

            // User can not issue any command until key is personalized.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin2.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = admin2;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            admin2.updateKey("Barocco");

            // user can get his name after key personalization
            admin2.syncroFields(admin2);
            name = admin2.getName();

            Logger.detail("User Name: " + name + " ----");

//#
//# Administrator can reset the keys of an Administrator; Administrator RAO object in Perso state
//#
            Logger.detail("-- Admin can reset the keys of an Administrator; Administrator RAO object in Perso state --");

            admin2.resetKey(admin, "ribrezza");

            // User can not issue any command until key is personalized.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin2.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "No action possible if not update keys";
            c.requester = admin2;
            c.execute(apduObject.toString(), expectedRes);

            // object personalized
            admin2.updateKey("Barocco");

            // user can get his name after key personalization
            admin2.syncroFields(admin2);
            name = admin2.getName();

            Logger.detail("User Name: " + name + " ----");
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            admin2.delete(superA);
            user1.delete(superA);
            user2.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase13
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test of Rest keys command invalid cases;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase13() throws TestException {
        String expectedRes;
        Apdu apdu;

        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 13";
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
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            User user = new User(superA, User.USER_ROLE_USER, "riccitto");
            // object personalized
            user.updateKey("usera");
            user.syncroFields(user);


            User user1 = new User(superA, User.USER_ROLE_USER, "riccitto");
            // object personalized
            user1.updateKey("userb");
            user1.syncroFields(user1);

            User admin = new User(superA, User.USER_ROLE_ADMIN, "riccitto");
            // object personalized
            admin.updateKey("usera");
            admin.syncroFields(admin);

//#
//# User can not reset Super A Keys
//#
            Logger.detail("-- User can not reset Super A Keys --");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.RESET_USER_KEY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apdu.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            // send command
            c.description = "User can not reset Super A Keys";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);

//#
//# Administrator can not reset Super A Keys
//#
            Logger.detail("-- Administrator can not reset Super A Keys --");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.RESET_USER_KEY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apdu.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            // send command
            c.description = "Administrator can not reset Super A Keys";
            c.requester = admin;
            c.execute(apdu.toString(), expectedRes);

//#
//# Administrator can not reset its Keys
//#                        

            Logger.detail("-- Administrator can not reset its Keys --");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.RESET_USER_KEY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apdu.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            // send command
            c.description = "Administrator can not reset its Keys";
            c.requester = admin;
            c.execute(apdu.toString(), expectedRes);

//#
//# User can not reset administrator Keys 
//#
            Logger.detail("-- Administrator can not reset Super A Keys --");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.RESET_USER_KEY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apdu.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            // send command
            c.description = "Administrator can not reset Super A Keys";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);

//#
//# User can not reset User Keys 
//#                                                
            Logger.detail("-- User can not reset User Keys  --");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.RESET_USER_KEY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, user1.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apdu.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            // send command
            c.description = "User can not reset User Keys ";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);
//#
//# User can not reset its Keys 
//#
            Logger.detail("-- User can not reset its Keys  --");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.RESET_USER_KEY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_RAO_KIC, "000102030405060708090A0B0C0D0E0F");
            apdu.addTlv(Atlv.DATA_TAG_RAO_KID, "000102030405060708090A0B0C0D0E0F");

            // send command
            c.description = "User can not reset its Keys ";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            user1.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase014
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: It test that after update Key command the log is updated.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase14() throws TestException {
        int index;
        LogEntry le;
        String a;

        String testCode = testBatch + "/" + "Test Case 14";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

//#
//# Log is updated with update key and ping key commands
//#

            Logger.detail("-- Log is updated with update key and ping key commands  --");
            // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            // object personalized
            admin.updateKey("adminx");
            admin.syncroFields(admin);
            // object created
            User user = new User(admin, User.USER_ROLE_USER, "s[atter");

            index = DeviceLogger.getLastEntryIndex(superA);

            // get the index;
            index = (index + 1) & (0x0FFF);
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != "FFFFFFFF".compareTo(le.requesterId)) ||
                    (0 != "FFFFFFFF".compareTo(le.objectId)) ||
                    (0 != "FFFF".compareTo(le.event)) ||
                    (0 != "FFFF".compareTo(le.result)) ||
                    (0 != "FFFFFFFF".compareTo(le.timestamp))) {
                throw new TestException();
            }

            // object personalized
            admin.updateKey("newAdministratorKeysSutta");

            // Check that the event is logged
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != admin.getObjectId().compareTo(le.requesterId)) ||
                    (0 != admin.getRemoteAuthenticatorId().compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_UPDATE_KEY.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }

            // increase the index
            index = (index + 1) & (0x0FFF);
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != "FFFFFFFF".compareTo(le.requesterId)) ||
                    (0 != "FFFFFFFF".compareTo(le.objectId)) ||
                    (0 != "FFFF".compareTo(le.event)) ||
                    (0 != "FFFF".compareTo(le.result)) ||
                    (!"FFFFFFFF".equals(le.timestamp))) {
                throw new TestException();
            }

            // object created
            // object personalized
            user.updateKey("newAdministratorKeysSatta");

            // Check that the event is logged
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != user.getObjectId().compareTo(le.requesterId)) ||
                    (0 != user.getRemoteAuthenticatorId().compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_UPDATE_KEY.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }

            // increase the index
            index = (index + 1) & (0x0FFF);
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != "FFFFFFFF".compareTo(le.requesterId)) ||
                    (0 != "FFFFFFFF".compareTo(le.objectId)) ||
                    (0 != "FFFF".compareTo(le.event)) ||
                    (0 != "FFFF".compareTo(le.result)) ||
                    (0 != "FFFFFFFF".compareTo(le.timestamp))) {
                throw new TestException();
            }

            a = user.getRemoteAuthenticatorId();
            // ping key
            user.resetKey(superA, "newAdministratorKeysSatta");

            // Check that the event is logged
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != superA.getObjectId().compareTo(le.requesterId)) ||
                    (0 != a.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_RESET_KEY.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }

            // increase the index
            index = (index + 1) & (0x0FFF);
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != "FFFFFFFF".compareTo(le.requesterId)) ||
                    (0 != "FFFFFFFF".compareTo(le.objectId)) ||
                    (0 != "FFFF".compareTo(le.event)) ||
                    (0 != "FFFF".compareTo(le.result)) ||
                    (0 != "FFFFFFFF".compareTo(le.timestamp))) {
                throw new TestException();
            }

            a = admin.getRemoteAuthenticatorId();

            // ping Key
            admin.resetKey(superA, "newAdministratorKeysSatta");

            // Check that the event is logged
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != superA.getObjectId().compareTo(le.requesterId)) ||
                    (0 != a.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_RESET_KEY.compareTo(le.event)) ||
                    (0 != Apdu.SW_9000_STRING.compareTo(le.result))) {
                throw new TestException();
            }

            // increase the index
            index = (index + 1) & (0x0FFF);
            // check that the log entry is empty
            le = DeviceLogger.getEntry(superA, index);
            if ((0 != "FFFFFFFF".compareTo(le.requesterId)) ||
                    (0 != "FFFFFFFF".compareTo(le.objectId)) ||
                    (0 != "FFFF".compareTo(le.event)) ||
                    (0 != "FFFF".compareTo(le.result)) ||
                    (0 != "FFFFFFFF".compareTo(le.timestamp))) {
                throw new TestException();
            }
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | TestException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
     testCase015
     --------------------------------------------------------------------------
     AUTHOR:	PDI

     DESCRIPTION: It test the delete user. Valid test case;

     Security Level: None

     ------------------------------------------------------------------------------*/
    public static void testCase15() throws TestException {
        String expectedRes, name, raoId, authId;
        Apdu apduObject;

        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 15";
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
            superA.updateKey("superAadmin");

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.updateKey("adminx");
            admin.syncroFields(admin);

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.updateKey("newAdministratorKeysSutta");
            user.syncroFields(user);

            // object created
            User admin1 = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin1.updateKey("adminx");
            admin1.syncroFields(admin1);

            // object created
            User admin2 = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin2.updateKey("adminx");
            admin2.syncroFields(admin2);

            User user1 = new User(admin, User.USER_ROLE_USER, "s[atter");
            user1.updateKey("newAdministratorKeysSutta");
            user1.syncroFields(user1);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            name = "3031323334353637383930313233343536373839303132333435363738393031";

//#
//# Super A can delete user object
//#
            Logger.detail("-- Super A can delete user object  --");

            raoId = user.getRemoteAuthenticatorId();
            authId = user.getLocalAuthenticatorId();

            user.delete(superA);

            // try to get user name.
            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "User object is deleted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);

            // try to read  RAO object.
            apduObject = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, raoId);

            // send command
            c.description = "Local Authenticator object is deleted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);

            // try to read local authenticator object.
            apduObject = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, authId);

            // send command
            c.description = "Rao object is deleted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);
//#
//# Super A can delete administrator object
//#
            Logger.detail("-- Super A can delete administrator object  --");

            raoId = admin.getRemoteAuthenticatorId();
            authId = admin.getLocalAuthenticatorId();

            admin.delete(superA);

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "object is deleted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);

            // try to reat  RAO object.
            apduObject = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, raoId);

            // send command
            c.description = "Local Authenticator object is deleted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);

            // try to read local authenticator object.
            apduObject = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, authId);

            // send command
            c.description = "Rao object is deleted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);
//#
//# Administrator can delete user object
//#
            Logger.detail("-- Administrator can delete user object  --");

            raoId = user1.getRemoteAuthenticatorId();
            authId = user1.getLocalAuthenticatorId();

            user1.delete(admin1);

            apduObject = new Apdu(Apdu.UPDATE_USER_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user1.getObjectId());
            apduObject.addTlv(Atlv.DATA_TAG_NAME, name);

            // send command
            c.description = "object is deleted";
            c.requester = admin1;
            c.execute(apduObject.toString(), expectedRes);

            // try to reat  RAO object.
            apduObject = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, raoId);

            // send command
            c.description = "Local Authenticator object is deleted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);

            // try to read local authenticator object.
            apduObject = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, authId);

            // send command
            c.description = "Rao object is deleted";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);
//#
//# Administrator can not delete Administrator object
//#
            Logger.detail("-- Administrator can not delete Administrator  --");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            String objectId = admin1.getObjectId();

            apduObject = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin1.getObjectId());

            // send command
            c.description = "object Deletion";
            c.requester = admin2;
            c.execute(apduObject.toString(), expectedRes);

            admin1.syncroFields(admin2);
            if (0 != objectId.compareTo(admin1.getObjectId()))
                throw new TestException();
//#
//# Object deletion
//#                        
            admin1.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase016
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: It test the delete user. Invalid test case

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase16() throws TestException {
        String expectedRes, objectId;
        Apdu apduObject;

        Command c = new Command();

        String testCode = testBatch + "/" + "Test Case 16";
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
            superA.updateKey("superAadmin");

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.updateKey("adminx");
            admin.syncroFields(admin);

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.updateKey("newAdministratorKeysSutta");
            user.syncroFields(user);

            User user1 = new User(admin, User.USER_ROLE_USER, "s[atter");
            user1.updateKey("newAdministratorKeysSutta");
            user1.syncroFields(user1);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

//#
//# User can not delete SuperA
//#
            Logger.detail("-- User can not delete Super A  --");

            objectId = superA.getObjectId();

            apduObject = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());

            // send command
            c.description = "object Deletion";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);

            if (0 != objectId.compareTo(superA.getObjectId()))
                throw new TestException();
//#
//# User can not delete Administrator
//#
            Logger.detail("-- User can not delete Admin --");

            objectId = admin.getObjectId();

            apduObject = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, admin.getObjectId());

            // send command
            c.description = "object Deletion";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);

            if (0 != objectId.compareTo(admin.getObjectId()))
                throw new TestException();
//#
//# User can not delete User
//#
            Logger.detail("-- User can not delete User --");

            objectId = user1.getObjectId();

            apduObject = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, user1.getObjectId());

            // send command
            c.description = "object Deletion";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);

            if (0 != objectId.compareTo(user1.getObjectId()))
                throw new TestException();
//#
//# Administrator can not delete SuperA
//#
            Logger.detail("-- Administrator can not delete Super A --");

            objectId = superA.getObjectId();

            apduObject = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());

            // send command
            c.description = "object Deletion";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);

            if (0 != objectId.compareTo(superA.getObjectId()))
                throw new TestException();
//#
//# Object deletion
//#
            admin.delete(superA);
            user.delete(superA);
            user1.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | TestException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
testCase17
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Device Id is associated with the SuperA.
                 Active device in IoStream is set accordingly and
                 commands are succesfully executed.

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase17() throws TestException {
        String a, name;

        String testCase = testBatch + "/" + "Test Case 17";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // ping shall not be sent;

            // instantiate a local User object for the SUPER-A
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);
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
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
    testCase18
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Super-A creates User object.
                     Device Id is associated with the SuperA and created objects.
                     Active device in IoStream is set accordingly and
                     commands are succesfully executed.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase18() throws TestException {
        String a, name;

        String testCode = testBatch + "/" + "Test Case 18";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCode);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------

        try {
            Logger.testCase(testCode);


            // ping shall not be sent;

            // instantiate a local User object for the SUPER-A
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            //#
            //# Creation of an User by an Admin
            //# Result: OK

            // object created
            User user = new User(superA, User.USER_ROLE_USER, "newUser");
            // object personalized
            user.updateKey("newUserKeys");
            user.syncroFields(user);

            // Role is user
            Logger.detail("------------ Check Role ------------");
            a = user.getRole();
            if (0 != a.compareTo(User.USER_ROLE_USER))
                throw new ObjectException();

            // Status is active
            Logger.detail("------------ Check Status ------------");
            a = user.getStatus();
            if (0 != a.compareTo(User.STATUS_ACTIVE))
                throw new ObjectException();

            // Security Domain is the initial security domain
            Logger.detail("------------ Check Security Domain ------------");
            a = user.getSecurityDomain();
            if (0 != a.compareTo(SecurityDomain.INITIAL_SD_ID))
                throw new ObjectException();

            // Remote authentication object has been created
            Logger.detail("------------ Check that remote authenticator is created ------------");
            RemoteAuthenticator rAuth = user.getRemoteAuthenticatorObject();
            rAuth.syncroFields(user);
            a = rAuth.getOwnerID();
            if (0 != a.compareTo(user.getObjectId()))
                throw new ObjectException();

            // Local Authentication object has been created
            Logger.detail("------------ Check that local authenticator is created ------------");
            LocalAuthenticator lAuth = user.getLocalAuthenticatorObject();
            lAuth.syncroFields(user);
            a = lAuth.getOwnerID();
            if (0 != a.compareTo(user.getObjectId()))
                throw new ObjectException();

            // AC Policy is the default policy
            Logger.detail("------------ Check that access policy is the default one ------------");
            a = user.getAcPolicy();
            if (0 != a.compareTo(AccessPolicy.DEFAULT_ACCESS_POLICY_ID))
                throw new ObjectException();

            // Notification Policy is the default policy
            Logger.detail("------------ Check that notification policy is the default one ------------");
            a = user.getNotificationPolicy();
            if (0 != a.compareTo(NotificationPolicy.INITIAL_NOTIFICATION_OBJECT_ID))
                throw new CommandErrorException();

            // The Name is the initial name
            Logger.detail("------------ Check that name is the default one ------------");
            a = user.getName();
            name = user.getObjectId();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();
            //#
            //# Object deletion
            //#
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }

    /*----------------------------------------------------------------------------
testCase19
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: User read policy

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase19() throws TestException {
        String testCase = testBatch + "/" + "Test Case 19";
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        String expectedRes;
        Apdu apdu;
        Command c = new Command();

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            //   IoStream.resetActiveDevice();

            // ping shall not be sent;

            // instantiate a local User object for the SUPER-A
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.updateKey("adminx");
            admin.syncroFields(admin);

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.updateKey("newAdministratorKeysSutta");
            user.syncroFields(user);

            // object created
            User admin1 = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin1.updateKey("adminx");
            admin1.syncroFields(admin1);


            User user1 = new User(admin, User.USER_ROLE_USER, "s[atter");
            user1.updateKey("newAdministratorKeysSutta");
            user1.syncroFields(user1);


            Door door = new Door(superA);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);

            //#
            //#  User can not read the fileds of another user
            //#
            Logger.detail("-- User can not read the fileds of another user --");

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());

            // send command
            c.description = "User attempt to read the fields of another user but fail";
            c.requester = user1;
            c.execute(apdu.toString(), expectedRes);

            //#
            //#  If User is Inactive can not read the field of any object (Door or Access Policy for example)
            //#
            Logger.detail("-- If User is Inactive can not read the field of any object (Door or Access Policy for example) --");

            // change the user status to inactive
            user1.setStatus(User.STATUS_INACTIVE);
            user1.update(superA);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, door.getObjectId());

            // send command
            c.description = "Inactive user attempt to read a door and fails";
            c.requester = user1;
            c.execute(apdu.toString(), expectedRes);
            //#
            //#  If Administrator is Inactive can not read the field of any object (Door or Access Policy for example)
            //#

            Logger.detail("If Administrator is Inactive can not read the field of any object (Door or Access Policy for example)");

            // change the user status to inactive
            admin1.setStatus(User.STATUS_INACTIVE);
            admin1.update(superA);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.READ_OBJECT_BY_ID_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, door.getObjectId());

            // send command
            c.description = "Inactive administrator attempt to read a door and fails";
            c.requester = admin1;
            c.execute(apdu.toString(), expectedRes);

            //#
            //#  If Administrator is Inactive can not create users;
            //#
            Logger.detail("If Administrator is Inactive can not create an user");

            // change the user status to inactive
            admin1.setStatus(User.STATUS_INACTIVE);
            admin1.update(superA);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.CREATE_SE_USER_APDU);
            apdu.addTlv(Atlv.DATA_TAG_USER_ROLE, User.USER_ROLE_USER);
            apdu.addTlv(Atlv.DATA_TAG_RAO_KIC, "00112233445566778899AABBCCDDEEFF");
            apdu.addTlv(Atlv.DATA_TAG_RAO_KID, "00112233445566778899AABBCCDDEEFF");

            // send command
            c.description = "Inactive administrator can not create user";
            c.requester = admin1;
            c.execute(apdu.toString(), expectedRes);

            //#
            //#  If Administrator is inactive can not delete users;
            //#
            Logger.detail("If Administrator is Inactive can not delete an user");

            // change the user status to inactive
            admin1.setStatus(User.STATUS_INACTIVE);
            admin1.update(superA);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, user.getObjectId());

            // send command
            c.description = "Inactive administrator can not delete an user";
            c.requester = admin1;
            c.execute(apdu.toString(), expectedRes);
            //#
            //# Object deletion
            //#
            admin.delete(superA);
            admin1.delete(superA);
            user.delete(superA);
            user1.delete(superA);
            door.delete(superA);

            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "ok");

    }
}

