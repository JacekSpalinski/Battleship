import java.io.IOException;
import java.util.Scanner;

// gird where players place their ships
public class GridWithShips {

    static Scanner scanner = new Scanner(System.in);
    static final int N = 10; // size of a battlefield
    static final int LC = 65; // used for letter char to convert into meaningful numbers according to ASCII
    static final int NC = 49; // used to number char to convert into meaningful numbers according to ASCII
    String[][] grid;
    Ship[] ships;

    GridWithShips(int player) {
        grid = new String[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = "~";
            }
        }
        ships = new Ship[5];
        ships[0] = new Ship("Aircraft Carrier", 5);
        ships[1] = new Ship("Battleship", 4);
        ships[2] = new Ship("Submarine", 3);
        ships[3] = new Ship("Cruiser", 3);
        ships[4] = new Ship("Destroyer", 2);

        System.out.printf("\nPlayer %d, place your ships on the game field\n\n", player);
        printGrid(grid);
        placeAllShips(grid, ships);
        promptEnterKey();
    }

    public GridWithShips() {
    }

    public static void placeAllShips(String[][] grid, Ship[] ships) {
        for (Ship ship : ships) {
            System.out.printf("%nEnter the coordinates of the %s (%d cells):%n%n> ", ship.name, ship.size);
            String cor1, cor2; // used to store coordinates provided by the player
            cor1 = scanner.next();
            cor2 = scanner.next();
            checkInput(grid, ship, cor1, cor2);
            System.out.println();
            printGrid(grid);
        }
    }

    // checks if the input is valid (= respects the rules of placing the ships on the grid)
    public static void checkInput(String[][] grid, Ship ship, String cor1, String cor2) {

        int x1, x2, y1, y2;
        String start, end;

        // the loop will run until player provides valid input
        while (true) {

            // determine which point is a start and which is an end of a ship
            if (comparePoints(cor1, cor2)) {
                start = cor1;
                end = cor2;
            } else {
                start = cor2;
                end = cor1;
            }

            // convert input into number coordinates
            y1 = Character.toUpperCase(start.charAt(0)) - LC; // accepts both upper- and lowercase input
            x1 = start.charAt(1) - NC;
            y2 = Character.toUpperCase(end.charAt(0)) - LC; // accepts both upper- and lowercase input
            x2 = end.charAt(1) - NC;
            // accounts for a case when input contains "10" as 2nd coordinate of a point
            if (start.length() == 3) {
                if (start.charAt(2) - NC == -1) {
                    x1 = 9;
                } else {
                    x1 = 10; // arbitrarily chosen value, used in condition below to determine wrong input
                }
            }
            if (end.length() == 3) {
                if (end.charAt(2) - NC == -1) {
                    x2 = 9;
                } else {
                    x2 = 10; // arbitrarily chosen value, used in condition below to determine wrong input
                }
            }

            // check if coordinates are valid i.e. between 0 and 9
            if (y1 < 0 || y1 > 9 || x1 < 0 || x1 > 9 || y2 < 0 || y2 > 9 || x2 < 0 || x2 > 9) {
                System.out.printf("Error! You entered the wrong coordinates! Try again:%n%n> ");
                cor1 = scanner.next();
                cor2 = scanner.next();
            }
            // check length of ship
            else if ((Math.abs(x1 - x2) != ship.size - 1) && (Math.abs(y1 - y2) != ship.size - 1)) {
                System.out.printf("%nError! Wrong length of the Submarine! Try again:%n%n> ");
                cor1 = scanner.next();
                cor2 = scanner.next();
            }
            // check if it is horizontal or vertical
            else if (x1 != x2 && y1 != y2) {
                System.out.printf("%nError! Wrong ship location! Try again:%n%n> ");
                cor1 = scanner.next();
                cor2 = scanner.next();
            }
            // check if it does not touch or cross other ships
            else if (checkNearbyShips(grid, x1, x2, y1, y2)) {
                System.out.printf("%nError! You placed it too close to another one. Try again:%n%n> ");
                cor1 = scanner.next();
                cor2 = scanner.next();
            } else break;
        }
        placeShip(grid, x1, x2, y1, y2, ship);
    }

    /*
    Checks if point cor1 is starting point (= is smaller than cor2).
    We know in correct input either x or y coordinate must be equal,
    so it is sufficient to check sums of coordinates of 2 point.
    */
    public static boolean comparePoints(String cor1, String cor2) {
        int y1 = cor1.charAt(0) - LC;
        int x1 = cor1.charAt(1) - NC;
        int y2 = cor2.charAt(0) - LC;
        int x2 = cor2.charAt(1) - NC;
        if (cor1.length() == 3) {
            x1 = 9;
        }
        if (cor2.length() == 3) {
            x2 = 9;
        }
        return x1 + y1 <= x2 + y2;
    }

    // for every cell of a given ship checks, if all nearby cell (or this cell itself) is another ship
    public static boolean checkNearbyShips(String[][] grid, int x1, int x2, int y1, int y2) {
        for (int row = y1; row <= y2; row++) {
            for (int col = x1; col <= x2; col++) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if ((row + i) < 0 || (row + i) > 9 || (col + j) < 0 || (col + j) > 9) {
                            continue;
                        }
                        if (grid[row + i][col + j].equals("O")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // places a ship on a grid and adds those points to position of a given ship
    public static void placeShip(String[][] grid, int x1, int x2, int y1, int y2, Ship ship) {
        for (int i = y1; i <= y2; i++) {
            for (int j = x1; j <= x2; j++) {
                grid[i][j] = "O";
                ship.position.add(i * 10 + j);
            }
        }
    }

    public static void printGrid(String[][] grid) {
        System.out.print(" ");
        for (int i = 1; i <= N; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.print((char) (i + LC));
            for (int j = 0; j < N; j++) {
                System.out.print(" " + grid[i][j]);
            }
            System.out.println();
        }
    }

    public static void promptEnterKey() {
        System.out.println("\nPress Enter and pass the move to another player\n...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
