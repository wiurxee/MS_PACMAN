package maquina.estado;

import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class SubState_FleeDeff  extends SubMachine_Deffensive{

	public SubMachine_Deffensive next(Game game) {
		return super.next(game);
	}
	public MOVE action() {
		return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), closestIndex, DM.PATH);
	}
}
