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

    protected static double dragStartX;
    protected static double dragStartY;
    
    abstract public void play();
    
    abstract public void update();
    
    abstract public void addToSelection();
    
    abstract public void removeSelectStyle();
    
    abstract public void delete();

    abstract public HashSet<NoteBar> getChildLeaves();
    
    abstract public void move(double deltaX, double deltaY);
    
    public void removeFromTop(){
        TuneComposer.ALLTOP.remove(this);
    }

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
