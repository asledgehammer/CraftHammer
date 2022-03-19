package com.asledgehammer.crafthammer.api.event.network

import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.api.event.Cancelable
import com.asledgehammer.crafthammer.api.network.Connection

class PostLoginEvent(connection: Connection, val player: Player) : NetworkEvent(connection), Cancelable {
  override var cancelled: Boolean = false
}
