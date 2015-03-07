package maquina.estado;

import pacman.game.Constants.MOVE;

public class State_Defensive extends SuperMachine
{
	public SubMachine_Deffensive substate;
	
	public State_Defensive ()
	{
		
	}
	
	public SuperMachine next() 
	{
		return super.next();
	}

	public MOVE action() 
	{
		substate = substate.next(game);
		return substate.action();
	}

}
