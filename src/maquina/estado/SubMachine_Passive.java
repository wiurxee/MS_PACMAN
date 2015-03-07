package maquina.estado;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

public abstract class SubMachine_Passive {

	protected Game game;
	/**
	 *  This method will change the actual state depending on different values
	 */
	public SubMachine_Passive next(Game game)
	{
		this.game = game;
		return new SubState_RecollectPass();
	}
	/**
	 * This method will return the next move of Pacman
	 * @return next MOVE of Pacman
	 */
	public abstract MOVE action();
}
