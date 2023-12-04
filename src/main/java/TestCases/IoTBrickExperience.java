package TestCases;

import com.sdk.*;
import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IoTBrickExperience {

    public static final String testBatch = "TestUserMQTT001";
    public static final String deviceId = MainTest.TestMain.deviceId;
    public static final String clientId = "3333333333";

    //public static final String hostName =  "54.76.137.235"; //mqtt cloud
    // public static final int port =  18223; // mqtt cloud

    public static final String HOSTNAME_TESTING = "80.211.32.249"; //mqtt cloud
    public static final String HOSTNAME_STAGING = "80.211.35.177"; //mqtt cloud
    public static final String hostName = HOSTNAME_STAGING; //mqtt cloud
    public static final int port = 8883;

    public static final int RESET_DELAY = 40000;

    //    /********************************
//     PUBLIC Methods
//     ********************************/
    public static User superA;
    public static Device thisDevice;

    private static final TestUnit thisUnit = new TestUnit();

    /*----------------------------------------------------------------------------
    run
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test run method

    Security Level: None

    ------------------------------------------------------------------------------*/
    @Test
    public void run() {
        thisUnit.setTestTitle(testBatch);

        int j;
        // ---------------------- Code -------------------------------

        Command.onError = Command.ALT_ON_ERROR;

        j = 1;
        try {

            ConnectionDetailsMqtt connectionDetails = new ConnectionDetailsMqtt();

            connectionDetails.setBearer(ConnectionDetailsMqtt.BEARER_MQTT_OVER_ETHERNET);

            connectionDetails.setClientId(clientId);

            connectionDetails.setHostName(hostName);
            connectionDetails.setPort(port);

            connectionDetails.setKeepAliveTime(60000);

            connectionDetails.setPassword("111");
            connectionDetails.setUserName(clientId);

            connectionDetails.setDeviceId(deviceId);

            connectionDetails.setStreamSecurity(true);

            thisDevice = new Device();
            thisDevice.setConnectionDetails(connectionDetails);
            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);

            thisDevice.startCommandSession();

            thisUnit.setDevice(thisDevice);

            for (int u = 0; u < 1; u++) {

           //     ledExperience();
                j++;

            //   telemetryExperience();
                j++;

          //      vitalExperience();
                j++;

                resetToFactorySetting();
            }


        } catch (TestException | IOException e) {
            Logger.detail("TEST FAILURE ----->" + j);
            thisUnit.testCompleted(false, "failure at test case " + j);

            Assertions.fail("TEST FAILURE ----->" + j);
            //return false;
        } finally {
            try {
                thisDevice.completeCommandSession();
            } catch (IOException ignored) {

            }
        }
        thisUnit.testCompleted(true, "success!");

        Logger.detail("OK");
    }

