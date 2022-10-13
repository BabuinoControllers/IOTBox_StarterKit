
package TestCases;
    
import com.sdk.*;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

public class TestDoorWithProximity001 {
    
 /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestDoorWithProximity001";
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
                thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET,3,2000);     
                
                superA = new SuperA( RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);                 
                
                for(int u = 0; u<1; u++)
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
                    
                    testCase01();
                    j++;                    
                    if (u == 0)
                    {
                       /* Logger.detail("<-------------------------------->");
                        Logger.detail("<-------------------------------->"); 
                        Logger.detail("<-------------------------------->"); 
                        Logger.detail(" WIFI "); 
                        Logger.detail("<-------------------------------->"); 
                        Logger.detail("<-------------------------------->"); 
                        Logger.detail("<-------------------------------->"); 
                       
                        thisDevice = Device.discover(deviceId, Device.WIFI);
                        IoStream.setActiveDevice(thisDevice);
                        j=1;*/
                    }      
                }
         }
         catch ( DiscoveryException e){
            
             Logger.detail("TEST FAILURE ----->" + j);
            return false;
        }            
        
        return true;
    }
    /*----------------------------------------------------------------------------
    testCase01
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA update the beaconId of a door;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase01()
    {
            boolean result;
            String a;
            Door door;
            Command c = new Command();
            Beacon beacon;
            String testCase = testBatch +" /" + "Test Case 01";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
 
                        // Create a Beacon;
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                                              
                    beacon = new Beacon(superA);                    
                    beacon.setCounter(superA, counter);                    
                    beacon.setIdentityKey(superA, identityKey);                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);  

    //#
    //# Super A creates a door object. Beacon is set with a beacon ID
    //#
                        // Create Door       
                    door = new Door(superA);
                    door.setGpioId("00");
                    door.setMode(Door.SE_DOOR_MODE_ON_OFF);
                    door.setStatus(Door.SE_DOOR_ENABLED);
                    door.setBeaconId(beacon.getObjectId());
                    door.update(superA);
    //#
    //# Beacon Id is correct after syncronization
    //#                    
                    door.syncroFields(superA);
                    
                        // Check beaconId
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getBeaconId();
                    if (0 != a.compareTo(beacon.getObjectId()))
                        throw new ObjectException();

    //#
    //# Super A can not update beaconId field of a doorr with an object id not of beacon type;
    //#

                    String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A88_DATA_NOT_FOUND_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                    Apdu apdu = new Apdu(Apdu.UPDATE_DOOR_APDU);                    
                    apdu.addTlv(Atlv.DATA_TAG_BEACON_EID, superA.getObjectId());                    

                        // send command
                    c.description = "Update Door Object";
                    c.requester = superA;
                    a = apdu.toString();
                    c.execute(a, expectedRes);
                    
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
                    beacon.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException e)
            {
                    result = false;
            }

            Logger.testCase(testCase);
            Logger.testResult(result);		

            return result;
    }
    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA, admin and user can trigger a dorr in proximity. Eid List of one element

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase02()
    {
            boolean result;
            String gpioValue;
            String beaconList, policyId;
            AccessPolicy defaultPolicy;                        
            Door door;
            Command c = new Command();
            Beacon beacon;
            String testCase = testBatch +" /" + "Test Case 02";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
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
                    
                    user.setDefaultAccessPolicy(policyId);
                    user.update(superA);
                    
                        // Create a Beacon;
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                                              
                    beacon = new Beacon(superA);                    
                    beacon.setCounter(superA, counter);                    
                    beacon.setIdentityKey(superA, identityKey);                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);  

                        // Create Door       
                    door = new Door(superA);
                    door.setGpioId("00");
                    door.setMode(Door.SE_DOOR_MODE_ON_OFF);
                    door.setSecurity(Door.SE_DOOR_SECURITY_NO_PIN_EID);
                    door.setStatus(Door.SE_DOOR_ENABLED);
                    door.setBeaconId(beacon.getObjectId());
                    door.update(superA);
    //#
    //# SuperA can trigger a door passing the correct eId. EID List of one element
    //#                 
                    for (int i = 0; i<30; i++)
                    {                        
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        door.setOutputEid(superA, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(superA);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(superA, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(superA);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                    
    //#
    //# Admin can trigger a door passing the correct eId
    //#                 
                    for (int i = 0; i<30; i++)
                    {                        
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        door.setOutputEid(admin, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(admin);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(admin, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(admin);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                    
    //#
    //# User can trigger a door passing the correct eId
    //#                 
                    for (int i = 0; i<30; i++)
                    {                        
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        door.setOutputEid(user, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(user);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(user, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(user);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                                        
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
                    beacon.delete(superA);
                    user.delete(superA);
                    admin.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException | TestException e)
            {
                    result = false;
            }

            Logger.testCase(testCase);
            Logger.testResult(result);		

            return result;
    }
    /*----------------------------------------------------------------------------
    testCase03
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA, admin and user can trigger a dorr in proximity. Eid List of two element

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase03()
    {
            boolean result;
            String gpioValue;
            String beaconList, policyId;
            AccessPolicy defaultPolicy;                        
            Door door;
            Command c = new Command();
            Beacon beacon;
            String testCase = testBatch +" /" + "Test Case 03";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
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
                    
                    user.setDefaultAccessPolicy(policyId);
                    user.update(superA);
                    
                        // Create a Beacon;
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                                              
                    beacon = new Beacon(superA);                    
                    beacon.setCounter(superA, counter);                    
                    beacon.setIdentityKey(superA, identityKey);                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);  

                        // Create Door       
                    door = new Door(superA);
                    door.setGpioId("00");
                    door.setMode(Door.SE_DOOR_MODE_ON_OFF);
                    door.setSecurity(Door.SE_DOOR_SECURITY_NO_PIN_EID);
                    door.setStatus(Door.SE_DOOR_ENABLED);
                    door.setBeaconId(beacon.getObjectId());
                    door.update(superA);
    //#
    //# SuperA can trigger a door passing the correct eId. EID List of one element
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        beaconList = beaconList + "0102030405060708";
                        door.setOutputEid(superA, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(superA);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(superA, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(superA);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                    
    //#
    //# Admin can trigger a door passing the correct eId
    //#                 
                    for (int i = 0; i<1; i++)
                    {
                        beaconList = "0102030405060708";
                        beaconList = beaconList + TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        door.setOutputEid(admin, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(admin);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(admin, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(admin);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                    
    //#
    //# User can trigger a door passing the correct eId. Eid List of three elements
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "FF02030405060708";
                        door.setOutputEid(user, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(user);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(user, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(user);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                                        
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
                    beacon.delete(superA);
                    user.delete(superA);
                    admin.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException | TestException e)
            {
                    result = false;
            }

            Logger.testCase(testCase);
            Logger.testResult(result);		

            return result;
    }
    /*----------------------------------------------------------------------------
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA, admin and user can trigger a dorr in proximity. Eid List of four elements

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase04()
    {
            boolean result;
            String gpioValue;
            String beaconList, policyId;
            AccessPolicy defaultPolicy;                        
            Door door;
            Command c = new Command();
            Beacon beacon;
            String testCase = testBatch +" /" + "Test Case 04";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
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
                    
                    user.setDefaultAccessPolicy(policyId);
                    user.update(superA);
                    
                        // Create a Beacon;
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                                              
                    beacon = new Beacon(superA);                    
                    beacon.setCounter(superA, counter);                    
                    beacon.setIdentityKey(superA, identityKey);                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);  

                        // Create Door       
                    door = new Door(superA);
                    door.setGpioId("00");
                    door.setMode(Door.SE_DOOR_MODE_ON_OFF);
                    door.setSecurity(Door.SE_DOOR_SECURITY_NO_PIN_EID);
                    door.setStatus(Door.SE_DOOR_ENABLED);
                    door.setBeaconId(beacon.getObjectId());
                    door.update(superA);
    //#
    //# SuperA can trigger a door passing the correct eId. EID List of one element
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";
                        door.setOutputEid(superA, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(superA);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(superA, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(superA);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                    
    //#
    //# Admin can trigger a door passing the correct eId
    //#                 
                    for (int i = 0; i<1; i++)
                    {
                        beaconList = "0102030405060708";
                        beaconList = beaconList + TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";
                        door.setOutputEid(admin, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(admin);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(admin, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(admin);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                    
    //#
    //# User can trigger a door passing the correct eId. Eid List of three elements
    //#                 
                    for (int i = 0; i<1; i++)
                    {   
                        beaconList = "0102030405060708";
                        beaconList = beaconList + "FF02030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);

                        door.setOutputEid(user, Door.DOOR_COMMAND_SWITCH_ON, beaconList);
                        gpioValue = door.getOutput(user);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(user, Door.DOOR_COMMAND_SWITCH_OFF, beaconList);
                        gpioValue = door.getOutput(user);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                                        
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
                    beacon.delete(superA);
                    user.delete(superA);
                    admin.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException | TestException e)
            {
                    result = false;
            }

            Logger.testCase(testCase);
            Logger.testResult(result);		

            return result;
    }
    /*----------------------------------------------------------------------------
    testCase05
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA, admin and user can trigger a dorr in proximity and with PIN. 
                Eid List of four elements

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase05()
    {
            boolean result;
            String gpioValue;
            String beaconList, policyId;
            AccessPolicy defaultPolicy;                        
            Door door;
            Command c = new Command();
            Beacon beacon;
            String testCase = testBatch +" /" + "Test Case 05";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    superA.setPin(superA, "01020304");                    
                    
                        // object created
                    User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                    admin.updateKey("newAdministratorKeysCiccia");
                    admin.syncroFields(admin);
                    admin.setPin(admin, "01020304");

                        // object created
                    User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                    user.updateKey("newSutta");
                    user.syncroFields(admin);
                    user.setPin(user, "01020304");
                    
                        // set the policy at always            
                    policyId = admin.getAcPolicy();
                    defaultPolicy = new AccessPolicy(admin, policyId);
                    defaultPolicy.setAlwaysWeeklyPolicy();
                    defaultPolicy.update(superA);
                    
                    user.setDefaultAccessPolicy(policyId);
                    user.update(superA);
                    
                        // Create a Beacon;
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                                              
                    beacon = new Beacon(superA);                    
                    beacon.setCounter(superA, counter);                    
                    beacon.setIdentityKey(superA, identityKey);                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);  

                        // Create Door       
                    door = new Door(superA);
                    door.setGpioId("00");
                    door.setMode(Door.SE_DOOR_MODE_ON_OFF);
                    door.setSecurity(Door.SE_DOOR_SECURITY_PIN_EID);
                    door.setStatus(Door.SE_DOOR_ENABLED);
                    door.setBeaconId(beacon.getObjectId());
                    door.update(superA);
    //#
    //# SuperA can trigger a door passing the correct eId. EID List of one element
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";
                        door.setOutputEid(superA, Door.DOOR_COMMAND_SWITCH_ON, "01020304", beaconList);
                        gpioValue = door.getOutput(superA);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(superA, Door.DOOR_COMMAND_SWITCH_OFF, "01020304", beaconList);
                        gpioValue = door.getOutput(superA);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                    
    //#
    //# Admin can trigger a door passing the correct eId
    //#                 
                    for (int i = 0; i<1; i++)
                    {
                        beaconList = "0102030405060708";
                        beaconList = beaconList + TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";
                        door.setOutputEid(admin, Door.DOOR_COMMAND_SWITCH_ON, "01020304", beaconList);
                        gpioValue = door.getOutput(admin);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(admin, Door.DOOR_COMMAND_SWITCH_OFF, "01020304", beaconList);
                        gpioValue = door.getOutput(admin);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                    
    //#
    //# User can trigger a door passing the correct eId. Eid List of three elements
    //#                 
                    for (int i = 0; i<1; i++)
                    {   
                        beaconList = "0102030405060708";
                        beaconList = beaconList + "FF02030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = TestBeacon001.calculateEid(identityKey, (byte)0x0A, 0x404);

                        door.setOutputEid(user, Door.DOOR_COMMAND_SWITCH_ON, "01020304", beaconList);
                        gpioValue = door.getOutput(user);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                        {
                            throw new TestException();
                        } 

                        door.setOutputEid(user, Door.DOOR_COMMAND_SWITCH_OFF, "01020304", beaconList);
                        gpioValue = door.getOutput(user);
                        if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                        {
                             throw new TestException();
                         }                        
                    }                                        
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
                    beacon.delete(superA);
                    user.delete(superA);
                    admin.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException | TestException e)
            {
                    result = false;
            }

            Logger.testCase(testCase);
            Logger.testResult(result);		

            return result;
    }
  /*----------------------------------------------------------------------------
    testCase06
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: if eId is not in the eId list or the eid list has a wrong length then 
                the door command fails. This test checks the PIN security.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase06()
    {
            boolean result;
            String a, beaconList, policyId;
            AccessPolicy defaultPolicy;                        
            Door door;
            Command c = new Command();
            Beacon beacon;
            String testCase = testBatch +" /" + "Test Case 06";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    superA.setPin(superA, "01020304");                    
                    
                        // object created
                    User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                    admin.updateKey("newAdministratorKeysCiccia");
                    admin.syncroFields(admin);
                    admin.setPin(admin, "01020304");

                        // object created
                    User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                    user.updateKey("newSutta");
                    user.syncroFields(admin);
                    user.setPin(user, "01020304");
                    
                        // set the policy at always            
                    policyId = admin.getAcPolicy();
                    defaultPolicy = new AccessPolicy(admin, policyId);
                    defaultPolicy.setAlwaysWeeklyPolicy();
                    defaultPolicy.update(superA);
                    
                    user.setDefaultAccessPolicy(policyId);
                    user.update(superA);
                    
                        // Create a Beacon;
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                                              
                    beacon = new Beacon(superA);                    
                    beacon.setCounter(superA, counter);                    
                    beacon.setIdentityKey(superA, identityKey);                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);  

                        // Create Door       
                    door = new Door(superA);
                    door.setGpioId("00");
                    door.setMode(Door.SE_DOOR_MODE_ON_OFF);
                    door.setSecurity(Door.SE_DOOR_SECURITY_PIN_EID);
                    door.setStatus(Door.SE_DOOR_ENABLED);
                    door.setBeaconId(beacon.getObjectId());
                    door.update(superA);
    //#
    //# If the eId is not in the list then the door command reqested by superA fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");
                        apdu.addTlv(Apdu.DATA_TAG_BEACON_EID_LIST, beaconList);                   

                            // send command
                        c.description = "Update Door Object";
                        c.requester = superA;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }
    //#
    //# If the eId is not in the list then the door command reqested by admin fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");
                        apdu.addTlv(Apdu.DATA_TAG_BEACON_EID_LIST, beaconList);                   

                            // send command
                        c.description = "Update Door Object";
                        c.requester = admin;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }
    //#
    //# If the eId is not in the list then the door command reqested by admin fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");
                        apdu.addTlv(Apdu.DATA_TAG_BEACON_EID_LIST, beaconList);                   

                            // send command
                        c.description = "Update Door Object";
                        c.requester = user;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }
    //#
    //# eId list has a length not multiple of 8
    //#             
                    Logger.detail("------------ eID List not multiple of 8 ------------");
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");
                        apdu.addTlv(Apdu.DATA_TAG_BEACON_EID_LIST, beaconList.substring(0, 48));                   

                            // send command
                        c.description = "Update Door Object";
                        c.requester = user;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }                     
                    
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
                    beacon.delete(superA);
                    user.delete(superA);
                    admin.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException  e)
            {
                    result = false;
            }

            Logger.testCase(testCase);
            Logger.testResult(result);		

            return result;
    }
    /*--------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: if eId is not in the eId list or the eid list has a wrong length then 
                the door command fails. THis test checks the NO PIN security with EID.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase07()
    {
            boolean result;
            String a, beaconList, policyId;
            AccessPolicy defaultPolicy;                        
            Door door;
            Command c = new Command();
            Beacon beacon;
            String testCase = testBatch +" /" + "Test Case 07";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    superA.setPin(superA, "01020304");                    
                    
                        // object created
                    User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                    admin.updateKey("newAdministratorKeysCiccia");
                    admin.syncroFields(admin);
                    admin.setPin(admin, "01020304");

                        // object created
                    User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                    user.updateKey("newSutta");
                    user.syncroFields(admin);
                    user.setPin(user, "01020304");
                    
                        // set the policy at always            
                    policyId = admin.getAcPolicy();
                    defaultPolicy = new AccessPolicy(admin, policyId);
                    defaultPolicy.setAlwaysWeeklyPolicy();
                    defaultPolicy.update(superA);
                    
                    user.setDefaultAccessPolicy(policyId);
                    user.update(superA);
                    
                        // Create a Beacon;
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                                              
                    beacon = new Beacon(superA);                    
                    beacon.setCounter(superA, counter);                    
                    beacon.setIdentityKey(superA, identityKey);                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);  

                        // Create Door       
                    door = new Door(superA);
                    door.setGpioId("00");
                    door.setMode(Door.SE_DOOR_MODE_ON_OFF);
                    door.setSecurity(Door.SE_DOOR_SECURITY_NO_PIN_EID);
                    door.setStatus(Door.SE_DOOR_ENABLED);
                    door.setBeaconId(beacon.getObjectId());
                    door.update(superA);
    //#
    //# If the eId is not in the list then the door command reqested by superA fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");
                        apdu.addTlv(Apdu.DATA_TAG_BEACON_EID_LIST, beaconList);                   

                            // send command
                        c.description = "Update Door Object";
                        c.requester = superA;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }
    //#
    //# If the eId is not in the list then the door command reqested by admin fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");
                        apdu.addTlv(Apdu.DATA_TAG_BEACON_EID_LIST, beaconList);                   

                            // send command
                        c.description = "Update Door Object";
                        c.requester = admin;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }
    //#
    //# If the eId is not in the list then the door command reqested by admin fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");
                        apdu.addTlv(Apdu.DATA_TAG_BEACON_EID_LIST, beaconList);                   

                            // send command
                        c.description = "Update Door Object";
                        c.requester = user;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }                                                             
                    
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
                    beacon.delete(superA);
                    user.delete(superA);
                    admin.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException  e)
            {
                    result = false;
            }

            Logger.testCase(testCase);
            Logger.testResult(result);		

            return result;
    }
    /*--------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: if proximity authentication is required and the eId llist is not in the command then
                    the command fails.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase08()
    {
            boolean result;
            String a, beaconList, policyId;
            AccessPolicy defaultPolicy;                        
            Door door;
            Command c = new Command();
            Beacon beacon;
            String testCase = testBatch +" /" + "Test Case 08";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    superA.setPin(superA, "01020304");                    
                    
                        // object created
                    User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                    admin.updateKey("newAdministratorKeysCiccia");
                    admin.syncroFields(admin);
                    admin.setPin(admin, "01020304");

                        // object created
                    User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                    user.updateKey("newSutta");
                    user.syncroFields(admin);
                    user.setPin(user, "01020304");
                    
                        // set the policy at always            
                    policyId = admin.getAcPolicy();
                    defaultPolicy = new AccessPolicy(admin, policyId);
                    defaultPolicy.setAlwaysWeeklyPolicy();
                    defaultPolicy.update(superA);
                    
                    user.setDefaultAccessPolicy(policyId);
                    user.update(superA);
                    
                        // Create a Beacon;
                    Logger.detail("------------ Create Beacon ------------"); 
                    String identityKey = "FAA70C7DC91120EC575A42EB08744D4A";
                    String beaconId = "31313131313131313131313131313131";
                    String rotationExponent = "0A";
                    String counter = "00000404";
                                              
                    beacon = new Beacon(superA);                    
                    beacon.setCounter(superA, counter);                    
                    beacon.setIdentityKey(superA, identityKey);                    
                    beacon.setRotationExponent(rotationExponent);
                    beacon.setBeaconId(beaconId);
                    beacon.update(superA);
                    beacon.syncroFields(superA);  

                        // Create Door       
                    door = new Door(superA);
                    door.setGpioId("00");
                    door.setMode(Door.SE_DOOR_MODE_ON_OFF);
                    door.setSecurity(Door.SE_DOOR_SECURITY_NO_PIN_EID);
                    door.setStatus(Door.SE_DOOR_ENABLED);
                    door.setBeaconId(beacon.getObjectId());
                    door.update(superA);
    //#
    //# If the eId is not in the list then the door command reqested by superA fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");                   

                            // send command
                        c.description = "Update Door Object";
                        c.requester = superA;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }
    //#
    //# If the eId is not in the list then the door command reqested by admin fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");                                           

                            // send command
                        c.description = "Update Door Object";
                        c.requester = admin;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }
    //#
    //# If the eId is not in the list then the door command reqested by admin fails.
    //#                 
                    for (int i = 0; i<1; i++)
                    {                        
                        beaconList = "FFFFFFFFFFFFFFFF";
                        beaconList = beaconList + "0102030405060708";
                        beaconList = beaconList + "F102030405060708";
                        beaconList = beaconList + "F202030405060708";

                        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6A52_UNRESOLVED_BEACON_EID_ERROR +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

                            // create the apdu object
                        Apdu apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "01020304");                  

                            // send command
                        c.description = "Update Door Object";
                        c.requester = user;
                        a = apdu.toString();
                        c.execute(a, expectedRes);
                    }                                                             
                    
    //#
    //# Object deletion
    //#                       
                    door.delete(superA);
                    beacon.delete(superA);
                    user.delete(superA);
                    admin.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

            }
            catch (CommandErrorException | ObjectException | IOException  e)
            {
                    result = false;
            }

            Logger.testCase(testCase);
            Logger.testResult(result);		

            return result;
    }        
}
