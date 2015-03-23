package maquina.estado;

import java.awt.List;
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
		if(controller.game.getActivePowerPillsIndices().length == 0 || targetGhost == null || controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(targetGhost)) > controller.MINDISTANCE)
		{
			Final();
		}
		
		int[] powerPills = controller.game.getPowerPillIndices();	
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
			
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
		{	
			if(controller.game.isPowerPillStillAvailable(i))
			{
				targets.add(powerPills[i]);	
			}
		}
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
		{
			targetsArray[i]=targets.get(i);
		}
		if(targetsArray.length != 0 && targetGhost != null)
		{
			
			int nearestPowerPillIndex = controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH);
			
			boolean bShouldStayOnFlee = false;
			
			for(GHOST ghost : GHOST.values())
			{
				if(controller.game.getGhostLairTime(ghost) == 0)
				{
					int[] pathPacman = controller.game.getShortestPath(nearestPowerPillIndex,current);
					int[] pathGhost = controller.game.getShortestPath(controller.game.getGhostCurrentNodeIndex(ghost), current);
					
					for(int i = 0; i < pathGhost.length ; i++)
					{
						for(int j = 0 ; j < pathPacman.length ; j++)
						{
							if(pathGhost[i] == pathPacman[j] && controller.game.getShortestPathDistance(controller.game.getGhostCurrentNodeIndex(ghost) , pathGhost[i]) < controller.game.getShortestPathDistance(current, pathGhost[i]))
							{
								bShouldStayOnFlee = true;
								break;
							}
						}
					}
				}
			}
			
			if(!bShouldStayOnFlee)
			{
				if(controller.SuperMachine.currentState instanceof State_Defensive)
				{
					State_Defensive defState = (State_Defensive) controller.SuperMachine.currentState;
					
					// set current submachine State to FindSuperPillDeff.
					defState.SubMachine.currentState = defState.SubMachine.states.get(0);
				}
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
			if(sortedGhosts.size() == 0)
			{
				sortedGhosts.add(ghost);
			}
			else
			{
				boolean bShouldAddLast = true;
				for(int i = 0 ; i < sortedGhosts.size() ; i++)
				{
					if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(sortedGhosts.get(i))))
					{
						sortedGhosts.add(i,ghost);
						bShouldAddLast = false;
						break;
					}
				}
				if(bShouldAddLast && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1)
				{
					sortedGhosts.add(ghost);
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
			
		for(GHOST ghost : sortedGhosts)
		{
			MOVE move = controller.game.getNextMoveTowardsTarget(current, controller.game.getGhostCurrentNodeIndex(ghost), DM.PATH);
			
			for (int i = 0 ; i > possibleMovesList.size() ; i++)
			{
				if (possibleMovesList.get(i) == move && possibleMovesList.size() > 1)
				{
					possibleMovesList.remove(i);
				}
			}
		}
		
		int[] powerPills = controller.game.getPowerPillIndices();	
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
			
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
		{	
			if(controller.game.isPowerPillStillAvailable(i))
			{
				targets.add(powerPills[i]);	
			}
		}
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
		{
			targetsArray[i]=targets.get(i);
		}
		if(targetsArray.length != 0)
		{
			int nearestPowerPillIndex = controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH);
			MOVE move = controller.game.getNextMoveTowardsTarget(current, nearestPowerPillIndex, DM.PATH);
			for(int i = 0 ; i < possibleMovesList.size() ; i++)
			{
				if(move == possibleMovesList.get(i))
				{
					return move;
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
