
package TestCases;
    
import com.sdk.*;
import java.io.IOException;

public class TestDeviceExtender001 {
    
  /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestDeviceExtender001";
    public static final String deviceId = MainTest.TestMain.deviceId;
    
    public static User superA;
    public static Device thisDevice;    
    
    /*----------------------------------------------------------------------------
    run
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test run method

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean run()
    {
        int j;
       // ---------------------- Code -------------------------------        
       Command.onError = Command.ALT_ON_ERROR;
       
       j = 1;
        try {
                thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET,3,2000);     
                
                superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);                
                
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
        }
        catch ( TestException |  DiscoveryException e){
            
             Logger.detail("TEST FAILURE ----->" + j);
            return false;
        }            
        
        return true;
    }
    /*----------------------------------------------------------------------------
    testCase01
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA creates a Device Extender object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException
    {
            String a, name;
            DeviceExtender deviceExt;
            Command c = new Command();
            String testCase = testBatch +" /" + "Test Case 01";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a Device Extender object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Device Extender ------------");                        
                    deviceExt = new DeviceExtender(superA, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    deviceExt.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = deviceExt.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = deviceExt.getStatus();
                    if (0 != a.compareTo( DeviceExtender.SE_DEVICE_EXTENDER_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = deviceExt.getName();			
                    name = deviceExt.getObjectId();
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // Remote User Identifier 
                    Logger.detail("------------ Check Initial RemoteUser Id ------------");
                    a = deviceExt.getRemoteUserId();
                    if (0 != a.compareTo( User.SUPER_ADM_ID))
                            throw new ObjectException();

                        // Check Security 
                    Logger.detail("------------ Check DeviceId ------------");
                    a = deviceExt.getDeviceId();
                    if (0 != a.compareTo( "0000000000000000"))
                            throw new ObjectException();
                    
                        // Check Bearer
                    Logger.detail("------------ Check Bearer ------------");
                    a = deviceExt.getBearer();
                    if (0 != a.compareTo( DeviceExtender.SE_DEVICE_EXTENDER_BEARER_RS485 ))
                            throw new ObjectException();                    

    //# Object deletion
    //#                       
                    deviceExt.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException e)
            {
                Logger.testResult(false);		
                TestException t = new TestException();
                throw t;
            }

            Logger.testCase(testCase);
            Logger.testResult(true);		
    }
    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Only SuperA can create Device Extender.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException
    {
        String  expectedRes;
        Apdu apduObject;
        Command c = new Command();               

        String testCase = testBatch +" /" + "Test Case 02";               

        // ---------------------- Code -------------------------------
        try
        {			
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
//# User can not create a Device Extender
//# 
                Logger.detail("------------ User can not create a Device Extender ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to create a door
                apduObject = new Apdu(Apdu.CREATE_SE_DEVICE_EXTENDER_APDU );
		apduObject.addTlv(Atlv.DATA_TAG_RAO_KIC, "00000000000000000000000000000000");
		apduObject.addTlv(Atlv.DATA_TAG_RAO_KID, "00000000000000000000000000000000");  
                
                    // send command
                c.description = "Create Device Extender";
                c.requester = user;
                c.execute(apduObject.toString(), expectedRes);                         
//#
//# Administrator can not create a Device Extender
//#
                Logger.detail("------------ Administrator can not create a Device Extender ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to create a door
                apduObject = new Apdu(Apdu.CREATE_SE_DEVICE_EXTENDER_APDU );
		apduObject.addTlv(Atlv.DATA_TAG_RAO_KIC, "00000000000000000000000000000000");
		apduObject.addTlv(Atlv.DATA_TAG_RAO_KID, "00000000000000000000000000000000"); 

                    // send command
                c.description = "Create Device Extender";
                c.requester = admin;
                c.execute(apduObject.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

        }
        catch (CommandErrorException | ObjectException | IOException e)
        {
            Logger.testResult(false);		
            TestException t = new TestException();
            throw t;
        }

        Logger.testCase(testCase);
        Logger.testResult(true);		
    }
    /*----------------------------------------------------------------------------
    testCase03
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Readeable Fields can be read by all user object despite their role

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException
    {
            String a ;
            DeviceExtender deviceExtender;
            Command c = new Command();                
            String testCase = testBatch +" /" + "Test Case 03";               

            // ---------------------- Code -------------------------------
            try
            {

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
                    Logger.detail("------------ Create Door ------------");                        
                    deviceExtender = new DeviceExtender(superA, "pipolo");
                    deviceExtender.syncroFields(superA);
    //#
    //# Super A can read the status field of a Device Extender
    //#
                    Logger.detail("-- Super A can read the status field of a Device Extender  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    deviceExtender.syncroFields(superA);
                    a = deviceExtender.getStatus();
                    if (0 != a.compareTo( DeviceExtender.SE_DEVICE_EXTENDER_ENABLED ))
                            throw new ObjectException();                        

    //#
    //# Administrator can read the status field of a Device Extender
    //#
                    Logger.detail("-- Administrator can read the status field of a Device Extender  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    deviceExtender.syncroFields(admin);
                    a = deviceExtender.getStatus();
                    if (0 != a.compareTo( DeviceExtender.SE_DEVICE_EXTENDER_ENABLED ))
                            throw new ObjectException();                        
    //#
    //# User can read the status field of a Device Extender
    //#
                    Logger.detail("-- User can read the status field of a Device Extender  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    deviceExtender.syncroFields(user);
                    a = deviceExtender.getStatus();
                    if (0 != a.compareTo( DeviceExtender.SE_DEVICE_EXTENDER_ENABLED ))
                            throw new ObjectException(); 
    //#
    //# Object deletion
    //#                       
                    admin.delete(superA);
                    admin1.delete(superA);
                    user.delete(superA);
                    user1.delete(superA);
                    deviceExtender.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            }
        catch (CommandErrorException | ObjectException | IOException e)
        {
            Logger.testResult(false);		
            TestException t = new TestException();
            throw t;
        }

        Logger.testCase(testCase);
        Logger.testResult(true);
    }
    /*----------------------------------------------------------------------------
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can updated the fileds of a Device Extender. User and Admin not.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException
    {
            String a, expectedRes;
            DeviceExtender deviceExtender;
            Command c = new Command();

            String testCode = testBatch+"/"+"Test Case 04";                

            // ---------------------- Code -------------------------------
            try
            {

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
                Logger.detail("------------ Create DeviceExtender ------------");                        
                deviceExtender = new DeviceExtender(superA, "piopio");
                deviceExtender.syncroFields(superA);

//#
//# Super A can updated the field of a Device Extender
//#
                    // Status
                deviceExtender.setStatus(DeviceExtender.SE_DEVICE_EXTENDER_DIASABLED);
                deviceExtender.update(superA);
                deviceExtender.syncroFields(superA);

                Logger.detail("------------ Check Status ------------");
                a = deviceExtender.getStatus();
                if (0 != a.compareTo( DeviceExtender.SE_DEVICE_EXTENDER_DIASABLED ))
                        throw new ObjectException(); 

                    // remote user Id
                deviceExtender.setRemoteUserId(user.getObjectId());
                deviceExtender.update(superA);
                deviceExtender.syncroFields(superA);                

                Logger.detail("------------ Remote User Id ------------");
                a = deviceExtender.getRemoteUserId();
                if (0 != a.compareTo( user.getObjectId()))
                        throw new ObjectException();

                    // 
                deviceExtender.setDeviceId("0123456789ABCDEF");
                deviceExtender.update(superA);
                deviceExtender.syncroFields(superA);                

                Logger.detail("------------ Check Device Identifier ------------");
                a = deviceExtender.getDeviceId();
                if (0 != a.compareTo( "0123456789ABCDEF" ))
                        throw new ObjectException(); 

                    // name
                deviceExtender.setName("0123456789ABCDEF0123456789ABCDEF");
                deviceExtender.update(superA);
                deviceExtender.syncroFields(superA); 
                
                Logger.detail("------------ Check Name ------------");
                a = deviceExtender.getName();							
                if (0 != a.compareTo("0123456789ABCDEF0123456789ABCDEF"))
                        throw new CommandErrorException();
                
                    // bearer
                deviceExtender.setBearer(DeviceExtender.SE_DEVICE_EXTENDER_BEARER_ETHERNET);
                deviceExtender.update(superA);
                deviceExtender.syncroFields(superA); 
                
                Logger.detail("------------ Check Bearer ------------");
                a = deviceExtender.getBearer();							
                if (0 != a.compareTo(DeviceExtender.SE_DEVICE_EXTENDER_BEARER_ETHERNET))
                        throw new CommandErrorException();                 


//#
//# Administrator can not update Device Extender
//#
                Logger.detail("------------ Administrator can not update Device Estender ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                Apdu apdu = new Apdu(Apdu.UPDATE_DEVICE_EXTENDER_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, deviceExtender.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_STATUS, DeviceExtender.SE_DEVICE_EXTENDER_ENABLED);
                    // send command
                c.description = "Update Device Extender";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not update Device Extender
//#
                Logger.detail("------------ user can not update Device Extender ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.UPDATE_DEVICE_EXTENDER_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, deviceExtender.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_STATUS, DeviceExtender.SE_DEVICE_EXTENDER_ENABLED);
                    // send command
                c.description = "Update Device Extender";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                deviceExtender.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

        }
        catch (CommandErrorException | ObjectException | IOException e)
        {
            Logger.testResult(false);		
            TestException t = new TestException();
            throw t;
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
    /*----------------------------------------------------------------------------
    testCase05
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can Delete a device extender. 
                 User and administrators can not delete a device extender.

    Security Level: None
    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException
    {
            String a, expectedRes;
            DeviceExtender deviceExtender;
            Command c = new Command();

            String testCode = testBatch+"/"+"Test Case 05";                

            // ---------------------- Code -------------------------------
            try
            {

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
//# Super A can delete a Device Extender
//#
                Logger.detail("------------ Super A can delete a Device Extender ------------");

                    // Create Door
                Logger.detail("------------ Create Device Extender ------------");                        
                deviceExtender = new DeviceExtender(superA, "didimo");

                String objId = deviceExtender.getObjectId();

                deviceExtender.delete(superA);                                               

                    // check that the door has been deleted
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                Apdu apdu = new Apdu(Apdu.UPDATE_DEVICE_EXTENDER_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID,objId );
                apdu.addTlv(Atlv.DATA_TAG_STATUS, DeviceExtender.SE_DEVICE_EXTENDER_ENABLED);
                
                    // send command
                c.description = "Delete Device Extender";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);                        

//#
//# Administrator can not delete a DeviceExtender
//#
                deviceExtender = new DeviceExtender(superA, "rutelli");

                Logger.detail("------------ Administrator can not delete a DeviceExtender ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, deviceExtender.getObjectId());
                    // send command
                c.description = "Delete Device Extender";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not delete a Device Extender
//#

                Logger.detail("------------ user can not delete a Device Extender ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, deviceExtender.getObjectId());

                    // send command
                c.description = "Delete Device Extender";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                deviceExtender.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

        }
        catch (CommandErrorException | ObjectException | IOException e)
        {
            Logger.testResult(false);		
            TestException t = new TestException();
            throw t;
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
    /*----------------------------------------------------------------------------
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: DeviceId fiels can not be the same of the device

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException
    {
            String a, expectedRes;
            DeviceExtender deviceExtender;
            Command c = new Command();

            String testCode = testBatch+"/"+"Test Case 06";

            // ---------------------- Code -------------------------------
            try
            {

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
                Logger.detail("------------ Create DeviceExtender ------------");                        
                deviceExtender = new DeviceExtender(superA, "piopio");
                deviceExtender.syncroFields(superA);

//#
//# SuperA can not set Device Extender Id ans the device ID of the device under test
//#
                Logger.detail("------------ SuperA can not set Device Extender Id ans the device ID of the device under test ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AF9_INCORRECT_ID_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                Apdu apdu = new Apdu(Apdu.UPDATE_DEVICE_EXTENDER_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, deviceExtender.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_DEVICE_ID, "0000000000000B00");
                    // send command
                c.description = "Update Device Extender";
                c.requester = superA;
                c.execute(apdu.toString(), expectedRes);                         
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                deviceExtender.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

        }
        catch (CommandErrorException | ObjectException | IOException e)
        {
            Logger.testResult(false);		
            TestException t = new TestException();
            throw t;
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
    
    /*----------------------------------------------------------------------------
    testCase07
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check that Create and delete DEvice Extender updates the log.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException
    {
        DeviceExtender deviceExtender;
        int index;
        LogEntry le;
        String doorId, requestorId;
        Command c = new Command();               
        String testCode = testBatch+"/"+"Test Case 07";                

        // ---------------------- Code -------------------------------
        try
        {

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
//# Log is updated when a Device Extender is created
//#
            Logger.detail("------------ Log is updated when a Device Extender is created ------------");                        

            deviceExtender = new DeviceExtender(superA, "suite");
            deviceExtender.syncroFields(superA);

            index = DeviceLogger.getLastEntryIndex(superA);	

            requestorId = superA.getObjectId();
            doorId = deviceExtender.getObjectId();

            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_CREATE_EVENT.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result)))
                    {
                            throw new TestException();
            }                 
//#
//# Log is updated when a Device Extender is deleted;
//#
            Logger.detail("------------ Log is updated when a Device Extender is deleted; ------------");                        

            deviceExtender.delete(superA);

            index = (index + 1) & 0xFFFF;	

            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) ||
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_DELETE_EVENT.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result)))
                    {
                            throw new TestException();
                    }
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
            Logger.testResult(false);		
            TestException t = new TestException();
            throw t;
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
}
