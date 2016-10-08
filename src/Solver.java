import java.util.ArrayList;
import java.util.Collections;

public class Solver {

	public static ArrayList<Cube> generateReverseSolutionTree(){

		ArrayList<Cube> cubes = new ArrayList<>();

		Cube baseCube = new Cube();
		cubes.add(baseCube);

		return cubes;
	}

	public static String solveCubeSimpleSearch(Cube scrambled, int maxDepth){
		String path = "";


		return path;

	}

	private static ArrayList<String> firstStepSimpleSearch(Cube scrambled, int depth){
		if (depth == 0 && !scrambled.solved()) return null;
		return null;
	}

	public static boolean firstStepSolved(Cube cube) {
		// check colours for up/down
		ArrayList<Cube.Square> facelets_up_down;
		facelets_up_down = new ArrayList<>();
		Collections.addAll(facelets_up_down,
				// All possible facelets for up and down face
				Cube.Square.U1, Cube.Square.U2, Cube.Square.U3, Cube.Square.U4,
				Cube.Square.U5, Cube.Square.U6, Cube.Square.U7, Cube.Square.U8,
				Cube.Square.U9, Cube.Square.D1, Cube.Square.D2, Cube.Square.D3,
				Cube.Square.D4, Cube.Square.D5, Cube.Square.D6, Cube.Square.D7,
				Cube.Square.D8, Cube.Square.D9
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

	}



}
