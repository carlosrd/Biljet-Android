# BiljetApp for Android
The following is short description about this directory

## General Description

BiljetApp is the mobile extension for the web site: Biljet. This site provides services and tools to manage events and control the access to them with systems as QR codes

This Open Source (GNU License) project is being developed by a group of students from Universidad Complutense de Madrid for Ingeniería del Software (Software Enginnering) subject

## General Requirements

You'll need a few additional software to compile/run the project

* [Eclipse IDE](http://www.eclipse.org/downloads/packages/eclipse-classic-422/junosr2)
 Available in different packages (Helios, Indigo, Juno, ...) and versions (Windows x86/64, Mac, ...)

* [Android SDK Manager](http://developer.android.com/sdk/index.html)
 Revision 22 + Android 2.3.3 `(API 10)` + ADT Plug-in v22. Now it's also possible to get both (SDK + ADT Plugin + Eclipse) at same time 

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
 Java Development Kit 1.6 or greater to build Java and Android apps. 
 
## BiljetApp 

This project contains BiljetApp Android principal application. The following sections are finally implemented;

* **SignUp** : Shows a form to register an user at Biljet system
* **Login** : Shows a form to let the user to enter its username and password in order to start a session
* **Index** : It's the first view after login view. It shows direct access to the most important options for Biljet users: Discover, My Events and My Calendar
* **Help** : Displays information about how the app works in order to teach the user to use it
* **Discover** : In this view, you can see the latest events created (Event View). In this section is possible to search events
* **Search** : It lets the user find events by title, province, category or by combining some of them
* **My Events** : In this area you can follow events organized by yourself or events for which you have a pass to get on. In first case of event type you can launch the QR reader to validate the invitations of your guests. In second case you can see a QR code that will allow you entry to the event. Both cases shows data relative to the event such as date, time, place with maps (where available), 
* **Event View** : Shows total information for an event: Title, image, description, place, maps (where available), duration, price, etc... A button can appear next to the event image that has differents behaviours depending how the user reach the event:
	* From Discover: This button lets the user get tickets (a QR code) to go to the event.
	* From MyEvents: This button lets the user show its own ticket (QR code) for the event when it is reached at "to go" list. However, this button lets to launch the QR Reader to validate guests if the event was reached at "created" list
* **New Event** : Shows a form to fill in order to create a new event. User has to provide all information required for a correct creation.
* **My Profile** : This view let the user check its own data
* **Calendar** : Shows a Calendar to manage the events more easily by date.
* **Day View** : When the user click a day on the calendar, this view will be launched to display its own events detailed on the chosen date
	
## CaptureActivity

This is the library needed to capture and decode QR codes within our own project. This auxiliary project has been developed by the [zXing Team](http://code.google.com/p/zxing/) and it has been embedded as a native QR decoder of our app. Some internal changes were added to adjust the decoder to our needs like change the focus area of rectangular to square. Texts on capturer have been translated and been rotated to simulate a portrait view

## ActionBar

This is the library that allows us to easily configure the appearance and actions of the Action Bar that is displayed on top of all activities. This provide us an API to add/remove Actions and show/hide a Progress Bar. All the changes are make directly on BiljetApp to overriding the original settings

## *ghost* folder
Contains a relation of files such as Java classes, XML files, Android Activities and other files like drawables that have been removed from the final version of May 2013. This files are maintained because are already finished or it lays the groundwork for subsequent iterations of the project not reached (We'd need one more year to establish the social network for example). These are:
- Implement friends section and all its functionality (Biljet as social network)
- Implement notification system
- Implement preferences in the app (to change user data, connect with other social networks (Facebook / Twitter) and managing the way the user wanted to receive notifications

## Disclaimer
The software is provided 'as is', without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no ecent shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
