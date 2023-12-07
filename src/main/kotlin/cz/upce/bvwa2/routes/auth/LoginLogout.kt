package cz.upce.bvwa2.routes.auth

import io.ktor.resources.*

@Resource("/login")
class Login

@Resource("/logout")
class Logout

@Resource("/refresh")
class Refresh