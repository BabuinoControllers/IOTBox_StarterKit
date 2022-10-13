package TestCases;

import com.sdk.*;
import java.io.IOException;

public class TestExtension001 {
    
    /********************************
    Public Methods
    ********************************/
    public static User superA;
    public static User superAExtension;
    public static Device thisDevice;
    public static Device extensionDevice;

      /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestExtension001";
    public static final String deviceId = MainTest.TestMain.deviceId;
    public static final String deviceIdExtension = "0000000000000B01";

    

    /*----------------------------------------------------------------------------
    run
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test run method

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean run()
    {	
        Device d;
        int j;
       // ---------------------- Code -------------------------------        

        Command.onError = Command.ALT_ON_ERROR;
        
        superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
    
        j = 1;
        try {
           // Device.discover(deviceIdExtension, Device.ETHERNET,3,2000);
                thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET,3,2000);     
                
                superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);                 
                              
                thisDevice.ping();
                extensionDevice = thisDevice.getExtensionDevice(deviceIdExtension);
      
                superAExtension = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, extensionDevice);                
                
                for (int u = 0; u<1; u++)
                {

                    testCase01();
                    j++;

                    testCase02();
                    j++;
                     
                    testCase03();
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
        }
        catch ( TestException | DiscoveryException | IOException e){
            Logger.detail("TEST FAILURE ----->" + j);            
            return false;
        }            
        
        return true;
    }
     	/*----------------------------------------------------------------------------
	testCase01
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Super-A creates an administrator object in the extension. 
        The initial fields are set as expected;
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase01() throws TestException
	{
            String a, name;
                
            String testCase = testBatch+"/"+"Test Case 01";
		
	// ---------------------- Code -------------------------------
            try
            {
                Logger.testCase(testCase);
			
                    // launch a ping
                 thisDevice.ping();
                 extensionDevice.ping();
                 extensionDevice.ping();
                
                    // instantiate a local User object for the SUPER-A
                    // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    // object personalized
		superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superAExtension.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                
	//#
	//# Creation of an Administrator by the Super A in the Extension
	//# Result: OK
                    // object created
                User admin = new User(superAExtension, User.USER_ROLE_ADMIN,"initialAdmin");	
                        // object personalized
                //for(int i=0; i<1000; i++)                
                admin.updateKey("admin");

                admin.syncroFields(admin);
                        			
                    // Role is administrator
                Logger.detail("------------ Check Role ------------");
                a = admin.getRole();
                if (0 != a.compareTo(User.USER_ROLE_ADMIN))
                        throw new ObjectException();	
				
                            // Status is active
                    Logger.detail("------------ Check Status ------------");
                    a = admin.getStatus();
                    if (0 != a.compareTo(User.STATUS_ACTIVE))
                            throw new ObjectException();

                            // Security Domain is the initial security domain
                    Logger.detail("------------ Check Security Domain ------------");
                    a = admin.getSecurityDomain();
                    if (0 != a.compareTo(SecurityDomain.INITIAL_SD_ID))
                            throw new ObjectException();				

                            // Remote authentication object has been created
                    Logger.detail("------------ Check that remote authenticator is created ------------");
                    RemoteAuthenticator rAuth = admin.getRemoteAuthenticatorObject();
                    rAuth.syncroFields(admin);
                    a = rAuth.getOwnerID();
                    if (0 != a.compareTo(admin.getObjectId()))
                            throw new ObjectException();

                            // Local Authentication object has been created
                    Logger.detail("------------ Check that local authenticator is created ------------");
                    LocalAuthenticator lAuth = admin.getLocalAuthenticatorObject();
                    lAuth.syncroFields(admin);
                    a = lAuth.getOwnerID();
                    if (0 != a.compareTo(admin.getObjectId()))
                            throw new ObjectException();

                            // AC Policy is the default policy
                    Logger.detail("------------ Check that access policy is the default one ------------");
                    a = admin.getAcPolicy();
                    if (0 != a.compareTo(AccessPolicy.DEFAULT_ACCCESS_POLICY_ID))
                            throw new ObjectException();	

                            // Notification Policy is the default policy
                    Logger.detail("------------ Check that notification policy is the default one ------------");
                    a = admin.getNotificationPolicy();
                    if (0 != a.compareTo(NotificationPolicy.INITIAL_NOTIFICATION_OBJECT_ID))
                            throw new CommandErrorException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = admin.getName();			
                    name = admin.getObjectId();                    
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();
        //#
	//# Object deletion
	//#                       
                        admin.delete(superAExtension);
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        superAExtension.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
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
	testCase02
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Super-A creates an administrator object in the extension. 
        The Device ID is sent. The initial fields are set as expected;
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase02() throws TestException
	{
            String a, name;
                            
            String testCase = testBatch+"/"+"Test Case 02";
		
	// ---------------------- Code -------------------------------
            try
            {
                Logger.testCase(testCase);
			
                    // launch a ping
                thisDevice.ping();
                extensionDevice.ping();
                extensionDevice.ping();
			
                    // instantiate a local User object for the SUPER-A
                    // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    // object personalized
		superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superAExtension.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                
	//#
	//# Creation of an Administrator by the Super A in the Extension
	//# Result: OK
                    // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN,"initialAdmin");	
                        // object personalized
                //for(int i=0; i<1000; i++)                
                admin.updateKey("admin");

                admin.syncroFields(admin);
                        			
                    // Role is administrator
                Logger.detail("------------ Check Role ------------");
                a = admin.getRole();
                if (0 != a.compareTo(User.USER_ROLE_ADMIN))
                        throw new ObjectException();	
				
                            // Status is active
                    Logger.detail("------------ Check Status ------------");
                    a = admin.getStatus();
                    if (0 != a.compareTo(User.STATUS_ACTIVE))
                            throw new ObjectException();

                            // Security Domain is the initial security domain
                    Logger.detail("------------ Check Security Domain ------------");
                    a = admin.getSecurityDomain();
                    if (0 != a.compareTo(SecurityDomain.INITIAL_SD_ID))
                            throw new ObjectException();				

                            // Remote authentication object has been created
                    Logger.detail("------------ Check that remote authenticator is created ------------");
                    RemoteAuthenticator rAuth = admin.getRemoteAuthenticatorObject();
                    rAuth.syncroFields(admin);
                    a = rAuth.getOwnerID();
                    if (0 != a.compareTo(admin.getObjectId()))
                            throw new ObjectException();

                            // Local Authentication object has been created
                    Logger.detail("------------ Check that local authenticator is created ------------");
                    LocalAuthenticator lAuth = admin.getLocalAuthenticatorObject();
                    lAuth.syncroFields(admin);
                    a = lAuth.getOwnerID();
                    if (0 != a.compareTo(admin.getObjectId()))
                            throw new ObjectException();

                            // AC Policy is the default policy
                    Logger.detail("------------ Check that access policy is the default one ------------");
                    a = admin.getAcPolicy();
                    if (0 != a.compareTo(AccessPolicy.DEFAULT_ACCCESS_POLICY_ID))
                            throw new ObjectException();	

                            // Notification Policy is the default policy
                    Logger.detail("------------ Check that notification policy is the default one ------------");
                    a = admin.getNotificationPolicy();
                    if (0 != a.compareTo(NotificationPolicy.INITIAL_NOTIFICATION_OBJECT_ID))
                            throw new CommandErrorException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = admin.getName();			
                    name = admin.getObjectId();                    
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();
        //#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        superAExtension.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
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

	DESCRIPTION: If a wrong device Id is used then the forwarder (gateway) timeout
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase03() throws TestException
	{
            String x;
            int len;
            byte[] rx, pingBuffer;
            BerTlv pingCommand, pingResponse;
            TlvBuff c;
            SimpleTlv response;

            String testCode = testBatch+"/"+"Test Case 03";                

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                    // launch a ping
                thisDevice.ping();
                extensionDevice.ping();
                extensionDevice.ping();              

	//#
	//# If device is not connected there is a timeout response
	//# 
        
                String deviceIdExtension1 = "0000000000000BBB";                                
                        
                    // create ping command and send
                rx = new byte[256];              
                pingBuffer = new byte[256];
        
                pingCommand = new BerTlv(Atlv.DATA_TAG_SYSTEM_PING);                     
                pingCommand.addTlv(Atlv.DATA_TAG_APDU_COMMAND, Apdu.PING_APDU);
                        
                c = new TlvBuff();            
                c.addTlv(pingCommand);
                c.addTlv(Atlv.DATA_TAG_DEVICE_ID, deviceIdExtension1);
                        
                c.toArray(pingBuffer, 0);
            
                    // log command Ping
                len = TLV.getTLVFUllSize(pingBuffer, 0);
                x = (Util.bytesToHexString(pingBuffer)).substring(0, len<<1);
                Logger.ping(x);
            
                    //send the command
                thisDevice.sendAndReceive(pingBuffer, 0, 256, rx,  0);

                    // Log Ping Response
                len = TLV.getTLVFUllSize(rx, 0);
                x = (Util.bytesToHexString(rx)).substring(0, len<<1);
                Logger.pingResponse(x);

                response = new SimpleTlv(rx[0], rx, 2, rx[1]);                                                
                
                if (null != response)
                {
                    if (0 != Apdu.SW_6A00_TIMEOUT_ERROR.compareTo(response.toString()))
                    {
                        Logger.testResult(false);		
                        TestException t = new TestException();
                        throw t;
                    }                                             
                } else {
                    Logger.testResult(false);		
                    TestException t = new TestException();
                    throw t;                    
                }                

//#
//# Object deletion
//#
            }
            catch ( IOException | TestException e)
            {
                Logger.testResult(false);		
                TestException t = new TestException();
                throw t;
            }	

            Logger.testCase(testCode);
            Logger.testResult(true);

	}
}

