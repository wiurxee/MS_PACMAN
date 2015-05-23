package ghosts.machine;

import java.util.ArrayList;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;

public class GhostMachine 
{
	public ArrayList<State> states;
	public State currentState; 	
// 	ghost this Machine belongs to
	public GHOST myGhost;
	/**
	 * Overloaded SubMachine initializer.
	 * @param states
	 */
	public GhostMachine(ArrayList<State> states, State auto,GHOST g)
	{
//		this.bSuperMachine = false;
		this.states = states;
		this.currentState = auto;
		this.myGhost = g;
	}

	
	/**
	 *  This method will change the actual state depending on different values
	 */
	public void next()
	{
		currentState.next();
	}
	
	/**
	 * This method will return the next move of Pacman
	 * @return next MOVE of Pacman
	 */
	public  MOVE action()
	{
		return currentState.action();
	}
}