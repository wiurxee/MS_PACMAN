package pacman.controllers.examples;

import java.util.ArrayList;
import maquina.estado.*;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public final class AJICONTROLLER extends Controller<MOVE>
{
	public static AJICONTROLLER singleton;
	private MOVE[] allMoves = MOVE.values();
	public static Game game;	
	public int MINDISTANCE = 20;
	public StateMachine SuperMachine;
	
	public AJICONTROLLER()
	{
//		ArrayList<StateMachine> subMachines = new ArrayList<StateMachine>();
		
		ArrayList<State> estadosAgg = new ArrayList<State>();
		SubState_EatGhostAgg eatGhostAggState = new SubState_EatGhostAgg();
		estadosAgg.add(eatGhostAggState);
		
		ArrayList<State> estadosDef = new ArrayList<State>();
		SubState_FindSuperPillDeff findSuperPillDeffState = new SubState_FindSuperPillDeff();
		SubState_FleeDeff fleeDeffState = new SubState_FleeDeff();
		estadosDef.add(findSuperPillDeffState);
		estadosDef.add(fleeDeffState);
		
		ArrayList<State> estadosPas = new ArrayList<State>();
		SubState_RecollectPass recollectPassState = new SubState_RecollectPass();
		estadosPas.add(recollectPassState);
		
		
		State_Aggressive AggState = new State_Aggressive();
		State_Defensive DefState = new State_Defensive();
		State_Passive PasState = new State_Passive();
		
		AggState.setSubMachine(new StateMachine(estadosAgg,eatGhostAggState));
		DefState.setSubMachine(new StateMachine(estadosDef,fleeDeffState));
		PasState.setSubMachine(new StateMachine(estadosPas, recollectPassState));
		
		ArrayList<State> estadosSup= new ArrayList<State>();
		estadosSup.add(AggState);
		estadosSup.add(DefState);
		estadosSup.add(PasState);
		
//		subMachines.add(new StateMachine(estadosAgg));
//		subMachines.add(new StateMachine(estadosDef));
//		subMachines.add(new StateMachine(estadosPas));
		
		SuperMachine = new StateMachine(estadosSup,PasState);
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game,long timeDue)
	{
		AJICONTROLLER.game = game;
		SuperMachine.next();
		return SuperMachine.action();
	}	
}
