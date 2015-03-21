package maquina.estado;

import java.util.ArrayList;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class SubState_FindSuperPillDeff extends State{

	
	public void next() 
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
		if(controller.game.getGhostEdibleTime(minGhost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)))
		{
			controller.SuperMachine.currentState.Final();
		}
		
		int[] powerPills = controller.game.getPowerPillIndices();	
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
			
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
		{	
			if(controller.game.isPowerPillStillAvailable(i))
			{
				targets.add(powerPills[i]);	
			}
		}
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
		{
			targetsArray[i]=targets.get(i);
		}
		
		
	}
	public MOVE action() 
	{
		
		
		return MOVE.DOWN;
	}
	public void Final()
	{
		
	}
}
