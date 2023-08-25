# Switches and Doors

### Switches and Doors are remotely controlled switches

---

Switches and Doors are switches Users may control remotely. They may be configured as either monostable or bistable, and may implement further security measures such as PIN or Biometric (to be implemented client-side). Activation of a switch is conditional to a weekly time policy defined by an Admin.

What sets a Door apart from a Switch is that its output corresponds to the state of one of the Relays on the circuit board; a Switch does not reflect its state on an output relay but on a virtual output used to set input to other functional blocks.
 
## Instantiation
Only Super Administrator can instantiate and configure a Door.
The command below creates an instance of a Door on the device, and its local copy in the application, which is stored in the 'door' variable.
Subsequent commands set fields in the Door. These only operate on the local copy, therefore in order to propagate changes to the remote device, the update method shall be invoked.
```
	Door door = new Door(superA);                        
	door.setGpioId("00");
	door.setMode(Door.SE_DOOR_MODE_ON_OFF);
	door.setStatus(Door.SE_DOOR_ENABLED);
	door.update(superA);
	door.syncroFields(superA);
```

To create local instance of a Door from object ID use the command below. This local instance is mirroring the remote object stored into the device, of which the object ID had been kept track of.
```
	Door door = new  Door(User requester, String doorId)
```

## GPIO association
Doors and Switches shall be associated with a GPIO output. GPIO can be a real output connected to a real relay, or it can be a virtual one used by IOT Brick as input or output of a functional block. In case GPIO is a relay of the device the Switch object is called Door. Otherwise simply Switch.

To associate a GPIO with a door following command shall be used:
```
	Door door = new Door(superA);                        
	door.setGpioId("00");
	door.setGpioId(SE_GPIO_0);
```
SetGpioId methods needs in input an hex string representing the GPIO ID. The first 4 GPIO IDs are physical, while the others are virtual. Value "FF" is reserved to Void GPIO ID.

Examples:
```
    // corresponding to the state of Relays 0-1-2-3
	public static final String SE_GPIO_0 = "00";
	public static final String SE_GPIO_1 = "01";        
	public static final String SE_GPIO_2 = "02";        
	public static final String SE_GPIO_3 = "03";        


	// Void GPIO ID
	public static final String SE_DOOR_VOID_GPIOID = "FF";

    // Virtual GPIOs
	public static final String SE_VIRTUAL_GPIO_0 = "80";
	public static final String SE_VIRTUAL_GPIO_1 = "81";        
	public static final String SE_VIRTUAL_GPIO_2 = "82";        
	public static final String SE_VIRTUAL_GPIO_3 = "83";        
	public static final String SE_VIRTUAL_GPIO_4 = "84";        
	public static final String SE_VIRTUAL_GPIO_5 = "85";        
	public static final String SE_VIRTUAL_GPIO_6 = "86";        
	public static final String SE_VIRTUAL_GPIO_7 = "87";
	public static final String SE_VIRTUAL_GPIO_8 = "88";
	public static final String SE_VIRTUAL_GPIO_9 = "89";        
	public static final String SE_VIRTUAL_GPIO_10 = "8A";        
	public static final String SE_VIRTUAL_GPIO_11 = "8B";        
	public static final String SE_VIRTUAL_GPIO_12 = "8C";        
	public static final String SE_VIRTUAL_GPIO_13 = "8D";        
	public static final String SE_VIRTUAL_GPIO_14 = "8E";        
	public static final String SE_VIRTUAL_GPIO_15 = "8F"; 
```

## Operative Modes
Switches and Doors have two operative modes: bistable mode (On/Off) or monostable mode (pulsed). In bistable mode the state is controlled by the user and can be ON or OFF. In monostable mode, instead, the output change status for a programmable time and switches back to the original state after that time.

The methods below set the operation mode of the Door. Important: set methods only change field values of the local object instance. After setting, update() method shall be called in order to make the change effective on the device.

```
 door.setMode(Door.SE_DOOR_MODE_PULSE);
 door.setMode(Door.SE_DOOR_MODE_ON_OFF);
 door.setMode(Door.SE_DOOR_MODE_TOGGLE);
 
 door.update(superA);
  
 mode = door.getMode(superA);
```
In toggle mode the output changes its status each time it receives the relevant command.

To set pulse duration use the following command. Value is pulse duration in milliseconds:
``` 
 door.setPulseDuration(3000);
```

## Initial Output State
Initial state for door and switches can be set by means of this Set method:

```
	 door.setInitialOutputValue( Door.SE_DOOR_STATUS_LOW); 
	 door.setInitialOutputValue( Door.SE_DOOR_STATUS_HIGH);  
	 
	 door.update(superA);
```

The Door can be configured to restore its last output value at startup via the following method:

```
	door.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
	
	door.update(superA);
```
## Security
User authentication can be set on a Door or Switch object. Door output status will change only if Users present valid credentials.
Authentication level are based on either PIN or Biometry. Pin is verified by the device. Biometric authentication shall be performed by local application.

```           
	door.setSecurity(Door.SE_DOOR_SECURITY_PIN);
	door.update(superA);
	
	door.setSecurity(Door.SE_DOOR_SECURITY_FINGERPRINT);
	door.update(superA);
``` 

## Send Command and Read Status
Below some example code on how to send a command to a door or a switch that changes the output status, and commands to read the output status of a door.

Bistable(ON/OFF) output control:
```  		
		// *********************************************
		//	Set output state to ON and check output value
		// *********************************************
	door.setOutput(user, Door.DOOR_COMMAND_SWITCH_ON);
	gpioValue = door.getOutput(superA);
	if(!(gpioValue.equals(Door.SE_DOOR_STATUS_HIGH)))
	{
		throw new TestException();
	} 

		// *********************************************
		//Set output state to OFF and check output value
		// *********************************************
	door.setOutput(user, Door.DOOR_COMMAND_SWITCH_OFF);
	gpioValue = door.getOutput(superA);
	if(!(gpioValue.equals(Door.SE_DOOR_STATUS_LOW)))
	{
		throw new TestException();
	}
```

Monostable(pulsed) output control:
```				
	door.setPulseDuration(3000);
	door.update(superA);
	door.syncroFields(superA);
	for (int i = 0; i<10; i++)
	{
		door.setOutput(user, Door.DOOR_COMMAND_PULSE);
		gpioValue = door.getOutput(superA);
		if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_HIGH))
		{
			throw new TestException();
		} 
		Thread.sleep(4000);

		gpioValue = door.getOutput(superA);
		if(0 != gpioValue.compareTo(Door.SE_DOOR_STATUS_LOW))
		{
			throw new TestException();
		} 
	}
```
In case PIN verification is enabled, following commands shall be used.
```
	door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON, PIN);
	door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF, PIN);
	door.setOutput(superA, Door.DOOR_COMMAND_PULSE, PIN);
```

## Door Activation Status
Doors and Switches can be in one of two possible activation status: enabled or disabled. If a Door is disabled it is not possible to change its output status.

To set Door activation status use below commands.
To disable:
```                      
	door.setStatus( Door.SE_DOOR_DISABLED);
	door.update(superAdmin);
```
To enable a disabled object:
```
	door.setStatus(Door.SE_DOOR_ENABLED);
	door.update(superAdmin);
```

## Delete
Super Administrator can delete a Door instance into the device by calling the appropriate delete() method. When a door instance is deleted from the device, memory is erased, collected and can be reused for other objects.
```
	door.delete(superA);
```
