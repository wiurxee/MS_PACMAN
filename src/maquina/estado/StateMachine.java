package maquina.estado;

import java.util.ArrayList;

import javax.sound.midi.MidiChannel;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import static pacman.game.Constants.*;

public class StateMachine 
{
	
	private boolean bSuperMachine;
	
	private ArrayList<StateMachine> subMachines;
	
	private ArrayList<State> states;
	
	private StateMachine superMachine;
	
	/**
	 * Constructor sobrecargado para SuperMaquina
	 * @param estados
	 * @param subMaq
	 */
	public StateMachine(ArrayList<State> estados,ArrayList<StateMachine> subMaq)
	{
		this.bSuperMachine = true;
		this.states = estados;
		this.subMachines = subMaq;
		
		for(StateMachine subMachine : this.subMachines)
		{
			subMachine.setSuper(this);
		}
	}
	/**
	 * Constructor sobrecargado para Submaquinas
	 * @param estados
	 */
	public StateMachine(ArrayList<State> estados)
	{
		this.bSuperMachine = false;
		this.states = estados;
	}
	public void setSuper(StateMachine sup)
	{
		this.superMachine = sup;
	}
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
	public  MOVE action()
	{ 
		return MOVE.LEFT;
	}
	public  void Final()
	{
		
	}
	
	public boolean isSuperMachine()
	{
		return this.bSuperMachine;
	}

}
