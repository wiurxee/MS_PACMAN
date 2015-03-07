package maquina.estado;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.MOVE;

public abstract class AjiMaquinaEstado {
	/**
	 *  This method will change the actual state depending on different values
	 */
	public abstract AjiMaquinaEstado next();
	/**
	 * This method will return the next move of Pacman
	 * @return next MOVE of Pacman
	 */
	public abstract MOVE action();
}
