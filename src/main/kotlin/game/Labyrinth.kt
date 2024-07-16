package io.labyrinth.game

import core.Game
import core.GameWindow
import io.labyrinth.core.graphics.Mesh
import io.labyrinth.core.graphics.Renderer
import io.labyrinth.core.graphics.Scene
import org.apache.logging.log4j.LogManager
import org.lwjgl.glfw.GLFW

class Labyrinth(private val gameWindow: GameWindow, private val scene: Scene, private val renderer: Renderer) : Game {
    private var direction = 0
    private var color = 0f

    private val logger = LogManager.getLogger(Labyrinth::class.java)

    override fun init() {
        val positions = floatArrayOf(
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
        )
        val mesh = Mesh(positions, 3)
        scene.addMesh("triangle", mesh)
    }

    override fun start() {
    }

    override fun run() {
    }

    override fun input() {
        direction = if (gameWindow.isKeyPressed(GLFW.GLFW_KEY_UP)) // ARROW UP
            1
        else if (gameWindow.isKeyPressed(GLFW.GLFW_KEY_DOWN)) // ARROW DOWN
            -1
        else
            0 // No key pressed
    }

    override fun update(deltaTime: Float) {
        val speed = 1f
        color += direction * speed * deltaTime

        if (color > 1)
            color = 1f
        else if (color < 0)
            color = 0f
    }

    override fun fixedUpdate(fixedDeltaTime: Float) {
        // Do nothing - used for physics
    }

    override fun render() {
        renderer.render(gameWindow, scene)
    }

    override fun cleanup() {
        renderer.cleanup()
    }
}