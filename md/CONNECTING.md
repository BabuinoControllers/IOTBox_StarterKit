# Connecting to IOT Brick

### Users remotly access device resources

---

IOT Brick Users remotely access device resources through LAN or WAN connectivity. Based on their role, users can perform administrative actions like create, update or delete users, manages policies, assign rights, configure the device logic and delete objects instance on the device. Moreover Users can read value measured by sensors and can activate or deactivate a switch.

## Discovery Protocol
IOT Brick implements a discovery protocol that allows applicaiton to discover connected devices. There is no need to configure an IP address in IOT Brick devices. Only information required to connect to a device is a its serial number. A serial number is an hext string of 32.

To discover a device over the network use the following commands. This command tries to discover the device with given serial number over the ethernet bearer. In case first attempt fails the command retries to discover the device for three times after 2s each.
```
	Device thisDevice = Device.discover(serialNumber, ConnectionDetails.BEARER_ETHERNET,3,2000); 
```
## Local Network Scan
In order to identify all IOT Brick devices connected to the network it is possible to launch a generic network discover. It returns the list of all devices connected over the network.

```
	Device.discover(timeout_ms)
```

## Ping
Ping is used to check if a device is connected to the bearer. Ping allows to refresh data that device provides at ping response.

```
	thisDevice.ping(); 
```
Ping command works for local connection and wan connection as well.

## User Connection
An user that want to connect with a device shall first extabilish a connection with it. So first device instance shall be discovered and after user instance shall be created.

```
		// Discover the device and perform first connection
	Device thisDevice = Device.discover(deviceSerial, ConnectionDetails.BEARER_ETHERNET,3,2000);     
	
		// Get a local instance of superAdministrator
	superA = new User(User.SUPER_ADM_ID, "SuperAdmin_Key", thisDevice);  
	
	User user = new User(superA, userId)

```
