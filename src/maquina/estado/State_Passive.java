package maquina.estado;

import pacman.game.Constants.MOVE;

public class State_Passive extends SuperMachine 
{
	SubMachine_Passive substate = new SubState_RecollectPass();
	
	public SuperMachine next() 
	{
		return super.next();
	}
	
	public MOVE action() 
	{
		substate = substate.next();
		return substate.action();
	}


}
