**L2Server**

Yet another try to implement Login and Game Server for **Lineage Interlude** client.  

The main goal is to practice of making architecture for big application with networking and multithreading.
And now I get stuck with it. The main issue with **cyclic dependencies**

Maybe i need to introduce some Manager / Handler and another object should become simple Data classes

Client -> Player -> World -> Player -> Client

**The list of main features will be implemented ASAP:**
- ~~Login Server <-> Game Server communication~~ (Partialy done)
- ~~User login~~
- ~~Character creation and choosing of already created~~
- ~~Entering world~~
- ~~Movement and saving its state without AI~~
- ~~Chat of all types without validation~~


**The list of additional features:**
- Inventory
- Equipment
- ~~NPC spawn~~

**TODO list**
- ~~Separate Core (Shared) and Network~~
- ~~Separate config and models for status and packet?~~
- ~~All configs should have Prefix Config~~
- New naming for clients and servers
- Separate `GameServer` and `GameBridge`
- Selector should be able to receive list of packets. It is important when client sends a lot of data
- ~~Remove ip from Registered server. It should come directly from connected server in `ServerStatus`. By default, the same as LoginServer ?~~

