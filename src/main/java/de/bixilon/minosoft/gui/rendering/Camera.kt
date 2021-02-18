package de.bixilon.minosoft.gui.rendering

import de.bixilon.minosoft.Minosoft
import de.bixilon.minosoft.config.config.game.controls.KeyBindingsNames
import de.bixilon.minosoft.config.key.KeyAction
import de.bixilon.minosoft.config.key.KeyCodes
import de.bixilon.minosoft.data.entities.EntityRotation
import de.bixilon.minosoft.data.entities.Location
import de.bixilon.minosoft.gui.rendering.shader.Shader
import de.bixilon.minosoft.protocol.network.Connection
import de.bixilon.minosoft.protocol.packets.serverbound.play.PacketPlayerPositionAndRotationSending
import de.bixilon.minosoft.protocol.protocol.ProtocolDefinition
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import kotlin.math.cos
import kotlin.math.sin

class Camera(private val connection: Connection, private var fov: Float) {
    private var mouseSensitivity = Minosoft.getConfig().config.game.camera.moseSensitivity
    private var movementSpeed = 7
    var cameraPosition = Vec3(0.0f, 0.0f, 0.0f)
    private var lastMouseX = 0.0
    private var lastMouseY = 0.0
    var yaw = 0.0
    var pitch = 0.0
    private var zoom = 0f

    private var lastPositionChange = 0L
    private var currentPositionSent = false

    private var cameraFront = Vec3(0.0f, 0.0f, -1.0f)
    private var cameraRight = Vec3(0.0f, 0.0f, -1.0f)
    private var cameraUp = Vec3(0.0f, 1.0f, 0.0f)

    private var screenHeight = 0
    private var screenWidth = 0
    private val shaders: MutableSet<Shader> = mutableSetOf()

    private var keyForwardDown = false
    private var keyLeftDown = false
    private var keyRightDown = false
    private var keyBackDown = false
    private var keyFlyUp = false
    private var keyFlyDown = false
    private var keySprintDown = false
    private var keyZoomDown = false

    fun mouseCallback(xPos: Double, yPos: Double) {
        var xOffset = xPos - this.lastMouseX
        var yOffset = this.lastMouseY - yPos // reversed since y-coordinates go from bottom to top
        lastMouseX = xPos
        lastMouseY = yPos
        xOffset *= mouseSensitivity
        yOffset *= mouseSensitivity
        yaw += xOffset
        pitch -= yOffset

        // make sure that when pitch is out of bounds, screen doesn't get flipped
        if (this.pitch > 89.9) {
            this.pitch = 89.9
        } else if (this.pitch < -89.9) {
            this.pitch = -89.9
        }
        if (this.yaw > 180) {
            this.yaw -= 360
        } else if (this.yaw < -180) {
            this.yaw += 360
        }
        this.yaw %= 180
        setRotation(yaw, pitch)
    }

    fun init(renderWindow: RenderWindow) {
        renderWindow.registerKeyCallback(KeyBindingsNames.MOVE_FORWARD) { _: KeyCodes, keyAction: KeyAction ->
            keyForwardDown = keyAction == KeyAction.PRESS
        }
        renderWindow.registerKeyCallback(KeyBindingsNames.MOVE_LEFT) { _: KeyCodes, keyAction: KeyAction ->
            keyLeftDown = keyAction == KeyAction.PRESS
        }
        renderWindow.registerKeyCallback(KeyBindingsNames.MOVE_BACKWARDS) { _: KeyCodes, keyAction: KeyAction ->
            keyBackDown = keyAction == KeyAction.PRESS
        }
        renderWindow.registerKeyCallback(KeyBindingsNames.MOVE_RIGHT) { _: KeyCodes, keyAction: KeyAction ->
            keyRightDown = keyAction == KeyAction.PRESS
        }
        renderWindow.registerKeyCallback(KeyBindingsNames.MOVE_FLY_UP) { _: KeyCodes, keyAction: KeyAction ->
            keyFlyUp = keyAction == KeyAction.PRESS
        }
        renderWindow.registerKeyCallback(KeyBindingsNames.MOVE_FLY_DOWN) { _: KeyCodes, keyAction: KeyAction ->
            keyFlyDown = keyAction == KeyAction.PRESS
        }
        renderWindow.registerKeyCallback(KeyBindingsNames.MOVE_SPRINT) { _: KeyCodes, keyAction: KeyAction ->
            keySprintDown = keyAction == KeyAction.PRESS
        }
        renderWindow.registerKeyCallback(KeyBindingsNames.ZOOM) { _: KeyCodes, keyAction: KeyAction ->
            keyZoomDown = keyAction == KeyAction.PRESS
        }
    }

