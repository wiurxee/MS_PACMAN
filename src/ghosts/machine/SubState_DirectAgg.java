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
		
		float weight = 0;
		float Wmax; 
		int currDistance = controller.game.getShortestPathDistance(controller.game.getPacmanCurrentNodeIndex(), controller.game.getGhostCurrentNodeIndex(myGhost));
		
		/*Calculate distance percentage of weight*/
		float min, max;
		Wmax = 0.3f;
		
		min = controller.veryCloseDistance - 5;
		max = controller.veryCloseDistance + 5;
				
		weight += Wmax - (Math.max(0, Math.min(max-min, currDistance - min))/(max - min) ) * Wmax;
		
		Wmax = 0.9f;
		
		min = controller.farDistance - 5;
		max = controller.farDistance + 5;
		
		weight += (Math.max(0, Math.min(max-min, currDistance - min))/(max - min) ) * Wmax;
		
		/*End Distance*/
		
		/*Distance Order Between The Ghosts*/
		
		Wmax = 0.7f;
		
		boolean bIAmClosest = true;
		
		int distanceMyGhostToPacman = controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getPacmanCurrentNodeIndex());
		
		for(GHOST g: GHOST.values())
		{
			if(g != myGhost && controller.game.getGhostLairTime(g) == 0)
			{
				if(controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(g), controller.game.getPacmanCurrentNodeIndex()) < distanceMyGhostToPacman)
				{
					bIAmClosest = false;
				}
			}
		}	
		
		if (bIAmClosest)
			weight += Wmax;
		
		/*END DISTANCE ORDER*/
		
		if (myGhost == GHOST.BLINKY)
			System.out.println(weight);
		
		return weight;
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
