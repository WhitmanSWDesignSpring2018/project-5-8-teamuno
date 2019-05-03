/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */ 
package tunecomposer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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

    @FXML private Pane compositionpane;
    @FXML private Pane instrumentpane;
    @FXML private ToggleGroup instrumentgroup;

    @FXML private MenuItem selectAllButton;
    @FXML private MenuItem selectNoneButton;
    @FXML private MenuItem deleteButton;
    @FXML private MenuItem groupButton;
    @FXML private MenuItem ungroupButton;
    @FXML private MenuItem stopButton;
    @FXML private MenuItem playButton;
    @FXML private MenuItem undoButton;
    @FXML private MenuItem redoButton;
    // TODO Add more buttons here and in FXML
 
    private File currentFile;
    private FileChooser fileChooser;
    private Line playLine;
    private TranslateTransition playAnimation;
    private Rectangle selectionRect;
    public static CommandHistory history;

    /**
     * Construct a new composition pane.
     */
    public TuneComposer() {
        player = new MidiPlayer(Constants.TICKS_PER_BEAT,
                                Constants.BEATS_PER_MINUTE);
        history = new CommandHistory();
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Compositions", "*.ser")
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
            Instrument.addAll(player);
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
        for (Instrument inst : Instrument.values()) {
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
     * prepares the composition and the menubar
     */
    private void setupComposition() {
        menuBar = new TuneMenuBar(
                stopButton,
                playButton,
                selectNoneButton,
                selectAllButton,
                deleteButton,
                groupButton,
                ungroupButton,
                undoButton,
                redoButton);

        composition = new Composition(compositionpane);
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
     * https://code.makery.ch/blog/javafx-dialogs-official/
     * @param event 
     */
    @FXML
    protected void handleNewAction(ActionEvent event) {
        if(!history.isSaved()){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog with Custom Actions");
            alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
            alert.setContentText("Choose your option.");

            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeTwo = new ButtonType("No");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                handleSaveAction(event);
                composition.clearAll();
                history.clear();
            } else if (result.get() == buttonTypeTwo) {
                composition.clearAll();
                history.clear();
            }
        }
        else{
            composition.clearAll();
            history.clear();
        }
        
    }
    
    @FXML
    protected void handleAboutAction(ActionEvent event) {
    }
    
    
    @FXML
    protected void handleCutAction(ActionEvent event) {
    }
    
    @FXML
    protected void handleCopyAction(ActionEvent event) {
        
    }
    
    @FXML
    protected void handlePasteAction(ActionEvent event) {
    }
    /**
     * Selects all top-level items drawn in the composition pane.
     * @param event the click on the menu item
     */
    @FXML
    protected void handleSelectAllAction(ActionEvent event) {
        composition.selectAll();
        history.addNewCommand(new SelectionCommand());
        menuBar.update();
    }

    /**
     * Deselects all currently selected items.
     * @param event the click on the menu item
     */
    @FXML
    protected void handleSelectNoneAction(ActionEvent event) {
        composition.clearSelection();
        history.addNewCommand(new SelectionCommand());
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
        composition.groupSelection();
        menuBar.update();
    }

    /**
     * Removes selected top-level gestures.
     * The gesture is removed and its children are freed and selected.
     * @param event
     */
    @FXML
    protected void handleUngroup(ActionEvent event) {
        composition.ungroupSelected();
        menuBar.update();
    }
    
    /**
     * redoes the latest undone action
     * @param event , the button click
     */
    @FXML
    protected void handleRedo(ActionEvent event) {
        history.redo();
        menuBar.update();
    }

    /**
     * undoes the latest action
     * @param event , the button click
     */
    @FXML
    protected void handleUndo(ActionEvent event) {
        history.undo();
        menuBar.update();
    }

    /**
     * When the user selects the "Play" menu item, play the composition.
     * @param event the menu selection event
     */
    @FXML
    protected void handlePlayAction(ActionEvent event) {
        play();
        stopButton.setDisable(false);
    }

    /**
     * When the user selects the "Stop" menu item, stop playing.
     * @param event the menu selection event
     */
    @FXML
    protected void handleStopAction(ActionEvent event) {
        stopPlaying();
        stopButton.setDisable(true);
    }

    /**
     * When the user clicks the "Exit" menu item, exit the program.
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitAction(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML
    protected void handleSaveAction(ActionEvent event) {
        if(currentFile == null){
            handleSaveAsAction(event);
        }
        else{
            save(currentFile);
        }
    }
    
    @FXML
    protected void handleSaveAsAction(ActionEvent event) {
        currentFile = fileChooser.showSaveDialog(primaryStage);
        if(currentFile == null){
            return;
        }
        save(currentFile);
    }

    private void save(File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(composition.getRoots());
            System.out.println(composition.getRoots());
            out.close();
            fileOut.close();
            history.recordSave();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    protected void handleOpenAction(ActionEvent event) throws ClassNotFoundException {
        currentFile = fileChooser.showOpenDialog(primaryStage);
        if(currentFile == null){
            return;
        }
        load(currentFile);
    }
    
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
        NoteBar.selectArea(selectionRect);
        selectionRect.setVisible(false);
        if (!event.isStillSincePress()) {
            history.addNewCommand(new SelectionCommand());
        }
        menuBar.update();
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
            NoteBar forCommand = new NoteBar(note);
            history.addNewCommand(new CreationCommand(forCommand));
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
        
    }

    /**
     * Launch the application.
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }
}
