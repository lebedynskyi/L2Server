**L2Server**

Yet another try to implement Login and Game Server for **Lineage 2 Interlude** client.

The main goal is to practice of making architecture for big application with networking and multithreading. And now I
get stuck with it. The main issue with **cyclic dependencies**

Client -> Player -> World -> Player -> Client

Maybe I need to introduce some Manager / Handler and World object should become simple Data class

**The list of main features will be implemented ASAP:**
- [X] User login
- [ ] Login Server <-> Game Server communication (Partially done)
- [X] Character creation and choosing of already created
- [X] ÏEntering world
- [X] Movement and saving its state without AI
- [X] Chat of all types without validation

**The list of additional features:**
- [x] NPC spawn
- [ ] Inventory
- [ ] Equipment

**Tech TODO list**
- [ ] After adding announcer something wrong with sending packets to some clients
- [ ] Sending ServerStatusUpdate via some manager ?
- [ ] New naming for clients and servers
- [ ] Selector should be able to receive list of packets. It is important when client sends a lot of data
- [ ] Validation of logged-in user via Login Server <-> Game Server communication
- [x] Remove user from login lobby after connect to game server. Need to finish bridge communication
- [ ] Remove user from game world after logout
- [x] Implement packet for logout
- [ ] Select and save account for game client after connect to game server