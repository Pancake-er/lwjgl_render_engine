public class Vector2 {
    public float x;
    public float y;

    /* Simple 2D vector math class. */

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2 add(Vector2 otherVector2) {
        x += otherVector2.x;
        y += otherVector2.y;

        return this;
    }
    public Vector2 add(float x, float y) {
        this.x += x;
        this.y += y;

        return this;
    }
    public Vector2 subtract(Vector2 otherVector2) {
        x -= otherVector2.x;
        y -= otherVector2.y;

        return this;
    }
    public Vector2 subtract(float x, float y) {
        this.x -= x;
        this.y -= y;

        return this;
    }
    public Vector2 multiply(Vector2 otherVector) {
        x *= otherVector.x;
        y *= otherVector.y;

        return this;
    }
    public Vector2 multiply(float scalar) {
        x *= scalar;
        y *= scalar;

        return this;
    }
    public Vector2 multiply(float x, float y) {
        this.x *= x;
        this.y *= y;

        return this;
    }
    public Vector2 divide(Vector2 otherVector) {
        x /= otherVector.x;
        y /= otherVector.y;

        return this;
    }
    public Vector2 divide(float scalar) {
        x /= scalar;
        y /= scalar;

        return this;
    }
    public Vector2 divide(float x, float y) {
        this.x /= x;
        this.y /= y;

        return this;
    }
    public void set(Vector2 otherVector2) {
        x = otherVector2.x;
        y = otherVector2.y;
    }
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
