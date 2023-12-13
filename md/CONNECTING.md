# Connecting to IOT Brick

### Simple device discovery through LAN or WAN

---


## Discovery Protocol
IOT Brick implements a discovery protocol that allows applications to discover connected devices. There is no need to configure any IP address. The only information required to connect to a device is its serial number. A serial number is a 16-digit HEX string.

## Local Network Discovery
To discover a device over a local network, use the following command. This command tries to discover the device with given serial number over the ethernet bearer. In case first attempt fails, the command retries to discover the device for 3 times after 2s (2000 ms) each.
```
	Device thisDevice = Device.discover(serialNumber, ConnectionDetails.BEARER_ETHERNET,3,2000); 
```
## Local Network Scan
In order to identify all IOT Brick devices connected to the local network it is possible to launch a generic network discover. It returns the list of all devices connected to the local network.

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
An application that wants to interact with a device shall first establish a connection with it. So first a device instance shall be discovered, then a User instance shall be synchronized, which can be used to interact with the device.

```
		// Discover the device and perform first connection
	Device thisDevice = Device.discover(deviceSerial, ConnectionDetails.BEARER_ETHERNET,3,2000);     
	
		// Get a local instance of superAdministrator
	superA = new User(User.SUPER_ADM_ID, "SuperAdmin_Key", thisDevice);  
	
	User user = new User(superA, userId)

```
