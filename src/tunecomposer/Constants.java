/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import javafx.util.Duration;

/**
 * Provides constants used across multiple classes.
 * @author janet
 */
class Constants {
    public static final int TICKS_PER_BEAT = 100;
    public static final int BEATS_PER_MINUTE = 60;
    
    public static final int NUM_PITCHES = 128;
    public static final int LINE_SPACING = 10;
    public static final int LINE_THICKNESS = 1;
    public static final int DURATION = 100;
    
    public static final int WIDTH = DURATION * 20;
    public static final int HEIGHT = NUM_PITCHES * LINE_SPACING;    
    
    /**
     * Convert a y coordinate to the nearest pitch.
     * @param y the coordinate in pixels
     * @return a pitch in the range 0-127
     */
    public static int coordToPitch(double y) {
        return Constants.NUM_PITCHES 
                - (int)Math.floor(y/Constants.LINE_SPACING)
                - 1;
    }           
    
    public static Duration ticksToDuration(int ticks) {
        return Duration.minutes(((float)ticks) / 
                                (TICKS_PER_BEAT * BEATS_PER_MINUTE));
    }
}
