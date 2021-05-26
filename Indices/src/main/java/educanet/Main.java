package educanet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;


public class Main {

    public static void main(String[] args) throws Exception {

        GLFW.glfwInit();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        long window = GLFW.glfwCreateWindow(800, 600, "Screensaver", 0, 0);

        if(window == 0) {
            GLFW.glfwTerminate();
            throw new Exception("jejda neco se pokazilo, LOL");
        }

        GLFW.glfwMakeContextCurrent(window);

        GL.createCapabilities();
        GL33.glViewport(0, 0, 800, 600);

        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            GL33.glViewport(0, 0, w, h);
        });

        Shaders.initShaders();

        Square ctverda = new Square(-1.00f, 0.5f, 0.3f);

        while (!GLFW.glfwWindowShouldClose(window)) {
            if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
                GLFW.glfwSetWindowShouldClose(window, true);

            GL33.glClearColor(0f, 0f, 0f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            ctverda.update(window);
            ctverda.render();

            GLFW.glfwSwapBuffers(window);

            GLFW.glfwPollEvents();
        }

        GLFW.glfwTerminate();
    }
}
