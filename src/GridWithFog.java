// grid covered initially with only fog (~), used for keeping track of opponent's ships positions
public class GridWithFog extends GridWithShips {

    String[][] grid = new String[N][N];

    GridWithFog() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = "~";
            }
        }
    }
}
