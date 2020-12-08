//// class used for actual game
public class Game extends GridWithShips {

    static GridWithFog[] gridWithFog = new GridWithFog[2];
    static GridWithShips[] gridWithShips = new GridWithShips[2];

    Game() {
        gridWithFog[0] = new GridWithFog();
        gridWithFog[1] = new GridWithFog();
        gridWithShips[0] = new GridWithShips(1);
        gridWithShips[1] = new GridWithShips(2);
        playGame();
    }

    public static void playGame() {

        int p = 0; // keeps track of the current player

        // main loop of the game where players take shots in turns, continues until the game is finished
        while (true) {

            int x, y;
            String cor;
            printGrids(p);
            System.out.printf("\nPlayer %d, it's your turn:\n\n> ", p%2 + 1);
            cor = scanner.next();

            // checks if the input is correct
            while (true) {
                y = Character.toUpperCase(cor.charAt(0)) - LC;
                x = cor.charAt(1) - NC;
                if (cor.length() == 3) {
                    if (cor.charAt(2) - NC == -1) {
                        x = 9;
                    } else {
                        x = 10; // arbitrarily chosen value, used in condition below to determine wrong input
                    }
                }
                // check if coordinates are valid i.e. between 0 and 9
                if (y < 0 || y > 9 || x < 0 || x > 9) {
                    System.out.printf("Error! You entered the wrong coordinates! Try again:%n%n> ");
                    cor = scanner.next();
                } else break;
            }
            System.out.println();

            // checks if given shot is a hit or a miss, accounting for the case when players shots the same cell again
            if (gridWithShips[Math.abs(p%2-1)].grid[y][x].equals("O") || gridWithFog[p%2].grid[y][x].equals("X")) {
                gridWithShips[Math.abs(p%2-1)].grid[y][x] = "X";
                gridWithFog[p%2].grid[y][x] = "X";
                // if the shot is a hit, has to check if it ends the game or if it sinks a ship
                if (isSunk(x, y, gridWithShips[Math.abs(p%2-1)].ships)) {
                    if (areAllSunk(gridWithShips[Math.abs(p%2-1)].ships)) {
                        System.out.printf("You sank the last ship. You won. Congratulations!%n");
                        break;
                    } else {
                        System.out.printf("You sank a ship!%n");
                    }
                } else System.out.printf("You hit a ship!%n");
            } else {
                gridWithShips[Math.abs(p%2-1)].grid[y][x] = "M";
                gridWithFog[p%2].grid[y][x] = "M";
                System.out.printf("You missed.%n");
            }
            p++; // moves to next player
            promptEnterKey();
        }
    }

    /*
    Checks if given shot sinks a ship.
    Shot removes given cell from list of points given ship occupies (stored in ArrayList position).
    If after this operation list is empty, it means all point have been shot and therefore ship is sunk.
    */
    public static boolean isSunk(int x, int y, Ship[] ships) {
        for (Ship s : ships) {
            if (s.position.contains(y * 10 + x)) {
                s.position.remove((Integer) (y * 10 + x));
                if (s.position.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    // checks if all ships are sunk and therefore game is finished
    public static boolean areAllSunk(Ship[] ships) {
        for (Ship s : ships) {
            if (!s.position.isEmpty()) return false;
        }
        return true;
    }

    public static void printGrids(int p) {
        printGrid(gridWithFog[p%2].grid);
        System.out.println("---------------------");
        printGrid(gridWithShips[p%2].grid);
    }
}
