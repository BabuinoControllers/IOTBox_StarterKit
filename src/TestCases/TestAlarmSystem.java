
package TestCases;
    
import com.sdk.*;
import java.io.IOException;


public class TestAlarmSystem {
    
   /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "AllarmSystemExample";
    public static final String deviceId = MainTest.TestMain.deviceId;
    public static final String pinTest = "31323334";
    
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
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.syncroFields();
                superA.setPin(pinTest);                
                
                configure();
                j++;

  
                    
        }
        catch ( CommandErrorException |  ObjectException |IOException | DiscoveryException e){
            
             Logger.detail("TEST FAILURE ----->" + j);
            return false;
        }            
        
        return true;
    }
 
    /*----------------------------------------------------------------------------
    testCase06
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Configure the allarm system

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean configure()
    {
            boolean result;
            String pin;
            Command c = new Command();                
            String testCode = "Allarm Setting";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {

                Logger.testCase(testCode);

                pin = "31323334";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);
                
//#
//# Allarm On Off. Enabler Switch
//#   
                Door enabler = new Door(superA);                        
                enabler.setGpioId(Door.SE_VIRTUAL_GPIO_0);                      
                enabler.setStatus(Door.SE_DOOR_ENABLED);
                enabler.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
                enabler.setSecurity(Door.SE_DOOR_SECURITY_PIN);
                enabler.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
                enabler.setName("Allarm on/off");
                enabler.update(superA);
                enabler.syncroFields(superA);

//#
//# Zone1 Allarm On Off. Enabler Switch
//# 
                Door enablerZone1 = new Door(superA);                        
                enablerZone1.setGpioId(Door.SE_VIRTUAL_GPIO_1);                      
                enablerZone1.setStatus(Door.SE_DOOR_ENABLED);
                enablerZone1.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
                enablerZone1.setSecurity(Door.SE_DOOR_SECURITY_PIN);
                enablerZone1.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
                enablerZone1.setName("Zone1");
                enablerZone1.update(superA);
                enablerZone1.syncroFields(superA);                

//#
//# Zone2. Enabler Switch
//# 
                Door enablerZone2 = new Door(superA);                        
                enablerZone2.setGpioId(Door.SE_VIRTUAL_GPIO_2);                      
                enablerZone2.setStatus(Door.SE_DOOR_ENABLED);
                enablerZone2.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
                enablerZone2.setSecurity(Door.SE_DOOR_SECURITY_PIN);
                enablerZone2.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
                enablerZone2.setName("Zone2");
                enablerZone2.update(superA);
                enablerZone2.syncroFields(superA);

//#
//# Zone3. Enabler Switch
//# 
                Door enablerZone3 = new Door(superA);                        
                enablerZone3.setGpioId(Door.SE_VIRTUAL_GPIO_3);                      
                enablerZone3.setStatus(Door.SE_DOOR_ENABLED);
                enablerZone3.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
                enablerZone3.setSecurity(Door.SE_DOOR_SECURITY_PIN);
                enablerZone3.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
                enablerZone3.setName("Zone3");
                enablerZone3.update(superA);
                enablerZone3.syncroFields(superA);

//#
//# Zone4. Enabler Switch
//# 
                Door enablerZone4 = new Door(superA);                        
                enablerZone4.setGpioId(Door.SE_VIRTUAL_GPIO_4);                      
                enablerZone4.setStatus(Door.SE_DOOR_ENABLED);
                enablerZone4.setInitialOutputValue(Door.SE_DOOR_STATUS_HIGH);
                enablerZone4.setSecurity(Door.SE_DOOR_SECURITY_PIN);
                enablerZone4.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
                enablerZone4.setName("Zone4");
                enablerZone4.update(superA);
                enablerZone4.syncroFields(superA);
                
//#
//# Silent Alarm Swirch
//# 
                Door silentSwitch = new Door(superA);                        
                silentSwitch.setGpioId(Door.SE_VIRTUAL_GPIO_5);                      
                silentSwitch.setStatus(Door.SE_DOOR_ENABLED);
                silentSwitch.setMode(Door.DOOR_COMMAND_PULSE);
                silentSwitch.setPulseDuration(500);
                silentSwitch.setSecurity(Door.SE_DOOR_SECURITY_PIN);
                silentSwitch.setName("Silent");
                silentSwitch.update(superA);
                silentSwitch.syncroFields(superA);
 
//#
//# Sensor Zone 1
//# 
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("09");
                s1.setName("Zone 1");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);

//#
//# Sensor Zone 2
//# 
                Sensor s2 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s2.setGpioId("0A");
                s2.setName("Zone 2");
                s2.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s2.update(superA);

//#
//# Sensor Zone 3
//#                 
                Sensor s3 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s3.setGpioId("0B");
                s3.setName("Zone 3");
                s3.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s3.update(superA);

//#
//# Sensor Zone 4
//# 
                Sensor s4 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s4.setGpioId("0C");
                s4.setName("Zone 4");
                s4.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s4.update(superA);

//#
//# Sensor Anti Tearing
//#                
                Sensor antiTearingSensor = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, true);
                antiTearingSensor.setLowThreshold(1000);
                antiTearingSensor.setHighThreshold(2700);
                antiTearingSensor.setName("Anti Tearing");
                antiTearingSensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                antiTearingSensor.update(superA);

//#
//# Alarm Calendar
//#                
                AccessPolicy allarmCalendar = new AccessPolicy(superA);
                allarmCalendar.setAlwaysWeeklyPolicy();
                allarmCalendar.setName("Alarm Calendar");
                allarmCalendar.update(superA);
                
//#
//# Logic Function Zone1
//#                                               
                DigitalFunctionBlock LogicZone1 = new DigitalFunctionBlock(superA);
                LogicZone1.setInputId(enabler.getObjectId(), 2);
                LogicZone1.setInputId(enablerZone1.getObjectId(), 1);
                LogicZone1.setInputId(s1.getObjectId(), 0); 
                LogicZone1.setFunction("FDFD");
                LogicZone1.update(superA);

//#
//# Logic Function Zone2
//#                                               
                DigitalFunctionBlock LogicZone2 = new DigitalFunctionBlock(superA);
                LogicZone2.setInputId(enabler.getObjectId(), 2);
                LogicZone2.setInputId(enablerZone2.getObjectId(), 1);
                LogicZone2.setInputId(s2.getObjectId(), 0);                
                LogicZone2.setFunction("FDFD");
                LogicZone2.update(superA);

//#
//# Logic Function Zone3
//#                                               
                DigitalFunctionBlock LogicZone3 = new DigitalFunctionBlock(superA);
                LogicZone3.setInputId(enabler.getObjectId(), 2);
                LogicZone3.setInputId(enablerZone3.getObjectId(), 1);
                LogicZone3.setInputId(s3.getObjectId(), 0);                
                LogicZone3.setFunction("FDFD");
                LogicZone3.update(superA);

//#
//# Logic Function Zone4
//#                                               
                DigitalFunctionBlock LogicZone4 = new DigitalFunctionBlock(superA);
                LogicZone4.setInputId(enabler.getObjectId(), 2);
                LogicZone4.setInputId(enablerZone4.getObjectId(), 1);
                LogicZone4.setInputId(s4.getObjectId(), 0);                
                LogicZone4.setFunction("FDFD");
                LogicZone4.update(superA);
                
//#
//# LogicFunction Zone Allarm
//#                                               
                DigitalFunctionBlock zoneAllarm = new DigitalFunctionBlock(superA);
                zoneAllarm.setInputId(LogicZone1.getObjectId(), 0);
                zoneAllarm.setInputId(LogicZone2.getObjectId(), 1);
                zoneAllarm.setInputId(LogicZone3.getObjectId(), 2);
                zoneAllarm.setInputId(LogicZone4.getObjectId(), 3);
                zoneAllarm.setLogEvent(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_LOG_WHEN_OUTPUT_SET);
                zoneAllarm.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_NAND_4_INPUT);
                zoneAllarm.setWeeklyPolicy(allarmCalendar.getObjectId());
                zoneAllarm.update(superA);

//#
//# LogicFunction Allarm
//#                                               
                DigitalFunctionBlock allarm = new DigitalFunctionBlock(superA);
                allarm.setInputId(zoneAllarm.getObjectId(), 0);
                allarm.setInputId(antiTearingSensor.getObjectId(), 1);
                allarm.setResetObjectId(silentSwitch.getObjectId());
                allarm.setGpio("00");
                allarm.setOffDelay(5000);
                allarm.setLogEvent(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_LOG_WHEN_OUTPUT_SET);
                allarm.setFunction("DDDD");
                allarm.update(superA);                
                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                }                           
                
//#
//# Object deletion
//#                       

        }
        catch (CommandErrorException | ObjectException | IOException e)
        {
                result = false;
        }

        Logger.testCase(testCode);
        Logger.testResult(result);
        return result;
    }

}
