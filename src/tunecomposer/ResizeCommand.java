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
        
    private final Set<TuneRectangle> editedRects;
    private final double lengthChange;
    private final Command selection;

    public ResizeCommand(Set<TuneRectangle> edits, double dragLength) {
        editedRects = edits;
        lengthChange = dragLength;
        System.out.println(dragLength);
        System.out.println(edits);
        selection = new SelectionCommand();
    }
    
    @Override
    public void execute() {
        for(TuneRectangle rect : editedRects){
            if(rect instanceof NoteBar && rect.getParentGesture() == null){
                ((NoteBar) rect).resize(lengthChange);
            }
        }
        TuneComposer.composition.updateResized();
        selection.execute();
    }

    @Override
    public void unexecute() {
        for(TuneRectangle rect : editedRects){
            if(rect instanceof NoteBar && rect.getParentGesture() == null){
                ((NoteBar) rect).resize(-lengthChange);
            }
        }
        TuneComposer.composition.updateResized();
        selection.unexecute();
    }
}
