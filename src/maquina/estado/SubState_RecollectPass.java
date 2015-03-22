package maquina.estado;

import java.util.ArrayList;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class SubState_RecollectPass extends State {

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
			else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)))
			{
				minGhost = ghost;
			}
		}
		
		if(minGhost != null)
		{
			if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)) != -1 && (controller.game.getGhostEdibleTime(minGhost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)) || controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)) < controller.MINDISTANCE))
			{
				Final();
			}
		}	
	}
	
	public MOVE action() 
	{	
		AJICONTROLLER controller = AJICONTROLLER.singleton;
			
		int[] pills = controller.game.getPillIndices();
		
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		for(int i = 0; i < pills.length ; i++)
		{
			if(controller.game.isPillStillAvailable(i))
			{
				targets.add(pills[i]);
			}
		}
		
		int[] targetsArray = new int[targets.size()];
		
		for(int i = 0 ; i < targetsArray.length; i++)
		{
			targetsArray[i] = targets.get(i);
		}
		
		if(controller.debug)
		{
			System.out.println("RecollectPass");
		}
		
		return controller.game.getNextMoveTowardsTarget(controller.game.getPacmanCurrentNodeIndex(), controller.game.getClosestNodeIndexFromNodeIndex(controller.game.getPacmanCurrentNodeIndex(),targetsArray,DM.PATH), DM.PATH);
	}
	
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		controller.SuperMachine.currentState.Final();
	}
}
