/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;
import javafx.scene.control.MenuItem;


/**
 * Handles the graying out of menu items.
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
    private MenuItem cutButton;
    private MenuItem copyButton;
    private MenuItem pasteButton;
    private MenuItem aboutButton;
    private MenuItem newButton;
    private MenuItem saveButton;
    
    public TuneMenuBar (
            Composition composition,
            MenuItem stopButton,
            MenuItem playButton, 
            MenuItem selectNoneButton,
            MenuItem selectAllButton,
            MenuItem deleteButton,
            MenuItem groupButton,
            MenuItem ungroupButton,
            MenuItem undoButton,
            MenuItem redoButton,
            MenuItem cutButton,
            MenuItem copyButton,
            MenuItem pasteButton,
            MenuItem aboutButton,
            MenuItem newButton,
            MenuItem saveButton,
            MenuItem saveAsButton,
            MenuItem openButton) {

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
        this.cutButton          = cutButton;
        this.copyButton         = copyButton;
        this.pasteButton        = pasteButton;
        this.aboutButton        = aboutButton;
        this.newButton          = newButton;
        this.saveButton         = saveButton;

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
        disable(cutButton);
        disable(copyButton);
        disable(newButton);
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
        updateCopyCut();
        updateNew();
    }
    
    /**
     * Updates the 'Play' button, for a change in number of notes.
     */
    private void updatePlay() {
        playButton.setDisable(0 == composition.size());
    }
    
    /**
     * Updates the 'Selectall' and selectnone buttons.
     */
    private void updateSelect() {
        selectAllButton.setDisable(composition.size() <= composition.getSelectionSize());
        selectNoneButton.setDisable(0 == composition.getSelectionSize());
    }
    
    /**
     * Updates the 'Delete' button.
     */
    private void updateDelete() {
        deleteButton.setDisable(composition.getSelectionSize() == 0);
    }
    
    /**
     * Update 'Group' and 'Ungroup' buttons.
     */
    private void updateGroupUngroup() {
        groupButton.setDisable(composition.getSelectionSize() <= 1);
        ungroupButton.setDisable(!composition.isGestureSelected());
    }
    
    /**
     * Updates the 'Redo' and 'Undo' buttons.
     */
    private void updateRedoUndo() {
        redoButton.setDisable(!TuneComposer.history.canRedo());
        undoButton.setDisable(!TuneComposer.history.canUndo());        
    }

    /**
     * Updates the 'Copy' and 'Cut' buttons.
     */
    private void updateCopyCut() {
        boolean ability = composition.getSelectionSize() <= 0;
        copyButton.setDisable(ability);
        cutButton.setDisable(ability);
    }

    /**
     * Updates the 'New' button.
     */
    private void updateNew() {
        boolean hasHistory;
        hasHistory  = TuneComposer.history.canUndo();
        hasHistory |= TuneComposer.history.canRedo();

        newButton.setDisable(hasHistory && composition.isEmpty());
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
