package com.asledgehammer.crafthammer.api.event

import java.util.*

/**
 * **Events** TODO: Document.
 *
 * @author Jab
 */
interface Events {
  fun register(id: UUID, listener: EventListener)
  fun unregister(id: UUID)
  fun unregister(listener: EventListener, sort: Boolean = true)
  fun dispatch(event: Event)
  fun reset()

  /**
   * **Events.Wrapper** TODO: Document.
   *
   * @author Jab
   */
  interface Wrapper {
    val eventWrappers: HashMap<Class<out Event?>, ArrayList<EventHandleWrapper>>
    fun register(listener: EventListener)
    fun unregister(listener: EventListener)
    fun reset()
  }
}