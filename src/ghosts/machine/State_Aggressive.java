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
		
		float directAttackWeight = Math.max(0, Math.min(1, SubMachine.states.get(0).next()));
		float wrapAttackWeight = Math.max(0, Math.min(1, SubMachine.states.get(1).next()));
		float keepOfWeight = Math.max(0, Math.min(1, SubMachine.states.get(2).next()));
		
		if (directAttackWeight >= wrapAttackWeight &&  directAttackWeight > keepOfWeight)
		{
			SubMachine.currentState = SubMachine.states.get(0);
		}
		else if (wrapAttackWeight > directAttackWeight && wrapAttackWeight >= keepOfWeight)
		{
			SubMachine.currentState = SubMachine.states.get(1);
		}
		else if ( keepOfWeight > directAttackWeight && keepOfWeight > wrapAttackWeight)
		{
			SubMachine.currentState = SubMachine.states.get(2);
		}
		else
		{
			SubMachine.currentState = SubMachine.states.get(0);
		}
		
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
