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
public class PasteCommand implements Command {

    private final Set<TuneRectangle> pasted;
    private final SelectionCommand selection;
    
    public PasteCommand(Set<TuneRectangle> added) {
        pasted = added;
        selection = new SelectionCommand();
    }
    /**
     * creates the notes
     */
    @Override
    public void execute() {
        for(TuneRectangle rect : pasted){
            TuneComposer.composition.add(rect);
        }
        selection.execute();
    }
    
    
    public void init() {
        for(TuneRectangle rect : pasted){
            rect.init();
        }
    }
    
    /**
     * uncreates the notes
     */
    @Override
    public void unexecute() {
        for(TuneRectangle rect : pasted){
            TuneComposer.composition.remove(rect);
        }
        selection.unexecute();
    }
    
}