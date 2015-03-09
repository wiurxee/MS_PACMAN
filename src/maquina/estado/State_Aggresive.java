package maquina.estado;

import pacman.game.Constants.MOVE;

public  class State_Aggresive extends SuperMachine
{
	SubMachine_Aggresive substate;
	
	public void next() 
	{
	}
	
	public MOVE action() 
	{
		substate.next();
		return substate.action();
	}

}
