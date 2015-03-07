package maquina.estado;

import java.util.ArrayList;

import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public abstract class SubMachine_Deffensive {

	protected int ghostToRunFromNodeIndex;
	protected int closestIndex;
	protected Game game;
	
	SubState_FindSuperPillDeff findSuperPill;
	SubState_FleeDeff flee;
	
	public int getGhostToRunFromNodeIndex() {
		return ghostToRunFromNodeIndex;
	}
	public void setGhostToRunFromNodeIndex(int ghostToRunFromNodeIndex) {
		this.ghostToRunFromNodeIndex = ghostToRunFromNodeIndex;
	}
	/**
	 *  This method will change the actual state depending on different values
	 */
	public SubMachine_Deffensive next(Game game)
	{
		this.game = game;
		int[] superPills = game.getPowerPillIndices();
		
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		for(int i = 0; i < superPills.length ; i++)
		{
			if(game.isPowerPillStillAvailable(i))
			{
				targets.add(superPills[i]);
			}
		}
		
		int[] targetsArray = new int[targets.size()+1];
		
		for(int i = 0 ; i < targetsArray.length -1 ; i++)
		{
			targetsArray[i] = targets.get(i);
		}
		
		targetsArray[targetsArray.length - 1] = ghostToRunFromNodeIndex;
		
		closestIndex = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray, DM.PATH);
		
		if(closestIndex == ghostToRunFromNodeIndex)
		{
			return flee;
		}
		else
		{
			return findSuperPill;
		}
	}
	/**
	 * This method will return the next move of Pacman
	 * @return next MOVE of Pacman
	 */
	public abstract MOVE action();
	
}
