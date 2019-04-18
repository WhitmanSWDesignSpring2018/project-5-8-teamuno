/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */ 
package tunecomposer;

import java.io.IOException;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * This JavaFX app lets the user compose music.
 * @author Ian, Spencer, Ben, Taka, Janet
 */
public class TuneComposer extends Application {

    private final MidiPlayer player;

    public static Composition composition;

    @FXML private Pane compositionpane;
    @FXML private Pane instrumentpane;
    @FXML private ToggleGroup instrumentgroup;
 
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
        playAnimation.setOnFinished((ActionEvent e) -> { resetPlayLine(); });
    }

    private void setupComposition() {
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
     * Selects all top-level items drawn in the composition pane.
     * @param event the click on the menu item
     */
    @FXML
    protected void handleSelectAllAction(ActionEvent event) {
        composition.selectAll();
        history.addNewCommand(new SelectionCommand());
    }

    /**
     * Deselects all currently selected items.
     * @param event the click on the menu item
     */
    @FXML
    protected void handleSelectNoneAction(ActionEvent event) {
        composition.clearSelection();
        history.addNewCommand(new SelectionCommand());
    }

    /**
     * Deletes all currently selected items.
     * @param event the click on the menu item
     */
    @FXML
    protected void handleDeleteAction(ActionEvent event) {
        composition.deleteSelection();
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
    }

    /**
     * Removes selected top-level gestures.
     * The gesture is removed and its children are freed and selected.
     * @param event
     */
    @FXML
    protected void handleUngroup(ActionEvent event) {
        composition.ungroupSelected();
    }
    
    @FXML
    protected void handleRedo(ActionEvent event) {
        history.redo();
    }

    /**
     * Removes selected top-level gestures.
     * The gesture is removed and its children are freed and selected.
     * @param event
     */
    @FXML
    protected void handleUndo(ActionEvent event) {
        history.undo();
    }

    /**
     * When the user selects the "Play" menu item, play the composition.
     * @param event the menu selection event
     */
    @FXML
    protected void handlePlayAction(ActionEvent event) {
        play();
    }

    /**
     * When the user selects the "Stop" menu item, stop playing.
     * @param event the menu selection event
     */
    @FXML
    protected void handleStopAction(ActionEvent event) {
        stopPlaying();
    }

    /**
     * When the user clicks the "Exit" menu item, exit the program.
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * When he user presses the mouse button in the composition pane, stop
     * playing the notes. This will perform another action (as usual) in
     * addition to stopping the tune.
     * @param event the mouse click event
     */
    @FXML
    protected void handleCompositionPaneMousePressed(MouseEvent event) {
        //TODO start selection
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
