# IOT Brick Starter Kit

### The IOT solution ready when you are

---

This is the starter kit of IOTBrick - BR001 the IOT solution that provides a general purpose secure unit usable in wide range of applications. Starter kit
is designed for java applications and includes software libraries as JAR files that simplifies the communication and interaction with IOT Brick devices. It include alaso an example project, API specifications of the libriaries as a javadoc and a datasheet of IOTBrick - BR001.

## About IOT Brick
IOT Brick - BR001 is a secure unit designed as general-purpose IOT device by [Babuino Controllers](https://babuinocontrollers.com) that simplifies the design, the implementation and the deployment of IOT solutions on the field minimizing the time to market. IOT Brick do not implement application logic. Application logic shall be configured according to the application requirements just by connecting together several functional blocks. Functional blocks are basic blocks that transform inputs into outputs following specific configurable function. Connecting togheter the input and the output of functional blocks it is ipossible to implement a wide range of application logic.
Without any hardware development and firmware programming it is possible to implement a large variety of applications and use cases just configuring the functional blocks provided by the device. So, development moves from hardware and firmware development to logic configuration.
IOT Brick is a connected object that can be controlled remotely by any device or application including app on smartphone or other smart devices. Each unit comes with six configurable inputs and four outputs. Input can be configured to be analog and digital. Number of inputs and outputs can be extended by connecting additional units via RS485 and Ethernet. To simplify the integration in operating environment and applications an SDK (Software development kit) for desktop, server and mobile (Android and IOS) is available. 
IOTBricks logic can also be changed when the device is deployed on the field just by sendind the device the new configuration.
To buy IOT Brick device [contact us](https://www.babuinocontrollers.com/contacts/).
More details about IOT BRick - BR001 are in the [datesheet](https://github.com/BabuinoControllers/IOTBrick_StarterKit/raw/main/doc/20210929_BR001V1_EN.pdf).

<p align="center">
  <br />
  <img src="https://github.com/BabuinoControllers/IOTBrick_StarterKit/raw/main/doc/IOT_Brick_BR001.jpg" width="300px" alt="IOT Brick - BR001" >
  <br />
</p>

## Why IOT Brick?
Most IOT projects fails for many reasons: 
- Businness objective not well designed
- Technological complexity and missing know how
- Development costs including hardware and software
- Industrialization costs

On the other side most of IOT projects share common requirements. IOT Brick technology try to share development, implementation and industrialization efforts of one projects with others by providing a general purpose device covering a wide range of applciations that can be simply integrated in complex IOT architecture.

## From Development to configuration
Application logic can be easily implemented by configuring and combining the functional blocks into the device. Functional blocks are studied to support the implementation of a wide range of use cases and applications. Functional blocks have inputs and outputs. Logic is created by instantiating a functional block and connecting the output of a functional block to the input of the others. Several types of functional blocks are available. Follow a short summary:

- [Sensor](md/SENSORS.md) block: used to read the value of an input or an output of the board or of others functional block. Input can be configured to read analog values (only from board input) and can implement a Schmitt Trigger whose thresholds are fully configurable. Sensor blocks can also be read remotely by users.

- [Switch](md/DOORS.md) block: it is a switch that can be controlled remotely by user. It can be configured as monostable (pulse) or bistable (on/off or toggle). Activation of the switch is conditional to a weekly time policy defined by administrator.

- [Door](md/DOORS.md) block: it is like a switch block but connected to relays driving device outputs. 
 
- Mealy Machine: finite State Machine based on a Mealy model with max 8 inputs, 4 outputs and 16 states. Activity of the machine can be conditioned by means of a weekly time policy defined by administrator. Outputs of the block can change its status only if the weekly time policy reports that the machine is active.

- [Digital Function Block](md/DFB.md): implementing any combinatory logic on 4 inputs. Output can be configured as monostable (Pulse) or bistable. Moreover, transition from one logical level to the other can be delayed by configuring the function block. Block is active conditionally to a weekly timed policy.

- Weekly policy block: It defines a weekly time policy. Time policy is regulated by real time clock (RTC) operating in the board. Some of the functional block activities are regulated by the weekly time policy. Access control policy has a validity time defined by a starting time and an expiration time. Group access policy can be defined per user.

<p align="center">
  <br />
  <img src="https://github.com/BabuinoControllers/IOTBrick_StarterKit/raw/main/doc/IOT Brick Architecture.png" width="800px" alt="IOT Brick Architecture" >
  <br />
</p>

## How to start
Starter kit includes a project for NetBeans version 8.2. So install NetBeans version 8.2 before starting.
After just clone locally latest stable version of the Starter Kit Project, launch NetBeans and open the project.
Power on IOT Brick - BR001 and connect to the lan. Ethernet led start blinking. Also device system blu led start blinking reporting activity of device. If the network is connected to the internet the blue led should alternate two fast blink to a long pasue. This means that the device is connected to the server.
Note: it is not mandatory required that the device connect to the server. Device can also controllerd over the LAN.

There is no need to configure any IP address. The device implements a discovery protocol that allow applications to discover devices connected to the network. Only configuration required is to copy in the main file the serial number of the device and the initial password of the device.

## System Reset and Factory Reset
Device has a factory rest. Pressing reset button for more than 5s cause a system reset. Pressing for more than 15s cause a factory reset. Factory reset delete all the information from the non-volatile memory of the device.

## Library
Library as JAR file are in the library folder. Library provides a cohmprensive set of objects that mirrors objects in the device. By interacting with these objects it is possible to interact remotely with the device.
To use the library in a new project just import the JAR file in the project and start developing code. Download the [JAR File from this link](https://github.com/BabuinoControllers/IOTBrick_StarterKit/raw/main/lib/SDK_IOTBrick.jar)

Library can be easly included also in Android Projects. If you need library for IOS device please [reach out](https://www.babuinocontrollers.com/contacts/).

Java doc is available [here](https://github.com/BabuinoControllers/IOTBrick_StarterKit/raw/main/doc/javadoc.zip).

## Objects
On IOT Brick everything is an [object](md/OBJECT.md). Users are objects, digital funcion blocks are object, device itself is considered as an object. Objects share some common behaviour.

[Learn more about objects.](md/OBJECTS.md)

## Users
Users can monitor remotely the application and perform actions. Before any command sent by user is executed user is authenticated. Only authenticated users can perform an action. User authentication is based on a key and additionally for some blocks like switch block or door block also by means of a PIN.
Three different roles can be assigned to users:
- Super Administrator with full access to all the features of the device including hardware
settings and log analysis.

- Administrator able to create, delete users and define access policies.

- User allowed to open close the output based on the policy defined by the administrators.

On average 250 users can be registered with the system, Effective number of users depends on the settings. Each user has its own key used to guarantee end to end security.

[Here find more about users.](md/USERS.md)

## Connecting to IOTBrick
[Here find more about connecting users.](md/CONNECTING.md)

## Security
Communication between devices and administrator/user entities is protected by end-to-end security. Communication protocol creates a secure tunnel between users and Iot Brick that protect exchanged data guaranteeing authenticity confidentiality and integrity. User are authenticated at each and every command by the end-to-end protocol. For some specific commands it is possible to enforce an additional level of user authentication through a PIN.

## Troubleshooting

Please [reach out](https://www.babuinocontrollers.com/contacts/) to the Babuino Controllers team with any technical issues or questions about the Github integration. We're happy to help!
