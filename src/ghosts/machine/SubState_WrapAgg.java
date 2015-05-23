package ghosts.machine;

import java.awt.font.NumericShaper.Range;
import java.util.ArrayList;
import java.util.Random;

import pacman.controllers.examples.AJIGHOSTS;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class SubState_WrapAgg extends State {

	public SubState_WrapAgg(GHOST g) {
		super(g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public float next() {
		// TODO Auto-generated method stub
		AJIGHOSTS controller = AJIGHOSTS.singleton;
		GhostMachine myMachine = null;
		
		for(GhostMachine gMachine:controller.Ghosts)
		{
			if(gMachine.myGhost == myGhost)
			{
				myMachine = gMachine;
				break;
			}
		}
		
		// TODO Auto-generated method stub
		if(myMachine.currentState instanceof State_Aggressive)
		{
			State_Aggressive aggState = (State_Aggressive) myMachine.currentState;
			boolean bShouldChangeDir = true;
			int distanceMyGhostToPacman = controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getPacmanCurrentNodeIndex());
			for(GHOST g: GHOST.values())
			{
				if(g != myGhost && controller.game.getGhostLairTime(g) == 0)
				{
					if(controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(g), controller.game.getPacmanCurrentNodeIndex()) < distanceMyGhostToPacman)
					{
						bShouldChangeDir = false;
					}
				}
			}	
			
			Random rand = new Random();
			
			float random = rand.nextFloat() * 0.4f + 0.4f;
			
			if(bShouldChangeDir && distanceMyGhostToPacman/50f > random)
			{
				aggState.SubMachine.currentState = aggState.SubMachine.states.get(0);
			}
		}
		return 0;
	}

	@Override
	public MOVE action() {
		AJIGHOSTS controller = AJIGHOSTS.singleton;
		if(controller.debug)
		{
			System.out.println("ataque rodeando");
		}	
		
		MOVE moveTowardPacman = controller.game.getApproximateNextMoveTowardsTarget(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getPacmanCurrentNodeIndex(),controller.game.getGhostLastMoveMade(myGhost), DM.PATH);
		
		
		int[] path = controller.game.getShortestPath(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getPacmanCurrentNodeIndex(), controller.game.getGhostLastMoveMade(myGhost));
		
		int crossWithLotOfPossibleMoves = 0;
		
		for(int i = 0; i < path.length; i++)
		{
			if(controller.game.getPossibleMoves(path[i]).length > 2)
			{
				crossWithLotOfPossibleMoves++;
			}
		}
		if(crossWithLotOfPossibleMoves > 1)
		{
			return moveTowardPacman;
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
		for(GHOST g:GHOST.values())
		{
			if(g != myGhost)
			{							
				for(int i = 0; i < path.length; i++)
				{
					if(path[i] == controller.game.getGhostCurrentNodeIndex(g) && remainingPosibleMoves.size() > 1)
					{
						remainingPosibleMoves.remove(moveTowardPacman);
					}
				}
				
			}			
		}
		
		for(int i = 0; i < remainingPosibleMoves.size();i++)
		{
			if(remainingPosibleMoves.get(i) == moveTowardPacman)
			{
				return moveTowardPacman;
			}
		}
		
		
		if(remainingPosibleMoves.size() > 1)
		{
			int maxDistance = 0;
			MOVE moveToRemove = null;
			for(int j = 0; j < remainingPosibleMoves.size() ; j++)
			{
				if(controller.game.getShortestPathDistance(controller.game.getNeighbour(controller.game.getGhostCurrentNodeIndex(myGhost), remainingPosibleMoves.get(j)),controller.game.getPacmanCurrentNodeIndex()) > maxDistance)
				{
					maxDistance = controller.game.getShortestPathDistance(controller.game.getNeighbour(controller.game.getGhostCurrentNodeIndex(myGhost), remainingPosibleMoves.get(j)),controller.game.getPacmanCurrentNodeIndex());
					moveToRemove = remainingPosibleMoves.get(j);
				}
			}
			
			remainingPosibleMoves.remove(moveToRemove);			
		}
				
		
		return remainingPosibleMoves.get(0);
	}

	@Override
	public void Final() {
		// TODO Auto-generated method stub
		
	}

}
