/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

import tunecomposer.command.SelectionCommand;
import java.util.HashSet;
import tunecomposer.Composition;
import tunecomposer.Gesture;
import tunecomposer.TuneRectangle;

/**
 * Command for grouping some TuneRectangles.
 * @author Spencer
 */
public class GroupCommand implements Command {
    
    private final Composition composition;
    private HashSet<Gesture> toUngroup;
    private HashSet<HashSet<TuneRectangle>> toGroup;

    // TODO Split into two constructors or two classes
    public GroupCommand(Composition composition, HashSet stuff, boolean wasGrouped) {
        this.composition = composition;
        if (wasGrouped) {
            groupedConstructor(stuff);
        } else {
            ungroupedConstructor(stuff);
        }
        new SelectionCommand(composition);
    }
    /**
     * Sets up the command for grouping.
     * @param grouped the new gestures
     */
    private void groupedConstructor(HashSet<Gesture> grouped) {
        toUngroup = new HashSet(grouped);
        toGroup = new HashSet<HashSet<TuneRectangle>>();
    }
    
    /**
     * Sets up the command for ungrouping.
     * @param ungrouped the freed children
     */
    private void ungroupedConstructor(HashSet<HashSet<TuneRectangle>> ungrouped) {
        toGroup = new HashSet(ungrouped);
        toUngroup = new HashSet<Gesture>();
    }
    
    /**
     * Groups the TuneRectangles into a gesture.
     */
    @Override
    public void execute() {
        toggle();
    }
    
    /**
     * Removes a gesture, leaving its components.
     */
    @Override
    public void unexecute() {
        toggle();
    }
    
    /**
     * Toggles grouping on the given gestures/notes.
     */
    private void toggle(){
        if(toUngroup.isEmpty()){
            for(HashSet<TuneRectangle> rectGroup : toGroup){
                Gesture toAdd = composition.groupTuneRectangles(rectGroup);
                if(!composition.isSelectedTop(toAdd)){
                    composition.addToSelection(toAdd);
                }
                toUngroup.add(toAdd);
            }
            toGroup.clear();
        }
        else{
            for(Gesture gest : toUngroup){
                toGroup.add(new HashSet<TuneRectangle>(composition.ungroupGesture(gest)));
            }
            toUngroup.clear();
        }
    }
}
