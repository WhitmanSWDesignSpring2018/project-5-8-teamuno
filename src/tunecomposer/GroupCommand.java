/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author vankoesd
 */
public class GroupCommand implements Command{
    
    private HashSet<Gesture> toUngroup;
    private HashSet<HashSet<TuneRectangle>> toGroup;

    
    
    
    
    
    public GroupCommand(HashSet stuff, boolean wasGrouped){
        if(wasGrouped){groupedConstructor(stuff);}
        else{ungroupedConstructor(stuff);}
        new SelectionCommand();
    }
    /**
     *
     * @param grouped
     */
    private void groupedConstructor(HashSet<Gesture> grouped) {
        toUngroup = new HashSet(grouped);
        toGroup = new HashSet<HashSet<TuneRectangle>>();
    }
    
    private void ungroupedConstructor(HashSet<HashSet<TuneRectangle>> ungrouped) {
        toGroup = new HashSet(ungrouped);
        toUngroup = new HashSet<Gesture>();
    }
    
    @Override
    public void execute() {
        toggle();
    }

    @Override
    public void unexecute() {
        toggle();
    }
    
    private void toggle(){
        if(toUngroup.isEmpty()){
            for(HashSet<TuneRectangle> rectGroup : toGroup){
                toUngroup.add(TuneComposer.composition.groupTuneRectangles(rectGroup));
            }
            toGroup.clear();
        }
        else{
            for(Gesture gest : toUngroup){
                toGroup.add(new HashSet<TuneRectangle>(TuneComposer.composition.ungroupGesture(gest)));
            }
            toUngroup.clear();
        }
    }
}
