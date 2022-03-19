@file:Suppress("unused")

package com.asledgehammer.crafthammer

import com.asledgehammer.crafthammer.api.permission.*
import com.asledgehammer.crafthammer.util.cfg.CFGSection
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import java.io.File
import java.util.*

/**
 * **CraftPermissions** TODO: Document.
 *
 * @author Jab
 */
class CraftPermissions : Permissions {

  private val groupsById = HashMap<UUID, PermissionGroup>()
  private val groupsByName = HashMap<String, PermissionGroup>()
  private val usersById = HashMap<UUID, PermissionUser>()
  private val usersByName = HashMap<String, PermissionUser>()

  override fun loadYAML(file: File) {
    val groupParentIds = HashMap<UUID, UUID>()

    val config = YamlFile(file)
    config.read()

    if (config.isSection("users")) {
      loadUsers(config.getSection("users"))
    }
    if (config.isSection("groups")) {
      loadGroups(config.getSection("groups"), groupParentIds)
    }
    if (groupsById.isNotEmpty() && groupParentIds.isNotEmpty()) {
      pairGroups(groupParentIds)
    }
  }

  override fun saveYAML(file: File) {
    val config = YamlFile(file)
    config.read()
    saveGroups(config.createSection("groups"))
    saveUsers(config.createSection("users"))
  }

  override fun addGroup(group: PermissionGroup) {
    require(groupsById[group.id] == null) { "Permission group already exists: $group" }
    groupsById[group.id] = group
    groupsByName[group.name] = group
  }

  override fun getGroup(id: UUID): PermissionGroup {
    val group = groupsById[id]
    requireNotNull(group) { "Permission group not found: $id" }
    return group
  }

  override fun getGroup(name: String): PermissionGroup {
    val formattedName = name.lowercase(Locale.getDefault()).trim()
    val group = groupsByName[formattedName]
    requireNotNull(group) { "Permission group not found: $formattedName" }
    return group
  }

  override fun removeGroup(group: PermissionGroup) {
    groupsById.remove(group.id)
    groupsByName.remove(group.name)
  }

  override fun removeGroup(id: UUID) {
    val group = groupsById.remove(id)
    requireNotNull(group) { "Permission group not found: $id" }
    removeGroup(group)
  }

  override fun removeGroup(name: String) {
    val formattedName = name.lowercase(Locale.getDefault()).trim()
    val group = groupsByName.remove(formattedName)
    requireNotNull(group) { "Permission group not found: $formattedName" }
    removeGroup(group)
  }

  override fun addUser(user: PermissionUser) {
    require(usersById[user.id] == null) { "Permission user already exists: $user" }
    usersById[user.id] = user
    usersByName[user.name] = user
  }

  override fun getUser(id: UUID): PermissionUser {
    val user = usersById[id]
    requireNotNull(user) { "Permission user not found: $id" }
    return user
  }

  override fun getUser(name: String): PermissionUser {
    val formattedName = name.lowercase(Locale.getDefault()).trim()
    val user = usersByName[formattedName]
    requireNotNull(user) { "Permission user not found: $formattedName" }
    return user
  }

  override fun removeUser(user: PermissionUser) {
    usersById.remove(user.id)
    usersByName.remove(user.name)
  }

  override fun removeUser(id: UUID) {
    val user = usersById.remove(id)
    requireNotNull(user) { "Permission user not found: $id" }
    removeUser(user)
  }

  override fun removeUser(name: String) {
    val formattedName = name.lowercase(Locale.getDefault()).trim()
    val user = usersByName.remove(formattedName)
    requireNotNull(user) { "Permission user not found: $formattedName" }
    removeUser(user)
  }

  private fun loadPermissions(cfg: CFGSection): List<Permission> {
    val list = ArrayList<Permission>()
    val permissions = cfg.getSection("permissions")
    for (key in permissions.allKeys) {
      if (!key.startsWith(CFGSection.SEPARATOR)) {
        CraftHammer.logError("Invalid permission context syntax: $key")
        continue
      }
      if (!permissions.isBoolean(key)) {
        CraftHammer.logError("Invalid permission flag: ${permissions.get(key)}")
        continue
      }
      list.add(Permission(key, permissions.getBoolean(key)))
    }
    return list
  }

