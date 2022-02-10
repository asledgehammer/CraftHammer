package com.asledgehammer.crafthammer.api.permission

/**
 * **Permissionable** TODO: Document.
 *
 * @author Jab
 */
interface Permissionable {
  fun hasPermission(permission: String): Boolean
}
