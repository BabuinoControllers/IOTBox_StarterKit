# Digital Function Block

### Combinatory logic on four input and one output

---

Digital function blocks implements and arbitrary and programmable combinatory logic on 4 inputs and one output.
 
## Instantiation
Only super administrator can instantiate and configure digital function blocks. 
Below commands create an instance of a digital function block on the device and create also a local object instance.
Commands set also fileds value. Set methods operates on local object fields only. In order to propagate changes to remote device the update method shall be invoked.

```                                       
	DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
	dfb.setInputId(door.getObjectId(), 0);
	dfb.setInputId(door1.getObjectId(), 1);
	dfb.setInputId(door2.getObjectId(), 2);
	dfb.setInputId(door3.getObjectId(), 3);
	dfb.setGpio("00");
	dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_AND_4_INPUT);
	dfb.update(superA);
```

IMPORTANT: in order to run logic into device, device shall be reset manually or programmatically.

To create only local instance of a digital function blocks from object Id use below command. This local instance is mirroring the remote object stored into the device.
```
	Sensor sensor = new  Door(user, sensorId)
```
## Input 
To connect input of digital funciton block with output of other functional block just provide in input to digital function block setInputId the object identifier of the output digital function block and the input id.

``` 
	dfb.setInputId(door.getObjectId(), 0);
	dfb.setInputId(door1.getObjectId(), 1);
	dfb.setInputId(door2.getObjectId(), 2);
	dfb.setInputId(door3.getObjectId(), 3);
	dfb.update(superA);
```

## GPIO association
Digital function block output can be associated to a GPIO.

To perform association following command shall be used:
```
	dfb.setGpioId(0);
	dfb.update(superA);
```
## Logic Function
Digital function blocks implement an arbitrary combinatory logic on four inputs and one output. There are several common logic already predefined like AND, OR, NAND, NOR, XOR. But any combinatory logic can be implemented based on the expected true table. The logic function is represented by a 4 digit hex string.

This figure reports how to create the string based on the true table.

Predefined logics are the following:
```
	public static final String SE_DIGITAL_FUNCTION_BLOCK_AND_4_INPUT = "0001";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_AND_3_INPUT = "0101";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_AND_2_INPUT = "1111";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_OR = "7FFF";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_XOR = "6666";
	
	public static final String SE_DIGITAL_FUNCTION_BLOCK_NAND_4_INPUT = "FFFE";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_NAND_3_INPUT = "FEFE";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_NAND_2_INPUT = "EEEE";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_NOR = "1000";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_XNOR = "9999";
	
	public static final String SE_DIGITAL_FUNCTION_BLOCK_SET = "FFFF";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_RESET = "0000";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_IDENTITY = "5555";
	public static final String SE_DIGITAL_FUNCTION_BLOCK_NOT = "AAAA";
```

## Pulsed Output
Digital Function Block can be bistable (On or Off) or monostable (Plused). In case output is bistable then the outptu stay stable at output status as defined by the output of the logical function block. In case it is pulsed then the ouput stay stable at Off if the logical function output is output otherwise the output is a pulse. The pulse stay high for all puls duration time even if the digital function block out turns to low.

Pulse duration can be programmed in this way.
```
	dfb.setPulse(3000);
	dfb.update(superA);
```

## Delay On and Delay Off
When output of digital function block change from on to off and viceversa it is possible to introduce a transition delay that delays the output transition.
For example Delay On delays the transition of the output from off to On. Delay off delays transition from on to Off. In case the output fo digital function block output changes during this delay this change is not considered until Delay is not over.

In this example Delay On and Off is used to create a Multivibrator Astable that changes output to on from off every 3 seconds.
```
	DigitalFunctionBlock dfb = new DigitalFunctionBlock(superA);
	dfb.setInputId(dfb.getObjectId(), 0);
	dfb.setFunction(DigitalFunctionBlock.SE_DIGITAL_FUNCTION_BLOCK_NOT);
	dfb.setOnDelay(3000);
	dfb.setOffDelay(3000);
	dfb.setGpio("00");
	dfb.update(superA);
	
		// restart the device and make the logic running
	thisDevice.systemReset(pin, superA, true);
```

## Reset
Digital function blocks have also a input for reset. If this input is true then the output of digital function block is reset. In order to associate the reset input with a function block output call below methods. Reset command have priority over all the others including delays.

```
	dfb.setResetObjectId(door1.getObjectId());
	dfb.update(superA);
```

## Digital Function Block Activation Status
Digital Function Blocks can be in one of the two possible activation states: enabled or disabled. If digital function block is disabled it is not providing other output than zero.

To set digital function block activation status use below commands.
To disable:
```                      
	dfb.setStatus(Sensor.SE_DIGITAL_FUNCTION_BLOCK_STATUS_INACTIVE);
	dfb.update(superAdmin);
```
To enable a disable object:
```
	dfb.setStatus(Sensor.SE_DIGITAL_FUNCTION_BLOCK_STATUS_ACTIVE);
	dfb.update(superAdmin);
```
## Access Control Policy
It is possible to associate an Access Policy (Weekly Policy wiht start and espiration time) object to a digital function block in order to allow output of digital function block to change to true only when the Access Policy is allowing.

## Delete
Super Administrator can delete a digital function block instance into the device by calling relevant delete method. When a digital function block instance is deleted from the device memory is erased, collected and can be reused for other objects.
```
	dfb.delete(superA);
```
