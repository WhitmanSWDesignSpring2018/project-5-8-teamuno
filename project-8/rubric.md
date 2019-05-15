# GRADE: 65/80 

## Original composition - 5/6 points

| Points Earned | Points Possible | Requirement | Comments
|--------------:|----------------:|-------------|---------
|1|1| A composition is presented.
|0|1| The composition is original.
|4|4| The composition is artistic and/or demonstrates the features of your software.

## Demo - 6/6 points

| Points Earned | Points Possible | Requirement | Comments
|--------------:|----------------:|-------------|---------
|4|4| Each new feature was justified and demonstrated.
|1|1| The composition was played.
|1|1| All team members were included. | _Ben was not present, but I understand this was a surprise to all. I will penalize Ben individually for not contributing._

## User manual - 5/6 points

| Points Earned | Points Possible | Requirement | Comments
|--------------:|----------------:|-------------|---------
|2|2| There is a file named ```manual.md``` in the top-level directory. | _I found it in the ```project-8``` directory, which is fine_
|2|2| It describes each new feature added in Project 8.
|1|2| It explains how to use each new feature. | _It would have been helpful to be told where to find the menu items. Also some advice on how large a MIDI file is manageable, as well as the limitations and what you should / should not expect to be imported._

## Feature selection - 12/12 points

One difficult enhancement may be worth several easier enhancements.

_Import MIDI is very cool! I'm glad you decided to do this. I tried it on a file I found myself and it worked well._

https://www.lynnemusic.com/midi/bjorn__lynne-_witchwood.mid

## Execution -  28/28 points

### Implementation of new features - 20/20 points

* All new features behave as described in the user manual.  If the description in the user manual is incomplete, then the behavior follows standard conventions and is otherwise unsurprising.
* There are no uncaught exceptions from new features.
* There is no debugging output from new features.

### No regressions - 8/8 points

All prior requirements are met, unless they have been superseded by new requirements or are documented by the team as known bugs for this iteration.

## Release - 2/2 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 1 | 1 | The release is tagged as ```project-8-release```.
| 1 | 1 | Documentation is in the ```project-8``` directory.

## Reflection and elegance - 13/20 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 3 | 4 | UML diagram is accurate and complete. | _How is ```MidiAdapter``` related to existing classes?_
| 2 | 4 | New classes/methods are reasonably self-explanatory. | _```InstrumentCommand``` should be named ```InstrumentChangeCommand``` for consistency. I see it's documented. ```MidiAdapter is not documented - seems you were in a hurry.  I note the only comments are commented out code, and a comment which is fairly obvious and not really necessary._
| 2 | 2 | Design overview addresses changes from Project 7 in general.
| 1 | 2 | Design overview addresses the implementation of each new feature. | _Just load from MIDI_
| 4 | 6 | Assessment of what is elegant and what is not thoughtfully addresses object-oriented design principles. | _See below_
| 1 | 1 | Velocity is presented. 
| 0 | 1 | Team retrospective is presented.

_Comments:_
* _You did not quite apply the adapter pattern, because there is not a common interface for load from XML and load from MIDI, but it's similar._
* _I would have liked to see some discussion of completeness, efficiency, and scalability in the assessment of what is not elegant._
* _I also would have liked you to unpack this: "We've managed to introduce a lot of new faults in this project, and not just in the MIDI importing feature. Despite that, we haven't introduced much new inelegance." What new faults, for example?_
