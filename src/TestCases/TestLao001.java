/********************************************************************************
 *
 *							PDI COPYRIGHT 2015
 *
 * HEADER
 *
 * $Workfile: Test0003.java $
 * $Date: 11/09/2015 13:21 $
 * $Author:  $
 * $Revision:  $
 *
 * DESCRIPTION:	Test of Local Authentication object
 *
 *
 * Project: ACCESS CONTROL
 ********************************************************************************/

package TestCases;

import com.sdk.*;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;


public class TestLao001
{
   /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestLao001";
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
        Device d;
        int j;
       // ---------------------- Code -------------------------------        
        
        Command.onError = Command.ALT_ON_ERROR;
        
        superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
    
        j = 1;
        try {
                thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET,3,2000);     
                
                superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);               
                
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

                    testCase06();
                    j++;
                    
                    testCase07();
                    j++;

                    testCase08();
                    j++;
                    
                    testCase09();
                    j++;

                    testCase10();
                    j++;
                    
                    testCase11();
                    j++;

                    testCase12();
                    j++;

                    testCase01();
                    j++;                    

                    testCase13();
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
        catch ( TestException |DiscoveryException e){
            Logger.detail("TEST FAILURE ----->" + j);            
            return false;
        }            
        
        return true;
    }
	/*----------------------------------------------------------------------------
	testCase01
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Create a Local Authentication Object(Super A) object and 
                        check the default value of the fields.
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase01() throws TestException
	{
                String a ;
                LocalAuthenticator lao;
                Command c = new Command();
                String testCase = testBatch +" /" + "Test Case 01";               
		
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
                
	//#
	//#  Recover of LAO ID; Read LAO default values;
	//# 

                        lao = superA.getLocalAuthenticatorObject();
                        lao.syncroFields(superA);
                        
                            // Check TAG
			Logger.detail("------------ Check Tag ------------");
			a = lao.getTag();
			if (0 != a.compareTo(LocalAuthenticator.TAG))
			{
				ObjectException e = new ObjectException();
				e.reason = ObjectException.TAG_MISMATCH_ERROR;
				throw e;
			}
                        
                            // Check Owner ID
			Logger.detail("------------ Check Owner ID ------------");
			a = lao.getOwnerID();
			if (0 != a.compareTo(superA.getObjectId()))
				throw new ObjectException();

                            // Check SD ID
			Logger.detail("------------ Check Security Domain ID ------------");
			a = lao.getSecurityDomain();
			if (0 != a.compareTo(superA.getSecurityDomain()))
				throw new ObjectException();
                                
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();
                        
                            // Check initial Retry Counter Value 
			Logger.detail("------------ Check Initial Retry Counter Value ------------");
			a = lao.getRetryCounter();
			if (0 != a.compareTo(String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();
                        
                            // Check initial Retry Counter Value 
			Logger.detail("------------ Check Initial Retry Counter Value ------------");
			a = lao.getRetryCounter();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();

                            // Max Retries 
			Logger.detail("------------ Check Initial Max Retries Value ------------");
			a = lao.getMaxRetries();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();


                            // MinPINSize
			Logger.detail("------------ Check Initial MinPinSize------------");
			a = lao.getMinPINSize();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE)))
				throw new ObjectException();                         
                                                             
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

	DESCRIPTION: Create a Local Authnetication(Admin) object and check the default value of the fields.
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase02() throws TestException
	{
                String a ;
                LocalAuthenticator lao;
                Command c = new Command();                
                String testCase = testBatch +" /" + "Test Case 02";               
		
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
                        
                            // instantiate a local User object for the SUPER-A
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			
                            // object personalized
			admin.updateKey("panda");                        
	//#
	//# Creation of the Super A; Recover of Rao ID; Read Rao default values;
	//# 

                        lao = admin.getLocalAuthenticatorObject();
                        lao.syncroFields(admin);
                        
                            // Check TAG
			Logger.detail("------------ Check Tag ------------");
			a = lao.getTag();
			if (0 != a.compareTo(LocalAuthenticator.TAG))
			{
				ObjectException e = new ObjectException();
				e.reason = ObjectException.TAG_MISMATCH_ERROR;
				throw e;
			}
                        
                            // Check Owner ID
			Logger.detail("------------ Check Owner ID ------------");
			a = lao.getOwnerID();
			if (0 != a.compareTo(admin.getObjectId()))
				throw new ObjectException();

                            // Check SD ID
			Logger.detail("------------ Check Security Domain ID ------------");
			a = lao.getSecurityDomain();
			if (0 != a.compareTo(admin.getSecurityDomain()))
				throw new ObjectException();
                                
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo(LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();
                        
                            // Check initial Retry Counter Value 
			Logger.detail("------------ Check Initial Retry Counter Value ------------");
			a = lao.getRetryCounter();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();

                            // Max Retries 
			Logger.detail("------------ Check Initial Max Retries Value ------------");
			a = lao.getMaxRetries();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();

                            // Max Retries 
			Logger.detail("------------ Check Initial Max Retries Value ------------");
			a = lao.getMaxRetries();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();

                            // MinPINSize
			Logger.detail("------------ Check Initial MinPinSize------------");
			a = lao.getMinPINSize();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE)))
				throw new ObjectException(); 

 	//#
	//# Object deletion
	//#                       
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

	DESCRIPTION: Create a Local Authnetication(User) object and check the default value of the fields.
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase03() throws TestException
	{
                String a ;
                LocalAuthenticator lao;
                Command c = new Command();                
                String testCase = testBatch +" /" + "Test Case 03";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
			
                            // object personalized
			superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        
                            // instantiate a local User object for the SUPER-A
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			
                            // object personalized
			admin.updateKey("panda");

                            // instantiate a local User object for the SUPER-A
			User user = new User(superA, User.USER_ROLE_USER, "zaffata");
			
                            // object personalized
			user.updateKey("penda");                         
	//#
	//# Recover of Lao ID; Read Lao default values;
	//# 

                        lao = user.getLocalAuthenticatorObject();
                        lao.syncroFields(user);
                             // Check TAG
			Logger.detail("------------ Check Tag ------------");
			a = lao.getTag();
			if (0 != a.compareTo(LocalAuthenticator.TAG))
			{
				ObjectException e = new ObjectException();
				e.reason = ObjectException.TAG_MISMATCH_ERROR;
				throw e;
			}
                        
                            // Check Owner ID
			Logger.detail("------------ Check Owner ID ------------");
			a = lao.getOwnerID();
			if (0 != a.compareTo(user.getObjectId()))
				throw new ObjectException();

                            // Check SD ID
			Logger.detail("------------ Check Security Domain ID ------------");
			a = lao.getSecurityDomain();
			if (0 != a.compareTo(user.getSecurityDomain()))
				throw new ObjectException();
                                
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();
                        
                            // Check initial Retry Counter Value 
			Logger.detail("------------ Check Initial Retry Counter Value ------------");
			a = lao.getRetryCounter();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();

                            // Max Retries 
			Logger.detail("------------ Check Initial Max Retries Value ------------");
			a = lao.getMaxRetries();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();

                            // Max Retries 
			Logger.detail("------------ Check Initial Max Retries Value ------------");
			a = lao.getMaxRetries();
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE)))
				throw new ObjectException();

                            // MinPINSize
			Logger.detail("------------ Check Initial MinPinSize------------");
			a = lao.getMinPINSize();
			if (0 != a.compareTo( String.format("%02X",LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE)))
				throw new ObjectException();

 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);
                         
		}
		catch (CommandErrorException | ObjectException  | IOException e)
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

	DESCRIPTION: It is not possible delelte the LAO object without removing the User
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase04() throws TestException
	{
                String expectedRes, laoId;
                Apdu apduObject;
                LocalAuthenticator lao;
                
                Command c = new Command();
                
                String testCode = testBatch+"/"+"Test Case 04";                
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCode);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                           
			superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                                              
                                               			
                            // object created
			User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");                         
			admin.updateKey("adminx");
                                               
                        User user = new User(admin, User.USER_ROLE_USER, "s[atter");						
			user.updateKey( "newAdministratorKeysSutta");
                        
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                            String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                            String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                        
                                                
        //#
	//# It is not possible to delete the Lao object of the superA
	//#
                        Logger.detail("-- It is not possible to delete the Lao object of the superA  --"); 
                       
                        lao = superA.getLocalAuthenticatorObject();
                        lao.syncroFields(superA);
                        
                        laoId = lao.getObjectId(); 
                        
                            // try to delete the RAO object of Super A
                        apduObject = new Apdu(Apdu.DELETE_OBJECT_APDU );
                        apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, laoId);

                            // send command
			c.description = "Not Possible to Delete the :AO Object of Super A";
			c.requester = superA;
			c.execute(apduObject.toString(), expectedRes);
        //#
	//# It is not possible to delete the Lao object of an Administrator
	//#
                        Logger.detail("-- It is not possible to delete the Lao object of an Administrator  --"); 
                       
                        laoId = admin.getLocalAuthenticatorId();
                        
                            // try to delete the RAO object of Super A
                        apduObject = new Apdu(Apdu.DELETE_OBJECT_APDU );
                        apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, laoId);

                            // send command
			c.description = "Not Possible to Delete the LAO Object of an Administrator";
			c.requester = superA;
			c.execute(apduObject.toString(), expectedRes);                        
        //#
	//# It is not possible to delete the Lao object of a User
	//#
                        Logger.detail("-- It is not possible to delete the Lao object of an User  --"); 
                       
                        laoId = user.getLocalAuthenticatorId();
                        
                            // try to delete the RAO object of Super A
                        apduObject = new Apdu(Apdu.DELETE_OBJECT_APDU );
                        apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, laoId);

                            // send command
			c.description = "Not Possible to Delete the LAO Object of an User";
			c.requester = superA;
			c.execute(apduObject.toString(), expectedRes); 

 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);                        
		}
		catch (CommandErrorException | ObjectException  | IOException e)
		{
                    Logger.testResult(false);		
                    TestException t = new TestException();
                    throw t;
                }
		
		Logger.testCase(testCode);
		Logger.testResult(true);
	}
      	/*----------------------------------------------------------------------------
	testCase05
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Read Policy of LAO object. 
                     Readeable Fields can be read by all user object despite their role
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase05() throws TestException
	{
                String a ;
                LocalAuthenticator lao;
                 Command c = new Command();               
                String testCase = testBatch +" /" + "Test Case 05";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A			
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY); 
                        
                            // instantiate a local User object for Admin
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin.updateKey("panda");

                            // instantiate a local User object for Admin
			User admin1 = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin1.updateKey("panda");
                        
                            // instantiate a local User 
			User user = new User(superA, User.USER_ROLE_USER, "pinco");
			user.updateKey("pallino");
                        
                            // instantiate a local User 
			User user1 = new User(superA, User.USER_ROLE_USER, "pinco");
			user1.updateKey("pallino");                        
	//#
	//# Super A can read the status field of a LAO's SuperA
	//#
                        Logger.detail("-- Super A can read the status field of LAO's SuperA  --");                      
                        lao = superA.getLocalAuthenticatorObject();
                        lao.syncroFields(superA);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                        
                        
        //#
	//# Super A can read the status field of LAO's User
	//#
                        Logger.detail("-- Super A can read the status field of LAO's User  --");                      
                        lao = user.getLocalAuthenticatorObject();
                        lao.syncroFields(superA);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                         
	//#
	//# Super A can read the status field of LAO's Administrator
	//#
                        Logger.detail("-- Super A can read the status field of LAO's Administrator  --");                      
                        lao = admin.getLocalAuthenticatorObject();
                        lao.syncroFields(superA);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                        
	//#
	//# Administrator can read the status field of LAO's SuperA
	//#
                        Logger.detail("-- Administrator can read the status field of LAO's SuperA  --");                      
                        lao = superA.getLocalAuthenticatorObject();
                        lao.syncroFields(admin);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                        
        //#
	//# Administrator can read the status field of LAO's User
	//#
                        Logger.detail("-- Administrator can read the status field of LAO's User  --");                      
                        lao = user.getLocalAuthenticatorObject();
                        lao.syncroFields(admin);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                         
	//#
	//# Administrator can read the status field of LAO's Administrator
	//#
                        Logger.detail("-- Administrator can read the status field of LAO's Administrator  --");                      
                        lao = admin1.getLocalAuthenticatorObject();
                        lao.syncroFields(admin);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                                              
        //#
	//# User can read the status field of LAO's User
	//#
                        Logger.detail("-- User can read the status field of LAO's User  --");                      
                        lao = user.getLocalAuthenticatorObject();
                        lao.syncroFields(user);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                       
	//#
	//# User can read the status field of LAO's Administrator
	//#
                        Logger.detail("-- User can read the status field of LAO's Administrator  --");                      
                        lao = admin.getLocalAuthenticatorObject();
                        lao.syncroFields(user);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                        
        //#
	//# User can read the status field of LAO's Super A
	//#
	//#
                        Logger.detail("-- User can read the status field of LAO's Super A  --");                      
                        lao = superA.getLocalAuthenticatorObject();
                        lao.syncroFields(user);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();  
 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);
                        user1.delete(superA);
                        admin1.delete(superA);
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
	testCase06
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Change PIN. 
                     Check that Super A can set the PIN and change it;
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase06() throws TestException
	{
                String a, pin;
                LocalAuthenticator lao;
                Command c = new Command();                
                String testCase = testBatch +" /" + "Test Case 06";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);		
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        
                            // instantiate a local User object for Admin
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin.updateKey("panda");
                        
                            // instantiate a local User 
			User user = new User(superA, User.USER_ROLE_USER, "pinco");
			user.updateKey("pallino");
                        
                        pin = "313233343536373839303132";
                                               
	//#
	//# Super A can set the PIN. PIN Status changes. PIN Size Min.
	//#
                        Logger.detail("-- SuperA sets the LAO PIN  --");  
                        
                        lao = superA.getLocalAuthenticatorObject();
                        
                        superA.setPin(superA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        
                        lao.syncroFields(superA);
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

        //#
	//# Super A can Change the PIN , Old PIN verified. Status do not changes. PIN Size Medium.
	//#
                        
                            // Verify PIN and PIN change
			Logger.detail("------------ Super A Verify PIN and change PIN ------------");
                        superA.changePin(superA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1), pin.substring(2, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-3)<<1));                        
                        lao.syncroFields(superA);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

        //#
	//# Super A can Change the PIN , Old PIN verified. Status do not changes. PIN Size Max.
	//#
                        
                            // Verify PIN and PIN change
			Logger.detail("------------ Super A Verify PIN and change PIN Max Len ------------");
                        superA.changePin(superA, pin.substring(2, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-3)<<1), pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                        
                        lao.syncroFields(superA);
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

                            // Verify PIN and PIN change
			Logger.detail("------------ Super A Verify PIN------------");
                        superA.changePin(superA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1), pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                                                

	//#
	//# Admin can set the PIN. PIN Status changes. PIN Size Min.
	//#
                        Logger.detail("-- Admin set the PIN  --");  
                        
                        lao = admin.getLocalAuthenticatorObject();
                        lao.syncroFields(admin);
                        
                        admin.setPin(admin, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

        //#
	//# Admin changes the PIN, Old PIN verified. Status do not changes. PIN Size Medium.
	//#
                        
                            // Verify PIN and PIN change
			Logger.detail("------------ Admin change the PIN ------------");
                        admin.changePin(admin, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1), pin.substring(2, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-3)<<1));                        
                        lao.syncroFields(admin);
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

        //#
	//# Admin changes the PIN, Old PIN verified. Status do not changes. PIN Size Max.
	//#
                        
                            // Verify PIN and PIN change
			Logger.detail("------------ Admin can change the PIN with max Len ------------");
                        admin.changePin(admin, pin.substring(2, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-3)<<1), pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                        
                        lao.syncroFields(admin);
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

                            // Verify PIN and PIN change
			Logger.detail("------------ Check PIN and change PIN ------------");
                        admin.changePin(admin, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1), pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                                                
                        
	//#
	//# User can set the PIN. PIN Status changes. PIN Size Min.
	//#
                        Logger.detail("-- User set the PIN  --");  
                        
                        lao = user.getLocalAuthenticatorObject();
                        lao.syncroFields(user);
                        
                        user.setPin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

        //#
	//# User changes the PIN, Old PIN verified. Status do not changes. PIN Size Medium.
	//#
                        
                            // Verify PIN and PIN change
			Logger.detail("------------ User change the PIN ------------");
                        user.changePin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1), pin.substring(2, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-3)<<1));                        
                        lao.syncroFields(user);
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

        //#
	//# User changes the PIN, Old PIN verified. Status do not changes. PIN Size Max.
	//#
                        
                            // Verify PIN and PIN change
			Logger.detail("------------ User can change the PIN with max Len ------------");
                        user.changePin(user, pin.substring(2, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-3)<<1), pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                        
                        lao.syncroFields(user);
                        
                            // Check Status 
			Logger.detail("------------ Check Status ------------");
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_READY ))
				throw new ObjectException();

                            // Verify PIN and PIN change
			Logger.detail("------------ Check PIN and change PIN ------------");
                        user.changePin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1), pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                                                                       
 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);                        
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
	testCase07
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: PIN decrease in case of wrong verification
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase07() throws TestException
	{
                String a, pin, expectedRes;
                LocalAuthenticator lao;
                Apdu apdu;
                int startRetryCounter, retryCounter;
                Command c = new Command();
                
                String testCase = testBatch +" /" + "Test Case 07";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);		
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        
                            // instantiate a local User object for Admin
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin.updateKey("panda");
                        
                            // instantiate a local User 
			User user = new User(superA, User.USER_ROLE_USER, "pinco");
			user.updateKey("pallino");
                        
                        pin = "313233343536373839303132";
                        
                        lao = user.getLocalAuthenticatorObject();
                        
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");                        
	//#
	//# Super A can update the max retried Retry of a User Lao
	//#
                        Logger.detail("-- Super A can update the max retried Retry of a User Lao  --");
                        
                        user.setPin(user, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        
                        lao = user.getLocalAuthenticatorObject();
                        
                        lao.setMaxRetries(superA, (byte) 3);

                        lao.syncroFields(user);
                        a = lao.getRetryCounter();
                        startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];
                        
                            // block the PIN
                        for (int i = 1; i<(startRetryCounter+1); i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = user;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(user);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();                          
                        }
                        
                        lao.syncroFields(user);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. 
                        if (0 != retryCounter)
                              throw new ObjectException();                        
                        
                        a = user.resetPin(admin);            

                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if ( LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE != retryCounter)
                                throw new ObjectException();                       
                        
	//#
	//# Super A can update the max retries of a Admin Lao 
	//#  
                       Logger.detail("-- Super A can update the max retries of a Admin Lao  --");
                        
                        admin.setPin(admin, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        
                        lao = admin.getLocalAuthenticatorObject();
                        
                        lao.setMaxRetries(superA, (byte) 3);

                        lao.syncroFields(admin);
                        a = lao.getRetryCounter();
                        startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];
                        
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");                          
                        
                        for (int i = 1; i<(startRetryCounter+1); i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = admin;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(admin);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();                          
                        }
                        lao.syncroFields(admin);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];                      

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();                        
                        
                        a = user.resetPin(admin);            

                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if ( LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE != retryCounter)
                                throw new ObjectException();                          
	//#
	//# Super A can update the MAX retries Counter of a Super A Lao 
	//#
                      Logger.detail("-- Super A can update the MAX retries Counter of a Super A Lao   --");
                        
                        superA.setPin(superA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        
                        lao = superA.getLocalAuthenticatorObject();
                        
                        lao.setMaxRetries(superA, (byte) 3);
                        
                        lao.syncroFields(admin);
                        a = lao.getRetryCounter();
                        startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];                       

                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");                           
                        
                        for (int i = 1; i<(startRetryCounter+1); i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = superA;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(admin);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();                          
                        }
                        
                        lao.syncroFields(admin);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();                        
                        
                        a = user.resetPin(admin);            

                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if ( LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE != retryCounter)
                                throw new ObjectException();                
                                                 
	//#
	//# User can not update the Retry Counter of a Super A Lao 
	//# 
	//#
                        Logger.detail("-- User can not update the Retry Counter of a Super A Lao    --");
                      
                        lao = superA.getLocalAuthenticatorObject();
                        lao.syncroFields(superA);
                        
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                        apdu = 	new Apdu(Apdu.UPDATE_LAO_RETRY_COUNTER_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, lao.getObjectId());
                        apdu.addTlv(Atlv.DATA_TAG_PIN_MAX_RETRIES, "03");

                        c.description = "Update Retry Counter ";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);                        
	//#
	//# User can not update the Retry Counter of an Admin Lao 
	//# 
                        Logger.detail("-- User can not update the Retry Counter of a Super A Lao    --");
                      
                        lao = admin.getLocalAuthenticatorObject();
                        lao.syncroFields(admin);
                        
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                        apdu = 	new Apdu(Apdu.UPDATE_LAO_RETRY_COUNTER_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, lao.getObjectId());
                        apdu.addTlv(Atlv.DATA_TAG_PIN_MAX_RETRIES, "03");

                        c.description = "Update Retry Counter ";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);                        
	//#
	//# User can not update the Retry Counter of an User Lao 
	//#
                        Logger.detail("-- User can not update the Retry Counter of a Super A Lao    --");
                      
                        lao = user.getLocalAuthenticatorObject();
                        lao.syncroFields(admin);                        
                        
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                        apdu = 	new Apdu(Apdu.UPDATE_LAO_RETRY_COUNTER_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, lao.getObjectId());
                        apdu.addTlv(Atlv.DATA_TAG_PIN_MAX_RETRIES, "03");

                        c.description = "Update Retry Counter ";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);   
 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);                        
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
	testCase08
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Check the retry counter, PIN block (User)
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase08() throws TestException
	{
                String a, pin, expectedRes;
                Apdu apdu;
                int retryCounter, startRetryCounter;
                
                Command c = new Command();
                
                LocalAuthenticator lao;
                
                String testCase = testBatch +" /" + "Test Case 08";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);		
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        
                            // instantiate a local User object for Admin
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin.updateKey("panda");
                        
                            // instantiate a local User 
			User user = new User(superA, User.USER_ROLE_USER, "pinco");
			user.updateKey("pallino");
                        
                        pin = "313233343536373839303132";
                        
                        lao = user.getLocalAuthenticatorObject();
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");                        
	//#
	//# User Retry counter decrease 
	//#
                        Logger.detail("-- User retry counter decrease  --");  
                                                
                        a = user.setPin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));
                        
                            // Check Retry Counter 
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();                       
                        
                        startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];
                        
                        for (int i = 1; i<4; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = user;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(superA);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
         
	//#
	//# User Retry counter blocks 
	//#
                        a = user.changePin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1), pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        
                            // Check Retry Counter 
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                        startRetryCounter =  DatatypeConverter.parseHexBinary(a)[0];
                        
                       for (int i = 1; i<LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE+1; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = user;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(superA);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
                        
	//#
	//# Retry counter is over. it is not possible ro change the PIN.
	//#                            // retry counter is over
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A81_INCORRECT_PIN_COUNTER_LIMIT_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                       
                            // Correct PIN
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
                        
                        c.description = "Change PIN attempt: retry counter over ";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);
                        
                        lao.syncroFields(user);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();                        

                            // Wrong PIN
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE)<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
                        
                        c.description = "Change PIN attempt: retry counter over ";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);
                        
                        lao.syncroFields(user);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();
	//#
	//# superA Reset user PIN
	//#                         
                        a = user.resetPin(superA);
                        
                              // Transaction Counter is set to the Max Value
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                            // Check Status - PIN not ready
			Logger.detail("------------ Check Status ------------");
                        lao.syncroFields(user);
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();
 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);                        
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
	testCase09
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Check the retry counter, PIN block; Super A;
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase09() throws TestException
	{
                String a, pin, expectedRes;
                Apdu apdu;
                int retryCounter, startRetryCounter;
                
                Command c = new Command();
                
                LocalAuthenticator lao;
                
                String testCase = testBatch +" /" + "Test Case 09";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);		
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        superA.resetPin(superA);
                        
                            // instantiate a local User object for Admin
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin.updateKey("panda");
                        
                            // instantiate a local User 
			User user = new User(superA, User.USER_ROLE_USER, "pinco");
			user.updateKey("pallino");
                        
                        pin = "313233343536373839303132";
                        
                        lao = superA.getLocalAuthenticatorObject();
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");                        
	//#
	//# User Retry counter decrease 
	//#
                        Logger.detail("-- User retry counter decrease  --");  
                                                
                        a = superA.setPin(superA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));
                        
                            // Check Retry Counter 
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();                       
                        
                        startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];
                        
                        for (int i = 1; i<4; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = superA;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(superA);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
         
	//#
	//# User Retry counter blocks 
	//#
                        a = superA.changePin(superA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1), pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        
                            // Check Retry Counter 
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                        startRetryCounter =  DatatypeConverter.parseHexBinary(a)[0];
                        
                       for (int i = 1; i<LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE+1; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = superA;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(superA);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
                        
	//#
	//# Retry counter is over. it is not possible ro change the PIN.
	//#                            // retry counter is over
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A81_INCORRECT_PIN_COUNTER_LIMIT_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                       
                            // Correct PIN
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
                        
                        c.description = "Change PIN attempt: retry counter over ";
                        c.requester = superA;
                        c.execute(apdu.toString(), expectedRes);
                        
                        lao.syncroFields(superA);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();                        

                            // Wrong PIN
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE)<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
                        
                        c.description = "Change PIN attempt: retry counter over ";
                        c.requester = superA;
                        c.execute(apdu.toString(), expectedRes);
                        
                        lao.syncroFields(superA);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();
	//#
	//# superA Reset his PIN
	//#                         
                        a = superA.resetPin(superA);
                        
                              // Transaction Counter is set to the Max Value
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                            // Check Status - PIN not ready
			Logger.detail("------------ Check Status ------------");
                        lao.syncroFields(superA);
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();
 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);                        
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
	testCase10
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Check the retry counter, PIN block; Admin;
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase10() throws TestException
	{
                String a, pin, expectedRes;
                Apdu apdu;
                int retryCounter, startRetryCounter;
                
                Command c = new Command();
                
                LocalAuthenticator lao;
                
                String testCase = testBatch +" /" + "Test Case 10";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);		
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        superA.resetPin(superA);
                        
                            // instantiate a local User object for Admin
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin.updateKey("panda");
                        
                            // instantiate a local User 
			User user = new User(superA, User.USER_ROLE_USER, "pinco");
			user.updateKey("pallino");
                        
                        pin = "313233343536373839303132";
                        
                        lao = admin.getLocalAuthenticatorObject();
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                     Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");                        
	//#
	//# User Retry counter decrease 
	//#
                        Logger.detail("-- User retry counter decrease  --");  
                                                
                        a = admin.setPin(admin, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));
                        
                            // Check Retry Counter 
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();                       
                        
                        startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];
                        
                        for (int i = 1; i<4; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = admin;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(admin);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
         
	//#
	//# User Retry counter blocks 
	//#
                        a = admin.changePin(admin, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1), pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        
                            // Check Retry Counter 
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                       startRetryCounter =  DatatypeConverter.parseHexBinary(a)[0];
                        
                       for (int i = 1; i<LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE+1; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = admin;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(admin);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
                        
	//#
	//# Retry counter is over. it is not possible to change the PIN.
	//#                            // retry counter is over
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A81_INCORRECT_PIN_COUNTER_LIMIT_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                       
                            // Correct PIN
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
                        
                        c.description = "Change PIN attempt: retry counter over ";
                        c.requester = admin;
                        c.execute(apdu.toString(), expectedRes);
                        
                        lao.syncroFields(admin);                        
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();                        

                            // Wrong PIN
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE)<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
                        
                        c.description = "Change PIN attempt: retry counter over ";
                        c.requester = admin;
                        c.execute(apdu.toString(), expectedRes);

                        lao.syncroFields(admin);                        
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();
	//#
	//# superA Reset admin PIN
	//#                         
                        a = admin.resetPin(superA);
                        
                              // Transaction Counter is set to the Max Value
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                            // Check Status - PIN not ready
			Logger.detail("------------ Check Status ------------");
                        lao.syncroFields(admin);
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();
 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);                        
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
	testCase11
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Test Reset PIN.
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase11() throws TestException
	{
                String a, pin, expectedRes;
                LocalAuthenticator lao;                               
                Command c = new Command();
                int startRetryCounter, retryCounter;
                Apdu apdu;
                String testCase = testBatch +" /" + "Test Case 11";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);		
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        superA.resetPin(superA);
                        
                            // instantiate a local User object for Admin
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin.updateKey("panda");
                        
                            // instantiate a local User 
			User user = new User(superA, User.USER_ROLE_USER, "pinco");
			user.updateKey("pallino");
                        
                        pin = "313233343536373839303132";
                        lao = user.getLocalAuthenticatorObject();
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, LocalAuthenticator.AUTHOBJ_MIN_PIN_SIZE_VALUE<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
	//#
	//# superA Reset user PIN when retry counter is at 0
	//#
                       a = user.setPin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                       
                       startRetryCounter =  DatatypeConverter.parseHexBinary(a)[0];
                        
                       for (int i = 1; i<LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE+1; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = user;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(admin);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
                        a = user.resetPin(superA);
                        
                              // Transaction Counter is set to the Max Value
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                            // Check Status - PIN not ready
			Logger.detail("------------ Check Status ------------");
			lao.syncroFields(user);
                        a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                                                                       
	//#
	//# superA Reset user PIN when retry counter is not at 0 and not at the Max Value
	//#
                        user.setPin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                       
                       
                        c.description = "Change PIN attempt ";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);

 			lao.syncroFields(user);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if ((LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE-1) != retryCounter)
                                throw new ObjectException();

                        a = user.resetPin(superA);
                        
                              // Transaction Counter is set to the Max Value
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                            // Check Status - PIN not ready
			Logger.detail("------------ Check Status ------------");
			lao.syncroFields(user);			
                        a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                                                                       
                        
	//#
	//# administrator Reset user PIN when retry counter is no at 0
	//#
                       a = user.setPin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                       
                       startRetryCounter =  DatatypeConverter.parseHexBinary(a)[0];
                        
                        for (int i = 1; i<LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE+1; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = user;
                            c.execute(apdu.toString(), expectedRes); 
                            lao.syncroFields(admin);                            
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
                        a = user.resetPin(admin);
                        
                              // Transaction Counter is set to the Max Value
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                            // Check Status - PIN not ready
			Logger.detail("------------ Check Status ------------");
                        lao.syncroFields(admin); 			
                        a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                        
	//#
	//# administrator Reset user PIN when retry counter is at Max Value
	//#
                       a = user.setPin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));                       

                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if ((LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE) != retryCounter)
                                throw new ObjectException();

                        a = user.resetPin(admin);
                        
                              // Transaction Counter is set to the Max Value
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                            // Check Status - PIN not ready
			Logger.detail("------------ Check Status ------------");
                        lao.syncroFields(user);                         
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();                         
	//#
	//# user can not reset his PIN
	//#
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                        
                        lao = user.getLocalAuthenticatorObject();
                        
                        apdu = 	new Apdu(Apdu.RESET_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, lao.getObjectId());

                        c.description = "User Can Not Reset his PIN";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);                        
	//#
	//# user can not reset superA PIN
	//#
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                                        
                        lao = superA.getLocalAuthenticatorObject();
                        
                        apdu = 	new Apdu(Apdu.RESET_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, lao.getObjectId());

                        c.description = "User Can Not Reset his PIN";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);                        
	//#
	//# user can not reset admin PIN
	//#
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                                        
                        lao = admin.getLocalAuthenticatorObject();
                        
                        apdu = 	new Apdu(Apdu.RESET_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, lao.getObjectId());

                        c.description = "User Can Not Reset his PIN";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);
	//#
	//# An error is returned if the object is not a LAO
	//#
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                                        
                        lao = admin.getLocalAuthenticatorObject();
                        
                        apdu = 	new Apdu(Apdu.RESET_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, superA.getObjectId());

                        c.description = "User Can Not Reset his PIN";
                        c.requester = superA;
                        c.execute(apdu.toString(), expectedRes);  
 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);                        
                }
            catch (CommandErrorException | ObjectException  | IOException e)
            {
                    Logger.testResult(false);		
                    TestException t = new TestException();
                    throw t;
            }

            Logger.testCase(testCase);
            Logger.testResult(true);
    }
	/*----------------------------------------------------------------------------
	testCase12
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Check that Create, update and delete door updates the log.
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase12() throws TestException
	{
                int index, pIndex;
                LogEntry le;
                String laoId, requestorId;
                LocalAuthenticator lao;
                 Command c = new Command();               
                String testCode = testBatch+"/"+"Test Case 12";                
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCode);
			
				// launch a ping
			thisDevice.ping();
			
				// instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        superA.resetPin(superA);	
                        
				// object created
			User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
			admin.updateKey("newAdministratorKeysCiccia");
                        
				// object created
			User user = new User(admin, User.USER_ROLE_USER, "rigqa");
			user.updateKey("newSutta");
                        
                        lao = superA.getLocalAuthenticatorObject();
                        			
        //#
	//# Log is updated when a PIN is set
	//#
                        Logger.detail("------------ Log is updated when a PIN is set ------------");                                                
                        
                        pIndex = DeviceLogger.getLastEntryIndex(superA);
                        
                        superA.setPin(superA, "3132333435");
                        
                        index = DeviceLogger.getLastEntryIndex(superA);
                        
                        if ((index-1) != pIndex)
                        {
                            throw new TestException();
                        }

                        requestorId = superA.getObjectId();
                        
                        lao.syncroFields(superA);
                        laoId = lao.getObjectId();

                        le = DeviceLogger.getEntry(superA, index);

                            // check log entry
                        if (!( requestorId.equals(le.requesterId) ) ||
                                (!laoId.equals(le.objectId)) ||
                                (!DeviceLogger.SE_LOG_APDU_CHANGE_PIN.equals(le.event))||
                                (!"9000".equals(le.result)))
                        {
                            throw new TestException();
                        }                 
        //#
	//# Log is updated when there is a change PIN;
	//#
                       Logger.detail("------------ Log is updated when a PIN is changed ------------");                                                
                        
                        pIndex = DeviceLogger.getLastEntryIndex(superA);
                        
                        superA.changePin(superA, "3132333435", "313233343535");
                        
                        index = DeviceLogger.getLastEntryIndex(superA);
                        
                        if ((index-1) != pIndex)
                        {
                            throw new TestException();
                        }

                        requestorId = superA.getObjectId();
                        lao.syncroFields(superA);
                        laoId = lao.getObjectId();

                        le = DeviceLogger.getEntry(superA, index);

                            // check log entry
                        if (!( requestorId.equals(le.requesterId) ) || 
                                (!laoId.equals(le.objectId)) ||
                                (!DeviceLogger.SE_LOG_APDU_CHANGE_PIN.equals(le.event)) ||
                                (!"9000".equals(le.result)))
                        {
                            throw new TestException();
                        }
        //#
	//# Log is updated when there is a reset PIN;
	//#
                       Logger.detail("------------ Log is updated when a PIN is reset ------------");                                                
                        
                        pIndex = DeviceLogger.getLastEntryIndex(superA);
                        
                        superA.resetPin(superA);
                        
                        index = DeviceLogger.getLastEntryIndex(superA);
                        
                        if ((index-1) != pIndex)
                        {
                            throw new TestException();
                        }

                        requestorId = superA.getObjectId();
                        
                        lao.syncroFields(superA);
                        laoId = lao.getObjectId();

                        le = DeviceLogger.getEntry(superA, index);

                            // check log entry
                        if (!( requestorId.equals(le.requesterId) ) || 
                                (!laoId.equals(le.objectId)) ||
                                (!DeviceLogger.SE_LOG_APDU_RESET_PIN.equals(le.event)) ||
                                (!"9000".equals(le.result)))
                        {
                            throw new TestException();
                        }                         
        //#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA);                     
                        
		}
		catch (CommandErrorException | ObjectException | TestException | IOException e)
		{
                    Logger.testResult(false);		
                    TestException t = new TestException();
                    throw t;
                }
		
		Logger.testCase(testCode);
		Logger.testResult(true);
        }
     	/*----------------------------------------------------------------------------
	testCase13
	--------------------------------------------------------------------------
	AUTHOR:	PDI

	DESCRIPTION: Check the retry counter, PIN block when try to access a door
	
	Security Level: None

	------------------------------------------------------------------------------*/
	public static void testCase13() throws TestException
	{
                String a, pin, expectedRes;
                Apdu apdu;
                int retryCounter, startRetryCounter;
                
                Command c = new Command();
                
                LocalAuthenticator lao;
                
                String testCase = testBatch +" /" + "Test Case 13";               
		
		// ---------------------- Code -------------------------------
		try
		{
			
			Logger.testCase(testCase);
			
				// launch a ping
			thisDevice.ping();
			
                            // instantiate a local User object for the SUPER-A
			//User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);		
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        superA.resetPin(superA);                        
                        
                            // instantiate a local User object for Admin
			User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
			admin.updateKey("panda");    
                        
                            // instantiate a local User 
			User user = new User(superA, User.USER_ROLE_USER, "pinco");
			user.updateKey("pallino");
                        
                        Door door = new Door(superA);
                        door.syncroFields(superA);
                            // Update the door
                        door.setStatus(Door.SE_DOOR_ENABLED);
                        door.setSecurity(Door.SE_DOOR_SECURITY_PIN);
                        door.setGpioId(0);
                        door.update(superA);

                        String policyId = admin.getAcPolicy();
                        AccessPolicy  defaultPolicy = new AccessPolicy(admin, policyId);
                        defaultPolicy.setAlwaysWeeklyPolicy();
                        defaultPolicy.update(superA);                                               
                        
                        pin = "313233343536373839303132";
                        
                        lao = user.getLocalAuthenticatorObject();
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A80_INCORRECT_PIN_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                
                            // create the apdu object
                        apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
                        apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());                
                        apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);
                        apdu.addTlv(Apdu.DATA_TAG_PIN_DATA, "31323334353637");                        
	//#
	//# User Retry counter decrease 
	//#
                        Logger.detail("-- User retry counter decrease  --");  
                                                
                        a = user.setPin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));
                        
                            // Check Retry Counter 
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();                       
                        
                        startRetryCounter = DatatypeConverter.parseHexBinary(a)[0];
                        
                        for (int i = 1; i<4; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = user;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(superA);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
         
	//#
	//# User Retry counter blocks 
	//#
                        door.setOutput (user, Door.DOOR_COMMAND_SWITCH_ON, pin);
                        a = user.changePin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1), pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        
                            // Check Retry Counter 
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                        startRetryCounter =  DatatypeConverter.parseHexBinary(a)[0];
                        
                       for (int i = 1; i<LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE+1; i++)
                        {
                            c.description = "Change PIN attempt " + i;
                            c.requester = user;
                            c.execute(apdu.toString(), expectedRes); 
                            
                            lao.syncroFields(superA);
                            a = lao.getRetryCounter();
                            retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                                // Check Retry Counter. It is decreased of 1
                            if ((startRetryCounter - i) != retryCounter)
                                    throw new ObjectException();
                        }
                        
	//#
	//# Retry counter is over. it is not possible ro change the PIN.
	//#                            // retry counter is over
                                // set the expected response; It checks that all the fields are present.
                        expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                                String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                                String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 
                                                    Apdu.SW_6A81_INCORRECT_PIN_COUNTER_LIMIT_TLV_STRING +
                                                String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                       
                            // Correct PIN
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE-1)<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
                        
                        c.description = "Change PIN attempt: retry counter over ";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);
                        
                        lao.syncroFields(user);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();                        

                            // Wrong PIN
                        apdu = 	new Apdu(Apdu.CHANGE_PIN_APDU);
                        apdu.addTlv(Atlv.DATA_TAG_PIN_DATA, pin.substring(0, (LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE)<<1));
                        apdu.addTlv(Atlv.DATA_TAG_NEW_PIN_DATA, "31323334353637");
                        
                        c.description = "Change PIN attempt: retry counter over ";
                        c.requester = user;
                        c.execute(apdu.toString(), expectedRes);
                        
                        lao.syncroFields(user);
                        a = lao.getRetryCounter();
                        retryCounter =  DatatypeConverter.parseHexBinary(a)[0];

                            // Check Retry Counter. It is decreased of 1
                        if (0 != retryCounter)
                              throw new ObjectException();
	//#
	//# superA Reset user PIN
	//#                         
                        a = user.resetPin(superA);
                        
                              // Transaction Counter is set to the Max Value
			if (0 != a.compareTo( String.format("%02X", LocalAuthenticator.AUTHOBJ_INITIAL_RETRY_COUNTER_VALUE )))
				throw new ObjectException();
                        
                            // Check Status - PIN not ready
			Logger.detail("------------ Check Status ------------");
                        lao.syncroFields(user);
			a = lao.getStatus();
			if (0 != a.compareTo( LocalAuthenticator.AUTHOBJ_PIN_NOT_READY ))
				throw new ObjectException();

                        a = user.setPin(user, pin.substring(0, LocalAuthenticator.AUTHOBJ_MAX_PIN_SIZE_VALUE<<1));
                        door.setOutput (user, Door.DOOR_COMMAND_SWITCH_OFF, pin);                        
 	//#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        user.delete(superA); 
                        door.delete(superA);
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

