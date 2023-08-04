package TestCases;
import com.sdk.*;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.testlog.TestCase;
import com.testlog.TestEventHandler;
import com.testlog.TestUnit;
import com.testlog.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;


@SuppressWarnings("unused")
public class TestAccessPolicy001 {

    //    /********************************
//     PUBLIC Fields
//     ********************************/
    public static final String testBatch = "TestAccessPolicy001";
    public static final String deviceId = MainTest.TestMain.deviceId;
    public static final String pinTest = "31323334";
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
        superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);

        try {
            thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);

            thisUnit.setDevice(thisDevice);

            superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
            superA.syncroFields(superA);
            superA.setPin(superA, pinTest);

            for (int u = 0; u < 1; u++) {
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

                // it reset the PIN of Super A
                superA.resetPin(superA);
            }
        } catch (CommandErrorException | TestException | ObjectException | IOException | DiscoveryException e) {
            Logger.detail("TEST FAILURE ----->" + j);
            Assertions.fail("TEST FAILURE ----->" + j);
            thisUnit.testCompleted(false, "failure at test case " + j);
            //return false;
        }
        thisUnit.testCompleted(true, "success!");
        Logger.detail("OK");
    }

    /*----------------------------------------------------------------------------
testCase01
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Access Policy Creation. Super A can create an access policy.
                Initial values are as expected (Never Policy).

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase01() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        AccessPolicy ap;
        String a, name;
        DailyPolicy dailyPolicy;

        String testCase = testBatch + " /" + "Test Case 01";

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
                        
/*################################################################################################
                Super A can create an access policy                 
  ################################################################################################*/
            ap = new AccessPolicy(superA);
                    
/*################################################################################################
                The initial value are the one expected                 
  ################################################################################################*/

            // The Name is the initial name
            Logger.detail("------------ Check that name is the default one ------------");
            a = ap.getName();
            name = ap.getObjectId();

            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            // Security Domain is the initial security domain
            Logger.detail("------------ Check Security Domain ------------");
            a = ap.getSecurityDomain();
            if (0 != a.compareTo(SecurityDomain.INITIAL_SD_ID))
                throw new ObjectException();

            // Starting Date is set at 000101
            Logger.detail("------------ Check Starting Date ------------");
            a = ap.getStartingDate();
            if (0 != a.compareTo("000101"))
                throw new ObjectException();


            // Expiration Date is set at 500101
            Logger.detail("------------ Expiration Date ------------");
            a = ap.getExpirationgDate();
            if (0 != a.compareTo("500101"))
                throw new ObjectException();

            // check the weekly policy;
            Logger.detail("------------ Weekly Policy ------------");

            for (int i = 0; i < 7; i++) {
                dailyPolicy = ap.getDailyPolicy(i);

                for (int j = 0; j < 3; j++) {

                    // check the start time
                    a = dailyPolicy.getStartTime(j);
                    if (0 != a.compareTo("0000"))
                        throw new ObjectException();

                    // check the end time
                    a = dailyPolicy.getEndTime(j);
                    if (0 != a.compareTo("0000"))
                        throw new ObjectException();
                }
            }
                               
  /*################################################################################################
                Super A can delete an access policy                 
  ################################################################################################*/
            ap.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "fail");
            throw new TestException();
        }
        Logger.testCase(testCase);
        Logger.testResult(true);
        tc.testCompleted(true, "success");
    }

    /*----------------------------------------------------------------------------
testCase02
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Access Policy Creation. Adminr can create an access policy.
                Initial values are as expected (Never Policy).

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase02() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command c = new Command();
        AccessPolicy ap;
        String a, name, expectedRes;
        DailyPolicy dailyPolicy;
        User admin, user;
        Apdu apduObject;

        String testCase = testBatch + " /" + "Test Case 02";

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

/*################################################################################################
                admin can create an access policy                 
  ################################################################################################*/
            ap = new AccessPolicy(admin);
                    
