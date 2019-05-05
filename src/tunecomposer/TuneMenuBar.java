/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;
import javafx.scene.control.MenuItem;


/**
 * handles the graying out of menu items
 * @author Ben, Ian, Spencer
 */
public class TuneMenuBar {
    
    private Composition composition;
    private MenuItem stopButton;
    private MenuItem playButton;
    private MenuItem selectNoneButton;
    private MenuItem selectAllButton;
    private MenuItem deleteButton;
    private MenuItem groupButton;
    private MenuItem ungroupButton;
    private MenuItem undoButton;
    private MenuItem redoButton;
    
    public TuneMenuBar(
            Composition composition,
            MenuItem stopButton,
            MenuItem playButton, 
            MenuItem selectNoneButton,
            MenuItem selectAllButton,
            MenuItem deleteButton,
            MenuItem groupButton,
            MenuItem ungroupButton,
            MenuItem undoButton,
            MenuItem redoButton) {

        this.composition        = composition;
        this.stopButton         = stopButton;
        this.playButton         = playButton;
        this.selectNoneButton   = selectNoneButton;
        this.selectAllButton    = selectAllButton;
        this.deleteButton       = deleteButton;
        this.groupButton        = groupButton;
        this.ungroupButton      = ungroupButton;
        this.undoButton         = undoButton;
        this.redoButton         = redoButton;

        setup();
    }

    /**
     * Starts all buttons as disabled.
     */
    public void setup() {
        disable(stopButton);
        disable(selectAllButton);
        disable(selectNoneButton);
        disable(deleteButton);
        disable(playButton);
        disable(ungroupButton);
        disable(groupButton);
        disable(undoButton);
        disable(redoButton);
    }

    /**
     * Checks all non stop button buttons and updates their disability.
     */
    public void update() {
        updatePlay();
        updateSelect();
        updateDelete();
        updateGroupUngroup();
        updateRedoUndo();
    }
    
    /**
     * Updates the play button, for a change in number of notes.
     */
    private void updatePlay() {
        playButton.setDisable(0 == composition.size());
    }
    
    /**
     * Updates the selectall and selectnone buttons.
     */
    private void updateSelect(){
        selectAllButton.setDisable(composition.size() <= composition.getSelectionSize());
        selectNoneButton.setDisable(0 == composition.getSelectionSize());
    }
    
    /**
     * Updates the delete button.
     */
    private void updateDelete(){
        deleteButton.setDisable(composition.getSelectionSize() == 0);
    }
    
    /**
     * Update group and ungroup buttons.
     */
    private void updateGroupUngroup(){
        groupButton.setDisable(composition.getSelectionSize() <= 1);
        ungroupButton.setDisable(!composition.isGestureSelected());
    }
    
    /**
     * Updates the redo and undo buttons.
     */
    private void updateRedoUndo(){
        redoButton.setDisable(!TuneComposer.history.canRedo());
        undoButton.setDisable(!TuneComposer.history.canUndo());        
    }

    /**
     * Enable a menu item, un-greying it and making it clickable.
     * This also enables the corresponding keyboard shortcut.
     * Added for readability.
     * @param item the button to be enabled
     */
    private void enable(MenuItem button) {
        button.setDisable(false);
    }

    /**
     * Disable a menu item, greying it out and making it unclickable.
     * This also disables the corresponding keyboard shortcut.
     * Added for readability.
     * @param item the button to be disabled
     */
    private void disable(MenuItem button) {
        button.setDisable(true);
    }
}
