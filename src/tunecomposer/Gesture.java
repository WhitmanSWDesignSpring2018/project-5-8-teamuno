/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.util.HashSet;
import javafx.scene.layout.Pane;
import static tunecomposer.Constants.LINE_SPACING;

/**
 * Represents a Gesture, a collection of notes.
 * @author Ian, Ben, Spencer, Taka
 */
public class Gesture extends TuneRectangle {

    /**
     * The notes contained in this gesture.
     */
    private final HashSet<TuneRectangle> children;

    /**
     * Location at the start of a mouse drag.
     * Used for calculating the distance this is dragged.
     */
    private double rectStartX;
    private double rectStartY;

    /**
     * Creates a gesture.
     * @param children, the TuneRectangles that will be inside the gesture
     */
    public Gesture(HashSet<TuneRectangle> children) {
        this.parent = null;
        this.children = children;
        for(TuneRectangle child : children) {
            child.setParent(this);
            child.removeFromTop();
        }
        this.addToTop();
        this.setMouseTransparent(true);
        Double minX = null;
        Double maxX = null;
        Double minY = null;
        Double maxY = null;
        for(TuneRectangle child : children) {
            if(minX == null || child.getX() < minX) {
                minX = child.getX();
            }
            if(minY == null || child.getY() < minY) {
                minY = child.getY();
            }
            if(maxX == null || child.getX()+child.getWidth() > maxX) {
                maxX = child.getX()+child.getWidth();
            }
            if(maxY == null || child.getY()+child.getHeight() > maxY) {
                maxY = child.getY()+child.getHeight();
            }
        }
        this.setX(minX);
        this.setY(minY);
        this.setWidth(maxX-minX);
        this.setHeight(maxY-minY);
        TuneComposer.clearSelection();
        getStyleClass().add("gesture");
        addToSelection();
    }

    /**
     * Selects this gesture and everything contained by it.
     */
    @Override
    public void addToSelection() {
        if(!getStyleClass().contains("selected")) { //this works, change it if we have time
            getStyleClass().add("selected");
        }
        for(TuneRectangle child : children) {
            child.addToSelection();
        }
        if(parent == null) {
            TuneComposer.addToSelection(this);
        }
    }

    /**
     * Gets all NoteBars contained in this gesture.
     * @return notes, the note bars that belong to this gesture
     */
    public HashSet<NoteBar> getChildLeaves() {
        HashSet notes = new HashSet<NoteBar>();
        for (TuneRectangle p : children) {
            notes.addAll(p.getChildLeaves());
        }
        return notes;
    }

    /**
     * Gets the hashset of child TuneRectangles.
     * @return children, a hashset of child TuneRectangles
     */
    public HashSet<TuneRectangle> getChildren() {
        return children;
    }

    /**
     * Undraws and removes this gesture and all of its children.
     * @param compositionpane the current pane
     */
    public void delete(Pane compositionpane) {
        for(TuneRectangle child : children) {
            child.delete(compositionpane);
        }
        TuneComposer.ALLTOP.remove(this);
        compositionpane.getChildren().remove(this);
    }

    /**
     * Sets the parent references of children to null.
     */
    public void freeChildren() {
        this.removeSelectStyle();
        for(TuneRectangle child : children) {
            child.parent = null;
        }
    }

    /**
     * Stores the current location of this gesture. Used for dragging.
     */
    public void setStart() {
        rectStartX = this.getX();
        rectStartY = this.getY();
        for (TuneRectangle child : children) {
            if(child instanceof Gesture) {((Gesture) child).setStart();}
        }
    }

    /**
     * Move this rectangle.
     * @param deltaX distance to move horizontally
     * @param deltaY distance to move vertically
     */
    @Override
    public void move(double deltaX, double deltaY) {
        for (TuneRectangle child : children) {
            child.move(deltaX, deltaY);
        }
        setX(rectStartX + deltaX);
        setY(rectStartY + deltaY);
    }

    /**
     * Adjusts the gesture and its child gestures to be in line with the
     * background lines.
     */
    public void snapY() {
        setY(getY() - (getY()) % LINE_SPACING);
        for(TuneRectangle child : children) {
            if (child instanceof Gesture) {
                ((Gesture) child).snapY();
            }
        }
    }

    /**
     * Makes the gesture and all of its children look unselected.
     */
    @Override
    public void removeSelectStyle() {
       this.getStyleClass().remove("selected");
       for (TuneRectangle child : children) {
           child.removeSelectStyle();
       }
    }
}
