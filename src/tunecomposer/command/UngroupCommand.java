
package tunecomposer.command;

import tunecomposer.command.SelectionCommand;
import java.util.Set;
import java.util.HashSet;
import tunecomposer.Composition;
import tunecomposer.Gesture;
import tunecomposer.TuneRectangle;

/**
 * Command for grouping some TuneRectangles.
 * @author Spencer, Ian
 */
public class UngroupCommand implements Command {
    
    private final Composition composition;
    private final Set<Gesture> gestures;

    public UngroupCommand(Composition composition, Set<Gesture> ungrouped) {
        this.composition = composition;
        this.gestures = ungrouped;
    }

    /**
     * Groups the TuneRectangles into a gesture.
     */
    @Override
    public void execute() {
        for(Gesture g : gestures) {
            composition.ungroupGesture(g);
        }
    }
    
    /**
     * Removes a gesture, leaving its components.
     */
    @Override
    public void unexecute() {
        for(Gesture g : gestures) {
            g.init(composition);
        }
    }
}
