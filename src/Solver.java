import java.util.ArrayList;

public class Solver {

	private Cube cube;

	public Solver(Cube cube){
		this.cube = cube;
	}

	public ArrayList<Cube> generateReverseSolutionTree(){

		ArrayList<Cube> cubes = new ArrayList<>();

		Cube baseCube = new Cube();
		cubes.add(baseCube);

		return cubes;
	}



}
