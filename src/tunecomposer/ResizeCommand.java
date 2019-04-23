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
public class ResizeCommand implements Command {

    private final Composition composition;
    private final Set<TuneRectangle> editedRects;
    private final double lengthChange;
    private final Command selection;

    public ResizeCommand(Composition composition, Set<TuneRectangle> edits,
            double dragLength) {
        this.composition = composition;
        editedRects = edits;
        lengthChange = dragLength;
        System.out.println(dragLength);
        System.out.println(edits);
        selection = new SelectionCommand(composition);
    }
    
    /**
     * extends note to its changed length
     */
    @Override
    public void execute() {
        for(TuneRectangle rect : editedRects){
            if(rect instanceof NoteBar && rect.getParentGesture() == null){
                ((NoteBar) rect).resize(lengthChange);
            }
        }
        composition.updateResized();
        selection.execute();
    }

    /**
     * reverts note length to its initial value
     */
    @Override
    public void unexecute() {
        for(TuneRectangle rect : editedRects){
            if(rect instanceof NoteBar && rect.getParentGesture() == null){
                ((NoteBar) rect).resize(-lengthChange);
            }
        }
        composition.updateResized();
        selection.unexecute();
    }
}
