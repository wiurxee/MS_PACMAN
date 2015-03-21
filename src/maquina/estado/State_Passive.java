package maquina.estado;

import java.util.ArrayList;

import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class State_Passive extends State 
{
	private StateMachine SubMachine;
	
	public void next() 
	{
		
	}
	
	@SuppressWarnings("static-access")
	public MOVE action() 
	{
		//Declare controller.
		pacman.controllers.examples.AJICONTROLLER controller = pacman.controllers.examples.AJICONTROLLER.singleton;
		
		int current=controller.game.getPacmanCurrentNodeIndex();
		int[] pills=controller.game.getPillIndices();
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
		for(int i=0;i<pills.length;i++)					//check which pills are available			
			if(controller.game.isPillStillAvailable(i))
				targets.add(pills[i]);
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i]=targets.get(i);
		
		//return the next direction once the closest target has been identified
		return controller.game.getNextMoveTowardsTarget(current,controller.game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH),DM.PATH);
	}
	
	public void Final()
	{
		
	}
	
	public void setSubMachine(StateMachine sub)
	{
		this.SubMachine = sub;
	}
}
