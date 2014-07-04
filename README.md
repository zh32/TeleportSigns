# TeleportSigns
Use signs to teleport between servers and display server status on them.

[Developement Builds](http://ci.zh32.de/job/TeleportSigns)

![Example](http://i.imgur.com/O731Xgs.png)

#### TeleportSigns

 * display status from other servers on signs
 * display motd on multiple lines
 * teleport to other servers by clicking the sign (when the servers are behind a BungeeCord proxy)
 * only updates a defined amount of signs per tick

#### Installation:

Drop the .jar into your plugin folder and start/stop your server. Edit the config file to your needs.


#### Config:

        offline-message: '&cServer is offline!' #message send to player
        show-offline-message: true 
        interval: 5 #time between pings/signupdate in seconds
        sign-updates: 20 #signs updates per tick 
        timeout: 1500 #connect timeout
        cooldown: 2000 #teleport cooldown 
        debug: false #debugmode

	servers:
  	pvp:        #as specified in bungee's config
    	address: '127.0.0.1:25566' #address
    	displayname: '&cPVP' #name to display on signs

	layouts:
  	default:
    	teleport: true #if the sign should teleport the player to another server
    	layout:
        - '&bTeleport to:'
        - '%motd%'
        - '&f&l%numpl%/%maxpl%'
        - '%isonline%'
    	online: '&aOnline'  #replaced with %isonline%
    	offline: '&cOffline'  #replaced with %isonline%

#### Placeholder

        %numpl%         -> online players
        %maxpl%         -> max players
        %motd%          -> motd send from server
        %displayname%   -> servers displayname (defined in config)
        %isonline%      -> displays text defined by 'online' and 'offline'
        
#### Stats:

![MC-Stats](http://api.mcstats.org/signature/TeleportSigns.png)
[Link](http://mcstats.org/plugin/TeleportSigns)