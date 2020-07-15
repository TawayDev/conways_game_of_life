package com.itsJeras;
import java.util.concurrent.ThreadLocalRandom;
public class Main {

    public static void main(String[] args) {
        data.constant = 0;
        int rand = random(0,10);
        if (rand < 6) {
            genRanShapes();
            System.out.println("Generating random cell shapes.");
        } else {
            genClusters();
            System.out.println("Generating cell clusters.");
        }
        render();
        data.run = true;
        while (data.run) {
        //for (int i = 0; i < 10; i++) {
            applyMask();
            render();
            ifConstant();
        }
    }
    // Checks if the number of cells changes and if it does not in 3 generations it stops the process.
    public static void ifConstant() {
        if (data.constant == 3) {
            data.constant = 0;
        }
        int constant = data.constant;
        data.intArray[constant] = data.cellNumCurrent;
        if (data.intArray[0] + data.intArray[1] + data.intArray[2] != 0) {
            if (data.intArray[0] == data.intArray[1] && data.intArray[1] == data.intArray[2]) {
                data.run = false;
                System.out.println("Number of alive cells is constant. Simulation has been stopped.");
            }
        }
        if (data.cellNumCurrent == 0) {
            data.run = false;
            System.out.println("Population died out. Simulation has been stopped.");
        }
        //System.out.println(data.intArray[0] + ", " + data.intArray[1] + ", " + data.intArray[2]);
        data.constant += 1;
    }

    // Applies rules of the game aka the "mask":
    public static void applyMask() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int AON = 0; // AON == Amount of neighbours.
                int k = i;
                int l = j;
                // count neighbours:
                if ( -1 < k + 1 && - 1 < l && k + 1 < 8 && l < 8) { if (map.Map[k + 1][l].equals("X")) { AON += 1; } }
                if ( -1 < k && - 1 < l + 1 && k < 8 && l + 1 < 8) { if (map.Map[k][l + 1].equals("X")) { AON += 1; } }
                if ( -1 < k - 1 && - 1 < l && k - 1 < 8 && l < 8) { if (map.Map[k - 1][l].equals("X")) { AON += 1; } }
                if ( -1 < k && - 1 < l - 1 && k < 8 && l - 1 < 8) { if (map.Map[k][l - 1].equals("X")) { AON += 1; } }
                if ( -1 < k - 1 && - 1 < l + 1 && k - 1 < 8 && l + 1 < 8) { if (map.Map[k - 1][l + 1].equals("X")) { AON += 1; } }
                if ( -1 < k + 1 && - 1 < l - 1 && k + 1 < 8 && l - 1 < 8) { if (map.Map[k + 1][l - 1].equals("X")) { AON += 1; } }
                if ( -1 < k + 1 && - 1 < l + 1 && k + 1 < 8 && l + 1 < 8) { if (map.Map[k + 1][l + 1].equals("X")) { AON += 1; } }
                if ( -1 < k - 1 && - 1 < l - 1 && k - 1 < 8 && l - 1 < 8) { if (map.Map[k - 1][l - 1].equals("X")) { AON += 1; } }

