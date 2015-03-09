package maquina.estado;

import pacman.game.Constants.MOVE;

public class State_Defensive extends SuperMachine
{
	public SubMachine_Deffensive substate;
	
	
	public void next() 
	{
	}

	public MOVE action() 
	{
		return substate.action();
	}

}
