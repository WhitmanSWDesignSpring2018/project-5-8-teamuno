/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.command;

/**
 * Interface for command pattern.
 * @author Spencer
 */
public interface Command {
    /**
     * Does the command.
     */
    public void execute();

    /**
     * Undoes the command.
     */
    public void unexecute();
}
