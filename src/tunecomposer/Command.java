/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

/**
 *
 * @author vankoesd
 * TODO this
 */
public interface Command {
    
    /**
     * Does the command
     */
    public void execute();
    /**
     * undoes the command
     */
    public void unexecute();
}
