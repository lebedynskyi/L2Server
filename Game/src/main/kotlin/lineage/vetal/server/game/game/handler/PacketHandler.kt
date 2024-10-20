package lineage.vetal.server.game.game.handler

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GameClientPacket
import lineage.vetal.server.game.gameserver.packet.client.*
import lineage.vetal.server.game.gameserver.packet.server.ActionFailed
import lineage.vetal.server.game.gameserver.packet.server.QuestList

private const val TAG = "PacketHandler"


class PacketHandler(
    private val context: GameContext
) {
    fun handlePacket(client: GameClient, packet: GameClientPacket) {
        with(packet) {
            when (this) {
                is Connected -> context.requestAuthHandler.onPlayerConnected(client)
                is Disconnected -> context.requestAuthHandler.onPlayerDisconnected(client)
                is RequestAuthLogin -> context.requestAuthHandler.requestAuthLogin(client, account, loginKey1, loginKey2, playKey1, playKey2)
                is RequestProtocolVersion -> context.requestAuthHandler.requestProtocolVersion(client, version)
                is RequestCharacterTemplates -> context.requestAuthHandler.requestCharacterTemplates(client)
                is RequestCreateCharacter -> context.requestAuthHandler.requestCreateChar(client, name, classId, race, sex, hairStyle, hairColor, face)
                is RequestSelectCharacter -> context.requestAuthHandler.requestSelectChar(client, slotIndex)
                else -> handleInGamePacket(client, packet)
            }
        }
    }

    private fun handleInGamePacket(client: GameClient, packet: GameClientPacket){
        val player = client.player
        if (player == null) {
            writeError(TAG, "No player attached for client $client during handling packet $packet")
            client.saveAndClose()
            return
        }

        with(packet) {
            when (this) {
                //TODO. Game world should became a manager. It is good to handle these request
                is RequestEnterWorld -> {
                    context.requestWorldHandler.onPlayerEnterWorld(client, player)
                }

                is RequestRestart -> {
                    context.requestWorldHandler.onPlayerRequestRestart(client, player)
                }

                is RequestQuit -> {
                    context.requestWorldHandler.onPlayerRequestQuit(client, player)
                }

                is RequestAction -> {
                    context.requestActionHandler.onPlayerAction(player, actionObjectId)
                    client.sendPacket(ActionFailed.STATIC_PACKET)
                }

                is RequestAttack -> {
                    context.requestActionHandler.onPlayerAction(player, objectId)
                    player.sendPacket(ActionFailed.STATIC_PACKET)
                }

                is RequestCancelAction -> {
                    context.requestActionHandler.onPlayerCancelAction(player, unselect)
                }

                is RequestItemList -> {
                    context.requestItemHandler.onPlayerRequestInventory(player)
                }

                is RequestUseItem -> {
                    context.requestItemHandler.onPlayerUseItem(player, objectId, ctrlPressed)
                }

                is RequestDropItem -> {
                    context.requestItemHandler.onPlayerDropItem(player, objectId, count, x, y, z)
                }

                is RequestMoveToLocation -> {
                    val destination = Position(targetX, targetY, targetZ)
                    context.requestMovementHandler.onPlayerStartMovement(player, destination)
                }

                is RequestValidatePosition -> {
                    player.clientPosition = SpawnPosition(currentX, currentY, currentZ, heading)
                }

                is RequestSay2 -> {
                    context.requestChatHandler.playerSay(player, text, typeId, targetName)
                }

                is RequestQuestList -> {
                    client.sendPacket(QuestList())
                }

                is RequestManorList -> {
                    context.manorManager.onPlayerRequestList(player)
                }
            }
        }
    }
}