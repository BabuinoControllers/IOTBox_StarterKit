package TestCases;

import com.sdk.*;
import java.io.IOException;

public class TestGetDataOutput001 {
    
    /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestGetDataOutput001";
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
                    
                    if (u == 0)
                    {
                            //    Logger.detail("<-------------------------------->"); 
                            //    Logger.detail(" WIFI "); 
                            //    Logger.detail("<-------------------------------->"); 
                            //   d = Device.discover(deviceId, Device.WIFI, 2 , 1000);
                            //    IoStream.setActiveDevice(d);
                            //    j=1;
                    }
                }

            } catch ( TestException | DiscoveryException e){
                Logger.detail("TEST FAILURE ----->" + j);            
                return false;
            }

            return true;
    }
    /*----------------------------------------------------------------------------
    testCase01
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Super Administrator can recover output GOIO value by means of get data output

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException
    {
        String pin;
        String testCase = testBatch+"/"+"Test Case 01";
        String a[];

    // ---------------------- Code -------------------------------
        try
        {
            Logger.testCase(testCase);

                // ping
            thisDevice.ping();
            pin = "01020304";

                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);                

    //#
    //# SuperA turns output on; get data output returns the output value;
    //#             

            for (int i = 0; i<Device.GPIO_NUMBER; i++)
            {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);

                Logger.detail(" ON Output " + i);
                superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_ON, 0);

                a = superA.getDataGpio();

                for (int j=0; j<Device.GPIO_NUMBER; j++)
                {
                    if (j<=i)
                    {
                        if (0 != a[j].compareTo( Sensor.SENSOR_VALUE_ON))
                            throw new TestException();                  
                    }
                    else
                    {
                        if (0 != a[j].compareTo( Sensor.SENSOR_VALUE_OFF))
                            throw new TestException();                                              
                    }
                }

                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) 
                {
                }                    
            }
    //#
    //# SuperA turns output off
    //#                                            
            for (int i =0; i<Device.GPIO_NUMBER; i++)
            {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);

                Logger.detail(" OFF Output " + i);
                superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_OFF, 0);

                a = superA.getDataGpio();

                for (int j=0; j<Device.GPIO_NUMBER; j++)
                {
                    if (j<=i)
                    {
                        if (0 != a[j].compareTo( Sensor.SENSOR_VALUE_OFF))
                            throw new TestException();                  
                    }
                    else
                    {
                        if (0 != a[j].compareTo( Sensor.SENSOR_VALUE_ON))
                            throw new TestException();                                              
                    }
                }

                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) 
                {
                }                    
            }

    //#
    //# Object deletion
    //#          
            s1.delete(superA);
            }
            catch (CommandErrorException | ObjectException | IOException | TestException e)
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

    DESCRIPTION: Administrators and users can not get gpio via getDataGPIO

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException
    {
        Command command = new Command();
        String pin;
        String testCase = testBatch+"/"+"Test Case 02";         

    // ---------------------- Code -------------------------------
        try
        {
            Logger.testCase(testCase);

                // ping
            thisDevice.ping();
            pin = "01020304";

                // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin); 

                // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");                         
            admin.updateKey("adminx");
            admin.syncroFields(admin);
            admin.setPin(superA, pin); 

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");						
            user.updateKey( "newAdministratorKeysSutta");
            user.syncroFields(user);
            user.setPin(superA, pin); 

    //#
    //# Admin can not get GPIO output via get data GPIO
    //#             

            // create the apdu object
        Apdu apdu = new Apdu(Apdu.GET_DATA_SENSOR_APDU);                                

            // set the expected result and send the command;              
        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                            String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                            String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                            String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
        command.description = "Get Data GPIO status";
        command.requester = admin;                        
        command.execute(apdu.toString(), expectedRes);
    //#
    //# User can not get GPIO output via get data GPIO
    //#             

            // create the apdu object
        apdu = new Apdu(Apdu.GET_DATA_SENSOR_APDU);                       

            // set the expected result and send the command;              
        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                            String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                            String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                            String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
        command.description = "Get Data GPIO status";
        command.requester = admin;                        
        command.execute(apdu.toString(), expectedRes);            

    //#
    //# Object deletion
    //#            
            user.delete(superA);
            admin.delete(superA);
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

    DESCRIPTION: Super Administrator can recover output GOIO value by means of get data sensor adding
    GPIO as sensor type.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException
    {
        String pin;
        String testCase = testBatch+"/"+"Test Case 03";
        String a[];

    // ---------------------- Code -------------------------------
        try
        {
            Logger.testCase(testCase);

                // ping
            thisDevice.ping();
            pin = "01020304";

                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);                

    //#
    //# SuperA turns output on; get data output returns the output value;
    //#             

            for (int i = 0; i<Device.GPIO_NUMBER; i++)
            {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);

                Logger.detail(" ON Output " + i);
                superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_ON, 0);

                a = superA.getDataSensors(Sensor.SENSOR_TYPE_GPIO);

                for (int j=0; j<Device.GPIO_NUMBER; j++)
                {
                    if (j<=i)
                    {
                        if (0 != a[j].compareTo( Sensor.SENSOR_VALUE_ON))
                            throw new TestException();                  
                    }
                    else
                    {
                        if (0 != a[j].compareTo( Sensor.SENSOR_VALUE_OFF))
                            throw new TestException();                                              
                    }
                }

                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) 
                {
                }                    
            }
    //#
    //# SuperA turns output off
    //#                                            
            for (int i =0; i<Device.GPIO_NUMBER; i++)
            {
                s1.setGpioId(String.format("%02X", i));
                s1.update(superA);

                Logger.detail(" OFF Output " + i);
                superA.putDataOutput(pin, String.format("%02X", i), SuperA.SE_OUTPUT_OFF, 0);

                a = superA.getDataSensors(Sensor.SENSOR_TYPE_GPIO);

                for (int j=0; j<Device.GPIO_NUMBER; j++)
                {
                    if (j<=i)
                    {
                        if (0 != a[j].compareTo( Sensor.SENSOR_VALUE_OFF))
                            throw new TestException();                  
                    }
                    else
                    {
                        if (0 != a[j].compareTo( Sensor.SENSOR_VALUE_ON))
                            throw new TestException();                                              
                    }
                }

                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) 
                {
                }                    
            }

    //#
    //# Object deletion
    //#          
            s1.delete(superA);
            }
            catch (CommandErrorException | ObjectException | IOException | TestException e)
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

    DESCRIPTION: Super Administrator can recover data from temperature and humidity sensor.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException
    {
        String pin;
        String testCase = testBatch+"/"+"Test Case 04";
        String a[];

    // ---------------------- Code -------------------------------
        try
        {
            Logger.testCase(testCase);

                // ping
            thisDevice.ping();
            pin = "01020304";

                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
            s1.setGpioId("00");
            s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            s1.update(superA);                

    //#
    //# SuperA get temperature from the sensor via get data sensor;
    //#                         
                a = superA.getDataSensors(Sensor.SENSOR_TEMPERATURE_HTS221_TYPE);
                Logger.detail(" >>>>>>>>>>>>>>>>>>>>>>> Rough HTS221 Temperature " + a[0]);
                
    //#
    //# SuperA get temperature from the sensor via get data sensor;
    //#                                            
                a = superA.getDataSensors(Sensor.SENSOR_HUMIDITY_HTS221_TYPE);
                Logger.detail(" >>>>>>>>>>>>>>>>>>>>>>> Rough HTS221 Humidity " + a[0]);                

    //#
    //# Object deletion
    //#          
            s1.delete(superA);
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

    DESCRIPTION: Administrators and users can not get sensor data with get data sensor command

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException
    {
        Command command = new Command();
        String pin;
        String testCase = testBatch+"/"+"Test Case 05";         

    // ---------------------- Code -------------------------------
        try
        {
            Logger.testCase(testCase);

                // ping
            thisDevice.ping();
            pin = "01020304";

                // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin); 

                // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");                         
            admin.updateKey("adminx");
            admin.syncroFields(admin);
            admin.setPin(superA, pin); 

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");						
            user.updateKey( "newAdministratorKeysSutta");
            user.syncroFields(user);
            user.setPin(superA, pin); 

    //#
    //# Admin can not get sensor data with the get sensor command
    //#             

            // create the apdu object
        Apdu apdu = new Apdu(Apdu.GET_DATA_SENSOR_APDU);
        apdu.addTlv(Apdu.DATA_TAG_SENSOR_TYPE, Sensor.SENSOR_HUMIDITY_HTS221_TYPE);

            // set the expected result and send the command;              
        String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                            String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                            String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                            String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
        command.description = "Get Data GPIO status";
        command.requester = admin;                        
        command.execute(apdu.toString(), expectedRes);
    //#
    //# User can not get sensor data with the get sensor command
    //#             

            // create the apdu object
        apdu = new Apdu(Apdu.GET_DATA_SENSOR_APDU);
        apdu.addTlv(Apdu.DATA_TAG_SENSOR_TYPE, Sensor.SENSOR_TEMPERATURE_HTS221_TYPE);        

            // set the expected result and send the command;              
        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                            String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                            String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                            String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
        command.description = "Get Data GPIO status";
        command.requester = admin;                        
        command.execute(apdu.toString(), expectedRes);            

    //#
    //# Object deletion
    //#            
            user.delete(superA);
            admin.delete(superA);
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
}

