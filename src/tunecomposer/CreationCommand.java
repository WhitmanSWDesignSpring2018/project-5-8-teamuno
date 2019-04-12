/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

/**
 *
 * @author vankoesd
 */
public class CreationCommand implements Command {

    private final NoteBar newNote;
    //private final SelectionCommand selection;
    
    public CreationCommand(NoteBar created/*, SelectionCommand selectionChange*/) {
        newNote = created;
        //selection = selectionChange;
    }
    
    @Override
    public void execute() {
        TuneComposer.composition.add(newNote);
        //selection.execute();
    }

    @Override
    public void unexecute() {
        TuneComposer.composition.remove(newNote);
        //selection.unexecute();
    }
    
}
