package maquina.estado;

import pacman.game.Constants.MOVE;

public  class State_Aggresive extends State
{
	private StateMachine SubMachine;
	public void next() 
	{
	}
	
	public MOVE action() 
	{
		return MOVE.DOWN;
	}
	public void Final()
	{
		
	}

	public void setSubMachine(StateMachine sub)
	{
		this.SubMachine = sub;
	}
}
