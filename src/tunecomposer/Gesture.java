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
        }
    }
    
    @Override
    public void play(){
        for (Playable child : children) {
            child.play();
        }
    }
    
    @Override
    public void update(){}
    
    @Override
    public void addToSelection(){
        for(Playable child: children) {
            child.addToSelection();
        }
        
        //TODO add this to SELECTED, change css
    }
    
    @Override
    public void removeFromSelection() {
        for(Playable child: children){
            child.removeFromSelection();
        }
        
        //TODO remove from SELECTED, change css
    }
    
    public HashSet<NoteBar> getChildLeaves() {
        HashSet notes = new HashSet<NoteBar>();
        for (Playable p : children) {
            notes.addAll(p.getChildLeaves());
        }
        return notes;
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
        
}
    
