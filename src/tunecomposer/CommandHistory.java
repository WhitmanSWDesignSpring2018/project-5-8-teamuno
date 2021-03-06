/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import tunecomposer.command.*;
import java.util.Stack;

/**
 * Keeps track of commands, as well as undoing and redoing them.
 * @author Spencer
 */
public class CommandHistory {
    private final Stack<Command> undoableCommands;
    private final Stack<Command> undoneCommands;
    private boolean isSaved = true;
    private final Composition composition;

    public CommandHistory(Composition composition) {
        this.composition = composition;
        undoableCommands = new Stack<>();
        undoneCommands = new Stack<>();
    }
    
    /**
     * Records a new command that can be undone later.
     * @param toAdd, the new command
     */
    public void addNewCommand(Command toAdd) {
        undoableCommands.push(toAdd);
        undoneCommands.clear();
        isSaved = false;
        TuneComposer.menuBar.update();
    }
    
    /**
     * Undoes the top command and makes it available for redo.
     */
    public void undo() {
        if(canUndo()) {
            Command undoing = undoableCommands.pop();
            undoing.unexecute();
            undoneCommands.push(undoing);
            composition.clearSelectionTracker();
            isSaved = false;
        }
    }
    
    /**
     * Redoes the top command and makes it available for undo.
     */
    public void redo() {
        if(canRedo()) {
            Command redoing = undoneCommands.pop();
            redoing.execute();
            undoableCommands.push(redoing);
            composition.clearSelectionTracker();
            isSaved = false;
        }
    }
    
    /**
     * Checks if it is possible to undo.
     * @return boolean
     */
    public boolean canUndo() {
        return !undoableCommands.isEmpty();
    }
    
    /**
     * Checks if it is possible to redo.
     * @return boolean
     */
    public boolean canRedo() {
        return !undoneCommands.isEmpty();
    }
    
    public void recordSave() {
        isSaved = true;
    }
    
    public boolean isSaved() {
        return isSaved;
    }
    
    public void clear() {
        undoableCommands.clear();
        undoneCommands.clear();
    }
}
