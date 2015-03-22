package maquina.estado;

import java.util.ArrayList;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class State_Passive extends State 
{
	public StateMachine SubMachine;
	
	public void next() 
	{
		SubMachine.next();
	}
	
	@SuppressWarnings("static-access")
	public MOVE action() 
	{	
		return SubMachine.action();
	}
	
	public void Final()
	{
		// game controller
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		// current Pacman index node position
		int current = controller.game.getPacmanCurrentNodeIndex();
				
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			// if there are not in the center box
			if(controller.game.getGhostLairTime(ghost) == 0)
			{
				// if the ghost is edible
				if(controller.game.getGhostEdibleTime(ghost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)))
				{
					// set the current state to Aggresive
					controller.SuperMachine.currentState = controller.SuperMachine.states.get(0);
					return;
				}
			}
		}
		// set the current state to Deffensive
		controller.SuperMachine.currentState = controller.SuperMachine.states.get(1);
		
		if(controller.SuperMachine.currentState instanceof State_Defensive)
		{
			State_Defensive defState = (State_Defensive) controller.SuperMachine.currentState;
			// i set the submachine current state to his auto state (FleeDefensive)
			defState.SubMachine.currentState = defState.SubMachine.states.get(1);
		}
		
				
	}
	
	public void setSubMachine(StateMachine sub)
	{
		this.SubMachine = sub;
	}
}