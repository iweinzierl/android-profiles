# android-profiles
Android application to create profiles for adjusting sound, wifi, etc settings easily

#### Features:
* create profiles for different scenarios (e.g. home, office, night, ...)
* create triggers to enable/disable specific profiles under certain conditions
* manual profile activation

#### What is a Profile?
A profile is a set of settings like sound settings, wifi, bluetooth, gps, etc. Currently, a profile consists of the following properties:
* ringtone volume
* notification volume
* alarm volume
* media volume
* ringtone mode (normal, silent, vibrate)

#### What is a Trigger?
A trigger is used to activate/deactivate a specific profile automatically by detecting changes in the environment (e.g. a connection to a wifi is established). Currently, the following trigger types are implemented:
* wifi -> activate profile on connect / activate profile on disconnect
* location -> activate profile when entering a specific area / activate profile when leaving this area
* time based -> activate profile at a specific point in time (start time) until the end (stop time) is reached and
  another profile is activated
