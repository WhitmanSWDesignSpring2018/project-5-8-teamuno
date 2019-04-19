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
    private final SelectionCommand selection;
    
    public CreationCommand(NoteBar created) {
        newNote = created;
        selection = new SelectionCommand();
    }
    /**
     * creates the note
     */
    @Override
    public void execute() {
        TuneComposer.composition.add(newNote);
        selection.execute();
    }
    /**
     * uncreates the note
     */
    @Override
    public void unexecute() {
        TuneComposer.composition.remove(newNote);
        selection.unexecute();
    }
    
}