/*################################################################################################
                The initial value are the one expected                 
  ################################################################################################*/

            // The Name is the initial name
            Logger.detail("------------ Check that name is the default one ------------");
            a = ap.getName();
            name = ap.getObjectId();

            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            // Security Domain is the initial security domain
            Logger.detail("------------ Check Security Domain ------------");
            a = ap.getSecurityDomain();
            if (0 != a.compareTo(SecurityDomain.INITIAL_SD_ID))
                throw new ObjectException();

            // Starting Date is set at 000101
            Logger.detail("------------ Check Starting Date ------------");
            a = ap.getStartingDate();
            if (0 != a.compareTo("000101"))
                throw new ObjectException();

            // Expiration Date is set at 500101
            Logger.detail("------------ Expiration Date ------------");
            a = ap.getExpirationgDate();
            if (0 != a.compareTo("500101"))
                throw new ObjectException();

            // check the weekly policy;
            Logger.detail("------------ Weekly Policy ------------");

            for (int i = 0; i < 7; i++) {
                dailyPolicy = ap.getDailyPolicy(i);

                for (int j = 0; j < 3; j++) {

                    // check the start time
                    a = dailyPolicy.getStartTime(j);
                    if (0 != a.compareTo("0000"))
                        throw new ObjectException();

                    // check the end time
                    a = dailyPolicy.getEndTime(j);
                    if (0 != a.compareTo("0000"))
                        throw new ObjectException();
                }
            }
                        
/*################################################################################################
                User can create an access policy                 
  ################################################################################################*/

            Logger.detail("------------ User Can not create access policy ------------");

            apduObject = new Apdu(Apdu.CREATE_SE_AC_POLICY_APDU);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            c.requester = user;
            c.description = "User Can not create an access policy";
            c.execute(apduObject.toString(), expectedRes);
                        
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
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
testCase03
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Super A can update all the fields of a policy object

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase03() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        AccessPolicy ap;
        String a, name, startingDate, expirationDate, alwaysPolicy;
        User admin, user;

        String testCase = testBatch + " /" + "Test Case 03";

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);
                    
/*################################################################################################
                It shall be possible to update all the fields               
  ################################################################################################*/

            // The Name is the initial name
            Logger.detail("------------ Update test ------------");

            name = "31323334";
            startingDate = "161201";
            expirationDate = "181201";
            alwaysPolicy = "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359";

            ap.setName(name);
            ap.setAlwaysWeeklyPolicy();
            ap.setExpirationDate(expirationDate.substring(0, 2), expirationDate.substring(2, 4), expirationDate.substring(4, 6));
            ap.setStartingDate(startingDate.substring(0, 2), startingDate.substring(2, 4), startingDate.substring(4, 6));

            ap.update(superA);
            ap.syncroFields(superA);

            a = ap.getName();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            a = ap.getStartingDate();
            if (0 != a.compareTo(startingDate)) {
                throw new ObjectException();
            }

            a = ap.getExpirationgDate();
            if (0 != a.compareTo(expirationDate)) {
                throw new ObjectException();
            }

            a = ap.getWeeklyPolicyString();
            if (0 != a.compareTo(alwaysPolicy)) {
                throw new ObjectException();
            }
                        
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
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

DESCRIPTION: Admint can update all the fields of a policy object. User not;

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase04() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command c = new Command();
        AccessPolicy ap;
        String a, name, startingDate, expirationDate, alwaysPolicy, expectedRes;
        User admin, user;
        String testCase = testBatch + " /" + "Test Case 04";

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);
                    
/*################################################################################################
                It shall be possible to update all the fields               
  ################################################################################################*/

            // The Name is the initial name
            Logger.detail("------------ Update test ------------");

            name = "31323334";
            startingDate = "161201";
            expirationDate = "181201";
            alwaysPolicy = "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359";

            ap.setName(name);
            ap.setAlwaysWeeklyPolicy();
            ap.setExpirationDate(expirationDate.substring(0, 2), expirationDate.substring(2, 4), expirationDate.substring(4, 6));
            ap.setStartingDate(startingDate.substring(0, 2), startingDate.substring(2, 4), startingDate.substring(4, 6));

            ap.update(admin);
            ap.syncroFields(admin);

            a = ap.getName();
            if (0 != a.compareTo(name))
                throw new CommandErrorException();

            a = ap.getStartingDate();
            if (0 != a.compareTo(startingDate)) {
                throw new ObjectException();
            }

            a = ap.getExpirationgDate();
            if (0 != a.compareTo(expirationDate)) {
                throw new ObjectException();
            }

            a = ap.getWeeklyPolicyString();
            if (0 != a.compareTo(alwaysPolicy)) {
                throw new ObjectException();
            }
                    
  /*################################################################################################
                user can not update the fields of a polioy object                 
  ################################################################################################*/
            Logger.detail("------------ User Can not modify access policy ------------");

            name = "0000000000000000000000000000000000000000000000000000000000000000";

            Apdu apdu = new Apdu(Apdu.UPDATE_POLICY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, ap.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_NAME, name);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            c.requester = user;
            c.description = "User Can not create an access policy";
            c.execute(apdu.toString(), expectedRes);
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase05
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: It is not possible update policy with wrong time;
                 user can not delete a policy

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase05() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command c = new Command();
        AccessPolicy ap;
        String inversion, alwaysPolicy, expectedRes;
        User admin, user;
        String testCase = testBatch + " /" + "Test Case 05";
        StringBuilder sb;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);
                    
