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
	
	
	
	public AJICONTROLLER()
	{
		ArrayList<StateMachine> subMachines = new ArrayList<StateMachine>();
		
		ArrayList<State> estadosAgg = new ArrayList<State>();
		estadosAgg.add(new SubState_EatGhostAgg());
		
		ArrayList<State> estadosDef = new ArrayList<State>();
		estadosDef.add(new SubState_FindSuperPillDeff());
		estadosDef.add(new SubState_FleeDeff());
		
		ArrayList<State> estadosPas = new ArrayList<State>();
		estadosPas.add(new SubState_RecollectPass());
		
		ArrayList<State> estadosSup= new ArrayList<State>();
		estadosSup.add(new State_Aggresive());
		estadosSup.add(new State_Defensive());
		estadosSup.add(new State_Passive());
		
		subMachines.add(new StateMachine(estadosAgg));
		subMachines.add(new StateMachine(estadosDef));
		subMachines.add(new StateMachine(estadosPas));
		SuperMachine = new StateMachine(estadosSup,subMachines);		
		
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
