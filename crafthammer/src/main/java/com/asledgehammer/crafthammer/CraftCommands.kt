package com.asledgehammer.crafthammer

import ReflectionUtils
import com.asledgehammer.crafthammer.api.command.*
import java.lang.reflect.Method
import java.util.*

/**
 * **CraftCommands** TODO: Document.
 *
 * @author Jab
 */
class CraftCommands : Commands {

  private val wrappers = HashMap<UUID, CraftWrapper>()
  private val sortedCommandWrappers = HashMap<String, ArrayList<CommandHandleWrapper>>()
  private val commandWrappersToRemove = ArrayList<CommandHandleWrapper>()

  override fun register(id: UUID, listener: CommandListener) {
    wrappers.computeIfAbsent(id) { CraftWrapper() }.register(listener)
    sort()
  }

  override fun unregister(id: UUID) {
    val wrapper = wrappers.remove(id) ?: return
    wrapper.reset()
  }

  override fun unregister(listener: CommandListener, sort: Boolean) {
    for ((_, wrapper) in wrappers) wrapper.unregister(listener)
    if (sort) sort()
  }

  override fun dispatch(command: Command, commander: CommandSender): CommandExecution {
    val response = Command.Response()
    val key = command.name.lowercase(Locale.getDefault()).trim()
    val execution = CommandExecution(command, commander, response)
    val wrappers = sortedCommandWrappers[key] ?: return execution
    response.found = true
    for (wrapper in wrappers) {
      try {
        wrapper.dispatch(execution)
        if (response.denied) break
      } catch (throwable: Throwable) {
        CraftHammer.logError("Failed to execute listener: ${wrapper.javaClass.simpleName}. Disabling.")
        throwable.printStackTrace(System.err)
        wrapper.isEnabled = false
        commandWrappersToRemove.add(wrapper)
      }
    }
    // Remove any disabled wrappers.
    if (commandWrappersToRemove.isNotEmpty()) {
      for (wrapper in commandWrappersToRemove) unregister(wrapper.listener, false)
      sort()
    }
    return execution
  }

  override fun reset() {
    for ((_, wrapper) in wrappers) wrapper.reset()
    wrappers.clear()
  }

  private fun sort() {
    sortedCommandWrappers.clear()
    for ((_, wrapper) in wrappers) {
      for ((key, collection) in wrapper.commandWrappers) {
        val next = sortedCommandWrappers.computeIfAbsent(key) { ArrayList() }
        for (handle in collection) next.add(handle)
      }
      for ((_, list) in sortedCommandWrappers) list.sortByDescending { it.annotation.priority }
    }
  }

  private class CraftWrapper : Commands.Wrapper {

    override val commandWrappers = HashMap<String, ArrayList<CommandHandleWrapper>>()

    override fun register(listener: CommandListener) {
      val methods: List<Method> = ReflectionUtils.getAllDeclaredMethods(listener.javaClass)
      if (methods.isEmpty()) return
      for (method in methods) {
        val commandHandler = method.getAnnotation(CommandHandler::class.java)
        if (commandHandler != null) {
          val wrapper = CommandHandleWrapper(listener, commandHandler, method)
          for (command in commandHandler.commands) {
            val key = command.lowercase(Locale.getDefault()).trim()
            commandWrappers.computeIfAbsent(key) { ArrayList() }.add(wrapper)
          }
        }
      }
    }

    override fun unregister(listener: CommandListener) {
      val commandsToRemove = ArrayList<String>()
      for ((command, wrapper) in commandWrappers) {
        val iterator = wrapper.iterator()
        while (iterator.hasNext()) {
          val next = iterator.next()
          if (next.listener == listener) iterator.remove()
        }
        if (wrapper.isEmpty()) commandsToRemove.add(command)
      }
      if (commandsToRemove.isNotEmpty()) {
        for (command in commandsToRemove) commandWrappers.remove(command)
      }
    }

    override fun reset() {
      for ((_, list) in commandWrappers) {
        val iterator: MutableIterator<CommandHandleWrapper> = list.iterator()
        while (iterator.hasNext()) {
          val wrapper: CommandHandleWrapper = iterator.next()
          wrapper.isEnabled = false
          iterator.remove()
        }
      }
      commandWrappers.clear()
    }
  }
}