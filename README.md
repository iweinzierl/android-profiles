# android-profiles
Android application to create profiles for adjusting sound, wifi, etc settings easily

#### Features:
* create profiles for different scenarios (e.g. home, office, ...)
* create wifi trigger in order to activate a specific profile when connecting to a specific wifi
* manual activation of a profile

#### What is a Profile?
A profile is a set of settings like sound settings, wifi, bluetooth, gps, etc. Currently, a profile consists of the following properties:
* ringtone volume
* notification volume
* alarm volume
* media volume
* ringtone mode (normal, silent, vibrate)

#### What is a Trigger?
A trigger is used to activate a specific profile automatically by detecting changes in the environment (e.g. a connection to a wifi is established). Currently, there are the following trigger types implemented:
* wifi connection established
