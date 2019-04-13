/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author vankoesd
 */
public class MoveCommand implements Command {
    
    private final Set<TuneRectangle> editedRects;
    private final double xChange;
    private final double yChange;
    private final Command selection;

    public MoveCommand(HashSet<TuneRectangle> edits, double dX, double dY) {
        editedRects = edits;
        xChange = dX;
        yChange = dY;
        selection = new SelectionCommand();
    }
    
    @Override
    public void execute() {
        for(TuneRectangle rect : editedRects){
            rect.setX(rect.getX()+xChange);
            rect.setY(rect.getY()+yChange);
        }
        selection.execute();
    }

    @Override
    public void unexecute() {
        for(TuneRectangle rect : editedRects){
            rect.setX(rect.getX()-xChange);
            rect.setY(rect.getY()-yChange);
        }
        selection.unexecute();
    }
    
}