                // apply rules:
                if (map.Map[i][j].equals("X")) {
                    //Rule #1: Any live cell with fewer than two live neighbours dies, as if by underpopulation.
                    if (AON < 2) {
                        map.Map[i][j] = " ";
                        data.cellNumDied += 1;
                    }
                    //Rule #2: Any live cell with two or three live neighbours lives on to the next generation.
                    if (AON > 1 && AON < 4) {
                        map.Map[i][j] = "X";
                    }
                    //Rule #3: Any live cell with more than three live neighbours dies, as if by overpopulation.
                    if (AON > 3) {
                        map.Map[i][j] = " ";
                        data.cellNumDied += 1;
                    }
                } else {
                    //Rule #4: Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
                    if (AON == 3) {
                        map.Map[i][j] = "X";
                        data.cellNumBorn += 1;
                    }
                }
            }
        }
    }

    // Checks for survivors and displays gathered data:
    public static void displayData () {
        data.cellNumDied = 0;
        data.cellNumCurrent = 0;
        data.cellNumBorn = 0;
        // check every cell. if equal to X then add +1 to survivor score
        // Count data.cellNumCurrent
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (map.Map[i][j].equals("X")) {
                    data.cellNumCurrent += 1;
                }
            }
        }
        //display data:
        System.out.println("Generation: " + data.generation);
        System.out.println("Start amount of cells: " + data.cellNumStart);
        System.out.println("Current amount of cells: " + data.cellNumCurrent);
        System.out.println("Amount of born cells: " + data.cellNumBorn);
        System.out.println("Amount of died cells: " + data.cellNumDied);
        //save data:
        data.cellNumPrevious = data.cellNumCurrent;
        data.cellAmount = data.cellNumCurrent;
        data.generation += 1;
    }

    // Renders stuff on screen:
    public static void render () {
        displayData();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(map.Map[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("//////////////////////////////");
    }

    // Generate random number in a interval:
    public static int random (int min, int max) {
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }
    // Will spawn cells in clusters.
    public static void genClusters () {
        int m = random(5,30);
        // generate a random position:
        int row = random(0, 7);
        int height = random(0, 7);
        // Place spot on grid:
        map.Map[row][height] = "X";
        int k = row;
        int l = height;
        for (int i = 0; i < m; i++) {
            int rand = random(5,45);
            if (rand < 6 ) { if ( -1 < k + 1 && - 1 < l && k + 1 < 8 && l < 8) { if (map.Map[k + 1][l].equals(" ")) { map.Map[k + 1][l] = "X"; k = k + 1;} } }
            if (rand > 5 && rand < 11 ) { if ( -1 < k && - 1 < l + 1 && k < 8 && l + 1 < 8) { if (map.Map[k][l + 1].equals(" ")) { map.Map[k][l + 1] = "X"; l = l + 1;} } }
            if (rand > 10 && rand < 21 ) { if ( -1 < k - 1 && - 1 < l && k - 1 < 8 && l < 8) { if (map.Map[k - 1][l].equals(" ")) { map.Map[k - 1][l] = "X"; k = k - 1;} } }
            if (rand > 20 && rand < 26 ) { if ( -1 < k && - 1 < l - 1 && k < 8 && l - 1 < 8) { if (map.Map[k][l - 1].equals(" ")) { map.Map[k][l - 1] = "X"; l = l - 1;} } }
            if (rand > 25 && rand < 31 ) { if ( -1 < k - 1 && - 1 < l + 1 && k - 1 < 8 && l + 1 < 8) { if (map.Map[k - 1][l + 1].equals(" ")) { map.Map[k - 1][l + 1] = "X"; k = k - 1; l = l + 1;} } }
            if (rand > 30 && rand < 36 ) { if ( -1 < k + 1 && - 1 < l - 1 && k + 1 < 8 && l - 1 < 8) { if (map.Map[k + 1][l - 1].equals(" ")) { map.Map[k + 1][l - 1] = "X"; k = k + 1; l = l - 1;} } }
            if (rand > 35 && rand < 41 ) { if ( -1 < k + 1 && - 1 < l + 1 && k + 1 < 8 && l + 1 < 8) { if (map.Map[k + 1][l + 1].equals(" ")) { map.Map[k + 1][l + 1] = "X"; k = k + 1; l = l + 1;} } }
            if (rand > 40 && rand < 46) { if ( -1 < k - 1 && - 1 < l - 1 && k - 1 < 8 && l - 1 < 8) { if (map.Map[k - 1][l - 1].equals(" ")) { map.Map[k - 1][l - 1] = "X"; k = k - 1; l = l - 1;} } }
        }
        data.generation = 0;
        data.cellNumStart = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (map.Map[i][j].equals("X")) {
                    data.cellNumStart += 1;
                }
            }
        }
        data.cellNumPrevious = data.cellNumStart;
    }

    // Will generate random shapes on screen:
    public static void genRanShapes () {
        int k = random(5,20);
        for (int i = 0; i < k; i++) {
            // generate a random position:
            int row = random(0, 7);
            int height = random(0, 7);
            // Place spot on grid:
            map.Map[row][height] = "X";
        }
        data.generation = 0;
        data.cellNumStart = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (map.Map[i][j].equals("X")) {
                    data.cellNumStart += 1;
                }
            }
        }
        data.cellNumPrevious = data.cellNumStart;
    }

}

class map {
    public static String[][] Map = {
            // 8x8 grid
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
    };
}

class data {
    public static boolean run;
    public static int constant;
    public static int generation;
    public static int cellNumStart;
    public static int cellNumPrevious;
    public static int cellNumCurrent;
    public static int cellNumDied;
    public static int cellNumBorn;
    public static int cellAmount;
    public static int[] intArray = new int[3];
}
