package com.asledgehammer.crafthammer.api.event.network

import com.asledgehammer.crafthammer.api.event.Event
import com.asledgehammer.crafthammer.api.network.Connection

abstract class NetworkEvent(val connection: Connection): Event()