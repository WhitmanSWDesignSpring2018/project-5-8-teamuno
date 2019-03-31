/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.io.IOException;
import java.util.HashSet;
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

/**
 * This JavaFX app lets the user compose music.
 * @author Janet Davis 
 * @author SOLUTION - PROJECT 4
 * @since March 26, 2017
 */
public class TuneComposer extends Application {
    
    private final MidiPlayer player;
    
    @FXML private Pane compositionpane;
    @FXML private Pane instrumentpane;
    @FXML private ToggleGroup instrumentgroup;
    
    private Line playLine;
    private TranslateTransition playAnimation;
    private Rectangle selectionRect;
    static final HashSet<Playable> SELECTION = new HashSet<>();
    static final HashSet<Playable> ALLTOP = new HashSet<>();
    
    /**
     * Construct a new composition pane.
     */
    public TuneComposer() {
        player = new MidiPlayer(Constants.TICKS_PER_BEAT, 
                                Constants.BEATS_PER_MINUTE);
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
            System.out.println(end);
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
    
    /**
     * Reset the position of the play line to offscreen left.
     */
    private void resetPlayLine() {
         playLine.setTranslateX(-1);       
    }    
    
    
    private void setupSelectionRect() {
        selectionRect = new Rectangle();
        selectionRect.setId("selectionarea");
        selectionRect.setVisible(false);
        compositionpane.getChildren().add(selectionRect);
    }
    
    public void draw(Playable p){
        compositionpane.getChildren().add(p);
    }

    @FXML
    protected void handleSelectAllAction(ActionEvent event) {
        NoteBar.selectAll();
    }
    
    @FXML
    protected void handleSelectNoneAction(ActionEvent event) {
        TuneComposer.clearSelection();
    }
    
    @FXML
    protected void handleDeleteAction(ActionEvent event) {
        for (Playable p : SELECTION) {
            compositionpane.getChildren().remove(p);
            p.delete();
        }
        TuneComposer.clearSelection();
    }
    
    @FXML
    protected void handleGroup(ActionEvent event){
        // Pass the selection by value, not by reference
        HashSet group = new HashSet<Playable>(SELECTION);
        if(SELECTION.isEmpty()){return;}
        clearSelection();
        compositionpane.getChildren().add(new Gesture(group)); 
    }
    
    @FXML
    protected void handleUngroup(ActionEvent event){
        for(Playable p : SELECTION){
            if (p instanceof Gesture) {
                SELECTION.remove(p);
                ((Gesture) p).freeChildren();
                compositionpane.getChildren().remove(p);
            }
        }
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
     * When he user presses the mouse button in the composition pane,
     * stop playing the notes.
     * @param event the mouse click event
     */
    @FXML
    protected void handleCompositionPaneMousePressed(MouseEvent event) {
        stopPlaying();
        if (!event.isControlDown()) {
            clearSelection();
        } 
        selectionRect.setX(event.getX());
        selectionRect.setY(event.getY());
        selectionRect.setWidth(0);
        selectionRect.setHeight(0);
        selectionRect.setVisible(true); 
    }
        
    @FXML
    protected void handleCompositionPaneMouseDragged(MouseEvent event) {
        selectionRect.setWidth(event.getX() - selectionRect.getX());
        selectionRect.setHeight(event.getY() - selectionRect.getY());
    }
    
    @FXML
    protected void handleCompositionPaneMouseReleased(MouseEvent event) {
        NoteBar.selectArea(selectionRect);
        selectionRect.setVisible(false);             
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
                clearSelection();
            }
            compositionpane.getChildren().add(new NoteBar(note)); 
        }
    }
    
    /**
     * When the user clicks the "Exit" menu item, exit the program.
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitAction(ActionEvent event) {
        System.exit(0);
    }
    
    public static void addToSelection(Playable toAdd) {
        // TODO This check could be redundant
        if (toAdd.getParent() == null) {
            SELECTION.add(toAdd);
        }
        toAdd.getStyleClass().add("selected");
    }
        
    public static void removeFromSelection(Playable toRemove) {
        SELECTION.remove(toRemove);
        toRemove.getStyleClass().remove("selected");
    }

    public static void clearSelection() {
        for(Playable p : SELECTION){
            p.removeSelectStyle();
        }
        SELECTION.clear();
    }    
    
    public static HashSet<Playable> getSelection() {
        return SELECTION;
    }
    
    public static HashSet<NoteBar> getSelectedNotes() {
        HashSet notes = new HashSet<NoteBar>();
        for (Playable p : SELECTION) {
           notes.addAll(p.getChildLeaves());
        } 
        return notes;
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
    }

    /**
     * Launch the application.
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
