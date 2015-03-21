package maquina.estado;

import java.util.ArrayList;

import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class SubState_RecollectPass extends State {

	public void next() {
	}
	public MOVE action() {
		
		Game game = pacman.controllers.examples.AJICONTROLLER.game;
		
		int[] pills = game.getPillIndices();
		
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		for(int i = 0; i < pills.length ; i++)
		{
			if(game.isPillStillAvailable(i))
			{
				targets.add(pills[i]);
			}
		}
		
		int[] targetsArray = new int[targets.size()];
		
		for(int i = 0 ; i < targetsArray.length; i++)
		{
			targetsArray[i] = targets.get(i);
		}
		
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),targetsArray,DM.PATH), DM.PATH);
	}
	public void Final()
	{
		
	}
}
