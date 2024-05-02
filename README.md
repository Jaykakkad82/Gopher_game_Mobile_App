# FindGopher: Multi-threaded Mobile Application for 2-player Gopher game in Java
- Designed and Implemented a game of Gopher hunting on a 10X10 matrix as an Android App. 
- Employed two different algorithms i.e. BFS and Modified BFS to play against each other in parallel on different Threads. For every guess, algorithms received feedback from the main UI thread 
- Established communication amongst the threads via messages and runnables. Each thread implemented a looper, handler and message queue and they interact via runnables and messages
- Used the framework’s active object design pattern to set up concurrent execution of each thread.

