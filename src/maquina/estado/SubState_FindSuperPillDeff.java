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
		GHOST targetGhost = null;
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(controller.game.getGhostLairTime(ghost) == 0)
			{
				if(controller.game.getGhostEdibleTime(ghost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)))
				{
					Final();
					return;
				}
				if(targetGhost == null)
				{
					targetGhost = ghost;
				}
				else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)))
				{
					targetGhost = ghost;
				}
			}
		}
		if(targetGhost == null || controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)) > controller.MINDISTANCE)
		{
			Final();
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
		
		if(targets.size() == 0)
		{
			if(controller.SuperMachine.currentState instanceof State_Defensive)
			{
				State_Defensive defState = (State_Defensive) controller.SuperMachine.currentState;
				
				// set current submachine State to FindSuperPillDeff.
				defState.SubMachine.currentState = defState.SubMachine.states.get(1);
			}
		}
	}
	public MOVE action() 
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;

		if(controller.debug)
		{
			System.out.println("FindSuperPillDef");
		}
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		int[] powerPills = controller.game.getPowerPillIndices();	
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
			
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
		{	
			if(controller.game.isPowerPillStillAvailable(i))
			{
				targets.add(powerPills[i]);	
			}
		}
		
		int[] targetsArray =new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
		{
			targetsArray[i]=targets.get(i);
		}
		int nearestPowerPill = controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH);
		return controller.game.getNextMoveTowardsTarget(current, nearestPowerPill , DM.PATH);
		
	}
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		controller.SuperMachine.currentState.Final();
	}
}
