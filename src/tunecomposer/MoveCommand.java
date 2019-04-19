/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Set;

/**
 *
 * @author vankoesd
 */

//does not work because the Gesture.move method is wierd and different from Note.move

//TODO fix that
public class MoveCommand implements Command {
    
    private final Set<TuneRectangle> editedRects;
    private final double xChange;
    private final double yChange;
    private final Command selection;

    public MoveCommand(Set edits, double dX, double dY) {
        editedRects = edits;
        xChange = dX;
        yChange = dY;
        selection = new SelectionCommand();
    }
    
    /**
     * moves notes forward to where they went
     */
    @Override
    public void execute() {
        for(TuneRectangle rect : editedRects){
            if(rect instanceof Gesture){((Gesture)rect).setStart();}
            rect.move(xChange, yChange);
            if(rect instanceof Gesture){((Gesture)rect).snapY();}
            rect.updateNoteMoved();
        }
        selection.execute();
    }

    /**
     * moves notes back to where they started
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
