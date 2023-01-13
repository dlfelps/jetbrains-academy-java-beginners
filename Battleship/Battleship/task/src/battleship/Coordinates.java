package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Math.abs;

record Coordinates(int col, int row) {
    public static Coordinates fromString(String s) throws IllegalArgumentException{

        var row = switch(s.toUpperCase().charAt(0)) {
            case 'A' -> 1;
            case 'B' -> 2;
            case 'C' -> 3;
            case 'D' -> 4;
            case 'E' -> 5;
            case 'F' -> 6;
            case 'G' -> 7;
            case 'H' -> 8;
            case 'I' -> 9;
            case 'J' -> 10;
            default -> throw new IllegalArgumentException("Error! You entered the wrong coordinates! Try again:");
        };

        int col;
        if (s.length() == 2){ //1-9
            col = Integer.parseInt(s.substring(1,2));
        } else { //10
            col = Integer.parseInt(s.substring(1,3));
        }
        if (col > 10){
            throw new IllegalArgumentException("Error: Wrong ship location! Try again:");
        }
        return new Coordinates(col, row);
    }

    public int distanceTo(Coordinates other) throws IllegalArgumentException{
        int distance;

        if (this.row == other.row) {
            distance = abs(this.col - other.col) + 1;
        } else if (this.col == other.col) {
            distance = abs(this.row - other.row) + 1;
        } else {
            throw new IllegalArgumentException("Error! Not a valid set of coordinates!");
        }
        return distance;
    }

    public List<Coordinates> spans(Coordinates other) throws IllegalArgumentException{

        List<Coordinates> coords = new ArrayList<>();


        if (this.row == other.row) {
            var low = (this.col < other.col) ? this.col : other.col;
            var high = (this.col < other.col) ? other.col : this.col;

            for (int i = low; i <= high; i++) {
                coords.add(new Coordinates(i, this.row));
            }
        } else if (this.col == other.col) {
            var low = (this.row < other.row) ? this.row : other.row;
            var high = (this.row < other.row) ? other.row : this.row;

            for (int i = low; i <= high; i++) {
                coords.add(new Coordinates(this.col, i));
            }
        } else {
            throw new IllegalArgumentException("Error! Not a valid set of coordinates!");
        }
        return coords;
    }

    public Stream<Coordinates> surroundingPoints() {
        return List.of(new Coordinates(this.col, this.row),
                new Coordinates(this.col-1, this.row),
                new Coordinates(this.col+1, this.row),
                new Coordinates(this.col, this.row-1),
                new Coordinates(this.col, this.row+1)
                ).stream();
    }
}