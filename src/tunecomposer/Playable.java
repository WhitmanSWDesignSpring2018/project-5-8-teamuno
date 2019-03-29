/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.util.HashSet;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * Represents a note.
 * @author janet
 */
public abstract class Playable extends Rectangle {
    Gesture parent;
    
    abstract public void play();
    
    abstract public void update();
    
    abstract public void addToSelection();
    
    abstract public void removeFromSelection();

    abstract public HashSet<NoteBar> getChildLeaves();
    
    /**
     *
     * @param me
     */
    abstract protected void onMouseDragged(MouseEvent me);
    
    abstract public void move(MouseEvent me);

    public void setParent(Gesture newParent){
        this.parent = newParent;
    }
    
    public Gesture getParentGesture(){
        return parent;
    }
    
    public Playable getHighestParent(){
        if (parent == null) {
            return this;
        } else {
            return parent.getHighestParent();
        }
    }
}
    
