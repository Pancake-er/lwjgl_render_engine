import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL41C.*;
import static org.lwjgl.system.MemoryUtil.*;

public class App {
	private long window;
	// The version of the game.
	private float version = 0.1f;

	Renderer renderer;

	public static void main(String[] args) {
		App app = new App();
		app.run();
		app.cleanup();
	}
	public void run() {
		if (!glfwInit()) {
            throw new RuntimeException("Unable to initialize GLFW");
        }

		// Configures some stuff for GLFW.
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(1920, 1080, "Game " + version + " (OpenGL)",
			NULL, NULL);
		if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

		// Specifies which window we're working with.                                                                 
		glfwMakeContextCurrent(window);

		// Enables v-sync.
		glfwSwapInterval(1);

		glfwShowWindow(window);

		GL.createCapabilities();

		glClearColor(0f, 0f, 0f, 0f);

		// Needed for textures to work.
        glEnable(GL_TEXTURE_2D);
		// Needed for textures to have transparency.
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		renderer = new Renderer(GameUtils.getWindowWidth(window),
			GameUtils.getWindowHeight(window));

		Texture texture = new Texture("./res/random/dog.jpg");
		Texture texture1 = new Texture("./res/random/testAnimation.png");

		Animation animation = new Animation(texture1, 128f, 128f, 128f, 128f,
			0f, 0f, (1f/6f), (1f/6f), 6, 6);

		// Main loop.
		while (!glfwWindowShouldClose(window)) {
			//System.out.println("fps: " + GameUtils.figureOutFPS());
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			animation.addFrameToRenderer(renderer);
			animation.update();
		
			renderer.addQuad(0f, 0f, 128f, 128f, 0f, 0f, 1f, 1f, texture);

			renderer.flush();

			// Swaps the color buffers.
			glfwSwapBuffers(window);

			/* Polls for window events. Things like key callbacks will only be
			invoked during this call. */
			glfwPollEvents();
		}
	}
	private void cleanup() {
		renderer.freeBufferMemory();
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
	}
}