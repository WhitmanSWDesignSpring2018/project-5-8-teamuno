/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.scene.paint.Color;

/**
 *
 * @author vankoesd
 */
public class Instrument {
    
    private final String displayName;
    private final int midiProgram;
    private final int channel;
    private final Color displayColor;
    
    Instrument(int midiProgram, int channel, 
        String displayName, Color displayColor) {
        this.midiProgram = midiProgram;
        this.channel = channel;
        this.displayName = displayName;
        this.displayColor = displayColor;
    }
    
    /**
     * Gets the program that is being modified.
     * @return midiprogram, the program that is modified
     */
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
}
