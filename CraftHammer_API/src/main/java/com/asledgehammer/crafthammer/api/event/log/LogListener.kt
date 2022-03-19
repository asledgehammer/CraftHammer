package com.asledgehammer.crafthammer.api.event.log

interface LogListener {
  fun onLogMessage(entry: LogEntry)
}