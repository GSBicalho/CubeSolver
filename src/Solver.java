import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class Solver {

	public static final	int MAX_DEPTH_FIRST_STEP = 18;
	public static final int MAX_DEPTH_SECOND_STEP = 7;
	public static final String[][][] moves_to_try = {
			{
					{"F", "F'", "F2"},
					{"R", "R'", "R2"},
					{"U", "U'", "U2"},
					{"D", "D'", "D2"},
					{"L", "L'", "L2"},
					{"B", "B'", "B2"}
			},
			{
					{"F2"},
					{"R2"},
					{"U", "U'", "U2"},
					{"D", "D'", "D2"},
					{"L2"},
					{"B2"}
			}
	};

	public static ArrayList<Cube> generateReverseSolutionTree(){

		ArrayList<Cube> cubes = new ArrayList<>();

		Cube baseCube = new Cube();
		cubes.add(baseCube);

		return cubes;
	}

	public static String solveCubeHeuristicSearch(Cube scrambled, int depth){
		StringBuilder path = new StringBuilder("");

		Stack<String> pathList = new Stack<>();
		if (!heuristicSearch(scrambled, depth, pathList)) return "Failed";
		System.out.println("path: " + pathList.toString());
		while (!pathList.empty()) path.append(pathList.pop()).append(" ");

		return path.toString();
	}

	public static boolean heuristicSearch(Cube scrambled, int depth, Stack<String> pathList) {
		int threshold = getEstimatedCost(scrambled);
		while(true) {
			int tmp = heuristicSearch(
					0, scrambled.clone(), depth, pathList, -1, "", 0, threshold
			);
			if (tmp == -1) {
				return true;
			}
			if (tmp == Integer.MAX_VALUE){
				return false;
			}

			threshold = tmp;
		}
	}

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
		int f = gCost + getEstimatedCost(scrambled);
		if (f > threshold)
			return f;
		if (solved(step, scrambled)) {
			step = step + 1;
			if (step == 2){
				path.push(move);
				return -1;
			}
		}
		int min = Integer.MAX_VALUE;
		if (depth == 0)
			return min;
		for (int i = 0, len = moves_to_try[step].length; i < len; i++) {
			System.out.println("trying move " + move + " in depth " + depth);
			if (i == lastMoveCategory) {
				continue;
			}
			if (i + lastMoveCategory == len - 1 && lastMoveCategory >= len / 2){
				continue;
			}

			for (String m : moves_to_try[step][i]) {
				int tmp = heuristicSearch(
						step, scrambled.clone(), depth - 1,
						path, i, m, gCost + 1, threshold
				);
				if (tmp == -1){
					path.push(m);
					return -1;
				}
				if (tmp < min) min = tmp;
			}
			return min;
		}
		return -1;
	}

	public static int getEstimatedCost(Cube node) {
		float cornersH = 0;
		float edgesH = 0;

		Cube.CornerCubelet[] Ccubelets = node.getCornerCubelets();
		for (byte i = 0; i < Ccubelets.length; i++) {
			Cube.CornerCubelet cornerCubelet = Ccubelets[i];
			if (cornerCubelet.rotation == Cube.CornerCubeletRotation.R1)
				cornersH += node.getRot0Corners(Cube.intToCornerCubeletName(i), cornerCubelet.name);
			else if (cornerCubelet.rotation == Cube.CornerCubeletRotation.R2)
				cornersH += node.getRot1Corners(Cube.intToCornerCubeletName(i), cornerCubelet.name);
			else
				cornersH += node.getRot2Corners(Cube.intToCornerCubeletName(i), cornerCubelet.name);
		}
		Cube.EdgeCubelet[] Ecubelets = node.getEdgeCubelets();
		for (byte i = 0; i < Ecubelets.length; i++) {
			Cube.EdgeCubelet edgeCubelet = Ecubelets[i];
			if (edgeCubelet.rotation == Cube.EdgeCubeletRotation.R1)
				edgesH += node.getRot0Edges(Cube.intToEdgeCubeletName(i), edgeCubelet.name);
			else
				edgesH += node.getRot1Edges(Cube.intToEdgeCubeletName(i), edgeCubelet.name);
		}

		cornersH /= 4;
		edgesH /= 4;
		return (int) Math.ceil(Float.max(cornersH, edgesH));

	}

	public static int manhattanBFS(Cube[] nodes, int depth, int[] status) {
		for (Cube cube : nodes) {
			Integer[] s = isFinalNode(cube);
			for (int solvedCubelet : s) {
				status[solvedCubelet] = depth;
			}
			if (Arrays.stream(status).filter(a -> a == -1).count() == 0) {
				float cornersH = 0;
				float edgesH = 0;
				for (int i = 0; i < 8; i++){
					cornersH += status[i];
				}
				for (int i = 8; i < 20; i++){
					edgesH += status[i];
				}
				cornersH /= 4;
				edgesH /= 4;
				return (int) Math.ceil(Float.max(cornersH, edgesH));
			}
		}
		return manhattanBFS(expandNodes(nodes, -1), depth + 1, status);
	}

	private static Cube[] expandNodes(Cube[] nodes, int lastMoveCategory){
		String[][] moves = moves_to_try[0];

		ArrayList<Cube> ret = new ArrayList<>(nodes.length*15);
		for (int i = 0, len = moves.length; i < len; i++) {
			if (i == lastMoveCategory) {
				continue;
			}
			if (i + lastMoveCategory == len - 1 && lastMoveCategory >= len / 2) {
				continue;
			}

			for (Cube c : nodes) {
				for (String m : moves[i]) {
					ret.add(c.clone().executeCommand(m));
				}
			}
		}

		return ret.toArray(new Cube[ret.size()]);
	}

	private static Integer[] isFinalNode(Cube node){
		ArrayList<Integer> tmp = new ArrayList<>(20);

		Cube.CornerCubelet[] ccs = node.getCornerCubelets();
		for (int i = 0, ccsLength = ccs.length; i < ccsLength; i++) {
			Cube.CornerCubelet cornerCubelet = ccs[i];
			if (ccs[cornerCubelet.name.v].name == cornerCubelet.name) {
				if (cornerCubelet.rotation == Cube.CornerCubeletRotation.R1) {
					tmp.add(i);
				}
			}
		}

		Cube.EdgeCubelet[] ecs = node.getEdgeCubelets();
		for (int i = 0, ecsLength = ecs.length; i < ecsLength; i++) {
			Cube.EdgeCubelet edgeCubelet = ecs[i];
			if (ecs[edgeCubelet.name.v].name == edgeCubelet.name) {
				if (edgeCubelet.rotation == Cube.EdgeCubeletRotation.R1) {
					tmp.add(i);
				}
			}

		}

		return tmp.toArray(new Integer[tmp.size()]);
	}

	public static String solveCubeSimpleSearch(Cube scrambled, int depth){
		StringBuilder path = new StringBuilder("");

		Stack<String> pathList = new Stack<>();
		if (!simpleSearch(scrambled, depth, pathList)) return "Failed";
		System.out.println("path: " + pathList.toString());
		while (!pathList.empty()) path.append(pathList.pop()).append(" ");

		return path.toString();

	}

	private static boolean simpleSearch(Cube scrambled, int depth, Stack<String> pathList) {
		return simpleSearch(0, scrambled.clone(), depth, pathList, "", -1);
	}

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
			step += 1;
			if (step == 2) {
				path.push(move);
				return true;
			}
		}
		if (depth == 0) {
			return false;
		}

		for (int i = 0, len = moves_to_try[step].length; i < len; i++) {
			if (i == lastMoveCategory) {
				continue;
			}
			if (i + lastMoveCategory == len-1 && lastMoveCategory >= len/2) {
				continue;
			}
			for (String m : moves_to_try[step][i]) {
				Cube clone = scrambled.clone();
				if (simpleSearch(step, clone, depth - 1, path, m, i)) {
					path.push(move);
					return true;
				}
			}
		}

		return false;
	}

	public static boolean solved(int step, Cube cube) {
		switch (step){
			case 0:
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
			case 1:
			default:
				return cube.equals(new Cube());

		}

	}



}
