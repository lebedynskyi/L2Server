package lineage.vetal.server.game.game.handler

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GameClientPacket
import lineage.vetal.server.game.gameserver.packet.client.*
import lineage.vetal.server.game.gameserver.packet.server.ActionFailed
import lineage.vetal.server.game.gameserver.packet.server.QuestList

private const val TAG = "PacketHandler"

class GamePacketHandler(
    private val context: GameContext
) {
    fun handlePacket(client: GameClient, packet: GameClientPacket) {
        with(packet) {
            when (this) {
                is Connected -> context.requestAuthHandler.onPlayerConnected(client)
                is Disconnected -> context.requestAuthHandler.onPlayerDisconnected(client)
                is RequestAuthLogin -> context.requestAuthHandler.requestAuthLogin(
                    client, account, loginKey1, loginKey2, playKey1, playKey2
                )

                is RequestProtocolVersion -> context.requestAuthHandler.requestProtocolVersion(client, version)
                is RequestCharacterTemplates -> context.requestAuthHandler.requestCharacterTemplates(client)
                is RequestCreateCharacter -> context.requestAuthHandler.requestCreateChar(
                    client, name, classId, race, sex, hairStyle, hairColor, face
                )

                is RequestSelectCharacter -> context.requestAuthHandler.requestSelectChar(client, slotIndex)
                else -> handleInGamePacket(client, packet)
            }
        }
    }

    private fun handleInGamePacket(client: GameClient, packet: GameClientPacket) {
        val player = client.player
        if (player == null) {
            writeError(TAG, "No player attached for client $client during handling packet $packet")
            client.close()
            return
        }

        with(packet) {
            when (this) {
                is RequestEnterWorld -> {
                    context.requestWorldHandler.onRequestEnterWorld(client, player)
                }

                is RequestRestart -> {
                    context.requestWorldHandler.onRequestRestart(client, player)
                }

                is RequestQuit -> {
                    context.requestWorldHandler.onRequestQuit(client, player)
                }

                is RequestAction -> {
                    context.requestActionHandler.onRequestAction(player, actionObjectId)
                    client.sendPacket(ActionFailed.STATIC_PACKET)
                }

                is RequestAttack -> {
                    context.requestActionHandler.onRequestAction(player, objectId)
                    player.sendPacket(ActionFailed.STATIC_PACKET)
                }

                is RequestCancelAction -> {
                    context.requestActionHandler.onRequestCancelAction(player, unselect)
                }

                is RequestInventoryList -> {
                    context.requestInventoryHandler.onRequestInventoryList(player)
                }

                is RequestUseItem -> {
                    context.requestInventoryHandler.onRequestUseItem(player, objectId, ctrlPressed)
                }

                is RequestDropItem -> {
                    context.requestInventoryHandler.onRequestDropItem(player, objectId, count, x, y, z)
                }

                is RequestMoveToLocation -> {
                    context.requestMovementHandler.onRequestMoveTo(player, Position(targetX, targetY, targetZ))
                }

                is RequestValidatePosition -> {
                    context.requestMovementHandler.onRequestValidatePosition(
                        player, Position(currentX, currentY, currentZ), heading,
                    )
                }

                is RequestSay2 -> {
                    context.requestChatHandler.onRequestSay(player, text, sayTypeId, targetName)
                }

                is RequestQuestList -> {
                    client.sendPacket(QuestList())
                }

                is RequestManorList -> {
                    context.manorManager.onRequestManorList(player)
                }
            }
        }
    }
}