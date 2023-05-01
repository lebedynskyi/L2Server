package lineage.vetal.server.game.game.model.player

class Appearance(
    var face: Int,
    var hairColor: Int,
    var hairStyle: Int,
    var sex: CharacterSex
    ) {
    var isVisible = Boolean
    var nameColor = 0xFFFFFF
    var titleColor = 0xFFFF77

    fun setNameColor(red: Int, green: Int, blue: Int) {
        nameColor = (red and 0xFF) + (green and 0xFF shl 8) + (blue and 0xFF shl 16)
    }

    fun setTitleColor(red: Int, green: Int, blue: Int) {
        titleColor = (red and 0xFF) + (green and 0xFF shl 8) + (blue and 0xFF shl 16)
    }
}