package com.asledgehammer.crafthammer.api

import com.asledgehammer.crafthammer.api.command.Commands
import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.api.event.Events
import com.asledgehammer.crafthammer.api.permission.Permissions
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.crafthammer.util.component.TextComponent

/**
 * **Hammer** TODO: Document.
 *
 * @author Jab
 */
interface Hammer : LogSupported {

  companion object {
    lateinit var INSTANCE: Hammer
  }

  val VERSION: String
  val BETA_VERSION: String
  val IS_BETA: Boolean
  var events: Events
  var commands: Commands
  var permissions: Permissions
  val cfg: YamlFile
  val onlinePlayers: Collection<Player>

  fun broadcast(name: String = "Server", message: String, filter: ((player: Player) -> Boolean)? = null)
  fun broadcast(name: String = "Server", message: TextComponent, filter: ((player: Player) -> Boolean)? = null)
  fun isPvpEnabled(): Boolean
}