package pacman.controllers.examples;

import ghosts.machine.GhostMachine;
import ghosts.machine.State;
import ghosts.machine.State_Aggressive;
import ghosts.machine.State_Defensive;
import ghosts.machine.SubState_DirectAgg;
import ghosts.machine.SubState_FleeDeff;
import ghosts.machine.SubState_KeepOff;
import ghosts.machine.SubState_WrapAgg;

import java.util.ArrayList;
import java.util.EnumMap;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.controllers.Controller;

public class AJIGHOSTS  extends Controller<EnumMap<GHOST,MOVE>>{

	public static AJIGHOSTS singleton;
	public Game game;	
	public GhostMachine[] Ghosts = new GhostMachine[4];	
	public boolean debug = false;
	
	public AJIGHOSTS(boolean debug)
	{
				
//		ArrayList<StateMachine> subMachines = new ArrayList<StateMachine>();
		singleton = this;
		this.debug = debug;
		
		for(int i = 0; i< 4; i++)
		{
			GHOST myGhost = null;			
			switch(i)
			{
			case 0:
				myGhost = GHOST.BLINKY;
				break;
			case 1:
				myGhost = GHOST.INKY;
				break;
			case 2:
				myGhost = GHOST.SUE;
				break;
			case 3:
				myGhost = GHOST.PINKY;
				break;
			}	
			
			ArrayList<State> estadosAgg = new ArrayList<State>();
			SubState_DirectAgg directAggState = new SubState_DirectAgg(myGhost);
			SubState_WrapAgg wrapAggState = new SubState_WrapAgg(myGhost);
			SubState_KeepOff keepOffState = new SubState_KeepOff(myGhost);
			estadosAgg.add(directAggState);
			estadosAgg.add(wrapAggState);
			estadosAgg.add(keepOffState);
			
			ArrayList<State> estadosDef = new ArrayList<State>();
			SubState_FleeDeff fleeDeffState = new SubState_FleeDeff(myGhost);
			estadosDef.add(fleeDeffState);
			
			
			State_Aggressive AggState = new State_Aggressive(myGhost);
			State_Defensive DefState = new State_Defensive(myGhost);
			
			
			AggState.setSubMachine(new GhostMachine(estadosAgg,directAggState,myGhost));
			DefState.setSubMachine(new GhostMachine(estadosDef,fleeDeffState,myGhost));
			
			
			ArrayList<State> estadosSup= new ArrayList<State>();
			estadosSup.add(AggState);
			estadosSup.add(DefState);		
			
			Ghosts[i] = new GhostMachine(estadosSup,AggState,myGhost);
		}
//BLYNKY

	}
	public EnumMap<GHOST,MOVE> getMove(Game game,long timeDue)
	{
		this.game = game;
		EnumMap<GHOST,MOVE> myMoves=new EnumMap<GHOST,MOVE>(GHOST.class);
		for(GhostMachine ghost : Ghosts)	//for each ghost
		{
			if(game.doesGhostRequireAction(ghost.myGhost))
			{
				ghost.next();
				myMoves.put(ghost.myGhost, ghost.action());
			}
		}
		return myMoves;		
	}
}
