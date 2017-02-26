# eso-lrpe
#### Lithe RP Essentials: a roleplaying addon for Elder Scrolls Online

---

### about
eso-lrpe is an experimental addon that adds profile support to Elder Scrolls Online for roleplaying, so you can describe in detail how lithe and curvy your Bosmer waifu character is.

If you're familiar with World of Warcraft roleplaying profile addons, I'm trying to recreate something like that. The problem with Elder Scrolls Online (and the reason why there is no profile addon so far) is that it's almost impossible to transmit information to other players ingame.

eso-lrpe gets around this by using a centralized database that stores all profiles. You run a tool every day before you play ESO to download all the latest profiles.

---

### features
* Supports North America megaserver only (for now).
* Create profiles describing your characters that other eso-lrpe users can read.
* Examine other players using the /examine (or /ex) command.
* Keybind available to examine the player you are currently looking at.
* Still experimental, so please keep in mind not everything is complete or will work properly.

---

### installation
1. You must have the Java Runtime Environment (at least Version 8) installed: https://java.com/en/download/
2. Download the eso-lrpe addon here: TODO
3. Install the eso-lrpe addon to your Elder Scrolls Online addons folder.
4. Navigate to the addon folder and run the "eso-lrpe-updater.jar" file to bring up the eso-lrpe updater tool.
5. The updater will ask you to confirm the location of your addons folder.
6. The updater will then ask you to login or register an account for eso-lrpe. This will be the account used to manage your profiles. Please do not use the same username and password as your actual Elder Scrolls Online account.
7. After you successfully register an eso-lrpe account, you will be presented with a list of your profiles.
8. Create a profile, then click the "Update Profile Database" button once you are complete.

---

### usage
* User profiles are uploaded to eso-lrpe.com to a centralized database.
* The eso-lrpe-updater tool will download all roleplaying profiles and make them available ingame.
* eso-lrpe users will not see changes made to profiles until they have run the updater tool.
* You will need to run the eso-lrpe-updater frequently to keep up to date on everyone's profiles. The addon will nag you if you have gone too long without updating.

* To examine players, type /examine (or /ex) followed by the character's full name. For example: "/examine Nelanya of Lillandril".
* Alternative, you can configure a keybind (in the Controls menu) to examine your current target.

---

### terms of use
* Refrain from creating sexually explicit or offensive profiles. Please keep profiles abiding by Elder Scrolls Online's code of conduct.
* Please do not break my server or database. Don't create thousands of profiles, extremely large profiles, scam profiles or any of that nonsense.
* I am not responsible if my addon breaks anything (although I've implemented security features to stop abuse). Use at your own risk.

---

### contact

You can contact me (for bug reports, suggestions, contributions or shitposts) on eso-rp.com, some nerd site for roleplayers. I also hang out in their discord.

http://www.eso-rp.com/profile/17561202

If you like my work, you can also send me hot Bosmer waifus to @Tactitocalon (North America / PC).

### future enhancements

High priority:
* Character names should be case insensitive.
* Ability to delete profiles.
* Limit on number of profiles per account.
* Limit on profile size.
* Whitelisting of all user input for security purposes.
* Better branding / standardization of addon name across addon content.

Normal priority:
* Better ingame profile display.
* Addon configuration menu.
* Make nag period configurable.
* Show display name and roleplayer status when looking at players.
* Ability to link an external URL to a profile.
* Right click context menu option in chat to view someone's profile.
* EU / PTS megaserver support.
* Rewrite character names to display names in chat. Needs to be compatible with pChat at minimum.
* Incremental profile downloads.
* Make the client tool not a hacky piece of shit that barely works. Loading bars would be hot.

Low priority:
* Ability to advertise looking for roleplay / events with a single button click ingame. Also single button click to view all LFRP / event advertisments.
* Command to display location (via the user's map) of LFRP / event advertisment.
* Flag 'mature' profiles (using blacklist of naughty words).
* Profile compression.
* Timeout of inactive profiles from the database.
* Autocompletion of /examine command based on available profiles. (select matching profile name, if multiple matches, display list of matches)
* Autocomplete /examine command based on nearby players (or maybe recently seen / chatting players). Not sure if possible. Maybe a GUI element for it too so you can just bring up a list.
* Counter on website to display the number of times the word "lithe" has been used in profiles.
