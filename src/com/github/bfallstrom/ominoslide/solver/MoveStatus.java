package com.github.bfallstrom.ominoslide.solver;

public enum MoveStatus {
	WINNING,	// A move is winning if it is a valid solution or is known to lead to a valid solution.
	BLOCKED,	// A move is blocked if it is invalid, has been pruned, or all possible moves from it are blocked.
	UNKNOWN,	// A move is unknown if at least one possible move leading from it has not been evaluated.
	GENERATED	// A move is generated if the board it leads to has already been inserted into the map.
}
