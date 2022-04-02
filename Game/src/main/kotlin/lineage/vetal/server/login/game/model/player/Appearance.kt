package lineage.vetal.server.login.game.model.player

class Appearance(var face: Byte, var hairColor: Byte, var hairStyle: Byte, var sex: Sex) {
    var isVisible = Boolean
    var nameColor = 0xFFFFFF
    var titleColor = 0xFFFF77

    fun setFace(value: Int) {
        face = value.toByte()
    }

    fun setHairColor(value: Int) {
        hairColor = value.toByte()
    }

    fun setHairStyle(value: Int) {
        hairStyle = value.toByte()
    }

    fun setNameColor(red: Int, green: Int, blue: Int) {
        nameColor = (red and 0xFF) + (green and 0xFF shl 8) + (blue and 0xFF shl 16)
    }

    fun setTitleColor(red: Int, green: Int, blue: Int) {
        titleColor = (red and 0xFF) + (green and 0xFF shl 8) + (blue and 0xFF shl 16)
    }
}