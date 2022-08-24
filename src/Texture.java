import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.opengl.ARBBindlessTexture;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryStack.stackPush;

import static org.lwjgl.opengl.GL41.*;

public class Texture {
    private IntBuffer width;
    private IntBuffer height;
    private IntBuffer components;
    private ByteBuffer data;
    public int id;
    public long bindlessHandle;

    /* Class to create an OpenGL texture. */

    Texture(String path) {
        Path pathTest = Paths.get(path);
        if (Files.notExists(pathTest)) {
            throw new RuntimeException("Not a valid path for a texture.");
        }
        try (MemoryStack stack = stackPush()) {
            width = stack.mallocInt(1);
            height = stack.mallocInt(1);
            components = stack.mallocInt(1);

            // Decodes an image into a byte buffer.
            data = STBImage.stbi_load(path, width, height, components, 4);

            // Creates and binds new OpenGL texture.
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            /* Configures the binded texture, GL_NEAREST results in a more 
            crisp/clear texture. */
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            // Uploads texture data.
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(),
                    0, GL_RGBA, GL_UNSIGNED_BYTE, data);

            bindlessHandle = ARBBindlessTexture.glGetTextureHandleARB(id); 
            ARBBindlessTexture.glMakeTextureHandleResidentARB(bindlessHandle);  

            STBImage.stbi_image_free(data);
        }
    }
}

// Info on textures here: https://math.hws.edu/graphicsbook/c6/s4.html 
// And here: https://community.khronos.org/t/why-use-or-define-texture-units-and-what-is-the-mapping-with-uniforms-and-texture-units/106265.