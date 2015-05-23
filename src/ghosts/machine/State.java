package ghosts.machine;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public abstract class State {

	GHOST myGhost;
	
	public State(GHOST g)
	{
		this.myGhost = g;
	}
	
	/**
	 *  This method will change the actual state depending on different values
	 */
	public abstract float next();
	/**
	 * This method will return the next move of Pacman
	 * @return next MOVE of Pacman
	 */
	public abstract MOVE action();
	public abstract void Final();
}
