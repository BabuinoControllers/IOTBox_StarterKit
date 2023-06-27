
package TestCases;

import com.sdk.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestDemoNightLight001 {
    
 /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestDemoNightLight001";    
    public static final String deviceId = MainTest.TestMain.deviceId;

        // Mqtt parameters
    public static final String clientId = "3333333333";    
    public static final String hostName =  "80.211.35.177"; //mqtt cloud
    public static final int port =  1883;    

        // OBJECT Identifiers
    public static final String SENSOR_LDR_OBJECT_ID = "00000008";
    public static final String SENSOR_SWITCH_OBJECT_ID = "00000009";
    public static final String DOOR_OBJECT_ID = "00000007";
    public static final String SENSOR_GPIO_OBJECT_ID = "0000000D";
    public static final String SENSOR_LDR_VOLTAGE_OBJECT_ID = "0000000A";
    public static final String SENSOR_LDR_OHM_OBJECT_ID = "0000000B";
               
    //********************************
    // USE CASE SELECTOR
    public static final int RESET_TO_FACTORY_SETTING = 1;
    public static final int CONFIGURATION_UC = 2;
    public static final int TELEMETRY_UC = 3;
    public static final int EXERCISE_UC = 4;
    
        // Use Case selector
    public static int useCase = RESET_TO_FACTORY_SETTING;
    
    //********************************
    // CONNECTIVITY SELECTOR
    public static final int CONNECTIVITY_LAN = 1;
    public static final int CONNECTIVITY_MQTT = 2;
    
        // Connectivity selector
    public static int connectivitySelector = CONNECTIVITY_MQTT;

    /********************************
     Private Methods
     ********************************/
    private static Device connect() throws DiscoveryException, IOException{

        Device  device;

        switch (connectivitySelector) {

            case CONNECTIVITY_LAN:

                device = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

                break;

            case CONNECTIVITY_MQTT:

                ConnectionDetailsMqtt connectionDetails = new ConnectionDetailsMqtt();

                connectionDetails.setBearer(ConnectionDetailsMqtt.BEARER_MQTT_OVER_ETHERNET);
                connectionDetails.setClientId(clientId);
                connectionDetails.setHostName(hostName);
                connectionDetails.setPort(port);
                connectionDetails.setKeepAliveTime(60000);
                connectionDetails.setPassword("Ma6#fht<!!");
                connectionDetails.setUserName(clientId);
                connectionDetails.setDeviceId(deviceId);

                device = new Device();
                device.setConnectionDetails(connectionDetails);
                device.startCommandSession();

                break;

            default:

                device = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

        }

        return device;
    }
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
    @Test
    public void run()
    {
        int j;
       // ---------------------- Code -------------------------------        
       Command.onError = Command.ALT_ON_ERROR;
       
       j = 6;
        try {

            switch(useCase){

                case CONFIGURATION_UC:

                    configurationAndTelemetry();
                    break;

                case TELEMETRY_UC:

                    telemetry();
                    break;                        

                case RESET_TO_FACTORY_SETTING:

                    resetToFactorySetting();
                    break;

                case EXERCISE_UC:

                    exercise();
                    break;

            }
        }
        catch (TestException e){
                         Logger.detail("TEST FAILURE ----->" + j);
                         Assertions.fail();
            //return false;
        }            
        
        //return true;
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

            String testCode = testBatch + "/" + "Configuration and Telemetry";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                Device device = connect();
                SuperA superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, device);
                    // launch a ping
                device.ping();

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

                device.systemReset(pin, superA, true);

        }
        catch (CommandErrorException | ObjectException | IOException | DiscoveryException e)
        {
            Logger.testResult(false);
            throw new TestException();
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

                Device device = connect();

                    // creates super A instance
                SuperA superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, device);

                    // launch a ping
                device.ping();
//#
//# Creates local object and synchronize with the device
//#
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
                    double vcc = device.getVcc(superA);
                    double temperature = device.getMicroTemperature(superA);
                    int batteryVoltage = device.getBatteryVoltage(superA);
                    
                    Logger.detail("---------------------------------------------------");

                    Logger.detail("sensor Ldr Out: " + sensorLdrOut );
                    Logger.detail("sensor Ldr Voltage Out [mV]: " + Integer.parseInt(sensorLdrVoltageOut, 16) );
                    Logger.detail("sensor Ldr OHM Out [OHM]: " + Integer.parseInt(sensorLdrOHMOut, 16));
                    Logger.detail("sensor Switch Out: " + sensorSwitchOut);
                    Logger.detail("sensor Gpio Out: " + sensorGpioOut);
                    Logger.detail("doorOut: " + doorOut);
                    
                        // device parameters
                    Logger.detail(" vcc: " + vcc * 1000);
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
        catch (CommandErrorException | ObjectException | IOException | DiscoveryException e)
        {
            Logger.testResult(false);
            throw new TestException();
        }       
   }
   /*----------------------------------------------------------------------------
    reset the device to factory setting
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperAdministrator reset the device to factory reset.

    Security Level: None

    ------------------------------------------------------------------------------*/
   public static void resetToFactorySetting() throws TestException {

       String testCode = testBatch + "/" + "reset to factory setting";

       // ---------------------- Code -------------------------------
       try {

           Logger.testCase(testCode);

           Device device = connect();
           SuperA superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, device);

           // launch a ping
           device.ping();

//#
//# System Reset to factory setting
//#
           Logger.detail("*****************************************************************");
           Logger.detail("*****************************************************************");
           Logger.detail("Reset to Factory settings ongoing: PLEASE WAIT Until test is over");
           Logger.detail("*****************************************************************");
           Logger.detail("*****************************************************************");

           String pin = "31313131";
           device.systemFactoryReset(pin, superA, true);

       } catch (CommandErrorException | IOException | DiscoveryException e) {
           Logger.testResult(false);
           throw new TestException();
       }

       Logger.testCase(testCode);
       Logger.testResult(true);
   }
    /*----------------------------------------------------------------------------
    exercise
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperAdministrator reset the device to factory reset.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void exercise() throws TestException {

        String testCode = testBatch + "/" + "Exercise";

        // ---------------------- Code -------------------------------
        try {

            Logger.testCase(testCode);

            Device device = connect();
            SuperA superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, device);

            // launch a ping
            device.ping();

            while(true){

                // sleep
                try {

                    Thread.sleep(2000);

                } catch( InterruptedException e){

                }
            }

        } catch (IOException | DiscoveryException e) {
            Logger.testResult(false);
            throw new TestException();
        }
    }
}
