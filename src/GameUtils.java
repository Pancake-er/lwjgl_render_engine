import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.time.Instant;

import org.lwjgl.system.MemoryStack;

public class GameUtils {
    static double lastEpoch;
    static int frames = 0;
    static int fps;

	/* Utility class with things like a function to get cursor position and fps. */

    public static float getCursorPosX(long window) {
		try (MemoryStack stack = stackPush()) {
			DoubleBuffer posX = stack.mallocDouble(1);

			glfwGetCursorPos(window, posX, null);

			return (float)posX.get(0);
		}
	}
	public static float getCursorPosY(long window) {
		try (MemoryStack stack = stackPush()) {
			DoubleBuffer posY = stack.mallocDouble(1);

			glfwGetCursorPos(window, null, posY);

			return (float)posY.get(0);
		}
	}
	public static int getWindowWidth(long window) {
		try (MemoryStack stack = stackPush()) {
			IntBuffer width = stack.mallocInt(1);

			glfwGetWindowSize(window, width, null);

			return width.get(0);
		}
	}
	public static int getWindowHeight(long window) {
		try (MemoryStack stack = stackPush()) {
			IntBuffer height = stack.mallocInt(1);

			glfwGetWindowSize(window, null, height);

			return height.get(0);
		}
	}
    public static int figureOutFPS() {
        /* Figures out frames per second in the while loop. IN REALITY NOT A
		VERY GOOD BENCHMARK! */
        if (lastEpoch != Instant.now().getEpochSecond()) {
			lastEpoch = Instant.now().getEpochSecond();
            fps = frames;
            frames = 1;
		}
		else {
			frames++;
		}
        return fps;
    }
}
