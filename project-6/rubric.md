# GRADE: 43.5/48 

## Functional and implementation requirements: 28/28 points total

### Menu: 0.5/2 points

Earned|Possible|Requirement  | Comments
------|--------|-------------|-
0.5|1|Add **Undo** and **Redo** menu items to the **Edit** menu. The **Undo** menu item should have a keyboard equivalent of Ctrl/Cmd-Z. The **Redo** menu item should have a keyboard equivalent Ctrl/Cmd-Shift-Z.
0|1|Put a separator between them and the other **Edit** menu items.
0|0|**Undo** is disabled when the program starts and when all actions have been undone; it is enabled when the first action is taken. | _Addressed in the Project 7 rubric_
0|0|**Redo** is disabled unless there is an action to redo.
0|0|**Group** is disabled when there are fewer than two items selected.
0|0|**Ungroup** is disabled when the selection is not (or does not contain) a gesture.
0|0|**Delete** is disabled when nothing is selected.
0|0|**Play** is disabled when there are no notes.
0|0|**Stop** is disabled when the composition is not playing.

### Undo/redo operations: 16/18 points

1 pt each|Undo|Redo
---------|----|----
Add      |1|1
Select   |1|1
Delete   |1|1
Move     |1|1
Stretch  |1|1
Group    |1|1
Ungroup  |1|1

Earned|Possible|Requirement
------|--------|------------
2|4|If notes are deleted, edited, or selected as a group, for example, through a gesture or by using the **Select All** menu item, then an undo operation should affect all of the notes. | _Redo moving a group is buggy. I also lost the duration of a note through redo._

### Undo/redo stack: 5/6 points

Earned|Possible|Requirement | Comments
------|--------|------------
3|4|The user should be allowed to undo actions repeatedly, all the way back to the original blank composition pane when the application was first started. | _I can undo all the way back to the start, but there are some "ghost" notes and gestures that remain._
2|2|Similarly, if a sequence of undo actions was performed, then the user should be allowed to redo the whole sequence. | _If all the actions are create a note, this is true._
2|2|Taking a new action should clear the redo stack.

### No regressions - 0/0 points

All prior requirements are met, unless they have been superseded by new requirements.  (I will not replicate points taken off in Project 5.) _Not assessed to avoid double jeopardy_

## Release - 2/2 points
Earned|Possible|Requirement
------|--------|------------
2|2| The release is tagged as project-6-release.

## Reflection and elegance - 18/18 points

Earned|Possible|Requirement
------|--------|------------
4|4| UML diagram is accurate and complete. | _Very clear_
3|3| Design overview addresses strategy for Undo/Redo.
3|3| Design overview addresses strategy for disabling/enabling menu items or other changes from Project 5.
6|6| Assessment of what is elegant and what is not thoughtfully addresses object-oriented design principles.
1|1| Velocity is presented. 
1|1| Team retrospective is presented. | _Thanks for these thoughts on planning and communication - perpetual topics in software project management!_
