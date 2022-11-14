package com.example.heroesandroid.gui

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import com.example.heroesandroid.R
import com.example.heroesandroid.WarActivity
import com.example.heroesandroid.heroes.auxiliaryclasses.ActionTypes
import com.example.heroesandroid.heroes.clientserver.Data
import com.example.heroesandroid.heroes.commands.CommonCommands
import com.example.heroesandroid.heroes.gamelogic.Board
import com.example.heroesandroid.heroes.gamelogic.Fields
import com.example.heroesandroid.heroes.gamelogic.GameStatus
import com.example.heroesandroid.heroes.gui.IGUI
import com.example.heroesandroid.heroes.player.Answer
import com.example.heroesandroid.heroes.player.controlsystem.Selector
import com.example.heroesandroid.heroes.units.Unit

class GUI constructor(var activity: WarActivity): IGUI {

    private fun runOnUi(job: Runnable) {
        activity.runOnUi(job)
    }

    override fun refresh() {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun printPlayer(field: Fields?) {
        runOnUi{
            activity.whoAmI.text = when (field) {
                Fields.PLAYER_ONE -> "<<<<<< Вы слева"
                else -> "Вы справа >>>>>>"
            }
            activity.mSyncCommand.countDown()
        }
    }

    override fun drawBots(selector: Selector?) {
        activity.mSyncCommand.countDown()
        TODO("Not yet implemented")
    }

    override fun drawWait() {
        activity.mSyncCommand.countDown()
        TODO("Not yet implemented")
    }

    private fun drawUnit() {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun drawAction(action: ActionTypes, attacker: TextView, defender: TextView,
                           activeDef: Boolean, defenderAlive: Boolean,
                           activeAttacker: Boolean, field: Boolean,
                           armyOne: Array<Array<Unit>>?, armyTwo: Array<Array<Unit>>?) {
        when (action) {
            ActionTypes.AREA_DAMAGE -> {
                attacker.background = when (activeAttacker) {
                    true -> activity.getDrawable(R.drawable.attacker_active)
                    false -> activity.getDrawable(R.drawable.attacker)
                }
                val armyTextView: Array<TextView>
                val armyUnits: Array<Array<Unit>>

                when (field) {
                    true -> {
                        armyUnits = armyOne!!
                        armyTextView = activity.leftPlayerArmy
                    }
                    false -> {
                        armyUnits = armyTwo!!
                        armyTextView = activity.rightPlayerArmy
                    }
                }

                for (i in 0..1) {
                    for (j in 0..2) {
                        if (!armyUnits[i][j].isAlive) continue
                        armyTextView[i * 3 + j].background = when(armyUnits[i][j].isActive) {
                            true -> activity.getDrawable(R.drawable.attacking_unit_active)
                            false -> activity.getDrawable(R.drawable.attacking_unit)
                        }
                    }
                }
            }
            ActionTypes.DEFENSE -> {
                attacker.background = activity.getDrawable(R.drawable.defender)
            }
            ActionTypes.HEALING -> {
                attacker.background = activity.getDrawable(R.drawable.attacker)
                defender.background = when (activeDef) {
                    true -> activity.getDrawable(R.drawable.healing_active)
                    false -> activity.getDrawable(R.drawable.healing)
                }
            }
            else -> {
                attacker.background = activity.getDrawable(R.drawable.attacker)
                if (defenderAlive) {
                    defender.background = when (activeDef) {
                        true -> activity.getDrawable(R.drawable.attacking_unit_active)
                        false -> activity.getDrawable(R.drawable.attacking_unit)
                    }
                }
                else {
                    defender.background = activity.getDrawable(R.drawable.attacking_unit_dead)
                }
            }
        }
    }

    private fun resetBorders() {
        for (i in 0..5) {
            activity.leftPlayerArmy[i].setBackgroundResource(android.R.color.transparent)
            activity.rightPlayerArmy[i].setBackgroundResource(android.R.color.transparent)
        }
    }

    private fun updateRound(board: Board?) {
        activity.roundInfo.text = "Раунд: ${board!!.curNumRound} / 10"
    }

    private fun updateAnswers(answer: Answer?, armyOne: Array<Array<Unit>>?, armyTwo: Array<Array<Unit>>?) {
        if (answer != null && armyOne != null && armyTwo != null) {
            val isActiveDef = when (answer.defender.F()) {
                Fields.PLAYER_ONE ->
                    armyOne[answer.defender.X()][answer.defender.Y()].isActive
                else ->
                    armyTwo[answer.defender.X()][answer.defender.Y()].isActive
            }
            val isActiveAtc = when (answer.attacker.F()) {
                Fields.PLAYER_ONE ->
                    armyOne[answer.attacker.X()][answer.attacker.Y()].isActive
                else ->
                    armyTwo[answer.attacker.X()][answer.attacker.Y()].isActive
            }
            val isAlive = when (answer.defender.F()) {
                Fields.PLAYER_ONE ->
                    armyOne[answer.defender.X()][answer.defender.Y()].isAlive
                else ->
                    armyTwo[answer.defender.X()][answer.defender.Y()].isAlive
            }

            val arr = when (answer.attacker.F()) {
                Fields.PLAYER_ONE -> activity.leftPlayerArmy
                else -> activity.rightPlayerArmy
            }
            val arr2 = when (answer.attacker.F()) {
                Fields.PLAYER_TWO -> activity.leftPlayerArmy
                else -> activity.rightPlayerArmy
            }
            val attacker = arr[answer.attacker.X() * 3 + answer.attacker.Y()]
            val defender = when (answer.actionType) {
                ActionTypes.HEALING -> arr[answer.defender.X() * 3 + answer.defender.Y()]
                ActionTypes.AREA_DAMAGE -> arr[0]
                else -> arr2[answer.defender.X() * 3 + answer.defender.Y()]
            }
            drawAction(answer.actionType, attacker, defender, isActiveDef, isAlive, isActiveAtc,
                answer.defender.F() == Fields.PLAYER_ONE ,armyOne, armyTwo)
        }
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun updateArmies(armyOne: Array<Array<Unit>>?, armyTwo: Array<Array<Unit>>?) {
        for (i in 0..1) {
            for (j in 0..2) {
                if (armyOne != null) {
                    activity.leftPlayerArmy[i * 3 + j].setTextColor(armyOne[i][j].color)
                    activity.leftPlayerArmy[i * 3 + j].text =
                        GetASCIIUnit.getASCII(armyOne[i][j].actionType) + "\n     HP:" +
                                armyOne[i][j].currentHP
                    if (!armyOne[i][j].isAlive) {
                        activity.leftPlayerArmy[i * 3 + j].background =
                            activity.getDrawable(R.drawable.dead_unit)
                    }
                    if (armyOne[i][j].isActive) {
                        activity.leftPlayerArmy[i * 3 + j].setBackgroundColor(
                            Color.argb(
                                0x30,
                                0,
                                0,
                                0xFF
                            )
                        )
                    }
                }
                if (armyTwo != null) {
                    activity.rightPlayerArmy[i * 3 + j].setTextColor(armyTwo[i][j].color)
                    activity.rightPlayerArmy[i * 3 + j].text =
                        GetASCIIUnit.getASCII(armyTwo[i][j].actionType) + "\n     HP:" +
                                armyTwo[i][j].currentHP
                    if (!armyTwo[i][j].isAlive) {
                        activity.rightPlayerArmy[i * 3 + j].background =
                            activity.getDrawable(R.drawable.dead_unit)
                    }
                    if (armyTwo[i][j].isActive) {
                        activity.rightPlayerArmy[i * 3 + j].setBackgroundColor(
                            Color.argb(
                                0x50,
                                0,
                                0,
                                0xFF
                            )
                        )
                    }
                }
            }
        }
    }

    override fun update(answer: Answer?, board: Board?) {
        if (board == null) return

        runOnUi {
            resetBorders()

            val armyOne = board.getArmy(Fields.PLAYER_ONE)
            val armyTwo = board.getArmy(Fields.PLAYER_TWO)

            updateArmies(armyOne, armyTwo)

            updateAnswers(answer, armyOne, armyTwo)

            updateRound(board)

            Log.d("DRAWING", "FINISHED")
            activity.mSyncCommand.countDown()
        }
    }

    override fun stop() {
        activity.mSyncCommand.countDown()
    }

    override fun clear() {
        activity.mSyncCommand.countDown()
    }

    override fun endGame(data: Data?) {
        activity.mSyncCommand.countDown()
    }

    override fun continueGame(data: Data?) {
        if (data!!.command == CommonCommands.CONTINUE_GAME) {
            val msg = when (data.board.status) {
                GameStatus.PLAYER_ONE_WINS -> "Left player wins"
                GameStatus.PLAYER_TWO_WINS -> "Right player wins"
                GameStatus.NO_WINNERS -> "No player wins"

                else -> ""
            }

            if (msg != "") {
                runOnUi{
                    activity.roundInfo.text = msg
                    Log.d("CONTINUE", activity.mContinue.isEnabled.toString())
                    activity.mContinue.isEnabled = true
                    Log.d("CONTINUE", activity.mContinue.isEnabled.toString())
                    activity.mSyncCommand.countDown()
                }
            }
        }
    }

}