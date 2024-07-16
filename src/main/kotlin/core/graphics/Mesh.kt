package io.labyrinth.core.graphics

import org.apache.logging.log4j.LogManager
import org.lwjgl.system.MemoryStack
import org.lwjgl.opengl.GL30.*

class Mesh(private var positions: FloatArray, var numVertices: Int) {

    private var vboIdList : ArrayList<Int>? = null
    var vaoId : Int = 0

    private val logger = LogManager.getLogger(Mesh::class.java)

    init {
        MemoryStack.stackPush().use { stack ->
            vboIdList = ArrayList()
            vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            // Positions VBO
            val vboId: Int = glGenBuffers()
            vboIdList!!.add(vboId)
            val positionsBuffer = stack.callocFloat(positions.size)
            positionsBuffer.put(0, positions)
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)
        }

        logger.info("Mesh created successfully with ${positions.size} vertices and $numVertices indices in VAO: $vaoId")
    }

    fun cleanup() {
        vboIdList?.forEach { vboId ->
            glDeleteBuffers(vboId)
        }

        glDeleteVertexArrays(vaoId)
    }
}