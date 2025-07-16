# Libitum

> 🎵 Plataforma digital para artistas callejeros, con login seguro, sistema de donaciones a través de código QR y mapa en tiempo real.

## 📌 Índice

- [📝 Descripción](#-descripción)
- [⚙️ Tecnologías](#️-tecnologías)
- [🔐 Autenticación](#-autenticación)
- [📁 Estructura de carpetas](#-estructura-de-carpetas)
- [♨️ Documentación Java Spring Boot](#-documentación-java-spring-boot)

---

## 📝 Descripción

Libitum es una aplicación web para dar visibilidad a artistas callejeros. Permite a los artistas gestionar su perfil, generar un código QR para recibir donaciones, mostrarse en un mapa en tiempo real, y conectar con su audiencia.

---

## ⚙️ Tecnologías
- Java 17 + Spring Boot 3 ♨️
- Spring Security (Jwt)
- PostgreSQL 🐘
- Angular 🅰️
- Git + GitHub 

---

## 🔐 Autenticación 

> Actualmente implementado:
- [x] Login propio con JWT
- [x] Registro de usuarios
- [ ] Verificación por email
- [ ] Login con Google (OAuth2)

---
## 📁 Estructura de carpetas

src/
└── main/
    └── java/com/libitum/app/
        ├── config/
        │   └── SecurityConfig.java
        ├── controllers/
        │   └── AuthController.java
        ├── jwt/
        │   ├── JwtAuthenticationFilter.java
        │   ├── JwtEntryPoint.java
        │   └── JwtUtil.java
        ├── model/
        │   ├── enums/
        │   │   └── RoleList.java
        │   ├── user/
        │   │   ├── LoginUserDto.java
        │   │   ├── RegisterUserDto.java
        │   │   ├── ResponseUserDto.java
        │   │   └── User.java
        │   └── Role.java
        ├── repositories/
        │   ├── RoleRepository.java
        │   └── UserRepository.java
        ├── services/
        │   ├── AuthService.java
        │   └── UserService.java
        └── util/
            └── UserMapper.java
---

## ♨️ Documentación Java Spring Boot