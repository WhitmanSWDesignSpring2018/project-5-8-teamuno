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
public class SelectionCommand implements Command{
    
    private final Set<TuneRectangle> changedRects;

    public SelectionCommand() {
        changedRects = new HashSet<>(TuneComposer.composition.getSelectionTracker());
        TuneComposer.composition.clearSelectionTracker();
    }
    @Override
    public void execute() {
        toggle();
    }

    @Override
    public void unexecute() {
        toggle();
    }
    
    private void toggle(){
        for(TuneRectangle rect : changedRects){
            if(TuneComposer.composition.isSelectedTop(rect)){
                TuneComposer.composition.removeFromSelection(rect);
            }
            else{
                rect.addToSelection();
            }
        }
    }
    
}
