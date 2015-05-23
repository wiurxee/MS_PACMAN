package ghosts.machine;

import java.util.Random;

import pacman.controllers.examples.AJIGHOSTS;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public  class State_Aggressive extends State
{
	public State_Aggressive(GHOST g) {
		super(g);
		// TODO Auto-generated constructor stub
	}

	public GhostMachine SubMachine;
	
	
	public float next() 
	{
		AJIGHOSTS controller = AJIGHOSTS.singleton;
		
						
		if(controller.game.getGhostEdibleTime(myGhost) > 0)
		{
			GhostMachine myMachine = null;
			
			for(GhostMachine gMachine:controller.Ghosts)
			{
				if(gMachine.myGhost == myGhost)
				{
					myMachine = gMachine;
					break;
				}
			}
			
			myMachine.currentState = myMachine.states.get(1);
			return 0;
		}		
		SubMachine.next();
		return 0;
	}
	
	public MOVE action() 
	{
		return SubMachine.action();
	}
	//cambio entre estados superiores
	public void Final()
	{
		AJIGHOSTS controller = AJIGHOSTS.singleton;
		
		controller.debug = true;
	}

	public void setSubMachine(GhostMachine sub)
	{
		this.SubMachine = sub;
	}
}
