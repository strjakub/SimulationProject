package agh.ics.oop;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public MapDirection next1(){
        return switch(this){
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public MapDirection next2(){
        return switch(this){
            case NORTH -> EAST;
            case NORTHEAST -> SOUTHEAST;
            case EAST -> SOUTH;
            case SOUTHEAST -> SOUTHWEST;
            case SOUTH -> WEST;
            case SOUTHWEST -> NORTHWEST;
            case WEST -> NORTH;
            case NORTHWEST -> NORTHEAST;
        };
    }

    public MapDirection next3(){
        return switch(this){
            case NORTH -> SOUTHEAST;
            case NORTHEAST -> SOUTH;
            case EAST -> SOUTHWEST;
            case SOUTHEAST -> WEST;
            case SOUTH -> NORTHWEST;
            case SOUTHWEST -> NORTH;
            case WEST -> NORTHEAST;
            case NORTHWEST -> EAST;
        };
    }

    public MapDirection next5(){
        return switch(this){
            case NORTH -> SOUTHWEST;
            case NORTHEAST -> WEST;
            case EAST -> NORTHWEST;
            case SOUTHEAST -> NORTH;
            case SOUTH -> NORTHEAST;
            case SOUTHWEST -> EAST;
            case WEST -> SOUTHEAST;
            case NORTHWEST -> SOUTH;
        };
    }

    public MapDirection next6(){
        return switch(this){
            case NORTH -> WEST;
            case NORTHEAST -> NORTHWEST;
            case EAST -> NORTH;
            case SOUTHEAST -> NORTHEAST;
            case SOUTH -> EAST;
            case SOUTHWEST -> SOUTHEAST;
            case WEST ->SOUTH;
            case NORTHWEST -> SOUTHWEST;
        };
    }

    public MapDirection next7(){
        return switch(this){
            case NORTH -> NORTHWEST;
            case NORTHEAST -> NORTH;
            case EAST -> NORTHEAST;
            case SOUTHEAST -> EAST;
            case SOUTH -> SOUTHEAST;
            case SOUTHWEST -> SOUTH;
            case WEST -> SOUTHWEST;
            case NORTHWEST -> WEST;
        };
    }

    public Vector2d toUnitVector(){
        return switch(this){
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
        };
    }

    public MapDirection roll(){
        Random generator = new Random();
        int x = generator.nextInt(8);

        return switch(x){
            case 1 -> NORTHWEST;
            case 2 -> NORTH;
            case 3 -> NORTHEAST;
            case 4 -> EAST;
            case 5 -> SOUTHEAST;
            case 6 -> SOUTH;
            case 7 -> SOUTHWEST;
            default -> WEST;
        };
    }
}
