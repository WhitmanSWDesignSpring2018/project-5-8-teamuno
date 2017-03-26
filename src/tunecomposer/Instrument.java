/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import javafx.scene.paint.Color;
import javax.sound.midi.ShortMessage;

/**
 * Enumerated type for instruments.
 * @author janet
 */
public enum Instrument {
    
    PIANO         (1,  0, "Piano",        Color.SLATEGRAY),
    HARPSICHORD   (7,  1, "Harpsichord",  Color.CRIMSON),
    MARIMBA       (13, 2, "Marimba",      Color.DARKORANGE),
    CHURCH_ORGAN  (20, 3, "Church organ", Color.GOLD),
    ACCORDION     (22, 4, "Accordion",    Color.GREEN),
    GUITAR        (25, 5, "Guitar",       Color.BLUE),
    VIOLIN        (41, 6, "Violin",       Color.DARKVIOLET), 
    FRENCH_HORN   (61, 7, "French horn",  Color.PURPLE);

    private final int midiProgram;
    private final int channel;    
    private final String displayName;
    private final Color displayColor;

    Instrument(int midiProgram, int channel, 
               String displayName, Color displayColor) {
        this.midiProgram = midiProgram;
        this.channel = channel;
        this.displayName = displayName;
        this.displayColor = displayColor;
    }
    
    public int getMidiProgram() {
        return midiProgram;
    }
    
    public int getChannel() {
        return channel;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Color getDisplayColor() {
        return displayColor;
    }
    
    public static void addAll(MidiPlayer player) {
        for (Instrument inst : Instrument.values()) {
            player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + inst.getChannel(), 
                                inst.getMidiProgram()-1, 
                                0, 0, 0);
        }
    }
}