/*################################################################################################
                Not possible to update the policy if end time is lower than starting time               
  ################################################################################################*/

            // The Name is the initial name
            Logger.detail("------------ Update test Wrong Policy ------------");

            alwaysPolicy = "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359";

            inversion = "23592358";

            int i = 0;
            while (i < alwaysPolicy.length()) {

                sb = new StringBuilder(alwaysPolicy);
                sb.insert(i, inversion);

                ap.setWeeklyPolicyString(sb.toString());

                Apdu apdu = new Apdu(Apdu.UPDATE_POLICY_APDU);
                apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, ap.getObjectId());
                apdu.addTlv(Atlv.DATA_TAG_POLICYA, ap.getWeeklyPolicyString());

                expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                        String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                        String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                        Apdu.SW_6A89_INCORRECT_DATA_TLV_STRING +
                        String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
                c.requester = superA;
                c.description = "Not possible update a policy with expiration before than start";
                c.execute(apdu.toString(), expectedRes);

                i += 8;
            }
            // last policy entry
            ap.setWeeklyPolicyString(alwaysPolicy + inversion);

            Apdu apdu = new Apdu(Apdu.UPDATE_POLICY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, ap.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_POLICYA, ap.getWeeklyPolicyString());

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6A89_INCORRECT_DATA_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            c.requester = superA;
            c.description = "Not possible update a policy with expiration before than start";
            c.execute(apdu.toString(), expectedRes);
                                                            
  /*################################################################################################
                user can not update the fields if incorrect time is used.                 
  ################################################################################################*/
            alwaysPolicy = "00002459" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359" +
                    "00002359" + "00002359" + "00002359";

            // last policy entry
            ap.setWeeklyPolicyString(alwaysPolicy + inversion);

            apdu = new Apdu(Apdu.UPDATE_POLICY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, ap.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_POLICYA, ap.getWeeklyPolicyString());

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6A89_INCORRECT_DATA_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";
            c.requester = superA;
            c.description = "Not possible update a policy with expiration before than start";
            c.execute(apdu.toString(), expectedRes);
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase06
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Door triggering test Super A

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase06() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        AccessPolicy ap;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 06";
        DailyPolicy d;
        Calendar calendar;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);

            // set the policy for admin
            admin.setDefaultAccessPolicy(ap.getObjectId());
            admin.update(superA);
            admin.syncroFields(admin);

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);
                    
/*################################################################################################
                It is possible to trigger the door if the policy is set to always               
  ################################################################################################*/

            Logger.detail("------------ Trigger the door always Policy ------------");

            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            ap.setAlwaysWeeklyPolicy();
            ap.update(superA);
            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            ap.setNeverWeeklyPolicy();
            ap.setStartingDate("00", "00", "00");
            ap.setExpirationDate("FF", "FF", "FF");
            ap.update(superA);
