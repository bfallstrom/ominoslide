package com.github.bfallstrom.ominoslide.solver;

import java.util.HashSet;
import java.util.Set;

public class Moves {
	private Set<Move> possibleMoves = new HashSet<Move>(); // the possible moves from the associated layout, generated at construction time
	private int numberOfMovesIn = 0;	// how many moves have been made since the initial position.
}
