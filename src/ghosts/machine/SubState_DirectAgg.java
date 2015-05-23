package ghosts.machine;

import pacman.controllers.examples.AJIGHOSTS;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class SubState_DirectAgg extends State {

	public SubState_DirectAgg(GHOST g) {
		super(g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public float next() {
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
			boolean bShouldChangeWrap = false;
			int distanceMyGhostToPacman = controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getPacmanCurrentNodeIndex());
			for(GHOST g: GHOST.values())
			{
				if(g != myGhost && controller.game.getGhostLairTime(g) == 0)
				{
					if(controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(g), controller.game.getPacmanCurrentNodeIndex()) < distanceMyGhostToPacman)
					{
						bShouldChangeWrap = true;
					}
				}
			}	
			
			if(bShouldChangeWrap)
			{
				aggState.SubMachine.currentState = aggState.SubMachine.states.get(1);
			}
		}
		return 0;
	}

	@Override
	public MOVE action() {

		AJIGHOSTS controller = AJIGHOSTS.singleton;
		
		if(controller.debug)
		{
			System.out.println("ataque directo");
		}
		
		return controller.game.getApproximateNextMoveTowardsTarget(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getPacmanCurrentNodeIndex(),controller.game.getGhostLastMoveMade(myGhost), DM.PATH);
	}

	@Override
	public void Final() {
		// TODO Auto-generated method stub
		
	}

}
