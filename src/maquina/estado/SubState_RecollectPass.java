package maquina.estado;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.SortOrder;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class SubState_RecollectPass extends State {

	public void next() 
	{	
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		// get pacman position index node		
		int current = controller.game.getPacmanCurrentNodeIndex();
		// nearest ghost
		GHOST minGhost = null;
		
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
				if(minGhost == null)
				{
					minGhost = ghost;
				}
				else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)))
				{
					minGhost = ghost;
				}
			}
		}
		
		if(minGhost != null)
		{
			if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)) < controller.MINDISTANCE)
			{
				Final();
			}
		}	
	}
	
	public MOVE action() 
	{	
		AJICONTROLLER controller = AJICONTROLLER.singleton;

		
		if(controller.debug)
		{
			System.out.println("RecollectPass");
		}
			
		int[] pills = controller.game.getPillIndices();
		int[] powerPills = controller.game.getPowerPillIndices();
		
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		for(int i = 0; i < pills.length ; i++)
		{
			if(controller.game.isPillStillAvailable(i))
			{
				targets.add(pills[i]);
			}
		}
		for(int i = 0; i < powerPills.length ; i++)
		{
			if(controller.game.isPowerPillStillAvailable(i))
			{
				targets.add(powerPills[i]);
			}
		}
		
		int[] targetsArray = new int[targets.size()];
		
		for(int i = 0 ; i < targetsArray.length; i++)
		{
			targetsArray[i] = targets.get(i);
		}
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		MOVE[] possibleMoves = controller.game.getPossibleMoves(current);
		
		ArrayList<MOVE> remainingPosibleMoves = new ArrayList<MOVE>();
		
		for(int i = 0; i < possibleMoves.length ; i++)
		{
			if(possibleMoves[i] != MOVE.NEUTRAL)
			{
				remainingPosibleMoves.add(possibleMoves[i]);
			}
		}
		ArrayList<GHOST> sortedGhosts = new ArrayList<GHOST>();
		// First i sort the Ghost by the distances to PacMan
		for(GHOST ghost : GHOST.values())
		{
			if(sortedGhosts.size() == 0)
			{
				sortedGhosts.add(ghost);
			}
			else
			{
				boolean bShouldAddLast = true;
				for(int i = 0 ; i < sortedGhosts.size() ; i++)
				{
					if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(sortedGhosts.get(i))))
					{
						sortedGhosts.add(i,ghost);
						bShouldAddLast = false;
						break;
					}
				}
				if(bShouldAddLast && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1)
				{
					sortedGhosts.add(ghost);
				}
			}
		}
	
		for(int i = 0; i < powerPills.length ; i++)
		{
			if(controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH) == powerPills[i])
			{
				for(int j = 0; j < remainingPosibleMoves.size(); j++)
				{
					// if the closest pill is an Power pill go in other direction, we want to eat Power pills only for attacking the ghost
					if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(sortedGhosts.get(0))) > controller.MAXATTACKDISTANCE && remainingPosibleMoves.get(j) == controller.game.getNextMoveTowardsTarget(controller.game.getPacmanCurrentNodeIndex(),powerPills[i] , DM.PATH))
					{
						remainingPosibleMoves.remove(j);
					}
				}
			}
		}
		
		for(int i = 0; i < remainingPosibleMoves.size(); i++)
		{
			if(remainingPosibleMoves.get(i) == controller.game.getNextMoveTowardsTarget(controller.game.getPacmanCurrentNodeIndex(), controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH), DM.PATH))
			{
				return controller.game.getNextMoveTowardsTarget(controller.game.getPacmanCurrentNodeIndex(), controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH), DM.PATH);
			}
		}
		
		
		for(GHOST ghost : sortedGhosts)
		{
			MOVE moveTowardGhost = controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(ghost), DM.PATH);
			
			for(int i = 0; i < remainingPosibleMoves.size() ; i++)
			{
				if(remainingPosibleMoves.get(i)  == moveTowardGhost && remainingPosibleMoves.size() > 1)
				{
					remainingPosibleMoves.remove(i);
				}
			}
		}
		
		Random rand = new Random();		
		return remainingPosibleMoves.get(rand.nextInt(remainingPosibleMoves.size()));
	}
	
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		controller.SuperMachine.currentState.Final();
	}
}
