package com.github.bfallstrom.ominoslide.solver;

import java.util.HashMap;
import java.util.Map;

import com.github.bfallstrom.ominoslide.areastructure.Board;
import com.github.bfallstrom.ominoslide.areastructure.WinningPosition;

public class Boards {
	private Map<Board,Moves>	states = new HashMap<Board,Moves>(); // maps each known board state to the possible moves that can be made from it
	private WinningPosition		solved = null;	// The position to seek.
	private int					winFoundAt = Integer.MAX_VALUE;
}
