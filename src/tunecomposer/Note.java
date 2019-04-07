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
    
    /**
     * changes the pitch of the current note
     * @param pitch, the pitch to change to 
     */
    public void setPitch(int pitch) {
        this.pitch = pitch;
    }
    
    /**
     * returns the pitch of the current note
     */
    public int getPitch() {
        return pitch;
    }
    
    /**
     * changes the start tick of the current note
     * @param startTick, the starTick to change to 
     */
    public void setStartTick(int startTick) {
        this.startTick = startTick;
    }
    
    /**
     * returns the startTick of the current note
     */
    public int getStartTick() {
        return startTick;
    }    
    
    /**
     * changes the duration of the current note
     * @param duration, the duration to change to 
     */
    public void setDuration(int duration) {
        this.duration = duration;
    } 
    
    /**
     * returns the Duration of the current note
     */
    public int getDuration() {
        return duration;
    }
    
    /**
     * returns the endTick of the current note
     */
    public int getEndTick() {
        return startTick + duration;
    }
    
    /**
     * returns the instrument of the current note
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * checks if any notes exist
     */
    public static boolean isEmpty() {
        return ALL_NOTES.isEmpty();
    }

    /**
     * returns the lastTick of the current note
     */
    public static int getLastTick() {
        Note last = null;
        for (Note n : ALL_NOTES) {
            if ((last == null) || (n.getEndTick() > last.getEndTick())) {
                last = n;
            }
        }
        return last.getEndTick();
    }

    /**
     * adds note to the midiplayer so that it may be played
     */
    public void addToPlayer(MidiPlayer player) {
        player.addNote(pitch, VOLUME, startTick, duration, 
                       instrument.getChannel(), TRACK);
    }
    
    /**
     * removes current note from allnotes
     */
    public void delete() {
        ALL_NOTES.remove(this);
    }
    
    //TODO undelete for undoing deletes
    
    /**
     * adds all note to the midiplayer
     */
    public static void playAllNotes(MidiPlayer player) {
        for (Note n : ALL_NOTES) {
            n.addToPlayer(player);
        }
    }  
}
    
