package io.labyrinth.core.graphics

import io.labyrinth.core.shader.ShaderModuleData
import io.labyrinth.core.shader.ShaderProgram
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL30.*

class SceneRenderer {

    private val logger = LogManager.getLogger(SceneRenderer::class.java)
    private var shaderProgram: ShaderProgram? = null

    init {
        logger.info("Initializing SceneRenderer")
        val shaderModuleDataList =  ArrayList<ShaderModuleData>()
        val vertexShaderPath = "/shaders/scene.vert"
        val fragmentShaderPath = "/shaders/scene.frag"

        shaderModuleDataList.add(ShaderModuleData(vertexShaderPath, GL_VERTEX_SHADER))
        shaderModuleDataList.add(ShaderModuleData(fragmentShaderPath, GL_FRAGMENT_SHADER))

        shaderProgram = ShaderProgram(shaderModuleDataList)
        logger.info("SceneRenderer initialized")
    }

    fun cleanup()
    {
        shaderProgram?.cleanup()
    }

    fun render(scene: Scene)
    {
        shaderProgram?.bind()
        // Render
        scene.meshMap.forEach { (k, v) ->
            glBindVertexArray(v.vaoId)
            glDrawArrays(GL_TRIANGLES, 0, v.numVertices)
        }
        glBindVertexArray(0)
        shaderProgram?.unbind()
    }
}