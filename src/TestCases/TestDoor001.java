
package TestCases;
    
import com.sdk.*;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

public class TestDoor001 {
    
 /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestDoor001";    
    public static final String deviceId = MainTest.TestMain.deviceId;

    /********************************
    Public Methods
    ********************************/
    public static SuperA superA;  
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
                thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

                superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);                
                
                for (int u = 0; u<1; u++)
                {

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
                    
                  //  testCase13(); //Test wrong
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
                    
                    testCase20();                    
                    j++;
                    
                    testCase21();                    
                    j++;                   

                    testCase22();                    
                    j++;      
                    
                    testCase01();
                    j++;
                    
                    if (u == 0)
                    {
                 /*       Logger.detail("<-------------------------------->");
                        Logger.detail("<-------------------------------->"); 
                        Logger.detail("<-------------------------------->"); 
                        Logger.detail(" WIFI "); 
                        Logger.detail("<-------------------------------->"); 
                        Logger.detail("<-------------------------------->"); 
                        Logger.detail("<-------------------------------->"); 
                       // d = Device.discover(deviceId, Device.WIFI);
                        //IoStream.setActiveDevice(d);
                        j=1;*/
                    }                    
                }
        }
        catch (TestException | DiscoveryException e){
                         Logger.detail("TEST FAILURE ----->" + j);
            return false;
        }            
        
        return true;
    }
    /*----------------------------------------------------------------------------
    testCase01
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA creates a door object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException
    {
            String a, name;
            Door door;
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
    //# Super A creates a door object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Door ------------");                        
                    door = new Door(superA);
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_DIASABLED ))
                            throw new ObjectException();
                    
                        // Restore Last Output Value
                    Logger.detail("------------ Check Restore Last Output Value ------------");
                    a = door.getRestoreLastOutputValue();
                    if (0 != a.compareTo( Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_FALSE ))
                            throw new ObjectException();                    

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = door.getObjectId();
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( Door.SE_DOOR_VOID_GPIOID))
                            throw new ObjectException();

                        // Check Security 
                    Logger.detail("------------ Check Initial Security ------------");
                    a = door.getSecurity();
                    if (0 != a.compareTo( Door.SE_DOOR_SECURITY_NO_PIN))
                            throw new ObjectException();

                        // Time 
                    Logger.detail("------------ Check InitialTime Value ------------");
                    a = door.getPulseDuration();
                    if (0 != a.compareTo(Door.SE_DOOR_INITIAL_TIME))
                            throw new ObjectException();

                        // Initial Value
                    Logger.detail("------------ Check Initial Initial Value------------");
                    a = "000000" + door.getInitialOutputValue();
                    if (0 != a.compareTo( Door.SE_DOOR_STATUS_LOW))
                            throw new ObjectException();

                        // Mode                     
                    Logger.detail("------------ Mode------------");
                    a = door.getMode();
                    if (0 != a.compareTo( Door.SE_DOOR_MODE_ON_OFF))
                            throw new ObjectException();
                        
                        // BeaconId
                    Logger.detail("------------ BeaconId------------");
                    a = door.getBeaconId();
                    if (0 != a.compareTo( Beacon.SE_NULL_BEACON_ID))
                            throw new ObjectException();
                        
                        // Device Extension Object Id
                    Logger.detail("------------ DeviceExtensionId------------");
                    a = door.getDeviceExtensionId();
                    if (0 != a.compareTo( DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                            throw new ObjectException(); 
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
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

    DESCRIPTION: Only SuperA can create door.

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
//# User can not create door
//# 
                Logger.detail("------------ User can not create door ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to create a door
                apduObject = new Apdu(Apdu.CREATE_DOOR_APDU );

                    // send command
                c.description = "Create Door";
                c.requester = user;
                c.execute(apduObject.toString(), expectedRes);                         
//#
//# Administrator can not create door
//#
                Logger.detail("------------ Administrator can not create door ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to create a door
                apduObject = new Apdu(Apdu.CREATE_DOOR_APDU );


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
            Door door;                
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
                    door = new Door(superA);
                    door.syncroFields(superA);
    //#
    //# Super A can read the status field of a Door
    //#
                    Logger.detail("-- Super A can read the status field of Door  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    door.syncroFields(superA);
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_DIASABLED ))
                            throw new ObjectException();                        

    //#
    //# Administrator can read the status field of a Door
    //#
                    Logger.detail("-- Administrator can read the status field of a Door  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    door.syncroFields(admin);
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_DIASABLED ))
                            throw new ObjectException();                        
    //#
    //# User can read the status field of a Door
    //#
                    Logger.detail("-- User can read the status field of a Door  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    door.syncroFields(user);
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_DIASABLED ))
                            throw new ObjectException(); 
    //#
    //# Object deletion
    //#                       
                    admin.delete(superA);
                    admin1.delete(superA);
                    user.delete(superA);
                    user1.delete(superA);
                    door.delete(superA);
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

    DESCRIPTION: SuperA can updated the fileds of a Door object. User and Admin not.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException
    {
            String a, expectedRes;
            Door door;
            Command c = new Command();

            String testCase = testBatch+"/"+"Test Case 04";                

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCase);

                        // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);

                    // Create Door
                Logger.detail("------------ Create Door ------------");                        
                door = new Door(superA);
                door.syncroFields(superA);

//#
//# Super A can updated the field of a Door Object
//#
                    // RestoreLastOutputValue
                door.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
                door.update(superA);
                door.syncroFields(superA);

                Logger.detail("------------ Check Restore Last Output Value ------------");
                a = door.getRestoreLastOutputValue();
                if (0 != a.compareTo( Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE ))
                        throw new ObjectException(); 
                
                    // Status
                door.setStatus(Door.SE_DOOR_DIASABLED);
                door.update(superA);
                door.syncroFields(superA);

                Logger.detail("------------ Check Status ------------");
                a = door.getStatus();
                if (0 != a.compareTo( Door.SE_DOOR_DIASABLED ))
                        throw new ObjectException(); 

                    // mode
                door.setMode(Door.SE_DOOR_MODE_PULSE);
                door.update(superA);
                door.syncroFields(superA);                

                Logger.detail("------------ Check Mode ------------");
                a = door.getMode();
                if (0 != a.compareTo( Door.SE_DOOR_MODE_PULSE ))
                        throw new ObjectException();

                    // GPIO 00
                door.setGpioId("00");
                door.update(superA);
                door.syncroFields(superA);                

                Logger.detail("------------ Check GPIO ------------");
                a = door.getGpioId();
                if (0 != a.compareTo( "00" ))
                        throw new ObjectException(); 

                    // security
                door.setSecurity(Door.SE_DOOR_SECURITY_PIN);
                door.update(superA);
                door.syncroFields(superA);                

                Logger.detail("------------ Check Security ------------");
                a = door.getSecurity();
                if (0 != a.compareTo( Door.SE_DOOR_SECURITY_PIN ))
                        throw new ObjectException(); 

                    // Initial Configuration
                door.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
                door.update(superA);
                door.syncroFields(superA);                

                Logger.detail("------------ Check Initial Configuration ------------");
                a = "000000" + door.getInitialOutputValue();
                if (0 != a.compareTo( Door.SE_DOOR_STATUS_HIGH ))
                        throw new ObjectException();

                    // Initial Configuration
                door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                door.update(superA);
                door.syncroFields(superA);                 

                Logger.detail("------------ Check Initial Configuration ------------");
                a = "000000" + door.getInitialOutputValue();
                if (0 != a.compareTo( Door.SE_DOOR_STATUS_LOW))
                        throw new ObjectException();

                    // name
                door.setName("0123456789ABCDEF0123456789ABCDEF");
                door.update(superA);
                door.syncroFields(superA); 
                
                Logger.detail("------------ Check Name ------------");
                a = door.getName();							
                if (0 != a.compareTo("0123456789ABCDEF0123456789ABCDEF"))
                        throw new CommandErrorException(); 

                    // time
                door.setPulseDuration("ABCD");
                door.update(superA);
                door.syncroFields(superA);                

                    // Check Time 
                Logger.detail("------------ Check Time ------------");
                a = door.getPulseDuration();
                if (0 != a.compareTo( "ABCD" ))
                        throw new ObjectException();

                    // Device Extension Identifier                
                DeviceExtender deviceEstender = new DeviceExtender(superA, "pepe");
                
                door.setDeviceExtensionId(deviceEstender.getObjectId());
                door.update(superA);
                door.syncroFields(superA);
                
                    // Check Device Extension Identifier 
                Logger.detail("------------ Device Extension Identifier ------------");
                a = door.getDeviceExtensionId();
                if (0 != a.compareTo( deviceEstender.getObjectId() ))
                        throw new ObjectException();
//#
//# Administrator can not update door
//#
                Logger.detail("------------ Administrator can not update door ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                Apdu apdu = new Apdu(Apdu.UPDATE_DOOR_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, door.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_GPIO, "03");
                    // send command
                c.description = "Update Door";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not update door
//#

                Logger.detail("------------ user can not update door ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.UPDATE_DOOR_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, door.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_GPIO, "03");
                    // send command
                c.description = "Update Door";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                deviceEstender.delete(superA);
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
    testCase05
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can delete a door. User and administrators can not delete a door.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException
    {
            String a, expectedRes;
            Door door;
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
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);

                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);

//#
//# Super A can delete a door
//#
                Logger.detail("------------ Super A can delete a door ------------");

                    // Create Door
                Logger.detail("------------ Create Door ------------");                        
                door = new Door(superA);

                    // Update Initial Configuration. Door exist/
                door.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
                door.update(superA);

                String objId = door.getObjectId();

                door.delete(superA);                                               

                    // check that the door has been deleted
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                Apdu apdu = new Apdu(Apdu.UPDATE_DOOR_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID,objId );
                apdu.addTlv(Atlv.DATA_TAG_GPIO, "00");
                    // send command
                c.description = "Delete Door";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);                        

//#
//# Administrator can not delete a door
//#
                door = new Door(superA);

                Logger.detail("------------ Administrator can not delete a door ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, door.getObjectId());
                    // send command
                c.description = "Delete Door";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not delete a door
//#

                Logger.detail("------------ user can not delete a door ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, door.getObjectId());

                    // send command
                c.description = "Delete Door";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
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
    testCase06
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA, administrator and user launch a command to a door. 
                Triggering did not request security
                Status is checked through getGPIOValue API.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException
    {
            Door door;
            int index;
            LogEntry le;
            String doorId, requestorId, gpioValue, policyId;
            AccessPolicy defaultPolicy;               
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
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
                door.setGpioId("00");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                doorId = door.getObjectId();                        

//#
//# Super A can trigger a door
//#
                Logger.detail("------------ Super A can trigger a door ------------");                        

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                requestorId = superA.getObjectId();

                    // get the last log entry
                index = DeviceLogger.getLastEntryIndex(superA);							
                le = DeviceLogger.getEntry(superA, index);

                    // check log entry
                if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                        ( 0 != doorId.compareTo(le.objectId)) ||
                        ( 0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                        ( 0 != "9000".compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                gpioValue = door.getOutput(superA);
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                {
                    throw new TestException();
                }

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                gpioValue = door.getOutput(superA);
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                {
                    throw new TestException();
                }
//#
//# Administrator can trigger a Door
//#
                Logger.detail("------------ Admin can trigger a door ------------");                           
                door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);
                gpioValue = door.getOutput(admin);
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                {
                    throw new TestException();
                }

                requestorId = admin.getObjectId();                        

                    // get the last log entry
                index = DeviceLogger.getLastEntryIndex(superA);							
                le = DeviceLogger.getEntry(superA, index);

                    // check log entry
                if ((0 != requestorId.compareTo(le.requesterId) ) || 
                        (0 != doorId.compareTo(le.objectId)) ||
                        (0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                        (0 != "9000".compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                gpioValue = door.getOutput(admin);
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                {
                    throw new TestException();
                }

//#
//# User can trigger a Door
//#

                Logger.detail("------------ user can not update door ------------");
                door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);
                gpioValue = door.getOutput(user);
                if(0 != (gpioValue.compareTo(Door.SE_DOOR_STATUS_HIGH)))
                {
                    throw new TestException();
                }  

                requestorId = user.getObjectId();                        

                    // get the last log entry
                index = DeviceLogger.getLastEntryIndex(superA);							
                le = DeviceLogger.getEntry(superA, index);

                    // check log entry
                if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                        ( 0 != doorId.compareTo(le.objectId)) ||
                        ( 0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                        ( 0 != "9000".compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF); 
                gpioValue = door.getOutput(user);
                if((0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_LOW)))
                {
                    throw new TestException();
                }                        

//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
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
    /*----------------------------------------------------------------------------
    testCase07
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA, administrator and user launch a command to a door. 
                Triggering mandate PIN verification

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException
    {
        Door door;
        int index;
        LogEntry le;
        String doorId, requestorId, gpioValue, status, policyId;
        AccessPolicy defaultPolicy;
        LocalAuthenticator superALocalAuthetnicator;
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
            superALocalAuthetnicator = superA.getLocalAuthenticatorObject();
            superALocalAuthetnicator.syncroFields(superA);
            status = superALocalAuthetnicator.getStatus();
            
            superA.resetPin();
            if (0 == status.compareTo(LocalAuthenticator.AUTHOBJ_PIN_READY))
                superA.changePin(superA, "01020304", "31323334");                            
            else
                superA.setPin(superA, "31323334");


                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.setPin(admin, "31323334");
            admin.syncroFields(admin);

                    // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.setPin(user, "31323334");
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

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON, "31323334");
            gpioValue = door.getOutput(superA);
            if((0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_HIGH)))
            {
                throw new TestException();
            } 

            requestorId = superA.getObjectId();

                // get the last log entry
            index = DeviceLogger.getLastEntryIndex(superA);							
            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                    ( 0 != doorId.compareTo(le.objectId)) ||
                    ( 0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                    ( 0 != "9000".compareTo(le.result)))
                    {
                            throw new TestException();
                    }

            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF, "31323334");
            gpioValue = door.getOutput(superA);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }                         

//#
//# Administrator can trigger a Door
//#
            Logger.detail("------------ Admin can trigger a door ------------");                           
            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON, "31323334");
            gpioValue = door.getOutput(admin);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
            {
                throw new TestException();
            }                         

            requestorId = admin.getObjectId();                        

                // get the last log entry
            index = DeviceLogger.getLastEntryIndex(superA);							
            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                    ( 0 != doorId.compareTo(le.objectId)) ||
                    ( 0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                    ( 0 != "9000".compareTo(le.result)))
                    {
                            throw new TestException();
                    }                          
            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF, "31323334");
            gpioValue = door.getOutput(admin);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }                         
//#
//# User can trigger a Door
//#

            Logger.detail("------------ user can not update door ------------");
            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON, "31323334");
            gpioValue = door.getOutput(user);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
            {
                throw new TestException();
            }                         

            requestorId = user.getObjectId();                        

                // get the last log entry
            index = DeviceLogger.getLastEntryIndex(superA);							
            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 !=  requestorId.compareTo(le.requesterId) ) || 
                    ( 0 != doorId.compareTo(le.objectId)) ||
                    ( 0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                    ( 0 != "9000".compareTo(le.result)))
                    {
                            throw new TestException();
                    }                         
            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF, "31323334");
            gpioValue = door.getOutput(user);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }                         
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
    /*----------------------------------------------------------------------------
    testCase08
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Door is not triggering if wrong PIN.
                 Door is not triggered if PIN not input

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase08() throws TestException
    {
        String expectedRes, objId, doorId, a, requestorId, status, policyId;
        Door door;
        Command c = new Command();
        LocalAuthenticator lao;
        int startRetryCounter, retryCounter;
        AccessPolicy defaultPolicy;
        int index;
        LogEntry le;
        Apdu apdu;

        String testCode = testBatch+"/"+"Test Case 08";                

        // ---------------------- Code -------------------------------
        try
        {
            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

                    // instantiate a local User object for the SUPER-A
            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            lao = superA.getLocalAuthenticatorObject();
            lao.syncroFields(superA);
            status = lao.getStatus();
            if (0 == status.compareTo(LocalAuthenticator.AUTHOBJ_PIN_READY))
                superA.changePin(superA, "31323334", "31323334");                            
            else
                superA.setPin(superA, "31323334");


                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.setPin(admin, "31323334");
            admin.syncroFields(admin);

                    // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.setPin(user, "31323334");
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
//# Super A can  not trigger a door with a wrong PIN
//#
            Logger.detail("------------ Super A can not trigger a door with a wrong PIN------------");       

            objId = door.getObjectId();
            lao = superA.getLocalAuthenticatorObject();
            lao.syncroFields(superA);

                // get the retry counter
            a = lao.getRetryCounter();
            startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];              

                 // create the apdu object
            apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, objId);                
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
            apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "3132333435");                   

                // set the expected result and send the command;              
            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

                // send command
            c.description = "Trigger Door ";
            c.requester = superA;                
            c.execute(apdu.toString(), expectedRes);

                // retry cunter shall be decrised of 1
            lao.syncroFields(admin);
            a = lao.getRetryCounter();
            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                // Check Retry Counter. It is decreased of 1
            if ((startRetryCounter - 1) != retryCounter)
                    throw new ObjectException(); 
                        // get the last log entry

            index = DeviceLogger.getLastEntryIndex(superA);	

            requestorId = superA.getObjectId();

            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                    (0 != objId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                    (0 != "6A80".compareTo(le.result)))
                    {
                            throw new TestException();
                    } 
//#
//# Super A can  not trigger a door if security is at PIN and there is no PIN
//#
            Logger.detail("------------ Super A can not trigger a door with a wrong PIN------------");       

            objId = door.getObjectId();
            lao = superA.getLocalAuthenticatorObject();
            lao.syncroFields(superA);

                // get the retry counter
            a = lao.getRetryCounter();
            startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];              

                 // create the apdu object
            apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, objId);                
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);                 

                // set the expected result and send the command;              
            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                    Apdu.SW_6A88_DATA_NOT_FOUND_TLV_STRING +
                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

                // send command
            c.description = "Trigger Door ";
            c.requester = superA;                
            c.execute(apdu.toString(), expectedRes);

                // retry cunter shall be decrised of 1
            lao.syncroFields(admin);
            a = lao.getRetryCounter();
            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                // Check Retry Counter. It is decreased of 1
            if (startRetryCounter != retryCounter)
                    throw new ObjectException(); 
                        // get the last log entry

            index = DeviceLogger.getLastEntryIndex(superA);	

            requestorId = superA.getObjectId();

            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                    (0 != objId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_ACTUATOR_ON.compareTo(le.event)) ||
                    (0 != "6A88".compareTo(le.result)))
                    {
                            throw new TestException();
                    }             
//#
//# Object deletion
//#                       
        admin.delete(superA);
        user.delete(superA);
        door.delete(superA); 
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
    /*----------------------------------------------------------------------------
    testCase09
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check that Create, update and delete door updates the log.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase09() throws TestException
    {
        Door door;
        int index;
        LogEntry le;
        String doorId, requestorId;              
        String testCode = testBatch+"/"+"Test Case 09";                

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
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.syncroFields(admin);

                    // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

//#
//# Log is updated when a door is created
//#
            Logger.detail("------------ Log is updated when a door is created ------------");                        

            door = new Door(superA);
            door.syncroFields(superA);

            index = DeviceLogger.getLastEntryIndex(superA);	

            requestorId = superA.getObjectId();
            doorId = door.getObjectId();

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
//# Log is updated when a door is deleted;
//#
            Logger.detail("------------ Log is updated when a door is deleted; ------------");                        

            door.delete(superA);

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
    /*----------------------------------------------------------------------------
    testCase10
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: GIO0 Led Blinks Test.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase10() throws TestException
    {
        Door door;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;              
        String testCode = testBatch+"/"+"Test Case 10";                

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
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setGpioId("00");                      
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);
          
//#
//# Super A can trigger a door
//#
            Logger.detail("------------ Super A can trigger a door ------------");                        

            for (int i = 0; i<30; i++)
            {
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                gpioValue = door.getOutput(superA);
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                {
                    throw new TestException();
                } 

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                gpioValue = door.getOutput(superA);
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                {
                    throw new TestException();
                }                            
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
    /*----------------------------------------------------------------------------
    testCase11
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: GIO0 Led Pulse Test.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase11() throws TestException
    {
        Door door;
        String  gpioValue, policyId;
        AccessPolicy defaultPolicy;             
        String testCode = testBatch+"/"+"Test Case 11";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setGpioId("00");
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);

//#
//# Super A can trigger a door with a pulse
//#
            Logger.detail("------------ Super A can trigger a door with a pulse ------------");                        

            try
            {
                door.setPulseDuration("0BB8");
                door.update(superA);
                door.syncroFields(superA);
                for (int i = 0; i<10; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_HIGH))
                    {
                        throw new TestException();
                    } 
                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_LOW))
                    {
                        throw new TestException();
                    } 
                }

                door.setPulseDuration("03E8");
                door.setGpioId("04");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<10; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_HIGH))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_LOW))
                    {
                        throw new TestException();
                    }                                 
                }
            } catch (InterruptedException e) 
            {
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
    /*----------------------------------------------------------------------------
    testCase12
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: It is not possible to trigger a door when disabled

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase12() throws TestException
    {
        String  expectedRes, doorId, policyId;
        AccessPolicy defaultPolicy;
        Apdu apduObject;
        Command c = new Command();
        Door door;

        String testCode = testBatch+"/"+"Test Case 12";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setGpioId("00");
            door.setInitialOutputValue( Door.SE_DOOR_STATUS_LOW);          
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.update(superA);
            door.syncroFields(superA);

            doorId = door.getObjectId();            

//#
//# SuperA can not trigger a disabled door door.  Missing Door ID case
//#                      
            Logger.detail("------------ Super A can not trigger a disabled door ------------");             

            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                    Apdu.SW_6A88_DATA_NOT_FOUND_TLV_STRING +
                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                // try to trigger a door
            apduObject = new Apdu(Apdu.TRIGGER_DOOR_APDU );
            apduObject.addTlv(Atlv.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);                        

                // send command
            c.description = "Trigger the Door";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);
//#
//# SuperA can not trigger a disabled door door.
//#                      
            Logger.detail("------------ Super A can not trigger a disabled door ------------");             

            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                // try to trigger a door
            apduObject = new Apdu(Apdu.TRIGGER_DOOR_APDU );
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, doorId);
            apduObject.addTlv(Atlv.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);                        

                // send command
            c.description = "Trigger the Door";
            c.requester = superA;
            c.execute(apduObject.toString(), expectedRes);                        
//#
//# Admin try to trigger the door. 
//#                        
            Logger.detail("------------ Super A can not trigger a disabled door ------------");             

            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                // try to trigger a door
            apduObject = new Apdu(Apdu.TRIGGER_DOOR_APDU );
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, doorId);                       
            apduObject.addTlv(Atlv.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);                        

                // send command
            c.description = "Trigger the Door";
            c.requester = admin;
            c.execute(apduObject.toString(), expectedRes);
//#
//# User  try to trigger the door. 
//#
            Logger.detail("------------ Super A can not trigger a disabled door ------------");             

            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                // try to trigger a door
            apduObject = new Apdu(Apdu.TRIGGER_DOOR_APDU );
            apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, doorId);                        
            apduObject.addTlv(Atlv.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);                        

                // send command
            c.description = "Trigger the Door";
            c.requester = user;
            c.execute(apduObject.toString(), expectedRes);                        

//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
    testCase13
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Initial status change is not effective when a door is disabled.
    TODO: Needs a sensor object.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase13() throws TestException
    {
        String  gpioValue, policyId;
        AccessPolicy defaultPolicy;
        Door door;              

        String testCode = testBatch+"/"+"Test Case 13";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setGpioId("00");
            door.setInitialOutputValue( Door.SE_DOOR_STATUS_LOW);          
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.update(superA);
            door.syncroFields(superA);
                     

//#
//# Super A can not trigger a disabled door
//#                      
            Logger.detail("------------ Super A can not trigger a disabled door ------------");             

            door.setStatus(Door.SE_DOOR_DIASABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
            door.update(superA);
            door.syncroFields(superA);

            gpioValue = door.getOutput(superA);
            System.out.println(">>>>>>>>>>>>Value: " + gpioValue);
            if( 0 != (gpioValue.compareTo(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }

//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
    /*----------------------------------------------------------------------------
    testCase14
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: When updated the initial status is set accordingly.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase14() throws TestException
    {
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;
        Door door;
        String testCode = testBatch+"/"+"Test Case 14";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();
            
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setGpioId("00");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue( Door.SE_DOOR_STATUS_LOW);          
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.update(superA);
            door.syncroFields(superA);            

//#
//# Super A set initial status to High
//#                      
            Logger.detail("------------ Set Initial status to High------------");
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
            door.update(superA);            

            gpioValue = door.getOutput(superA);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }
//#
//# Super A set initial status to Low
//#                        
            Logger.detail("------------ Set Initial status to low------------");                        
                 // Initial Status
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);            

            gpioValue = door.getOutput(superA);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }

//#
//# Initial setting is kept over a reset
//#                      
            Logger.detail("------------ Set Initial status to High------------");
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
            door.update(superA);  
            
            gpioValue = door.getOutput(superA);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }
            
            superA.setPin(superA, "31323334");
            thisDevice.systemReset("31323334", superA, true);

            Thread.sleep(15000); 

            gpioValue = door.getOutput(superA);
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
            {
                throw new TestException();
            }

//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

        }
        catch (CommandErrorException | ObjectException | TestException  | IOException | InterruptedException e)
        {
            Logger.testResult(false);		
            TestException t = new TestException();
            throw t;
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }        
   /*----------------------------------------------------------------------------
    testCase15
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: GPIO Pulse test from 0 tp 7.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase15() throws TestException
    {
        Door door;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;              
        String testCode = testBatch+"/"+"Test Case 15";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setGpioId("00");
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);         

//#
//# Super A can trigger a door
//#
            Logger.detail("------------ Super A can trigger a door ------------");                        

            try
            {
                door.setPulseDuration("0BB8");
                door.update(superA);
                door.syncroFields(superA);
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 
                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    } 
                }

                door.setPulseDuration("03E8");
                door.setGpioId("01");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    }                                 
                }
                
                door.setPulseDuration("03E8");
                door.setGpioId("02");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    }                                 
                }
                door.setPulseDuration("03E8");
                door.setGpioId("03");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    }                                 
                }
                door.setPulseDuration("03E8");
                door.setGpioId("04");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    }                                 
                }
                
                    // GPIOID can be changed to extend test
                door.setPulseDuration("03E8");
                door.setGpioId("04");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    }                                 
                }
                
                 // GPIOID can be changed to extend test
                door.setPulseDuration("03E8");
                door.setGpioId("04");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    }                                 
                }
                   // GPIOID can be changed to extend test
                door.setPulseDuration("03E8");
                door.setGpioId("04");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    }                                 
                }
            } catch (InterruptedException e) 
            {
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
    	/*----------------------------------------------------------------------------
	testCase16
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: User trigger door when disabled
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase16() throws TestException
	{              
            String testCase = testBatch+"/"+"Test Case 16";
            
            String expectedRes;
            Apdu apdu;
            Command c = new Command();
		
	// ---------------------- Code -------------------------------
            try
            {
                Logger.testCase(testCase);            

                // ping shall not be sent;
                
                    // instantiate a local User object for the SUPER-A
                    // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    // object personalized
		superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

                User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");                         
                admin.updateKey("adminx");
                admin.syncroFields(admin);

                User user = new User(admin, User.USER_ROLE_USER, "s[atter");						
                user.updateKey( "newAdministratorKeysSutta");
                user.syncroFields(user);

                    // object created
                User admin1 = new User(superA, User.USER_ROLE_ADMIN, "splash");                         
                admin1.updateKey("adminx");
                admin1.syncroFields(admin1);

                
                User user1 = new User(admin, User.USER_ROLE_USER, "s[atter");						
                user1.updateKey( "newAdministratorKeysSutta");
                user1.syncroFields(user1);
                
                
                Door door = new Door(superA);
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.setGpioId(0);
                door.update(superA);
                                                    
	//#
	//#  User can not trigger a door if disabled
	//#
  
            Logger.detail("User can not trigger a door if disabled");

                // change the user status to inactive
            user1.setStatus(User.STATUS_INACTIVE);
            user1.update(superA);
            
            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                 String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                 String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                       Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                 String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            
                    // create the apdu object
                apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

                // send command
            c.description = "User can not trigger a door if disabled";
            c.requester = user1;
            c.execute(apdu.toString(), expectedRes);

	//#
	//#  Admin can not trigger a dorr if disabled
	//#
  
            Logger.detail("Admin can not trigger a door if disabled");

                // change the user status to inactive
            admin1.setStatus(User.STATUS_INACTIVE);
            admin1.update(superA);
            
            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                 String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                 String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                       Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                 String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            
                    // create the apdu object
                apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

                // send command
            c.description = "Admin can not trigger a door if disabled";
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
    testCase17
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: GPIO Pulse test from 0 tp 7. SYncro is used to get the output value/

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase17() throws TestException
    {
        Door door;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;              
        String testCode = testBatch+"/"+"Test Case 17";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setGpioId("00");
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);         

//#
//# Super A can trigger a door syncroFileds of a door get latest output value
//#
            Logger.detail("------------ Super A can trigger a door ------------");                        

            try
            {
                door.setPulseDuration("0BB8");
                door.update(superA);
                door.syncroFields(superA);
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    door.syncroFields(superA);
                    gpioValue = door.getLastOutputValue();
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 
                    Thread.sleep(4000);

                    door.syncroFields(superA);
                    gpioValue = door.getLastOutputValue();
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    } 
                }

                door.setPulseDuration("03E8");
                door.setGpioId("01");
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<4; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    door.syncroFields(superA);
                    gpioValue = door.getLastOutputValue();
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                    {
                        throw new TestException();
                    }                                 
                }
            } catch (InterruptedException e) 
            {
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
   /*----------------------------------------------------------------------------
    testCase18
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: GPIO Virtual Door test

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase18() throws TestException
    {
        Door door;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;              
        String testCode = testBatch+"/"+"Test Case 18";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.update(superA);
            door.syncroFields(superA);         

//#
//# Super A can trigger virtual door
//#
            Logger.detail("------------ Super A can trigger a virtual door ------------");                        
            for (int i = 0x80; i<0x90; i++)
            {
                door.setGpioId(i);
                door.update(superA);
                door.syncroFields(superA);

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                gpioValue = door.getOutput(superA);                    
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                {
                    throw new TestException();
                } 

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                gpioValue = door.getOutput(superA);                    
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                {
                    throw new TestException();
                } 
            }


            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
  /*----------------------------------------------------------------------------
    testCase19
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test Restore Last Output Value

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase19() throws TestException
    {
        Door door;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;              
        String pin;
        String testCode = testBatch+"/"+"Test Case 19";                        

        // ---------------------- Code -------------------------------
        try
        {

            pin = "01020304";
            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            
            thisDevice.systemReset(pin, superA, true);
                
                // wait;
            try{
                   Thread.sleep(4000);
             } catch (InterruptedException e){
             }

            gpioValue = door.getOutput(superA);                    
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
            {
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
            try{
                   Thread.sleep(4000);
             } catch (InterruptedException e){
             }

            gpioValue = door.getOutput(superA);                    
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }               
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
 /*----------------------------------------------------------------------------
    testCase19
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test Restore Last Output Value

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase20() throws TestException
    {
        Door door, door1, door2;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;              
        String pin;
        String testCode = testBatch+"/"+"Test Case 20";                        

        // ---------------------- Code -------------------------------
        try
        {

            pin = "01020304";
            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            
                // set the door1
            door1 = new Door(superA);                        
            door1.setStatus(Door.SE_DOOR_ENABLED);
            door1.setGpioId(1);
            door1.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
            door1.update(superA);
            door1.syncroFields(superA);

                // set the door
            door2 = new Door(superA);                        
            door2.setStatus(Door.SE_DOOR_ENABLED);
            door2.setGpioId(2);
            door2.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
            door2.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
            door2.update(superA);
            door2.syncroFields(superA);        

//#
//# Initial Value is on if no entry in the log and initial value is on
//#
            Logger.detail("------------ Initial Value is on if no entry in the log and initial value is on ------------");                        

            thisDevice.systemReset(pin, superA, true);
                
                // wait;
            try{
                   Thread.sleep(4000);
             } catch (InterruptedException e){
             }
            
            gpioValue = door1.getOutput(superA);                    
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
            {
                throw new TestException();
            }
            gpioValue = door2.getOutput(superA);                    
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
            {
                throw new TestException();
            }
//#
//# Restore on two doors
//#            
            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
            
                // introduce some extra information into the log
            door.update(superA);
            user.update(superA);        
            door1.update(superA);
            user.update(superA);
            door.update(superA);
            door2.update(superA);
            user.update(superA);
            
            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
            
            thisDevice.systemReset(pin, superA, true);
                
                // wait;
            try{
                   Thread.sleep(4000);
             } catch (InterruptedException e){
             }

            gpioValue = door.getOutput(superA);                    
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
            {
                throw new TestException();
            }
            
            gpioValue = door1.getOutput(superA);                    
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
            {
                throw new TestException();
            }

            gpioValue = door2.getOutput(superA);                    
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }            
         
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            door1.delete(superA);
            door2.delete(superA);
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
 /*----------------------------------------------------------------------------
    testCase21
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test Restore Last Output Value; 
    wrong SW in the log are not considered for initial value

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase21() throws TestException
    {
        Door door;
        Command c = new Command();
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;              
        String pin;
        String testCode = testBatch+"/"+"Test Case 21";                        

        // ---------------------- Code -------------------------------
        try
        {

            pin = "01020304";
            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setSecurity(Door.SE_DOOR_SECURITY_PIN);
            door.update(superA);
            door.syncroFields(superA);         

//#
//# Wrong attempt to manage a door are not considered in the initial value setting
//#
            Logger.detail("------------ Wrong SW ------------");                        
            
            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON, pin);
            
                // introduce some extra information into the log
            door.update(superA);
            user.update(superA);
            
            door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF, pin);
                // introduce some extra information into the log
            door.update(superA);
            user.update(superA);
            door.update(superA);
            door.update(superA);
            user.update(superA);
            
                 //Attempt to switch on
            Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);                 

                // set the expected result and send the command;              
            String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                    Apdu.SW_6A88_DATA_NOT_FOUND_TLV_STRING +
                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

                // send command            
            c.description = "Trigger Door ";
            c.requester = superA;                
            c.execute(apdu.toString(), expectedRes);
            
            thisDevice.systemReset(pin, superA, true);
                
                // wait;
            try{
                   Thread.sleep(4000);
             } catch (InterruptedException e){
             }

            gpioValue = door.getOutput(superA);                    
            if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
            {
                throw new TestException();
            }            
            
            
            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
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
   /*----------------------------------------------------------------------------
    testCase22
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: GIO0 Pulse Test on virtual doors.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase22() throws TestException
    {
        Door door;
        String  gpioValue, policyId;
        AccessPolicy defaultPolicy;             
        String pin;
        String testCode = testBatch+"/"+"Test Case 22";                

        // ---------------------- Code -------------------------------
        try
        {
            pin = "01020304";                

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	
            superA.setPin(superA, pin);
            
                    // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
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
            door.setGpioId(0x80);
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.update(superA);
            door.syncroFields(superA);

                // reset needed since pulse door are available just after a reset
            thisDevice.systemReset(pin, superA, true);

//#
//# Super A can pulse first virtual door 
//#
            Logger.detail("------------ Super A can pulse first virtual door  ------------");                        

            try
            {
                door.setPulseDuration("0BB8");
                door.update(superA);
                door.syncroFields(superA);
                for (int i = 0; i<10; i++)
                {

                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_HIGH))
                    {
                        throw new TestException();
                    } 
                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_LOW))
                    {
                        throw new TestException();
                    } 
                }
//#
//# Super A can pulse last virtual door 
//#
                Logger.detail("------------ Super A can pulse last virtual door  ------------"); 
                
                door.setPulseDuration("0BB8");
                door.setGpioId(0x8F);
                door.update(superA);
                door.syncroFields(superA);                
                for (int i = 0; i<10; i++)
                {
                    System.out.println("i-----> " + i);
                    door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                    gpioValue = door.getOutput(superA);
                    if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_HIGH))
                    {
                        throw new TestException();
                    } 

                    Thread.sleep(4000);

                    gpioValue = door.getOutput(superA);
                    System.out.println("gpioValue " + gpioValue);
                    System.out.println("Door.SE_DOOR_STATUS_LOW " + Door.SE_DOOR_STATUS_LOW);
                    if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_LOW))
                    {
                        System.out.println("TestException");
                        throw new TestException();
                    }                                 
                }
            } catch (InterruptedException e) 
            {
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            thisDevice.systemReset(pin, superA, true);

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
