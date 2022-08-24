import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Matrix4 {
    float m00;
    float m01;
    float m02;
    float m03;
    float m10;
    float m11;
    float m12;
    float m13;
    float m20;
    float m21;
    float m22;
    float m23;
    float m30;
    float m31;
    float m32;
    float m33;

    /* 4x4 matrix of floats. This matrix is in column major order. It looks like
    this:
        m00  m10  m20  m30
        m01  m11  m21  m31
        m02  m12  m22  m32
        m03  m13  m23  m33 */
        
    Matrix4(float m00, float m01, float m02, float m03, float m10, float m11,
        float m12, float m13, float m20, float m21, float m22, float m23, 
        float m30, float m31, float m32, float m33) 
    {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }
    // Constructor for unit matrix.
    Matrix4() {
        m00 = 1f;
        m01 = 0f;
        m02 = 0f;
        m03 = 0f;
        m10 = 0f;
        m11 = 1f;
        m12 = 0f;
        m13 = 0f;
        m20 = 0f;
        m21 = 0f;
        m22 = 1f;
        m23 = 0f;
        m30 = 0f;
        m31 = 0f;
        m32 = 0f;
        m33 = 1f;
    }
    public Matrix4 orthographic(float left, float right, float bottom, 
        float top, float near, float far) 
    {
        m00 = 2f / (right - left);
        m11 = 2f / (top - bottom);
        m22 = 2f / (far - near);
        m30 = -(right + left) / (right - left);
        m31 = -(top + bottom) / (top - bottom);
        m32 = -(far + near) / (far - near);
    
        return this;
    }
    public Matrix4 scale(float x, float y, float z) {
        m00 *= x;
        m11 *= y;
        m22 *= z;

        return this;
    }
    public Matrix4 scale(float scalar) {
        m00 *= scalar;
        m11 *= scalar;
        m22 *= scalar;

        return this;
    }
    public Matrix4 setScale(float x, float y, float z) {
        m00 = x;
        m11 = y;
        m22 = z;

        return this;
    }
    public Matrix4 translate(float x, float y, float z) {
        m30 += x;
        m31 += y;
        m32 += z;

        return this;
    }
    public Matrix4 setPosition(float x, float y, float z) {
        m30 = x; 
        m31 = y; 
        m32 = z;

        return this;
    }
    public float getXPosition() {
        return m30;
    }
    public float getYPosition() {
        return m31;
    }
    public float getZPosition() {
        return m32;
    }
    public void set(Matrix4 otherMatrix) {
        this.m00 = otherMatrix.m00;
        this.m01 = otherMatrix.m01;
        this.m02 = otherMatrix.m02;
        this.m03 = otherMatrix.m03;
        this.m10 = otherMatrix.m10;
        this.m11 = otherMatrix.m11;
        this.m12 = otherMatrix.m12;
        this.m13 = otherMatrix.m13;
        this.m20 = otherMatrix.m20;
        this.m21 = otherMatrix.m21;
        this.m22 = otherMatrix.m22;
        this.m23 = otherMatrix.m23;
        this.m30 = otherMatrix.m30;
        this.m31 = otherMatrix.m31;
        this.m32 = otherMatrix.m32;
        this.m33 = otherMatrix.m33;
    }
    /* Returns an array in COLUMN MAJOR ORDER. This means you put the columns in
    row after row like it is done below, it is not a typo. */
    public float[] toFloatArray() {
        return new float[] {
            m00,  m01,  m02,  m03,
            m10,  m11,  m12,  m13,
            m20,  m21,  m22,  m23,
            m30,  m31,  m32,  m33,
        };
    }
    public FloatBuffer toFloatBuffer() {
        try (MemoryStack stack = stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4 * 4);
            
            buffer.put(m00);
            buffer.put(m01);
            buffer.put(m02);
            buffer.put(m03);
            buffer.put(m10);
            buffer.put(m11);
            buffer.put(m12);
            buffer.put(m13);
            buffer.put(m20);
            buffer.put(m21);
            buffer.put(m22);
            buffer.put(m23);
            buffer.put(m30);
            buffer.put(m31);
            buffer.put(m32);
            buffer.put(m33);

            buffer.flip();

			return buffer;
		}
    }
    public String toString() {
        return m00 + ", " + m10 + ", " + m20 + ", " + m30 + ",\n" 
            + m01 + ", " + m11 + ", " + m21 + ", " + m31 + ",\n"
            + m02 + ", " + m12 + ", " + m22 + ", " + m32 + ",\n"
            + m03 + ", " + m13 + ", " + m23 + ", " + m33 + ",";
    }
}

// Info on matrices here: http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
// And general + projection stuff here: https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/building-basic-perspective-projection-matrix