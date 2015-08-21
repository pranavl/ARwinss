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
This application provides an example to track markers, render shapes with respect to these tracked positions, 
and transfer tranformation matrices via the OpenIGTLink protocol.

####`ARTracker`
The `ARTracker` activity is the main activity of this application. Here, UI changes can be made and threads are called. 
`ARTracker` contains an instance of the `TrackerRenderer` class, which is used to track specified markers and render models with respect to these markers.

####`TrackerRenderer`
The `TrackerRenderer` class is contains the methods necessary to configure an AR scene, where markers are tracked and models are rendered with respect to those markers.
This class is constructed with a reference to the `ARActivity` that created it, in this case `ARTracker`, to allow access to certain Activity-level methods.
Most important in this case is access to the `assets` folder, used to initalize markers and create models.

**Setting up new markers:**
```java
private int markerA = -1;

@Override
public boolean configureARScene() {
	markerA = ARToolKit.getInstance().addMarker("single;Data/multi/patt.a;80");
	return markerA >= 0;
}	
```
Here, `Data/multi/patt.a` is the filepath of the marker configuration, and `80` is the given size of the marker.

**Creating STL models from a file:**
```java
InputStream is = this.activity.getAssets().open("stl_file.stl");
STLSurface sur = new STLSurface(is);
```
Here, `this.activity` is a reference to the `ARTracker` activity that created this `TrackerRenderer`.

**Rendering objects with respect to a marker:**
```java
@Override
public void draw(GL10 gl) {

    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

    // Apply the ARToolKit projection matrix
    gl.glMatrixMode(GL10.GL_PROJECTION);
    gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);

	// Settings -- typically don't change
    gl.glEnable(GL10.GL_CULL_FACE);
    gl.glShadeModel(GL10.GL_SMOOTH);
    gl.glEnable(GL10.GL_DEPTH_TEST);
    gl.glFrontFace(GL10.GL_CW);

    // If the marker is visible, render the model
    if (ARToolKit.getInstance().queryMarkerVisible(markerA)) {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadMatrixf(ARToolKit.getInstance().
                queryMarkerTransformation(markerA), 0);
        sur.draw(gl)
    }
}	
```
It is possible to track multiple markers by repeating the `if-statement` with different markers and models.

##API Reference
All code added to these libraries is documented in `Javadoc` format.

####ARToolKit
This project makes use of a [modified version of the ARToolKit Augmented Reality Library](https://github.com/pranavl/ARwinss). 

The original library can be found at http://artoolkit.org/ with the [original documentation](http://artoolkit.org/documentation/).

####OpenIGTLink
This project also uses the [OpenIGTLink protocol](http://openigtlink.org/), 
specifically, the [Java implementation](https://code.google.com/p/igtlink4j/) of this library (Copyright (c) 2010, Absynt Technologies).
The source was compiled into `JOpenIGT.jar` for use as a class library in the project.

**Note:** Android does not support use of JAR's compiled with Java 1.8. To use this library, the Java source must be compiled using JDK 7.

##Contributors
This project was made possible with support from Dr. Peter Kazanzides and Dr. Sungmin Kim
of the Laboratory of Computational Sensing and Robotics at Johns Hopkins University.
