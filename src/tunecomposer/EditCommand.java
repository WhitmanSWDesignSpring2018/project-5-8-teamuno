/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.HashSet;

/**
 *
 * @author vankoesd
 */
public class EditCommand implements Command {
    
    private final HashSet<TuneRectangle> editedRects;
    private final int lengthChange;

    public EditCommand(HashSet<TuneRectangle> edits /*length change and 2 of, start, end and movement */) {
        editedRects = edits;
        lengthChange = 0; //CHANGE THIS LATER, this is just so it can run
    }
    
    @Override
    public void execute() {
        
    }

    @Override
    public void unexecute() {
        
    }
    
}
