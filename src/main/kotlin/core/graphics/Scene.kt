package io.labyrinth.core.graphics

import java.util.function.Consumer


class Scene {
    val meshMap: MutableMap<String, Mesh> = HashMap()

    fun addMesh(meshId: String, mesh: Mesh) {
        meshMap[meshId] = mesh
    }

    fun cleanup() {
        meshMap.values.forEach(Consumer { obj: Mesh -> obj.cleanup() })
    }
}