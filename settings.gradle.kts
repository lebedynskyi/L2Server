rootProject.name = "L2Server"

include("Login", "Game", "Shared", "Socks")
//project("Socks").projectDir = file(File(rootDir, "L2Server/Socks"))
project(":Socks").projectDir = file("Socks/app")

