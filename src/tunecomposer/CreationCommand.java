/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.scene.layout.Pane;

/**
 *
 * @author vankoesd
 */
public class CreationCommand implements Command {

    private final NoteBar newNote;
    private final SelectionCommand selection;
    private final Pane compositionPane;
    
    public CreationCommand(NoteBar created, SelectionCommand selectionChange, Pane pane) {
        newNote = created;
        selection = selectionChange;
        compositionPane = pane;
    }
    
    @Override
    public void execute() {
        TuneComposer.composition.add(rect);
        selection.execute();
    }

    @Override
    public void unexecute() {
        TuneComposer.composition.remove(rect);
        selection.unexecute();
    }
    
}
