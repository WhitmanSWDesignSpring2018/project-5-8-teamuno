# project-5-8-teamuno
project-5-8-teamuno created by GitHub Classroom

We implemented undo and redo using the command pattern. The Tunecomposer not has an instance of the CommandHistory class which contains two stacks of commands and implements the undo and redo methods. We created a command interface, which has only the execute and unexecute methods. We then created different implementations for each kind of command that is added to the CommandHistory instance for handling. One large way this project is different from Project 5 is that we had a pretty large refactor. We pulled all of the pane and selection management out of the TuneComposer and put it in the composition class. Beyond this there were numerous bugfixes for problems from the last project, including dragging notes off the edge of the screen, drag selecting a single note in a gesture and ungrouping more than one gesture.

This was an elegant solution as we achieved a high degree of encapsulation between the CommandHistory class, the command classes and the composition classes. Furthermore the command interface allows for other kinds of commands to be added to the program without too much difficulty. The commands and Composition are in accordance with the expert pattern and the open/closed principle.

One thing in our solution that is inelegant is that there is a significant amount of reaching. Often we call something like `TuneComposer.composition.doThing();` from all kinds of classes. This feels like a violation of the law of Demeter. We did not know a better way to access the composition. We could have passed the composition around, so that TuneRectangles know what composition they are a part of, but this seemed like a lot of passing that could be avoided. Another inelegant solution is that TuneRectangles add themselves to the composition, it would have been better to implement this in composition, using the TuneRectangles as arguments. This would be a pretty significant refactor and we did not have time for it.

We estimated that this project would take 26 story points, where a story point was initially based on a half hour of pair programming. We spent roughly 48 half-hours of pair programming on this project. Our velocity was .54 story points per hour. Our previous velocity was .65 story points per hour. This is significantly slower than our previous velocity.

We had a more intense planning phase than the last couple projects, this led to a lot of implementation going smoothly, which was a nice surprise. This is definitely something that we will continue with going forward. One thing that we could improve is team communication. There were a couple subgroups working on different things, but they didn’t really know what the other group had gotten done, so there were some requirements that both teams thought the other was doing that had to be finished later. This could pretty easily be prevented by being more explicit when assigning tasks.



