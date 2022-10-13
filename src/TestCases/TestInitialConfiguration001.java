package TestCases;

import com.sdk.*;
import java.io.IOException;

public class TestInitialConfiguration001 {
    
    /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestInitialConfiguration001";
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
                
//                scriptAid4Doors6Sensors();
                
                testCase01();
                j++;

                testCase02();
                j++;
                
                testCase03();
                j++;

                testCase04();
                j++;

               // testCase05(); Door 4 has been removed from the configuration
                j++;

               // testCase06(); Door 5 has been removed from the configuration
                j++;
                
               // testCase07(); Door 6 has been removed from the configuration
                j++;
                
               // testCase08(); Door 7 has been removed from the configuration
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
                         
        }
        catch (TestException | DiscoveryException e){
            
             Logger.detail("TEST FAILURE ----->" + j);
            return false;
        }            
        
        return true;
    }
    /*******************************************************************************
     * testInitialConfiguration001: It checks the initial configuration of Door 0;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase01() throws TestException
    {
            String a, name;
            Door door;
            String testCase = testBatch +" /" + "TestCase 01";               

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
                    door = new Door(superA, "00000007");
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = "Access 0";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();

                        // Check Security 
                    Logger.detail("------------ Check Initial Security ------------");
                    a = door.getSecurity();
                    if (0 != a.compareTo( Door.SE_DOOR_SECURITY_NO_PIN))
                            throw new ObjectException();

                        // Time 
                    Logger.detail("------------ Check InitialTime Value ------------");
                    a = door.getPulseDuration();
                    if (0 != a.compareTo("03E8"))
                            throw new ObjectException();

                        // Initial Value
                    Logger.detail("------------ Check Initial Initial Value------------");
                    a = "000000" + door.getInitialOutputValue();
                    if (0 != a.compareTo( Door.SE_DOOR_STATUS_LOW))
                            throw new ObjectException();

                        // Mode                     
                    Logger.detail("------------ Mode------------");
                    a = door.getMode();
                    if (0 != a.compareTo( Door.SE_DOOR_MODE_PULSE))
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
    /*******************************************************************************
     * testInitialConfiguration002: It checks the initial configuration of Door 1;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase02() throws TestException
    {
            String a, name;
            Door door;
            String testCase = testBatch +" /" + "TestCase 02";               

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
                    door = new Door(superA, "00000008");
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = "Access 1";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( "01"))
                            throw new ObjectException();

                        // Check Security 
                    Logger.detail("------------ Check Initial Security ------------");
                    a = door.getSecurity();
                    if (0 != a.compareTo( Door.SE_DOOR_SECURITY_NO_PIN))
                            throw new ObjectException();

                        // Time 
                    Logger.detail("------------ Check InitialTime Value ------------");
                    a = door.getPulseDuration();
                    if (0 != a.compareTo("03E8"))
                            throw new ObjectException();

                        // Initial Value
                    Logger.detail("------------ Check Initial Initial Value------------");
                    a = "000000" + door.getInitialOutputValue();
                    if (0 != a.compareTo( Door.SE_DOOR_STATUS_LOW))
                            throw new ObjectException();

                        // Mode                     
                    Logger.detail("------------ Mode------------");
                    a = door.getMode();
                    if (0 != a.compareTo( Door.SE_DOOR_MODE_PULSE))
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
    /*******************************************************************************
     * testInitialConfiguration003: It checks the initial configuration of Door 2;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase03() throws TestException
    {
            String a, name;
            Door door;
            String testCase = testBatch +" /" + "TestCase 03";               

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
                    door = new Door(superA, "00000009");
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = "Access 2";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( "02"))
                            throw new ObjectException();

                        // Check Security 
                    Logger.detail("------------ Check Initial Security ------------");
                    a = door.getSecurity();
                    if (0 != a.compareTo( Door.SE_DOOR_SECURITY_NO_PIN))
                            throw new ObjectException();

                        // Time 
                    Logger.detail("------------ Check InitialTime Value ------------");
                    a = door.getPulseDuration();
                    if (0 != a.compareTo("03E8"))
                            throw new ObjectException();

                        // Initial Value
                    Logger.detail("------------ Check Initial Initial Value------------");
                    a = "000000" + door.getInitialOutputValue();
                    if (0 != a.compareTo( Door.SE_DOOR_STATUS_LOW))
                            throw new ObjectException();

                        // Mode                     
                    Logger.detail("------------ Mode------------");
                    a = door.getMode();
                    if (0 != a.compareTo( Door.SE_DOOR_MODE_PULSE))
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
   /*******************************************************************************
     * testInitialConfiguration004: It checks the initial configuration of Door 3;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase04() throws TestException
    {
            String a, name;
            Door door;
            String testCase = testBatch +" /" + "TestCase 04";               

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
                    door = new Door(superA, "0000000A");
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = "Access 3";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( "03"))
                            throw new ObjectException();

                        // Check Security 
                    Logger.detail("------------ Check Initial Security ------------");
                    a = door.getSecurity();
                    if (0 != a.compareTo( Door.SE_DOOR_SECURITY_NO_PIN))
                            throw new ObjectException();

                        // Time 
                    Logger.detail("------------ Check InitialTime Value ------------");
                    a = door.getPulseDuration();
                    if (0 != a.compareTo("03E8"))
                            throw new ObjectException();

                        // Initial Value
                    Logger.detail("------------ Check Initial Initial Value------------");
                    a = "000000" + door.getInitialOutputValue();
                    if (0 != a.compareTo( Door.SE_DOOR_STATUS_LOW))
                            throw new ObjectException();

                        // Mode                     
                    Logger.detail("------------ Mode------------");
                    a = door.getMode();
                    if (0 != a.compareTo( Door.SE_DOOR_MODE_PULSE))
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
   /*******************************************************************************
     * testInitialConfiguration005: It checks the initial configuration of Door 4;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase05() throws TestException
    {
            String a, name;
            Door door;
            String testCase = testBatch +" /" + "TestCase 05";               

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
                    door = new Door(superA, "0000000B");
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = "Access 4";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( "04"))
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
   /*******************************************************************************
     * testInitialConfiguration006: It checks the initial configuration of Door 5;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase06() throws TestException
    {
            String a, name;
            Door door;
            String testCase = testBatch +" /" + "TestCase 06";               

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
                    door = new Door(superA, "0000000C");
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = "Access 5";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( "05"))
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
     /*******************************************************************************
     * testInitialConfiguration007: It checks the initial configuration of Door 6;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase07() throws TestException
    {
            String a, name;
            Door door;
            String testCase = testBatch +" /" + "TestCase 07";               

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
                    door = new Door(superA, "0000000D");
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = "Access 6";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( "06"))
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
    /*******************************************************************************
     * testInitialConfiguration008: It checks the initial configuration of Door 6;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase08() throws TestException
    {
            String a, name;
            Door door;
            String testCase = testBatch +" /" + "TestCase 08";               

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
                    door = new Door(superA, "0000000E");
                    door.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = door.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = door.getStatus();
                    if (0 != a.compareTo( Door.SE_DOOR_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = door.getName();			
                    name = "Access 7";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial Retry gpioID ------------");
                    a = door.getGpioId();
                    if (0 != a.compareTo( "07"))
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
   /*******************************************************************************
     * testInitialConfiguration001: It checks the initial configuration of Sensor 0;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase09() throws TestException
    {
            String a, name;
            Sensor sensor;
            String testCase = testBatch +" /" + "TestCase 09";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a Sensor object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Sensor ------------");                        
                    sensor = new Sensor(superA, "0000000B");
                    sensor.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = sensor.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = sensor.getStatus();
                    if (0 != a.compareTo( Sensor.SENSOR_STATUS_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = sensor.getName();			
                    name = "I0 Logic";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial gpioID ------------");
                    a = sensor.getGpioId();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();
                           // Check Event 

                    Logger.detail("------------ Event ------------");
                    a = sensor.getEvent();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();

                        // type 
                    Logger.detail("------------ Check type Value ------------");
                    a = sensor.getType();
                    if (0 != a.compareTo(Sensor.SENSOR_ANALOG_TYPE))
                            throw new ObjectException();

                        // lowTreshold
                    Logger.detail("------------ Check Initial LowThreshold------------");
                    a = sensor.getLowThreshold();
                    if (0 != a.compareTo("00000320"))
                            throw new ObjectException();

                        // highTreshold                     
                    Logger.detail("------------ highThreshold------------");
                    a = sensor.getHighThreshold();
                    if (0 != a.compareTo( "00000ED8"))
                            throw new ObjectException();

                        // Device Extension Object Id
                    Logger.detail("------------ DeviceExtensionId------------");
                    a = sensor.getDeviceExtensionId();
                    if (0 != a.compareTo( DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                            throw new ObjectException(); 

    //#
    //# Object deletion
    //#                       
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
  /*******************************************************************************
     * testInitialConfiguration001: It checks the initial configuration of Sensor 0;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase10() throws TestException
    {
            String a, name;
            Sensor sensor;
            String testCase = testBatch +" /" + "TestCase 010";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a Sensor object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Sensor ------------");                        
                    sensor = new Sensor(superA, "0000000C");
                    sensor.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = sensor.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = sensor.getStatus();
                    if (0 != a.compareTo( Sensor.SENSOR_STATUS_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = sensor.getName();			
                    name = "I1 Logic";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial gpioID ------------");
                    a = sensor.getGpioId();
                    if (0 != a.compareTo( "01"))
                            throw new ObjectException();
                           // Check Event 

                    Logger.detail("------------ Event ------------");
                    a = sensor.getEvent();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();

                        // type 
                    Logger.detail("------------ Check type Value ------------");
                    a = sensor.getType();
                    if (0 != a.compareTo(Sensor.SENSOR_ANALOG_TYPE))
                            throw new ObjectException();

                        // lowTreshold
                    Logger.detail("------------ Check Initial LowThreshold------------");
                    a = sensor.getLowThreshold();
                    if (0 != a.compareTo("00000320"))
                            throw new ObjectException();

                        // highTreshold                     
                    Logger.detail("------------ highThreshold------------");
                    a = sensor.getHighThreshold();
                    if (0 != a.compareTo( "00000ED8"))
                            throw new ObjectException();

                        // Device Extension Object Id
                    Logger.detail("------------ DeviceExtensionId------------");
                    a = sensor.getDeviceExtensionId();
                    if (0 != a.compareTo( DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                            throw new ObjectException(); 

    //#
    //# Object deletion
    //#                       
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
    /*******************************************************************************
     * testInitialConfiguration001: It checks the initial configuration of Sensor 0;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase11() throws TestException
    {
            String a, name;
            Sensor sensor;
            String testCase = testBatch +" /" + "TestCase 011";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a Sensor object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Sensor ------------");                        
                    sensor = new Sensor(superA, "0000000D");
                    sensor.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = sensor.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = sensor.getStatus();
                    if (0 != a.compareTo( Sensor.SENSOR_STATUS_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = sensor.getName();			
                    name = "I2 Logic";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial gpioID ------------");
                    a = sensor.getGpioId();
                    if (0 != a.compareTo( "02"))
                            throw new ObjectException();
                           // Check Event 

                    Logger.detail("------------ Event ------------");
                    a = sensor.getEvent();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();

                        // type 
                    Logger.detail("------------ Check type Value ------------");
                    a = sensor.getType();
                    if (0 != a.compareTo(Sensor.SENSOR_ANALOG_TYPE))
                            throw new ObjectException();

                        // lowTreshold
                    Logger.detail("------------ Check Initial LowThreshold------------");
                    a = sensor.getLowThreshold();
                    if (0 != a.compareTo("00000320"))
                            throw new ObjectException();

                        // highTreshold                     
                    Logger.detail("------------ highThreshold------------");
                    a = sensor.getHighThreshold();
                    if (0 != a.compareTo( "00000ED8"))
                            throw new ObjectException();

                        // Device Extension Object Id
                    Logger.detail("------------ DeviceExtensionId------------");
                    a = sensor.getDeviceExtensionId();
                    if (0 != a.compareTo( DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                            throw new ObjectException(); 

    //#
    //# Object deletion
    //#                       
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
/*******************************************************************************
     * testInitialConfiguration001: It checks the initial configuration of Sensor 3;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase12() throws TestException
    {
            String a, name;
            Sensor sensor;
            String testCase = testBatch +" /" + "TestCase 012";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a Sensor object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Sensor ------------");                        
                    sensor = new Sensor(superA, "0000000E");
                    sensor.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = sensor.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = sensor.getStatus();
                    if (0 != a.compareTo( Sensor.SENSOR_STATUS_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = sensor.getName();			
                    name = "I3 Logic";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial gpioID ------------");
                    a = sensor.getGpioId();
                    if (0 != a.compareTo( "03"))
                            throw new ObjectException();
                           // Check Event 

                    Logger.detail("------------ Event ------------");
                    a = sensor.getEvent();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();

                        // type 
                    Logger.detail("------------ Check type Value ------------");
                    a = sensor.getType();
                    if (0 != a.compareTo(Sensor.SENSOR_ANALOG_TYPE))
                            throw new ObjectException();

                        // lowTreshold
                    Logger.detail("------------ Check Initial LowThreshold------------");
                    a = sensor.getLowThreshold();
                    if (0 != a.compareTo("00000320"))
                            throw new ObjectException();

                        // highTreshold                     
                    Logger.detail("------------ highThreshold------------");
                    a = sensor.getHighThreshold();
                    if (0 != a.compareTo( "00000ED8"))
                            throw new ObjectException();

                        // Device Extension Object Id
                    Logger.detail("------------ DeviceExtensionId------------");
                    a = sensor.getDeviceExtensionId();
                    if (0 != a.compareTo( DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                            throw new ObjectException(); 

    //#
    //# Object deletion
    //#                       
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
     /*******************************************************************************
     * testInitialConfiguration001: It checks the initial configuration of Sensor 4;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase13() throws TestException
    {
            String a, name;
            Sensor sensor;
            String testCase = testBatch +" /" + "TestCase 013";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a Sensor object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Sensor ------------");                        
                    sensor = new Sensor(superA, "0000000F");
                    sensor.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = sensor.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = sensor.getStatus();
                    if (0 != a.compareTo( Sensor.SENSOR_STATUS_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = sensor.getName();			
                    name = "I4 Logic";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial gpioID ------------");
                    a = sensor.getGpioId();
                    if (0 != a.compareTo( "04"))
                            throw new ObjectException();
                           // Check Event 

                    Logger.detail("------------ Event ------------");
                    a = sensor.getEvent();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();

                        // type 
                    Logger.detail("------------ Check type Value ------------");
                    a = sensor.getType();
                    if (0 != a.compareTo(Sensor.SENSOR_ANALOG_TYPE))
                            throw new ObjectException();

                        // lowTreshold
                    Logger.detail("------------ Check Initial LowThreshold------------");
                    a = sensor.getLowThreshold();
                    if (0 != a.compareTo("00000320"))
                            throw new ObjectException();

                        // highTreshold                     
                    Logger.detail("------------ highThreshold------------");
                    a = sensor.getHighThreshold();
                    if (0 != a.compareTo( "00000ED8"))
                            throw new ObjectException();

                        // Device Extension Object Id
                    Logger.detail("------------ DeviceExtensionId------------");
                    a = sensor.getDeviceExtensionId();
                    if (0 != a.compareTo( DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                            throw new ObjectException(); 

    //#
    //# Object deletion
    //#                       
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
     
/*******************************************************************************
     * testInitialConfiguration001: It checks the initial configuration of Sensor 5;
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase14() throws TestException
    {
            String a, name;
            Sensor sensor;
            String testCase = testBatch +" /" + "TestCase 014";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates a Sensor object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Sensor ------------");                        
                    sensor = new Sensor(superA, "00000010");
                    sensor.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = sensor.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = sensor.getStatus();
                    if (0 != a.compareTo( Sensor.SENSOR_STATUS_ENABLED ))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = sensor.getName();			
                    name = "I5 OHM";
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // GPIOiD 
                    Logger.detail("------------ Check Initial gpioID ------------");
                    a = sensor.getGpioId();
                    if (0 != a.compareTo( "05"))
                            throw new ObjectException();
                           // Check Event 

                    Logger.detail("------------ Event ------------");
                    a = sensor.getEvent();
                    if (0 != a.compareTo( "00"))
                            throw new ObjectException();

                        // type 
                    Logger.detail("------------ Check type Value ------------");
                    a = sensor.getType();
                    if (0 != a.compareTo(Sensor.SENSOR_OHM_TYPE))
                            throw new ObjectException();

                        // lowTreshold
                    Logger.detail("------------ Check Initial LowThreshold------------");
                    a = sensor.getLowThreshold();
                    if (0 != a.compareTo("00000000"))
                            throw new ObjectException();

                        // highTreshold                     
                    Logger.detail("------------ highThreshold------------");
                    a = sensor.getHighThreshold();
                    if (0 != a.compareTo( "00000000"))
                            throw new ObjectException();

                        // Device Extension Object Id
                    Logger.detail("------------ DeviceExtensionId------------");
                    a = sensor.getDeviceExtensionId();
                    if (0 != a.compareTo( DeviceExtender.SE_NULL_DEVICE_EXTENDER_OBJECT_ID))
                            throw new ObjectException(); 

    //#
    //# Object deletion
    //#                       
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
/*******************************************************************************
     * testInitialConfiguration001: It checks applicaiton version and application pack
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
     public static void testCase15() throws TestException
    {
            String a;
            String testCase = testBatch +" /" + "TestCase 015";

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

    //#
    //# Get Applicaiton Version.
    //# 
                        // Check applicaiton Version
                    Logger.detail("------------ Check Applicaiton Version ------------");
                    a = "2.1.0"; // Ver 2.0.0
                    String x = thisDevice.getAppPackVersion();
                    if (0 != a.compareTo(x))
                            throw new ObjectException();

                        // check applicaiton pack
                    Logger.detail("------------ Check Applicaiton Pack ------------");
                    a = "0001"; // Ver 0001
                    if (0 != a.compareTo(thisDevice.getAppPackType()))
                            throw new ObjectException();

    //#
    //# Object deletion
    //#                                              

            }
            catch ( ObjectException | IOException e)
            {
                    Logger.testResult(false);		
                    TestException t = new TestException();
                    throw t;
		}
		
		Logger.testCase(testCase);
		Logger.testResult(true);
    }         
    /*******************************************************************************
     * It run the master script to derive the initialization script array
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
    public static void scriptAid8Door() throws TestException
    {
        AccessPolicy defaultPolicy;              
        String testCode = testBatch+"/"+"Test Case 01";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	
         
//#
//# Super A can trigger a door
//#
            Logger.detail("------------ Check initial configuration Object creation ------------");  
            
                    String defaultPolicyId = superA.getAcPolicy();
                    defaultPolicy = new AccessPolicy(superA, defaultPolicyId);
                    defaultPolicy.setAlwaysWeeklyPolicy();
                    defaultPolicy.update(superA);

                    // creates and initialises doors 
                    Door door0;
                    door0 = new Door(superA);
                    door0.setGpioId("00");
                    door0.setName("Access 0");
                    door0.setStatus(Door.SE_DOOR_ENABLED);
                    door0.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door0.update(superA);

                    // creates and initialises doors
                    Door door1;
                    door1 = new Door(superA);
                    door1.setGpioId("01");
                    door1.setName("Access 1");
                    door1.setStatus(Door.SE_DOOR_ENABLED);
                    door1.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door1.update(superA);

                    // creates and initialises doors
                    Door door2;
                    door2 = new Door(superA);
                    door2.setGpioId("02");
                    door2.setName("Access 2");
                    door2.setStatus(Door.SE_DOOR_ENABLED);
                    door2.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door2.update(superA);

                    // creates and initialises doors
                    Door door3;
                    door3 = new Door(superA);
                    door3.setGpioId("03");
                    door3.setName("Access 3");
                    door3.setStatus(Door.SE_DOOR_ENABLED);
                    door3.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door3.update(superA);

                    // creates and initialises doors
                    Door door4;
                    door4 = new Door(superA);
                    door4.setGpioId("04");
                    door4.setName("Access 4");
                    door4.setStatus(Door.SE_DOOR_ENABLED);
                    door4.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door4.update(superA);

                    // creates and initialises doors
                    Door door5;
                    door5 = new Door(superA);
                    door5.setGpioId("05");
                    door5.setName("Access 5");
                    door5.setStatus(Door.SE_DOOR_ENABLED);
                    door5.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door5.update(superA);

                    // creates and initialises doors
                    Door door6;
                    door6 = new Door(superA);
                    door6.setGpioId("06");
                    door6.setName("Access 6");
                    door6.setStatus(Door.SE_DOOR_ENABLED);
                    door6.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door6.update(superA);

                    // creates and initialises doors
                    Door door7;
                    door7 = new Door(superA);
                    door7.setGpioId("07");
                    door7.setName("Access 7");
                    door7.setStatus(Door.SE_DOOR_ENABLED);
                    door7.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door7.update(superA);

    //#
    //# Object deletion
    //#
            door0.delete(superA);
            door1.delete(superA);
            door2.delete(superA);
            door3.delete(superA);
            door4.delete(superA);
            door5.delete(superA);
            door6.delete(superA);
            door7.delete(superA);
            
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
   /*******************************************************************************
     * It run the master script to derive the initialization script array
     * 
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
    public static void scriptAid4Doors6Sensors() throws TestException
    {
        AccessPolicy defaultPolicy;              
        String testCode = testBatch+"/"+"Test Case 01";                

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCode);

                    // launch a ping
            thisDevice.ping();

            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);	
         
//#
//# Door
//#
            Logger.detail("------------ Doors ------------");  
            
                    String defaultPolicyId = superA.getAcPolicy();
                    defaultPolicy = new AccessPolicy(superA, defaultPolicyId);
                    defaultPolicy.setAlwaysWeeklyPolicy();
                    defaultPolicy.update(superA);

                    // creates and initialises doors 
                    Door door0;
                    door0 = new Door(superA);
                    door0.setGpioId("00");
                    door0.setName("Access 0");
                    door0.setStatus(Door.SE_DOOR_ENABLED);
                    door0.setMode(Door.SE_DOOR_MODE_PULSE);
                    door0.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door0.update(superA);

                    // creates and initialises doors
                    Door door1;
                    door1 = new Door(superA);
                    door1.setGpioId("01");
                    door1.setName("Access 1");
                    door1.setStatus(Door.SE_DOOR_ENABLED);
                    door1.setMode(Door.SE_DOOR_MODE_PULSE);
                    door1.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door1.update(superA);

                    // creates and initialises doors
                    Door door2;
                    door2 = new Door(superA);
                    door2.setGpioId("02");
                    door2.setName("Access 2");
                    door2.setStatus(Door.SE_DOOR_ENABLED);
                    door2.setMode(Door.SE_DOOR_MODE_PULSE);
                    door2.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door2.update(superA);

                    // creates and initialises doors
                    Door door3;
                    door3 = new Door(superA);
                    door3.setGpioId("03");
                    door3.setName("Access 3");
                    door3.setStatus(Door.SE_DOOR_ENABLED);
                    door3.setMode(Door.SE_DOOR_MODE_PULSE);
                    door3.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
                    door3.update(superA);                    

//#
//# Sensors
//#
            Logger.detail("------------ Sensors ------------");  
            
                    // creates and initialises Sensors 
                    Sensor sensor0;
                    sensor0 = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);
                    sensor0.syncroFields(superA);                    
                    sensor0.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor0.setGpioId("00");
                    sensor0.setName("I0 Logical");
                    sensor0.setStatus(Door.SE_DOOR_ENABLED);
                    sensor0.setLowThreshold(800);
                    sensor0.setHighThreshold(3800);
                    sensor0.update(superA);

                    Sensor sensor1;
                    sensor1 = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);
                    sensor1.syncroFields(superA);                    
                    sensor1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor1.setGpioId("01");
                    sensor1.setName("I1 Logical");
                    sensor1.setStatus(Door.SE_DOOR_ENABLED);
                    sensor1.setLowThreshold(800);
                    sensor1.setHighThreshold(3800);                    
                    sensor1.update(superA);
                    
                    Sensor sensor2;
                    sensor2 = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);
                    sensor2.syncroFields(superA);                    
                    sensor2.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor2.setGpioId("02");
                    sensor2.setName("I2 Logical");
                    sensor2.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor2.setLowThreshold(800);
                    sensor2.setHighThreshold(3800);                    
                    sensor2.update(superA);

                    Sensor sensor3;
                    sensor3 = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);
                    sensor3.syncroFields(superA);                    
                    sensor3.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor3.setGpioId("03");
                    sensor3.setName("I3 Logical");
                    sensor3.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor3.setLowThreshold(800);
                    sensor3.setHighThreshold(3800);                    
                    sensor3.update(superA);
                    
                    Sensor sensor4;
                    sensor4 = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);
                    sensor4.syncroFields(superA);                    
                    sensor4.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor4.setGpioId("04");
                    sensor4.setName("I4 Logical");
                    sensor4.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor4.setLowThreshold(800);
                    sensor4.setHighThreshold(3800);                    
                    sensor4.update(superA);
                    
                    Sensor sensor5;
                    sensor5 = new Sensor(superA, Sensor.SENSOR_OHM_TYPE, true);
                    sensor5.syncroFields(superA);                    
                    sensor5.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor5.setGpioId("05");
                    sensor5.setName("I5 OHM");
                    sensor5.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                    sensor5.update(superA);
//#
//# Policy
//#
            Logger.detail("------------ Policies ------------"); 
            
                AccessPolicy alw;
                alw = new AccessPolicy(superA);
                alw.setAlwaysWeeklyPolicy();
                alw.setStartingDate("17", "03", "28");
                alw.setStartingDate("99", "03", "28");
                alw.setName("Always");
                alw.update(superA);
                
                AccessPolicy nvr;
                nvr = new AccessPolicy(superA);
                nvr.setNeverWeeklyPolicy();
                nvr.setName("Never");
                nvr.update(superA);

    //#
    //# Object deletion
    //#
          /*  sensor0.delete(superA);
            sensor1.delete(superA);
            sensor2.delete(superA);
            sensor3.delete(superA);
            sensor4.delete(superA);
            door0.delete(superA);
            door1.delete(superA);
            door2.delete(superA);
            door3.delete(superA);*/
            
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
}

