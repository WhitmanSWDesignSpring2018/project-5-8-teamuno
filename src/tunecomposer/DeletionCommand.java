/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.HashSet;

/**
 *
 * @author vankoesd
 */
public class DeletionCommand implements Command {
    private final HashSet<TuneRectangle> deletedRects;
    
    public DeletionCommand(HashSet<TuneRectangle> rects) {
        deletedRects = rects;
        new SelectionCommand();
    }
    
    @Override
    public void execute() {
        for(TuneRectangle rect : deletedRects){
            TuneComposer.composition.remove(rect);
        }
    }

    @Override
    public void unexecute() {
        TuneComposer.composition.clearSelection();
        for (TuneRectangle rect : deletedRects){
            TuneComposer.composition.add(rect);
            rect.addToSelection();
        }
    }
    
}