  private fun loadUsers(cfg: CFGSection): List<PermissionUser> {
    val list = ArrayList<PermissionUser>()
    for ((idString, cfgUser) in cfg.sections) {
      val id: UUID
      try {
        id = UUID.fromString(idString)
      } catch (e: IllegalArgumentException) {
        CraftHammer.logError("Invalid UUID for permission user: $idString")
        continue
      }
      require(cfgUser.contains("name")) {
        "Permission user does not have a name."
      }
      require(cfgUser.isString("name")) {
        "Permission user does not have a valid name. (${cfgUser.get("name")})"
      }
      val name = cfgUser.getString("name")
      val user = PermissionUser(id, name)
      if (cfgUser.isSection("permissions")) {
        val permissions = loadPermissions(cfg.getSection("permissions"))
        user.set(permissions)
      }
      usersById[user.id] = user
      usersByName[user.name] = user
      list.add(user)
    }
    return list
  }

  private fun loadGroups(cfg: CFGSection, groupParentIds: HashMap<UUID, UUID>): List<PermissionGroup> {
    val list = ArrayList<PermissionGroup>()
    for ((idString, cfgGroup) in cfg.sections) {
      val id: UUID
      try {
        id = UUID.fromString(idString)
      } catch (e: IllegalArgumentException) {
        CraftHammer.logError("Invalid UUID for permission group: $idString")
        continue
      }
      require(cfgGroup.contains("name")) {
        "Permission group does not have a name."
      }
      require(cfgGroup.isString("name")) {
        "Permission group does not have a valid name. (${cfgGroup.get("name")})"
      }
      val name = cfgGroup.getString("name")
      val group = PermissionGroup(id, name)
      if (cfgGroup.isSection("permissions")) {
        val permissions = loadPermissions(cfg.getSection("permissions"))
        group.set(permissions)
      }

      if (cfgGroup.isList("members")) {
        for (userIdString in cfgGroup.getStringList("members")) {
          val userId: UUID
          try {
            userId = UUID.fromString(userIdString)
          } catch (e: IllegalArgumentException) {
            CraftHammer.logError("Invalid UUID for group member: (group: ${group.name}, id: $userIdString)")
            continue
          }
          val user = usersById[userId]
          if (user == null) {
            CraftHammer.logError("User does not exist: (group: ${group.name}, id: $userId)")
            continue
          }
          group.addMember(user)
        }
      }

      if (cfgGroup.isString("parent")) {
        val parentIdString = cfgGroup.getString("parent")
        val parentId: UUID
        try {
          parentId = UUID.fromString(parentIdString)
        } catch (e: IllegalArgumentException) {
          System.err.println("Invalid UUID for group parent: (group: ${group.name}, id: $parentIdString)")
          continue
        }
        groupParentIds[group.id] = parentId
      }

      groupsById[group.id] = group
      groupsByName[group.name] = group

      list.add(group)
    }

    return list
  }

  private fun pairGroups(groupParentIds: HashMap<UUID, UUID>) {
    for ((groupId, parentId) in groupParentIds) {
      val group = groupsById[groupId]!!
      val parent = groupsById[parentId]
      if (parent == null) {
        System.err.println("Parent for group \"$group\" doesn't exist: $parentId")
        continue
      }
      group.parent = parent
    }
  }

  private fun savePermissions(cfg: CFGSection, collection: PermissionCollection) {
    for ((_, permission) in collection.permissions) {
      cfg.set(permission.context, permission.flag)
    }
  }

  private fun saveGroup(cfg: CFGSection, group: PermissionGroup) {
    cfg.set("name", group.name)
    if (group.parent != null) cfg.set("parent", group.parent!!.id.toString())
    if (group.hasMembers()) {
      val list = ArrayList<String>()
      for (member in group.members) {
        list.add(member.id.toString())
      }
      cfg.set("members", list)
    }
    savePermissions(cfg.createSection("permissions"), group)
  }

  private fun saveUser(cfg: CFGSection, user: PermissionUser) {
    cfg.set("name", user.name)
    savePermissions(cfg.createSection("permissions"), user)
  }

  private fun saveGroups(cfg: CFGSection) {
    for ((_, group) in groupsById) saveGroup(cfg.createSection(group.id.toString()), group)
  }

  private fun saveUsers(cfg: CFGSection) {
    for ((_, user) in usersById) saveUser(cfg.createSection(user.id.toString()), user)
  }
}
