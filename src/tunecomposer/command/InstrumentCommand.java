/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

import java.util.HashSet;
import java.util.Map;
import tunecomposer.Instrument;
import tunecomposer.NoteBar;
import static tunecomposer.TuneComposer.composition;
import tunecomposer.TuneRectangle;

/**
 * Command for changing the instrument on rectangles.
 * @author Spencer
 */
public class InstrumentCommand implements Command {
    private final Map initialInsts;
    private final Instrument newInst;
    private final HashSet<TuneRectangle> changedRects;
    
    public InstrumentCommand(HashSet<TuneRectangle> rects, Instrument changedTo, Map firstInsts) {
        initialInsts = firstInsts;
        newInst = changedTo;
        changedRects = rects;
        new SelectionCommand(composition);
    }
    
    /**
     * Deletes the note.
     */
    @Override
    public void execute() {
        for(TuneRectangle rect : changedRects){
            rect.changeInstruments(newInst);
        }
    }

    /**
     * Undeletes the note.
     */
    @Override
    public void unexecute() {
        for(TuneRectangle rect : changedRects){
            for(NoteBar bar : rect.getChildLeaves()){
                bar.changeInstruments((Instrument) initialInsts.get(bar));
            }
        }
    }
    
}
