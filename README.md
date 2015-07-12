# BountyHunter ![Travis-CI](https://travis-ci.org/intronate67/BountyHunter.svg)

Plugin for new Minecraft Server API Sponge. It includes a whole entire economy system in itself so no need to install another one.

About
----------

Bounty Hunter was created upon the request of the SpongeAPI community. It has many features including its own working Economy until the developers of sponge implement their own Economy API like Bukkit's vault.

Features
-----------
**Implemented**
* Economy
* Rewards for completing bounties
* Each Bounty costs money
* Admin bounty exclusion.

**Planned**
* Configurable decuctions for placing a bounty
* Dropping player heads
* Leaderboards

Installation
------------
> **Note**: Check Travis-CI build to see if build is passing or failing 

* Download the Zip file from [Here](http://huntersharpe.net/no-sidebar.html)
* Extract the BountyHunter-SNAPCHAT-X.X.Jar to your SpongeVanilla or Sponge server's mods/ folder
* Configure your permissions and configuration file to your liking
* Start up your server

Permissions
------------
- Standard Users
  * bountyhunter.use.* - Gives user permission to use all basic commands.
  * bountyhunter.use.accept - Gives user permission to accept a bounty.
  * bountyhunter.use.abandon - Gives user permission to abandon their current accepted bounty.
- Trusted Users
  * bountyhunter.use.place - Gives user permission to place a bounty.
  * bountyhunter.use.remove - Gives user permission to remove their bounty.
- Staff
  * bountyhunter.exempt - Allows them to be exempt from having bounties placed on them.
  * bountyhunter.override - Allows them to override placed bounties and remove them.* Planned
