package com.asledgehammer.sledgehammer.api.entity.living

import com.asledgehammer.crafthammer.api.command.Messagable
import com.asledgehammer.sledgehammer.api.network.Connection
import org.joml.Vector3f

/**
 * **Player** TODO: Document.
 *
 * @author Jab
 */
interface Player : Messagable {

  val name: String
  val index: Short
  val accessLevel: AccessLevel
  val connection: Connection
  val isOnline: Boolean
  val isStaff: Boolean
  fun disconnect(reason: String?)
  fun teleport(location: Vector3f)
  fun teleport(x: Float, y: Float, z: Float)
  fun getLocation(): Vector3f

  /**
   * **AccessLevel** TODO: Document.
   *
   * @author Jab
   */
  enum class AccessLevel {
    NONE,
    GM,
    OBSERVER,
    MODERATOR,
    ADMIN;

    companion object {
      fun get(name: String): AccessLevel {
        val nameUpper = name.toUpperCase().trim()
        for (next in values()) if (nameUpper.equals(next.name)) return next
        return NONE
      }
    }
  }
}