/*----------------------------------------------------------------------------
ledExperience
--------------------------------------------------------------------------
DESCRIPTION: Tutorial on led

------------------------------------------------------------------------------*/
    public static void ledExperience() throws TestException {
        String a, name;
        String testCase = testBatch + "/" + "Led Experience";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

                // Synchronize Super A Fields
            superA.syncroFields(superA);

                // creates a door object
            Door door = new Door(superA);
            door.setGpioId(1);
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setMode(Door.SE_DOOR_MODE_ON_OFF);
            door.update(superA);

            for (int i = 0; i<10; i++) {

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                String doorOut = door.getOutput(superA);
                Logger.detail("doorOut: " + doorOut);

                // two seconds of delay
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                doorOut = door.getOutput(superA);
                Logger.detail("doorOut: " + doorOut);

                // two seconds of delay
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            door.delete(superA);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }

        Logger.testCase(testCase);
        tc.testCompleted(true, "success");
        Logger.testResult(true);
    }
   /*----------------------------------------------------------------------------
   telemetryExperience
   --------------------------------------------------------------------------
   AUTHOR:	PDI

   DESCRIPTION: Tutorial on telemetry

   ------------------------------------------------------------------------------*/
    public static void telemetryExperience() throws TestException {

        String testCase = testBatch + "/" + "Telemetry Experience";

        // ---------------------- Code -------------------------------
        try
        {

            Logger.testCase(testCase);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // Synchronize Super A Fields
            superA.syncroFields(superA);
//#
//# Creates local object and synchronize with the device
//#

            Sensor sensorLdrOhm = new Sensor(superA, Sensor.SENSOR_OHM_TYPE, false);
            Sensor sensorLdrVoltage = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, false);
            Sensor sensorSwitch = new Sensor(superA, Sensor.SENSOR_ANALOG_TYPE, false);

            sensorLdrOhm.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensorLdrOhm.setGpioId(4);
            sensorLdrOhm.update(superA);

            sensorLdrVoltage.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensorLdrVoltage.setGpioId(4);
            sensorLdrVoltage.update(superA);

            sensorSwitch.setStatus(Sensor.SENSOR_STATUS_ENABLED);
            sensorSwitch.setGpioId(5);
            sensorSwitch.setLowThreshold(500);
            sensorSwitch.setHighThreshold(900);
            sensorSwitch.update(superA);

            while(true) {

                String sensorLdrOHMOut = sensorLdrOhm.getMeasure(superA);
                String sensorLdrVoltageOut = sensorLdrVoltage.getMeasure(superA);
                String sensorSwitchOut = sensorSwitch.getMeasure(superA);

                Logger.detail("---------------------------------------------------");

                Logger.detail("sensor Ldr Voltage Out [mV]: " + Integer.parseInt(sensorLdrVoltageOut, 16) );
                Logger.detail("sensor Ldr OHM Out [OHM]: " + Integer.parseInt(sensorLdrOHMOut, 16));
                Logger.detail("sensor Switch Out: " + sensorSwitchOut);

                Logger.detail("---------------------------------------------------");

                // two seconds of delay
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        catch (CommandErrorException | ObjectException | IOException e)
        {
            Logger.testResult(false);
            throw new TestException();
        }
    }

    /*----------------------------------------------------------------------------
  vital Experience
  --------------------------------------------------------------------------
  AUTHOR:	PDI

  DESCRIPTION: Tutorial on Bios Vitals
  ------------------------------------------------------------------------------*/
    public static void vitalExperience() throws TestException {

        String testCode = testBatch + "/" + "Vital Experience";

        // ---------------------- Code -------------------------------
        try
        {
            Logger.testCase(testCode);

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // Synchronize Super A Fields
            superA.syncroFields(superA);
//#
//# Creates local object and synchronize with the device
//#

            for(int i=0; i<5; i++){


                // get System measures
                double vcc = thisDevice.getVcc(superA);
                double temperature = thisDevice.getMicroTemperature(superA);
                int batteryVoltage = thisDevice.getBatteryVoltage(superA);

                Logger.detail("---------------------------------------------------");

                // device parameters
                Logger.detail(" vcc: " + vcc + "V");
                Logger.detail(" DeviceTemperature Celsius: " + temperature);
                Logger.detail(" Battery Voltage: " + batteryVoltage + "mV");

                Logger.detail("---------------------------------------------------");

                // sleep
                try {

                    Thread.sleep(4000);

                } catch( InterruptedException e){

                }
            }
        }
        catch (CommandErrorException | ObjectException | IOException e)
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


            String pin = "31313131";

            // launch a ping
            thisDevice.ping();

            // instantiate a local User object for the SUPER-A
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(superA, pin);

            // Synchronize Super A Fields
            superA.syncroFields(superA);
//#
//# System Reset to factory setting
//#
            Logger.detail("*****************************************************************");
            Logger.detail("*****************************************************************");
            Logger.detail("Reset to Factory settings ongoing: PLEASE WAIT Until test is over");
            Logger.detail("*****************************************************************");
            Logger.detail("*****************************************************************");

            thisDevice.systemFactoryReset(pin, superA, true);

        } catch (CommandErrorException | IOException | ObjectException e) {
            Logger.testResult(false);
            throw new TestException();
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
}
