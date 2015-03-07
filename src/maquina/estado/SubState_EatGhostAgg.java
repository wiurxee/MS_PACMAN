package maquina.estado;

import pacman.game.Constants.MOVE;

public final class SubState_EatGhostAgg extends State_Aggresive{

	public AjiMaquinaEstado next() {
		return super.next();		
	}
	public MOVE action() {
		return null;
	}
}
