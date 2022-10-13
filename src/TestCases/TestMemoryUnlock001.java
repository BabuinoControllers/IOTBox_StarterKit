package TestCases;

import com.sdk.*;
import java.io.IOException;

public class TestMemoryUnlock001 {
    
    /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestMemoryUnlock001";
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
                
                for (int u = 0; u<1; u++)
                {
                    testCase01();
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

	DESCRIPTION: Memory Unlock
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase01() throws TestException
	{
            String pin;
            String testCase = testBatch+"/"+"Test Case 01";
            Apdu apdu;
            
           
            
            Command c = new Command();
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
                ;
	//#
	//# Memory Unlock Success
	//#             
                apdu = new Apdu("ACB50300" );
                apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin);
                apdu.addTlv((byte)0xEB, "3132333435363738");                                						

                        // send command
                c.description = "Memory Unlock";
                c.requester = superA;
                c.execute(apdu.toString(), Command.COMMAND_EXPECTED_SUCCESFUL_RESPONSE);
        //#
	//# Object deletion
	//#                
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
	testCase01
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Memory Unlock
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase02() throws TestException
	{
            String pin;
            String testCase = testBatch+"/"+"Test Case 02";
            Apdu apdu;
            
           
            
            Command c = new Command();
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
                ;
	//#
	//# Memory Unlock Success
	//#             
                apdu = new Apdu("ACB50300" );
                apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin);
                apdu.addTlv((byte)0xEB, "3232333435363738");                                						

                String expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                     String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                     String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                           Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                     String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                        // send command
                c.description = "Memory Unlock Wrong UKey";
                c.requester = superA;
                c.execute(apdu.toString(), expectedRes);
        //#
	//# Object deletion
	//#                
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

