rootProject.name = "L2Server"

include("Login", "Game", "Shared", "Socks")
project(":Socks").projectDir = file("Socks/app")

