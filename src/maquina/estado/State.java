package maquina.estado;

import pacman.game.Constants.MOVE;

public abstract class State {

	
	/**
	 *  This method will change the actual state depending on different values
	 */
	public abstract void next();
	/**
	 * This method will return the next move of Pacman
	 * @return next MOVE of Pacman
	 */
	public abstract MOVE action();
	public abstract void Final();
}
