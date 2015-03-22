package maquina.estado;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public  class State_Aggressive extends State
{
	public StateMachine SubMachine;
	
	public void next() 
	{
		SubMachine.next();
	}
	
	@SuppressWarnings("static-access")
	public MOVE action() 
	{
//		//Declare controller.
//		pacman.controllers.examples.AJICONTROLLER controller = pacman.controllers.examples.AJICONTROLLER.singleton;
//		
//		return controller.game.getNextMoveTowardsTarget(controller.game.getPacmanCurrentNodeIndex(),controller.game.getGhostCurrentNodeIndex(ghost),DM.PATH);
		return SubMachine.action();
	}
	//cambio entre estados superiores
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current= controller.game.getPacmanCurrentNodeIndex();
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(controller.game.getGhostLairTime(ghost) == 0 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.MINDISTANCE)
			{		
				// set current State to deffensive
				controller.SuperMachine.currentState = controller.SuperMachine.states.get(1);
				return;
			}					
		}
		// set current State to Passive
		controller.SuperMachine.currentState = controller.SuperMachine.states.get(2);
	}

	public void setSubMachine(StateMachine sub)
	{
		this.SubMachine = sub;
	}
}
