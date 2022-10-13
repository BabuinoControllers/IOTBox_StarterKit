
package TestCases;
    
import com.sdk.*;
import java.io.IOException;
import java.util.Calendar;


public class TestDigitalFunctionBlock001 {
    
   /********************************
    PUBLIC Fields
    ********************************/
    public static final String testBatch = "TestFiniteStateMachine001";
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

                testCase13();
                j++;
                
                testCase14();
                j++;

                testCase15();
                j++;

                testCase16();
                j++;

                testCase17();
                j++;
                
                testCase18();
                j++;
                
                testCase19();
                j++;
                
                testCase20();
                j++;
                
                testCase21();
                j++;

                testCase22();
                j++; 

                testCase23();
                j++;
                    
        }
        catch ( TestException | CommandErrorException |  ObjectException |IOException | DiscoveryException e){
            
             Logger.detail("TEST FAILURE ----->" + j);
            return false;
        }            
        
        return true;
    }
    /*----------------------------------------------------------------------------
    testCase01
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA creates a Digital Function Block object. Initial field values are as expected.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException
    {
            String a, name;
            DigitalFunctionBlock dfb;
            Command c = new Command();
            String testCase = testBatch +" /" + "Test Case 01";               

            // ---------------------- Code -------------------------------
            try
            {			
                    Logger.testCase(testCase);

                            // launch a ping
                    thisDevice.ping();

                        // object personalized
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
    //#
    //# Super A creates Digital FUnction Block object. Initial values are according to specs.
    //# 
                        // Create Door
                    Logger.detail("------------ Create Logical Function Block ------------");                        
                    dfb = new DigitalFunctionBlock(superA);
                    dfb.syncroFields(superA);
                    

                        // Check SD ID
                    Logger.detail("------------ Check Security Domain ID ------------");
                    a = dfb.getSecurityDomain();
                    if (0 != a.compareTo(superA.getSecurityDomain()))
                            throw new ObjectException();

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    a = dfb.getStatus();
                    if (0 != a.compareTo( DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_STATUS_ACTIVE))
                            throw new ObjectException();

                            // The Name is the initial name
                    Logger.detail("------------ Check that name is the default one ------------");
                    a = dfb.getName();			
                    name = dfb.getObjectId();
                    if (0 != a.compareTo(name))
                            throw new CommandErrorException();

                        // Check Function
                    Logger.detail("------------ Check Function ------------");
                    a = dfb.getFunction();
                    if (0 != a.compareTo( DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OR))
                            throw new ObjectException();

                        // Check Output Object Id
                    Logger.detail("------------ Check Output Object Id ------------");
                    a = dfb.getGpio();
                    if (0 != a.compareTo( "FF"))
                            throw new ObjectException();
                        
                        // Check Event Log
                    Logger.detail("------------ Check Event Log ------------");
                    a = dfb.getLogEvent();
                    if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_NO_LOG))
                            throw new ObjectException();                    

                        // Check Weekly Policy Object id
                    Logger.detail("------------ Check Weekly Policy Object id ------------");
                    a = dfb.getWeeklyPolicyId();
                    if (0 != a.compareTo( "FFFFFFFE"))
                            throw new ObjectException();

                        // Check on Delay
                    Logger.detail("------------ Check on Delay ------------");
                    a = dfb.getOnDelay();
                    if (0 != a.compareTo( "00000000"))
                            throw new ObjectException();

                        // Check off Delay
                    Logger.detail("------------ Check off Delay ------------");
                    a = dfb.getOffDelay();
                    if (0 != a.compareTo( "00000000"))
                            throw new ObjectException();
                    
                        // Check pulse
                    Logger.detail("------------ Check pulse ------------");
                    a = dfb.getPulse();
                    if (0 != a.compareTo( "00000000"))
                            throw new ObjectException();                  

                        // Check Reset Object Id
                    Logger.detail("------------ Check Reset Object Id ------------");
                    a = dfb.getResetObjectId();
                    if (0 != a.compareTo( "FFFFFFFE"))
                            throw new ObjectException();
                    
                        // input list
                    Logger.detail("------------ Check input list ------------");
                    for (int i = 0; i<DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER; i++)
                    {
                        a = dfb.getInputId(i);
                        if (0 != a.compareTo( "FFFFFFFE"))
                                throw new ObjectException();                     
                    }
                    Logger.detail("------------ Check input list index ------------");
                    for (int i = 0; i<DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER; i++)
                    {
                        a = dfb.getInputWire(i);
                        if (0 != a.compareTo( "00"))
                                throw new ObjectException();                     
                    }               
    //#
    //# Object deletion
    //#                       
                    dfb.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

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

    DESCRIPTION: Only SuperA can create Digital Function Block.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException
    {
        String  expectedRes;
        Apdu apduObject;
        Command c = new Command();               

        String testCase = testBatch +" /" + "Test Case 02";               

        // ---------------------- Code -------------------------------
        try
        {			
                Logger.testCase(testCase);

                        // launch a ping
                thisDevice.ping();

                    // object personalized
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                    // instantiate a local User object for the SUPER-A
                User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");

                    // object personalized
                admin.updateKey("panda");
                admin.syncroFields(admin);

                    // instantiate a local User object for the SUPER-A
                User user = new User(superA, User.USER_ROLE_USER, "zaffata");

                    // object personalized
                user.updateKey("penda");
                user.syncroFields(user);
//#
//# User can not create Finite 
//# 
                Logger.detail("------------ User can not create digital function block ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to create a door
                apduObject = new Apdu(Apdu.CREATE_SE_LOGICAL_FUNCTION_BLOCK_APDU );

                    // send command
                c.description = "Create Digital Function Block";
                c.requester = user;
                c.execute(apduObject.toString(), expectedRes);                         
//#
//# Administrator can not create door
//#
                Logger.detail("------------ Administrator can not create digital function block ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to create a door
                apduObject = new Apdu(Apdu.CREATE_SE_LOGICAL_FUNCTION_BLOCK_APDU );


                    // send command
                c.description = "Create digital function block";
                c.requester = admin;
                c.execute(apduObject.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

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

    DESCRIPTION: Readeable Fields can be read by all user object despite their role

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException
    {
            String a ;
            DigitalFunctionBlock dfb;
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
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

                        // instantiate a local User object for Admin
                    User admin = new User(superA, User.USER_ROLE_ADMIN, "zebra");
                    admin.updateKey("panda");
                    admin.syncroFields(admin);

                        // instantiate a local User object for Admin
                    User admin1 = new User(superA, User.USER_ROLE_ADMIN, "zebra");
                    admin1.updateKey("panda");
                    admin1.syncroFields(admin1);

                        // instantiate a local User 
                    User user = new User(superA, User.USER_ROLE_USER, "pinco");
                    user.updateKey("pallino");
                    user.syncroFields(user);

                        // instantiate a local User 
                    User user1 = new User(superA, User.USER_ROLE_USER, "pinco");
                    user1.updateKey("pallino");
                    user1.syncroFields(user1);

                        // Create Door
                    Logger.detail("------------ Create Digital function block ------------");                        
                    dfb = new DigitalFunctionBlock(superA);
                    dfb.syncroFields(superA);
    //#
    //# Super A can read the status field of a Digital Function Block
    //#
                    Logger.detail("-- Super A can read the status field  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    dfb.syncroFields(superA);
                    a = dfb.getStatus();
                    if (0 != a.compareTo( DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_STATUS_ACTIVE))
                            throw new ObjectException();                        

    //#
    //# Administrator can read the status field of a Digital Function Block
    //#
                    Logger.detail("-- Administrator can read the status field  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    dfb.syncroFields(admin);
                    a = dfb.getStatus();
                    if (0 != a.compareTo( DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_STATUS_ACTIVE ))
                            throw new ObjectException();                        
    //#
    //# User can read the status field of a Digital Function Block
    //#
                    Logger.detail("-- User can read the status field  --");                      

                        // Check Status 
                    Logger.detail("------------ Check Status ------------");
                    dfb.syncroFields(user);
                    a = dfb.getStatus();
                    if (0 != a.compareTo( DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_STATUS_ACTIVE))
                            throw new ObjectException(); 
    //#
    //# Object deletion
    //#                       
                    admin.delete(superA);
                    admin1.delete(superA);
                    user.delete(superA);
                    user1.delete(superA);
                    dfb.delete(superA);
                    superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
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
    testCase04
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can updated the fileds of a Door object. User and Admin not.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException
    {
            String a, expectedRes;
            DigitalFunctionBlock dfb;
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
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);

                    // Create Door
                Logger.detail("------------ Create Door ------------");                        
                dfb = new DigitalFunctionBlock(superA);
                dfb.syncroFields(superA);

                Door door = new Door(superA);
                door.syncroFields(superA);
                
//#
//# Super A can updated the field of a Digital Functional Block Object
//#
                
                    // Function
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_XOR);
                dfb.update(superA);
                dfb.syncroFields(superA);

                Logger.detail("------------ Check Function ------------");
                a = dfb.getFunction();
                if (0 != a.compareTo( DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_XOR ))
                        throw new ObjectException(); 
                
                    // Status
                dfb.setStatus(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_STATUS_INACTIVE);
                dfb.update(superA);
                dfb.syncroFields(superA);

                Logger.detail("------------ Check Status ------------");
                a = dfb.getStatus();
                if (0 != a.compareTo( dfb.SE_DIGITAL_FUNCTION_BLOCK_STATUS_INACTIVE ))
                        throw new ObjectException(); 

                    // On Delay
                dfb.setOnDelay("12345678");
                dfb.update(superA);
                dfb.syncroFields(superA);                

                Logger.detail("------------ Check On Delay ------------");
                a = dfb.getOnDelay();
                if (0 != a.compareTo( "12345678" ))
                        throw new ObjectException();

                    // off Delay
                dfb.setOffDelay("87654321");
                dfb.update(superA);
                dfb.syncroFields(superA);                

                Logger.detail("------------ Check Off Delay ------------");
                a = dfb.getOffDelay();
                if (0 != a.compareTo( "87654321" ))
                        throw new ObjectException(); 

                    // Pulse
                dfb.setPulse("21324354");
                dfb.update(superA);
                dfb.syncroFields(superA);                

                Logger.detail("------------ Check Pulse ------------");
                a = dfb.getPulse();
                if (0 != a.compareTo( "21324354" ))
                        throw new ObjectException(); 

                Logger.detail("------------ Check Weekly Policy ------------");
                    // Weekly Policy
                dfb.setWeeklyPolicy(superA.getAcPolicy());
                dfb.update(superA);
                dfb.syncroFields(superA);
                a = dfb.getWeeklyPolicyId();
                if (0 != a.compareTo( superA.getAcPolicy()))
                        throw new ObjectException();                

                Logger.detail("------------ Reset Object Id ------------");
                    // Reset Object Id
                dfb.setResetObjectId(door.getObjectId());
                dfb.update(superA);
                dfb.syncroFields(superA);
                a = dfb.getResetObjectId();
                if (0 != a.compareTo(door.getObjectId()))
                        throw new ObjectException();

                    // Output Object Id
                dfb.setLogEvent(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_LOG_WHEN_OUTPUT_SET);
                dfb.update(superA);
                dfb.syncroFields(superA);                

                Logger.detail("------------ Check Log Event ------------");
                a = dfb.getLogEvent();
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_LOG_WHEN_OUTPUT_SET))
                        throw new ObjectException();                
                
                    // Output Object Id
                dfb.setGpio("05");
                dfb.update(superA);
                dfb.syncroFields(superA);                

                Logger.detail("------------ Check Gpio ------------");
                a = dfb.getGpio();
                if (0 != a.compareTo( "05"))
                        throw new ObjectException();

                    // Input                
                DigitalFunctionBlock dfbArray[] = new DigitalFunctionBlock[DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER];
                for (int i = 0; i<DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER; i++)
                {
                    dfbArray[i] = new DigitalFunctionBlock(superA);
                    dfb.setInputId(dfbArray[i].getObjectId(), i);
                }             
                dfb.update(superA);
                dfb.syncroFields(superA);                 

                Logger.detail("------------ Check Input Id ------------");
                for (int i=0; i<DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER; i++)
                {
                    a = dfb.getInputId(i);
                    if (0 != a.compareTo( dfbArray[i].getObjectId()))
                        throw new ObjectException();
                }

                                    // Input Wire
                for(int i = 0; i<DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER; i++)
                {
                    dfb.setInputWire("0"+i, i);
                    dfb.update(superA);
                    dfb.syncroFields(superA);   

                    Logger.detail("------------ Check Input Wire ------------");
                    a = dfb.getInputWire(i);
                    if (0 != a.compareTo( "0"+i))
                            throw new ObjectException();
                }
                    // name
                dfb.setName("0123456789ABCDEF0123456789ABCDEF");
                dfb.update(superA);
                dfb.syncroFields(superA); 
                
                Logger.detail("------------ Check Name ------------");
                a = dfb.getName();							
                if (0 != a.compareTo("0123456789ABCDEF0123456789ABCDEF"))
                        throw new CommandErrorException();                
//#
//# Administrator can not update door
//#
                Logger.detail("------------ Administrator can not update Digital Funciton Block ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                Apdu apdu = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_PULSE, "00000001");
                    // send command
                c.description = "Update Dfb";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not update Digital Function BLock
//#
                Logger.detail("------------ user can not update digital function block ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_PULSE, "00000001");
                
                    // send command
                c.description = "Update Digital Function Block";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                dfb.delete(superA);
                door.delete(superA);
                for (int i=0; i<DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER; i++)
                {
                    dfbArray[i].delete(superA);
                    
                }
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
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
    testCase05
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: SuperA can delete a door. User and administrators can not delete a digital function block.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException
    {
            String a, expectedRes;
            DigitalFunctionBlock dfb;
            Command c = new Command();

            String testCode = testBatch+"/"+"Test Case 05";                

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
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);

                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);

//#
//# Super A can delete a door
//#
                Logger.detail("------------ Super A can delete a door ------------");

                    // Create Door
                Logger.detail("------------ Create Door ------------");                        
                dfb = new DigitalFunctionBlock(superA);

                    // Update Initial Configuration. Door exist/
                dfb.setOffDelay("12345678");

                String objId = dfb.getObjectId();

                dfb.delete(superA);                                               

                    // check that the door has been deleted
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update digital function block
                Apdu apdu = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID,objId );
                apdu.addTlv(Atlv.DATA_TAG_ON_DELAY, "12340000");
                    // send command
                c.description = "Update Digital Funciton Blocl";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);                        

//#
//# Administrator can not delete a door
//#
                dfb = new DigitalFunctionBlock(superA);

                Logger.detail("------------ Administrator can not delete a digital funciton block ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());
                    // send command
                c.description = "Delete Digital Funciton Block";
                c.requester = admin;
                c.execute(apdu.toString(), expectedRes);                         

//#
//# user can not delete a door
//#

                Logger.detail("------------ user can not delete a digital funciton block ------------");  

                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                    // try to update the door
                apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);                                        
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());

                    // send command
                c.description = "Delete Digital function block";
                c.requester = user;
                c.execute(apdu.toString(), expectedRes);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                dfb.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        

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
    testCase06
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test the logical and with two input I1 and I2. THe two input are from a door;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException
    {
            Door door, door1;
            String pin, policyId;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 06";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId("04");                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);

                Sensor s = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s.setGpioId("05");
                s.update(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
//#
//# Test of the AND Digital function block (2 input)
//#
                Logger.detail("------------ Test of the AND Digital function block (2 input) ------------");
                Logger.detail("------------ Input 1 is a sensor; Input 2 is a door ------------");
                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(s.getObjectId(), 0);
                dfb.setInputId(door1.getObjectId(), 1);
                dfb.setGpio("00");
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_AND_2_INPUT);
                dfb.update(superA);
                                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                String a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                

                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }               
                
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                door1.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
    /*----------------------------------------------------------------------------
    testCase07
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test the Digital Function Block with 4 input. 

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException
    {
            Door door, door1, door2, door3;
            String pin, policyId;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 07";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId("04");                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);

                    // create the door. Configure it.
                door2 = new Door(superA);                        
                door2.setGpioId("03");                      
                door2.setStatus(Door.SE_DOOR_ENABLED);
                door2.update(superA);
                door2.syncroFields(superA);

                                    // create the door. Configure it.
                door3 = new Door(superA);                        
                door3.setGpioId("02");                      
                door3.setStatus(Door.SE_DOOR_ENABLED);
                door3.update(superA);
                door3.syncroFields(superA);

                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
//#
//# Test of the AND Digital function block (2 input)
//#
                Logger.detail("------------ Test of the AND Digital function block (2 input) ------------");
                Logger.detail("------------ Input 1 is a sensor; Input 2 is a door ------------");
                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setInputId(door1.getObjectId(), 1);
                dfb.setInputId(door2.getObjectId(), 2);
                dfb.setInputId(door3.getObjectId(), 3);
                dfb.setGpio("00");
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_AND_4_INPUT);
                dfb.update(superA);
                                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                }                
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                String a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }               
                
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                door1.delete(superA);
                door2.delete(superA);
                door3.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);
        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
    /*----------------------------------------------------------------------------
    testCase08
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Three digital function blocks.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase08() throws TestException
    {
            Door door, door1, door2, door3;
            String pin, policyId;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 08";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId("04");                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);

                    // create the door. Configure it.
                door2 = new Door(superA);                        
                door2.setGpioId("03");                      
                door2.setStatus(Door.SE_DOOR_ENABLED);
                door2.update(superA);
                door2.syncroFields(superA);

                                    // create the door. Configure it.
                door3 = new Door(superA);                        
                door3.setGpioId("02");                      
                door3.setStatus(Door.SE_DOOR_ENABLED);
                door3.update(superA);
                door3.syncroFields(superA);
                
//#
//# Test of the AND Digital function block (2 input)
//#
                Logger.detail("------------ Test of the AND Digital function block (2 input) ------------");
                Logger.detail("------------ Input 1 is a sensor; Input 2 is a door ------------");
                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setInputId(door1.getObjectId(), 1);
                dfb.setInputId(door2.getObjectId(), 2);
                dfb.setInputId(door3.getObjectId(), 3);
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_AND_4_INPUT);
                dfb.update(superA);

                DigitalFunctionBlock dfb1 = new DigitalFunctionBlock(superA);
                dfb1.setInputId(door.getObjectId(), 0);
                dfb1.setInputId(door1.getObjectId(), 1);
                dfb1.setInputId(door2.getObjectId(), 2);
                dfb1.setInputId(door3.getObjectId(), 3);
                dfb1.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OR);
                dfb1.update(superA);
                
                DigitalFunctionBlock dfb2 = new DigitalFunctionBlock(superA);
                dfb2.setInputId(dfb1.getObjectId(), 0);
                dfb2.setInputId(dfb.getObjectId(), 1);
                dfb2.setGpio("00");
                dfb2.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_XOR);
                dfb2.update(superA);

                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                }                
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                String a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }               
                
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                door1.delete(superA);
                door2.delete(superA);
                door3.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);
        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }    
    /*----------------------------------------------------------------------------
    testCase09
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Check that Create, update and delete Digital Function Block updates the log.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase09() throws TestException
    {
        DigitalFunctionBlock dfb;
        int index;
        LogEntry le;
        String doorId, requestorId;
        Command c = new Command();               
        String testCode = testBatch+"/"+"Test Case 09";                

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
            User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
            admin.updateKey("newAdministratorKeysCiccia");
            admin.syncroFields(admin);

                    // object created
            User user = new User(admin, User.USER_ROLE_USER, "rigqa");
            user.updateKey("newSutta");
            user.syncroFields(user);

//#
//# Log is updated when a Digital Function Block is created
//#
            Logger.detail("------------ Log is updated when a Digital Function Block is created ------------");                        

            dfb = new DigitalFunctionBlock(superA);
            dfb.syncroFields(superA);

            index = DeviceLogger.getLastEntryIndex(superA);	

            requestorId = superA.getObjectId();
            doorId = dfb.getObjectId();

            le = DeviceLogger.getEntry(superA, index-1);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) || 
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_CREATE_EVENT.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result)))
                    {
                            throw new TestException();
            }                 
//#
//# Log is updated when a door is deleted;
//#
            Logger.detail("------------ Log is updated when a door is deleted; ------------");                        

            dfb.delete(superA);

            index = (index + 1) & 0xFFFF;	

            le = DeviceLogger.getEntry(superA, index);

                // check log entry
            if (( 0 != requestorId.compareTo(le.requesterId) ) ||
                    (0 != doorId.compareTo(le.objectId)) ||
                    (0 != DeviceLogger.SE_LOG_APDU_DELETE_EVENT.compareTo(le.event)) ||
                    (0 != "9000".compareTo(le.result)))
                    {
                            throw new TestException();
                    }
//#
//# Object deletion
//#                       
            admin.delete(superA);
            user.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

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
    testCase10
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Weekly Policy Test

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase10() throws TestException
    {
            Door door;
            String pin, policyId;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 10";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                                                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_IDENTITY);
                dfb.setGpio("00");
                dfb.update(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                                
//#
//# An Access Policy is created. It is set tp Never. Output did not change.
//#
                Logger.detail("------------ An Access Policy is created. It is set to Never. Output did not change. ------------");                                                              
                
                ap = new AccessPolicy(superA);
                ap.setNeverWeeklyPolicy();
                ap.update(superA);

                dfb.setWeeklyPolicy(ap.getObjectId());
                dfb.update(superA);
                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                } 

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                String a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
//#
//# Access Policy is set to always. The Digital function block output turns on.
//#
                Logger.detail("------------ An Access Policy is created. It is set to Always. Output changes. ------------");                                                              
                  
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                ap.setAlwaysWeeklyPolicy();
                ap.update(superA);

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) 
                {
                }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);                
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                                

                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) 
                {
                }
//#
//# Access Policy is set to a date and is verified. The Digital function block output turns on.
//#
                Calendar calendar = Calendar.getInstance();
                    
                    // set(int year, int month, int date, int hourOfDay, int minute)
                    // WEDNESDAY 3 May 2017 17:30
                calendar.set(2017, 4, 3, 12, 01);
                thisDevice.setTimeAndDate( calendar, superA, pin);

                DailyPolicy d = new DailyPolicy();
                d.setInterval("1700", "1800", 2);
                d.setInterval("1200", "1300", 1);
                d.setInterval("0700", "1300", 0);
                ap.setDailyPolicy(d, DailyPolicy.WEDNESDAY);
                ap.update(superA);

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException(); 

                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) 
                {
                }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) 
                {
                }
//#
//# Access Policy is set to a date amd is not verified. The Digital function block output turns on.
//#
                calendar = Calendar.getInstance();
                    
                    // set(int year, int month, int date, int hourOfDay, int minute)
                    // WEDNESDAY 3 May 2017 17:30
                calendar.set(2017, 4, 3, 15, 01);
                thisDevice.setTimeAndDate( calendar, superA, pin);

                d = new DailyPolicy();
                d.setInterval("1700", "1800", 2);
                d.setInterval("1200", "1300", 1);
                d.setInterval("0700", "1300", 0);
                ap.setDailyPolicy(d, DailyPolicy.WEDNESDAY);
                ap.update(superA);

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException(); 
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) 
                {
                }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
   /*----------------------------------------------------------------------------
    testCase11
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Pulse

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase11() throws TestException
    {
            Door door;
            String pin, policyId;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 11";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                                                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_IDENTITY);
                dfb.setGpio("00");
                dfb.update(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
   
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                } 
//#
//# Pulse Test
//#
                Logger.detail("------------ Pulse Test ------------");                                                              
                
                dfb.setPulse("00007530");
                dfb.update(superA);
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                String a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) 
                {
                }
                
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                    throw new TestException();

                 try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) 
                {
                }                 
         
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
   /*----------------------------------------------------------------------------
    testCase12
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: ON OFF Delay

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase12() throws TestException
    {
            Door door;
            String pin, policyId;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 12";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                                                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_IDENTITY);
                dfb.setGpio("00");
                dfb.update(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);

                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {                         
                }       
                                
//#
//# On Delay test
//#
                Logger.detail("------------ On Delay test ------------");                                                              
                
                dfb.setOnDelay("00007530");
                dfb.update(superA);
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                String a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) 
                {
                }
                
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                    throw new TestException();

                 try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) 
                {
                }
                 
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);                
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                                
  
                try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) 
                {
                }
//#
//# Off Delay test
//#
                Logger.detail("------------ Off Delay test ------------");                                                              
                
                dfb.setOnDelay("00000000");
                dfb.setOffDelay("00007530");
                dfb.update(superA);
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) 
                {
                }
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                    throw new TestException();

                 try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) 
                {
                }   
                 
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                                

                try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) 
                {
                }
//#
//# ON and OFF Delay test
//#
                Logger.detail("------------ On And Off Delay test ------------");                                                              
                
                dfb.setOnDelay("00007530");
                dfb.setOffDelay("00007530");
                dfb.update(superA);
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) 
                {
                }
                
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                

                try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) 
                {
                }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                    throw new TestException();

                 try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) 
                {
                }   
                 
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();

//#
//# ON and OFF Delay Disabled
//#
                Logger.detail("------------ On And Off Delay Disabled test ------------");                                                              
                
                dfb.setOnDelay("00000000");
                dfb.setOffDelay("00000000");
                dfb.update(superA);
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) 
                {
                }                 
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF); 
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
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

    DESCRIPTION: Three digital function blocks with a different out each.

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase13() throws TestException
    {            
            Door door, door1, door2, door3;
            String pin, policyId;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 13";               

            // ---------------------- Code -------------------------------            
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("07");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId("05");                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);

                    // create the door. Configure it.
                door2 = new Door(superA);                        
                door2.setGpioId("04");                      
                door2.setStatus(Door.SE_DOOR_ENABLED);
                door2.update(superA);
                door2.syncroFields(superA);

                                    // create the door. Configure it.
                door3 = new Door(superA);                        
                door3.setGpioId("06");                      
                door3.setStatus(Door.SE_DOOR_ENABLED);
                door3.update(superA);
                door3.syncroFields(superA);
                
//#
//# Test of the AND Digital function block (2 input)
//#
                Logger.detail("------------ Test of the AND Digital function block (2 input) ------------");
                Logger.detail("------------ Input 1 is a sensor; Input 2 is a door ------------");
                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setInputId(door1.getObjectId(), 1);
                dfb.setInputId(door2.getObjectId(), 2);
                dfb.setInputId(door3.getObjectId(), 3);
                dfb.setGpio("02");
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_AND_4_INPUT);
                dfb.update(superA);

                DigitalFunctionBlock dfb1 = new DigitalFunctionBlock(superA);
                dfb1.setInputId(door.getObjectId(), 0);
                dfb1.setInputId(door1.getObjectId(), 1);
                dfb1.setInputId(door2.getObjectId(), 2);
                dfb1.setInputId(door3.getObjectId(), 3);
                dfb1.setGpio("01");
                dfb1.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OR);
                dfb1.update(superA);
                
                DigitalFunctionBlock dfb2 = new DigitalFunctionBlock(superA);
                dfb2.setInputId(dfb1.getObjectId(), 0);
                dfb2.setInputId(dfb.getObjectId(), 1);
                dfb2.setGpio("00");
                dfb2.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_XOR);
                dfb2.update(superA);

                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                
                Sensor s2 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s2.setGpioId("01");
                s2.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s2.update(superA);
                
                Sensor s3 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s3.setGpioId("02");
                s3.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s3.update(superA);                
                                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                }                
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                String a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException(); 
                
                a = s2.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException();
                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                a = s2.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException();

                a = s3.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                      throw new TestException();                
                
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                
                a = s2.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException();

                a = s3.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                      throw new TestException();                           
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();

                a = s2.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException(); 

                a = s3.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException();                           
                
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }                              
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                    // after deletion of door object the output rise
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();

                a = s2.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException(); 

                a = s3.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                      throw new TestException();                 
                
                door1.delete(superA);
                    // after deletion of door1 object the output is high
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();

                a = s2.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException(); 

                a = s3.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                      throw new TestException();                 
                
                door2.delete(superA);
                    // after deletion of door2 object the output is high
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
                
                a = s2.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                      throw new TestException(); 

                a = s3.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                      throw new TestException(); 
                
                door3.delete(superA);
                    // after deletion of door3 object the output is low
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();

                a = s2.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                      throw new TestException(); 

                a = s3.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                      throw new TestException();                 
                
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                dfb1.delete(superA);
                dfb2.delete(superA);
                s1.delete(superA);
                s2.delete(superA);
                s3.delete(superA);
        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)       
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
   /*----------------------------------------------------------------------------
    testCase14
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Pulse Generator

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase14() throws TestException
    {
            Door door;
            String pin, policyId;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 14";               

            // ---------------------- Code -------------------------------            
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(dfb.getObjectId(), 0);
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_NOT);
                dfb.setOnDelay("00007530");
                dfb.setOffDelay("00007530");
                dfb.setGpio("00");
                dfb.update(superA);

                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
             /*   try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) 
                {                         
                }      */          
