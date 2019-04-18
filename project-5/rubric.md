# GRADE: 64/64 

## Functional and implementation requirements: 31/36 points total

Earned|Possible|Requirement | Comments
------|--------|------------|----------
1|1| Add two new menu items to the Edit menu called Group and Ungroup.
4|4| When the Group menu item is chosen, all selected note bars are grouped into one gesture. A gesture behaves as if it is just one note bar with a complex sound to it.  If nothing is selected when the Group menu item is chosen, nothing happens.
2|2| Selecting any note in a gesture causes the same thing to happen to all the others in the gesture.
2|2| Unselecting any note in a gesture with control-click causes the same thing to happen to all the others in the gesture.
0|2| Drag-selecting over a gesture consistently selects or unselects all the notes in the gesture. | _See (issue #31)[https://github.com/WhitmanSWDesignSpring2018/project-5-8-teamuno/issues/31]_
2|4| Dragging note bars in a gesture moves or stretches all notes in the gesture. | _Moves, does not stretch. See (issue #41)[https://github.com/WhitmanSWDesignSpring2018/project-5-8-teamuno/issues/41]_
3|3| Gestures are indicated visually by a thin black dotted line around the group of note bars. 
1|1| A selected gesture is indicated using a style similar to that of a selected note bar.
4|4| Gestures can include note bars or other gestures (that is, gestures can be nested).
3|4| When the Ungroup menu item is chosen, the selected gesture is ungrouped. Note that nested inner gestures are not ungrouped; you have to ungroup the nested gestures separately after ungrouping the outer gesture.  If no groups are selected when Ungroup is chosen, nothing happens. | _If multiple gestures are selected, only one is ungrouped. See (issue #30)[https://github.com/WhitmanSWDesignSpring2018/project-5-8-teamuno/issues/30]_
1|1| After selecting the Group menu item, the new gesture becomes the only selected item. 
2|2| After choosing Ungroup, all the inner notebars and gestures of the ungrouped gesture are selected.

### "Stretch goal"

Earned|Possible|Requirement | Comments
------|--------|------------|----------
0|0| When the right edge of a gesture is grabbed and stretched left or right, the duration and offset of everything in the gesture should stretch proportionally.

### No regressions - 1.5/2 points

Earned|Possible|Requirement | Comments
------|--------|------------|----------
1.5|2| All prior requirements are met, unless they have been superseded by new requirements. | _There are no keyboard shortcuts for Group and Ungroup. All menu items should have keyboard shortcuts._

### Release tag - 1.5/2 points

Earned|Possible|Requirement | Comments
------|--------|------------|----------
1.5|2| The release is tagged as project-5-release. | _Close enough for me to find it, not close enough for automation_

## Reflection - 12/12 points

For project 5 and beyond, I will expect your discussion of design and elegance to refer explicitly to design principles we have discussed in class.

Earned|Possible|Requirement | Comments
------|--------|------------|----------
3|3| UML 
2|2| Design overview
2|2| What is elegant?
2|2| What is not elegant?
1|1| Velocity
1|1| Collaboration statement
1|1| Team retrospective

## Elegance - 12/12 points

I looked at your UML diagram to assess the elegance of your class design.  You will get a grade for class design starting with Project 6.
I spot-checked a new class () and a modified class () to assess method-level design and self-explanatory code.

Earned|Possible|Requirement | Comments
------|--------|------------|----------
0|0| Class design 
8|8| Method design
4|4| Self-explanatory code
