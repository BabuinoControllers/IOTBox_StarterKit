# Objects

### In IOT Brick Everything is an object

---
IOT Brick operative logic follows an object oriented paradigm. Everthing in is an object in IOT Brick with fields and methods. Users, digital function blocks, access policies and all the other entities in the device are treated as objecs. Each object is based on a class prototype and have its fields and methods different from each other. There are some fileds and methods in common among all the object classes. They are reproted in the following.

## Mirroring and method invocation
Method invocation of an IOT Brick object is performed remotely by invoking a method on a local object instance.
SDK defines classes that mirrors classes of objects into IOT Brick. This classes provides all the support to invoke remotely methods of objecs stored into IOT Brick.

First Local object shall be instantiated. Then methods on local objec shall be called. This methods manage communication over connectivity channel so to invoke relevant method on the object stored on the device.

So an object has two instances. One instance off device locally to a mobile phone or to computer and a mirrored object on the device. By interacting with the local object it is possible to interact transparently with the mirrored object. Communication is enterly managed by SDK wothout any action of programmer.

This is the code that shall be used in order to create a door function block, set some fields and switch ON the
```          
	door = new Door(superA);                        
	door.setGpioId("00");                      
	door.setStatus(Door.SE_DOOR_ENABLED);
	door.update(superA);
	door.syncroFields(superA);
	   
	door.setOutput(superA, Door.DOOR_COMMAND_SWITCH_ON);
```

## Object identifier
Object identifier or in short object ID is an unique identifier of the object. It is assigned authomatically by IOT Brick operating system at object creation and is a string of 8 hex digits.

For example the string "00000003" is an object identifier.

In order to get the object identifier from an object
```
	objectName.getObjectId();
```

## Object name
Each object has a name. Name is a string of maximum 32 bytes. Initial value of object name after object instantiation is the object Id.

Use the set and get methods in order to set and get object name.
```
	objectName.getName();
	objectName.setName("Name");
```

## Object Synchronization
In order to syncronize the value of fields of local object instance with the filed of mirrored object insctancce stored into the device syncroFields method shall be called. Handler of requesting user shall be passed in input to syncroFields method.

```
	objectName.syncroFields(userObjectHandler);
```

## Object Update
Each time a field of a local object is set with a new value relevant field of mirrored object into the device is not updated authomatically. In order to operate the update of relevant field into mirrored object on the device update method shall be called passing as input the handler of the local user object that is pushing the change.
Since update involve communication over channel it is reccommended to perform all the needed set on the object before pushing the update into mirrored object into the device.

```
	objectName.update(userObjectHandler);
```

## Object Deletion
To delete an object instance on the device it is needed to call delete method on relevant local object. This action delete the object on the device. All space allocated for the object on the device is erased and memory is recollected and usable for new object allocation.
Delete command delete only object instance on the device but it is not deleting local object instance.
Only users with priviledge like administrators and super administrator can delete an object. An exception is returned in case user has not the permission to delete the object. 

```
	objectName.delete(userObjectHandler);
```
