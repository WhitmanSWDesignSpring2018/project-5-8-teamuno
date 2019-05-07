We used the standard library serialization interface to serialize and either write to or read from a file, or make and store a bytearray on the system clipboard or retrieve and read a bytearray from the system clipboard. We did not add any new classes for save/open or copy/paste, although there is a new command class that can be undone. 

This was an elegant solution because not only did we not repeat ourselves we did not repeat anyone else either. All of our serialization details are handled by the interface and so it keeps our code concise and allowed for high reusability as well as low coupling.

We felt that our solution for this project was particularly elegant. One thing that could have been more elegant is to refactor our menubutton handlers into a metuHandler class to more closely adhere to the SRP and have good encapsulation and division of responsibilities, but this is also a persistent problem from previous projects.

We predicted 61 story points (where each story point represents a half hour of pair programming). We spent 21 half hours of pair programming on this project. Needless to say, this is pretty fast. The velocity is approximately 2.9. This is much faster than our previous velocity of .54. 

We spent a good amount of time researching existing libraries before implementation, this made our implementation particularly painless. We would like team members to be more conservative with refactoring and more thoroughly test the program after a refactor.
