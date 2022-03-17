package com.asledgehammer.crafthammer.api.network

/**
 * **Connection** TODO: Document.
 *
 * @author Jab
 */
interface Connection {
  fun getGuid(): Long
  fun disconnect(reason: String? = "Generic")
  fun isFullyConnected(): Boolean
  fun sendMessage(name: String, message: String)
}
