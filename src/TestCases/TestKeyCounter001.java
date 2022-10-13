package TestCases;

import com.sdk.*;
import java.io.IOException;

public class TestKeyCounter001 {
    
    /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestKeyCounter001";
    public static final String deviceId = MainTest.TestMain.deviceId;   

    /********************************
    Public Methods
    ********************************/
    public static User superA; 
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
        superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
    
        try {
                thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET,3,2000);     
                
                superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);               
                
                for (int u = 0; u<1; u++)
                {
                    testCase01();
                    j++;
                    
                    testCase02();
                    j++;
                                        
                    //thisDevice = Device.discover(deviceId, Device.WIFI);
                    //IoStream.setActiveDevice(thisDevice);                     
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

	DESCRIPTION: Check the transaction sequence. Onlty one User
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase01() throws TestException
	{
            RemoteAuthenticator a;
            Command command = new Command();
            int x, y;
            byte[]z;
                
            String testCase = testBatch+"/"+"Test Case 01";            		
	// ---------------------- Code -------------------------------
            try
            {
                Logger.testCase(testCase);
			
                    // ping
                    thisDevice.ping();
			
                    // instantiate a local User object for the SUPER-A
                    //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    // object personalized
		superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                a = superA.getRemoteAuthenticatorObject();
                
	//#
	//# Check Counter Sequence
	//# One User
                
                Command.isStopOnSyncroError = true;
                
                z = a.getKeyCounter();
                y =  ((z[5]&0xFF)<<16) + ((z[6]&0xFF)<<8) + (z[7] & 0xFF);
                    // object created
                for(int i=0; i<300; i++)
                { 
                    Logger.testCase("Attempt #" +i);
                    superA.syncroFields(superA);
                    
                    z = a.getKeyCounter();
                    x =  ((z[5]&0xFF)<<16) + ((z[6]&0xFF)<<8) +(z[7]&0xFF);
                    y++;
                    
                    if (x != y)
                        throw new TestException();

                }
                Command.isStopOnSyncroError = false;
        //#
	//# Object deletion
	//#                       
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
	testCase03
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Check the transaction sequence. Two Users
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase02() throws TestException
	{
            RemoteAuthenticator raoSuperA, raoAdmin;
            Command command = new Command();
            int sx, ax, sy, ay;
            byte[] superATransactionCounter, adminTransactionCounter;
                
            String testCase = testBatch+"/"+"Test Case 02";
            
		
	// ---------------------- Code -------------------------------
            try
            {
                Logger.testCase(testCase);
			
                    // ping
                    thisDevice.ping();
			
                    // instantiate a local User object for the SUPER-A
                    //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    // object personalized
		superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                raoSuperA = superA.getRemoteAuthenticatorObject();
                
                User admin = new User(superA, User.USER_ROLE_ADMIN,"initialAdmin");	
                        // object personalized
                //for(int i=0; i<1000; i++)                
                admin.updateKey("admin");
                
                raoAdmin = admin.getRemoteAuthenticatorObject();
                
	//#
	//# Check Counter Sequence
	//# One User
                
                Command.isStopOnSyncroError = true;
                
                superATransactionCounter = raoSuperA.getKeyCounter();
                adminTransactionCounter = raoSuperA.getKeyCounter();
                        
                sy =  ((superATransactionCounter[5]&0xFF)<<16) + ((superATransactionCounter[6]&0xFF)<<8) + (superATransactionCounter[7]&0xFF);
                ay =  ((adminTransactionCounter[5]&0xFF)<<16) + ((adminTransactionCounter[6]&0xFF)<<8) + (adminTransactionCounter[7]&0xFF);
                    // object created
                for(int i=0; i<300; i++)
                { 
                    Logger.testCase("Attempt #" +i);
                    
                    superA.syncroFields(superA);
                    
                    superATransactionCounter = raoSuperA.getKeyCounter();
                    adminTransactionCounter = raoSuperA.getKeyCounter();
                        
                    sx =  ((superATransactionCounter[5]&0xFF)<<16) + ((superATransactionCounter[6]&0xFF)<<8) + (superATransactionCounter[7]&0xFF);
                    
                    sy++;
                    if (sx != sy)
                        throw new TestException();
                    
                    admin.syncroFields(admin);
                    ax =  ((adminTransactionCounter[5]&0xFF)<<16) + ((adminTransactionCounter[6]&0xFF)<<8) + (adminTransactionCounter[7]&0xFF);

                    ay++;
                    
                    if (ax != ay)
                        throw new TestException();
                }
                Command.isStopOnSyncroError = false;
        //#
	//# Object deletion
	//#                       
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
}

