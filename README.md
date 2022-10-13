# IOT Brick Starter Kit

### The IOT solution that is ready when you are

---

This is the starter kit of IOTBrick - BR001 the IOT solution that provides a general purpose secure unit usable in wide range of applications. Starter kit
is designed for java applications and includes software libraries as JAR files that simplifies the communication and interaction with IOT Brick devices. It include alaso an example project, API specifications of the libriaries as a javadoc and a datasheet of IOTBrick - BR001.

## About IOT Brick
IOT Brick - BR001 is a secure unit designed as general-purpose IOT device by [Babuino Controllers](https://babuinocontrollers.com) that simplifies the design, the implementation and the deployment of IOT solutions on the field minimizing the time to market. IOT Brick do not implement application logic. Application logic shall be configured according to the application requirements just by connecting together several functional blocks. Functional blocks are basic blocks that transform inputs into outputs following specific configurable function. Connecting togheter the input and the output of functional blocks it is ipossible to implement a wide range of application logic.
Without any hardware development and firmware programming it is possible to implement a large variety of applications and use cases just configuring the functional blocks provided by the device. So, development moves from hardware and firmware development to logic configuration.
IOT Brick is a connected object that can be controlled remotely by any device or application including app on smartphone or other smart devices. Each unit comes with six configurable inputs and four outputs. Input can be configured to be analog and digital. Number of inputs and outputs can be extended by connecting additional units via RS485 and Ethernet. To simplify the integration in operating environment and applications an SDK (Software development kit) for desktop, server and mobile (Android and IOS) is available. 
IOTBricks logic can also be changed when the device is deployed on the field just by sendind the device the new configuration.
To buy IOT Brick device [contact us](https://www.babuinocontrollers.com/contacts/).
More details about IOT BRick - BR001 are in the [datesheet] ().

## Why IOT Brick?
Most IOT projects fails for many reasons: 
- Businness objective not well designed
- Technological complexity and missing know how
- Development costs including hardware and software
- Industrialization costs

On the other side most of IOT projects share common requirements. IOT Brick technology try to share development, implementation and industrialization efforts of one projects with others by providing a general purpose device covering a wide range of applciations that can be simply integrated in complex IOT architecture.

## How to start
Starter kit includes a project for NetBeans version 8.2. So install NetBeans version 8.2 before starting.
After just clone locally latest stable version of the Starter Kit Project, launch NetBeans and open the project.
Power on IOT Brick - BR001 and connect to the lan. Ethernet led start blinking. Also device system blu led start blinking reporting activity of device. If the network is connected to the internet the blue led should alternate two fast blink to a long pasue. This means that the device is connected to the server.
Note: it is not mandatory required that the device connect to the server. Device can also controllerd over the LAN.

There is no need to configure any IP address. The device implements a discovery protocol that allow to discover devices connected to the network. Only configuration required is to copy in the main file the serial number of the device and the initial password of the device.

## Library
Library as JAR file are in the library folder. Library provides a cohmprensive set of objects that mirrors objects in the device. By interacting with these objects it is possible to interact remotely with the device.
To use the library in a new project just import the JAR file in the project and start developing code. Download the [JAR File from this link]()

Java doc is available [here]().

## Troubleshooting

Please [reach out](https://www.babuinocontrollers.com/contacts/) to the Babuino Controllers team with any technical issues or questions about the Github integration. We're happy to help!
