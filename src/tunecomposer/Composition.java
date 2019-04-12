
package tunecomposer;

import java.util.HashSet;
import javafx.scene.layout.Pane;

public class Composition {
    private HashSet<TuneRectangle> allTop;
    private HashSet<TuneRectangle> selectionTop;
    private Pane pane;

    public Composition(Pane compositionpane) {
        pane = compositionpane;
        allTop = new HashSet<>();
        selectionTop = new HashSet<>();
    }

    public void selectAll() {
        clearSelection();
        for(TuneRectangle rect : allTop) {
            rect.addToSelection();
        }
    }

    public void clearSelection() {
        for(TuneRectangle rect : selectionTop) {
            rect.removeSelectStyle();
        }
        selectionTop.clear();
    }

    public void deleteSelection() {
        for(TuneRectangle rect : selectionTop) {
            remove(rect);
            rect.delete(pane);
        }
        selectionTop.clear();
    }

    public void groupSelection() {
        if(selectionTop.isEmpty()) {return;}

        // Pass the selection by value, not by reference
        HashSet<TuneRectangle> group = new HashSet<>(selectionTop);
        Gesture newGesture = new Gesture(group);
        HashSet<Gesture> forCommand = new HashSet();
        forCommand.add(newGesture);
        TuneComposer.history.addNewCommand(new GroupCommand(forCommand, true));
        
    }

    public void ungroupSelected() {
        HashSet<HashSet<TuneRectangle>> forCommand = new HashSet();
        for(TuneRectangle p : selectionTop) {
            if (p instanceof Gesture) {
                Gesture g = (Gesture) p;
                selectionTop.remove(g);
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
        if(selectionTop.contains(Ungroup)){selectionTop.remove(Ungroup);}
        Ungroup.freeChildren();
        pane.getChildren().remove(Ungroup);
        HashSet<TuneRectangle> children = Ungroup.getChildren();
        children.forEach((child) -> {
            child.addToSelection();
        });
        return children;
    }

    /**
     * Adds something to the composition.
     * Should be called on a newly created or un-deleted note.
     * @param newRect the TuneRectangle to be added
     */
    public void add(TuneRectangle newRect) {
        pane.getChildren().add(newRect);
        allTop.add(newRect);
    }

    /**
     * Remove something from the composition.
     * @param rect the TuneRectangle to be removed
     */
    public void remove(TuneRectangle rect) {
        // TODO Remove redundant compositionpane removals elsewhere
        pane.getChildren().remove(rect);
        allTop.remove(rect);
    }

    /**
     * Add an item to the selection.
     * Note that the style of the target TuneRectangle is not affected.
     * @param root the TuneRectangle to add
     */
    public void addToSelection(TuneRectangle root) {
        selectionTop.add(root);
    }

    /**
     * Remove an item from the selection.
     * @param root the TuneRectangle to remove
     */
    public void removeFromSelection(TuneRectangle root) {
        selectionTop.remove(root);
        root.removeSelectStyle();
    }

    public boolean isSelectedTop(TuneRectangle root) {
        return selectionTop.contains(root);
    }

    public void resizeSelected(double deltaX) {
        for (TuneRectangle rect : selectionTop) {
            if(rect instanceof NoteBar) {
                NoteBar bar = (NoteBar) rect;
                bar.resize(deltaX);
            }
        }
    }

    public void moveSelected(double deltaX, double deltaY) {
        for (TuneRectangle rect : selectionTop) {
            rect.move(deltaX, deltaY);
        }
    }

    public void updateResized() {
        for (TuneRectangle rect : selectionTop) {
            if (rect instanceof NoteBar) {
                NoteBar bar = (NoteBar) rect;
                bar.updateNoteDuration();
            }
        }
    }

    public void updateMoved() {
        for (TuneRectangle rect : selectionTop) {
            rect.updateNoteMoved();
        }
    }

    public void setSelectionStart() {
        for (TuneRectangle rect : selectionTop) {
            if(rect instanceof Gesture) {
                Gesture g = (Gesture) rect;
                g.setStart();
            }
        }
    }

    public void snapSelectionY() {
        for (TuneRectangle rect : selectionTop) {
            if(rect instanceof Gesture) {
                Gesture g = (Gesture) rect;
                g.snapY();
            }
        }
    }
}
