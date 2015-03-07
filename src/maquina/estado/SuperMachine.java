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
	public SuperMachine next()
	{
		int currentNode = AJICONTROLLER.game.getPacmanCurrentNodeIndex();
		
		boolean bShouldBeAggresive = true;
		for (GHOST ghost: GHOST.values()) 
		{
			if (AJICONTROLLER.game.getGhostEdibleTime(ghost) == 0 && AJICONTROLLER.game.getGhostLairTime(ghost) == 0)
			{
				if (AJICONTROLLER.game.getShortestPathDistance(currentNode, AJICONTROLLER.game.getGhostCurrentNodeIndex(ghost))< MINDISTANCE)
				{
					deffensive.substate.setGhostToRunFromNodeIndex(AJICONTROLLER.game.getGhostCurrentNodeIndex(ghost));
					return deffensive;
				}
			}
			else if(AJICONTROLLER.game.getGhostEdibleTime(ghost) > 0 && AJICONTROLLER.game.getShortestPathDistance(currentNode, AJICONTROLLER.game.getGhostCurrentNodeIndex(ghost)) < game.getGhostEdibleTime(ghost)-1)
			{
				bShouldBeAggresive = false;
			}
		}
		if(bShouldBeAggresive)
		{
			return aggresive;
		}
		else
		{
			return passive;
		}
	}
	/**
	 * This method will return the next move of Pacman
	 * @return next MOVE of Pacman
	 */
	public abstract MOVE action();
}
