
package tunecomposer;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;

public class MidiAdapter {

    private static final int NOTE_ON = ShortMessage.NOTE_ON;
    private static final int NOTE_OFF = ShortMessage.NOTE_OFF;
    private static int TEMPO_FACTOR = 5;

    public static void importMidi(File midiFile, Composition composition, Instruments instruments) throws NoteEndNotFoundException {
        Sequence sequence = makeSequence(midiFile);
        Track[] tracks = sequence.getTracks();
        for (Track t : tracks) {
            trackToNotes(t, composition, instruments, sequence);
        }
    }

    private static void trackToNotes(Track track, Composition composition, Instruments instruments, Sequence sequence) throws NoteEndNotFoundException {
        List<MidiEvent> events = eventsFromTrack(track);
        for (int i=0; i<events.size(); i++) {
            MidiEvent startEvent = events.get(i);

            if (isNoteOn(startEvent)) {
                MidiEvent endEvent = findEndEvent(startEvent, events, i);
                ShortMessage startMessage = (ShortMessage) startEvent.getMessage();

                int key = startMessage.getData1();
                int startTick = (int) startEvent.getTick()/TEMPO_FACTOR;
                long duration = endEvent.getTick() - startEvent.getTick()/TEMPO_FACTOR;
                int channel = startMessage.getChannel();
                int velocity = startMessage.getData2();
                
//                System.out.println("key: " + key);
//                System.out.println("start tick: " + startTick);
//                System.out.println("duration: " + duration);
//                System.out.println("channel: " + channel);
//                System.out.println("velocity: " + velocity);
//                System.out.println("-----------------------");
                makeNoteBar(key, startTick, duration, velocity, channel, composition, instruments);
            }
        }
    }

    private static void makeNoteBar(int pitch, int startTick, long duration, int velocity, int channel, Composition composition, Instruments instruments) {
        // Get 'piano' instrument
        Instrument instrument = instruments.getInstruments().get(0);

        Note note = new Note(pitch, instrument, startTick, duration, velocity);
        NoteBar noteBar = new NoteBar(note, composition);
    }

    private static MidiEvent findEndEvent(MidiEvent startEvent, List<MidiEvent> events, int start) throws NoteEndNotFoundException {
        for (int i=start; i<events.size(); i++) {
            if (isMatch(startEvent, events.get(i))) {
                return events.get(i);
            }
        }

        throw new NoteEndNotFoundException("Note end not found.");
    }

    private static boolean isMatch(MidiEvent startEvent, MidiEvent endEvent) {
        if (!isNoteOff(endEvent)) {
            return false;
        }

        ShortMessage startMessage = (ShortMessage) startEvent.getMessage();
        ShortMessage endMessage = (ShortMessage) endEvent.getMessage();
        return startMessage.getData1() == endMessage.getData1();
    }

    private static boolean isNoteOn(MidiEvent event) {
        if (event.getMessage() instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) event.getMessage();
            int command = sm.getCommand();
            int velocity = sm.getData2();
            return command == NOTE_ON && velocity != 0;
        } else {
            return false;
        }
    }

    private static boolean isNoteOff(MidiEvent event) {
        if (event.getMessage() instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) event.getMessage();
            int command = sm.getCommand();
            int velocity = sm.getData2();
            return command == NOTE_OFF || (command == NOTE_ON && velocity == 0);
        } else {
            return false;
        }
    }

    private static Sequence makeSequence(String filename) {
        File file = new File(filename);
        return makeSequence(file);
    }

    private static Sequence makeSequence(File file) {
        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(file);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return sequence;
    }

    private static List<MidiEvent> eventsFromTrack(Track track) {
        List<MidiEvent> events = new ArrayList<>();
        for (int i=0; i < track.size(); i++) {
            events.add(track.get(i));
        }
        return events;
    }
    
    public static void changeTempo(int newTempo){
        TEMPO_FACTOR = newTempo;
    }
}
