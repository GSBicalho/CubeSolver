import javafx.scene.paint.Color;

public class Samples {
	public static final Cube CUBE_PHASE_2 = getCubePhase2();
	public static final Cube CUBE_SCRAMBLED = getCubeScrambled();
	public static final Cube CUBE_2_AWAY = getCube2Away();

	private static Cube getCubePhase2(){
		Color[][][] aux = {
				{
						{Cube.UP_COLOR, Cube.UP_COLOR, Cube.UP_COLOR},
						{Cube.UP_COLOR, Cube.UP_COLOR, Cube.UP_COLOR},
						{Cube.UP_COLOR, Cube.UP_COLOR, Cube.DOWN_COLOR},
				},
				{
						{Cube.LEFT_COLOR, Cube.FRONT_COLOR, Cube.RIGHT_COLOR},
						{Cube.FRONT_COLOR, Cube.FRONT_COLOR, Cube.BACK_COLOR},
						{Cube.FRONT_COLOR, Cube.LEFT_COLOR, Cube.BACK_COLOR},
				},
				{
						{Cube.FRONT_COLOR, Cube.BACK_COLOR, Cube.LEFT_COLOR},
						{Cube.RIGHT_COLOR, Cube.RIGHT_COLOR, Cube.LEFT_COLOR},
						{Cube.LEFT_COLOR, Cube.RIGHT_COLOR, Cube.RIGHT_COLOR},
				},
				{
						{Cube.FRONT_COLOR, Cube.RIGHT_COLOR, Cube.FRONT_COLOR},
						{Cube.BACK_COLOR, Cube.BACK_COLOR, Cube.FRONT_COLOR},
						{Cube.BACK_COLOR, Cube.FRONT_COLOR, Cube.BACK_COLOR},
				},
				{
						{Cube.RIGHT_COLOR, Cube.LEFT_COLOR, Cube.BACK_COLOR},
						{Cube.RIGHT_COLOR, Cube.LEFT_COLOR, Cube.LEFT_COLOR},
						{Cube.RIGHT_COLOR, Cube.BACK_COLOR, Cube.LEFT_COLOR},
				},
				{
						{Cube.DOWN_COLOR, Cube.DOWN_COLOR, Cube.DOWN_COLOR},
						{Cube.DOWN_COLOR, Cube.DOWN_COLOR, Cube.DOWN_COLOR},
						{Cube.UP_COLOR, Cube.DOWN_COLOR, Cube.DOWN_COLOR},
				}
		};
		Cube cube = null;
		try {
			cube = new Cube(aux);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
		}

		return cube;
	}

	private static Cube getCubeScrambled(){

		Color[][][] aux = {
				{
						{Cube.FRONT_COLOR, Cube.LEFT_COLOR, Cube.FRONT_COLOR},
						{Cube.DOWN_COLOR, Cube.LEFT_COLOR, Cube.FRONT_COLOR},
						{Cube.FRONT_COLOR, Cube.LEFT_COLOR, Cube.RIGHT_COLOR}
				},
				{
						{Cube.LEFT_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
						{Cube.RIGHT_COLOR, Cube.FRONT_COLOR, Cube.FRONT_COLOR},
						{Cube.BACK_COLOR, Cube.RIGHT_COLOR, Cube.UP_COLOR}
				},
				{
						{Cube.DOWN_COLOR, Cube.DOWN_COLOR, Cube.FRONT_COLOR},
						{Cube.BACK_COLOR, Cube.DOWN_COLOR, Cube.UP_COLOR},
						{Cube.RIGHT_COLOR, Cube.LEFT_COLOR, Cube.BACK_COLOR}
				},
				{
						{Cube.UP_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
						{Cube.FRONT_COLOR, Cube.UP_COLOR, Cube.FRONT_COLOR},
						{Cube.DOWN_COLOR, Cube.RIGHT_COLOR, Cube.RIGHT_COLOR}
				},
				{
						{Cube.UP_COLOR, Cube.UP_COLOR, Cube.DOWN_COLOR},
						{Cube.DOWN_COLOR, Cube.RIGHT_COLOR, Cube.UP_COLOR},
						{Cube.RIGHT_COLOR, Cube.LEFT_COLOR, Cube.UP_COLOR}
				},
				{
						{Cube.LEFT_COLOR, Cube.UP_COLOR, Cube.LEFT_COLOR},
						{Cube.RIGHT_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
						{Cube.LEFT_COLOR, Cube.DOWN_COLOR, Cube.DOWN_COLOR}
				}
		};

		Cube c = null;
		try {
			c = new Cube(aux);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
		}
		return c;
	}

	private static Cube getCube2Away() {
		Color[][][] aux = {
				{
						{Cube.UP_COLOR, Cube.UP_COLOR, Cube.UP_COLOR},
						{Cube.UP_COLOR, Cube.UP_COLOR, Cube.UP_COLOR},
						{Cube.FRONT_COLOR, Cube.FRONT_COLOR, Cube.FRONT_COLOR},
				},
				{
						{Cube.RIGHT_COLOR, Cube.RIGHT_COLOR, Cube.RIGHT_COLOR},
						{Cube.FRONT_COLOR, Cube.FRONT_COLOR, Cube.DOWN_COLOR},
						{Cube.FRONT_COLOR, Cube.FRONT_COLOR, Cube.DOWN_COLOR},
				},
				{
						{Cube.UP_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
						{Cube.RIGHT_COLOR, Cube.RIGHT_COLOR, Cube.RIGHT_COLOR},
						{Cube.RIGHT_COLOR, Cube.RIGHT_COLOR, Cube.RIGHT_COLOR},
				},
				{
						{Cube.LEFT_COLOR, Cube.LEFT_COLOR, Cube.LEFT_COLOR},
						{Cube.UP_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
						{Cube.UP_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
				},
				{
						{Cube.FRONT_COLOR, Cube.FRONT_COLOR, Cube.DOWN_COLOR},
						{Cube.LEFT_COLOR, Cube.LEFT_COLOR, Cube.LEFT_COLOR},
						{Cube.LEFT_COLOR, Cube.LEFT_COLOR, Cube.LEFT_COLOR},
				},
				{
						{Cube.DOWN_COLOR, Cube.DOWN_COLOR, Cube.BACK_COLOR},
						{Cube.DOWN_COLOR, Cube.DOWN_COLOR, Cube.BACK_COLOR},
						{Cube.DOWN_COLOR, Cube.DOWN_COLOR, Cube.BACK_COLOR},
				}
		};
		Cube cube = null;
		try {
			cube = new Cube(aux);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
		}

		return cube;
	}
}
