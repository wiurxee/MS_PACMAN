package ghosts.machine;

import java.awt.font.NumericShaper.Range;
import java.util.ArrayList;
import java.util.Random;

import pacman.controllers.examples.AJIGHOSTS;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class SubState_KeepOff extends State {

	public SubState_KeepOff(GHOST g) {
		super(g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public float next() {
		// TODO Auto-generated method stub
		AJIGHOSTS controller = AJIGHOSTS.singleton;
		GhostMachine myMachine = null;
		
		for(GhostMachine gMachine:controller.Ghosts)
		{
			if(gMachine.myGhost == myGhost)
			{
				myMachine = gMachine;
				break;
			}
		}
		
		// TODO Auto-generated method stub
		if(myMachine.currentState instanceof State_Aggressive)
		{
			State_Aggressive aggState = (State_Aggressive) myMachine.currentState;
			
//			aggState.SubMachine.currentState = aggState.SubMachine.states.get(0);
			
		}
		return 0;
	}

	@Override
	public MOVE action() {
		AJIGHOSTS controller = AJIGHOSTS.singleton;
		if(controller.debug)
		{
			System.out.println("proteger super pill");
		}	
		
		int[] targetsArray=controller.game.getActivePowerPillsIndices();		//convert from ArrayList to array
		int nearestPowerPillIndex =  controller.game.getClosestNodeIndexFromNodeIndex(controller.game.getPacmanCurrentNodeIndex(),targetsArray,DM.PATH);
				
		int[] pathPacmanSuperPill = controller.game.getShortestPath(controller.game.getPacmanCurrentNodeIndex(), nearestPowerPillIndex);
		
		for(int i = 0; i < pathPacmanSuperPill.length ; i++)
		{
			if(pathPacmanSuperPill[i] == controller.game.getGhostCurrentNodeIndex(myGhost))
			{
				return controller.game.getApproximateNextMoveTowardsTarget(controller.game.getGhostCurrentNodeIndex(myGhost), controller.game.getPacmanCurrentNodeIndex(), controller.game.getGhostLastMoveMade(myGhost), DM.PATH);
			}
		}	
		
		int crossNearPillIndice = 0;
		
		for(int i = pathPacmanSuperPill.length - 1; i > 0 ; i--)
		{
			if(controller.game.getPossibleMoves(pathPacmanSuperPill[i]).length > 2)
			{
				crossNearPillIndice = pathPacmanSuperPill[i];
				break;
			}
		}		
				
		return controller.game.getApproximateNextMoveTowardsTarget(controller.game.getGhostCurrentNodeIndex(myGhost), crossNearPillIndice, controller.game.getGhostLastMoveMade(myGhost), DM.PATH);
	}

	@Override
	public void Final() {
		// TODO Auto-generated method stub
		
	}

}
