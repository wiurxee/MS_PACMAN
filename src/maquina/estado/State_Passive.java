package maquina.estado;

import pacman.game.Constants.MOVE;

public class State_Passive extends SuperMachine 
{
	SubMachine_Passive substate = new SubState_RecollectPass();
	
	public void next() 
	{
	}
	
	public MOVE action() 
	{
		return substate.action();
	}


}
