/*
 * Provided for use only in CS 370 at Whitman College.
 * DO NOT DISTRIBUTE.
 */
package tunecomposer;

import java.util.HashSet;

/**
 * Represents a note.
 * @author janet
 */
public class Note {

    private static final int VOLUME = 127;
    private static final int TRACK = 0;
    
    private static final HashSet<Note> ALL_NOTES = new HashSet<>();
   
    private int pitch;
    private int startTick;    
    private int duration;
    private final Instrument instrument;
    
    /**
     * Creates a new note.
     *
     * @param pitch      an integer from 0 to 127 giving the pitch
     * @param startTick  tells when the note is to start playing (in ticks)
     * @param instrument the instrument to play the note with
     */
    public Note(int pitch, int startTick, Instrument instrument) {
        ALL_NOTES.add(this);
        this.pitch = pitch;
        this.startTick = startTick;
        this.duration = Constants.DURATION;
        this.instrument = instrument;
    }
    
    public void setPitch(int pitch) {
        this.pitch = pitch;
    }
    
    public int getPitch() {
        return pitch;
    }
    
    public void setStartTick(int startTick) {
        this.startTick = startTick;
    }
    
    public int getStartTick() {
        return startTick;
    }    

    public void setDuration(int duration) {
        this.duration = duration;
    } 
    
    public int getDuration() {
        return duration;
    }
    
    public int getEndTick() {
        return startTick + duration;
    }
    
    public Instrument getInstrument() {
        return instrument;
    }

    public static boolean isEmpty() {
        return ALL_NOTES.isEmpty();
    }

    public static int getLastTick() {
        Note last = null;
        for (Note n : ALL_NOTES) {
            if ((last == null) || (n.getEndTick() > last.getEndTick())) {
                last = n;
            }
        }
        return last.getEndTick();
    }

    public void addToPlayer(MidiPlayer player) {
        player.addNote(pitch, VOLUME, startTick, duration, 
                       instrument.getChannel(), TRACK);
    }
    
    public void delete() {
        ALL_NOTES.remove(this);
    }
    
    public static void playAllNotes(MidiPlayer player) {
        for (Note n : ALL_NOTES) {
            n.addToPlayer(player);
        }
    }  
}
    
