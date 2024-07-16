package core

interface Game {
    fun init()
    fun start()
    fun run()

    /*
     * User input handling
     */
    fun input()

    /**
     * @param deltaTime time since last frame
     */
    fun update(deltaTime: Float)

    /**
     * @param fixedDeltaTime fixed time step
     */
    fun fixedUpdate(fixedDeltaTime: Float)

    fun render()
    fun cleanup()
}