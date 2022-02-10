package com.asledgehammer.sledgehammer.api.network

/**
 * **Connection** TODO: Document.
 *
 * @author Jab
 */
interface Connection {
  fun getGuid(): Long
  fun disconnect(reason: String?)
  fun isFullyConnected(): Boolean
  fun sendMessage(name: String, message: String)
}
