package pacman.controllers.examples;

import java.util.Random;

import maquina.estado.AjiMaquinaEstado;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public final class AJICONTROLLER extends Controller<MOVE>{
	
	private MOVE[] allMoves=MOVE.values();
	
	private AjiMaquinaEstado currentState;
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game,long timeDue)
	{
		currentState = currentState.next();
		return currentState.action();
	}
	
}
