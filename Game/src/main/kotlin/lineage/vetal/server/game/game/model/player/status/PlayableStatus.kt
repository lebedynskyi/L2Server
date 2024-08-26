package lineage.vetal.server.game.game.model.player.status

import lineage.vetal.server.game.game.model.template.pc.CharTemplate

abstract class PlayableStatus(
    template: CharTemplate
) : CreatureStats(template)