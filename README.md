# DETROIT
The Frontend Android Application

## Deployment
Use the latest Android Studio to build the project. Some variables have to be initialized first before the app can be compiled.

* **String OPENIVN** in MainActivity.java, Recorder.java, AppDisplay.java, Permissions.java, RawView.java
* **String OPENIVN_IP** in Recorder.java
* **String API_KEY** in MainActivity.java, Recorder.java, AppDisplay.java, Permissions.java, RawView.java
* **String VIN** in MainActivity.java if getVIN() not supported for your dongle or vehicle

Please make sure to remove "reverse-engineering-traces" in the app data folder before re-running the reverse engineering process for another vehicle.

## Reverse Engineering Process
The app can collect data for a CAN bus reverse engineering framework. We used LibreCAN (not included in OpenIVN repository due to IP reasons) that requires two categories of data:

* **Kinematic-related Data:** Use an OBD-II dongle (e.g., ELM327) besides your raw CAN dongle (e.g., OpenXC) to record both OBD-II and CAN data. A Y-cable has to be used to record both at the same time. We used "Torque Pro" from the Google Play Store to record OBD-II PIDs. Line 49 in RawView.java specifies the folder where "Torque Pro" writes its logs. Please change the path if another OBD-II app is used.
* **Body-related Data:** We record 53 body-related events in compliance with LibreCAN. Adjust the desired events in Lines 62ff. in RawView.java.
