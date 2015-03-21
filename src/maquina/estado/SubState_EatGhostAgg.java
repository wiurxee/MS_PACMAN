package maquina.estado;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class SubState_EatGhostAgg extends State{

	GHOST targetGhost = null;
	public void next() 
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(targetGhost == null)
			{
				targetGhost = ghost;
			}
			else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)))
			{
				targetGhost = ghost;
			}
		}
		if(controller.game.getGhostEdibleTime(targetGhost) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)))
		{
			Final();
		}
	}
	public MOVE action() 
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		return controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH);
	}
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		controller.SuperMachine.currentState.Final();
	}
}
