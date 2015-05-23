package ghosts.machine;

import java.util.ArrayList;

import pacman.controllers.examples.AJIGHOSTS;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Ghost;

public class SubState_FleeDeff extends State{

	public SubState_FleeDeff(GHOST g) {
		super(g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public float next() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MOVE action() {
		AJIGHOSTS controller = AJIGHOSTS.singleton;
		if(controller.debug)
		{
			System.out.println("huir");
		}
		
		// We get all the possible moves that pacman has at the current position
		MOVE[] possibleMoves = controller.game.getPossibleMoves(controller.game.getGhostCurrentNodeIndex(myGhost));
		
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
		MOVE lastMove = controller.game.getGhostLastMoveMade(myGhost);
		
		switch (lastMove) {
		case DOWN:	
			remainingPosibleMoves.remove(MOVE.UP);
			break;
		case UP:
			remainingPosibleMoves.remove(MOVE.DOWN);
			break;
		case LEFT:
			remainingPosibleMoves.remove(MOVE.RIGHT);
			break;
		case RIGHT:
			remainingPosibleMoves.remove(MOVE.LEFT);
			break;
		default:
			break;
		}
		if(remainingPosibleMoves.size() > 1)
		{
			MOVE moveTowardsPacman = controller.game.getApproximateNextMoveTowardsTarget(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getPacmanCurrentNodeIndex(),controller.game.getGhostLastMoveMade(myGhost), DM.PATH);
			
			for(int i = 0; i < remainingPosibleMoves.size() ; i++)
			{
				if(moveTowardsPacman == remainingPosibleMoves.get(i))
				{
					remainingPosibleMoves.remove(moveTowardsPacman);
				}
			}			
		}
		
		if(remainingPosibleMoves.size() > 1)
		{			
			// sorted list of ghosts due to their distances with pacman
			ArrayList<GHOST> sortedGhosts = new ArrayList<GHOST>();
			
			for(GHOST g: GHOST.values())
			{
				if(g != myGhost && controller.game.getGhostLairTime(g) == 0)
				{
					if(sortedGhosts.size() == 0)
					{
						sortedGhosts.add(g);
					}
					else
					{
						// Whether the ghost should be added at the end of the list or not
						boolean bShouldAddLast = true;
						// for every ghost in the sorted list
						for(int i = 0 ; i < sortedGhosts.size() ; i++)
						{
							if(controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getGhostCurrentNodeIndex(g)) < controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getGhostCurrentNodeIndex(sortedGhosts.get(i))))
							{
								sortedGhosts.add(i,g);
								bShouldAddLast = false;
								break;
							}
						}
						if(bShouldAddLast)
						{
							sortedGhosts.add(g);
						}
					}
					
				}
			}
			while(sortedGhosts.size() > 0)
			{
				if(remainingPosibleMoves.size() > 1)
				{
					MOVE moveToRemove = controller.game.getNextMoveTowardsTarget(controller.game.getGhostCurrentNodeIndex(myGhost),controller.game.getGhostCurrentNodeIndex(sortedGhosts.get(0)),DM.PATH);
					
					remainingPosibleMoves.remove(moveToRemove);
				}		
				sortedGhosts.remove(0);
			}		
			
		}
				
		
		return remainingPosibleMoves.get(0);
	}

	@Override
	public void Final() {
		// TODO Auto-generated method stub
		
	}

}
