package TestCases;
import com.sdk.*;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Calendar;
import com.testlog.*;

@SuppressWarnings("unused")
public class TestTime001 {
    
    /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestTime001";
    public static final String deviceId =  MainTest.TestMain.deviceId;

    /********************************
    Public Methods
    ********************************/
    public static SuperA superA;  
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
	public void run()
    {
        thisUnit.setTestTitle(testBatch);

        int j;
       // ---------------------- Code -------------------------------        
        
       Command.onError = Command.ALT_ON_ERROR;    
       
       j = 1;
        try {
                thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET,3,2000);

            thisUnit.setDevice(thisDevice);

                superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);                
                
                for (int u = 0; u<1; u++)
                {
                    testCase01();
                    j++;
                    
                    testCase02();
                    j++;


                }
            superA.resetPin(superA);
        } catch (TestException | DiscoveryException | CommandErrorException | IOException | ObjectException e){
            thisUnit.testCompleted(false, "failure at test case " + j);

            Logger.detail("TEST FAILURE ----->" + j);
             Assertions.fail("TEST FAILURE ----->" + j);
	//return false;
        }
        thisUnit.testCompleted(true, "success!");
        Logger.detail("OK");
    }
    /*******************************************************************************
     * testCase01: It checks that get time and get date commands return correct values
     *	 *
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
    public static void testCase01() throws TestException
    {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        String testCase = testBatch +" /" + "Test Case 01";
        tc.setCaseTitle(testCase);

        Calendar c, c1;
        // ---------------------- Code -------------------------------
        try
        {		
            String pin  = "31323334";
            Logger.testCase(testCase);

                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(pin);

    //#
    //# Check that it is possible to set the time and that the time returned is correct
    //#                     
            Logger.detail("------------ Check that get time and date return current time and date;  ------------");

                // get current local time
            c1 = Calendar.getInstance();
                // set time and date
            thisDevice.setTimeAndDate(c1, superA, pin);
                // get time and date
            c = thisDevice.getTimeAndDate();
            
            if ((c1.get(Calendar.MINUTE) != c.get(Calendar.MINUTE)) ||
                (c1.get(Calendar.HOUR_OF_DAY) != c.get(Calendar.HOUR_OF_DAY)) ||
                (c1.get(Calendar.DAY_OF_WEEK) != c.get(Calendar.DAY_OF_WEEK)) ||
                (c1.get(Calendar.DAY_OF_MONTH) != c.get(Calendar.DAY_OF_MONTH)) ||
                (c1.get(Calendar.MONTH) != c.get(Calendar.MONTH)) ||
                (c1.get(Calendar.YEAR) != c.get(Calendar.YEAR)))
            {		
                throw new TestException();
            }

    //#
    //# Check that RTC is counting the time. Time check after 10 seconds.
    //#                     
            Logger.detail("------------ Check that RTC is counting the time. Time check after 10 seconds.  ------------");
            Logger.detail("------------ Plese Wait 10 s.  ------------");
                // wait 10s;
            try{
                Thread.sleep(10000);
            } catch (InterruptedException ignored)
            {
            }
                // get time and date
            c = thisDevice.getTimeAndDate();

            //long a = c.getTimeInMillis();
            //long b = c1.getTimeInMillis();

            long deltaT = c.getTimeInMillis() - c1.getTimeInMillis();

            if (!((9000 <= deltaT) &&
                (deltaT <=11000)))
            {	
                Logger.detail("Delta Time :" + deltaT);
                throw new TestException();
            }

    //#
    //# Object deletion
    //#                       
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            }
            catch (CommandErrorException | ObjectException | IOException | TestException e)
            {
                Logger.testResult(false);
                tc.testCompleted(false, "fail");

                throw new TestException();
            }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
            Logger.testResult(true);
    }
/*******************************************************************************
     * testCase02: Admin and users can not set the time.
     *	 *
     * @throws TestException       	 In case of errors
     ********************************************************************************/    
    public static void testCase02() throws TestException
    {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command command = new Command();
        String testCase = testBatch +" /" + "Test Case 02";
        tc.setCaseTitle(testCase);

        String expectedRes;
        // ---------------------- Code -------------------------------
        try
        {		
            String pin  = "31313131";
            Logger.testCase(testCase);

                // launch a ping
            thisDevice.ping();

                // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.setPin(pin);
  
            User admin = new User(superA, User.USER_ROLE_ADMIN, "newAdministrator");
            admin.updateKey("impossible");
            
            User user = new User(superA, User.USER_ROLE_USER, "xxxxxxxxxx");
            user.updateKey("impossible");
            
    //#
    //# Admn can not set time and date
    //#          
            Logger.detail("------------ Admn can not set time and date  ------------");

                // create the apdu object
            Apdu apdu = new Apdu(Apdu.SET_TIME_APDU);
            apdu.addTlv(Atlv.DATA_TAG_SYSTEM_TIME, "0000000001010101");
            apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin);      

            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                            String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                            String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                            String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

            String time = "0000000001010101";
            // send command
            command.description = "Set time";
            command.requester = admin;
            String a = apdu.toString();
            command.execute(a, expectedRes);            

    //#
    //# User can not set time and date
    //#                     
            Logger.detail("------------ User can not set time and date  ------------");

                // create the apdu object
            apdu = new Apdu(Apdu.SET_TIME_APDU);
            apdu.addTlv(Atlv.DATA_TAG_SYSTEM_TIME, time);
            apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin);      

            expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                            String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                            String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                            String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";  

            // send command
            command.description = "Set time";
            command.requester = user;
            a = apdu.toString();
            command.execute(a, expectedRes);             

    //#
    //# Object deletion
    //#                       
                    user.delete(admin);
                    admin.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            }
            catch (CommandErrorException | ObjectException | IOException e)
            {
                Logger.testResult(false);
                tc.testCompleted(false, "fail");
                throw new TestException();
            }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
            Logger.testResult(true);
    }    
        
}

