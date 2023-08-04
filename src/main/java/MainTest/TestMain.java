/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainTest;

import TestCases.*;

/**
 * @author g508853
 */
public class TestMain {

    // IMPORTANT
    // Copy serial number of your device here
    public static String deviceId = "000000000000100A";

    public static void main(String[] args) {

        new TestHelloWord001().run();


        // Uncomment to run other examples

//        new TestDemoNightLight001().run();
//
//        new TestUser001().run();
//
//        new TestUser002().run();
//
//        new TestAccessPolicy001().run();
//
//        new TestAccessPolicy002().run();
//
//        new TestLao001().run();
//
//        new TestLog001().run();
//
//        new TestDoor001().run();
//
//        new TestSensors001().run();
//
//        new TestDoorWithProximity001().run();
//
//        new TestPutDataOutput001().run();
//
//        new TestGetDataOutput001().run();
//
//        new TestSystemReset001().run();
//
//        new TestTime001().run();
//
//        new TestKeyCounter001().run();
//
//        new TestDiscovery001().run();
//
//        // MQTT test repeated three times.
//        for (int i = 0; i < 3; i++) {
//            new TestUserMqtt001().run();
//        }
//
//        new TestDevice001().run();
//
//        new TestDigitalFunctionBlock001().run();
//
//        new TestFiniteStateMachine001().run();
//
//        new TestBeacon001().run();


    }

}
