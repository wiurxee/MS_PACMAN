package maquina.estado;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class State_Defensive extends State
{
	private StateMachine SubMachine;
	
	public void next() 
	{
		
	}

	public MOVE action(GHOST ghost)
	{
		//Declare controller.
		pacman.controllers.examples.AJICONTROLLER controller = pacman.controllers.examples.AJICONTROLLER.singleton;
		
		return controller.game.getNextMoveAwayFromTarget(controller.game.getPacmanCurrentNodeIndex(),controller.game.getGhostCurrentNodeIndex(ghost),DM.PATH);
	}
	
	public void Final()
	{
		
	}

	public void setSubMachine(StateMachine sub)
	{
		this.SubMachine = sub;
	}

	@Override
	public MOVE action() {
		// TODO Auto-generated method stub
		return null;
	}
}
