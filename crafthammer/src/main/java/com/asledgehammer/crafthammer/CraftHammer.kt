@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.asledgehammer.crafthammer

import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.crafthammer.util.component.TextComponent
import com.asledgehammer.craftnail.CraftNail

/**
 * **Craftboid** is the base class for all Craftboid operations. All handling of native code for PZ goes through here.
 *
 * @author Jab
 */
object CraftHammer {

  val VERSION = CraftNail.VERSION
  val BETA_VERSION = CraftNail.BETA_VERSION
  val IS_BETA = CraftNail.IS_BETA

  val cfg: YamlFile
    get() = CraftNail.cfg

  val onlinePlayers: Collection<Player>
    get() = CraftNail.onlinePlayers

  @JvmStatic
  fun log(list: List<Any?>) = CraftNail.log(list)

  @JvmStatic
  fun log(vararg objects: Any?) = CraftNail.log(objects)

  @JvmStatic
  @JvmOverloads
  fun logError(message: String, throwable: Throwable? = null) = CraftNail.logError(message, throwable)

  /**
   * TODO: Document.
   *
   * @param name
   * @param message
   * @param filter
   */
  @JvmOverloads
  @JvmStatic
  fun broadcast(name: String = "Server", message: String, filter: ((player: Player) -> Boolean)? = null) {
    val players = if (filter != null) onlinePlayers.filter(filter) else onlinePlayers
    for (player in players) {
      if (!player.isOnline) continue
      player.sendMessage(name, message)
    }
  }

  /**
   * TODO: Document.
   *
   * @param name
   * @param message
   * @param filter
   */
  @JvmOverloads
  @JvmStatic
  fun broadcast(name: String, message: TextComponent, filter: ((player: Player) -> Boolean)? = null) {
    val players = if (filter != null) onlinePlayers.filter(filter) else onlinePlayers
    for (player in players) {
      if (!player.isOnline) continue
      player.sendMessage(name, message)
    }
  }

  @JvmStatic
  fun isPvpEnabled(): Boolean = CraftNail.isPvpEnabled()
}