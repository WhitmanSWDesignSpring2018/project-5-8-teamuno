/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;
import javafx.scene.control.MenuItem;


/**
 *
 * @author hetterbl
 */
public class TuneMenuBar {
    
    private MenuItem stopButton;
    private MenuItem playButton;
    private MenuItem deleteButton;
    private MenuItem selectAllButton;
    private MenuItem selectNoneButton;
    private MenuItem groupButton;
    private MenuItem ungroupButton;
    
    public TuneMenuBar(MenuItem stopButton, MenuItem playButton, 
            MenuItem selectNoneButton,MenuItem selectAllButton,
            MenuItem deleteButton, MenuItem groupButton, MenuItem ungroupButton) 
    {
        this.stopButton = stopButton;
        this.playButton = playButton;
        this.selectNoneButton = selectNoneButton;
        this.selectAllButton = selectAllButton;
        this.deleteButton = deleteButton;
        this.groupButton = groupButton;
        this.ungroupButton = ungroupButton;
    }
    public void setup() {
        stopButton.setDisable(true);
        selectAllButton.setDisable(true);
        selectNoneButton.setDisable(true);
        deleteButton.setDisable(true);
        playButton.setDisable(true);
        ungroupButton.setDisable(true);
        groupButton.setDisable(true);
    }
    public void onSelection(boolean allSelected, boolean singleGesture,
            boolean noGesture) {
        if (allSelected) {
            selectAllButton.setDisable(true);
        }
        if (singleGesture) {
            groupButton.setDisable(true);
        } else if (noGesture) {
            ungroupButton.setDisable(true);
        }
        selectNoneButton.setDisable(false);
        deleteButton.setDisable(false);
        ungroupButton.setDisable(false);
        groupButton.setDisable(false);
        
    }
    public void onNoteCreation() {
        selectAllButton.setDisable(false);
        playButton.setDisable(false);
    }
}   
   