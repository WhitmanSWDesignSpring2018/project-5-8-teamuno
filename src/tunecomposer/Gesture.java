/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.util.HashSet;
import javafx.scene.layout.Pane;
import static tunecomposer.Constants.LINE_SPACING;

/**
 * Represents a Gesture.
 * @author
 */
public class Gesture extends Playable {
    private final HashSet<Playable> children;
    private double rectStartX;
    private double rectStartY;


    public Gesture(HashSet<Playable> children) {
        this.parent = null;
        this.children = children;
        for(Playable child : children){
            child.setParent(this);
            child.removeFromTop();
        }
        this.setMouseTransparent(true);
        Double minX = null;
        Double maxX = null;
        Double minY = null;
        Double maxY = null;
        for(Playable child : children){
            if(minX == null || child.getX() < minX){
                minX = child.getX();
            }
            if(minY == null || child.getY() < minY){
                minY = child.getY();
            }
            if(maxX == null || child.getX()+child.getWidth() > maxX){
                maxX = child.getX()+child.getWidth();
            }
            if(maxY == null || child.getY()+child.getHeight() > maxY){
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

    @Override
    public void play(){
        for (Playable child : children) {
            child.play();
        }
    }

    @Override
    public void update(){
        // TODO This method may not have a purpose.
        //for child in children update
        //draw
        //for (Playable child : children) {
        //}
    }

    @Override
    public void addToSelection() {
        if(!getStyleClass().contains("selected")){ //this works, change it if we have time
            getStyleClass().add("selected");
        }
        for(Playable child : children) {
            child.addToSelection();
        }
        if(parent == null){
            TuneComposer.addToSelection(this);
        }
    }

    public HashSet<NoteBar> getChildLeaves() {
        HashSet notes = new HashSet<NoteBar>();
        for (Playable p : children) {
            notes.addAll(p.getChildLeaves());
        }
        return notes;
    }

    public void delete(Pane compositionpane) {
        for(Playable child : children){
            child.delete(compositionpane);
        }
        TuneComposer.ALLTOP.remove(this);
    }

    public void freeChildren() {
        this.removeSelectStyle();
        for(Playable child : children){
            child.parent = null;
        }
    }

    /*
     * // I think this is unused.
    @Override
    public void onMouseDragged(MouseEvent me) {
        // TODO Do we want Gesture to have a mouse drag handler?
        // It's meant to be mouse-transparent.
        // TODO I don't think this is called.
        if (parent != null) {
            parent.onMouseDragged(me);
        } else {
            // TODO Move 
            move(me);
        }
    }
    */
    public void setStart(){
        rectStartX = this.getX();
        rectStartY = this.getY();
        for (Playable child : children){
            if(child instanceof Gesture){((Gesture) child).setStart();}
        }
    }
    
    @Override
    public void move(double deltaX, double deltaY){
        for (Playable child : children) {
            child.move(deltaX, deltaY);
        }
        setX(rectStartX + deltaX);
        setY(rectStartY + deltaY);
    }
    
    public void snapY(){
        setY(getY() - (getY()) % LINE_SPACING);
        for(Playable child : children){
            if (child instanceof Gesture){
                ((Gesture) child).snapY();
            }
        }
    }

    @Override
    public void removeSelectStyle() {
       System.out.println(getStyleClass());
       this.getStyleClass().remove("selected"); //css not being removed
       System.out.println(getStyleClass());
       for (Playable child : children){
           child.removeSelectStyle();
       }
       
    }
}
