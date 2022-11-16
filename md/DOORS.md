# Switches and Doors

### Switch and Doors are remotly controlled switches

---

Switches and doors are remotly controlled switches. Theyare functional blocks that can be in one of two possible states: ON or OFF. State of switches and door can be controlled remotely by users according to an access policy defined by administrator. Switch and doors are exactly same objects and refer to same classes. Only difference is that Doors reflect its output state on an real device output. Switch does not reflect its state on an output relay but on a virtual output into the device used to set input to other digital function blocks.
 
## Instantiation
Super Administrators can instantiate and configure switches and door objects on the device. 
Beloow commands creates an instance of a door object on the device and relevant local instance and set changes to fields. Set methods only change the status of field of local object. In order to propagate changes on remote device an update method shall be invoked.

```
	Door door = new Door(superA);                        
	door.setGpioId("00");
	door.setMode(Door.SE_DOOR_MODE_ON_OFF);
	door.setStatus(Door.SE_DOOR_ENABLED);
	door.update(superA);
	door.syncroFields(superA);
```

To create only local instance of a door object that is mirroring the remote object into the device given object Id use below command.
```
	Door door = new  Door(User requester, String doorId)
```

## GPIO association
Doors and Switches shall be associated with a GPIO. GPIO can be a real GPIO connected to a real device output or can be a virtual one used by IOT Brick as input or output of a functional block. In case GPIO is a real GPIO of the device the Switch object is called Door. Otherwise simply switch.

To associate a GPIO with a door following command shall be used:
```
	Door door = new Door(superA);                        
	door.setGpioId("00");
	door.setGpioId(SE_GPIO_0);
```
SetGpioId methods needs in input an hex string representing the GPIO ID. Usually first 8 GPIO Id are phisical GPIOs while the others are virtual. Value "FF" is reserved to Void GPIO ID

Examples:
```
	public static final String SE_GPIO_0 = "00";
	public static final String SE_GPIO_1 = "01";        
	public static final String SE_GPIO_2 = "02";        
	public static final String SE_GPIO_3 = "03";        
	public static final String SE_GPIO_4 = "04";        
	public static final String SE_GPIO_5 = "05";        
	public static final String SE_GPIO_6 = "06";        
	public static final String SE_GPIO_7 = "07";

	// Void GPIO ID
	public static final String SE_DOOR_VOID_GPIOID = "FF";

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
Switches and doors have two operative modes: bistable mode (On/Off) or monostable mode (pulsed). In bistable mode the state is controlled by the user and can be ON or OFF. In monostable mode, instead, the output change status for a programmable time and switch back to the original state after that time.

To set mode relevant set metod shall be used as reported below. Important: set methods only change fuiled value oflocal object. After setting update method shall be called in order to make the change effective on the device.

```
 door.setMode(Door.SE_DOOR_MODE_PULSE);
 door.setMode(Door.SE_DOOR_MODE_ON_OFF);
 door.setMode(Door.SE_DOOR_MODE_TOGGLE);
 
 door.update(superA);
  
 mode = door.getMode(superA);
```
In toggle mode the output change its status each time it receive relevant command.

To set pulse duration use following command. Value is pulse diration in milliseconds.
``` 
 door.setPulseDuration(3000);
```

## Initial Output State
Initial state for door and switches can be set by means of relevant set method.

```
	 door.setInitialOutputValue( Door.SE_DOOR_STATUS_LOW); 
	 door.setInitialOutputValue( Door.SE_DOOR_STATUS_HIGH);  
	 
	 door.update(superA);
```

It is possible to push the door to restore at power on its last output value by means of the following command.

```
	door.setRestoreLastOuptutValue(Door.SE_DOOR_RESTORE_LAST_OUTPUT_VALUE_TRUE);
	
	door.update(superA);
```
## Security
User authentication level can be set on a door or switch object. Door output status will change only if Users prents valid credentials.
Authentication level are based PIN and Biometrics. Pin is verified by the device. Biometric authentication shall be performed by local applicaiton.

```           
	door.setSecurity(Door.SE_DOOR_SECURITY_PIN);
	door.update(superA);
	
	door.setSecurity(Door.SE_DOOR_SECURITY_FINGERPRINT);
	door.update(superA);
``` 

## Send Command and Read Status
Below some example code on how to send a command to a door or a switch that changes the output status and commands to read the output status of a door.

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
In case PIN verification is enabled following commands shall be used.
```
	door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON, PIN);
	door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_OFF, PIN);
	door.setOutput(superA, Door.DOOR_COMMAND_PULSE, PIN);
```

## Door Activation Status
Doors and switches can be in one of the two possible activation status: enabled or disabled. If door is disabled it is not possible to change door output status.

To set door activation status use below commands.
To disable:
```                      
	door.setStatus( Door.SE_DOOR_DISABLED);
	door.update(superAdmin);
```
To enable a disable object:
```
	door.setStatus(Door.SE_DOOR_ENABLED);
	door.update(superAdmin);
```

## Delete
Super Administrator can delete a door instance into the device by calling relevant delete method.When a door instance is deleted from the device memory is erased, collected and can be reused for other objects.
```
	door.delete(superA);
```
