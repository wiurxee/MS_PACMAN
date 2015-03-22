package maquina.estado;

import java.util.ArrayList;

import pacman.controllers.examples.AJICONTROLLER;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class State_Passive extends State 
{
	public StateMachine SubMachine;
	
	public void next() 
	{
		SubMachine.next();
	}
	
	@SuppressWarnings("static-access")
	public MOVE action() 
	{
//		//Declare controller.
//		pacman.controllers.examples.AJICONTROLLER controller = pacman.controllers.examples.AJICONTROLLER.singleton;
//		
//		int current=controller.game.getPacmanCurrentNodeIndex();
//		int[] pills=controller.game.getPillIndices();
//		
//		ArrayList<Integer> targets=new ArrayList<Integer>();
//		
//		for(int i=0;i<pills.length;i++)					//check which pills are available			
//			if(controller.game.isPillStillAvailable(i))
//				targets.add(pills[i]);
//		
//		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
//		
//		for(int i=0;i<targetsArray.length;i++)
//			targetsArray[i]=targets.get(i);
//		
//		//return the next direction once the closest target has been identified
//		return controller.game.getNextMoveTowardsTarget(current,controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH),DM.PATH);
		return SubMachine.action();
	}
	
	public void Final()
	{
		AJICONTROLLER controller = AJICONTROLLER.singleton;
		
		//Calculate what SuperState must run.
		
		int current = controller.game.getPacmanCurrentNodeIndex();
		GHOST minGhost = null;
		
		//Check if has to transit to defensive state or aggressive state.
		for(GHOST ghost : GHOST.values())
		{
			if(minGhost == null)
			{
				minGhost = ghost;
			}
			else if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) != -1 && controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(ghost)) < controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)))
			{
				minGhost = ghost;
			}
		}
		
		if(controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)) != -1 && controller.game.getGhostEdibleTime(minGhost) >= controller.game.getShortestPathDistance(current, controller.game.getGhostCurrentNodeIndex(minGhost)))
		{
			// set State to Aggressive
			controller.SuperMachine.currentState = controller.SuperMachine.states.get(0);
		}
		else
		{
			// set State to Deffensive
			controller.SuperMachine.currentState = controller.SuperMachine.states.get(1);
		}
		
		
	}
	
	public void setSubMachine(StateMachine sub)
	{
		this.SubMachine = sub;
	}
}
