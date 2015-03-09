package pacman.controllers.examples;

import java.util.ArrayList;
import java.util.Random;

import maquina.estado.*;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public final class AJICONTROLLER extends Controller<MOVE>{
	
	private MOVE[] allMoves=MOVE.values();
	
	public static Game game;
	private static final int MINDISTANCE = 20;
	
	
	private StateMachine SuperMachine;
	private StateMachine AggresiveMachine;
	private StateMachine DeffensiveMachine;
	private StateMachine PassiveMachine;
	
	
	
	public AJICONTROLLER()
	{
		ArrayList<State> estados = new ArrayList<State>();
		
		ArrayList<StateMachine> subMachines = new ArrayList<StateMachine>();
		subMachines.add(new StateMachine(estados));
		subMachines.add(new StateMachine(estados));
		subMachines.add(new StateMachine(estados));
		SuperMachine = new StateMachine(estados,subMachines);
		
		
	}	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game,long timeDue)
	{
		this.game = game;
		return MOVE.DOWN;
	}
	
}
