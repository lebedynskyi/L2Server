package lineage.vetal.server.game.game.model.player.status

import lineage.vetal.server.game.game.model.template.pc.CharTemplate

abstract class PlayableStatus(
    private val template: CharTemplate
) : CreatureStats(template)