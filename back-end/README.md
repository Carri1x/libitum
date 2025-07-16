# Libitum

> 🎵 Plataforma digital para artistas callejeros, con login seguro, sistema de donaciones a través de código QR y mapa en tiempo real.

## 📌 Índice

- [📝 Descripción](#-descripción)
- [⚙️ Tecnologías](#️-tecnologías)
- [🔐 Autenticación](#-autenticación)
- [📁 Estructura de carpetas](#-estructura-de-carpetas)
- [♨️ Documentación Java Spring Boot](#documentación-java-spring-boot)

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
```text
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
```
- [ 🌲 tree-maker ](https://tree.nathanfriend.com/?s=(%27opti9s!(%27fancy!true~fullPath!false~trailingSlash!true~rootDot!false)~H(%27H%27src6main6-N%2Fcom%2Flibitum%2Fapp%2F*c9figBSecurityC9fig0cIsBGCI0jwtBJwtGenticati9Filt5OEntryPointOUtil0modelBenumsB7ListKus5B-Login4Regist54Resp9se42070r3ies*7R3yK2R3y0s8sBGS8K2S80utilB2Mapp50%27)~v5si9!%271%27)*6---%20%200.N*2Us53epositor42DtoK-5er6%5Cn-7-Role85vice9onB*-GAuthHsource!I9troll5K0-NjavaOKJwt%01ONKIHGB987654320-*)
---

## Documentación Java Spring Boot

> ¿Quieres saber más? Mira la documentación completa en la [Wiki del proyecto](https://github.com/Carri1x/libitum.wiki.git)

- [🔐 Autenticación JWT](https://github.com/Carri1x/libitum/wiki/Autenticaci%C3%B3n)
