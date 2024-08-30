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


// TODO. The idea is to separate handler and manager. Packets should be handled by handler.
class PacketHandler(
    private val context: GameContext
) {
    fun handlePacket(client: GameClient, packet: GameClientPacket) {
        with(packet) {
            when (this) {
                is Connected -> context.authHandler.onPlayerConnected(client)
                is Disconnected -> context.authHandler.onPlayerDisconnected(client)
                is RequestAuthLogin -> context.authHandler.requestAuthLogin(client, account, loginKey1, loginKey2, playKey1, playKey2)
                is RequestProtocolVersion -> context.authHandler.requestProtocolVersion(client, version)
                is RequestCharacterTemplates -> context.authHandler.requestCharacterTemplates(client)
                is RequestCreateCharacter -> context.authHandler.requestCreateChar(client, name, classId, race, sex, hairStyle, hairColor, face)
                is RequestSelectCharacter -> context.authHandler.requestSelectChar(client, slotIndex)
                else -> handlePacketWithPlayer(client, packet)
            }
        }
    }

    private fun handlePacketWithPlayer(client: GameClient, packet: GameClientPacket){
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
                    context.gameWorld.onPlayerEnterWorld(client, player)
                }
                is RequestQuit -> {
                    context.gameWorld.onPlayerQuit(client, player)
                }

                is RequestRestart -> {
                    context.gameWorld.onPlayerRestart(client, player)
                }

                is RequestAction -> {
                    context.requestActionHandler.onPlayerAction(player, actionObjectId)
                    client.sendPacket(ActionFailed.STATIC_PACKET)
                }

                is RequestAttack -> {
                    context.requestActionHandler.onPlayerAction(player, objectId)
                    player.sendPacket(ActionFailed.STATIC_PACKET)
                }

                is RequestCancelTarget -> {
                    context.requestActionHandler.onPlayerCancelAction(player, unselect)
                }

                is RequestDropItem -> {
                    context.requestItemHandler.onPlayerDropItem(player, objectId, count, x, y, z)
                }

                is RequestItemList -> {
                    context.requestItemHandler.onPlayerRequestInventory(player)
                }

                is RequestManorList -> {
                    context.manorManager.onPlayerRequestList(player)
                }

                is RequestMoveToLocation -> {
                    val destination = Position(targetX, targetY, targetZ)
                    context.requestMovementHandler.onPlayerStartMovement(player, destination)
                }

                is RequestQuestList -> {
                    client.sendPacket(QuestList())
                }

                is RequestSay2 -> {
                    context.requestChatHandler.playerSay(player, text, typeId, targetName)
                }

                is RequestUseItem -> {
                    context.requestItemHandler.onPlayerUseItem(player, objectId, ctrlPressed)
                }

                is RequestValidatePosition -> {
                    player.clientPosition = SpawnPosition(currentX, currentY, currentZ, heading)
                }
            }
        }
    }
}