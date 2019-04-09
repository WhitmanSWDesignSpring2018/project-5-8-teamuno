/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.HashSet;
import javafx.scene.layout.Pane;

/**
 *
 * @author vankoesd
 */
public class DeletionCommand implements Command {
    private final HashSet<TuneRectangle> deletedRects;
    private final SelectionCommand selection;
    private final Pane compositionPane;
    
    public DeletionCommand(HashSet<TuneRectangle> rects, SelectionCommand selectionChange, Pane pane) {
        deletedRects = rects;
        selection = selectionChange;
        compositionPane = pane;
    }
    
    @Override
    public void execute() {
        for(TuneRectangle rect : deletedRects){
            TuneComposer.composition.remove(rect);
        }
        selection.execute();
    }

    @Override
    public void unexecute() {
        for (TuneRectangle rect : deletedRects){
            TuneComposer.composition.add(rect);
        }
        selection.unexecute();
    }
    
}
