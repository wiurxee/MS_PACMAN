package maquina.estado;

import pacman.game.Constants.MOVE;

public class State_Passive extends State 
{
	
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
}
