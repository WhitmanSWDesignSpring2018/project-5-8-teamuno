/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.scene.paint.Color;
import javax.sound.midi.ShortMessage;
/**
 *
 * @author vankoesd
 */
public class Instruments {
    
    private final ArrayList<Instrument> instruments = new ArrayList<>();
    private HashSet<Color> usedColors = new HashSet<>();
    private final static Random r = new Random();
    
    
    
    public Instruments(){
        this.addInstrument(1,  0, "Piano",        Color.SLATEGRAY);
        this.addInstrument(7,  1, "Harpsichord",  Color.CRIMSON);
        this.addInstrument(13, 2, "Marimba",      Color.DARKORANGE);
        this.addInstrument(20, 3, "Church organ", Color.GOLD);
        this.addInstrument(22, 4, "Accordion",    Color.GREEN);
        this.addInstrument(25, 5, "Guitar",       Color.BLUE);
        this.addInstrument(41, 6, "Violin",       Color.DARKVIOLET);
        this.addInstrument(61, 7, "French horn",  Color.PURPLE);
        
    }
    
    public List<Instrument> getInstruments(){
        return new ArrayList<Instrument>(instruments);
    }
            
    private void addInstrument(int midiProgram, int channel, 
        String displayName, Color displayColor){
        instruments.add(new Instrument(midiProgram, channel, displayName, displayColor));
        usedColors.add(displayColor);
    }
    
    public void addInstrument(int midiProgram, int channel, 
        String displayName){
        for(Instrument inst : instruments){
            if(inst.getMidiProgram() == midiProgram){
                return;
            } //whatever the objective instrument number is, might not be MidiProgram TODO check this
        }
        Color displayColor = null;
        while(!usedColors.contains(displayColor)){
            displayColor = Color.valueOf(generateColor());
        }
        instruments.add(new Instrument(midiProgram, channel, displayName, displayColor));
    }
    
    private static String generateColor() {
        final char [] hex = { '0', '1', '2', '3', '4', '5', '6', '7',
                              '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char [] s = new char[7];
        int     n = r.nextInt(0x1000000);

        s[0] = '#';
        for (int i=1;i<7;i++) {
            s[i] = hex[n & 0xf];
            n >>= 4;
        }
        return new String(s);
    }
    
    public void addAll(MidiPlayer player) {
        for (Instrument inst : instruments) {
            player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + inst.getChannel(),
                    inst.getMidiProgram()-1,
                    0, 0, 0);
        }
    }
    
}
