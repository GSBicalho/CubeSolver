import java.util.ArrayList;
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
		return "";
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
		return simpleSearch(0, scrambled.clone(), depth, pathList, "", -1, false);
	}

	private static boolean simpleSearch(
											   int step,
											   Cube scrambled,
											   int depth,
											   Stack<String> path,
											   String move,
											   int lastMoveCategory,
											   boolean lockOppositeCategory
	){
		scrambled.executeCommands(move);
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

		for (int i = 0; i < moves_to_try[step].length; i++) {
			if (i == lastMoveCategory) {
				continue;
			}
			if (lockOppositeCategory) {
				if (i + lastMoveCategory == moves_to_try[step].length - 1) {
					continue;
				}
			}
			for (String m : moves_to_try[step][i]) {
				boolean lock = (i + lastMoveCategory == moves_to_try[step].length -1);
				Cube clone = scrambled.clone();
				if (simpleSearch(step, clone, depth - 1, path, m, i, lock)) {
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
