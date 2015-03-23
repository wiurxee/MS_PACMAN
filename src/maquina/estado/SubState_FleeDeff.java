package maquina.estado;

import java.awt.List;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Random;

import org.omg.CORBA.portable.RemarshalException;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class SubState_FleeDeff  extends State{

	
	public void next() {
		
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		GHOST targetGhost = null;
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(controller.game.getGhostLairTime(ghost) == 0)
			{
				if(controller.game.getGhostEdibleTime(ghost) > controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)))
				{
					Final();
					return;
				}
				if(targetGhost == null)
				{
					targetGhost = ghost;
				}
				else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)))
				{
					targetGhost = ghost;
				}
			}
		}
		if(targetGhost == null || controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)) > controller.MINDISTANCE)
		{
			Final();
		}
		
		
		
		int[] targetsArray=controller.game.getActivePowerPillsIndices();		//convert from ArrayList to array
		int nearestPowerPillIndex =  controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH);			
		if(targetsArray.length != 0 && controller.game.getShortestPathDistance(current, nearestPowerPillIndex) < controller.MAXATTACKDISTANCE)
		{
			if(controller.SuperMachine.currentState instanceof State_Defensive)
			{
				State_Defensive defState = (State_Defensive) controller.SuperMachine.currentState;
				
				// set current submachine State to FindSuperPillDeff.
				defState.SubMachine.currentState = defState.SubMachine.states.get(0);
			}
		}
		
	}
	public MOVE action() 
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		

		if(controller.debug)
		{
			System.out.println("FleeDeff");
		}
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		
		
		ArrayList<GHOST> sortedGhosts = new ArrayList<GHOST>();
		// First i sort the Ghost by the distances to PacMan
		for(GHOST ghost : GHOST.values())
		{
			if(controller.game.getGhostLairTime(ghost) == 0 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1)
			{
				if(sortedGhosts.size() == 0)
				{
					sortedGhosts.add(ghost);
				}
				else
				{
					boolean bShouldAddLast = true;
					for(int i = 0 ; i < sortedGhosts.size() ; i++)
					{
						if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(sortedGhosts.get(i))))
						{
							sortedGhosts.add(i,ghost);
							bShouldAddLast = false;
							break;
						}
					}
					if(bShouldAddLast)
					{
						sortedGhosts.add(ghost);
					}	
				}
			}
		}
		
		MOVE[] possibleMoves = controller.game.getPossibleMoves(current);
		ArrayList<MOVE> possibleMovesList = new ArrayList<MOVE>();
		
		for (MOVE move : possibleMoves)
		{
			if(move != MOVE.NEUTRAL)
			{
				possibleMovesList.add(move);
			}
		}
			
		for(GHOST ghost: sortedGhosts)
		{
			MOVE move = controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(ghost), DM.PATH);
			int[] pathPacman = controller.game.getShortestPath(current, controller.game.getGhostCurrentNodeIndex(ghost));
			int primerCruce = 0;
			for(int i = 0; i < pathPacman.length ; i++)
			{
				if(controller.game.getPossibleMoves(pathPacman[i]).length > 2)
				{
					primerCruce = pathPacman[i];
					break;
				}
			}
			boolean bShouldChangeDirection = true;
			if(primerCruce != 0)
			{
				if(controller.game.getShortestPathDistance(current, primerCruce) < controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(ghost), primerCruce))
				{
					bShouldChangeDirection = false;
				}
			}
			
			for (int j = 0 ; j < possibleMovesList.size() ; j++)
			{
				if (possibleMovesList.get(j) == move && possibleMovesList.size() > 1 && bShouldChangeDirection)
				{
					possibleMovesList.remove(j);
				}
			}
		}
		if(possibleMovesList.size() == 1)
		{
			return possibleMovesList.get(0);
		}
		
		int[] powerPills = controller.game.getActivePowerPillsIndices();
		
		if(powerPills.length != 0 && possibleMovesList.size() > 1)
		{
			int nearestPowerPillIndex = controller.game.getClosestNodeIndexFromNodeIndex(current,powerPills,DM.PATH);
			MOVE move = controller.game.getNextMoveTowardsTarget(current, nearestPowerPillIndex, DM.PATH);
			for(int i = 0 ; i < possibleMovesList.size() ; i++)
			{
				if(move == possibleMovesList.get(i))
				{
					return move;
				}
				// else if there is one possibility that distance us from the target, we will remove it, because it is counterproductive
				else if(possibleMovesList.get(i) == controller.game.getNextMoveAwayFromTarget(current,nearestPowerPillIndex, DM.PATH))
				{
					possibleMovesList.remove(i);
				}
			}
		}
		Random random = new Random();	
		return possibleMovesList.get(random.nextInt(possibleMovesList.size()));
	}
	
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		controller.SuperMachine.currentState.Final();
	}
}
