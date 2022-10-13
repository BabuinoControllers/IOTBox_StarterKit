
package TestCases;
    
import com.sdk.*;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

public class TestExtenderWithCentralizedControlEthernet001 {
    
 /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestDoorExtender001";
    public static final String deviceId = MainTest.TestMain.deviceId;
    public static final String deviceIdExtension = "0000000000000B01";
    
    public static final String pin = "31323334";

    /********************************
    Public Methods
    ********************************/
    public static User superA;
    public static User superAExtension;
    public static Device thisDevice;
    public static Device extensionDevice;

    /*----------------------------------------------------------------------------
    run
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test run method

    Security Level: None

    ------------------------------------------------------------------------------*/
   public static boolean run()
    {	boolean result;
        int j;
       // ---------------------- Code -------------------------------        
        Command.onError = Command.ALT_ON_ERROR;
    
       j = 1;
       try {
            thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET,3,2000);
            extensionDevice = Device.discover(deviceIdExtension, ConnectionDetails.BEARER_ETHERNET, 3,2000);
           
            thisDevice.ping();                        

            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);
                
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                            
            superA.syncroFields(superA);                        
            superA.setPin(superA, pin);
            
     
            extensionDevice.ping();
            superAExtension = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, extensionDevice);                
            superAExtension.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY); 
            superAExtension.syncroFields(superA);
            superAExtension.setPin(superAExtension, pin);
            
            
            result = testCase01();
            j++;
            
            result = testCase02();
            j++;
            
            result = testCase03();
            j++;
            
            result = testCase04();
            j++;
            
            result = testCase05();
            j++;           
        }
        catch (DiscoveryException | IOException | CommandErrorException | ObjectException e){
            Logger.detail("TEST FAILURE ----->" + j);            
            return false;
        }         

        return result;
    }
   
    /*----------------------------------------------------------------------------
    testCase01
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Super A can open a door in an extension board; Ethernet

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase01()
    {
        boolean result;
        Door door;
        DeviceExtender deviceExtender;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;
        Command c = new Command();              
        String testCode = testBatch+"/"+"Test Case 01";                

        // ---------------------- Code -------------------------------
        result = true;
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

                // set the device extender
            String deviceId = "0000000000000B01";                        
            deviceExtender = new DeviceExtender(superA, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            deviceExtender.setDeviceId(deviceId);
            deviceExtender.setBearer(DeviceExtender.SE_DEVICE_EXTENDER_BEARER_ETHERNET);
            deviceExtender.update(superA);            
            deviceExtender.setPin(superA, "31323334");
            
                // set the door
            door = new Door(superA);                        
            door.setGpioId(1);                      
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setDeviceExtensionId(deviceExtender.getObjectId());
            door.update(superA);
            door.syncroFields(superA);
            
       // Super A on the extension
       //     Device = new device();
       //     SuperA remoteSuperA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, device);
       //     remoteSuperA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
       //     remoteSuperA.setPin(remoteSuperA, "31323334");
            
//#
//# Super A can trigger a door on an extension
//#
            Logger.detail("------------ Super A can trigger a door on an extension------------");                        

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
            deviceExtender.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
            result = false;
        }

        Logger.testCase(testCode);
        Logger.testResult(result);
        return result;
    }
    /*----------------------------------------------------------------------------
    testCase02
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Super A can open a door in an extension board by a pulse;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase02()
    {
        boolean result;
        Door door;
        DeviceExtender deviceExtender;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;
        Command c = new Command();              
        String testCode = testBatch+"/"+"Test Case 02";                

        // ---------------------- Code -------------------------------
        result = true;
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

                // set the device extender
            String deviceId = "0000000000000B01";                        
            deviceExtender = new DeviceExtender(superA, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            deviceExtender.setDeviceId(deviceId);
            deviceExtender.update(superA);            
            deviceExtender.setPin(superA, "31323334");
            
                // set the door
            door = new Door(superA);                        
            door.setGpioId(1);                      
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setMode(Door.SE_DOOR_MODE_PULSE);
            door.setPulseDuration("07D0");            
            door.setDeviceExtensionId(deviceExtender.getObjectId());
            door.update(superA);
            door.syncroFields(superA);
            
            
//#
//# Super A can trigger a door on an extension
//#
            Logger.detail("------------ Super A can trigger a door on an extension------------");                        

            for (int i = 0; i<30; i++)
            {
                door.setOutput(superA, Door.DOOR_COMMAND_PULSE);
                gpioValue = door.getOutput(superA);
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
                {
                    throw new TestException();
                } 

                try{
                Thread.sleep(10000);
                }
                catch (InterruptedException e){                        
                }

                gpioValue = door.getOutput(superA);
                if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
                {
                    throw new TestException();
                }
                                              
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            deviceExtender.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
            result = false;
        }

        Logger.testCase(testCode);
        Logger.testResult(result);
        return result;
    }
    /*----------------------------------------------------------------------------
    testCase03
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Super A can get the value of a GPIO on an extension board;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase03()
    {
        boolean result;
        Door door;
        Sensor sensor;
        DeviceExtender deviceExtender;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;             
        String testCode = testBatch+"/"+"Test Case 03";                

        // ---------------------- Code -------------------------------
        result = true;
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

                // set the device extender
            String deviceId = "0000000000000B01";                        
            deviceExtender = new DeviceExtender(superA, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            deviceExtender.setDeviceId(deviceId);
            deviceExtender.update(superA);            
            deviceExtender.setPin(superA, "31323334");
            
                // set the door
            door = new Door(superA);                        
            door.setGpioId(0);                      
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setDeviceExtensionId(deviceExtender.getObjectId());
            door.update(superA);
            door.syncroFields(superA);
            
                            // Create a sensor and assign it to GPIO 0
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.setGpioId(0);
            sensor.setDeviceExtensionObjectId(deviceExtender.getObjectId());
            sensor.update(superA);
            
            
//#
//# Super A can get the value of a gpio sensor
//#
            Logger.detail("------------ Super A can get the value of a gpio sensor------------");                        

            for (int i = 0; i<4; i++)
            {
                door.setGpioId(i);
                door.update(superA);
                
                sensor.setGpioId(i);
                sensor.update(superA);
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                gpioValue = door.getOutput(superA);                
                gpioValue = sensor.getMeasure(superA);
                if(!(gpioValue.equals(Sensor.SENSOR_VALUE_ON)))
                {
                    throw new TestException();
                } 

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                gpioValue = sensor.getMeasure(superA);
                if(!(gpioValue.equals(Sensor.SENSOR_VALUE_OFF)))
                {
                    throw new TestException();
                }                            
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            deviceExtender.delete(superA);
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
            result = false;
        }

        Logger.testCase(testCode);
        Logger.testResult(result);
        return result;
    }
    /*----------------------------------------------------------------------------
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: User can trigger a door and get the value of a GPIO on an extension board;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase04()
    {
        boolean result;
        Door door;
        Sensor sensor;
        DeviceExtender deviceExtender;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;             
        String testCode = testBatch+"/"+"Test Case 04";                

        // ---------------------- Code -------------------------------
        result = true;
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

                // set the device extender
            String deviceId = "0000000000000B01";                        
            deviceExtender = new DeviceExtender(superA, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            deviceExtender.setDeviceId(deviceId);
            deviceExtender.update(superA);            
            deviceExtender.setPin(superA, "31323334");
            
                // set the door
            door = new Door(superA);                        
            door.setGpioId(0);                      
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setDeviceExtensionId(deviceExtender.getObjectId());
            door.update(superA);
            door.syncroFields(superA);
            
                            // Create a sensor and assign it to GPIO 0
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.setGpioId(0);
            sensor.setDeviceExtensionObjectId(deviceExtender.getObjectId());
            sensor.update(superA);
            
            
//#
//# Super A can get the value of a gpio sensor
//#
            Logger.detail("------------ Super A can get the value of a gpio sensor------------");                        

            for (int i = 0; i<4; i++)
            {
                door.setGpioId(i);
                door.update(superA);
                
                sensor.setGpioId(i);
                sensor.update(superA);
                
                door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);                
                gpioValue = sensor.getMeasure(user);
                if(!(gpioValue.equals(Sensor.SENSOR_VALUE_ON)))
                {
                    throw new TestException();
                } 

                door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);
                gpioValue = sensor.getMeasure(user);
                if(!(gpioValue.equals(Sensor.SENSOR_VALUE_OFF)))
                {
                    throw new TestException();
                }                            
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            deviceExtender.delete(superA);
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
            result = false;
        }

        Logger.testCase(testCode);
        Logger.testResult(result);
        return result;
    }
    /*----------------------------------------------------------------------------
    testCase05
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: User can trigger a door and get the value of a GPIO on an extension board;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase05()
    {
        boolean result;
        Door door;
        Sensor sensor;
        DeviceExtender deviceExtender;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;             
        String testCode = testBatch+"/"+"Test Case 05";                

        // ---------------------- Code -------------------------------
        result = true;
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

                // set the device extender
            String deviceId = "0000000000000B01";                        
            deviceExtender = new DeviceExtender(superA, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            deviceExtender.setDeviceId(deviceId);
            deviceExtender.update(superA);            
            deviceExtender.setPin(superA, "31323334");
            
                // set the door
            door = new Door(superA);                        
            door.setGpioId(0);                      
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setDeviceExtensionId(deviceExtender.getObjectId());
            door.update(superA);
            door.syncroFields(superA);
            
                            // Create a sensor and assign it to GPIO 0
            sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensor.setGpioId(0);
            sensor.setDeviceExtensionObjectId(deviceExtender.getObjectId());
            sensor.update(superA);
                        
//#
//# Super A can get the value of a gpio sensor
//#
            Logger.detail("------------ Super A can get the value of a gpio sensor------------");                        

            for (int i = 0; i<4; i++)
            {
                door.setGpioId(i);
                door.update(superA);
                
                sensor.setGpioId(i);
                sensor.update(superA);
                
                door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);                
                gpioValue = sensor.getMeasure(admin);
                if(!(gpioValue.equals(Sensor.SENSOR_VALUE_ON)))
                {
                    throw new TestException();
                } 

                door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);
                gpioValue = sensor.getMeasure(admin);
                if(!(gpioValue.equals(Sensor.SENSOR_VALUE_OFF)))
                {
                    throw new TestException();
                }                            
            }

            admin.delete(superA);
            user.delete(superA);
            door.delete(superA);
            deviceExtender.delete(superA);
            sensor.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
            result = false;
        }

        Logger.testCase(testCode);
        Logger.testResult(result);
        return result;
    }
/*----------------------------------------------------------------------------
    testCase06
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Super A can open a door in an extension board; Ethernet

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean testCase06()
    {
        boolean result;
        Door door;
        DeviceExtender deviceExtender;
        String gpioValue, policyId;
        AccessPolicy defaultPolicy;
        Command c = new Command();              
        String testCode = testBatch+"/"+"Test Case 06";                

        // ---------------------- Code -------------------------------
        result = true;
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

                // set the device extender
            String deviceId = "0000000000000B01";                        
            deviceExtender = new DeviceExtender(superA, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            deviceExtender.setDeviceId(deviceId);
            deviceExtender.setBearer(DeviceExtender.SE_DEVICE_EXTENDER_BEARER_ETHERNET);
            deviceExtender.update(superA);            
            deviceExtender.setPin(superA, "31323334");
            
                // set the door
            door = new Door(superA);                        
            door.setGpioId(1);                      
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setDeviceExtensionId(deviceExtender.getObjectId());
            door.update(superA);
            door.syncroFields(superA);
            
       // Super A on the extension
       //     Device = new device();
       //     SuperA remoteSuperA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, device);
       //     remoteSuperA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
       //     remoteSuperA.setPin(remoteSuperA, "31323334");
            
//#
//# Super A can trigger a door on an extension
//#
            Logger.detail("------------ Super A can trigger a door on an extension------------");                        

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
            deviceExtender.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        }
        catch (CommandErrorException | ObjectException | TestException | IOException e)
        {
            result = false;
        }

        Logger.testCase(testCode);
        Logger.testResult(result);
        return result;
    }    
}
