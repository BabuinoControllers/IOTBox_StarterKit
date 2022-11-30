/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainTest;
import TestCases.*;

/**
 *
 * @author g508853
 */
public class TestMain {
    
    // IMPORTANT
    // Copy serial number of your device here
    public static String deviceId = "000000000000100E";
   
    public static void main(String[] args)
    {  
       boolean result;
       
      
       /*result = TestHelloWord001.run();
       if (!result){
          System.exit(1);
        }*/
       
       result = TestDemoNightLight001.run();
       if (!result){
          System.exit(1);
        }
    
       // Uncomment to see other examples
       
  /*     result = TestUser001.run();
       if (!result){
          System.exit(1);
        }
       
        result = TestUser002.run();
       if (!result){
          System.exit(1);
        }
              
       result = TestAccessPolicy001.run();
        if (!result){
          System.exit(1);
        }
              
       result = TestAccessPolicy002.run();  
       if (!result){
          System.exit(1);
        }
            
       result = TestLao001.run();
       if (!result){
          System.exit(1);
        }       
              
       result = TestLog001.run();                             
       if (!result){
          System.exit(1);
        }
       
       result = TestDoor001.run();
       if (!result){
          System.exit(1);
        }
       
       result = TestSensors001.run();
      if (!result){
          System.exit(1);
        }
        
       result = TestDoorWithProximity001.run();
       if (!result){
          System.exit(1);
        }
  
       result = TestPutDataOutput001.run();
       if (!result){
          System.exit(1);
        }
  
       result = TestGetDataOutput001.run();       
       if (!result){
          System.exit(1);
        }
  
     result = TestSystemReset001.run();
       if (!result){
          System.exit(1);
        } 
  
       result = TestTime001.run();
       if (!result){
          System.exit(1);
        }
       
        result = TestKeyCounter001.run();
        if (!result){
          System.exit(1);
        }
  
        result = TestDiscovery001.run();
        if (!result){
          System.exit(1);
        }   
        
            // MQTT test repeated three times.
        for (int i = 0; i<3; i++){
            result = TestUserMqtt001.run();
            if (!result){
              System.exit(1);
            }
        }
        
        result = TestDevice001.run();
        if (!result){
          System.exit(1);
        }
           
        result = TestDigitalFunctionBlock001.run();
        if (!result){
          System.exit(1);
        } 
        
       result = TestFiniteStateMachine001.run();
        if (!result){
          System.exit(1);
        }     */
       
                      
      
    /* result =TestExtension001.run();
        if (!result){
          System.exit(1);
        }
              
        result =TestExtenderWithCentralizedControl001.run();
        if (!result){
          System.exit(1);
        }
                      
        result = TestBeacon001.run();       
        if (!result){
           System.exit(1);
         }
        */
   
    
    }
    
}
