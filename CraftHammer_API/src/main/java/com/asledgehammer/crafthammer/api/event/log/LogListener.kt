package com.asledgehammer.crafthammer.api.event.log

interface LogListener {
  fun onLogMessage(type: LogType, message: String)
}