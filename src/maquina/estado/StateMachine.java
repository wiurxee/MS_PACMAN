package maquina.estado;

import java.util.ArrayList;

import javax.sound.midi.MidiChannel;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
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
	 * @param subMac
	 */
	public StateMachine(ArrayList<State> estados,ArrayList<StateMachine> subMac)
	{
		this.bSuperMachine = true;
		this.states = estados;
		this.subMachines = subMac;
		
		for(State iState : this.states)
		{
			for(StateMachine iStateMachine : this.subMachines)
			{
				iStateMachine.setSuper(this);
				if(iState instanceof State_Aggressive)
				{
					State_Aggressive iStateAgg = (State_Aggressive) iState;
					iStateAgg.setSubMachine(iStateMachine);
				}
				else if(iState instanceof State_Defensive)
				{
					State_Defensive iStateDeff = (State_Defensive) iState;
					iStateDeff.setSubMachine(iStateMachine);
				}
				else if(iState instanceof State_Passive)
				{
					State_Passive iStatePas = (State_Passive) iState;
					iStatePas.setSubMachine(iStateMachine);
				}
			}
		}
	}
	
	/**
	 * Overloaded SubMachine initializer.
	 * @param states
	 */
	public StateMachine(ArrayList<State> states)
	{
		this.bSuperMachine = false;
		this.states = states;
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
		//Declare controller.
		pacman.controllers.examples.AJICONTROLLER controller = pacman.controllers.examples.AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current= controller.game.getPacmanCurrentNodeIndex();
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(controller.game.getGhostEdibleTime(ghost) == 0 && controller.game.getGhostLairTime(ghost) == 0)
			{
				if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.MINDISTANCE)
				{	
					return GetDeffState().action(ghost);		
				}
			}
			else if (controller.game.getGhostEdibleTime(ghost) != 0)
			{
				if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.MINDISTANCE)
				{	
					return GetAggState().action(ghost);
				}
			}
			else
			{
				return GetPassState().action();
			}
		}
		
		//Check if has to transit to passive state.
		
		
		return MOVE.LEFT;
	}
	
	public void Final()
	{
		
	}
	
	public boolean isSuperMachine()
	{
		return this.bSuperMachine;
	}

	private State_Defensive GetDeffState()
	{
		for(State iState : this.states)
		{
				if(iState instanceof State_Defensive)
				{
					return (State_Defensive) iState;
				}
		}
		return null;
	}
	
	private State_Aggressive GetAggState()
	{
		for(State iState : this.states)
		{
				if(iState instanceof State_Aggressive)
				{
					return (State_Aggressive) iState;
				}
		}
		return null;
	}
	
	private State_Passive GetPassState()
	{
		for(State iState : this.states)
		{
				if(iState instanceof State_Passive)
				{
					return (State_Passive) iState;
				}
		}
		return null;
	}
}