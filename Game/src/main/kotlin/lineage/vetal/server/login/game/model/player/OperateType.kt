package lineage.vetal.server.login.game.model.player

enum class OperateType(val id: Int) {
    NONE(0), SELL(1), SELL_MANAGE(2),
    BUY(3), BUY_MANAGE(4), MANUFACTURE(5),
    MANUFACTURE_MANAGE(6), OBSERVE(7), PACKAGE_SELL(8);
}