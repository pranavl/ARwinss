#ARwinss
Development of a tracking system on an Android platform using the ARToolKit Augmented Reality Library.

##Synopsis
This project uses the ARToolKit library and OpenIGTLink protocol to provide marker tracking capabilities 
and two-way transfer between the headset and a client device.

##Motivation
The goal of this project is to develop a head mounted display (HMD) for surgeons to use as a navigational aid during a procedure.
The Wearable Intelligent Navigation System for Surgery (WINSS) Augmented Reality (AR) system allows for image guided surgery without the need to occupy a surgeon’s hands or eyes. 
Rather than providing high-resolution preoperative images, the HMD will show derived graphics and models, 
such as tumor outlines and target points in a “picture in picture” manner, to assist in navigation. 
By using the AR device in conjunction with other sensors for optical tracking and inertial sensing, 
the system can operate even when markers on the patient frame are occluded from the view of a tracker.  

##Installation and Testing
This project uses a [modified version of the ARBaseLib package](https://github.com/pranavl/ARwinss) of ARToolKit.
After compiling the ARBaseLib package, this project can be compiled as an Android Application Project.

The Android version of the ARToolKit SDK is implemented using a Java wrapper around native C++ code, compiled using the Java Native Interface (JNI).

The application was built and tested on the [Epson Moverio BT-200 smart glasses](http://www.epson.com/cgi-bin/Store/jsp/Landing/moverio-bt-200-smart-glasses.do?ref=van_moverio_2014).

##Code Example


##API Reference

####ARToolKit
This project makes use of a [modified version of the ARToolKit Augmented Reality Library](https://github.com/pranavl/ARwinss). 
The original library can be found at http://artoolkit.org/ with the [original documentation](http://artoolkit.org/documentation/).

####OpenIGTLink
This project also uses the [OpenIGTLink protocol](http://openigtlink.org/), 
specifically, the [Java implementation](https://code.google.com/p/igtlink4j/) of this library (Copyright (c) 2010, Absynt Technologies).
The source was compiled into `JOpenIGT.jar` for use as a class library in the project.

##Contributors
This project was made possible with support from Dr. Peter Kazanzides and Dr. Sungmin Kim
of the Laboratory of Computational Sensing and Robotics at Johns Hopkins University.
