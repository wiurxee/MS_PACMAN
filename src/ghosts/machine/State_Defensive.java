package ghosts.machine;

import pacman.controllers.examples.AJIGHOSTS;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class State_Defensive extends State
{
	public State_Defensive(GHOST g) {
		super(g);
		// TODO Auto-generated constructor stub
	}

	public GhostMachine SubMachine;
	
	public float next() 
	{
		AJIGHOSTS controller = AJIGHOSTS.singleton;
		
		if(controller.game.getGhostEdibleTime(myGhost) == 0)
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
			
			myMachine.currentState = myMachine.states.get(0);
			return 0;
		}		
		SubMachine.next();
		return 0;
	}

	public MOVE action()
	{
		return SubMachine.action();
	}
	
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
