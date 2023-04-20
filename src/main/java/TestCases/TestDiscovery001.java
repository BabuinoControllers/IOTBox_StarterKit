package TestCases;
import com.sdk.*;


import java.io.IOException;

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import com.testlog.*;

@SuppressWarnings("unused")
public class TestDiscovery001 {

//    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestDiscovery001";

//    /********************************
//     PUBLIC Methods
//     ********************************/
    public static User superA;
    public static Device thisDevice;
    public static final String deviceId = MainTest.TestMain.deviceId;

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
            thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

            thisUnit.setDevice(thisDevice);

            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);

            testCase01(); // ETHERNET
            j++;

            //testCase02(); // WIFI
            j++;

            testCase03(); // ETHERNET
            j++;

            // testCase04(); // WIFI
            j++;

            //testCase05(); //WIFI
            j++;

            testCase06(); //ETHERNET
            j++;


            testCase01();
            j++;

        } catch (TestException | DiscoveryException e) {
            thisUnit.testCompleted(false, "failure at test case " + j);

            Logger.detail("TEST FAILURE ----->" + j);
            Assertions.fail("TEST FAILURE ----->" + j);
            //return false;
        }
        thisUnit.testCompleted(true, "success!");

        Logger.detail("OK");
        Logger.detail("OK");
    }

    /*----------------------------------------------------------------------------
testCase01
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Tests the discovery request on Ethernet;

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {


        String testCase = testBatch + "/" + "Test Case 01";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            //#
            //# Send a discovery request over Ethernet.
            //# The device replies providing the IP and port number of ethernet connection.
            //# After discovery it is possible to send commands to the card via ethernet.
            //#
            Logger.testCase("Test Discovery request over the ethernet");

            try {
                Device d = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

                superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, d);
            } catch (DiscoveryException ignored) {
            }

            // launch a ping
            thisDevice.ping();

            // Super A object personalization
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            // object created
            User admin = new User(superA, User.USER_ROLE_ADMIN, "initialAdmin");
            // object personalized
            //for(int i=0; i<1000; i++)
            admin.updateKey("admin");

            admin.syncroFields(admin);

            //#
            //# Object deletion
            //#
            admin.delete(superA);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
 testCase02
 --------------------------------------------------------------------------
 AUTHOR:	PDI

     DESCRIPTION: Check of discovery message information

 Security Level: None

 ------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {

 /*           String testCase = testBatch+"/"+"Test Case 02";
		
	// ---------------------- Code -------------------------------
            try
            {
                Logger.testCase(testCase);
						               
	//#
	//# Send a discovery request for Ethernet. 
	//# The discovery message return all the expected information
        //# 
                Logger.testCase("Test Discovery request over the ethernet");
                
                try {
                    Device d = Device.discover(deviceId, Device.WIFI, 3, 2000);
                    
                        // launch a ping
                    d.ping();                    
                }
                catch (DiscoveryException e)
                {                    
                }
                                 
                    // Super A object personalization
		superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                
                    // object created
                User admin = new User(superA, User.USER_ROLE_ADMIN,"initialAdmin");	
                        // object personalized
                //for(int i=0; i<1000; i++)                
                admin.updateKey("admin");

                admin.syncroFields(admin);
                        			
        //#
	//# Object deletion
	//#                       
                        admin.delete(superA);
                        superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

		}
		catch (CommandErrorException | ObjectException | IOException e)
		{
                    Logger.testResult(false);		
                    throw new TestException();
		}
		
		Logger.testCase(testCase);
		Logger.testResult(true);*/
    }

    /*----------------------------------------------------------------------------
testCase03
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Tests that discovery message report the correct information;

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        DatagramSocket socket;
        byte[] tx, rx;
        BerTlv discovery;
        InetAddress ip;
        Atlv out;
        String testCase = testBatch + "/" + "Test Case 03";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            //#
            //# Send a discovery request for Ethernet.
            //# The device replies with the discovery message
            //# items in the discovery message are checked
            //#
            Logger.testCase("Test Discovery request over the ethernet: Items in discovery message are as expected");


            // ---------------------- Code -------------------------------

            tx = new byte[256];
            rx = new byte[256];

            discovery = new BerTlv(Atlv.DATA_TAG_SYSTEM_PING);
            discovery.addTlv(Atlv.DATA_TAG_APDU_COMMAND, Apdu.PING_APDU);
            discovery.addTlv(Atlv.DATA_TAG_DEVICE_ID, deviceId);
            discovery.addTlv(Atlv.DATA_TAG_BEARER, ConnectionDetails.BEARER_ETHERNET);
            discovery.toArray(tx, 0);

            // broadcast ip address
            ip = InetAddress.getByName("255.255.255.255");

            Logger.response(discovery.toString(), "Discovery Request");
            socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(tx, tx.length, ip, IoStream.DEFAULT_ETHERNET_UDP_PORT);

            socket.send(packet);

            // add a timeout
            socket.setSoTimeout(3000);


            packet = new DatagramPacket(rx, 256);
            socket.receive(packet);

            discovery = new BerTlv(rx, 0, Atlv.getLen(rx, 0));
            Logger.response(discovery.toString(), "Discovery Response");

            // host Name
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_IP_ADDRESS);
            if (null == out) {
                throw new TestException();
            }
            // port number
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_PORT_NUM);
            if (null == out) {
                throw new TestException();
            }

            // Device Identifier
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_DEVICE_ID);
            if (null == out) {
                throw new TestException();
            }

            // Name
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_NAME);
            if (null == out) {
                throw new TestException();
            }

            // Time
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_SYSTEM_TIME);
            if (null == out) {
                throw new TestException();
            }

            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_BEARER);
            if (null == out) {
                throw new TestException();
            }

            // DATA_TAG_HW_VER
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_HW_VER);
            if (null == out) {
                throw new TestException();
            }

            // DATA_TAG_OS_VER
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_HW_VER);
            if (null == out) {
                throw new TestException();
            }


            //#
            //# Object deletion
            //#

        } catch (IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase04
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Tests that Wifi discovery message over Ethernet reports the correct information;

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
/*            DatagramSocket socket;
            byte[] tx, rx;
            BerTlv discovery;
            InetAddress ip;
            Atlv out;    
            String testCase = testBatch+"/"+"Test Case 04";
		
	// ---------------------- Code -------------------------------
            try
            {
                Logger.testCase(testCase);
						               
	//#
	//# Send a discovery request for Wifi. 
        //# The device replies with the discovery message
        //# items in the discovery message are checked
        //#         
                Logger.testCase("Test Discovery request over wifi: Items in discovery message are as expected");
                

        // ---------------------- Code -------------------------------
        
                tx = new byte[256];
                rx = new byte[256];

                discovery = new BerTlv(Atlv.DATA_TAG_SYSTEM_PING);
                discovery.addTlv(Atlv.DATA_TAG_APDU_COMMAND, Apdu.PING_APDU);
                discovery.addTlv(Atlv.DATA_TAG_DEVICE_ID, deviceId);                        
                discovery.addTlv(Atlv.DATA_TAG_BEARER, Device.WIFI);
                discovery.toArray(tx, 0);

                    // broadcast ip address
                ip = InetAddress.getByName("255.255.255.255");

                Logger.response(discovery.toString(), "Discovery Request");
                socket = new DatagramSocket();            
                DatagramPacket packet = new DatagramPacket(tx, tx.length, ip, IoStream.DEFAULT_UDP_PORT);

                socket.send(packet);

                    // add a timeout
                socket.setSoTimeout(3000);
            

                packet = new DatagramPacket(rx, 256);
                socket.receive(packet);
        
                discovery = new BerTlv(rx, 0, Atlv.getLen(rx, 0));
                Logger.response(discovery.toString(), "Discovery Response");

                    // host Name
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_IP_ADDRESS);        
                if(null == out)
                {
                    throw new TestException();
                }   
                    // port number
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_PORT_NUM);        
                if(null == out)
                {
                    throw new TestException();
                }

                    // Device Identifier
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_DEVICE_ID);        
                if(null == out)
                {
                    throw new TestException();
                }

                    // Name
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_DEVICE_NICK);        
                if(null == out)
                {
                    throw new TestException();
                }
                
                    // Time                    
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_SYSTEM_TIME);        
                if(null == out)
                {
                    throw new TestException();
                }

                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_BEARER);        
                if(null == out)
                {
                    throw new TestException();
                }
                
                    // DATA_TAG_HW_VER                    
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_HW_VER);        
                if(null == out)
                {
                    throw new TestException();
                }

                    // DATA_TAG_OS_VER                    
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_HW_VER);        
                if(null == out)
                {
                    throw new TestException();
                }                     
                
                        			
        //#
	//# Object deletion
	//#                       

		}
		catch ( IOException | TestException e)
		{
                    Logger.testResult(false);		
                    throw new TestException();
		}
		
		Logger.testCase(testCase);
		Logger.testResult(true);*/
    }

    /*----------------------------------------------------------------------------
 testCase05
 --------------------------------------------------------------------------
 AUTHOR:	PDI

 DESCRIPTION: Tests that Wifi discovery message report the correct information;

 Security Level: None

 ------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
  /*          DatagramSocket socket;
            byte[] tx, rx;
            BerTlv discovery;
            InetAddress ip;
            Atlv out;    
            String testCase = testBatch+"/"+"Test Case 05";
		
	// ---------------------- Code -------------------------------
            try
            {
                Logger.testCase(testCase);
						               
	//#
	//# Send a discovery request for Wifi. 
        //# The device replies with the discovery message
        //# items in the discovery message are checked
        //#         
                Logger.testCase("Test Discovery request over wifi: Items in discovery message are as expected");
                

        // ---------------------- Code -------------------------------
        
                tx = new byte[256];
                rx = new byte[256];

                discovery = new BerTlv(Atlv.DATA_TAG_SYSTEM_PING);
                discovery.addTlv(Atlv.DATA_TAG_APDU_COMMAND, Apdu.PING_APDU);
                //discovery.addTlv(Atlv.DATA_TAG_DEVICE_ID, deviceId);                        
                discovery.addTlv(Atlv.DATA_TAG_BEARER, Device.WIFI);
                discovery.toArray(tx, 0);

                    // broadcast ip address
                ip = InetAddress.getByName("255.255.255.255");

                discovery.toString();
                Logger.response(discovery.toString(), "Discovery Request");
                socket = new DatagramSocket();            
                DatagramPacket packet = new DatagramPacket(tx, tx.length, ip, IoStream.DEFAULT_ETHERNET_UDP_PORT);

                socket.send(packet);

                    // add a timeout
                socket.setSoTimeout(3000);
            

                packet = new DatagramPacket(rx, 256);
                socket.receive(packet);
        
                discovery = new BerTlv(rx, 0, Atlv.getLen(rx, 0));
                Logger.response(discovery.toString(), "Discovery Response");

                    // host Name
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_IP_ADDRESS);        
                if(null == out)
                {
                    throw new TestException();
                }   
                    // port number
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_PORT_NUM);        
                if(null == out)
                {
                    throw new TestException();
                }

                    // Device Identifier
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_DEVICE_ID);        
                if(null == out)
                {
                    throw new TestException();
                }

                    // Name
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_NAME);        
                if(null == out)
                {
                    throw new TestException();
                }
                
                    // Time                    
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_SYSTEM_TIME);        
                if(null == out)
                {
                    throw new TestException();
                }

                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_BEARER);        
                if(null == out)
                {
                    throw new TestException();
                }

                    // DATA_TAG_HW_VER                    
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_HW_VER);        
                if(null == out)
                {
                    throw new TestException();
                }

                    // DATA_TAG_OS_VER                    
                out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_HW_VER);        
                if(null == out)
                {
                    throw new TestException();
                }                     
                
                        			
        //#
	//# Object deletion
	//#                       

		}
		catch ( IOException | TestException e)
		{
                    Logger.testResult(false);		
                    throw new TestException();
		}
		
		Logger.testCase(testCase);
		Logger.testResult(true);*/
    }

    /*----------------------------------------------------------------------------
 testCase05
 --------------------------------------------------------------------------
 AUTHOR:	PDI

 DESCRIPTION: Tests that Ethernet discovery message report the correct information;

 Security Level: None

 ------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException {
        DatagramSocket socket;
        byte[] tx, rx;
        BerTlv discovery;
        InetAddress ip;
        Atlv out;
        String testCase = testBatch + "/" + "Test Case 06";

        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        tc.setCaseTitle(testCase);
        TestEventHandler.getInstance().subscribeAlone(tc);

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            //#
            //# Send a discovery request for Ethernet.
            //# The device replies with the discovery message
            //# items in the discovery message are checked
            //#
            Logger.testCase("Test Discovery request over Ethernet: Items in discovery message are as expected");


            // ---------------------- Code -------------------------------

            tx = new byte[256];
            rx = new byte[256];

            discovery = new BerTlv(Atlv.DATA_TAG_SYSTEM_PING);
            discovery.addTlv(Atlv.DATA_TAG_APDU_COMMAND, Apdu.PING_APDU);
            discovery.addTlv(Atlv.DATA_TAG_DEVICE_ID, deviceId);
            discovery.addTlv(Atlv.DATA_TAG_BEARER, ConnectionDetails.BEARER_ETHERNET);
            discovery.toArray(tx, 0);

            // broadcast ip address
            ip = InetAddress.getByName("255.255.255.255");

            Logger.response(discovery.toString(), "Discovery Request");
            socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(tx, tx.length, ip, IoStream.DEFAULT_ETHERNET_UDP_PORT);

            socket.send(packet);

            // add a timeout
            socket.setSoTimeout(3000);


            packet = new DatagramPacket(rx, 256);
            socket.receive(packet);

            discovery = new BerTlv(rx, 0, Atlv.getLen(rx, 0));
            Logger.response(discovery.toString(), "Discovery Response");

            // host Name
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_IP_ADDRESS);
            if (null == out) {
                Logger.detail("Missing IP address");
                throw new TestException();
            }
            // port number
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_PORT_NUM);
            if (null == out) {
                Logger.detail("Missing port Number");
                throw new TestException();
            }

            // Device Identifier
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_DEVICE_ID);
            if (null == out) {
                Logger.detail("Missing Device ID");
                throw new TestException();
            }

            // Name
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_NAME);
            if (null == out) {
                Logger.detail("Missing Name");
                throw new TestException();
            }

            // Time
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_SYSTEM_TIME);
            if (null == out) {
                Logger.detail("Missing System Time");
                throw new TestException();
            }

            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_BEARER);
            if (null == out) {
                Logger.detail("Missing Bearer");
                throw new TestException();
            }


            // DATA_TAG_HW_VER
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_HW_VER);
            if (null == out) {
                Logger.detail("Missing HW Ver");
                throw new TestException();
            }

            // DATA_TAG_OS_VER
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_OS_VER);
            if (null == out) {
                Logger.detail("Missing OS Ver");
                throw new TestException();
            }
            // DATA_TAG_APP_VER
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_APP_VER);
            if (null == out) {
                Logger.detail("Missing APP Ver");
                throw new TestException();
            }

            // DATA_TAG_APP_PACK
            out = discovery.lookUpFirstTlvOccurrence(Atlv.DATA_TAG_APP_PACK);
            if (null == out) {
                Logger.detail("Missing APP Pack");
                throw new TestException();
            }


            //#
            //# Object deletion
            //#

        } catch (IOException | TestException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");

            throw new TestException();
        }
        tc.testCompleted(true, "success");

        Logger.testCase(testCase);
        Logger.testResult(true);
    }
}

