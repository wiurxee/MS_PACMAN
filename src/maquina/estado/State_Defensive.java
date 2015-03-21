package maquina.estado;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class State_Defensive extends State
{
	private StateMachine SubMachine;
	
	public void next() 
	{
		SubMachine.next();
	}

	@SuppressWarnings("static-access")
	public MOVE action()
	{
		//Declare controller.
//		pacman.controllers.examples.AJICONTROLLER controller = pacman.controllers.examples.AJICONTROLLER.singleton;
//		
//		return controller.game.getNextMoveAwayFromTarget(controller.game.getPacmanCurrentNodeIndex(),controller.game.getGhostCurrentNodeIndex(ghost),DM.PATH);
		return SubMachine.action();
	}
	
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		GHOST minGhost = null;
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(minGhost == null)
			{
				minGhost = ghost;
			}
			else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)))
			{
				minGhost = ghost;
			}
		}
		
		if(controller.game.getGhostEdibleTime(minGhost) >= controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)))
		{
			// set State to Aggressive
			controller.SuperMachine.currentState = controller.SuperMachine.states.get(0);
		}
		else
		{
			// set State to Passive
			controller.SuperMachine.currentState = controller.SuperMachine.states.get(2);
		}
		
	}

	public void setSubMachine(StateMachine sub)
	{
		this.SubMachine = sub;
	}
}