    fun handleInput(deltaTime: Double) {
        var cameraSpeed = movementSpeed * deltaTime

        if (keySprintDown) {
            cameraSpeed *= 5
        }
        val lastPosition = cameraPosition
        val currentY = cameraPosition.y
        if (keyForwardDown) {
            cameraPosition = cameraPosition + cameraFront * cameraSpeed
        }
        if (keyBackDown) {
            cameraPosition = cameraPosition - cameraFront * cameraSpeed
        }
        if (keyLeftDown) {
            cameraPosition = cameraPosition - cameraRight * cameraSpeed
        }
        if (keyRightDown) {
            cameraPosition = cameraPosition + cameraRight * cameraSpeed
        }
        this.cameraPosition.y = currentY // stay on xz line when moving (aka. no clip): ToDo: make movement not slower when you look up
        if (keyFlyDown) {
            cameraPosition = cameraPosition - CAMERA_UP_VEC3 * cameraSpeed
        }
        if (keyFlyUp) {
            cameraPosition = cameraPosition + CAMERA_UP_VEC3 * cameraSpeed
        }
        if (lastPosition != cameraPosition) {
            recalculateViewMatrix()
            sendPositionToServer()
        }

        val lastZoom = zoom
        zoom = if (keyZoomDown) {
            2f
        } else {
            0f
        }
        if (lastZoom != zoom) {
            recalculateProjectionMatrix()
        }

    }

    fun addShaders(vararg shaders: Shader) {
        this.shaders.addAll(shaders)
    }

    fun screenChangeResizeCallback(screenWidth: Int, screenHeight: Int) {
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
        recalculateProjectionMatrix()
    }

    private fun recalculateProjectionMatrix() {
        for (shader in shaders) {
            shader.use().setMat4("projectionMatrix", calculateProjectionMatrix(screenWidth, screenHeight))
        }
    }

    private fun calculateProjectionMatrix(screenWidth: Int, screenHeight: Int): Mat4 {
        return glm.perspective(glm.radians(fov / (zoom + 1.0f)), screenWidth.toFloat() / screenHeight.toFloat(), 0.2f, 1000f)
    }

    private fun recalculateViewMatrix() {
        for (shader in shaders) {
            shader.use().setMat4("viewMatrix", calculateViewMatrix())
        }
    }

    private fun calculateViewMatrix(): Mat4 {
        return glm.lookAt(cameraPosition, cameraPosition + cameraFront, CAMERA_UP_VEC3)
    }

    fun setFOV(fov: Float) {
        this.fov = fov
    }

    fun setRotation(yaw: Double, pitch: Double) {
        cameraFront = Vec3(
            (cos(glm.radians(yaw + 90)) * cos(glm.radians(-pitch))).toFloat(),
            sin(glm.radians(-pitch)).toFloat(),
            (sin(glm.radians(yaw + 90)) * cos(glm.radians(-pitch))).toFloat())
            .normalize()

        cameraRight = cameraFront.cross(CAMERA_UP_VEC3).normalize()
        cameraUp = cameraRight.cross(cameraFront).normalize()
        recalculateViewMatrix()
        sendPositionToServer()
    }

    fun draw() {
        if (!currentPositionSent) {
            sendPositionToServer()
        }
    }

    private fun sendPositionToServer() {
        if (System.currentTimeMillis() - lastPositionChange > ProtocolDefinition.TICK_TIME) {
            // ToDo: Replace this with proper movement and only send it, when our position changed
            connection.sendPacket(PacketPlayerPositionAndRotationSending(Location(cameraPosition), EntityRotation(yaw, pitch), false))
            lastPositionChange = System.currentTimeMillis()
            currentPositionSent = true
            return
        }
        currentPositionSent = false
    }

    companion object {
        private val CAMERA_UP_VEC3 = Vec3(0.0f, 1.0f, 0.0f)
    }
}
