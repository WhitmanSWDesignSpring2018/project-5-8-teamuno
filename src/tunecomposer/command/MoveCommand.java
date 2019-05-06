/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

import tunecomposer.command.SelectionCommand;
import java.util.Set;
import tunecomposer.Composition;
import tunecomposer.Gesture;
import tunecomposer.TuneRectangle;

/**
 * Command for moving selected TuneRectangles.
 * @author Spencer
 */
public class MoveCommand implements Command {
    
    private final Composition composition;
    private final Set<TuneRectangle> editedRects;
    private final double xChange;
    private final double yChange;
    private final Command selection;

    public MoveCommand(Composition composition, double dX, double dY) {
        this.composition = composition;
        editedRects = composition.getSelectedRoots();
        xChange = dX;
        yChange = dY;
        selection = new SelectionCommand(composition);
    }
    
    /**
     * Moves notes.
     */
    @Override
    public void execute() {
        for(TuneRectangle rect : editedRects) {
            if (rect instanceof Gesture) {
                ((Gesture)rect).setStart();
            }

            rect.move(xChange, yChange);
            if (rect instanceof Gesture) {
                ((Gesture)rect).snapY();
            }
            rect.updateNoteMoved();
        }
        selection.execute();
    }

    /**
     * Moves notes back to their locations before being moved.
     */
    @Override
    public void unexecute() {
        for(TuneRectangle rect : editedRects){
            if(rect instanceof Gesture){((Gesture)rect).setStart();}
            rect.move(-xChange, -yChange);
            if(rect instanceof Gesture){((Gesture)rect).snapY();}
            rect.updateNoteMoved();
        }
        selection.unexecute();
    }
    
}
