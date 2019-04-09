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
    
    private Gesture toUngroup;
    private HashSet<TuneRectangle> toGroup;

    public GroupCommand(Gesture grouped) {
        toUngroup = grouped;
        toGroup = null;
    }
    
    public GroupCommand(HashSet<TuneRectangle> ungrouped) {
        toGroup = ungrouped;
        toUngroup = null;
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
        if(toGroup == null){
            toGroup = toUngroup.getChildren();
            //toUngroup.ungroup();
        }
        else{
            toUngroup = new Gesture(toGroup);
        }
    }
}
