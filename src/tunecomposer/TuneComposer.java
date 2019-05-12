/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */ 
package tunecomposer;

import tunecomposer.command.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

/**
 * This JavaFX app lets the user compose music.
 * @author Ian, Spencer, Ben, Taka, Janet
 */
public class TuneComposer extends Application {

    private final MidiPlayer player;

    public static Composition composition;
    public static TuneMenuBar menuBar;
    private Stage primaryStage;
    final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final Instruments instruments = new Instruments();

    @FXML private Pane compositionpane;
    @FXML private Pane instrumentpane;
    @FXML private ToggleGroup instrumentgroup;

    // Menu bar buttons
    @FXML private MenuItem selectAllButton;
    @FXML private MenuItem selectNoneButton;
    @FXML private MenuItem deleteButton;
    @FXML private MenuItem groupButton;
    @FXML private MenuItem ungroupButton;
    @FXML private MenuItem stopButton;
    @FXML private MenuItem playButton;
    @FXML private MenuItem undoButton;
    @FXML private MenuItem redoButton;
    @FXML private MenuItem cutButton;
    @FXML private MenuItem copyButton;
    @FXML private MenuItem pasteButton;
    @FXML private MenuItem aboutButton;
    @FXML private MenuItem newButton;
    @FXML private MenuItem saveButton;
    @FXML private MenuItem saveAsButton;
    @FXML private MenuItem openButton;
 
    private File currentFile;
    private FileChooser fileChooser;
    private FileChooser importChooser;
    private Line playLine;
    private TranslateTransition playAnimation;
    private Rectangle selectionRect;

    private static final int CONFIRMATIONYES = 1;
    private static final int CONFIRMATIONNO = 2;
    private static final int CONFIRMATIONCANCEL = 3;

    public static CommandHistory history;

