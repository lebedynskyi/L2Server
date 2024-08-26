package lineage.vetal.server.game.game.handler

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket
import lineage.vetal.server.game.gameserver.packet.client.*
import lineage.vetal.server.game.gameserver.packet.server.ActionFailed
import lineage.vetal.server.game.gameserver.packet.server.NewCharacterSuccess
import lineage.vetal.server.game.gameserver.packet.server.QuestList

private const val TAG = "PacketHandler"

class PacketHandler(
    private val context: GameContext
) {
    fun handlePacket(client: GameClient, packet: GamePacket?) {
        with(packet) {
            when (this) {
                is RequestAuthLogin -> {
                    context.gameLobby.requestAuthLogin(client, account, loginKey1, loginKey2, playKey1, playKey2)
                }

                is RequestSelectCharacter -> {
                    context.gameLobby.requestSelectChar(client, slotIndex)
                }

                is RequestProtocolVersion -> {
                    context.gameLobby.requestProtocolVersion(client, version)
                }

                is RequestCharacterTemplates -> {
                    context.gameLobby.requestCharacterTemplates(client)
                }

                is RequestCreateCharacter -> {
                    context.gameLobby.requestCreateChar(client, name, classId, race, sex, hairStyle, hairColor, face)
                }

                else -> {
                    val player = client.player
                    if (player == null) {
                        writeError(TAG, "No player attached for client $client during handling packet $packet")
                        return
                    }

                    when (this) {
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

                        is RequestEnterWorld -> {
                            //TODO what ?
                            context.gameWorld.onPlayerEnterWorld(client, player)
                        }

                        is RequestItemList -> {
                            writeDebug(TAG, "RequestItemList Not implemented")
                        }

                        is RequestManorList -> {
                            context.manorManager.onPlayerRequestList(player)
                        }

                        is RequestMoveToLocation -> {
                            val destination = Position(targetX, targetY, targetZ)
                            context.behaviourManager.onPlayerStartMovement(player, destination)
                        }

                        is RequestQuestList -> {
                            client.sendPacket(QuestList())
                        }

                        is RequestQuit -> {
                            context.gameWorld.onPlayerQuit(client, player)
                        }

                        is RequestRestart -> {
                            context.gameWorld.onPlayerRestart(client, player)
                        }

                        is RequestSay2 -> {
                            context.requestChatHandler.playerSay(client, text, typeId, targetName)
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
    }
}