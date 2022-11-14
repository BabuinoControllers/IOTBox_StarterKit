# Users in IOT Brick

### Users remotly access device resources

---

IOT Brick Users remotely access device resources through LAN or WAN connectivity. Based on their role, users can perform administrative actions like create, update or delete users, manages policies, assign rights, configure the device logic and delete objects instance on the device. Moreover Users can read value measured by sensors and can activate or deactivate a switch.

## Users Role
There are three different roles for users.

- Super Administrators
- Administrators
- Users

Each role have a different scope of actions.

Super Administrators have full access to device resources. Can instantiate or delete any object into the device including digital function blocks or other users and have full control of application logic configuration. Super administrators have full access to device settings and system logs. Super administrator can perform a device reset or a factory reset. Super administrator can create new users and define weekly policy. Super administrators can assign adimistrator priviledge to users and have full access to system logs. There is only one super administrator per device. 

Device Administrators have a management roles and not a configuration role. Administrators can create new users, change user properties for example enable/disable users, can define weekly policies and associate it to users. Administrators can not modify device setting and can not configure device logic. On each device there can be several administrators. An administrator can not change the setting of another administrator. For example an administrator can not revoke the administrator rights to another administrator.

Users have a limited scope. They can access read value from sensors, control switches based on the policy defined by super administrators and adimistrators and can change part of its own configuration.

Administrator priviledge can only by assigned by a super administrator.

## User Key
Each user has its own key set that is used to communicate with the device. Keys enforces authenticity, confidentiality and integrity of communication between user and the device.

When user is instantiated it is defined its role and an initial key. This initial key shall be changed by the user at first connection.

Super Administrator initial key is defined per device at production time. First access initial key shall be changed by super administrator.

Examples:
```
	User admin = new User(superA, User.USER_ROLE_ADMIN, "initial key");
```	

Super Administrator instantiate a new user in the device. User have the administrator role and the initial key as initial key

```
	Device thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);
	SuperA superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);
```

Mirrors in the remote application the super administrator of the device. Connection with the device is first established by the discovery procedure.

```
		// object created
	User user = new User(admin, User.USER_ROLE_USER, "rigqa");
	user.updateKey("newSutta");
	user.syncroFields(user);
```
New user instance is created on the device by the administrator user admin: admin is an object instance of an administrator.  First action user perform is update its key and syncronizing his fields.

```
	admin.updateKey("panda");
```

## Remote Authentication Object
Each time an user is created also other two objects are created and associated to user objects. They are the local administrator object and remote adinistrator object.
Remote administrator object (RAO) is in charge of enforcing security in the communication. It stores user keys that enforces security over communication guaranteeing integrity of messages, confidentiality and authentication of origin.
When user is created Administrators define an initial key used to iitially secure communication. Initial key is provided to users through a secure channel. User shall change the key at first access.
Each device has a different initial key for the super administrator.

If an user forget its key it is not possible to recover the key any longer. User shall ask to administrator a key reset.

```
	admin2.resetKey(admin, "ribrezza");
```

In case Super Administrator loose its own key set then only way to recover is device reset.

## Local Authenticator Object
Local authenticator (LAO) is in charge of user authentication through a PIN. Each user own a local authenticator that is associated to user at user creation time.

Relevant user local autianticator object can be retrieved in this way.
```
	user.getLocalAuthenticatorObject();
```

Users PIN can have a length min of 4 digits max of 12 digits. Only user itself can set or change its own pin. 

If number of unsecessful pin presented to the device exceed the max retry counter value set by the administrator then LAO object blocks and local user authentication by PIN fails. PIN reset is only possible through support of an administrator.

Retry counter value can be retrieved in this way:
```
	lao.getRetryCounter();
```
Administrator and usper administrator can set user max retry counter by recovering the LAO relevant to the user and setting the max retry counter value,
```
	lao.setMaxRetries(superA, (byte) 3);
```
User set its PIN at 1234. String "31323334" represent string "1234" in ascii code.
```
	user.setPin(user, pin.substring(0, "31323334"));
```
User change its PIN. To change the pin value user shall present first current pin "31323334".
```
	user.changePin(user, "31323334", "31313131");
```

## Delete User
Super Administrator and Administrators can delete users.

Super Administrator delete an administrator and an user object on the device. When an user instance is deleted from the device also relevant Remote Authenticator and Local Authenticator are removed.
```
	admin.delete(superA);
	user.delete(superA);
```
