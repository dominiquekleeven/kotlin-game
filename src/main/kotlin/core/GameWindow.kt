package core


import org.apache.logging.log4j.LogManager
import org.joml.Matrix4f
import org.lwjgl.glfw.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil


class GameWindow(val title: String, private val vSync: Boolean, var width: Int, var height: Int) {

    private val logger = LogManager.getLogger(GameWindow::class.java)
    private var window: Long? = null
    private var projectionMatrix = Matrix4f()

    companion object {
        const val FOV = 60.0f
        const val Z_FAR = 1000.0f
        const val Z_NEAR = 0.01f
    }

    // Initialize the game window
     init {
        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit()) {
            logger.error("Unable to initialize GLFW")
            throw IllegalStateException("Unable to initialize GLFW")
        }
        logger.info("GLFW initialized")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        window = glfwCreateWindow(width, height, title, 0, 0)

        if (window == 0L)  {
            logger.error("Failed to create window")
            throw IllegalStateException("Failed to create window")
        }


        glfwSetErrorCallback { errorCode: Int, msgPtr: Long ->
            logger.error(
                "Error code [{}], msg [{}]",
                errorCode,
                MemoryUtil.memUTF8(msgPtr)
            )
        }

        glfwMakeContextCurrent(window!!)

        if (vSync)
        {
            glfwSwapInterval(1)
        } else {
            glfwSwapInterval(0)
        }

        glfwShowWindow(window!!)

        val arrWidth = IntArray(1)
        val arrHeight = IntArray(1)
        glfwGetFramebufferSize(window!!, arrWidth, arrHeight)
        width = arrWidth[0]
        height = arrHeight[0]
    }

    // Main game loop
    fun update() {
        glfwSwapBuffers(window!!)
        glfwPollEvents()
    }

    // Check if a key is pressed
    fun isKeyPressed(keyCode: Int): Boolean {
        return glfwGetKey(window!!, keyCode) == GLFW_PRESS
    }

    // Check if the window should close
    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(window!!)
    }

    // Get the projection matrix
    fun getProjectionMatrix(): Matrix4f {
        return projectionMatrix
    }

    // Set the clear color
    fun setClearColor(r: Float, g: Float, b: Float, alpha: Float) {
        glClearColor(r, g, b, alpha)
    }

    // Update the projection matrix (default method)
    fun updateProjectionMatrix() {
        val aspectRatio = width.toFloat() / height.toFloat()
        projectionMatrix = Matrix4f().perspective(FOV, aspectRatio, 0.01f, 1000.0f)
    }


    // Update the projection matrix (with custom matrix)
    fun updateProjectionMatrix(matrix: Matrix4f, width: Int, height: Int) {
        val aspectRatio = width.toFloat() / height.toFloat()
        projectionMatrix = matrix.perspective(FOV, aspectRatio, 0.01f, 1000.0f)
    }

    // Update the window title
    fun updateWindowTitle(title: String) {
        glfwSetWindowTitle(window!!, title)
    }

    // Clean up the game window
    fun cleanup() {
        logger.info("Cleaning up")
        glfwFreeCallbacks(window!!)
        glfwDestroyWindow(window!!)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }




}