//#
//# Pulse generator with on delay and off delay
//#
                Logger.detail("------------ Pulse generator wirh on delay and off delay ------------");                                                              

                String a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
               try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) 
                {
                }
               
                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) 
                {
                }

                a = s1.getMeasure(superA);
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();
               
               try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) 
                {
                }                
               
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);
                
                thisDevice.systemReset(pin, superA, true);
        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
   /*----------------------------------------------------------------------------
    testCase15
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Update of input with a not allowed object

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase15() throws TestException
    {
            Door door;
            String pin, policyId, expectedRes, input;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Apdu apduObject;
            
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 15";               

            // ---------------------- Code -------------------------------            
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
               
//#
//# Super A try to update the input with a wrong value. First Input
//#
                Logger.detail("------------ Super A try to update the input with a wrong value. First Input ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AF9_INCORRECT_ID_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                

                input = superA.getObjectId();
                input += door.getObjectId();
                input += door.getObjectId();
                input += door.getObjectId();
                
                    // try to create a door
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_INPUT_OBJECT_ID_LIST, input.substring(0, DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER<<3));                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);
               
//#
//# Super A try to update the input with a wrong value. Second Input
//#
                Logger.detail("------------ Super A try to update the input with a wrong value. Second Input ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AF9_INCORRECT_ID_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                input = door.getObjectId();
                input += superA.getObjectId();
                input += door.getObjectId();
                input += door.getObjectId();
                
                    // try to create a door
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_INPUT_OBJECT_ID_LIST, input.substring(0, DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER<<3));                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);                
//#
//# Super A try to update the input with a wrong value. Third Input
//#
                Logger.detail("------------ Super A try to update the input with a wrong value. Third Input ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AF9_INCORRECT_ID_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                input = door.getObjectId();
                input += door.getObjectId();                
                input += superA.getObjectId();
                input += door.getObjectId();
                
                    // try to create a door
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_INPUT_OBJECT_ID_LIST, input.substring(0, DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER<<3));                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);
//#
//# Super A try to update the input with a wrong value. Fourth Input
//#
                Logger.detail("------------ Super A try to update the input with a wrong value. Fourth Input ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6AF9_INCORRECT_ID_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                input = door.getObjectId();
                input += door.getObjectId();
                input += door.getObjectId();                
                input += superA.getObjectId();
                
                    // try to create a door
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_INPUT_OBJECT_ID_LIST, input.substring(0, DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_INPUT_NUMBER<<3));                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);                 
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException  e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
  /*----------------------------------------------------------------------------
    testCase16
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Update of policy with a not allowed object

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase16() throws TestException
    {
            Door door;
            String pin, policyId, expectedRes;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Apdu apduObject;
            
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 16";               

            // ---------------------- Code -------------------------------

            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
               
//#
//# Super A try to update the policy with a door.
//#
                Logger.detail("------------ Super A try to update the policy with a door. ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6A89_INCORRECT_DATA_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                
                    // update with a wrong policy object Id
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_ACP, door.getObjectId());                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);
//#
//# Super A try to update the policy with a INVENTED VALUE.
//#
                Logger.detail("------------ Super A try to update the policy with a door. ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                
                    // update with a wrong policy object Id
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_ACP,"12345678");                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);                
               
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException  e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
    /*----------------------------------------------------------------------------
    testCase17
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Update of Reset time object with a wrong value

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase17() throws TestException
    {
            Door door;
            String pin, policyId, expectedRes;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Apdu apduObject;
            
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 17";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
               
//#
//# Super A try to update the policy with a door.
//#
                Logger.detail("------------ Super A try to update the policy with a door. ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6A89_INCORRECT_DATA_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                
                    // update with a wrong policy object Id
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_TIME_RESET_OBJECT_ID, user.getObjectId());                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);
//#
//# Super A try to update the policy with a INVENTED VALUE.
//#
                Logger.detail("------------ Super A try to update the policy with a door. ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                
                    // update with a wrong policy object Id
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_TIME_RESET_OBJECT_ID,"12345678");                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);                
               
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException  e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
    /*----------------------------------------------------------------------------
    testCase18
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Update of Log Event With a wrong value

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase18() throws TestException
    {
            Door door;
            String pin, policyId, expectedRes;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Apdu apduObject;
            
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 18";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
               
//#
//# Super A try to update the policy with a door.
//#
                Logger.detail("------------ Super A try to update the policy with a door. ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6A89_INCORRECT_DATA_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                
                    // update with a wrong policy object Id
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_LOG_EVENT, "03");                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);
//#
//# Super A try to update the policy with a INVENTED VALUE.
//#
                Logger.detail("------------ Super A try to update the policy with a door. ------------");                                                              
                                
                
                expectedRes = 	String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                 														// tag															// Object ID
                                        Apdu.SW_6A89_INCORRECT_DATA_TLV_STRING +
                                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";                                                
                
                    // update with a wrong policy object Id
                apduObject = new Apdu(Apdu.UPDATE_DIGITAL_FUNCTION_BLOCK_APDU );
                apduObject.addTlv(Atlv.DATA_TAG_OBJECT_ID, dfb.getObjectId());                
                apduObject.addTlv(Atlv.DATA_TAG_LOG_EVENT,"FE");                

                    // send command
                c.description = "Update Digital Function Block";
                c.requester = superA;
                c.execute(apduObject.toString(), expectedRes);                
               
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException  e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
   /*----------------------------------------------------------------------------
    testCase19
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: LOG Event Output set

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase19() throws TestException
    {
            Door door, door1, door2, door3;
            String pin, policyId;
            AccessPolicy defaultPolicy;
            int index, index1;
            LogEntry le;
            
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 19";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId(Door.SE_VIRTUAL_GPIO_0);                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);

                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId(Door.SE_VIRTUAL_GPIO_1);                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);
                
                                    // create the door. Configure it.
                door2 = new Door(superA);                        
                door2.setGpioId(Door.SE_VIRTUAL_GPIO_2);                      
                door2.setStatus(Door.SE_DOOR_ENABLED);
                door2.update(superA);
                door2.syncroFields(superA);

                    // create the door. Configure it.
                door3 = new Door(superA);                        
                door3.setGpioId(Door.SE_VIRTUAL_GPIO_3);                      
                door3.setStatus(Door.SE_DOOR_ENABLED);
                door3.update(superA);
                door3.syncroFields(superA);                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setInputId(door1.getObjectId(), 1);
                dfb.setInputId(door2.getObjectId(), 2);
                dfb.setInputId(door3.getObjectId(), 3);
                dfb.setGpio("02");
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OR);
                dfb.update(superA);
                
                thisDevice.systemReset(pin, superA, true);
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                } 

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF); 

                dfb.setLogEvent(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_LOG_WHEN_OUTPUT_SET);
                dfb.update(superA);                
//#
//# DFB Log on set is working fine input 0
//#
                Logger.detail("------------ DFB Log on set is working fine In 0 ------------");
                                                                 
                index = DeviceLogger.getLogCount(superA);

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                index1 = DeviceLogger.getLogCount(superA);
                
                    // Door switch and Digital function actions are both logged
                if ((index1-index)!= 2) {
                    throw new TestException();
                }               
                        // check that the log entry is empty
                le = DeviceLogger.getEntry(superA, index1-1);                        
                if ((0 != le.requesterId.compareTo(dfb.getObjectId())) || 
                        (0 != le.objectId.compareTo("00000001")) ||
                        (0 != DeviceLogger.SE_LOG_EVENT_DFB_ON.compareTo(le.event)) ||
                        ( 0 != Apdu.SW_9000_STRING.compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);                
//#
//# DFB Log on set is working fine input 1
//#
                Logger.detail("------------ DFB Log on set is working fine In 1 ------------");
                                
                index = DeviceLogger.getLogCount(superA);
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                index1 = DeviceLogger.getLogCount(superA);
                
                    // Door switch and Digital function actions are both logged
                if ((index1-index)!= 2) {
                    throw new TestException();
                }               

                le = DeviceLogger.getEntry(superA, index1-1);                        
                if ((0 != le.requesterId.compareTo(dfb.getObjectId())) || 
                        (0 != le.objectId.compareTo("00000002")) ||
                        (0 != DeviceLogger.SE_LOG_EVENT_DFB_ON.compareTo(le.event)) ||
                        ( 0 != Apdu.SW_9000_STRING.compareTo(le.result)))
                        {
                                throw new TestException();
                        } 
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);                 
//#
//# DFB Log on set is working fine input 2
//#
                Logger.detail("------------ DFB Log on set is working fine In 2 ------------");
                                
                index = DeviceLogger.getLogCount(superA);
                
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                index1 = DeviceLogger.getLogCount(superA);
                
                    // Door switch and Digital function actions are both logged
                if ((index1-index)!= 2) {
                    throw new TestException();
                }               
                le = DeviceLogger.getEntry(superA, index1-1);                        
                if ((0 != le.requesterId.compareTo(dfb.getObjectId())) || 
                        (0 != le.objectId.compareTo("00000004")) ||
                        (0 != DeviceLogger.SE_LOG_EVENT_DFB_ON.compareTo(le.event)) ||
                        ( 0 != Apdu.SW_9000_STRING.compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);                 
//#
//# DFB Log on set is working fine input 3
//#
                Logger.detail("------------ DFB Log on set is working fine in 3 ------------");
                                
                index = DeviceLogger.getLogCount(superA);
                
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                index1 = DeviceLogger.getLogCount(superA);
                
                    // Door switch and Digital function actions are both logged
                if ((index1-index)!= 2) {
                    throw new TestException();
                }               

                le = DeviceLogger.getEntry(superA, index1-1);                        
                if ((0 != le.requesterId.compareTo(dfb.getObjectId())) || 
                        (0 != le.objectId.compareTo("00000008")) ||
                        (0 != DeviceLogger.SE_LOG_EVENT_DFB_ON.compareTo(le.event)) ||
                        ( 0 != Apdu.SW_9000_STRING.compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);                 
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                door1.delete(superA);
                door2.delete(superA);
                door3.delete(superA);                
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
      /*----------------------------------------------------------------------------
    testCase20
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: LOG Event Output Reset

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase20() throws TestException
    {
            Door door, door1, door2, door3;
            String pin, policyId;
            AccessPolicy defaultPolicy;
            int index, index1;
            LogEntry le;
            
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 20";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId(Door.SE_VIRTUAL_GPIO_0);                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);

                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId(Door.SE_VIRTUAL_GPIO_1);                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);
                
                                    // create the door. Configure it.
                door2 = new Door(superA);                        
                door2.setGpioId(Door.SE_VIRTUAL_GPIO_2);                      
                door2.setStatus(Door.SE_DOOR_ENABLED);
                door2.update(superA);
                door2.syncroFields(superA);

                    // create the door. Configure it.
                door3 = new Door(superA);                        
                door3.setGpioId(Door.SE_VIRTUAL_GPIO_3);                      
                door3.setStatus(Door.SE_DOOR_ENABLED);
                door3.update(superA);
                door3.syncroFields(superA);                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setInputId(door1.getObjectId(), 1);
                dfb.setInputId(door2.getObjectId(), 2);
                dfb.setInputId(door3.getObjectId(), 3);
                dfb.setGpio("02");
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_AND_4_INPUT);
                dfb.update(superA);
                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                } 

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON); 

                dfb.setLogEvent(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_LOG_WHEN_OUTPUT_RESET);
                dfb.update(superA);                
//#
//# DFB Log on Reset is working fine input 0
//#
                Logger.detail("------------ DFB Log on reset is working fine In 0 ------------");
                                                                 
                index = DeviceLogger.getLogCount(superA);
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                index1 = DeviceLogger.getLogCount(superA);
                
                    // only one power on shall be logged
                if ((index1-index)!= 2) {
                    throw new TestException();
                }               
                        // check that the log entry is empty

                le = DeviceLogger.getEntry(superA, index1-1);                        
                if ((0 != le.requesterId.compareTo(dfb.getObjectId())) || 
                        (0 != le.objectId.compareTo("0000000E")) ||
                        (0 != DeviceLogger.SE_LOG_EVENT_DFB_OFF.compareTo(le.event)) ||
                        ( 0 != Apdu.SW_9000_STRING.compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);                
//#
//# DFB Log on Reset is working fine input 1
//#
                Logger.detail("------------ DFB Log on reset is working fine In 1 ------------");
                                
                index = DeviceLogger.getLogCount(superA);
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                index1 = DeviceLogger.getLogCount(superA);
                
                    // only one power on shall be logged
                if ((index1-index)!= 2) {
                    throw new TestException();
                }               
                        // check that the log entry is empty

                le = DeviceLogger.getEntry(superA, index1-1);                        
                if ((0 != le.requesterId.compareTo(dfb.getObjectId())) || 
                        (0 != le.objectId.compareTo("0000000D")) ||
                        (0 != DeviceLogger.SE_LOG_EVENT_DFB_OFF.compareTo(le.event)) ||
                        ( 0 != Apdu.SW_9000_STRING.compareTo(le.result)))
                        {
                                throw new TestException();
                        } 
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);                 
//#
//# DFB Log on reset is working fine input 2
//#
                Logger.detail("------------ DFB Log on reset is working fine In 2 ------------");
                                                                  
                index = DeviceLogger.getLogCount(superA);
                
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                index1 = DeviceLogger.getLogCount(superA);
                
                    // only one power on shall be logged
                if ((index1-index)!= 2) {
                    throw new TestException();
                }               
                        // check that the log entry is empty

                le = DeviceLogger.getEntry(superA, index1-1);                        
                if ((0 != le.requesterId.compareTo(dfb.getObjectId())) || 
                        (0 != le.objectId.compareTo("0000000B")) ||
                        (0 != DeviceLogger.SE_LOG_EVENT_DFB_OFF.compareTo(le.event)) ||
                        ( 0 != Apdu.SW_9000_STRING.compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                
                door2.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);                 
//#
//# DFB Log on reset is working fine input 3
//#
                Logger.detail("------------ DFB Log on reset is working fine in 3 ------------");
                                
                index = DeviceLogger.getLogCount(superA);
                
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                index1 = DeviceLogger.getLogCount(superA);
                
                    // only one power on shall be logged
                if ((index1-index)!= 2) {
                    throw new TestException();
                }               
                        // check that the log entry is empty

                le = DeviceLogger.getEntry(superA, index1-1);                        
                if ((0 != le.requesterId.compareTo(dfb.getObjectId())) || 
                        (0 != le.objectId.compareTo("00000007")) ||
                        (0 != DeviceLogger.SE_LOG_EVENT_DFB_OFF.compareTo(le.event)) ||
                        ( 0 != Apdu.SW_9000_STRING.compareTo(le.result)))
                        {
                                throw new TestException();
                        }
                door3.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);                 
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                door1.delete(superA);
                door2.delete(superA);
                door3.delete(superA);                
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
 /*----------------------------------------------------------------------------
    testCase21
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Reset Digital Function Block and getOutput

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase21() throws TestException
    {
            Door door, door1;
            String pin, policyId, a;
            AccessPolicy defaultPolicy;
            int index, index1;
            LogEntry le;
            
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 21";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId(Door.SE_VIRTUAL_GPIO_0);                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);

                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId(Door.SE_VIRTUAL_GPIO_1);                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setResetObjectId(door1.getObjectId());
                dfb.setGpio("02");
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_IDENTITY);
                dfb.update(superA);
                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                } 
               
//#
//# DFB Resert off Delay
//#
                Logger.detail("------------ Reset off delay ------------");
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                    // set off Delay
                dfb.setOffDelay(30000);
                dfb.update(superA);                                                
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                    // turn on the reset.
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                    // check that output is high
                a = dfb.getOutput(superA);                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_HIGH))
                    throw new TestException();
                
                   // turn off the reset
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);                
                    // check that output is high
                a = dfb.getOutput(superA);                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_HIGH))
                    throw new TestException();
                    
                    // turn on the reset
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                    // check that output is low
                a = dfb.getOutput(superA);                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_LOW))
                    throw new TestException();
                
                    // turn on the reset.
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);                
                
                     // check that output is low
                a = dfb.getOutput(superA);                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_LOW))
                    throw new TestException();
//#
//# DFB Resert on Delay
//#
                Logger.detail("------------ Reset on delay ------------");
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                    // set on Delay
                dfb.setOnDelay(0);
                dfb.setOnDelay(30000);
                dfb.update(superA);                                                
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                               
                    // check that output is low
                a = dfb.getOutput(superA);                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_LOW))
                    throw new TestException();
                
                   // turn on the reset
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                    // check that output is high
                a = dfb.getOutput(superA);                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_HIGH))
                    throw new TestException();

                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                    // turn on the reset
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                    // check that output is high
                a = dfb.getOutput(superA);                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_HIGH))
                    throw new TestException();

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                door1.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;                
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
/*----------------------------------------------------------------------------
    testCase21
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Reset Digital Function Block and getOutput

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase22() throws TestException
    {
            Door door, door1;
            String pin, policyId, a;
            AccessPolicy defaultPolicy;
            
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 22";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId(Door.SE_VIRTUAL_GPIO_0);                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);

                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId(Door.SE_VIRTUAL_GPIO_1);                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setResetObjectId(door1.getObjectId());
                dfb.setGpio("02");
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_IDENTITY);
                dfb.update(superA);
                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                } 
               
//#
//# DFB Resert off Delay
//#
                Logger.detail("------------ Reset off delay ------------");
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                    // set off Delay
                dfb.setOffDelay(30000);
                dfb.update(superA);                                                
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                    // turn on the reset.
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                
                    // get output
                dfb.syncroFields(superA);                    
                a = dfb.getLastOutputValue();                
                
                    // check that output is high
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_HIGH))
                    throw new TestException();
                
                   // turn off the reset
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);                

                    // get output
                dfb.syncroFields(superA);                    
                a = dfb.getLastOutputValue();  

                    // check that output is high                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_HIGH))
                    throw new TestException();
                    
                    // turn on the reset
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                    // get output
                dfb.syncroFields(superA);                    
                a = dfb.getLastOutputValue();
                
                    // check that output is low                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_LOW))
                    throw new TestException();
                
                    // turn on the reset.
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);                
                
                     // check that output is low
                a = dfb.getOutput(superA);                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_LOW))
                    throw new TestException();
//#
//# DFB Resert on Delay
//#
                Logger.detail("------------ Reset on delay ------------");
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                    // set on Delay
                dfb.setOnDelay(0);
                dfb.setOnDelay(30000);
                dfb.update(superA);                                                
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                                    // get output
                dfb.syncroFields(superA);                    
                a = dfb.getLastOutputValue();
                
                    // check that output is low                
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_LOW))
                    throw new TestException();
                
                   // turn on the reset
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                    // get output
                dfb.syncroFields(superA);                    
                a = dfb.getLastOutputValue();
                
                    // check that output is high               
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_HIGH))
                    throw new TestException();

                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                
                    // turn on the reset
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);

                    // get output
                dfb.syncroFields(superA);                    
                a = dfb.getLastOutputValue();
                
                    // check that output is high               
                if (0 != a.compareTo(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_OUT_HIGH))
                    throw new TestException();

                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                door1.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;                
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }
    /*----------------------------------------------------------------------------
    testCase23
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Test the logical and with two input I1 and I2. THe two input are from a door; 
                Syncro is used instead of get measure;

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static void testCase23() throws TestException
    {
            Door door, door1;
            String pin, policyId;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case 23";               

            // ---------------------- Code -------------------------------
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                
                    // create the door. Configure it.
                door1 = new Door(superA);                        
                door1.setGpioId("04");                      
                door1.setStatus(Door.SE_DOOR_ENABLED);
                door1.update(superA);
                door1.syncroFields(superA);

                Sensor s = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s.setGpioId("05");
                s.update(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
//#
//# Test of the AND Digital function block (2 input)
//#
                Logger.detail("------------ Test of the AND Digital function block (2 input) ------------");
                Logger.detail("------------ Input 1 is a sensor; Input 2 is a door ------------");
                
                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(s.getObjectId(), 0);
                dfb.setInputId(door1.getObjectId(), 1);
                dfb.setGpio("00");
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_AND_2_INPUT);
                dfb.update(superA);
                                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                s1.syncroFields(superA);
                String a = s1.getLastMeasure();
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                s1.syncroFields(superA);
                a = s1.getLastMeasure();
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_ON))
                        throw new TestException();                

                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                
                door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                s1.syncroFields(superA);
                a = s1.getLastMeasure();
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();                
                    // wait;
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }
                door1.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                s1.syncroFields(superA);
                a = s1.getLastMeasure();
                if (0 != a.compareTo( Sensor.SENSOR_VALUE_OFF))
                        throw new TestException();
                
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) 
                {
                }               
                
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                door1.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s.delete(superA);
                s1.delete(superA);

        }
        catch (CommandErrorException | ObjectException | IOException | TestException e)
        {
            Logger.testResult(false);
            TestException t = new TestException();
            throw t;               
        }

        Logger.testCase(testCode);
        Logger.testResult(true);
    }    
   /*----------------------------------------------------------------------------
    oscilloscopeTest
    --------------------------------------------------------------------------
    AUTHOR:	PDI

    DESCRIPTION: Oscilloscope test

    Security Level: None

    ------------------------------------------------------------------------------*/
    public static boolean oscilloscopeTest()
    {
            boolean result;
            Door door;
            String pin, policyId;
            AccessPolicy ap;
            AccessPolicy defaultPolicy;
            Command c = new Command();                
            String testCode = testBatch+"/"+"Test Case oscilloscope";               

            // ---------------------- Code -------------------------------
            result = true;
            try
            {

                Logger.testCase(testCode);

                pin = "01020304";                
                    // launch a ping
                thisDevice.ping();

                        // instantiate a local User object for the SUPER-A
                //User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                superA.setPin(superA, pin);

                        // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN, "pussy");
                admin.updateKey("newAdministratorKeysCiccia");
                admin.syncroFields(admin);
                policyId = admin.getAcPolicy();               
                
                        // object created
                User user = new User(admin, User.USER_ROLE_USER, "rigqa");
                user.updateKey("newSutta");
                user.syncroFields(user);
                
                defaultPolicy = new AccessPolicy(admin, policyId);
                defaultPolicy.setAlwaysWeeklyPolicy();
                defaultPolicy.update(superA);

                    // create the door. Configure it.
                door = new Door(superA);                        
                door.setGpioId("05");                      
                door.setStatus(Door.SE_DOOR_ENABLED);
                door.update(superA);
                door.syncroFields(superA);
                                                                
                DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
                dfb.setInputId(door.getObjectId(), 0);
                dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_IDENTITY);
                dfb.setGpio("00");
                dfb.update(superA);
                
                Sensor s1 = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true);
                s1.setGpioId("00");
                s1.setStatus(Sensor.SENSOR_STATUS_ENABLED);
                s1.update(superA);
                
                thisDevice.systemReset(pin, superA, true);
                
                    // wait;
                try{
                    Thread.sleep(4000);
                } catch (InterruptedException e) 
                {
                }                
                                
//#
//# Delay time.
//#
                Logger.detail("------------ Measure of the delay time ------------");                                                              
                for (int i=0; i<50; i++)
                {
                    door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
                    try{
                        Thread.sleep(500);
                    } catch (InterruptedException e) 
                    {
                    }                
                    door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF);
                    try{
                        Thread.sleep(500);
                    } catch (InterruptedException e) 
                    {
                    }                
                }                
//#
//# Object deletion
//#                       
                admin.delete(superA);
                user.delete(superA);
                door.delete(superA);
                superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);                        
                dfb.delete(superA);
                s1.delete(superA);

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
