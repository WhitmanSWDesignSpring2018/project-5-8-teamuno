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

        setup();
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
        disable(undoButton);
        disable(redoButton);
    }

    public void update(){
        updatePlay();
        updateSelect();
        updateDelete();
        updateGroupUngroup();
        updateRedoUndo();
    }
    
    private void updatePlay() {
        playButton.setDisable(0 == TuneComposer.composition.size());
    }
    
    private void updateSelect(){
        selectAllButton.setDisable(TuneComposer.composition.size() <= TuneComposer.composition.getSelectionSize());
        selectNoneButton.setDisable(0 == TuneComposer.composition.getSelectionSize());
    }
    
    private void updateDelete(){
        deleteButton.setDisable(TuneComposer.composition.getSelectionSize() == 0);
    }
    
    private void updateGroupUngroup(){
        groupButton.setDisable(TuneComposer.composition.getSelectionSize() <= 1);
        ungroupButton.setDisable(!TuneComposer.composition.isGestureSelected());
    }
    
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
   
