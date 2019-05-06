/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

import java.util.HashSet;
import tunecomposer.Composition;
import tunecomposer.NoteBar;
import tunecomposer.TuneRectangle;

/**
 * Command for deleting some TuneRectangles.
 * @author Spencer
 */
public class DeletionCommand implements Command {
    private final Composition composition;
    private final HashSet<TuneRectangle> deletedRects;
    
    public DeletionCommand(Composition composition, HashSet<TuneRectangle> rects) {
        this.composition = composition;
        deletedRects = rects;
        new SelectionCommand(composition);
    }
    
    /**
     * Deletes the note.
     */
    @Override
    public void execute() {
        for(TuneRectangle rect : deletedRects){
            composition.remove(rect);
        }
    }

    /**
     * Undeletes the note.
     */
    @Override
    public void unexecute() {
        composition.clearSelection();
        for (TuneRectangle rect : deletedRects){
            composition.add(rect);
            rect.addToSelection();
        }
    }
    
}