/*################################################################################################
                It is possible to trigger the door if the policy is set to current time -1 minutes               
  ################################################################################################*/

            // ########## Sunday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // sunday 30 April 2017 17:01
            calendar.set(2017, Calendar.APRIL, 30, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1900", 1);
            ap.setDailyPolicy(d, DailyPolicy.SUNDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### Monday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // monday 1 May 2017 17:58
            calendar.set(2017, Calendar.MAY, 1, 17, 58);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 0);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.MONDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### TUESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // TUESDAY 2 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 2, 17, 30);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.TUESDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### WEDNESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // WEDNESDAY 2 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 3, 12, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "1300", 0);
            ap.setDailyPolicy(d, DailyPolicy.WEDNESDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### THURSDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // THURSDAY 3 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 4, 7, 59);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.THURSDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### FRIDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // FRIDAY 5 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 5, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.FRIDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);
            // ######### SATURDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // SATURDAY 6 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 6, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.SATURDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase07
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Door triggering test. Invalid. Admin

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase07() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command c = new Command();
        AccessPolicy ap;
        String expectedRes;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 07";
        DailyPolicy d;
        Calendar calendar;
        Apdu apdu;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);

            // set the policy for admin
            admin.setDefaultAccessPolicy(ap.getObjectId());
            admin.update(superA);
            admin.syncroFields(admin);

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);
                    
/*################################################################################################
                It is not possible to trigger the door if the policy is set to never               
  ################################################################################################*/

            Logger.detail("------------ Door not triggers whit never policy ------------");

            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            ap.setNeverWeeklyPolicy();
            ap.update(superA);

            // create the apdu object
            apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Trigger Door ";
            c.requester = admin;
            String a = apdu.toString();
            c.execute(a, expectedRes);


            ap.setNeverWeeklyPolicy();
            ap.setStartingDate("00", "00", "00");
            ap.setExpirationDate("FF", "FF", "FF");

            ap.update(superA);
/*################################################################################################
                It is not possible triggers the door against the daily policy
  ################################################################################################*/

            // ########## Sunday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // sunday 30 April 2017 19:01
            calendar.set(2017, Calendar.APRIL, 30, 19, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1900", 1);
            ap.setDailyPolicy(d, DailyPolicy.SUNDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### Monday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // monday 1 May 2017 18:01
            calendar.set(2017, Calendar.MAY, 1, 18, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 0);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.MONDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### TUESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // TUESDAY 2 May 2017 18:30
            calendar.set(2017, Calendar.MAY, 2, 18, 30);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.TUESDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### WEDNESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // WEDNESDAY 2 May 2017 06:59
            calendar.set(2017, Calendar.MAY, 3, 6, 59);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "1300", 0);
            ap.setDailyPolicy(d, DailyPolicy.WEDNESDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### THURSDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // THURSDAY 3 May 2017 08:01
            calendar.set(2017, Calendar.MAY, 4, 8, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.THURSDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### FRIDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // FRIDAY 5 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 5, 18, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.FRIDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### SATURDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // SATURDAY 6 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 6, 16, 59);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.SATURDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);
                    
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase08
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Door triggering test user

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase08() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        AccessPolicy ap;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 08";
        DailyPolicy d;
        Calendar calendar;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);

            // set the policy for admin
            user.setDefaultAccessPolicy(ap.getObjectId());
            user.update(superA);
            user.syncroFields(admin);

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);
                    
/*################################################################################################
                It is possible to trigger the door if the policy is set to always               
  ################################################################################################*/

            Logger.detail("------------ Trigger the door always Policy ------------");

            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            ap.setAlwaysWeeklyPolicy();
            ap.update(superA);
            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

            ap.setNeverWeeklyPolicy();
            ap.setStartingDate("00", "00", "00");
            ap.setExpirationDate("FF", "FF", "FF");
            ap.update(superA);
/*################################################################################################
                It is possible to trigger the door if the policy is set to current time -1 minutes               
  ################################################################################################*/

            // ########## Sunday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // sunday 30 April 2017 17:01
            calendar.set(2017, Calendar.APRIL, 30, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1900", 1);
            ap.setDailyPolicy(d, DailyPolicy.SUNDAY);
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### Monday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // monday 1 May 2017 17:58
            calendar.set(2017, Calendar.MAY, 1, 17, 58);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 0);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.MONDAY);
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### TUESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // TUESDAY 2 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 2, 17, 30);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.TUESDAY);
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### WEDNESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // WEDNESDAY 2 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 3, 12, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "1300", 0);
            ap.setDailyPolicy(d, DailyPolicy.WEDNESDAY);
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### THURSDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // THURSDAY 3 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 4, 7, 59);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.THURSDAY);
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### FRIDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // FRIDAY 5 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 5, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.FRIDAY);
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);
            // ######### SATURDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // SATURDAY 6 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 6, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.SATURDAY);
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase09
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Door triggering test(User). Invalid.

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase09() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command c = new Command();
        AccessPolicy ap;
        String expectedRes;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 09";
        DailyPolicy d;
        Calendar calendar;
        Apdu apdu;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);

            // set the policy for admin
            user.setDefaultAccessPolicy(ap.getObjectId());
            user.update(superA);
            user.syncroFields(admin);

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);
                    
