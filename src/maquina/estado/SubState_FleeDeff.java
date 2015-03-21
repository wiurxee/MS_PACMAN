package maquina.estado;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class SubState_FleeDeff  extends State{

	GHOST targetGhost = null;
	public void next() {
		
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(targetGhost == null)
			{
				targetGhost = ghost;
			}
			else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)))
			{
				targetGhost = ghost;
			}
		}
	}
	public MOVE action() {
		return MOVE.DOWN;
	}
	public void Final()
	{
		
	}
}
