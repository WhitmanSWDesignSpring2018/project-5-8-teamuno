/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

import tunecomposer.Composition;
import tunecomposer.NoteBar;
import tunecomposer.command.SelectionCommand;

/**
 * Command for creating a new NoteBar.
 * @author Spencer
 */
public class CreationCommand implements Command {

    private final Composition composition;
    private final NoteBar newNote;
    private final SelectionCommand selection;
    
    public CreationCommand(Composition composition, NoteBar created) {
        this.composition = composition;
        newNote = created;
        selection = new SelectionCommand(composition);
    }
    /**
     * Creates the note.
     */
    @Override
    public void execute() {
        composition.add(newNote);
        selection.execute();
    }
    /**
     * Uncreates the note.
     */
    @Override
    public void unexecute() {
        composition.remove(newNote);
        selection.unexecute();
    }
    
}
