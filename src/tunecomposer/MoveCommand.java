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
        System.out.println(editedRects);
        selection = new SelectionCommand();
    }
    
    @Override
    public void execute() {
        for(TuneRectangle rect : editedRects){
            rect.jump(xChange, yChange);
            rect.updateNoteMoved();
        }
        selection.execute();
    }

    @Override
    public void unexecute() {
        for(TuneRectangle rect : editedRects){
            rect.jump(-xChange, -yChange);
            rect.updateNoteMoved();
        }
        selection.unexecute();
    }
    
}
