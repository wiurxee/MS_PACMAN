package maquina.estado;

import java.awt.List;
import java.util.ArrayList;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class SubState_FleeDeff  extends State
{
	public void next() 
	{
		
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
		if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)) != -1 && controller.game.getGhostEdibleTime(targetGhost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost))  || controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)) > controller.MINDISTANCE)
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
		
		GHOST targetGhost = null;
		GHOST[] ghosts = GHOST.values();
		MOVE[] ghostDirections = new MOVE[ghosts.length];

		//Check if has to transit to defensive state or aggressive state.
		for(int i = 0 ; i < ghosts.length ; i++)
		{
			targetGhost = ghosts[i];
			ghostDirections[i] = controller.game.getNextMoveAwayFromTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH);
		}
		
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
		
		if(controller.debug)
		{
			System.out.println("FleeDeff");
		}
		
		MOVE nextMove = controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(minGhost), DM.PATH);
		
		MOVE[] possibleMoves = controller.game.getPossibleMoves(current);
		
		int[] moveWeights = new int[4];
		moveWeights[0] = -100;
		moveWeights[1] = -100;
		moveWeights[2] = -100;
		moveWeights[3] = -100;
		
		for (int i = 0 ; i < possibleMoves.length ; i++)
		{
			for(int j = 0 ; j < possibleMoves.length ; j++)
			{
				if ( ghostDirections[i] == possibleMoves[j] )
				{
					switch(possibleMoves[j])
					{
					case DOWN:
						moveWeights[1] = 0;
						moveWeights[1] += 1;
						break;
					case LEFT:
						moveWeights[3] = 0;
						moveWeights[3] += 1;
						break;
					case RIGHT:
						moveWeights[2] = 0;
						moveWeights[2] += 1;
						break;
					case UP:
						moveWeights[0] = 0;
						moveWeights[0] += 1;
						break;
					}
				}
			}
		}

		int minWeight = Integer.MAX_VALUE;
		int minWeightIndex = -1;
		int sameWeightDirections = 1;
		ArrayList<MOVE> sameWeightDirectionsMove = new ArrayList<MOVE>();
		
		for (int i = 0; i < moveWeights.length ; i++)
		{
			if (moveWeights[i] < minWeight && moveWeights[i] > -1)
			{
				minWeight = moveWeights[i];
				minWeightIndex = i;
			}
			else if (moveWeights[i] == minWeight)
			{
				sameWeightDirections ++;
				
				switch(i)
				{
				case 1:
					sameWeightDirectionsMove.add(MOVE.DOWN);
					break;
				case 0:
					sameWeightDirectionsMove.add(MOVE.UP);
					break;
				case 2:
					sameWeightDirectionsMove.add(MOVE.RIGHT);
					break;
				case 3:
					sameWeightDirectionsMove.add(MOVE.LEFT);
					break;
				}
			}
		}
		
		if (sameWeightDirections > 1)
		{
			GHOST[] closestGhosts = new GHOST[GHOST.values().length];
			
			for (int i = 0 ; i < sameWeightDirectionsMove.size() ; i++)
			{
				for (int j = 0 ; j < ghosts.length ; j++)
				{
					if (controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(ghosts[j]), DM.PATH) == sameWeightDirectionsMove.get(i))
					{
						
					}
				}
			}
		}
		if(controller.debug)
		{
			System.out.println("Minweight move -> "+minWeightIndex);
		}
		
		return nextMove;
	}
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		controller.SuperMachine.currentState.Final();
	}
}
