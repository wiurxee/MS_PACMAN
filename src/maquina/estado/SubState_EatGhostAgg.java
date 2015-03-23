package maquina.estado;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.SortOrder;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Ghost;

public class SubState_EatGhostAgg extends State{

	
	public void next() 
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		// whether pacman should change current state or not.
		boolean bShouldExitState =true;
		//Check if there is a edible ghost he cant reach, if there is he stays on aggressive state, else he change the current state
		for(GHOST ghost : GHOST.values())
		{
			if(controller.game.getGhostLairTime(ghost) == 0 && controller.game.getGhostEdibleTime(ghost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)))
			{
				bShouldExitState = false;
			}
		}
		if(bShouldExitState)
		{
			Final();
		}
	}
	public MOVE action() 
	{
		

		AJICONTROLLER controller = AJICONTROLLER.singleton;
		

		if(controller.debug)
		{
			System.out.println("eatGhostAgg");
		}
		//Calculate what SuperState must run.
		int current = controller.game.getPacmanCurrentNodeIndex();
		// target ghost pacman should go after
		GHOST targetGhost = null;
		// ghost to be care about
		ArrayList<GHOST> ghostToBeCare = new ArrayList<GHOST>();
		// sorted list of ghosts due to their distances with pacman
		ArrayList<GHOST> sortedGhosts = new ArrayList<GHOST>();
		// First i sort the Ghost by the distances to PacMan
		for(GHOST ghost : GHOST.values())
		{
			if(controller.game.getGhostLairTime(ghost) == 0)
			{	
				// If there is no previus ghost on the list, this will be the first one
				if(sortedGhosts.size() == 0)
				{
					sortedGhosts.add(ghost);
				}
				else
				{
					// Whether the ghost should be added at the end of the list or not
					boolean bShouldAddLast = true;
					// for every ghost in the sorted list
					for(int i = 0 ; i < sortedGhosts.size() ; i++)
					{
						// first i check if the ghost is not on the lair
						// if the distance to the current ghost is less than the one to the ghost in the least, it means it should go before in the list
						if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(sortedGhosts.get(i))))
						{
							// add the ghost before the other in the list 
							sortedGhosts.add(i,ghost);
							// and we not need longer to add it at the end of the list
							bShouldAddLast = false;
							break;
						}
					}
					// check if the pacman is not on the lair and it should be added at the end
					if(bShouldAddLast && controller.game.getGhostLairTime(ghost) == 0)
					{
						sortedGhosts.add(ghost);
					}
				}
			}
		}
		
		// for every ghost on the sorted list
		for(int i = 0 ; i < sortedGhosts.size() ; i++)
		{
			// if the ghost is edible and is not on the lair
			if(controller.game.getGhostLairTime(sortedGhosts.get(i)) == 0 && controller.game.getGhostEdibleTime(sortedGhosts.get(i)) > 0)
			{
				// the ghost will be our target
				targetGhost = sortedGhosts.get(i);
				break;
			}
			// if not we should care about this ghost
			else
			{
				ghostToBeCare.add(sortedGhosts.get(i));
			}
		}
		// If there are no ghost to be care about
		if(ghostToBeCare.size() == 0)
		{
			// We can go after the target ghost
			return controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH);
		}
		// else
		
		// We get all the possible moves that pacman has at the current position
		MOVE[] possibleMoves = controller.game.getPossibleMoves(current);
		
		// we create a list of the possibleMoves, which is more flexible
		ArrayList<MOVE> remainingPosibleMoves = new ArrayList<MOVE>();
		
		// we add all the possible moves to the list
		for(int i = 0; i < possibleMoves.length ; i++)
		{
			// except the neutral, we will not use neutral moves on this behaviour
			if(possibleMoves[i] != MOVE.NEUTRAL)
			{
				remainingPosibleMoves.add(possibleMoves[i]);
			}
		}
		
		// for every ghost we need to care about 
		for(GHOST ghost : ghostToBeCare)
		{
			// get the move we need to reach that ghost, because we wont to delete it from our list of moves ( just to go in other direction)
			MOVE moveTowardGhost = controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(ghost), DM.PATH);
			if(controller.game.getGhostLairTime(ghost) == 0)
			{
//				int[] pathPacman = controller.game.getShortestPath(controller.game.getGhostCurrentNodeIndex(targetGhost),current);
//				int[] pathGhost = controller.game.getShortestPath(controller.game.getGhostCurrentNodeIndex(ghost), current);
//				boolean bShouldGoAfterTarget = false;
//				boolean bFirstCoincidence = false;
//				for(int i = 0; i < pathGhost.length ; i++)
//				{
//					for(int j = 0 ; j < pathPacman.length ; j++)
//					{
//						if(pathGhost[i] == pathPacman[j] && !bFirstCoincidence)
//						{
//							if( controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(ghost) , pathGhost[i]) < controller.game.getShortestPathDistance(current, pathGhost[i]))
//							{
//								bShouldGoAfterTarget = true;
//							}
//							bFirstCoincidence = true;
//						}
//					}
//				}
				
				
				// if that move is on our list, we remove it
				for(int i = 0; i < remainingPosibleMoves.size() ; i++)
				{
					if(remainingPosibleMoves.get(i)  == moveTowardGhost && remainingPosibleMoves.size() > 1 /*&& !bShouldGoAfterTarget*/)
					{
						remainingPosibleMoves.remove(i);
					}
				}
			}
		}
		// if we still have more than one possible move, we will want to do the one that bring us closest to the target
		if(remainingPosibleMoves.size() > 1 && targetGhost != null)
		{
			// for all possible moves
			for(int i = 0 ; i < remainingPosibleMoves.size() ; i++)
			{
				// if there is a possibility of fleeing ghosts going forward to the target, we will take it
				if(remainingPosibleMoves.get(i) == controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH))
				{
					return remainingPosibleMoves.get(i);
				}
				// else if there is one possibility that distance us from the target, we will remove it, because it is counterproductive
				else if(remainingPosibleMoves.get(i) == controller.game.getNextMoveAwayFromTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH))
				{
					remainingPosibleMoves.remove(i);
				}
			}
		}
		
		//If this code is reached means that we have more than 1 possibility to flee, but we dont know which one is going to bring us closer to the target
		// we take one of this possibilities randomly
		
		Random rand = new Random();
		
		return remainingPosibleMoves.get(rand.nextInt(remainingPosibleMoves.size()));
	}
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		controller.SuperMachine.currentState.Final();
	}
}
