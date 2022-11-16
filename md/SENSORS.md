# Sensors

### Sensors provide measures

---

Sensors are digital function blocks designed for telemetry. They allow users to get remotly measures collected from sensors connected to the board.
 
## Instantiation
Super Administrators can instantiate and configure sensors. 
Beloow commands creates an instance of a sensor object on the device and relevant local instance and set changes to fields. Set methods only change the status of field of local object. In order to propagate changes on remote device an update method shall be invoked.

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

## Read Sensor Value
To read sensor value just call getMeasure() methods.
```
	telemetry = sensor.getMeasure(admin);

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
Super Administrator can delete a sensor instance into the device by calling relevant delete method.When a sensor instance is deleted from the device memory is erased, collected and can be reused for other objects.
```
	sensor.delete(superA);
```
