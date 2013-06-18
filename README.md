# TeleportSigns
Use signs to teleport between servers and display server status on them.

[Developement Builds](http://ci.zh32.de/job/TeleportSigns)

![Example](http://i.imgur.com/O731Xgs.png)

#### TeleportSigns can:

 * display status from other servers
 * display motd on multiple lines
 * teleport to other servers by clicking the sign (when the servers are behind a BungeeCord proxy)

#### Installation:

Drop the .jar into your plugin folder and start/stop your server. Edit the config file to your needs.


#### Config:

	servers:
  	pvp:        #as specified in bungee's config
    	address: '127.0.0.1:25566' #address
    	displayname: '&cPVP' #name to display on signs[/CODE]

You can create multiple layouts with following placeholders: %numpl%, %maxpl%, %motd%, %displayname%, %isonline%

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
        
#### Stats:

![MC-Stats](http://api.mcstats.org/signature/TeleportSigns.png)

[Link](http://mcstats.org/plugin/TeleportSigns)