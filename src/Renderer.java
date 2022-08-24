import static org.lwjgl.opengl.GL41C.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.ARBBindlessTexture;

//import static org.lwjgl.system.MemoryStack.stackPush;

//import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Renderer {
    private int vertexId;
    private int indexId;
    private int vaoId;
    private final int maxQuadCount = 1000;
    private final int maxIndexCount = maxQuadCount * 6;
    private final int verticesBufferSize = (maxQuadCount * 4) * (5 * 4 + 8);
    private ByteBuffer verticesBuffer = MemoryUtil.memAlloc(verticesBufferSize);
    private Shader shader = new Shader("./shaders/VertexShader.glsl",
        "./shaders/FragmentShader.glsl");

    /* A batch renderer which uses bindless textures. */

    Renderer(int windowWidth, int windowHeight) {
        makeAndUseVAO();

		shader.use();

        // Creates and uses a new BO for vertices.
        vertexId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexId);

        // Uploads data to the BO.
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);

        /* Sets up (the location and data format of) the vertex attributes 
        for the VAO (made in App.java) to keep. A float is 4 bytes, so we
        multiply the float amounts by 4 because that is what the API wants. */
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4 + 8, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4 + 8, 3 * 4);
        glEnableVertexAttribArray(2);
        glVertexAttribLPointer(2, 1, ARBBindlessTexture.GL_UNSIGNED_INT64_ARB, 5
            * 4 + 8, 5 * 4);

        /* Index buffer. Indices are always ints. They are used to 
        optimize/lower the VBO's memory usage. */
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(maxIndexCount);
        int indexOffset = 0;
        for (int i = 0; i < maxIndexCount; i += 6) {
            indicesBuffer.put(0 + indexOffset);
            indicesBuffer.put(1 + indexOffset);
            indicesBuffer.put(2 + indexOffset);
            indicesBuffer.put(2 + indexOffset);
            indicesBuffer.put(3 + indexOffset);
            indicesBuffer.put(0 + indexOffset);
            
            indexOffset += 4;
        }
        indicesBuffer.flip();
        indexId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(indicesBuffer);

        // Unbinds the VBOs used (for saftey). f
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        Matrix4 projection = new Matrix4();
        projection.orthographic(0f, windowWidth, windowHeight, 0f, 0f, 10f);
        /* The first parameter should be the location set for the uniform in the
        shader. */
        glUniformMatrix4fv(0, false, projection.toFloatBuffer());
    }
    public void makeAndUseVAO() {
        // A VAO is used so you only set up vertex attributes once for the VBO.
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
    }
    public void addQuad(float xPosition, float yPosition, float width,
        float height, float xTextureCoord, float yTextureCoord, 
        float textureWidth, float textureHeight, Texture texture) 
    {
            
        if (verticesBuffer.position() == verticesBuffer.capacity()) {
            flush();
        }

        long textureIndex = texture.bindlessHandle;

        addVertex(xPosition, yPosition, xTextureCoord, yTextureCoord,
            textureIndex);

        addVertex(xPosition + width, yPosition, xTextureCoord + textureWidth,
            yTextureCoord, textureIndex);

        addVertex(xPosition + width, yPosition + height, xTextureCoord 
            + textureWidth, yTextureCoord + textureHeight, textureIndex);

        addVertex(xPosition, yPosition + height, xTextureCoord,
            yTextureCoord + textureHeight, textureIndex);
    }
    public void addVertex(float xPosition, float yPosition, float xTextureCoord, 
        float yTextureCoord, long textureIndex) 
    {
        //xyz uv i

        verticesBuffer.putFloat(xPosition);
        verticesBuffer.putFloat(yPosition);
        verticesBuffer.putFloat(0f);
        verticesBuffer.putFloat(xTextureCoord);
        verticesBuffer.putFloat(yTextureCoord);
        verticesBuffer.putLong(textureIndex);
    }
    public void flush() {
        glBindBuffer(GL_ARRAY_BUFFER, vertexId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexId);

        verticesBuffer.flip();
        glBufferSubData(GL_ARRAY_BUFFER, 0, verticesBuffer);
        verticesBuffer.clear();

        glDrawElements(GL_TRIANGLES, maxIndexCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    public void freeBufferMemory() {
        MemoryUtil.memFree(verticesBuffer);
    }
}

// Info on VBOs here: http://www.songho.ca/opengl/gl_vbo.html
// Info on VAOs here: https://stackoverflow.com/questions/23314787/use-of-vertex-array-objects-and-vertex-buffer-objects
// And both + example here: https://gamedev.stackexchange.com/questions/8042/whats-the-purpose-of-opengls-vertex-array-objects
// Info on vertex attrib array offset specific here: https://stackoverflow.com/questions/16380005/opengl-3-4-glvertexattribpointer-stride-and-offset-miscalculatio