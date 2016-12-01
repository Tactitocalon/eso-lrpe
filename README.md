# eso-lrpe
Lithe RP Essentials: a roleplaying addon for Elder Scrolls Online
LRPE will add profile support to Elder Scrolls Online for roleplaying, so you can describe in detail how lithe and curvy your Bosmer waifu character is.

First I need to build a proof of concept for LRPE, just to show that this is a viable addon.
There will be three components that need to be built:

1). The ESO addon component
	This component will be very simple: it will add a single command, /examine, that will take a character name and tell you if they have a profile. The important part is that this component will load the LRPE database (which will be an addon configuration file), containing thousands of entries - a simulation of a full-scale version of LRPE.

2). The client synchronization tool
	This will be the Java tool that users of LRPE will run to synchronize their local copy of the LRPE database. It will contact the LRPE master server, download a copy of the LRPE database, format it and put it in the ESO addons folder as an addon configuration file, and then launch ESO via a Steam URI.

3). The LRPE master server
	A very primitive master server that exposes RESTful web services for clients to call. I'll make it very basic for now - just gives you a whole copy of the LRPE database for download.


From this proof of concept, I can begin to add features, like:

 - authentication and registration of characters
 - profile display ingame and profile editing
 - incremental database synchronization

At this point, LRPE should be ready for a closed beta - I'll find a RP community on ESO who will be willing to test the tool.
There will still be important enhancements that need to be made before I am comfortable releasing this to the world at large:

 - better ingame UI (based on user suggestions)
 - security enhancements to protect users
 - anti-abuse features
 - flagging of mature profiles
 - profile compression
 - timeout of inactive profiles
 - update notifications

Once all this is done, I think LRPE will be ready to release on a large scale.