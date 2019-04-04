/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;
import java.util.HashSet;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import static tunecomposer.TuneComposer.ALLTOP;

/**
 * Represents a note bar on screen.
 * @author janet
 */
public class NoteBar extends Playable {


    static final HashSet<NoteBar> ALLNOTEBARS = new HashSet<>();
    private final Note note; 
    private static boolean dragWidth;

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
        
        TuneComposer.ALLTOP.add(this);        
        ALLNOTEBARS.add(this);
        addToSelection();
    }
    
    public void delete(Pane compositionpane) {
        note.delete();
        compositionpane.getChildren().remove(this);
        ALLNOTEBARS.remove(this);
        ALLTOP.remove(this);
    }    
    
    @Override
    public void update() {
        setX(note.getStartTick());
        // TODO Do something like this for gestures
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

    private boolean isSelected() {
        return TuneComposer.getSelection().contains(getHighestParent());
    }

    private void onMouseClicked(MouseEvent me) {
        if (me.isStillSincePress()) {
            if (me.isControlDown()) {
                if (isSelected()) {
                    TuneComposer.removeFromSelection(getHighestParent());
                } else {
                    getHighestParent().addToSelection();
                }
            } else {
                TuneComposer.clearSelection();
                getHighestParent().addToSelection();
            }
            me.consume(); // Do not pass this event to the composition pane
        }
    }
    
    private void onMousePressed(MouseEvent me) {                
        // Drag the edge if it is within 5 pixels 
        double rightEdge = getX() + getWidth();
        dragWidth = (me.getX() >= rightEdge - 5);

        dragStartX = me.getX();
        dragStartY = me.getY();
        for (Playable p : TuneComposer.getSelection()){
            if(p instanceof Gesture){((Gesture) p).setStart();}
        }
        if(getHighestParent() instanceof Gesture){((Gesture) getHighestParent()).setStart();}

        me.consume();

    }
    
    protected void onMouseDragged(MouseEvent me) {
        // If this notebar is not already selected, make it the only selection
        if (!isSelected()) {
            TuneComposer.clearSelection();
            TuneComposer.addToSelection(getHighestParent());
        }
        
        if (dragWidth) {
            if(parent == null){
            double dragDeltaWidth = me.getX() - dragStartX;
                for (Playable p : TuneComposer.getSelection()) {
                    if(p instanceof NoteBar && p.parent==null){
                    p.setWidth(
                    Math.max(5.0, ((NoteBar) p).note.getDuration() + dragDeltaWidth));
                    }
                }
            }
        } else {
            double dragDeltaX = me.getX() - dragStartX;
            double dragDeltaY = me.getY() - dragStartY;
            for (Playable p : TuneComposer.getSelection()) {
                p.getHighestParent().move(dragDeltaX, dragDeltaY);
            }
        }
        me.consume();
    }
    
    private void onMouseReleased(MouseEvent me) { //TODO come back to this please, do it recursively
        if (dragWidth) {
            for (Playable p : TuneComposer.getSelection()) {
                if(p instanceof NoteBar && p.parent==null){
                    ((NoteBar) p).note.setDuration((int)getWidth());
                    ((NoteBar) p).update();
                }
            }
        } else {
            for (NoteBar bar : TuneComposer.getSelectedNotes()) {
                bar.note.setStartTick((int)bar.getX());
                bar.note.setPitch(Constants.coordToPitch(bar.getY()));
                bar.update();
            }
            for (Playable p : TuneComposer.getSelection()){
                if (p instanceof Gesture){
                    ((Gesture) p).snapY();
                    p.addToSelection();
                }
            }
        }  
        me.consume();
    }
    
    public void addToSelection() {
        if (parent == null) {
            TuneComposer.SELECTION.add(this);
        }
        if(!getStyleClass().contains("selected")){ //this works, change it if we have time
            getStyleClass().add("selected");
        }
    }
        
    public void removeSelectStyle() {
        getStyleClass().remove("selected");
    }
    
    
    public static void selectAll() {
        for (Playable p : TuneComposer.ALLTOP) {
            p.addToSelection();
        }
    }
    
    public static void selectArea(Node selectionArea) {
        Bounds selectionBounds = selectionArea.getBoundsInParent();
        for (NoteBar bar : ALLNOTEBARS) {
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
    public void move(double deltaX, double deltaY) {
        update();
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }
}
