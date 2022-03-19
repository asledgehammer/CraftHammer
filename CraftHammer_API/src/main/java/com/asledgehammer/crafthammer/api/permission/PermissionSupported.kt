package com.asledgehammer.crafthammer.api.permission

/**
 * **Permissionable** TODO: Document.
 *
 * @author Jab
 */
interface PermissionSupported {
  fun hasPermission(permission: String): Boolean
}
