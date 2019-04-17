/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

/**
 *
 * @author vankoesd
 */
public class CommandImplementations { //one class each
    
    //FIRST decide if we are going to change dragSelect: NOT GOING TO
    
    //create is pretty simple, but needs selection command
    //deleting, pull references add back? talk to janet about best way, does not need selection command as whatever is selected is deleted
    //selection, going to also be used by other classes, like create, as well as its own thing, can just detect change (use exclusive union set [ask ian or spencer], check for parent == null, create initialize in handlers that signify the beginning action)
    //edit notes, record total mouse movement, add/subtract it for execute/unexecute, clear selecteion add moved stuff back
    //group ungroup, we can just call the regular methods, as selection state is remembered. Two classes
    
    //For all commands, have them include a selection command that runs last
    
}
