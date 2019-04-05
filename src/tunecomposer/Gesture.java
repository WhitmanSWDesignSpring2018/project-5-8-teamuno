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
public class Gesture extends TuneRectangle {
    private final HashSet<TuneRectangle> children;
    private double rectStartX;
    private double rectStartY;


    public Gesture(HashSet<TuneRectangle> children) {
        this.parent = null;
        this.children = children;
        for(TuneRectangle child : children){
            child.setParent(this);
            child.removeFromTop();
        }
        this.addToTop();
        this.setMouseTransparent(true);
        Double minX = null;
        Double maxX = null;
        Double minY = null;
        Double maxY = null;
        for(TuneRectangle child : children){
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
        for (TuneRectangle child : children) {
            child.play();
        }
    }


    @Override
    public void addToSelection() {
        if(!getStyleClass().contains("selected")){ //this works, change it if we have time
            getStyleClass().add("selected");
        }
        for(TuneRectangle child : children) {
            child.addToSelection();
        }
        if(parent == null){
            TuneComposer.addToSelection(this);
        }
    }

    public HashSet<NoteBar> getChildLeaves() {
        HashSet notes = new HashSet<NoteBar>();
        for (TuneRectangle p : children) {
            notes.addAll(p.getChildLeaves());
        }
        return notes;
    }
    
    public HashSet<TuneRectangle> getChildren() {
        return children;
    }

    public void delete(Pane compositionpane) {
        for(TuneRectangle child : children){
            child.delete(compositionpane);
        }
        TuneComposer.ALLTOP.remove(this);
        compositionpane.getChildren().remove(this);
    }

    public void freeChildren() {
        this.removeSelectStyle();
        for(TuneRectangle child : children){
            child.parent = null;
        }
    }

    public void setStart(){
        rectStartX = this.getX();
        rectStartY = this.getY();
        for (TuneRectangle child : children){
            if(child instanceof Gesture){((Gesture) child).setStart();}
        }
    }
    
    @Override
    public void move(double deltaX, double deltaY){
        for (TuneRectangle child : children) {
            child.move(deltaX, deltaY);
        }
        setX(rectStartX + deltaX);
        setY(rectStartY + deltaY);
    }
    
    public void snapY(){
        setY(getY() - (getY()) % LINE_SPACING);
        for(TuneRectangle child : children){
            if (child instanceof Gesture){
                ((Gesture) child).snapY();
            }
        }
    }

    @Override
    public void removeSelectStyle() {
       this.getStyleClass().remove("selected"); 
       for (TuneRectangle child : children){
           child.removeSelectStyle();
       }
       
    }
}
