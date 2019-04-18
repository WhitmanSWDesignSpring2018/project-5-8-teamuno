
package tunecomposer;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.layout.Pane;

public class Composition {
    private Set<TuneRectangle> allRoots;
    private Set<TuneRectangle> selectedRoots;
    private Set<TuneRectangle> selectionChanges; //TODID rename
    private Pane pane;
    private Double selectionTop;
    private Double selectionBottom;
    private Double selectionLeft;
    private Double selectionRight;
    
                    

    public Composition(Pane compositionpane) {
        pane = compositionpane;
        allRoots = new HashSet<>();
        selectedRoots = new HashSet<>();
        selectionChanges = new HashSet<>();
        
    }

    public Set<TuneRectangle> getSelectionTracker(){
        return new HashSet<>(selectionChanges);
    }
    
    public void clearSelectionTracker(){
        selectionChanges.clear();
    }
    
    public Set getSelectionTop(){
        return new HashSet<>(selectedRoots);
    }
    
    /**
     * Select all the items in the composition.
     */
    public void selectAll() {
        clearSelection();
        for(TuneRectangle rect : allRoots) {
            rect.addToSelection();
        }
    }

    /**
     * Clear the selection.
     */
    public void clearSelection() {
        for(TuneRectangle rect : selectedRoots) {
            rect.removeSelectStyle();
            trackRectSelect(rect);
        }
        selectionLeft = null;
        selectionRight = null;
        selectionTop = null;
        selectionBottom = null;
        selectedRoots.clear();
    }

    /**
     * Delete the contents of the selection.
     */
    public void deleteSelection() {
        HashSet forCommand = new HashSet(selectedRoots);
        TuneComposer.history.addNewCommand(new DeletionCommand(forCommand));
        for(TuneRectangle rect : selectedRoots) {
            remove(rect);
            rect.delete(pane);
        }
        selectedRoots.clear();
    }

    /**
     * Group the contents of the selection into a gesture.
     */
    public void groupSelection() {
        if(selectedRoots.isEmpty()) {return;}

        // Pass the selection by value, not by reference
        HashSet<TuneRectangle> group = new HashSet<>(selectedRoots);
        Gesture newGesture = new Gesture(group);
        HashSet<Gesture> forCommand = new HashSet();
        forCommand.add(newGesture);
        TuneComposer.history.addNewCommand(new GroupCommand(forCommand, true));
        
    }

    /**
     * Ungroup selected gestures. NoteBars are not affected.
     */
    public void ungroupSelected() {
        HashSet<HashSet<TuneRectangle>> forCommand = new HashSet();
        for(TuneRectangle p : new HashSet<TuneRectangle>(selectedRoots)) {
            if (p instanceof Gesture) {
                Gesture g = (Gesture) p;
                selectedRoots.remove(g);
                g.freeChildren();
                pane.getChildren().remove(g);
                HashSet<TuneRectangle> children = g.getChildren();
                children.forEach((child) -> {
                    child.addToSelection();
                });
                forCommand.add(children);
            }
        }
        TuneComposer.history.addNewCommand(new GroupCommand(forCommand, false));
    }
    
    public Gesture groupTuneRectangles(HashSet<TuneRectangle> toGroup) {
        if(toGroup.isEmpty()) {return null;}

        // Pass the selection by value, not by reference
        HashSet<TuneRectangle> group = new HashSet<>(toGroup);
        return new Gesture(group);
    }

    public HashSet<TuneRectangle> ungroupGesture(Gesture Ungroup) {
        if(selectedRoots.contains(Ungroup)){selectedRoots.remove(Ungroup);}
        Ungroup.freeChildren();
        pane.getChildren().remove(Ungroup);
        HashSet<TuneRectangle> children = Ungroup.getChildren();
        children.forEach((child) -> {
            child.addToSelection();
        });
        return children;
    }

    public void add(TuneRectangle rect) {
        if(pane.getChildren().contains(rect)){
            return;
        }
        else if(rect instanceof NoteBar){
            add((NoteBar) rect);
        }
        else{
            add((Gesture) rect);
        }
    }
    /**
     * Adds something to the composition.
     * Should be called on a newly created or un-deleted note.
     * @param newRect the TuneRectangle to be added
     */
    public void add(NoteBar rect) {
        pane.getChildren().add(rect);
        rect.note.addToAllNotes();
        if(rect.getParentGesture() == null) allRoots.add(rect);
    }
    
    public void add(Gesture rect) {
        pane.getChildren().add(rect);
        rect.addChildrenToComposition();
        if(rect.getParentGesture() == null) allRoots.add(rect);
    }

    /**
     * Remove something from the composition.
     * @param rect the TuneRectangle to be removed
     */
    public void remove(TuneRectangle rect) {
        // TODO Remove redundant compositionpane removals elsewhere
        pane.getChildren().remove(rect);
        for(NoteBar child : rect.getChildLeaves()){
            child.note.removeFromAllNotes();
        }
        allRoots.remove(rect);
    }
    
    
    public void trackRectSelect(TuneRectangle rect){
        if(selectionChanges.contains(rect)){
            selectionChanges.remove(rect);
        }
        else{
            selectionChanges.add(rect);
        }
    }

