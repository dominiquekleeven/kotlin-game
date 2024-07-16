package core

import io.labyrinth.Application
import org.apache.logging.log4j.LogManager
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback

class Engine(private val gameWindow: GameWindow, private val game: Game) {

    private val logger = LogManager.getLogger(Engine::class.java)

    companion object {
        private const val NANOSECOND = 1000000000.0
        private const val FRAMERATE = 10000
        private const val FIXED_TIME_STEP = 1.0 / FRAMERATE
    }

    // Engine state
    private var fps = 0

    private var isRunning = false
    // Game window
    private var errorCallBack: GLFWErrorCallback? = null

    // Initialize the engine
    private fun init() {
        errorCallBack = GLFWErrorCallback.createPrint(System.err)
        game.init()
    }

    fun start() {
        init()
        if (isRunning)
        {
            logger.error("Engine is already running")
            return
        }
        run()
    }

    private fun run() {
        isRunning = true
        var frames = 0
        var frameCounter = 0f
        var lastTime = System.nanoTime()
        var unprocessedTime = 0.0

        while (isRunning) {
            var render = false
            // Time calculations
            val startTime = System.nanoTime()
            val passedTime = startTime - lastTime
            lastTime = startTime
            unprocessedTime += passedTime / NANOSECOND
            frameCounter += passedTime

            // input
            input()

            // Variable time step for general game logic
            val deltaTime = (passedTime / NANOSECOND).toFloat()
            update(deltaTime)

            // Fixed time step for physics
            while (unprocessedTime >= FIXED_TIME_STEP) {
                fixedUpdate(FIXED_TIME_STEP.toFloat())
                unprocessedTime -= FIXED_TIME_STEP
                render = true

                if (gameWindow.shouldClose())
                    stop()

                if (frameCounter >= NANOSECOND) {
                    fps = frames
                    gameWindow.updateWindowTitle(gameWindow.title + " | FPS: " + frames)
                    frames = 0
                    frameCounter = 0f
                }
            }

            // rendering
            if (render) {
                render()
                frames++
            }
        }
        cleanup()
    }


    private fun stop() {
        if (!isRunning)
        {
            return
        }
        logger.info("Stopping engine")
        isRunning = false
    }

    private fun input() {
        game.input()
    }

    private fun update(deltaTime: Float) {
        game.update(deltaTime)
    }

    private fun fixedUpdate(fixedDeltaTime: Float) {
        game.fixedUpdate(fixedDeltaTime)
    }

    private fun render() {
        game.render()
        gameWindow.update()
    }

    private fun cleanup() {
        gameWindow.cleanup()
        game.cleanup()
        errorCallBack?.free()
        GLFW.glfwTerminate()
    }





}