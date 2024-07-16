package io.labyrinth.core.shader

import io.labyrinth.core.common.Util
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.GL30.*


class ShaderProgram (shaderModuleDataList : List<ShaderModuleData>) {
    private val logger = LogManager.getLogger(ShaderProgram::class.java)
    private var programId : Int = 0

    init {
        programId = glCreateProgram()

        if (programId == 0) {
            logger.error("Could not create Shader")
            throw Exception("Could not create Shader")
        }

        val shaderModules: MutableList<Int> = ArrayList()
        shaderModuleDataList.forEach { s ->
            shaderModules.add(
                createShader(
                    Util.readFile(s.shaderFile),
                    s.shaderType
                )
            )
        }

        link(shaderModules)
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() {
        glUseProgram(0)
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }

    private fun createShader(shaderCode: String?, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            logger.error("Error creating shader. Type: $shaderType")
            throw RuntimeException("Error creating shader. Type: $shaderType")
        }

        if (shaderCode != null) {
            glShaderSource(shaderId, shaderCode)
        }
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            logger.error("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024))
            throw RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024))
        }

        glAttachShader(programId, shaderId)
        return shaderId
    }

    private fun link(shaderModules: List<Int>) {
        shaderModules.forEach { s ->
            glAttachShader(programId, s)
        }

        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            logger.error("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024))
            throw RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024))
        }

        shaderModules.forEach { s ->
            glDetachShader(programId, s)
        }

        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            logger.error("Error validating Shader code: " + glGetProgramInfoLog(programId, 1024))
            throw RuntimeException("Error validating Shader code: " + glGetProgramInfoLog(programId, 1024))
        }
    }

}