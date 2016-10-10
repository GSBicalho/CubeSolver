import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Solver {

	public static final String[][][] moves_to_try = {
			{ // Possible moves for a scrambled cube (step 0)
					{"F", "F'", "F2"},
					{"R", "R'", "R2"},
					{"U", "U'", "U2"},
					{"D", "D'", "D2"},
					{"L", "L'", "L2"},
					{"B", "B'", "B2"}
			},
			{ // Possible moves for a cube that is in G1 (H case)
					{"F2"},
					{"R2"},
					{"U", "U'", "U2"},
					{"D", "D'", "D2"},
					{"L2"},
					{"B2"}
			}
	};

	// Solve a scrambled cube using IDA* search with 3D Manhattan Distance as
	// heuristic function. Stop at depth 'depth' on the possibility tree
	public static String solveCubeHeuristicSearch(Cube scrambled, int depth){
		StringBuilder path = new StringBuilder("");

		// Use a stack to store sequence of movements leading to solved state
		Stack<String> pathList = new Stack<>();
		// run IDA* algorithm
		if (!heuristicSearch(scrambled, depth, pathList)) return "Failed";
		System.out.println("path: " + pathList.toString());
		// Generate readable string of moves
		while (!pathList.empty()) path.append(pathList.pop()).append(" ");

		return path.toString();
	}

	// Main IDA* function
	public static boolean heuristicSearch(Cube scrambled, int depth, Stack<String> pathList) {
		int threshold = getEstimatedCost(scrambled);
		System.out.println("estimated cost: " + threshold);
		while(true) { // IDA* Iterative deepening loop
			// Call IDA* search function
			int tmp = heuristicSearch(
					0, scrambled.clone(), depth, pathList, -1, "", 0, threshold
			);
			if (tmp == -1) { // Found solution node
				return true;
			}
			if (tmp == Integer.MAX_VALUE){ // Couldn't find any more nodes: Failed
				return false;
			}

			threshold = tmp; // increase threshold
		}
	}

	// IDA* Recursive Search Function
	public static int heuristicSearch(
												int step,
												Cube scrambled,
												int depth,
												Stack<String> path,
												int lastMoveCategory,
												String move,
												int gCost,
												int threshold
	){
		scrambled.executeCommand(move);
		// A* Fscore = g + h
		int f = gCost + getEstimatedCost(scrambled);
		if (f > threshold)
			return f;
		while (solved(step, scrambled)) {
			// Found a solution to current step, advance to the next
			step = step + 1;
			if (step == 2){
				// second step was the last one, push the move that solved
				// the cube and return
				path.push(move);
				return -1;
			}
		}
		int min = Integer.MAX_VALUE;
		if (depth == 0)
			return min; // ran out of tries, return "infinite"
		// For each move category on this step (F, R, U, D, L, B face turns)
		for (int i = 0, len = moves_to_try[step].length; i < len; i++) {
			if (i == lastMoveCategory) {
				// ignore all movements that are on the same category as the last one
				continue;
			}
			if (i + lastMoveCategory == len - 1 && lastMoveCategory >= len / 2){
				// ignore all movements opposite to the last
				// one if they are out of order
				continue;
			}

			// for each move in that category
			for (String m : moves_to_try[step][i]) {
				// expand another node in the tree
				int tmp = heuristicSearch(
						step, scrambled.clone(), depth - 1,
						path, i, m, gCost + 1, threshold
				);
				if (tmp == -1){
					// if expanded node found a solution, push move that lead
					// here onto the stack and return
					path.push(move);
					return -1;
				}
				// find lowest cost path to continue (A*)
				if (tmp < min) min = tmp;
			}
		}
		// return lowest cost path
		return min;
	}

	// For any cube configuration, estimate how many moves are needed
	// to solve it. This approximation must be lower bound to the actual number
	// of moves needed, to be admissible as a heuristic function to the IDA*
	public static int getEstimatedCost(Cube node) {
		float cornersH = 0;
		float edgesH = 0;

		// get all Corners of the cube
		Cube.CornerCubelet[] Ccubelets = node.getCornerCubelets();
		// for each of the 8 corners, sum all the Manhattan Distances of each
		for (byte i = 0; i < Ccubelets.length; i++) {
			Cube.CornerCubelet cornerCubelet = Ccubelets[i];
			if (cornerCubelet.rotation == Cube.CornerCubeletRotation.R1)
				cornersH += node.getRot0Corners(Cube.intToCornerCubeletName(i), cornerCubelet.name);
			else if (cornerCubelet.rotation == Cube.CornerCubeletRotation.R2)
				cornersH += node.getRot1Corners(Cube.intToCornerCubeletName(i), cornerCubelet.name);
			else
				cornersH += node.getRot2Corners(Cube.intToCornerCubeletName(i), cornerCubelet.name);
		}
		// get all Edges of the cube
		Cube.EdgeCubelet[] Ecubelets = node.getEdgeCubelets();
		// for each of the 12 edges, sum all the Manhattan Distances of each
		for (byte i = 0; i < Ecubelets.length; i++) {
			Cube.EdgeCubelet edgeCubelet = Ecubelets[i];
			if (edgeCubelet.rotation == Cube.EdgeCubeletRotation.R1)
				edgesH += node.getRot0Edges(Cube.intToEdgeCubeletName(i), edgeCubelet.name);
			else
				edgesH += node.getRot1Edges(Cube.intToEdgeCubeletName(i), edgeCubelet.name);
		}

		// For the heuristic to be admissible, both values must be divided
		// by 4, since every face turn moves 4 corners and 4 edges
		cornersH /= 4;
		edgesH /= 4;
		// return the floor of the maximum of both numbers.
		// This gives a good estimate
		return (int) Math.floor(Float.max(cornersH, edgesH));

	}

	// Solve a scrambled cube using blind DFS search for up to a "depth" deep tree
	public static String solveCubeSimpleSearch(Cube scrambled, int depth){
		StringBuilder path = new StringBuilder("");

		// Use a stack to store sequence of movements leading to solved state
		Stack<String> pathList = new Stack<>();
		// run DFS algorithm
		if (!simpleSearch(scrambled, depth, pathList)) return "Failed";
		System.out.println("path: " + pathList.toString());
		// Generate readable string of moves
		while (!pathList.empty()) path.append(pathList.pop()).append(" ");

		return path.toString();

	}

	// Wrapper to call DFS the first time around, filling its arguments
	private static boolean simpleSearch(Cube scrambled, int depth, Stack<String> pathList) {
		return simpleSearch(0, scrambled.clone(), depth, pathList, "", -1);
	}

	// DFS Algorithm to solve the cube
	private static boolean simpleSearch(
											   int step,
											   Cube scrambled,
											   int depth,
											   Stack<String> path,
											   String move,
											   int lastMoveCategory
	){
		scrambled.executeCommand(move);
		while (solved(step, scrambled)) {
			// Found a solution to current step, advance to the next
			step = step + 1;
			if (step == 2){
				// second step was the last one, push the move that solved
				// the cube and return
				path.push(move);
				return true;
			}
		}
		if (depth == 0) {
			// reached maximum depth, return
			return false;
		}

		// For each move category on this step (F, R, U, D, L, B face turns)
		for (int i = 0, len = moves_to_try[step].length; i < len; i++) {
			if (i == lastMoveCategory) {
				// ignore all movements that are on the same category as the last one
				continue;
			}
			if (i + lastMoveCategory == len-1 && lastMoveCategory >= len/2) {
				// ignore all movements opposite to the last
				// one if they are out of order
				continue;
			}
			// for each move in that category
			for (String m : moves_to_try[step][i]) {
				Cube clone = scrambled.clone();
				// expand next node with that move
				if (simpleSearch(step, clone, depth - 1, path, m, i)) {
					// if this expansion solved the cube, push the move that
					// lead to it and return
					path.push(move);
					return true;
				}
			}
		}

		// Couldn't find solution (cube is impossible with this depth)
		return false;
	}

	// Returns if the current cube is solved.
	// solved depends on the step.
	// For the first step (0), solved means that
	// the cube is in the G1 subgroup of permutations (case H)
	// For the second step (1), solved means that all faces have only one colour
	public static boolean solved(int step, Cube cube) {
		switch (step){
			case 0: // check for G1 subgroup (case H)
				// check colours for up/down
				ArrayList<Cube.Square> facelets_up_down;
				facelets_up_down = new ArrayList<>();
				Collections.addAll(facelets_up_down,
						// All possible facelets for up and down face
						Cube.Square.U1, Cube.Square.U2, Cube.Square.U3,
						Cube.Square.U4, Cube.Square.U5, Cube.Square.U6,
						Cube.Square.U7, Cube.Square.U8, Cube.Square.U9,
						Cube.Square.D1, Cube.Square.D2, Cube.Square.D3,
						Cube.Square.D4, Cube.Square.D5, Cube.Square.D6,
						Cube.Square.D7, Cube.Square.D8, Cube.Square.D9
				);
				// check colours for front and back
				ArrayList<Cube.Square> facelets_front_back;
				facelets_front_back = new ArrayList<>();
				Collections.addAll(facelets_front_back,
						// All possible facelets for front 4th and 6th and
						// back 4th and 6th positions
						Cube.Square.F4, Cube.Square.F6,
						Cube.Square.B4, Cube.Square.B6
				);
				for(Cube.Square[][] face : new Cube.Square[][][]{cube.up.m, cube.down.m})
					for (Cube.Square[] facelets : face)
						for (Cube.Square facelet : facelets) {
							if (!facelets_up_down.contains(facelet)) return false;
						}

				return
						facelets_front_back.contains(cube.front.m[1][0]) &&
								facelets_front_back.contains(cube.front.m[1][2]) &&
								facelets_front_back.contains(cube.back.m[1][0]) &&
								facelets_front_back.contains(cube.back.m[1][2]);
			case 1: // check if the cube is actually 'solved'
			default:
				return cube.solved();

		}

	}



}
