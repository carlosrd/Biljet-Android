# BiljetApp for Android
The following is short description about this directory

## General Description

BiljetApp is the mobile extension for the web site: Biljet. This site provides services and tools to manage events and control the access to them with systems as QR codes

This project is being developed by a group of students from Universidad Complutense de Madrid for Ingeniería del Software (Software Enginnering) 
## General Requirements

You'll need a few additional software to compile/run the project

* [Eclipse IDE](http://www.eclipse.org/downloads/packages/eclipse-classic-422/junosr2)
 Available in different packages (Helios, Indigo, Juno, ...) and versions (Windows x86/64, Mac, ...)

* [Android SDK Manager](http://developer.android.com/sdk/index.html)
 Revision 21.1 + Android 2.3.3 `(API 10)` + ADT Plug-in. Now it's also possible to get both (SDK + Eclipse) at same time 

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
 Java Development Kit to build Java and Android apps.
 
## MenuActivity

This project contains BiljetApp Android principal application. The following sections are almost implemented (pendant to improve GUI)

* **Menu** : It's the first view after login view. It shows direct access to the most important options for Biljet users
* **Upcoming** Events : In this view, you can see which events are going to be celebrate soon (Event View). 
* **My Events** : In this area you can follow events organized by yourself or events for which you have a pass to get on. In first case of event type you can launch the QR reader to process the invitations of your guests. In second case you can see a QR code that will allow you entry to the event. Both cases shows data relative to the event such as date, time, place, etc
* **New Event** : Shows a form to fill in order to create a new event. User has to provide all information required for a correct creation. This view changes depending on the type of user (regular or company) which it's using the app
* **My Friends** : In this activity resides the social section. Here you can manage (add/delete) friends (other Biljet users) to your account and watch their public profiles with their personal data (Friend View). A search action will be available for this section
* **My Profile** : This view let the user check its own data and update them
	
## CaptureActivity

This is the library needed to capture and decode QR codes within our own project. This auxiliary project has been developed by the [zXing Team](http://code.google.com/p/zxing/) and it has been embedded as a native QR decoder of our app. Some internal changes were added to adjust the decoder to our needs like change the focus area of rectangular to square. Texts on capturer have been translated and been rotated to simulate a portrait view

## Features to be added

* Search action in `My Friends` and `Upcoming Events`
* Login access
* Internal data manager
* User settings view
* Calendar view (considering it)