    /**
     * Construct a new composition pane.
     */
    public TuneComposer() {
        player = new MidiPlayer(Constants.TICKS_PER_BEAT,
                                Constants.BEATS_PER_MINUTE);
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Compositions", "*.tcom")
        );
        importChooser = new FileChooser();
        List<String> toAdd = new ArrayList<>();
        toAdd.add("*.midi");
        toAdd.add("*.mid");
        importChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Compositions", toAdd)
        );
    }

    /**
     * Play the sounds and the animation from the beginning.
     */
    private void play() {
        stopPlaying();
        if (!Note.isEmpty()) {
            player.stop();
            player.clear();
            instruments.addAll(player);
            Note.playAllNotes(player);
            player.play();

            int end = Note.getLastTick();
            playAnimation.setToX(end);
            playAnimation.setDuration(Constants.ticksToDuration(end));
            playAnimation.playFromStart();
        }
    }

    /**
     * Stop the sounds and the animation. Reset the animation line to
     * offscreen left.
     */
    private void stopPlaying() {
        player.stop();
        player.clear();
        playAnimation.stop();
        resetPlayLine();
    }

    /**
     * Prepares the instrument pane and group so that instruments may be selected
     */
    private void setupInstruments() {
        boolean first = true;
        for (Iterator<Instrument> it = instruments.getInstruments().iterator(); it.hasNext();) {
            Instrument inst = it.next();
            RadioButton rb = new RadioButton();
            rb.setText(inst.getDisplayName());
            rb.setTextFill(inst.getDisplayColor());
            rb.setUserData(inst);
            rb.setToggleGroup(instrumentgroup);
            instrumentpane.getChildren().add(rb);
            if (first) {
                instrumentgroup.selectToggle(rb);
                first = false;
            }
        }
    }

    /**
     * Draws horizontal lines in the background.  The lines are spaced
     * vertically by LINE_SPACING and have CSS class pitchline.
     */
    private void drawLines() {
        for (int i = 1; i < Constants.NUM_PITCHES; i++) {
            int ypos = Constants.LINE_SPACING * i;
            Line line = new Line();
            line.setStartX(0);
            line.setStartY(ypos);
            line.setEndX(Constants.WIDTH);
            line.setEndY(ypos);
            line.getStyleClass().add("pitchline");
            compositionpane.getChildren().add(line);
        }
    }

    /**
     * Create the moving play line.
     */
    private void setupAnimation() {
        playLine = new Line(0, 0, 0, Constants.HEIGHT);
        playLine.setTranslateX(-1);     // just offscreen
        playLine.getStyleClass().add("playline");
        compositionpane.getChildren().add(playLine);

        playAnimation = new TranslateTransition(Duration.seconds(0), playLine);
        playAnimation.setInterpolator(Interpolator.LINEAR);
        playAnimation.setOnFinished((ActionEvent e) -> { 
            resetPlayLine();
            stopButton.setDisable(true);
        });
    }

    /**
     * Prepares the composition.
     */
    private void setupComposition() {
        composition = new Composition(compositionpane);
    }

    /**
     * Set up the empty command history for undo and redo.
     */
    private void setupCommandHistory() {
        history = new CommandHistory(composition);
    }

    /**
     * Set up the menu bar to enable and disable buttons.
     */
    private void setupMenuBar() {
        menuBar = new TuneMenuBar(
                composition,
                stopButton,
                playButton,
                selectNoneButton,
                selectAllButton,
                deleteButton,
                groupButton,
                ungroupButton,
                undoButton,
                redoButton,
                cutButton,
                copyButton,
                pasteButton,
                aboutButton,
                newButton,
                saveButton,
                saveAsButton,
                openButton);
    }

    /**
     * Reset the position of the play line to offscreen left.
     */
    private void resetPlayLine() {
         playLine.setTranslateX(-1);
    }

    /**
     * Creates the rectangle used for selection so that it may be used later.
     */
    private void setupSelectionRect() {
        selectionRect = new Rectangle();
        selectionRect.setId("selectionarea");
        selectionRect.setVisible(false);
        compositionpane.getChildren().add(selectionRect);
    }
    
    /**
     * Creates and displays a confirmation alert about saving before
     * continuing.
     * 
     * @return CONFIRMATIONYES, CONFIRMATIONNO, or CONFIRMATIONCANCEL
     */
    private int ConfirmationAlert() {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Progress");
            alert.setHeaderText("You have not saved your progress.");
            alert.setContentText("Would you like to save before continuing?");

            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeTwo = new ButtonType("No");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                return CONFIRMATIONYES;
            } else if (result.get() == buttonTypeTwo) {
                return CONFIRMATIONNO;
            }
            else{
                return CONFIRMATIONCANCEL;
            }
    }
    
    /**
     * Clears the pane and history.
     * https://code.makery.ch/blog/javafx-dialogs-official/
     * @param event 
     */
    @FXML
    protected void handleNewAction(ActionEvent event) {
        if(!history.isSaved()) {
            int choice = ConfirmationAlert();
            if (choice == CONFIRMATIONYES){
                handleSaveAction(event);
                composition.clearAll();
                history.clear();
            } else if (choice == CONFIRMATIONNO) {
                composition.clearAll();
                history.clear();
            }
        }
        else {
            composition.clearAll();
            history.clear();
        }
        
    }
    
    /**
     * Displays a window with information about the program.
     * consulted:
     * https://stackoverflow.com/questions/28937392/javafx-alerts-and-their-size
     * @param event 
     */
    @FXML
    protected void handleAboutAction(ActionEvent event) {
        menuBar.notifyWindowOpened();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About Dialog");
        alert.setHeaderText(null);
        alert.setContentText("This program was created by Ian, Taka, Ben and Spencer. "
                + "Its purpose is to be able to compose music. \n"
                + "We tried to make it as intuitive as possible, so click around, drag notes"
                + ", try the menu items, and above all have fun! \n"
                + "See you later Beethoven! ");
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                .forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
        alert.showAndWait();
        menuBar.notifyWindowClosed();
    }
    
    
    /**
     * Deletes selected notes and adds them to the clipboard.
     * @param event 
     */
    @FXML
    protected void handleCutAction(ActionEvent event) {
        handleCopyAction(event);
        handleDeleteAction(event);
    }
    
    /** 
     * Adds selected notes to the clipboard.
     * consulted: 
     * https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
     * https://stackoverflow.com/questions/46818958/invalid-stream-header-efbfbdef-when-converting-object-from-byte-string/46819395
     * @param event 
     */
    @FXML
    protected void handleCopyAction(ActionEvent event) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(bos);
            output.writeObject(composition.getSelectedRoots());
            final byte[] byteArray = bos.toByteArray();
            String toPut = Base64.getEncoder().encodeToString(byteArray);
            ClipboardContent content = new ClipboardContent();
            content.putString(toPut);
            clipboard.setContent(content);
            output.close();
            
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * Puts notes from clipboard onto the selected pane, leaves them the only selected notes.
     * consulted: 
     * https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
     * https://stackoverflow.com/questions/46818958/invalid-stream-header-efbfbdef-when-converting-object-from-byte-string/46819395
     * @param event 
     */
    @FXML
    protected void handlePasteAction(ActionEvent event) {
        try {
            final byte[] bytes = Base64.getDecoder().decode(clipboard.getString().getBytes());
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes); 
            ObjectInput in = new ObjectInputStream(bis);
            Set loadedRects = (HashSet<TuneRectangle>) in.readObject();
            composition.clearSelection();
            composition.loadRoots(loadedRects);
            history.addNewCommand(new PasteCommand(composition, loadedRects));
        }
        catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(IllegalArgumentException e) {
        //this is in case the user tries to copy a non-'set<TuneRectangle>', nothing should happen
        }
    }
    
    
    /**
     * Selects all top-level items drawn in the composition pane.
     * @param event the click on the menu item
     */
    @FXML
    protected void handleSelectAllAction(ActionEvent event) {
        composition.selectAll();
        history.addNewCommand(new SelectionCommand(composition));
        menuBar.update();
    }

    /**
     * Deselects all currently selected items.
     * @param event the click on the menu item
     */
    @FXML
    protected void handleSelectNoneAction(ActionEvent event) {
        composition.clearSelection();
        history.addNewCommand(new SelectionCommand(composition));
        menuBar.update();
    }

    /**
     * Deletes all currently selected items.
     * @param event the click on the menu item
     */
    @FXML
    protected void handleDeleteAction(ActionEvent event) {
        composition.deleteSelection();
        menuBar.update();
    }

    /**
     * Groups selected items into a gesture.
     * The gesture behaves like a complex note and things inside of it cannot
     * be modified individually.
     * @param event
     */
    @FXML
    protected void handleGroup(ActionEvent event) {
        composition.groupSelected();
        menuBar.update();
    }

    /**
     * Removes selected top-level gestures.
     * The gesture is removed and its children are freed and selected.
     * @param event menu button event
     */
    @FXML
    protected void handleUngroup(ActionEvent event) {
        composition.ungroupSelected();
        menuBar.update();
    }
    
    /**
     * Re-executes the latest unexecuted action.
     * @param event menu button event
     */
    @FXML
    protected void handleRedo(ActionEvent event) {
        history.redo();
        menuBar.update();
    }

    /**
     * undoes the latest action
     * @param event menu button event
     */
    @FXML
    protected void handleUndo(ActionEvent event) {
        history.undo();
        menuBar.update();
    }

    /**
     * When the user selects the "Play" menu item, play the composition.
     * @param event menu button event
     */
    @FXML
    protected void handlePlayAction(ActionEvent event) {
        play();
        stopButton.setDisable(false);
    }

    /**
     * When the user selects the "Stop" menu item, stop playing.
     * @param event menu button event
     */
    @FXML
    protected void handleStopAction(ActionEvent event) {
        stopPlaying();
        stopButton.setDisable(true);
    }

    /**
     * When the user clicks the "Exit" menu item, exit the program.
     * @param event menu button event
     */
    @FXML
    protected void handleExitAction(ActionEvent event) {
        if (!history.isSaved()) {
            menuBar.notifyWindowOpened();
            int choice = ConfirmationAlert();
            if (choice == CONFIRMATIONYES) {
                handleSaveAction(event);
                System.exit(0);
            } else if (choice == CONFIRMATIONNO) {
                System.exit(0);
            }
            menuBar.notifyWindowClosed();
        }
        else {
            System.exit(0);
        }
        
    }
    
    /**
     * Tests whether there is a current file, and saves or saves as appropriately
     * @param event 
     */
    @FXML
    protected void handleSaveAction(ActionEvent event) {
        if(currentFile == null){
            handleSaveAsAction(event);
        }
        else{
            save(currentFile);
        }
    }
    
    /**
     * Prompts the user for a file and saves to it.
     * @param event 
     */
    @FXML
    protected void handleSaveAsAction(ActionEvent event) {
        menuBar.notifyWindowOpened();
        currentFile = fileChooser.showSaveDialog(primaryStage);
        if(currentFile == null) {
            menuBar.notifyWindowClosed();
            return;
        }
        menuBar.notifyWindowClosed();
        save(currentFile);
    }

    /**
     * Serializes all notes and writes them to a file
     * @param file the file to write to
     */
    private void save(File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(composition.getRoots());
            out.close();
            fileOut.close();
            history.recordSave();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Prompts the user for a file and Deserializes the notes from it, clearing the pane except for the new notes
     * @param event
     * @throws ClassNotFoundException 
     */
    @FXML
    protected void handleOpenAction(ActionEvent event) throws ClassNotFoundException {
        menuBar.notifyWindowOpened();
        if(!history.isSaved()) {
            int choice = ConfirmationAlert();
            if (choice == CONFIRMATIONYES) {
                handleSaveAction(event);
            } else if (choice == CONFIRMATIONNO) {
                
            } else {
                return;
            }
        }
        currentFile = fileChooser.showOpenDialog(primaryStage);
        if(currentFile == null) {
            menuBar.notifyWindowClosed();
            return;
        }
        load(currentFile);
        menuBar.notifyWindowClosed();
    }
    
    @FXML
    protected void handleImportAction(ActionEvent event) throws ClassNotFoundException {
        menuBar.notifyWindowOpened();
        if(!history.isSaved()) {
            int choice = ConfirmationAlert();
            if (choice == CONFIRMATIONYES) {
                handleSaveAction(event);
            } else if (choice == CONFIRMATIONNO) {
                
            } else {
                return;
            }
        }
        importChooser.showOpenDialog(primaryStage);
        currentFile = null;
        if(currentFile == null) {
            menuBar.notifyWindowClosed();
            return;
        }
        loadMidi(currentFile);
        menuBar.notifyWindowClosed();
    }
    
    
    /**
     * Deserializes notes from a given file.
     * Also clears history.
     * @param file the file to read from
     * @throws ClassNotFoundException 
     */
    private void load(File file) throws ClassNotFoundException {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Set<TuneRectangle> loadSet = (HashSet<TuneRectangle>) in.readObject();
            in.close();
            fileIn.close();
            composition.clearAll();
            composition.loadRoots(loadSet);
            for(TuneRectangle rect : loadSet){
                rect.removeSelectStyle();
            }
            composition.clearSelection();
            history.clear();
            
        }catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        menuBar.update();
    }
    
    private void loadMidi(File file) throws ClassNotFoundException {
        composition.clearAll();
        composition.clearSelection();
        history.clear();
        menuBar.update();
    }

    /**
     * When he user presses the mouse button in the composition pane, stop
     * playing the notes. This will perform another action (as usual) in
     * addition to stopping the tune.
     * @param event the mouse click event
     */
    @FXML
    protected void handleCompositionPaneMousePressed(MouseEvent event) {
        stopPlaying();
        if (!event.isControlDown()) {
            composition.clearSelection();
        }
        selectionRect.setX(event.getX());
        selectionRect.setY(event.getY());
        selectionRect.setWidth(0);
        selectionRect.setHeight(0);
        selectionRect.setVisible(true);
        
    }

    /**
     * Changes the selection rectangle as the mouse is dragged across the
     * composition pane.
     * @param event the mouse drag
     */
    @FXML
    protected void handleCompositionPaneMouseDragged(MouseEvent event) {
        selectionRect.setWidth(event.getX() - selectionRect.getX());
        selectionRect.setHeight(event.getY() - selectionRect.getY());
    }

    /**
     * Selects notes inside of the selection rectangle and hides the rectangle.
     * @param event, the mouse released
     */
    @FXML
    protected void handleCompositionPaneMouseReleased(MouseEvent event) {
        selectArea(selectionRect);
        selectionRect.setVisible(false);
        if (!event.isStillSincePress()) {
            history.addNewCommand(new SelectionCommand(composition));
        }
        menuBar.update();
    }

    /**
     * Selects everything in the given area.
     * @param selectionArea the area in which TuneRectangles should be selected
     */
    public void selectArea(Node selectionArea) {
        Bounds selectionBounds = selectionArea.getBoundsInParent();
        for (TuneRectangle rect : composition.getRoots()) {
            for (NoteBar bar : rect.getChildLeaves()){
                Bounds barBounds = bar.getBoundsInParent();
                if (selectionBounds.contains(barBounds)) {
                    bar.getHighestParent().addToSelection();
                }
            }
        }
    }
    
    /**
     * When the user clicks in the composition pane, add a note.
     * @param event the mouse click event
     */
    @FXML
    protected void handleCompositionPaneMouseClick(MouseEvent event) {
        if (event.isStillSincePress()) {
            // Ignore click at the conclusion of a drag
            RadioButton instButton = (RadioButton)instrumentgroup.getSelectedToggle();
            Instrument instrument = (Instrument)instButton.getUserData();
            Note note = new Note(Constants.coordToPitch(event.getY()),
                                 (int)event.getX(),
                                 instrument);
            if (!event.isControlDown()) {
                composition.clearSelection();
            }
            NoteBar forCommand = new NoteBar(note, composition);
            history.addNewCommand(new CreationCommand(composition, forCommand));
        }
        menuBar.update();
        event.consume();
    }

    /**
     * Construct the scene and start the application.
     * @param primaryStage the stage for the main window
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TuneComposer.fxml"));
        Scene scene = new Scene(root);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Tune Composer");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });
        primaryStage.show();
    }

    /**
     * Initialize the composition pane.
     */
    @FXML
    protected void initialize() {
        compositionpane.setPrefHeight(Constants.HEIGHT);
        compositionpane.setPrefWidth(Constants.WIDTH);
        drawLines();
        setupAnimation();
        setupSelectionRect();
        setupInstruments();
        setupComposition();
        setupCommandHistory();
        setupMenuBar();
    }

    /**
     * Launch the application.
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }
}