    /**
     * Add an item to the selection.
     * Note that the style of the target TuneRectangle is not affected.
     * @param root the TuneRectangle to add
     */
    public void addToSelection(TuneRectangle root) {
        selectedRoots.add(root);
        trackRectSelect(root);
        updateBoundsNewRect(root); //changing selection is not properly changing the bounds
    }

    /**
     * Remove an item from the selection.
     * @param root the TuneRectangle to remove
     */
    public void removeFromSelection(TuneRectangle root) {
        selectedRoots.remove(root);
        root.removeSelectStyle();
        trackRectSelect(root);
        resetSelectionBounds();
    }
    
    private void updateBoundsNewRect(TuneRectangle root){
        if(selectionLeft == null || root.getX() < selectionLeft){selectionLeft = root.getX();}
        if(selectionTop == null || root.getY() < selectionTop){selectionTop = root.getY();}
        if(selectionRight == null || root.getX()+root.getWidth() > selectionRight){selectionRight = root.getX()+root.getWidth();}
        if(selectionBottom == null || root.getY() + Constants.LINE_SPACING > selectionBottom){selectionBottom = root.getY() + Constants.LINE_SPACING;}
        
    }
    
    public void resetSelectionBounds(){
        selectionLeft = null;
        selectionRight = null;
        selectionTop = null;
        selectionBottom = null;
        for (TuneRectangle rect : selectedRoots){
            updateBoundsNewRect(rect);
        }
    }
    
    public void resetSelectionRight(){
        selectionRight = null;
        for (TuneRectangle rect : selectedRoots){
            if(selectionRight == null || rect.getX()+rect.getWidth() > selectionRight){selectionRight = rect.getX()+rect.getWidth();}
        }
    }
    
    public void updateSelectionBounds(double deltaX, double deltaY){
        selectionLeft += deltaX;
        selectionRight += deltaX;
        selectionTop += deltaY;
        selectionBottom += deltaY;
    }

    /**
     * Tells whether the given TuneRectangle is selected.
     * @param root the TuneRectangle in question
     */
    public boolean isSelectedTop(TuneRectangle root) {
        return selectedRoots.contains(root);
    }

    /**
     * Resize the selected items.
     * @param deltaX the number of pixels to resize by
     */
    public void resizeSelected(double deltaX) {
        if(deltaX + selectionRight > Constants.WIDTH){deltaX = Constants.WIDTH - selectionRight;}
        for (TuneRectangle rect : selectedRoots) {
            if(rect instanceof NoteBar) {
                NoteBar bar = (NoteBar) rect;
                bar.resize(deltaX);
            }
        }
    }

    /**
     * Move the selected items.
     * @param deltaX the horizontal distance to move the selected items
     * @param deltaY the vertical distance to move the selected items
     */
    public void moveSelected(double deltaX, double deltaY) {
        if(selectionLeft + deltaX < 0){deltaX = -selectionLeft;}
        if(selectionRight + deltaX > Constants.WIDTH){deltaX = Constants.WIDTH-selectionRight;}
        if(selectionTop + deltaY < 0){deltaY = -selectionTop;}
        if(selectionBottom + deltaY > Constants.HEIGHT){deltaY = Constants.HEIGHT-selectionBottom;}
        for (TuneRectangle rect : selectedRoots) {
            rect.move(deltaX, deltaY);
        }
    }

    /**
     * Update the duration of selected notes.
     * This needs to be called after any number of NoteBars are resized.
     */
    public void updateResized() {
        for (TuneRectangle rect : selectedRoots) {
            if (rect instanceof NoteBar) {
                NoteBar bar = (NoteBar) rect;
                bar.updateNoteDuration();
            }
        }
    }

    /**
     * Update the start tick of selected notes.
     * This needs to be called after any number of NoteBars are moved.
     */
    public void updateMoved() {
        for (TuneRectangle rect : selectedRoots) {
            rect.updateNoteMoved();
        }
    }

    /**
     * Instructs each gesture to store its current location as a drag start.
     * This gets called when the mouse button is pressed.
     */
    public void setSelectionStart() {
        for (TuneRectangle rect : selectedRoots) {
            if(rect instanceof Gesture) {
                Gesture g = (Gesture) rect;
                g.setStart();
            }
        }
    }

    /**
     * Snap selected gestures to the horizontal lines after moving.
     * This only snaps gestures because notes snap themselves.
     */
    public void snapSelectionY() {
        for (TuneRectangle rect : selectedRoots) {
            if(rect instanceof Gesture) {
                Gesture g = (Gesture) rect;
                g.snapY();
            }
        }
    }

    /**
     * Checks if this composition contains nothing.
     * @return true if this composition contains nothing, otherwise false
     */
    public boolean isEmpty() {
        return allRoots.isEmpty();
    }
    
    /**
     * Checks if the selection is empty.
     */
    public boolean isSelectionEmpty() {
        return selectedRoots.isEmpty();
    }
}
