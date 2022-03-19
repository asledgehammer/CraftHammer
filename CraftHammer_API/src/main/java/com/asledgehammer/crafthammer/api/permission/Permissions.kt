package com.asledgehammer.crafthammer.api.permission

import java.io.File
import java.util.*

/**
 * **Permissions** TODO: Document.
 *
 * @author Jab
 */
interface Permissions {

  fun loadYAML(file: File)

  fun saveYAML(file: File)

  fun addGroup(group: PermissionGroup)

  fun getGroup(id: UUID): PermissionGroup

  fun getGroup(name: String): PermissionGroup

  fun removeGroup(group: PermissionGroup)

  fun removeGroup(id: UUID)

  fun removeGroup(name: String)

  fun addUser(user: PermissionUser)

  fun getUser(id: UUID): PermissionUser

  fun getUser(name: String): PermissionUser

  fun removeUser(user: PermissionUser)

  fun removeUser(id: UUID)

  fun removeUser(name: String)
}