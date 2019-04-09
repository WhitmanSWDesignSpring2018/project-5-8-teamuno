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
public class SelectionCommand implements Command{
    
    private final HashSet<TuneRectangle> changedRects;

    public SelectionCommand(HashSet<TuneRectangle> rects) {
        changedRects = rects;
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
        for(TuneRectangle rect : changedRects){
            if(/*rect selected*/){
                TuneComposer.composition.remove(rect);
            }
            else{
                TuneComposer.composition.add(rect);
            }
        }
    }
    
}
