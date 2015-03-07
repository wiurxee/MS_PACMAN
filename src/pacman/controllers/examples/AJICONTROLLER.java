package pacman.controllers.examples;

import java.util.Random;

import maquina.estado.State_Defensive;
import maquina.estado.State_Passive;
import maquina.estado.SuperMachine;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public final class AJICONTROLLER extends Controller<MOVE>{
	
	private MOVE[] allMoves=MOVE.values();
	
	public static Game game;
	
	private SuperMachine currentState = new State_Passive();
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game,long timeDue)
	{
		this.game = game;
		//currentState = currentState.next();
		return currentState.action();
	}
	
}
