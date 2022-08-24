import static org.lwjgl.opengl.GL41C.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Shader {
    private int program;
    private int vertexShader;
    private int fragmentShader;

    /* Shader takes the paths you give it and makes those into shaders (and
    enables them when you do .use()). */

    Shader(String vertexShaderPath, String fragmentShaderPath) {
        program = glCreateProgram();

        vertexShader = glCreateShader(GL_VERTEX_SHADER);

        // Takes the vertex shader path and compiles it into a vertex shader.
        glShaderSource(vertexShader, textFileToString(vertexShaderPath));
        glCompileShader(vertexShader);

        // Sets up an error output for the vertex shader.
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vertexShader));
            System.exit(1);
        }  

        // Fragment shader.
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShader, textFileToString(fragmentShaderPath));
        glCompileShader(fragmentShader);

        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fragmentShader));
            System.exit(1);
        }

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }

        glValidateProgram(program);
    }
    public void use() {
        glUseProgram(program);
    }
    private String textFileToString(String path) {
        Path shaderPath = Paths.get(path);
        if (Files.notExists(shaderPath)) {
            throw new RuntimeException("Not a valid path for a shader.");
        }
        try {
            String content = Files.readString(shaderPath);
            return content;
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}