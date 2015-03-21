package maquina.estado;

import java.util.ArrayList;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class SubState_RecollectPass extends State {

	public void next() {
	}
	public MOVE action() {
		
		AJICONTROLLER controller = AJICONTROLLER.singleton;
			
		int[] pills = AJICONTROLLER.game.getPillIndices();
		
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		for(int i = 0; i < pills.length ; i++)
		{
			if(controller.game.isPillStillAvailable(i))
			{
				targets.add(pills[i]);
			}
		}
		
		int[] targetsArray = new int[targets.size()];
		
		for(int i = 0 ; i < targetsArray.length; i++)
		{
			targetsArray[i] = targets.get(i);
		}
		
		return controller.game.getNextMoveTowardsTarget(controller.game.getPacmanCurrentNodeIndex(), controller.game.getClosestNodeIndexFromNodeIndex(controller.game.getPacmanCurrentNodeIndex(),targetsArray,DM.PATH), DM.PATH);
	}
	public void Final()
	{
		
	}
}
