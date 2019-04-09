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
public class NoteBar extends TuneRectangle {


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
    /**
     * Hides this NoteBar from the composition pane.
     * If this isn't in the composition pane, nothing happens.
     * @param compositionpane the pane from which this bar should be deleted
     */
    public void delete(Pane compositionpane) {
        note.delete();
        compositionpane.getChildren().remove(this);
        ALLNOTEBARS.remove(this);
        ALLTOP.remove(this);
    }

    /**
     * Updates this NoteBar to match the note itself.
     */
    public void update() {
        setX(note.getStartTick());
        setY(Constants.LINE_SPACING
             * (Constants.NUM_PITCHES - note.getPitch() - 1));
        setWidth(note.getDuration());
        setFill(note.getInstrument().getDisplayColor());
    }

    /**
    * Create a hash set out of children.
    * @return the set of note bars within the pane
    */
    public HashSet<NoteBar> getChildLeaves() {
        HashSet set =  new HashSet<NoteBar>();
        set.add(this);
        return set;
    }

    /**
     * Determines whether this NoteBar is selected.
     * For something to be selected indicates that it's in the selection tree.
     * @return true or false depending on what
     */
    private boolean isSelected() {
        return TuneComposer.getSelection().contains(getHighestParent());
    }

    /**
     * Deals with a mouse click by selecting and creating notes.
     * @param me
     */
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
            me.consume();
        }
    }
    /**
     * Handles a mouse press, and records where notes are, prepares for drag event or mouse up.
     * @param me
     */
    private void onMousePressed(MouseEvent me) {
        double rightEdge = getX() + getWidth();
        dragWidth = (me.getX() >= rightEdge - 5);
        dragStartX = me.getX();
        dragStartY = me.getY();
        for (TuneRectangle p : TuneComposer.getSelection()) {
            if(p instanceof Gesture) {((Gesture) p).setStart();}
        }
        if(getHighestParent() instanceof Gesture) {((Gesture) getHighestParent()).setStart();}

        me.consume();

    }
    /**
     * Handles a mouse drag event, whether that be a drag note bar width
     * or drag individual note bars, will also make select note if mouse is
     * dragged while on a note, while none are selected.
     * @param me
     */
    protected void onMouseDragged(MouseEvent me) {
        // If this notebar is not already selected, make it the only selection
        if (!isSelected()) {
            TuneComposer.clearSelection();
            TuneComposer.addToSelection(getHighestParent());
        }

        if (dragWidth) {
            if(parent == null) {
            double dragDeltaWidth = me.getX() - dragStartX;
                for (TuneRectangle p : TuneComposer.getSelection()) {
                    if(p instanceof NoteBar && p.parent==null) {
                    p.setWidth(
                    Math.max(5.0, ((NoteBar) p).note.getDuration() + dragDeltaWidth));
                    }
                }
            }
        } else {
            double dragDeltaX = me.getX() - dragStartX;
            double dragDeltaY = me.getY() - dragStartY;
            for (TuneRectangle p : TuneComposer.getSelection()) {
                p.getHighestParent().move(dragDeltaX, dragDeltaY);
            }
        }
        me.consume();
    }
    /**
     * Handles a mouse release whether that be a note bar width drag, or
     * a note bar movement.
     * @param me
     */
    private void onMouseReleased(MouseEvent me) {
        if (dragWidth) {
            for (TuneRectangle p : TuneComposer.getSelection()) {
                if(p instanceof NoteBar && p.parent==null) {
                    ((NoteBar) p).note.setDuration((int)getWidth());
                    ((NoteBar) p).update();
                }
            }
        } else {
            for (NoteBar bar : TuneComposer.getSelectedNotes()) {
                bar.note.setStartTick((int)bar.getX());
                bar.note.setPitch(Constants.coordToPitch(bar.getY()));
                bar.update();
                bar.addToSelection();
            }
            for (TuneRectangle p : TuneComposer.getSelection()) {
                if (p instanceof Gesture) {
                    ((Gesture) p).snapY();
                    p.addToSelection();
                }
            }
        }
        me.consume();
    }
    /**
     * Adds a child to the selection if it is not within a gesture.
     */
    public void addToSelection() {
        if (parent == null) {
            TuneComposer.SELECTION.add(this);
        }
        if(!getStyleClass().contains("selected")) { //this works, change it if we have time
            getStyleClass().add("selected");
        }
    }
    /**
     * Removes the style when a note is no longer selected.
     */
    public void removeSelectStyle() {
        getStyleClass().remove("selected");
    }


    /**
     * Selects everything in the given area.
     * @param selectionArea the area in which TuneRectangles should be selected
     */
    public static void selectArea(Node selectionArea) {
        Bounds selectionBounds = selectionArea.getBoundsInParent();
        for (NoteBar bar : ALLNOTEBARS) {
            Bounds barBounds = bar.getBoundsInParent();
            if (selectionBounds.contains(barBounds)) {
                bar.addToSelection();
            }
        }
    }

    /**
     * Moves this NoteBar.
     * @param deltaX distance to move horizontally
     * @param deltaY distance to move vertically
     */
    @Override
    public void move(double deltaX, double deltaY) {
        update();
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }
}
