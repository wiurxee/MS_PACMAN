package maquina.estado;

import java.awt.List;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Random;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class SubState_FleeDeff  extends State{

	
	public void next() {
		
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		GHOST targetGhost = null;
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(targetGhost == null)
			{
				targetGhost = ghost;
			}
			else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)))
			{
				targetGhost = ghost;
			}
		}
		if(controller.game.getGhostLairTime(targetGhost) > 0 && controller.game.getGhostEdibleTime(targetGhost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost))  || controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)) > controller.MINDISTANCE)
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
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
		{
			targetsArray[i]=targets.get(i);
		}
		
		if(controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH) != -1 && controller.game.getNextMoveAwayFromTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH) == controller.game.getNextMoveTowardsTarget(current, controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH), DM.PATH))
		{
			if(controller.SuperMachine.currentState instanceof State_Defensive)
			{
				State_Defensive defState = (State_Defensive) controller.SuperMachine.currentState;
				
				// set current submachine State to FleeDefensive.
				defState.SubMachine.currentState = defState.SubMachine.states.get(0);
			}
		}
	}
	public MOVE action() 
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		MOVE nextMove = MOVE.NEUTRAL;
		
		//Ghost local save.
		GHOST[] ghosts = GHOST.values();
		int[] ghostPositions = new int[ghosts.length];
		
		//Ghost position save.
		for (int i = 0 ; i < ghosts.length ; i++)
		{
			ghostPositions[i] = controller.game.getGhostCurrentNodeIndex(ghosts[i]);
		}
		
		ArrayList<GHOST> ghostsForOrderCalculation = new ArrayList<GHOST>();
		for(GHOST ghost : ghosts)
		{
			ghostsForOrderCalculation.add(ghost);
		}
		
		ArrayList<GHOST> ghostsInDistanceOrder = new ArrayList<GHOST>();
		double distance = Integer.MAX_VALUE;
		for (GHOST ghost : ghostsForOrderCalculation)
		{
			double newDistance = controller.game.getDistance(current, controller.game.getGhostCurrentNodeIndex(ghost), DM.PATH);
			
			if( newDistance < distance)
			{
				ghostsInDistanceOrder.add(0, ghost);
			}
			else
			{
				boolean addAtEnd = true;
				
				for (int i = 0 ; i < ghostsInDistanceOrder.size() ; i++)
				{
					if( newDistance < controller.game.getDistance(current, controller.game.getGhostCurrentNodeIndex(ghostsInDistanceOrder.get(i)), DM.PATH))
					{
						ghostsInDistanceOrder.add(i, ghost);
						addAtEnd = false;
					}
				}
				
				if (addAtEnd)
				{
					ghostsInDistanceOrder.add(ghost);
				}
			}
		}
		
		MOVE[] possibleMoves = controller.game.getPossibleMoves(current);
		ArrayList<MOVE> possibleMovesList = new ArrayList<MOVE>();
		
		for (MOVE move : possibleMoves)
			possibleMovesList.add(move);
		
		double closestDistance = controller.game.getDistance(current, controller.game.getGhostCurrentNodeIndex(ghostsInDistanceOrder.get(0)), DM.PATH) ;
		
		if (possibleMovesList.size() >= 2 )
		{
			ArrayList<GHOST> ghostsToHaveInAccount = ghostsInDistanceOrder;
			
			for(GHOST ghost : ghostsToHaveInAccount)
			{
				MOVE move = controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(ghost), DM.PATH);
				
				for (int i = 0 ; i > possibleMovesList.size() ; i++)
				{
					if (possibleMovesList.get(i) == move || possibleMovesList.get(i) == MOVE.NEUTRAL && possibleMovesList.size() != 1)
					{
						possibleMovesList.remove(i);
					}
				}
			}
			
			if (possibleMovesList.size() == 1)
			{
				nextMove = possibleMovesList.get(0);
			}
			else
			{
				Random random = new Random();
				int moveIndex = random.nextInt(possibleMovesList.size());
				
				nextMove = possibleMovesList.get(moveIndex);
			}
		}
		else
		{
			nextMove = controller.game.getNextMoveAwayFromTarget(current, controller.game.getGhostCurrentNodeIndex(ghostsInDistanceOrder.get(0)), DM.PATH);
		}
		
		return nextMove;
	}
	
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		controller.SuperMachine.currentState.Final();
	}
}
