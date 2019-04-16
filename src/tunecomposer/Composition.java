
package tunecomposer;

import java.util.HashSet;
import javafx.scene.layout.Pane;

public class Composition {
    private HashSet<TuneRectangle> allTop;
    private HashSet<TuneRectangle> selectionTop;
    private Pane pane;

    public Composition(Pane compositionpane) {
        pane = compositionpane;
        allTop = new HashSet<>();
        selectionTop = new HashSet<>();
    }

    /**
     * Select all the items in the composition.
     */
    public void selectAll() {
        clearSelection();
        for(TuneRectangle rect : allTop) {
            rect.addToSelection();
        }
    }

    /**
     * Clear the selection.
     */
    public void clearSelection() {
        for(TuneRectangle rect : selectionTop) {
            rect.removeSelectStyle();
        }
        selectionTop.clear();
    }

    /**
     * Delete the contents of the selection.
     */
    public void deleteSelection() {
        for(TuneRectangle rect : selectionTop) {
            remove(rect);
            rect.delete(pane);
        }
        selectionTop.clear();
    }

    /**
     * Group the contents of the selection into a gesture.
     */
    public void groupSelection() {
        if(selectionTop.isEmpty()) {return;}

        // Pass the selection by value, not by reference
        HashSet<TuneRectangle> group = new HashSet<>(selectionTop);
        new Gesture(group);
    }

    /**
     * Ungroup selected gestures. NoteBars are not affected.
     */
    public void ungroupSelected() {
        for(TuneRectangle p : selectionTop) {
            if (p instanceof Gesture) {
                Gesture g = (Gesture) p;
                selectionTop.remove(g);
                g.freeChildren();
                pane.getChildren().remove(g);
                HashSet<TuneRectangle> children = g.getChildren();
                children.forEach((child) -> {
                    child.addToSelection();
                });
            }
        }
    }

    /**
     * Adds something to the composition.
     * Should be called on a newly created or un-deleted note.
     * @param newRect the TuneRectangle to be added
     */
    public void add(TuneRectangle newRect) {
        pane.getChildren().add(newRect);
        allTop.add(newRect);
    }

    /**
     * Remove something from the composition.
     * @param rect the TuneRectangle to be removed
     */
    public void remove(TuneRectangle rect) {
        // TODO Remove redundant compositionpane removals elsewhere
        pane.getChildren().remove(rect);
        allTop.remove(rect);
    }

    /**
     * Add an item to the selection.
     * Note that the style of the target TuneRectangle is not affected.
     * @param root the TuneRectangle to add
     */
    public void addToSelection(TuneRectangle root) {
        selectionTop.add(root);
    }

    /**
     * Remove an item from the selection.
     * @param root the TuneRectangle to remove
     */
    public void removeFromSelection(TuneRectangle root) {
        selectionTop.remove(root);
        root.removeSelectStyle();
    }

    /**
     * Tells whether the given TuneRectangle is selected.
     * @param root the TuneRectangle in question
     */
    public boolean isSelectedTop(TuneRectangle root) {
        return selectionTop.contains(root);
    }

    /**
     * Resize the selected items.
     * @param deltaX the number of pixels to resize by
     */
    public void resizeSelected(double deltaX) {
        for (TuneRectangle rect : selectionTop) {
            if(rect instanceof NoteBar) {
                NoteBar bar = (NoteBar) rect;
                bar.resize(deltaX);
            }
        }
    }

    /**
     * Move the selected items.
     * @param deltaX the horizontal distance to move the selected items
     * @param deltaY the vertical distance to move the selected items
     */
    public void moveSelected(double deltaX, double deltaY) {
        for (TuneRectangle rect : selectionTop) {
            rect.move(deltaX, deltaY);
        }
    }

    /**
     * Update the duration of selected notes.
     * This needs to be called after any number of NoteBars are resized.
     */
    public void updateResized() {
        for (TuneRectangle rect : selectionTop) {
            if (rect instanceof NoteBar) {
                NoteBar bar = (NoteBar) rect;
                bar.updateNoteDuration();
            }
        }
    }

    /**
     * Update the start tick of selected notes.
     * This needs to be called after any number of NoteBars are moved.
     */
    public void updateMoved() {
        for (TuneRectangle rect : selectionTop) {
            rect.updateNoteMoved();
        }
    }

    /**
     * Instructs each gesture to store its current location as a drag start.
     * This gets called when the mouse button is pressed.
     */
    public void setSelectionStart() {
        for (TuneRectangle rect : selectionTop) {
            if(rect instanceof Gesture) {
                Gesture g = (Gesture) rect;
                g.setStart();
            }
        }
    }

    /**
     * Snap selected gestures to the horizontal lines after moving.
     * This only snaps gestures because notes snap themselves.
     */
    public void snapSelectionY() {
        for (TuneRectangle rect : selectionTop) {
            if(rect instanceof Gesture) {
                Gesture g = (Gesture) rect;
                g.snapY();
            }
        }
    }

    /**
     * Checks if this composition contains nothing.
     * @return true if this composition contains nothing, otherwise false
     */
    public boolean isEmpty() {
        return allTop.isEmpty();
    }
    
    /**
     * Checks if the selection is empty.
     */
    public boolean isSelectionEmpty() {
        return selectionTop.isEmpty();
    }
}
