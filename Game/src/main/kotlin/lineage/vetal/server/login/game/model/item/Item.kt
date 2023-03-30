package lineage.vetal.server.login.game.model.item

import lineage.vetal.server.login.game.model.GameObject
import lineage.vetal.server.login.game.model.position.SpawnPosition

abstract class Item : GameObject(0, "NoName", SpawnPosition.zero)