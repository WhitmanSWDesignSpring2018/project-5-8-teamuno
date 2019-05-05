/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

import java.util.HashSet;
import java.util.Set;
import tunecomposer.Composition;
import tunecomposer.TuneRectangle;

/**
 *
 * @author vankoesd
 */
public class SelectionCommand implements Command{
    
    private final Composition composition;
    private final Set<TuneRectangle> changedRects;

    public SelectionCommand(Composition composition) {
        this.composition = composition;
        changedRects = new HashSet<>(composition.getSelectionTracker());
        composition.clearSelectionTracker();
    }
    
    /**
     * calls toggle
     */
    @Override
    public void execute() {
        toggle();
    }
    
    /**
     * calls toggle
     */
    @Override
    public void unexecute() {
        toggle();
    }
    
    /**
     * toggles selection on all given notes
     */
    private void toggle(){
        for(TuneRectangle rect : changedRects){
            if(composition.isSelectedTop(rect)){
                composition.removeFromSelection(rect);
            }
            else{
                rect.addToSelection();
            }
        }
    }
    
}
