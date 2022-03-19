package com.asledgehammer.crafthammer.api.event.player

import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.api.event.Event

abstract class PlayerEvent(val player: Player): Event()