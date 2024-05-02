# FindGopher: Multi-threaded Mobile Application for 2-player Gopher game in Java
- Designed and Implemented a game of Gopher hunting on a 10X10 matrix as an Android App. 
- Employed two different algorithms i.e. BFS and Modified BFS to play against each other in parallel on different Threads. For every guess, algorithms received feedback from the main UI thread 
- Established communication amongst the threads via messages and runnables. Each thread implemented a looper, handler and message queue and they interact via runnables and messages
- Used the framework’s active object design pattern to set up concurrent execution of each thread.

## Launcher Screen (Requires Custom Permission that it asks from user)
<img width="259" alt="gopher1" src="https://github.com/Jaykakkad82/Gopher_game_Mobile_App/assets/97722419/7681c490-6435-47ef-806f-2bd5cd5d482b">

## Game Screen: Two bots play against each other
- Both Algorithms run computation on different threads and they communicate with UI thread.

<img width="176" alt="gopher3" src="https://github.com/Jaykakkad82/Gopher_game_Mobile_App/assets/97722419/c8774215-3ae7-4fb2-b365-a16129a08bf2">

<img width="197" alt="gopher 2" src="https://github.com/Jaykakkad82/Gopher_game_Mobile_App/assets/97722419/bb5d0e22-234a-479a-acf8-8889751703b6">


  

