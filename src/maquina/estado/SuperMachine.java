package maquina.estado;

import javax.sound.midi.MidiChannel;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import static pacman.game.Constants.*;

public abstract class SuperMachine 
{
	
	private static final int MINDISTANCE = 20;
	State_Defensive deffensive;
	State_Aggresive aggresive;
	State_Passive passive;
	 
	
	/**
	 *  This method will change the actual state depending on different values
	 */
	public void next()
	{

	}
	/**
	 * This method will return the next move of Pacman
	 * @return next MOVE of Pacman
	 */
	public abstract MOVE action();
	public void Final(){
	
	}
}