/*################################################################################################
                It is not possible to trigger the door if the policy is set to never               
  ################################################################################################*/

            Logger.detail("------------ Door not triggers whit never policy ------------");

            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            ap.setNeverWeeklyPolicy();
            ap.update(superA);

            // create the apdu object
            apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Trigger Door ";
            c.requester = user;
            String a = apdu.toString();
            c.execute(a, expectedRes);


            ap.setNeverWeeklyPolicy();
            ap.setStartingDate("00", "00", "00");
            ap.setExpirationDate("FF", "FF", "FF");

            ap.update(superA);
/*################################################################################################
                It is possible not possible triggers the door against the daily policy
  ################################################################################################*/

            // ########## Sunday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // sunday 30 April 2017 19:01
            calendar.set(2017, Calendar.APRIL, 30, 19, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1900", 1);
            ap.setDailyPolicy(d, DailyPolicy.SUNDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### Monday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // monday 1 May 2017 18:01
            calendar.set(2017, Calendar.MAY, 1, 18, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 0);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.MONDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### TUESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // TUESDAY 2 May 2017 18:30
            calendar.set(2017, Calendar.MAY, 2, 18, 30);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.TUESDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### WEDNESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // WEDNESDAY 2 May 2017 06:59
            calendar.set(2017, Calendar.MAY, 3, 6, 59);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "1300", 0);
            ap.setDailyPolicy(d, DailyPolicy.WEDNESDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### THURSDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // THURSDAY 3 May 2017 08:01
            calendar.set(2017, Calendar.MAY, 4, 8, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.THURSDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### FRIDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // FRIDAY 5 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 5, 18, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.FRIDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);

            // ######### SATURDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // SATURDAY 6 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 6, 16, 59);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.SATURDAY);
            ap.update(superA);

            // try to trigger the door;
            c.execute(a, expectedRes);
                    
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            tc.testCompleted(false, "failure");
            Logger.testResult(false);
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase010
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Door triggering test Admin with default policy

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase10() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        AccessPolicy ap;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 10";
        DailyPolicy d;
        Calendar calendar;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA, admin.getAcPolicy());

            // set the policy for user;

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);
                    
/*################################################################################################
                It is possible to trigger the door if the policy is set to always               
  ################################################################################################*/

            Logger.detail("------------ Trigger the door always Policy ------------");

            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            ap.setAlwaysWeeklyPolicy();
            ap.update(superA);
            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            ap.setNeverWeeklyPolicy();
            ap.setStartingDate("00", "00", "00");
            ap.setExpirationDate("FF", "FF", "FF");
            ap.update(superA);
/*################################################################################################
                It is possible to trigger the door if the policy is set to current time -1 minutes               
  ################################################################################################*/

            // ########## Sunday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // sunday 30 April 2017 17:01
            calendar.set(2017, Calendar.APRIL, 30, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1900", 1);
            ap.setDailyPolicy(d, DailyPolicy.SUNDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### Monday test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // monday 1 May 2017 17:58
            calendar.set(2017, Calendar.MAY, 1, 17, 58);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 0);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.MONDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### TUESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // TUESDAY 2 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 2, 17, 30);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            ap.setDailyPolicy(d, DailyPolicy.TUESDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### WEDNESDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // WEDNESDAY 3 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 3, 12, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "1300", 0);
            ap.setDailyPolicy(d, DailyPolicy.WEDNESDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### THURSDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // THURSDAY 4 May 2017 17:30
            calendar.set(2017, Calendar.MAY, 4, 7, 59);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.THURSDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);

            // ######### FRIDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // FRIDAY 5 May 2017 07:59
            calendar.set(2017, Calendar.MAY, 5, 7, 59);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.FRIDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);
            // ######### SATURDAY test
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // SATURDAY 6 May 2017 16:30
            calendar.set(2017, Calendar.MAY, 6, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            d = new DailyPolicy();
            d.setInterval("1700", "1800", 2);
            d.setInterval("1200", "1300", 1);
            d.setInterval("0700", "0800", 0);
            ap.setDailyPolicy(d, DailyPolicy.SATURDAY);
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(admin, Door.DOOR_COMMAND_SWITCH_OFF);
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase011
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Test policy validity

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase11() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        Command c = new Command();
        AccessPolicy ap;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 11";
        DailyPolicy d;
        Calendar calendar;
        Apdu apdu;
        String expectedRes;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);

            // set the policy for USER
            user.setDefaultAccessPolicy(ap.getObjectId());
            user.update(superA);
            user.syncroFields(admin);

            // set the policy for admin
            admin.setDefaultAccessPolicy(ap.getObjectId());
            admin.update(superA);
            admin.syncroFields(admin);
/*################################################################################################
        It is possible to trigger the door if the policy is valid            
  ################################################################################################*/

            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // 29 April 2017 17:01
            calendar.set(2017, Calendar.MARCH, 29, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            // validity fro, 29/3/2017
            ap.setAlwaysWeeklyPolicy();
            ap.setStartingDate("17", "03", "28");
            ap.setExpirationDate("17", "03", "30");
            ap.update(superA);

            d = new DailyPolicy();
            d.setInterval("1700", "1900", 1);
            ap.setDailyPolicy(d, DailyPolicy.SUNDAY);
            ap.update(superA);
            ap.syncroFields(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);

            // delay dummy
            ap.update(superA);

            door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);

/*################################################################################################
        It is not possible to trigger the door if the policy is not valid            
  ################################################################################################*/

            // ######### policy expired
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            calendar.set(2017, Calendar.MARCH, 27, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            // validity fro, 29/3/2017
            ap.setAlwaysWeeklyPolicy();
            ap.setStartingDate("17", "03", "28");
            ap.setExpirationDate("17", "03", "30");
            ap.update(superA);

            d = new DailyPolicy();
            d.setInterval("1700", "1900", 1);
            ap.setDailyPolicy(d, DailyPolicy.SUNDAY);
            ap.update(superA);

            // create the apdu object
            apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Trigger Door ";
            c.requester = admin;
            String a = apdu.toString();
            c.execute(a, expectedRes);


            // ######### policy expired
            calendar = Calendar.getInstance();

            // set(int year, int month, int date, int hourOfDay, int minute)
            // 29 April 2017 17:01
            calendar.set(2017, Calendar.MARCH, 30, 17, 1);
            thisDevice.setTimeAndDate(calendar, superA, pinTest);

            // validity fro, 29/3/2017
            ap.setAlwaysWeeklyPolicy();
            ap.setStartingDate("17", "03", "28");
            ap.setExpirationDate("17", "03", "29");
            ap.update(superA);

            d = new DailyPolicy();
            d.setInterval("1700", "1900", 1);
            ap.setDailyPolicy(d, DailyPolicy.SUNDAY);
            ap.update(superA);

            // create the apdu object
            apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Trigger Door ";
            c.requester = admin;
            a = apdu.toString();
            c.execute(a, expectedRes);
                    

  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase012
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: User can not delete the access policy. SuperA and admin can deleted.

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase12() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);
        
        Command c = new Command();
        AccessPolicy ap, ap1;
        User admin, user;
        Apdu apdu;
        String testCase = testBatch + " /" + "Test Case 11";
        Calendar calendar;
        String expectedRes;

        // ---------------------- Code -------------------------------
        //boolean result = true;
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);
            ap1 = new AccessPolicy(admin);

            // set the policy for USER
            user.setDefaultAccessPolicy(ap.getObjectId());
            user.update(superA);
            user.syncroFields(admin);

            // set the policy for admin
            admin.setDefaultAccessPolicy(ap1.getObjectId());
            admin.update(superA);
            admin.syncroFields(admin);
