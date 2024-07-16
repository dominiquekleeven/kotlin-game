package io.labyrinth.core.graphics

import core.GameWindow
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class Renderer {
    private var sceneRenderer: SceneRenderer? = null
    private val logger = LogManager.getLogger(Renderer::class.java)

    init {
        logger.info("Initializing Renderer")

        // Initialize OpenGL
        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_STENCIL_TEST)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        sceneRenderer = SceneRenderer()
        logger.info("OpenGL version: ${glGetString(GL_VERSION)}")
    }

    fun render(gameWindow: GameWindow, scene: Scene)
    {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glViewport(0, 0, gameWindow.width, gameWindow.height)
        sceneRenderer?.render(scene)
    }

    fun cleanup()
    {
        logger.info("Cleaning up Renderer")
        sceneRenderer?.cleanup()
    }
}