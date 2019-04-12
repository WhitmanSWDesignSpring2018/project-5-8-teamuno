/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.util.HashSet;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Class abstracted from NoteBar and Gesture.
 * @author ben, taka, spencer, ian
 */
public abstract class TuneRectangle extends Rectangle {
    Gesture parentGesture;

    /**
     * Location at the start of a mouse drag.
     * Used for calculating the distance this is dragged.
     */
    protected static double dragStartX;
    protected static double dragStartY;

    /**
     * Adds this to the selection.
     * TODO Change name to `select`
     */
    abstract public void addToSelection();

    /**
     * Makes this no longer appear selected.
     */
    abstract public void removeSelectStyle();

    /**
     * Gets the NoteBars in this tree, possibly including this.
     * @return the note bars in this tree
     */
    abstract public HashSet<NoteBar> getChildLeaves();

    /**
     * Move this rectangle.
     * @param deltaX distance to move horizontally
     * @param deltaY distance to move vertically
     */
    abstract public void move(double deltaX, double deltaY);
    
    
    abstract public void updateNoteMoved();

    /**
     * Set the parent gesture of this TuneRectangle.
     * Note that this method doesn't add this rectangle to the parent's
     * children.
     * @param newParent the gesture that this is put into
     */
    public void setParent(Gesture newParent) {
        this.parentGesture = newParent;
    }

    /**
     * Gets the gesture encompassing this TuneRectangle.
     */
    public Gesture getParentGesture() {
        return parentGesture;
    }
    
    

    /**
     * Gets the root of this rectangle's gesture tree.
     * @return the highest ancestor gesture, possibly this
     */
    public TuneRectangle getHighestParent() {
        if (parentGesture == null) {
            return this;
        } else {
            return parentGesture.getHighestParent();
        }
    }

    /**
     * Hides this rectangle from the composition pane.
     * @param compositionpane the pane from which this should be deleted
     */
    public abstract void delete(Pane compositionpane);
}
