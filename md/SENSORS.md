# Sensors

### Sensors provide measures

---

Sensors are digital function blocks designed for telemetry. They allow users to get remotly measures collected from sensors connected to the board.
 
## Instantiation
Only super administrator can instantiate and configure sensors. 
Below commands create an instance of a sensor object on the device and create also a local object instance.
Commands set also fileds value. Set methods operates on local object fields only. In order to propagate changes to remote device the update method shall be invoked.
```
	Sensor = new Sensor(superA, Sensor.SENSOR_TYPE_GPIO, true); 
	sensor.syncroFields(superA);
	sensor.setType( Sensor.SENSOR_TYPE_GPIO);
	sensor.setStatus(Sensor.SENSOR_STATUS_ENABLED);
	sensor.setGpioId(0);
	sensor.update(superA);
```

To create only local instance of a door object that is mirroring the remote object into the device given object Id use below command.
```
	Sensor sensor = new  Door(user, sensorId)
```

## GPIO association
Sensor shall be associated to a GPIO.

To associate a GPIO with a door following command shall be used:
```
	sensor.setGpioId(0);
	sensor.update(superA);
```
More sensor objects can be associated with same GPIO.

## Sensor Types
Sensors can be configured based on what shall be measured. Following sensor type are available. Additional sensors can be defined based on the board. 

- SENSOR_TYPE_GPIO: read value of a digital input (on or off).
- SENSOR_ANALOG_TYPE: read the voltage on the input.
- SENSOER_ANALOG_VCC_TYPE: read the value of device Vcc.
- SENSOR_CHIP_TEMPERATURE_TYPE: read the value of chip demperature in celsius degree.
- SENSOR_VBATT_TYPE: Read the voltage of Real time clock battery,
- SENSOR_OHM_TYPE: Read the value of resistors on the input.

To set sensor type
```
 sensor.setType(SENSOR.SENSOR_TYPE_GPIO);
 
 sensor.update(superA);
  
 type = door.getMode(superA);
```
Sensor type SENSOR_TYPE_GPIO measure voltage on the input and convert it to a digital value that can be on or off. Conversion is based on a [Schmitt trigger](https://en.wikipedia.org/wiki/Schmitt_trigger) comparator.

To set thresholds value use the following methods. In input to the methods a voltage value measured in millivolts shall be passed.
```
   sensor.setHighThreshold(8000);
   sensor.setLowThreshold(6000);
   
   sensor.update();
```

## Read Sensor Value
To read sensor value just call getMeasure() methods. This methods connect to device and measure value from relevant input. So this method invlolve io activities.

```
	telemetry = sensor.getMeasure(admin);

		// check telemetry
	if (!( telemetry.equals(Sensor.SENSOR_VALUE_ON) ))
	{
		throw new TestException();
	}
```

In case there is no need to get latest value measured by the device without permorming any connection with the device then getLastMeasure() method can be invoked. Information provided by this method could not be realistic since it provides latest information acquired from the device and do not start any communication session with the device.

```
	telemetry = sensor.getLastMeasure();(admin);

		// check telemetry
	if (!( telemetry.equals(Sensor.SENSOR_VALUE_ON) ))
	{
		throw new TestException();
	}
```

## Sensor Activation Status
Sensors can be in one of the two possible activation status: enabled or disabled. If sensor is disabled it is not possible to read input status.

To set sensor activation status use below commands.
To disable:
```                      
	sensor.setStatus(Sensor.SENSOR_STATUS_DISABLED);
	sensor.update(superAdmin);
```
To enable a disable object:
```
	sensor.setStatus(Sensor.SENSOR_STATUS_Enabled);
	sensor.update(superAdmin);
```

## Delete
Super Administrator can delete a sensor instance into the device by calling relevant delete method. When a sensor instance is deleted from the device memory is erased, collected and can be reused for other objects.
```
	sensor.delete(superA);
```
