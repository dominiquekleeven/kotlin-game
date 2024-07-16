package io.labyrinth

import core.Engine
import core.Game
import core.GameWindow
import io.labyrinth.core.graphics.Renderer
import io.labyrinth.core.graphics.Scene
import io.labyrinth.game.*
import org.apache.logging.log4j.LogManager


fun main() {
    Application().run()
}

class Application {
    private val logger = LogManager.getLogger(Application::class.java)

    fun run () {
        val gameWindow = GameWindow("Labyrinth", true, 1920, 1080)
        val renderer = Renderer()
        val scene = Scene()
        val game: Game = Labyrinth(gameWindow, scene, renderer)
        val engine = Engine(gameWindow, game)

        logger.info("Starting game: Labyrinth")
        engine.start()
        logger.info("Exiting game: Labyrinth")
    }
}




