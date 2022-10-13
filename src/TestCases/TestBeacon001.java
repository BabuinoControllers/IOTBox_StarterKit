
package TestCases;
    
import com.sdk.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class TestBeacon001 {
    
 /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestDoor001";
    public static final String deviceId = MainTest.TestMain.deviceId;   

    /********************************
    Public Methods
    ********************************/
    public static User superA;  
    public static Device d;

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
        superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
    
        try {
                d = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET,3,2000);     
                
                superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, d);               
                
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

    DESCRIPTION: SuperA creates a Beacon object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException
    {
        
            String a, name;
            Beacon beacon;
            Command c = new Command();
            String testCase = testBatch +" /" + "Test Case 01";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    d.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a beacon object. Initial values are according to specs.
    //# 
                        // Create Beacon
                    Logger.detail("------------ Create Beacon ------------");                        
                    beacon = new Beacon(superA);
                    beacon.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = beacon.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = beacon.getStatus();
                    if (0 != a.compareTo( Beacon.SE_BEACON_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = beacon.getName();			
                    name = beacon.getObjectId();
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();
                            
                        // The id is the initial id
                    Logger.detail("------------ Check that id is the default one ------------");
                    a = beacon.getBeaconId();			
                    name = beacon.getObjectId();
                    String z = Util.stringToHexAscii(name, 32);
                    if (0 != a.compareTo(z.substring(0,32)))//a.compareTo(Util.stringToHexAscii(name)+"00000000"))
                            throw new CommandErrorException();
                    
                        // Mode                     
                    Logger.detail("------------ Mode------------");
                    a = beacon.getMode();
                    if (0 != a.compareTo( Beacon.SE_BEACON_MODE_EID))
                            throw new ObjectException();

                        // Rotation Exponent
                    Logger.detail("------------ Rotation Exponent------------");
                    a = beacon.getRotationExponent();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();                    
                    
                            // Cross Time
                    Logger.detail("------------ CrossTIme------------");
                    a = beacon.getRotationExponent();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();                                        
    //#
    //# Object deletion
    //#                       
                    beacon.delete(superA);
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

    DESCRIPTION: Only SuperA can create Beacon.

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
                d.ping();

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
//# User can not create Beacon
//# 
                Logger.detail("------------ User can not create Beacons ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to create a beacon
                apduObject = new Apdu(Apdu.CREATE_SE_BEACON_APDU );

                    // send command
                c.description = "Create Beacon";
                c.requester = user;
                c.execute(apduObject.toString(), expectedRes);                         
//#
//# Administrator can not create Beacon
//#
                Logger.detail("------------ Administrator can not create Beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to create a beacon
                apduObject = new Apdu(Apdu.CREATE_SE_BEACON_APDU );


                    // send command
                c.description = "Create Beacon";
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
            Beacon beacon;
            Command c = new Command();                
            String testCase = testBatch +" /" + "Test Case 03";               

            // ---------------------- Code -------------------------------
            try
            {

                    Logger.testCase(testCase);

                            // launch a ping
                    d.ping();

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

                        // Create Beacon
                    Logger.detail("------------ Create Beacon ------------");                        
                    beacon = new Beacon(superA);
                    beacon.syncroFields(superA);
    //#
    //# Super A can read the status field of a Beacon
    //#
                    Logger.detail("-- Super A can read the status field of Beacon  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    beacon.syncroFields(superA);
                    a = beacon.getStatus();
                    if (0 != a.compareTo( Beacon.SE_BEACON_ENABLED ))
                            throw new ObjectException();                        

    //#
    //# Administrator can read the status field of a Beacon
    //#
                    Logger.detail("-- Administrator can read the status field of a Beacon  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    beacon.syncroFields(admin);
                    a = beacon.getStatus();
                    if (0 != a.compareTo( Beacon.SE_BEACON_ENABLED ))
                            throw new ObjectException();                        
    //#
    //# User can read the status field of a Beacon
    //#
                    Logger.detail("-- User can read the status field of a Beacon  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    beacon.syncroFields(user);
                    a = beacon.getStatus();
                    if (0 != a.compareTo( Beacon.SE_BEACON_ENABLED ))
                            throw new ObjectException(); 
    //#
    //# Object deletion
    //#                       
                    admin.delete(superA);
                    user.delete(superA);
                    admin1.delete(superA);
                    user1.delete(superA);
                    beacon.delete(superA);
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

    DESCRIPTION: SuperA can updated the fileds of a Beacon object. User and Admin not.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException
    {
            String a, expectedRes;
            Beacon beacon;
            Command c = new Command();

            String testCode = testBatch+"/"+"Test Case 04";                

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                        // launch a ping
                d.ping();

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

                    // Create Beacon
                Logger.detail("------------ Create Beacon ------------");                        
                beacon = new Beacon(superA);
                beacon.syncroFields(superA);

//#
//# Super A can updated the field of a Beacon Object
//#
                    // Status
                beacon.setStatus(Beacon.SE_BEACON_DISABLED);
                beacon.update(superA);
                beacon.syncroFields(superA);

                Logger.detail("------------ Check Status ------------");
                a = beacon.getStatus();
                if (0 != a.compareTo( Beacon.SE_BEACON_DISABLED ))
                        throw new ObjectException(); 

                    // mode
                beacon.setMode("00");
                beacon.update(superA);
                beacon.syncroFields(superA);                

                Logger.detail("------------ Check Mode ------------");
                a = beacon.getMode();
                if (0 != a.compareTo( "00" ))
                        throw new ObjectException();

                   // Rotation Exponent
                beacon.setRotationExponent("10");
                beacon.update(superA);
                beacon.syncroFields(superA);                

                Logger.detail("------------ Check K ------------");
                a = beacon.getRotationExponent();
                if (0 != a.compareTo( "10" ))
                        throw new ObjectException();

                                   // Cross Time
                beacon.setCrossTime("10");
                beacon.update(superA);
                beacon.syncroFields(superA);  
                
                Logger.detail("------------ Check crossTime ------------");
                a = beacon.getCrossTime();
                if (0 != a.compareTo( "10" ))
                        throw new ObjectException();                
                
                    // name
                beacon.setName("0123456789ABCDEF0123456789ABCDEF");
                beacon.update(superA);
                beacon.syncroFields(superA); 
                
                Logger.detail("------------ Check Name ------------");
                a = beacon.getName();							
                if (0 != a.compareTo("0123456789ABCDEF0123456789ABCDEF"))
                        throw new CommandErrorException();
                
                    // beaconId
                beacon.setBeaconId("0123456789ABCDEF0123456789ABCDEF");
                beacon.update(superA);
                beacon.syncroFields(superA); 
                
                Logger.detail("------------ Check beaconId ------------");
                a = beacon.getBeaconId();							
                if (0 != a.compareTo("0123456789ABCDEF0123456789ABCDEF"))
                        throw new CommandErrorException();                

//#
//# Administrator can not update beacon
//#
                Logger.detail("------------ Administrator can not update beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the Beacon
                Apdu apdu = new Apdu(Apdu.UPDATE_BEACON_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_STATUS, Beacon.SE_BEACON_DISABLED);
                    // send command
                c.description = "Update Beacon";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not update beacon
//#

                Logger.detail("------------ user can not update beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the Beacon
                apdu = new Apdu(Apdu.UPDATE_BEACON_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_STATUS, Beacon.SE_BEACON_DISABLED);
                    // send command
                c.description = "Update beacon";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                beacon.delete(superA);
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

    DESCRIPTION: SuperA can delete a beacon. User and administrators can not delete a beacon.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException
    {
            String a, expectedRes;
            Beacon beacon;
            Command c = new Command();

            String testCode = testBatch+"/"+"Test Case 05";                

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                        // launch a ping
                d.ping();

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
//# Super A can delete a Beacon
//#
                Logger.detail("------------ Super A can delete a Beacon ------------");

                    // Create Beacon
                Logger.detail("------------ Create Beacon ------------");                        
                beacon = new Beacon(superA);

                beacon.delete(superA);                                               

                    // check that the Beacon has been deleted
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the Beacon
                Apdu apdu = new Apdu(Apdu.UPDATE_BEACON_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_BEACON_K, "01");
                    // send command
                c.description = "Update deleted Beacon";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);                        
//#
//# Administrator can not delete a Beacon
//#
                beacon = new Beacon(superA);

                Logger.detail("------------ Administrator can not delete a Beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the Beacon
                apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());
                    // send command
                c.description = "Delete Beacon";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not delete a beacon
//#

                Logger.detail("------------ user can not delete a beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the Beacon
                apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());

                    // send command
                c.description = "Delete Beacon";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                beacon.delete(superA);
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

    DESCRIPTION: Check that Create, update and delete Beacon updates the log.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException
    {
        Beacon beacon;
        int index;
        LogEntry le;
        String beaconId, requestorId;
        Command c = new Command();               
        String testCode = testBatch+"/"+"Test Case 06";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            d.ping();

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
//# Log is updated when a beacon is created
//#
            Logger.detail("------------ Log is updated when a beacon is created ------------");                        

            beacon = new Beacon(superA);
            beacon.syncroFields(superA);

            index = DeviceLogger.getLastEntryIndex(superA);	

            requestorId = superA.getObjectId();
            beaconId = beacon.getObjectId();

            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                    (0 != beaconId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_CREATE_EVENT.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result)))
                    {
                            throw new TestException();
            }                 
//#
//# Log is updated when a beacon is deleted;
//#
            Logger.detail("------------ Log is updated when a beacon is deleted; ------------");                        

            beacon.delete(superA);

            index = (index + 1) & 0xFFFF;	

            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) ||
                    (0 != beaconId.compareTo(le.objectId)) ||
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
    testCase07
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can updated the identity key of a Beacon object. User and Admin not.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException
    {
            String a, expectedRes;
            Beacon beacon;
            Command c = new Command();

            String testCode = testBatch+"/"+"Test Case 07";                

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                        // launch a ping
                d.ping();

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

                    // Create Beacon
                Logger.detail("------------ Create Beacon ------------");                        
                beacon = new Beacon(superA);
                beacon.syncroFields(superA);

//#
//# Super A can updated the identity key of a Beacon Object
//#
                    // Status
                beacon.setStatus(Beacon.SE_BEACON_DISABLED);
                beacon.setIdentityKey(superA,"00112233445566778899AABBCCDDEEFF");
                beacon.syncroFields(superA); 

//#
//# Administrator can not update the identity key of beacon
//#
                Logger.detail("------------ Administrator can not update beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the beacon
                Apdu apdu = new Apdu(Apdu.UPDATE_BEACON_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_BEACON_ID_KEY, "00112233445566778899AABBCCDDEEFF");
                    // send command
                c.description = "Update Beacon";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not update beacon
//#

                Logger.detail("------------ user can not update beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the beacon
                apdu = new Apdu(Apdu.UPDATE_BEACON_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_BEACON_ID_KEY, "00112233445566778899AABBCCDDEEFF");
                    // send command
                c.description = "Update beacon";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                beacon.delete(superA);
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
    testCase08
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can updated the counter of a Beacon object. User and Admin not.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase08() throws TestException
    {
            String a, expectedRes;
            Beacon beacon;
            Command c = new Command();

            String testCode = testBatch+"/"+"Test Case 08";                

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                        // launch a ping
                d.ping();

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

                    // Create Beacon
                Logger.detail("------------ Create Beacon ------------");                        
                beacon = new Beacon(superA);
                beacon.syncroFields(superA);

//#
//# Super A can updated the identity key of a Beacon Object
//#
                beacon.setCounter(superA,"00112233");
                beacon.syncroFields(superA); 

//#
//# Administrator can not update the identity key of beacon
//#
                Logger.detail("------------ Administrator can not update beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the beacon
                Apdu apdu = new Apdu(Apdu.UPDATE_BEACON_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_BEACON_COUNTER, "00112233");
                    // send command
                c.description = "Update Beacon";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not update beacon
//#

                Logger.detail("------------ user can not update beacon ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the beacon
                apdu = new Apdu(Apdu.UPDATE_BEACON_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, beacon.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_BEACON_COUNTER, "0011223344");
                    // send command
                c.description = "Update beacon";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                beacon.delete(superA);
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
    testCase09
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Resolve eID

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase09() throws TestException
    {
            Beacon beacon, beacon1;
            Command c = new Command();
            String testCase = testBatch +" /" + "Test Case 09"; 
            String a;

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    d.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a beacon object. Initial values are according to specs.
    //# 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                    String eId = "C68626360dfdf811";
                            
                        // Create Beacon
                    Logger.detail("------------ Create Beacon ------------");
                    
                    beacon1 = new Beacon(superA);
                    beacon1.setBeaconId("0102030405060708090A0B0C0D0E0F00");
                    beacon1.setRotationExponent("0B");
                    beacon1.update(superA);
                    beacon1.syncroFields(superA);
                    
                    Logger.detail("------------ Create Beacon ------------"); 
                    beacon = new Beacon(superA);
                    
                    beacon.setCounter(superA, counter);
                    
                    beacon.setIdentityKey(superA, identityKey);
                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);
                    
                    
                    a = Device.resolveEid(superA, eId);
                    if (0 != a.compareTo( beaconId ))
                            throw new ObjectException(); 
                    
    //#
    //# Object deletion
    //#                       
                    beacon.delete(superA);
                    beacon1.delete(superA);
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
    testCase09
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Dynamic eID resolution 

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase10() throws TestException
    {
            Beacon beacon, beacon1, beacon2;
            Command c = new Command();
            String testCase = testBatch +" /" + "Test Case 10"; 
            String a;
            
            Calendar calendar;
            Date date;
            long time0, time;
            int cntr;             

            // ---------------------- Code -------------------------------
            try
            {			                                     
                        // calculate T0
                    calendar = Calendar.getInstance();       
                       // Calculate t0
                    date = calendar.getTime();
                    time0 = date.getTime()/1000;
                    
                    Logger.testCase(testCase);

                            // launch a ping
                    d.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a beacon object. Initial values are according to specs.
    //# 
                    
                                            // Create Beacon
                    Logger.detail("------------ Create Beacon #1------------");
                    
                    beacon1 = new Beacon(superA);
                    beacon1.setBeaconId("0102030405060708090A0B0C0D0E0F00");
                    beacon1.setRotationExponent("0B");
                    beacon1.update(superA);
                    beacon1.syncroFields(superA);
                    
                    
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                    String eId = "C68626360dfdf811";
                                              
                    beacon = new Beacon(superA);
                    
                    beacon.setCounter(superA, counter);
                    
                    beacon.setIdentityKey(superA, identityKey);
                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);                                       
                    
                        // Resolve the beacon for one
                    for (int i=0; i<50; i++)
                    {

                            // Calculates t 
                        calendar = Calendar.getInstance();
                        date= calendar.getTime();
                        time = date.getTime()/1000;

                        cntr = ((int)(time - time0) & 0xFFFFFFFF) + 0x404;
                        Logger.detail("**********>  "+time0);
                        Logger.detail("**********>  "+time);
                        Logger.detail("**********>  "+cntr);
        
                        String simulatedeId = calculateEid(identityKey, (byte)10, cntr);
                    
                        a = Device.resolveEid(superA, simulatedeId);
                        if (0 != a.compareTo( beaconId ))
                                throw new ObjectException(); 
                        try{
                            Thread.sleep(10000);
                        }
                        catch(InterruptedException e){
                        }
                    }
                    Logger.detail("------------ Create Beacon #2------------");   
                        // calculate T0
                    calendar = Calendar.getInstance();       
                     date = calendar.getTime();
                    time0 = date.getTime()/1000;
                    
                    String identityKey2 = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId2 = "32323232323232323232323232323232";
                    String rotationExponent2 = "08";
                    String counter2 = "12345678";
                    
                    try{
                        Thread.sleep(10000);
                    }
                    catch(InterruptedException e){
                    }
                    Logger.detail(counter2);
                    beacon2 = new Beacon(superA);
                    beacon2.setRotationExponent(rotationExponent2);
                    beacon2.setBeaconId(beaconId2);
                    beacon2.setCrossTime("12");
                    beacon2.update(superA);
                    beacon2.syncroFields(superA);                                         
                    beacon2.setCounter(superA, counter2);                    
                    beacon2.setIdentityKey(superA, identityKey2);
                                       
                        // Resolve the beacon for one
                    for (int i=0; i<50; i++)
                    {

                            // Calculates t 
                        calendar = Calendar.getInstance();
                        date = calendar.getTime();
                        time = date.getTime()/1000;

                        cntr = ((int)(time - time0) & 0xFFFFFFFF) + 0x12345678;
                        Logger.detail("**********>  "+time0);
                        Logger.detail("**********>  "+time);
                        Logger.detail("**********>  "+cntr);
        
                        String simulatedeId = calculateEid(identityKey2, (byte)8, cntr);
                    
                        a = Device.resolveEid(superA, simulatedeId);
                        if (0 != a.compareTo( beaconId2 ))
                                throw new ObjectException(); 
                        try{
                            Thread.sleep(10000);
                        }
                        catch(InterruptedException e){
                        }
                    }                    
                    
    //#
    //# Object deletion
    //#                       
                    beacon.delete(superA);
                    beacon1.delete(superA);
                    beacon2.delete(superA);
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
    calculateEid
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Resolve eID

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static String calculateEid(String identityKey, byte rotationExponent, int counter)
    {               
        // ---------------------- Code -------------------------------
                
// ********************************** Calculate the temporary Key
        
        byte[] idKey = Util.stringToHexByte(identityKey);
        
        byte[] temporaryKey = new byte[16];	
	
        temporaryKey[11] = (byte)0xFF;
        
	temporaryKey[14] = (byte)(((counter>>24) & 0xFF));
	temporaryKey[15] = (byte)(((counter>>16)& 0xFF));
                
                // initial vector
        byte[] iv = new byte[16];
        IvParameterSpec ivPar = new IvParameterSpec(iv);
        
            // set the key
        Key keyObject = new SecretKeySpec(idKey, 0, 16, "AES");
                    
        try
        {
            // get the Cipher Instance, initialize the Cipher object and doFinal
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keyObject, ivPar);
            cipher.doFinal(temporaryKey, 0, temporaryKey.length, temporaryKey, 0);
        }
        catch (Exception e)
        {
            // error
            return null;
        }
// ********************************** Calculate eId        

        
                    // set the key
        keyObject = new SecretKeySpec(temporaryKey, 0, 16, "AES");
        ivPar = new IvParameterSpec(iv);
        
        int rotation = 0;
	for (int i=0; i<rotationExponent; i++)
	{
		rotation = rotation << 1;
		rotation |= 1;
	}

	rotation = ~rotation;
        
        counter = counter & rotation;

                byte[] eId = new byte[16];
        
	eId[11] = rotationExponent;
        eId[12] = (byte)(((counter>>24) & 0xFF));
        eId[13] = (byte)(((counter>>16) & 0xFF));
        eId[14] = (byte)(((counter>>8) & 0xFF));
        eId[15] = (byte)(((counter) & 0xFF));
        
        try
        {
            // get the Cipher Instance, initialize the Cipher object and doFinal
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keyObject, ivPar);
            cipher.doFinal(eId, 0, eId.length, eId, 0);
        }
        catch (Exception e)
        {
            // error
            return null;
        }                
        return (Util.bytesToHexString(eId)).substring(0, 16);
    }
}
