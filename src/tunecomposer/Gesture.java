/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.util.HashSet;
import javafx.scene.input.MouseEvent;

/**
 * Represents a Gesture.
 * @author
 */
public class Gesture extends Playable {
    private final HashSet<Playable> children;
    
    
    public Gesture(HashSet<Playable> children) {
        this.parent = null;
        this.children = children;
        for(Playable child : children){
            child.setParent(this);
            child.removeFromTop();
        }
        this.setMouseTransparent(true);
        Double minX = null; //float?
        Double maxX = null;
        Double minY = null;
        Double maxY = null;
        for(Playable child : children){
            if(minX == null ||child.getX() < minX){
                minX = child.getX();
            }
            if(minY == null ||child.getY() < minY){
                minY = child.getY();
            }
            if(maxX == null ||child.getX()+child.getWidth() > maxX){
                maxX = child.getX()+child.getWidth();
            }
            if(maxY == null ||child.getY()+child.getHeight() > maxY){
                maxY = child.getY()+child.getHeight();
            }
        }
        this.setX(minX);
        this.setY(minY);
        this.setWidth(maxX-minX);
        this.setHeight(maxY-minY);
        getStyleClass().add("gesture");
        TuneComposer.clearSelection();
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
        //for child in children update
        //draw
    }
    
    @Override
    public void addToSelection(){
        for(Playable child: children) {
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
    
    public void delete(){
        for(Playable child : children){
            child.delete();
        }
        TuneComposer.ALLTOP.remove(this);
    }
    
    public void freeChildren(){
        this.removeSelectStyle();
        for(Playable child : children){
            child.parent = null;
        }
    }

    @Override
    public void onMouseDragged(MouseEvent me) {
        if (parent!= null) {
            parent.onMouseDragged(me);
        } else {
            parent.move(me);
        }
    }

    @Override
    public void move(MouseEvent me){
        for (Playable child : children) {
            child.move(me);
        }
        //TODO move current gesture
    }

    @Override
    public void removeSelectStyle() {
       for (Playable child : children){
           child.removeSelectStyle();
       }
       this.getStyleClass().remove("selected");
    }
        
}
    
