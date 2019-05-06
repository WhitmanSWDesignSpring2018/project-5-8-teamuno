/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

import tunecomposer.command.SelectionCommand;
import java.util.HashSet;
import java.util.Set;
import tunecomposer.Composition;
import tunecomposer.NoteBar;
import tunecomposer.TuneRectangle;

/**
 * Command for resizing NoteBars.
 * @author Spencer
 */
public class ResizeCommand implements Command {

    private final Composition composition;
    private final Set<TuneRectangle> editedRects;
    private final double lengthChange;
    private final Command selection;

    public ResizeCommand(Composition composition, double dragLength) {
        this.composition = composition;
        editedRects = composition.getSelectedRoots();
        lengthChange = dragLength;
        selection = new SelectionCommand(composition);
    }
    
    /**
     * Extends note to its changed length.
     */
    @Override
    public void execute() {
        for(TuneRectangle rect : editedRects) {
            if(rect instanceof NoteBar && rect.getParentGesture() == null) {
                ((NoteBar) rect).resize(lengthChange);
            }
        }
        composition.updateResized();
        selection.execute();
    }

    /**
     * Reverts note to its previous length.
     */
    @Override
    public void unexecute() {
        for(TuneRectangle rect : editedRects) {
            if(rect instanceof NoteBar && rect.getParentGesture() == null) {
                ((NoteBar) rect).resize(-lengthChange);
            }
        }
        composition.updateResized();
        selection.unexecute();
    }
}
