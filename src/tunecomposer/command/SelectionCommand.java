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
 * Command for selecting TuneRectangles.
 * @author Spencer
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
     * Toggles the selection status of each targeted note.
     */
    @Override
    public void execute() {
        toggle();
    }
    
    /**
     * Toggles the selection status of each targeted note.
     */
    @Override
    public void unexecute() {
        toggle();
    }
    
    /**
     * Toggles the selection status of each targeted note.
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
