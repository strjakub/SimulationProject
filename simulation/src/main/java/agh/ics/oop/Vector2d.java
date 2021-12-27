package agh.ics.oop;

import java.util.Objects;

public record Vector2d(int x, int y) {

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        return (((Vector2d) other).x == this.x) && (((Vector2d) other).y == this.y);
    }

    public boolean precedes(Vector2d other) {
        return other.x >= this.x && other.y >= this.y;
    }

    public boolean follows(Vector2d other) {
        return other.x <= this.x && other.y <= this.y;
    }

    public Vector2d upperRight(Vector2d other) {
        int x_index = this.x;
        int y_index = this.y;

        if (other.x > x_index)
            x_index = other.x;
        if (other.y > y_index)
            y_index = other.y;

        return new Vector2d(x_index, y_index);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int x_index = this.x;
        int y_index = this.y;

        if (other.x < x_index)
            x_index = other.x;
        if (other.y < y_index)
            y_index = other.y;

        return new Vector2d(x_index, y_index);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }
}