/*################################################################################################
        User can not delete an access policy            
  ################################################################################################*/

            Logger.detail("------------ User can not delete an Access Policy ------------");
            // create the apdu object
            apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, ap.getObjectId());

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            c.description = "Create Access Policy";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);
                    
 /*################################################################################################
        Admin and SuperA can delete a policy
  ################################################################################################*/

            Logger.detail("------------ SuperA and amdin can delete the access policy ------------");
            ap.delete(admin);
            ap1.delete(superA);

 /*################################################################################################
        Admin, SuperA and Users can not delete the systemaccess policy
  ################################################################################################*/
            Logger.detail("------------ Admin, SuperA, User can not delete system access policy ------------");
            apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, AccessPolicy.DEFAULT_ACCESS_POLICY_ID);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            c.description = "Create Access Policy";
            c.requester = user;
            c.execute(apdu.toString(), expectedRes);

            c.description = "Create Access Policy";
            c.requester = admin;
            c.execute(apdu.toString(), expectedRes);

            c.description = "Create Access Policy";
            c.requester = superA;
            c.execute(apdu.toString(), expectedRes);

  /*################################################################################################
                Deletion of objects                
  ################################################################################################*/
            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);
            user.delete(admin);
            admin.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false,"failure");
            throw new TestException();
        }
        tc.testCompleted(true,"success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
testCase13
--------------------------------------------------------------------------
AUTHOR:	PDI

DESCRIPTION: Door is not triggering if default policy is the null object identifier

Security Level: None

------------------------------------------------------------------------------*/
    public static void testCase13() throws TestException {
        TestCase tc = new TestCase();
thisUnit.addTestCase(tc);
TestEventHandler.getInstance().subscribeAlone(tc);

        Command c = new Command();
        AccessPolicy ap;
        String expectedRes;
        User admin, user;
        Door door;
        String testCase = testBatch + " /" + "Test Case 13";
        Calendar calendar;
        Apdu apdu;

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);

            // launch a reset
            thisDevice.ping();

            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            admin = new User(superA, User.USER_ROLE_ADMIN, "admin");
            admin.updateKey("xxxxxxxxxxxxxxxxx");

            user = new User(superA, User.USER_ROLE_USER, "user");
            user.updateKey("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

            ap = new AccessPolicy(superA);

            // set the policy for admin
            admin.setDefaultAccessPolicy(ap.getObjectId());
            admin.update(superA);
            admin.syncroFields(admin);

            door = new Door(superA);
            door.setGpioId("05");
            door.setStatus(Door.SE_DOOR_ENABLED);
            door.setInitialOutputValue(Door.SE_DOOR_STATUS_LOW);
            door.update(superA);
                    
/*################################################################################################
                User can not trigger the door if the default policy is the null object identifier              
  ################################################################################################*/

            Logger.detail("------------ Door not triggers whit null object id ------------");

            user.syncroFields(user);
            user.setDefaultAccessPolicy(Device.NULL_OBJECT_ID);
            user.update(admin);


            // create the apdu object
            apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AF9_INCORRECT_ID_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Trigger Door ";
            c.requester = user;
            String a = apdu.toString();
            c.execute(a, expectedRes);
/*################################################################################################
                Admin can not trigger the door if the default policy is the null object identifier              
  ################################################################################################*/

            Logger.detail("------------ Door not triggers whit null object id ------------");

            admin.syncroFields(admin);
            admin.setDefaultAccessPolicy(Device.NULL_OBJECT_ID);
            admin.update(superA);


            // create the apdu object
            apdu = new Apdu(Apdu.TRIGGER_DOOR_APDU);
            apdu.addTlv(Apdu.DATA_TAG_OBJECT_ID, door.getObjectId());
            apdu.addTlv(Apdu.DATA_TAG_DOOR_COMMAND, Door.DOOR_COMMAND_SWITCH_ON);

            // set the expected result and send the command;
            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AF9_INCORRECT_ID_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // send command
            c.description = "Trigger Door ";
            c.requester = admin;
            a = apdu.toString();
            c.execute(a, expectedRes);
                   
                    
  /*################################################################################################
                admin can delete an access policy                 
  ################################################################################################*/
            calendar = Calendar.getInstance();
            thisDevice.setTimeAndDate(calendar, superA, pinTest);
            ap.delete(admin);
            user.delete(admin);
            admin.delete(superA);
            door.delete(superA);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }

    /*----------------------------------------------------------------------------
  testCase19
  --------------------------------------------------------------------------
  AUTHOR:	PDI

  DESCRIPTION: User read policy

  Security Level: None

  ------------------------------------------------------------------------------*/
    public static void testCase14() throws TestException {
        TestCase tc = new TestCase();
        thisUnit.addTestCase(tc);
        TestEventHandler.getInstance().subscribeAlone(tc);

        String testCase = testBatch + "/" + "Test Case 16";

        String expectedRes;
        Apdu apdu;
        Command c = new Command();

        // ---------------------- Code -------------------------------
        try {
            Logger.testCase(testCase);


            // instantiate a local User object for the SUPER-A
            // User superA = new User(User.SUPER_ADM_ID, RemoteAuthenticator.SUPERA_INITIAL_KEY);
            // object personalized
            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);

            User admin = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin.updateKey("adminx");
            admin.syncroFields(admin);

            User user = new User(admin, User.USER_ROLE_USER, "s[atter");
            user.updateKey("newAdministratorKeysSutta");
            user.syncroFields(user);

            // object created
            User admin1 = new User(superA, User.USER_ROLE_ADMIN, "splash");
            admin1.updateKey("adminx");
            admin1.syncroFields(admin1);


            User user1 = new User(admin, User.USER_ROLE_USER, "s[atter");
            user1.updateKey("newAdministratorKeysSutta");
            user1.syncroFields(user1);


            AccessPolicy policy = new AccessPolicy(superA);
            policy.syncroFields(superA);

            //#
            //#  Administrator can not create a policy if disabled
            //#

            Logger.detail("Administrator can not create a policy if disabled");

            // change the user status to inactive
            admin1.setStatus(User.STATUS_INACTIVE);
            admin1.update(superA);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6985_CONDITION_OF_USE_NOT_SATISFIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // change the user status to inactive
            apdu = new Apdu(Apdu.CREATE_SE_AC_POLICY_APDU);

            // send command
            c.description = "Administrator can not create a policy if disabled";
            c.requester = admin1;
            c.execute(apdu.toString(), expectedRes);

            //#
            //#  Admin can not delete a policy if disabled
            //#

            Logger.detail("Admin can not delete a policy if disabled");

            // change the user status to inactive
            admin1.setStatus(User.STATUS_INACTIVE);
            admin1.update(superA);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // apdu object
            apdu = new Apdu(Apdu.DELETE_OBJECT_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, policy.getObjectId());

            // send command
            c.description = "Admin can not delete a policy if disabled";
            c.requester = admin1;
            c.execute(apdu.toString(), expectedRes);

            //#
            //#  Admin can not update a policy if disabled
            //#

            Logger.detail("Admin can not update a policy if disabled");

            // change the user status to inactive
            admin1.setStatus(User.STATUS_INACTIVE);
            admin1.update(superA);

            expectedRes = String.format("%02X", Atlv.DATA_TAG_RESPONSE) + "1A" +
                    String.format("%02X", Atlv.DATA_TRANSACTION_COUNTER_TAG) + "08????????????????" +
                    String.format("%02X", Atlv.DATA_TAG_APDU_RESPONSE) + "04" +                                                                                                        // tag															// Object ID
                    Apdu.SW_6AFC_ACCESS_DENIED_TLV_STRING +
                    String.format("%02X", Atlv.DATA_TAG_MAC) + "08????????????????";

            // apdu object
            apdu = new Apdu(Apdu.UPDATE_POLICY_APDU);
            apdu.addTlv(Atlv.DATA_TAG_OBJECT_ID, policy.getObjectId());
            apdu.addTlv(Atlv.DATA_TAG_NAME, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            // send command
            c.description = "Admin can not update a policy if disabled";
            c.requester = admin1;
            c.execute(apdu.toString(), expectedRes);

            //#
            //# Object deletion
            //#
            admin.delete(superA);
            admin1.delete(superA);
            user.delete(superA);
            user1.delete(superA);
            policy.delete(superA);

            superA.updateKey(RemoteAuthenticator.SUPERA_INITIAL_KEY);
        } catch (CommandErrorException | ObjectException | IOException e) {
            Logger.testResult(false);
            tc.testCompleted(false, "failure");
            throw new TestException();
        }
        tc.testCompleted(true, "success");
        Logger.testCase(testCase);
        Logger.testResult(true);
    }
}

