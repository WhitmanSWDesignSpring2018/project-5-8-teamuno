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
     * Changes the pitch of this note.
     * @param pitch the pitch to change to
     */
    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    /**
     * Gets the pitch of this note.
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * Changes the start tick of this note.
     * @param startTick the startTick to change to
     */
    public void setStartTick(int startTick) {
        this.startTick = startTick;
    }

    /**
     * Get the start tick of this note.
     */
    public int getStartTick() {
        return startTick;
    }

    /**
     * Changes the duration of this note.
     * @param duration the duration to change to
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the duration of this note.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Gets the endTick of this note.
     */
    public int getEndTick() {
        return startTick + duration;
    }

    /**
     * Returns the instrument of this note
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * Checks if any notes exist
     */
    public static boolean isEmpty() {
        return ALL_NOTES.isEmpty();
    }

    /**
     * Returns the last tick of the last note.
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
     * Adds note to the midiplayer so that it may be played.
     */
    public void addToPlayer(MidiPlayer player) {
        player.addNote(pitch, VOLUME, startTick, duration,
                       instrument.getChannel(), TRACK);
    }

    /**
     * Deletes this note.
     */
    public void delete() {
        ALL_NOTES.remove(this);
    }
    
    //TODO undelete for undoing deletes
    
    /**
     * Adds all notes to the midiplayer.
     */
    public static void playAllNotes(MidiPlayer player) {
        for (Note n : ALL_NOTES) {
            n.addToPlayer(player);
        }
    }
}

