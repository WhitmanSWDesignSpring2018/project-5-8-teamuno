# project-5-8-teamuno
project-5-8-teamuno created by GitHub Classroom

We created a new class called gesture and a new abstract class called
TuneRectangle. TuneRectangle inherits from Rectangle and both NoteBar and
Gesture inherit from TuneRectangle. We chose to use an abstract class as there
was sufficient code reuse to justify inheritence instead of just an interface.
Most of the methods in NoteBar, except mouse event handlers stayed the same.
The event handlers were changed so that they operated on the highest parents of
the selected notes, this allowed for gestures to operate on all of their member
notes. In order for gesture to operate recursively we had to closely mimic the
NoteBar class in the Gesture class. We also moved some static fields and static
methods from NoteBar into TuneComposer so that they could be accessed by both
NoteBar and Gesture.

We felt that the way events are passed from notes up to their parent gestures
and back down to all the leaves of the highest parent was particularly elegant.
This allowed us to reuse all of the code for NoteBar in the behavior of gesture
to has the gesture class be relatively simple. This also allowed for click
detection to be handled by only NoteBar. The use of polymorphism was also
elegant in our opinion. This allowed for gestures to contain a hashset of
TuneRectangles, which could either be gestures or NoteBars but they could be
treated the same way by the gesture methods. These two features allowed most of
the Gesture methods to simply call itself on all of the children of the
gesture.

One potential issue with our code is that gestures are entirely click
transparent. We still met all of the funcitonal requirements. but this made it
very difficult to implement the strech goal for this project, so we did not end
up doing that. One other thing is that when the user clicked on a NoteBar or a
Gesture more than once the selected stylec class would be added more than once,
so now we check with a conditional before we add it, there is likely a better
solution but this is alright.

We estimated 13 story points, each representing half an hour of pair
programming or an hour of solo programming. Our total work was about 10 hours
of pair programming. We had a velocity of .65 story points per hour (after
converting each story point into an individual's hour of work). We completed
all of our story points.

We collaborated by usually working in pairs. We had a whenisgood poll for
meeting times. Ian and Spencer met a couple times and implemented the basis of
Gesture. Ben and Ian worked on getting some of the later features done and
finishing the fxml. In terms of the roles reading, we were all developers,
there was no individual Development Manager, Ian and Spencer usually reached
out to the rest of the team about meeting. Ian and Spencer did most of the
quality assurance. We all worked together as solutions architects during our
too-short planning phase.
