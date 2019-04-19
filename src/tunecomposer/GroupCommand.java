/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

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
     *Sets up the command
     * @param grouped, the new gestures
     */
    private void groupedConstructor(HashSet<Gesture> grouped) {
        toUngroup = new HashSet(grouped);
        toGroup = new HashSet<HashSet<TuneRectangle>>();
    }
    
    /**
     * sets up the command
     * @param ungrouped, the freed children
     */
    private void ungroupedConstructor(HashSet<HashSet<TuneRectangle>> ungrouped) {
        toGroup = new HashSet(ungrouped);
        toUngroup = new HashSet<Gesture>();
    }
    
    /**
     * calls toggle
     */
    @Override
    public void execute() {
        toggle();
    }
    
    /**
     * calls toggle
     */
    @Override
    public void unexecute() {
        toggle();
    }
    
    /**
     * Toggles grouping on the given gestures/notes
     */
    private void toggle(){
        if(toUngroup.isEmpty()){
            for(HashSet<TuneRectangle> rectGroup : toGroup){
                Gesture toAdd = TuneComposer.composition.groupTuneRectangles(rectGroup);
                if(!TuneComposer.composition.isSelectedTop(toAdd)){
                    TuneComposer.composition.addToSelection(toAdd);
                }
                toUngroup.add(toAdd);
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
