/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.util.HashSet;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * Represents a notebar on screen.
 * @author janet
 */
public class NoteBar extends Rectangle {
    private static final HashSet<NoteBar> ALL = new HashSet<>();    
    private static final HashSet<NoteBar> SELECTION = new HashSet<>();
    
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
    
    private void update() {
        setX(note.getStartTick());
        setY(Constants.LINE_SPACING
             * (Constants.NUM_PITCHES - note.getPitch() - 1));
        setWidth(note.getDuration());
        setFill(note.getInstrument().getDisplayColor());
    } 

    private void onMouseClicked(MouseEvent me) {
        if (me.isStillSincePress()) {
            if (me.isControlDown()) {
                if (SELECTION.contains(this)) {
                    removeFromSelection();
                } else {
                    addToSelection();
                }
            } else {
                NoteBar.clearSelection();
                addToSelection();
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
    
    private void onMouseDragged(MouseEvent me) {
        // If this notebar is not already selected, make it the only selection
        if (!SELECTION.contains(this)) {
            clearSelection();
            addToSelection();
        }
        
        if (dragWidth) {
            double dragDeltaWidth = me.getX() - dragStartX;
            for (NoteBar bar : SELECTION) {
                bar.setWidth(
                        Math.max(5.0, bar.note.getDuration() + dragDeltaWidth));
            }
        } else {
            double dragDeltaX = me.getX() - dragStartX;
            double dragDeltaY = me.getY() - dragStartY;
            for (NoteBar bar : SELECTION) {
                bar.update();
                bar.setX(bar.getX() + dragDeltaX);
                bar.setY(bar.getY() + dragDeltaY);
            }
        }
        me.consume();
    }
    
    private void onMouseReleased(MouseEvent me) {
        if (dragWidth) {
            for (NoteBar bar : SELECTION) {
                bar.note.setDuration((int)getWidth());
                bar.update();
            }
        } else {
            for (NoteBar bar : SELECTION) {
                bar.note.setStartTick((int)bar.getX());
                bar.note.setPitch(Constants.coordToPitch(bar.getY()));
                bar.update();
            }
        }  
        me.consume();
    }
    
    private void addToSelection() {
        SELECTION.add(this);
        getStyleClass().add("selected");
    }
        
    private void removeFromSelection() {
        SELECTION.remove(this);
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
    
    public static void clearSelection() {
        for (NoteBar bar : SELECTION) {
            bar.getStyleClass().remove("selected");        
        }
        SELECTION.clear();
    }    
    
    public static HashSet<NoteBar> getSelection() {
        return SELECTION;
    }


}
