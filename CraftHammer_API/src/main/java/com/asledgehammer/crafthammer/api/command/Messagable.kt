package com.asledgehammer.crafthammer.api.command

import com.asledgehammer.crafthammer.util.component.TextComponent;

/**
 * **MessageSender** TODO: Document.
 *
 * @author Jab
 */
interface Messagable {

  fun sendMessage(name: String = "Server", message: String)
  fun sendMessage(name: String = "Server", message: TextComponent)

  fun sendMessages(name: String = "Server", vararg messages: String) {
    if (messages.isEmpty()) return
    for (message in messages) sendMessage(name, message)
  }

  fun sendMessages(name: String = "Server", vararg messages: TextComponent) {
    if (messages.isEmpty()) return
    for (message in messages) sendMessage(name, message)
  }

  fun sendMessages(name: String = "Server", messages: Collection<TextComponent>) {
    if (messages.isEmpty()) return
    for (message in messages) sendMessage(name, message)
  }
}
