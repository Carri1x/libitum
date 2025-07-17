package com.libitum.app.model.enums;

/**
 * Enum que define los roles disponibles en la aplicación.
 * Estos roles se utilizan para controlar el acceso a diferentes partes de la aplicación.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 * 
 * Los roles son:
 * - ROLE_USER: Rol básico para usuarios registrados.
 * - ROLE_ADMIN: Rol para administradores con acceso completo.
 * - ROLE_VIEWER: Rol para usuarios con acceso solo de lectura y visualización de contenido.
 */
public enum RoleList {
    ROLE_USER, ROLE_ADMIN, ROLE_VIEWER
}
