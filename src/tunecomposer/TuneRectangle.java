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
    Gesture parent;

    protected static double dragStartX;
    protected static double dragStartY;
    
    
    abstract public void addToSelection();
    
    abstract public void removeSelectStyle();

    abstract public HashSet<NoteBar> getChildLeaves();
    
    abstract public void move(double deltaX, double deltaY);
    
    /**
     * removes current TuneRectangle from ALLTOP
     */
    public void removeFromTop(){
        TuneComposer.ALLTOP.remove(this);
    }
    
    /**
     * adds current TuneRectangle to ALLTOP
     */
    public void addToTop(){
        TuneComposer.ALLTOP.add(this);
    }

    public void setParent(Gesture newParent){
        this.parent = newParent;
    }
    
    /**
     * returns the parent of the current TuneRectangle
     */
    public Gesture getParentGesture(){
        return parent;
    }
    
    /**
     * returns the top level contained that this TuneRectnalge is in
     */
    public TuneRectangle getHighestParent(){
        if (parent == null) {
            return this;
        } else {
            return parent.getHighestParent();
        }
    }

    void delete(Pane compositionpane) {
}

    void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
