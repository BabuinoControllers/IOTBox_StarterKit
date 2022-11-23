
package TestCases;
    
import com.sdk.*;
import java.io.IOException;

public class TestDemoNightLight001 {
    
 /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestDemoNightLight001";    
    public static final String deviceId = MainTest.TestMain.deviceId;

    public static SuperA superA;  
    public static Device thisDevice;
    
    public static final int RESET_TO_FACTORY_SETTING = 1;
    public static final int CONFIGURATION_AND_TELEMETRY_UC = 2;
    public static final int TELEMETRY_UC = 3;
    
    public static int useCase = CONFIGURATION_AND_TELEMETRY_UC;
    
    public static final String SENSOR_LDR_OBJECT_ID = "00000008";
    public static final String SENSOR_SWITCH_OBJECT_ID = "00000009";
    public static final String DOOR_OBJECT_ID = "00000007";
    public static final String SENSOR_GPIO_OBJECT_ID = "0000000D";
    public static final String SENSOR_LDR_VOLTAGE_OBJECT_ID = "0000000A";
    public static final String SENSOR_LDR_OHM_OBJECT_ID = "0000000B";
       
    
    /********************************
    Public Methods
    ********************************/
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
       
       j = 6;
        try {
                thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

                superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice); 
                
                
                switch(useCase){
                    
                    case CONFIGURATION_AND_TELEMETRY_UC:
                        
                        configurationAndTelemetry();
                        break;
                        
                    case TELEMETRY_UC:
                        
                        telemetry();
                        break;                        
                    
                    case RESET_TO_FACTORY_SETTING:
                        
                        resetTofactorySetting();
                        break;
                }
        }
        catch (TestException | DiscoveryException e){
                         Logger.detail("TEST FAILURE ----->" + j);
            return false;
        }            
        
        return true;
    }
    /*----------------------------------------------------------------------------
    configurationAndTelemetry
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Configure the application and start telemetry. 

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void configurationAndTelemetry() throws TestException
    {
            Door door;
            String policyId;
            AccessPolicy defaultPolicy;               
            String testCode = testBatch + "/" + "Configuration and Telemetry";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                    // launch a ping
                thisDevice.ping();

//#
//# Configure Users
//#
                String pin = "31313131";
                
                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

//#
//# Create Doors for Remote interaction
//#                
                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("02");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
               
//#
//# Create Sensor for LDR. It is on I3.
//#                                
                Logger.detail("------------ Create LDR SENSOR Instance------------");                        
                Sensor sensorLdr = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);

                sensorLdr.setGpioId(3);
                sensorLdr.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                sensorLdr.setLowThreshold(500);
                sensorLdr.setHighThreshold(900);
                
                sensorLdr.update(superA);                       

//#
//# Create Sensor for Switch
//#                                
                Logger.detail("------------ Create Switch SENSOR Instance------------");                        
                Sensor sensorSwitch = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);

                sensorSwitch.setGpioId(4);
                sensorSwitch.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                sensorSwitch.setLowThreshold(500);
                sensorSwitch.setHighThreshold(3000);
                
                sensorSwitch.update(superA);
                
//#
//# Create Sensor for device parameters
//#                
                Logger.detail("------------ Create LDR SENSOR to measure Voltage ------------");                        
                Sensor sensorLdrVoltage = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);
                sensorLdrVoltage.setGpioId(3);
                sensorLdrVoltage.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                
                sensorLdrVoltage.update(superA);

                Logger.detail("------------ Create LDR SENSOR to measure OHM ------------");                        
                Sensor sensorLdrOHM = new Sensor(superA, Sensor.SENSOR_OHM_TYPE, true);
                sensorLdrOHM.setGpioId(3);
                sensorLdrOHM.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                
                sensorLdrOHM.update(superA);
                
//#
//# Create digital Function Block
//#                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(sensorLdr.getObjectId(), 0);
                dfb.setInputId(sensorSwitch.getObjectId(), 1);
               // dfb.setInputId(door.getObjectId(), 2);
                
                dfb.setFunction("DDDD");
                
                    // 3 seconds
                // dfb.setOffDelay("00007530");
                // dfb.setOnDelay("00007530");
                
                dfb.setGpio("01");
                dfb.update(superA);
//#
//# Create Sensor to monitor GPIO 
//#
                Sensor sensorGpio = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                sensorGpio.setGpioId("01");
                sensorGpio.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                sensorGpio.update(superA);                

//#
//# System Reset to restart
//#   
    
                Logger.detail("LDR Sensor ObjectId: " + sensorLdr.getObjectId());
                Logger.detail("Switch Sensor ObjectId: " + sensorSwitch.getObjectId());
                Logger.detail("Door ObjectId: " + door.getObjectId()); 
                Logger.detail("sensorGpio ObjectId: " + sensorGpio.getObjectId());
                Logger.detail("sensorLdrOHM ObjectId: " + sensorLdrOHM.getObjectId());
                Logger.detail("sensorLdrVoltage ObjectId: " + sensorLdrVoltage.getObjectId());

//#
//# System Reset to restart
//#                                

                thisDevice.systemReset(pin, superA, true);
//#
//# Telemetry. Usually done by itself;
//#                                
                
                telemetry();

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
    telemetry
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Implements the telemetry use case. 
                It measures input and parameters and print on console.

    Security Level: None

    ------------------------------------------------------------------------------*/
   public static void telemetry() throws TestException {
             
        String testCode = testBatch + "/" + "Telemetry";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                    // launch a ping
                thisDevice.ping();

//#
//# Configure Super Administrator Users
//#
                String pin = "31313131";
                
                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

//#
//# Creates local object and syncronize with the device
//#                
                    // create the door. Configure it.
                Door door = new Door(superA, DOOR_OBJECT_ID);
                Sensor sensorLdr = new Sensor(superA, SENSOR_LDR_OBJECT_ID);
                Sensor sensorSwitch = new Sensor(superA, SENSOR_SWITCH_OBJECT_ID);
                Sensor sensorGpio = new Sensor(superA, SENSOR_GPIO_OBJECT_ID);
                Sensor sensorLdrOHM = new Sensor(superA, SENSOR_LDR_OHM_OBJECT_ID);
                Sensor sensorLdrVoltage = new Sensor(superA, SENSOR_LDR_VOLTAGE_OBJECT_ID);
                
                                    
                
                while(true){
                
                    
                    String sensorLdrOut = sensorLdr.getMeasure(superA);
                    String sensorLdrOHMOut = sensorLdrOHM.getMeasure(superA);
                    String sensorLdrVoltageOut = sensorLdrVoltage.getMeasure(superA);
                    String sensorSwitchOut = sensorSwitch.getMeasure(superA);
                    String sensorGpioOut = sensorGpio.getMeasure(superA);
                    String doorOut = door.getOutput(superA);
                    
                        // get System measures
                    double vcc = thisDevice.getVcc(superA);
                    double temperature = thisDevice.getMicroTemperature(superA);
                    int batteryVoltage = thisDevice.getBatteryVoltage(superA);
                    
                    Logger.detail("---------------------------------------------------");

                    Logger.detail("sensor Ldr Out: " + sensorLdrOut );
                    Logger.detail("sensor Ldr Voltage Out [mV]: " + Integer.parseInt(sensorLdrVoltageOut, 16) );
                    Logger.detail("sensor Ldr OHM Out [OHM]: " + Integer.parseInt(sensorLdrOHMOut, 16));
                    Logger.detail("sensor Switch Out: " + sensorSwitchOut);
                    Logger.detail("sensor Gpio Out: " + sensorGpioOut);
                    Logger.detail("doorOut: " + doorOut);
                    
                        // device parameters
                    Logger.detail(" vcc: " + vcc);                    
                    Logger.detail(" DeviceTemperature Celsius: " + temperature);
                    Logger.detail(" Battery Voltage: " + batteryVoltage);
                    
                    Logger.detail("---------------------------------------------------");
                    
                    door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                        // sleep
                    try {
                        
                        Thread.sleep(2000);
                        
                    } catch( InterruptedException e){
                        
                    }
                    door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                }

        }
        catch (CommandErrorException | ObjectException | IOException e)
        {
            Logger.testResult(false);		
            TestException t = new TestException();
            throw t;
        }       
   }
   /*----------------------------------------------------------------------------
    reset the device to factory setting
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperAdministrator reset the device to factory reset.

    Security Level: None

    ------------------------------------------------------------------------------*/
   public static void resetTofactorySetting() throws TestException {
                   
        String testCode = testBatch + "/" + "reset to factory setting";               

        // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                    // launch a ping
                thisDevice.ping();

//#
//# Configure Users
//#
                String pin = "31313131";
                
                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

//#
//# System Reset to factory setting
//#                                

                thisDevice.systemFactoryReset(pin, superA, true);

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
