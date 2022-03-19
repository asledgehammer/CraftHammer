package com.asledgehammer.crafthammer.api.event

abstract class Event {
  /** The timestamp when the event is created. */
  val timestamp = System.currentTimeMillis()

  /** If the event is handled by a listener. */
  var handled = false
}