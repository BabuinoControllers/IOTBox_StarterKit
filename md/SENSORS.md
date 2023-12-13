# Sensors

### Sensors provide measures

---

Sensors are functional blocks designed for telemetry. They allow users to remotely collect measurements from sensors connected to the board.
 
## Instantiation
Only Super Administrator can instantiate and configure a Sensor.
The command below creates an instance of a Sensor on the device, and its local copy in the application, which is stored in the 'sensor' variable.
Subsequent commands set fields in the Sensor. These only operate on the local copy, therefore in order to propagate changes to the remote device, the update method shall be invoked.
```
	Sensor sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true); 
	sensor.syncroFields(superA);
	sensor.setType( Sensor.SENSOR_TYPE_GPIO);
	sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
	sensor.setGpioId(0);
	sensor.update(superA);
```

To create local instance of a Sensor from object ID use the command below. This local instance is mirroring the remote object stored into the device, of which the object ID had been kept track of.
```
	Sensor sensor = new  Door(user, sensorId)
```

## GPIO association
A sensor shall be associated to a GPIO input port only, corresponding to the physical inputs on the device board. (IDs 0-5).

To associate a GPIO with a sensor, the following command shall be used:
```
	sensor.setGpioId(0);
	sensor.update(superA);
```
More sensor objects can be associated with the same input, for example measuring both voltage and resistance.

## Sensor Types
Sensors can be configured based on what shall be measured. The following sensor types are available:

- SENSOR_TYPE_GPIO: read value of a digital input (on or off).
- SENSOR_ANALOG_TYPE: read the voltage on the input.
- SENSOER_ANALOG_VCC_TYPE: read the value of device Vcc.
- SENSOR_CHIP_TEMPERATURE_TYPE: read the value of chip temperature in Celsius degrees.
- SENSOR_VBATT_TYPE: Read the voltage of Real Time Clock battery,
- SENSOR_OHM_TYPE: Read the value of resistor on the input.

To set sensor type:
```
 sensor.setType(SENSOR.SENSOR_TYPE_GPIO);
 
 sensor.update(superA);
  
 type = door.getMode(superA);
```
Sensor type SENSOR_TYPE_GPIO measures voltage on the input and converts it to a digital value that can be high or low. Conversion is based on a [Schmitt trigger](https://en.wikipedia.org/wiki/Schmitt_trigger) comparator.

To set threshold values use the following methods. As input to the methods a voltage value measured in millivolts shall be passed.
```
   sensor.setHighThreshold(8000);
   sensor.setLowThreshold(6000);
   
   sensor.update();
```

## Read Sensor Value
To read sensor value just call getMeasure() methods. This method connects to device and measures the value from the corresponding input. So this method involves IO activities over network.

```
	telemetry = sensor.getMeasure(admin);

		// check telemetry
	if (!( telemetry.equals(Sensor.SENSOR_VALUE_ON) ))
	{
		throw new TestException();
	}
```

In case there is no need to get an instantaneously accurate measurement, then getLastMeasure() method can be invoked. Information provided by this method could not be accurate since it provides the last information acquired from the device and does not start any communication session with the device.

```
	telemetry = sensor.getLastMeasure();(admin);

		// check telemetry
	if (!( telemetry.equals(Sensor.SENSOR_VALUE_ON) ))
	{
		throw new TestException();
	}
```

## Sensor Activation Status
Sensors can be in one of two possible activation status: enabled or disabled. If sensor is disabled it is not possible to read its value.

To set sensor activation status use the commands below:
To disable:
```                      
	sensor.setStatus(Sensor.SENSOR_STATUS_DISABLED);
	sensor.update(superAdmin);
```
To enable a disabled object:
```
	sensor.setStatus(Sensor.SENSOR_STATUS_Enabled);
	sensor.update(superAdmin);
```

## Delete
Super Administrator can delete a sensor instance into the device by calling the corresponding delete() method. When a sensor instance is deleted from the device, memory is erased, collected and can be reused for other objects.
```
	sensor.delete(superA);
```
