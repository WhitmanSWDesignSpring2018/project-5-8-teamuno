
package tunecomposer.command;

import tunecomposer.command.SelectionCommand;
import java.util.HashSet;
import tunecomposer.Composition;
import tunecomposer.Gesture;
import tunecomposer.TuneRectangle;

/**
 * Command for grouping some TuneRectangles.
 * @author Spencer, Ian
 */
public class GroupCommand implements Command {

    private final Composition composition;
    private final Gesture gesture;

    public GroupCommand(Composition composition, Gesture newGesture) {
        this.composition = composition;
        this.gesture = newGesture;

        // Reset selection tracker
        new SelectionCommand(composition);
    }

    /**
     * Groups the TuneRectangles into a gesture.
     */
    @Override
    public void execute() {
        // `init` might not be right, we may need to make something new.
        gesture.init(composition);
        gesture.updateChildrenParent();
    }
    
    /**
     * Removes a gesture, leaving its components.
     */
    @Override
    public void unexecute() {
        composition.ungroupGesture(gesture);
    }
}
