# Users in IOT Brick

### Users remotely access device resources

---

IOT Brick Users remotely access device resources through LAN or WAN connectivity. Based on their role, Users may perform administrative actions like managing Users and Policies, assigning rights, configuring the device logic and deleting objects instance on the device. Moreover, Users may read value measured by sensors and trigger switches.

## Users Role

Three roles with increasing privileges are provided:

- **Regular User (User)**
  Users have a limited scope. They can read values from sensors, control switches based on the policy defined by Administrators,and change part of their own configuration.

- **Administrator (Admin)**
  Admins have a management role. They can create, enable, and disable new users, and create and assign Access Policies. They do not have device configuration rights and they cannot change another User’s role.

- **Super Administrator (SuperA)**
  Super Administrators possess complete access to device resources, granting them the authority to instantiate or delete any Object within the device, be it Functional Blocks, other Users, or any application logic configuration. They enjoy unfettered access to device settings and system logs, enabling them to execute device resets or factory resets. On top of possessing Admin powers, they can also set other Users’ roles.

It is important to note that each device accommodates only one Super Administrator, which is set when the device is first configured.

## User Key
Each user has its own key set that is used to communicate with the device. Keys enforces authenticity, confidentiality and integrity of communication between user and the device.

SuperA’s key is supplied with the Device, as there can be only one, while Users and Admins’ keys must be set when initialized. 

Each of these keys must be updated on the first connection attempt made by that User. 

In case a key is lost, a higher-power User can reset it, but for SuperA’s key the only solution is to reset the device.

Examples:

Create a local mirror copy of the device's Super Administrator. Connection with the device is first established by the discover() procedure:
```
	Device thisDevice = Device.discover(deviceId, ConnectionDetails.BEARER_ETHERNET, 3, 2000);
	SuperA superA = new SuperA(RemoteAuthenticator.SUPERA_INITIAL_KEY, thisDevice);
```

Super Administrator instantiates a new user in the device. User has the Administrator role and 'initial key' as initial key. The admin then updates its own key and syncs with the device's instance:
```
	User admin = new User(superA, User.USER_ROLE_ADMIN, "initial key");
	
	admin.updateKey("panda");
	admin.syncroFields(admin);
```

New user instance is created on the device by the Administrator 'admin'.  The first action the new user performs is update its own key and synchronize its fields with the device's instance:
```
		// object created
	User user = new User(admin, User.USER_ROLE_USER, "rigqa");
	user.updateKey("newSutta");
	user.syncroFields(user);
```
## User Instantiation
New users can be created only by Administrators and SuperAdministrators.

Super Administrator user instance is created at factory time. In order to get superA instance, following commands shall be launched.

```
		// Discover the device and perform first connection
	Device thisDevice = Device.discover(deviceSerial, ConnectionDetails.BEARER_ETHERNET,3,2000);     
	
		// Get a local instance of superAdministrator
	superA = new User(User.SUPER_ADM_ID, "SuperAdmin_Key", thisDevice);  
```

Following command instantiate a new user instance (permanent) into the device and create a local instance of the user.
```
	User admin = new User(superA, User.USER_ROLE_ADMIN, "initial key");
```

Following command create a local instance of a user by an Administrator passing as input user objectId. Does not perform IO on the device.
```
	User user = new User(adminUserHandler, "00000010");
```

Following commands creates a local instance of a User given its object id and key. Does not perform IO on the device.
```
	User user = User("00000010", "MyKey");
```
## User Status
User can be enabled (STATUS_ACTIVE) and disabled (STATUS_INACTIVE). User status can be set by Administrators and SuperAdministrators. If user is disabled, they can not perform any action. Default status of user at creation is enabled.

To disable a User, status of the local object shall be set to disabled. In order to push the change to the mirror object on the device, the update() command shall be performed.

```                      
	user.setStatus(User.STATUS_INACTIVE);
	user.update(admin);
```
To enable a disabled object:
```
	user.setStatus(User.STATUS_ACTIVE);
	user.update(admin);
```
## Access Policy
A user can be associated with a default access policy and/or with a PolicyLink list. Purpose of policy is to define if and when a user is allowed to open a door.

Another important difference between SuperAdministrators and other roles is that SuperAdministrator can always trigger a Door/Switch.

[Here find more about how configure access policy to users](md/ACCESS_POLICY.md)

## Remote Authentication Object

At User initialization, two objects are also created, whose role is to enforce two levels of authentication: RAO and LAO.

Remote Authentication Object (RAO) is in charge of enforcing security in the communication. It stores user keys that enforces security over communication, guaranteeing integrity of messages, confidentiality and authenticity of origin.
It is used to encrypt commands at source, and decrypt them at destination. In case of mismatch, communication cannot be decrypted and commands fail.
When a User is created, Administrators must define an initial key, which must be provided to the User through a secure channel. User shall change the key at first access.
Each device has a different initial key for the Super Administrator.

If a User loses its key set, reset is only possible through the support of an Administrator:

```
	admin2.resetKey(admin, "ribrezza");
```

In case Super Administrator loses its own key set, the only way to recover control of the device is Factory Reset.

## Local Authenticator Object

Local Authentication Object (LAO) is in charge of user authentication through a PIN. Each User owns a LAO that is associated to them at their initialization time.

The LAO object can be retrieved in this way:
```
	user.getLocalAuthenticatorObject();
```

Users may set or change their own PIN, which can have a length ranging from 4 to 12 digits.

If the maximum retry counter value, set by the Administrator, is exceeded due to unsuccessful PIN attempts, the LAO locks, and local user authentication by PIN fails. PIN reset is only possible through the support of an administrator.

Retry counter value can be retrieved in this way:
```
	lao.getRetryCounter();
```
Administrator and Super Administrator can set a User's max retry counter by retrieving the User's LAO and setting the max retry counter value:
```
	lao.setMaxRetries(superA, (byte) 3);
```
User sets its PIN at 1234. String "31323334" represent string "1234" in ascii code:
```
	user.setPin(user, pin.substring(0, "31323334"));
```
User changes its PIN. To change the pin value user shall present first current pin "31323334":
```
	user.changePin(user, "31323334", "31313131");
```

## Delete User
Super Administrator and Administrators can delete Users with lower privileges than themselves.

When a User instance is deleted from the device, also relevant Remote Authenticator and Local Authenticator Objects are removed. After deletion, all memory on the device used to allocate user details is deleted and recovered, to be reused for new Users or other objects.
```
	admin.delete(superA);
	user.delete(superA);
```
