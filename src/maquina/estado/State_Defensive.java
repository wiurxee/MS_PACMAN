package maquina.estado;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class State_Defensive extends State
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
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
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
		
		// set State to Passive
		controller.SuperMachine.currentState = controller.SuperMachine.states.get(2);
		
		
	}

	public void setSubMachine(StateMachine sub)
	{
		this.SubMachine = sub;
	}
}
