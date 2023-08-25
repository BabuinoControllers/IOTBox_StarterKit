# Digital Function Block

### Combinatory logic on four inputs and one output

---

Digital function blocks implement an arbitrary and programmable combinatory logic on 4 inputs and one output.
 
## Instantiation
Only Super Administrator can instantiate and configure DFBs. 
The command below creates an instance of a DFB on the device, and its mirror copy in the application, which is stored in the 'dfb' variable.
Subsequent commands set fields in the DFB. These only operate on the local copy, therefore in order to propagate changes to the remote device, the update method shall be invoked.

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

IMPORTANT: in order to run logic into device, device shall be reset (restarted) either manually or programmatically.

To create local instance of a DFB from object ID use the command below. This local instance is mirroring the remote object stored into the device, of which the object ID had been kept track of.
```
	DigitalFunctionBlock dfb = new DigitalFunctionBlock(user,objectId);
```
## Input 
To connect the input of a DFB to the output of another functional block, provide setInputId with the object ID of the output block, and the input ID (0-3).

``` 
	dfb.setInputId(door.getObjectId(), 0);
	dfb.setInputId(door1.getObjectId(), 1);
	dfb.setInputId(door2.getObjectId(), 2);
	dfb.setInputId(door3.getObjectId(), 3);
	dfb.update(superA);
```

## GPIO association
Digital function block output can be associated to a digital output GPIO.

To perform association, the following command shall be used:
```
	dfb.setGpioId(0);
	dfb.update(superA);
```
## Logic Function
Digital function blocks implement an arbitrary combinatory logic on four inputs and one output. There are several common logics already predefined like AND, OR, NAND, NOR, XOR. But any combinatory logic can be implemented based on the desired truth table. The logic function is represented by a 4 digit hex string representing the binary value of the output column.

This figure reports how to create the string based on the truth table:

<p align="center">
  <br />
  <img src="/img/dfb_truth_table.PNG" width="800px" alt="Truth table conversion" >
  <br />
</p>

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
Digital Function Blocks can be bistable (On-Off) or monostable (Pulsed). In case output is bistable, it will follow every variation to the logical output. In case it is pulsed, the logical output transitioning to '1' triggers a pulse, which stays high for all the set pulse duration time even if the logical output turns to '0'.

Pulse duration can be programmed in this way:
```
	dfb.setPulse(3000);
	dfb.update(superA);
```
A pulse duration of '0' sets the DFB back to bistable behavior.

## Delay On and Delay Off
It is possible to introduce a transition delay for either of the ON->OFF (setOffDelay) or the OFF->ON (setOnDelay) transitions. 
In case the output of the digital function changes during this delay, the change is only considered after the delay is over.

In this example Delay On and Off is used to create an Astable Multivibrator that toggles output every 3 seconds.
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
Digital function blocks also have an input for reset. If this input is true, then the output of the digital function block is reset. In order to associate the reset input with a functional block output, call the method below. Reset command takes priority over all the others including delays.

```
	dfb.setResetObjectId(door1.getObjectId());
	dfb.update(superA);
```

## Digital Function Block Activation Status
Digital Function Blocks can be in one of two possible activation states: enabled or disabled. If digital function block is disabled it will not provide other output than zero.

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
It is possible to associate an Access Policy (Weekly Policy with start and expiration time) object to a DFB in order to allow its output to change to true only in the set time intervals.

## Delete
Super Administrator can delete a DFB instance from the device by calling the relevant delete() method. When a DFB is deleted from the device, memory is erased, collected and can be reused for other objects.
```
	dfb.delete(superA);
```
