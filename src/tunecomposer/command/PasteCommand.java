/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

import tunecomposer.command.SelectionCommand;
import java.util.Set;
import tunecomposer.Composition;
import tunecomposer.TuneComposer;
import tunecomposer.TuneRectangle;

/**
 * Command for pasting TuneRectangles.
 * @author Spencer
 */
public class PasteCommand implements Command {

    private final Set<TuneRectangle> pasted;
    private final SelectionCommand selection;
    private final Composition composition;
    
    public PasteCommand(Composition composition, Set<TuneRectangle> added) {
        this.composition = composition;
        pasted = added;
        selection = new SelectionCommand(composition);
    }
    /**
     * Pastes the notes from the clipboard.
     */
    @Override
    public void execute() {
        for(TuneRectangle rect : pasted) {
            composition.add(rect);
        }
        selection.execute();
    }
    
    
    public void init() {
        for(TuneRectangle rect : pasted) {
            rect.init(composition);
        }
    }
    
    /**
     * Uncreates (unpastes) the notes.
     */
    @Override
    public void unexecute() {
        for(TuneRectangle rect : pasted) {
            TuneComposer.composition.remove(rect);
        }
        selection.unexecute();
    }
}
