/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.util.HashSet;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Represents a notebar on screen.
 * @author janet
 */
public class NoteBar extends Playable {
    // TODO Move these somewhere appropriate for Playable sets
    private static final HashSet<NoteBar> ALL = new HashSet<>();    
    
    
    private final Note note; 
    
    private static boolean dragWidth;
    private static double dragStartX;
    private static double dragStartY;
    
    /**
     * Create a new note bar.
     * @param note The note that this bar will display on screen.
     */
    public NoteBar(Note note) {
        this.note = note;
        
        getStyleClass().add("note");
        setHeight(Constants.LINE_SPACING - Constants.LINE_THICKNESS);
        update();            

        setOnMouseClicked((MouseEvent me) -> { onMouseClicked(me); });
        setOnMousePressed((MouseEvent me) -> { onMousePressed(me); });
        setOnMouseDragged((MouseEvent me) -> { onMouseDragged(me); });
        setOnMouseReleased((MouseEvent me) -> { onMouseReleased(me); });
        
        ALL.add(this);        
        addToSelection();
    }
    
    public void delete() {
        note.delete();
        ALL.remove(this);
    }    
    
    @Override
    public void update() {
        setX(note.getStartTick());
        setY(Constants.LINE_SPACING
             * (Constants.NUM_PITCHES - note.getPitch() - 1));
        setWidth(note.getDuration());
        setFill(note.getInstrument().getDisplayColor());
    } 

    public HashSet<NoteBar> getChildLeaves() {
        HashSet set =  new HashSet<NoteBar>();
        set.add(this);
        return set;
    }

    private void onMouseClicked(MouseEvent me) {
        if (me.isStillSincePress()) {
            if (me.isControlDown()) {
                if (TuneComposer.getSelection().contains(this.getHighestParent())) {
                    this.getHighestParent().removeFromSelection();
                } else {
                    this.getHighestParent().addToSelection();
                }
            } else {
                TuneComposer.clearSelection();
                this.getHighestParent().addToSelection();
            }
        }
        me.consume(); // Do not pass this event to the composition pane
    }
    
    private void onMousePressed(MouseEvent me) {                
        // Drag the edge if it is within 5 pixels 
        double rightEdge = getX() + getWidth();
        dragWidth = (me.getX() >= rightEdge - 5);
        dragStartX = me.getX();
        dragStartY = me.getY();
        me.consume();
    }
    
    protected void onMouseDragged(MouseEvent me) {
        // If this notebar is not already selected, make it the only selection
        if (!TuneComposer.getSelection().contains(this.getHighestParent())) {
            TuneComposer.clearSelection();
            TuneComposer.addToSelection(this.getHighestParent());
        }
        
        if (dragWidth) {
            //double dragDeltaWidth = me.getX() - dragStartX;
            //for (Playable p :  TuneComposer.getSelection()) {
                //p.setWidth(
                    //Math.max(5.0, p.note.getDuration() + dragDeltaWidth));
            //}
        } else {
            double dragDeltaX = me.getX() - dragStartX;
            double dragDeltaY = me.getY() - dragStartY;
            for (Playable p : TuneComposer.getSelection()) {
                p.update();
                p.setX(p.getX() + dragDeltaX);
                p.setY(p.getY() + dragDeltaY);
            }
        }
        me.consume();
    }
    
    private void onMouseReleased(MouseEvent me) { //TODO come back to this please, do it recursively
        if (dragWidth) {
           // for (NoteBar bar : TuneComposer.getSelection()) {
             //   bar.note.setDuration((int)getWidth());
               // bar.update();
            //}
        } else {
            for (NoteBar bar : TuneComposer.getSelectedNotes()) {
                bar.note.setStartTick((int)bar.getX());
                bar.note.setPitch(Constants.coordToPitch(bar.getY()));
                bar.update();
            }
        }  
        me.consume();
    }
    
    public void addToSelection() {
        if (parent == null) {
            TuneComposer.SELECTION.add(this);
        }
        getStyleClass().add("selected");
    }
        
    // TODO Rename to indicate that this is just a style change
    public void removeFromSelection() {
        getStyleClass().remove("selected");
    }
    
    
    public static void selectAll() {
        for (NoteBar bar : ALL) {
            bar.addToSelection();
        }
    }
    
    public static void selectArea(Node selectionArea) {
        Bounds selectionBounds = selectionArea.getBoundsInParent();
        for (NoteBar bar : ALL) {
            Bounds barBounds = bar.getBoundsInParent();
            if (selectionBounds.contains(barBounds)) {
                bar.addToSelection();
            }
        }
    }

    @Override
    public void play() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void move(MouseEvent me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
