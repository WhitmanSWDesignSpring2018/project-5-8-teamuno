/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;
import javafx.scene.control.MenuItem;


/**
 * TODO javadoc
 * @author Ben, Ian
 */
public class TuneMenuBar {
    
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
            MenuItem stopButton,
            MenuItem playButton, 
            MenuItem selectNoneButton,
            MenuItem selectAllButton,
            MenuItem deleteButton,
            MenuItem groupButton,
            MenuItem ungroupButton,
            MenuItem undoButton,
            MenuItem redoButton) {

        this.stopButton         = stopButton;
        this.playButton         = playButton;
        this.selectNoneButton   = selectNoneButton;
        this.selectAllButton    = selectAllButton;
        this.deleteButton       = deleteButton;
        this.groupButton        = groupButton;
        this.ungroupButton      = ungroupButton;
        this.undoButton         = undoButton;
        this.redoButton         = redoButton;
    }

    /**
     * TODO
     */
    public void setup() {
        disable(stopButton);
        disable(selectAllButton);
        disable(selectNoneButton);
        disable(deleteButton);
        disable(playButton);
        disable(ungroupButton);
        disable(groupButton);
    }

    /**
     * TODO
     */
    public void onSelection(boolean allSelected, boolean singleGesture,
            boolean noGesture) {
        if (allSelected) {
            disable(selectAllButton);
        }

        if (singleGesture) {
            disable(groupButton);
        } else if (noGesture) {
            disable(ungroupButton);
        }

        enable(selectNoneButton);
        enable(deleteButton);
        enable(ungroupButton);
        enable(groupButton);
    }

    /**
     * TODO
     */
    public void onNoteCreation() {
        enable(selectAllButton);
        enable(playButton);
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
   
