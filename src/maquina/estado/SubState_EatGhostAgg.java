package maquina.estado;

import java.util.ArrayList;
import java.util.Random;

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
		boolean bShouldExitState =true;
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(controller.game.getGhostEdibleTime(ghost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)))
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
		
		//Calculate what SuperState must run.
		int current = controller.game.getPacmanCurrentNodeIndex();
		GHOST targetGhost = null;
		ArrayList<GHOST> ghostToBeCare = new ArrayList<GHOST>();
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
		for(int i = 0 ; i < sortedGhosts.size() ; i++)
		{
			if(controller.game.getGhostEdibleTime(sortedGhosts.get(i)) > 0)
			{
				targetGhost = sortedGhosts.get(i);
				break;
			}
			else
			{
				ghostToBeCare.add(sortedGhosts.get(i));
			}
		}
		if(ghostToBeCare.size() == 0)
		{
			return controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH);
		}
		
		
		MOVE[] possibleMoves = controller.game.getPossibleMoves(current);
		
		ArrayList<MOVE> remainingPosibleMoves = new ArrayList<MOVE>();
		
		for(int i = 0; i < possibleMoves.length ; i++)
		{
			if(possibleMoves[i] != MOVE.NEUTRAL)
			{
				remainingPosibleMoves.add(possibleMoves[i]);
			}
		}
		
		for(GHOST ghost : ghostToBeCare)
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
		
		if(remainingPosibleMoves.size() > 1 && targetGhost != null)
		{
			for(int i = 0 ; i < remainingPosibleMoves.size() ; i++)
			{
				if(remainingPosibleMoves.get(i) == controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH))
				{
					return remainingPosibleMoves.get(i);
				}
				else if(remainingPosibleMoves.get(i) == controller.game.getNextMoveAwayFromTarget(current, controller.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH))
				{
					remainingPosibleMoves.remove(i);
				}
			}
		}
		if(controller.debug)
		{
			System.out.println("eatGhostAgg");
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
