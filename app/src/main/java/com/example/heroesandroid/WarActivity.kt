package com.example.heroesandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.heroesandroid.client.commands.CommandFactory
import com.example.heroesandroid.gui.GetASCIIUnit
import com.example.heroesandroid.heroes.auxiliaryclasses.ActionTypes
import com.example.heroesandroid.heroes.clientserver.ClientsConfigs
import com.example.heroesandroid.heroes.clientserver.Data
import com.example.heroesandroid.heroes.clientserver.Deserializer
import com.example.heroesandroid.heroes.commands.CommonCommands
import com.example.heroesandroid.heroes.controller.AutoController
import com.example.heroesandroid.heroes.controller.IController
import com.example.heroesandroid.heroes.gamelogic.Board
import com.example.heroesandroid.heroes.gamelogic.Fields
import com.example.heroesandroid.heroes.gamelogic.GameStatus
import com.example.heroesandroid.heroes.gui.IGUI
import com.example.heroesandroid.heroes.player.Answer
import com.example.heroesandroid.heroes.player.BaseBot
import com.example.heroesandroid.heroes.player.botdimon.Dimon
import com.example.heroesandroid.heroes.player.botgleb.ExpectiMaxBot
import com.example.heroesandroid.heroes.player.botnikita.NikitaBot
import com.example.heroesandroid.heroes.player.controlsystem.Selector
import com.example.heroesandroid.heroes.units.Unit
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Semaphore


class WarActivity: Activity() {
    private val logger = LoggerFactory.getLogger(WarActivity::class.java)

    lateinit var leftPlayerArmy: Array<TextView>
    lateinit var rightPlayerArmy: Array<TextView>
    lateinit var clientsConfigs: ClientsConfigs
    lateinit var bot: BaseBot
    var gui: IGUI? = null
    lateinit var controller: IController
    lateinit var typeBot: String
    lateinit var whoAmI: TextView
    lateinit var roundInfo: TextView
    lateinit var mContinue: Button
    lateinit var mSyncCommand: CountDownLatch
    lateinit var mSyncThread: Semaphore
    lateinit var mThread: Thread
    lateinit var mDecorView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_war)

        mSyncCommand = CountDownLatch(1)
        mSyncThread = Semaphore(1)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        typeBot = intent.extras!!.getString("botname")!!
        val room = Integer.parseInt(intent.extras!!.getString("room")!!)
        val field = Integer.parseInt(intent.extras!!.getString("field")!!)

        val factory:BaseBot.BaseBotFactory = when (typeBot) {
            "gleb" -> ExpectiMaxBot.ExpectiMaxBotFactory()
            "nikita" -> NikitaBot.NikitaBotFactory()
            else -> Dimon.DimonFactory()
        }
        whoAmI = findViewById(R.id.playerInfo)
        roundInfo = findViewById(R.id.round_info)

        gui = GUI()
        clientsConfigs = ClientsConfigs(8081, "", "", "EXPONENT_FUNCTION_V2",
        3, false, "Dimon", room, field, "false", "const 1",
            "EXPECTI_SIMULATION")

        bot = factory.createBotWithConfigs(Fields.PLAYER_ONE, clientsConfigs)
        controller = AutoController(clientsConfigs)

        mContinue = findViewById(R.id.continueBtn)
        mContinue.setOnClickListener {
            roundInfo.text = "Раунд: 0 / 10"
            mContinue.isEnabled = false
        }
        mContinue.isEnabled = true
        mContinue.isEnabled = false

        leftPlayerArmy = arrayOf(
            findViewById(R.id.unit_l_01),
            findViewById(R.id.unit_l_11),
            findViewById(R.id.unit_l_21),
            findViewById(R.id.unit_l_00),
            findViewById(R.id.unit_l_10),
            findViewById(R.id.unit_l_20),
        )

        rightPlayerArmy = arrayOf(
            findViewById(R.id.unit_r_01),
            findViewById(R.id.unit_r_11),
            findViewById(R.id.unit_r_21),
            findViewById(R.id.unit_r_00),
            findViewById(R.id.unit_r_10),
            findViewById(R.id.unit_r_20),
        )

        mDecorView = window.decorView

        mThread = Thread {
            while (!Connection.isClosed()) {
                mSyncThread.acquire()
                if (Connection.inBuf!!.ready() && !mContinue.isEnabled) {
                    mSyncCommand = CountDownLatch(1)
                    val message: String = Connection.inBuf!!.readLine()
                    Log.d("CONNECTION", "============  ====$message")
                    val data = Deserializer.deserializeData(message)

                    val commandFactory = CommandFactory()
                    commandFactory.getCommand(data, Connection.outBuf, this).execute()
                    mSyncCommand.await()
                }
                mSyncThread.release()
            }
            finish()
        }
        mThread.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Connection.closeConnection()
    }

    fun getGUI(): IGUI? {
        return gui
    }

    fun getPlayer(): BaseBot? {
        mSyncCommand.countDown()
        return bot
    }

    fun runOnUi(job: Runnable) {
        runOnUiThread(job)
    }

    fun chooseBot(field: Fields) {
        runOnUi {
            bot.field = field
            mSyncCommand.countDown()
        }
        Log.d("BOT", bot.toString())
    }

    fun getBotType(): String {
        return typeBot
    }

    @JvmName("getController1")
    fun getController(): IController? {
        mSyncCommand.countDown()
        return controller
    }

    fun downService() {
        Connection.closeConnection()
        finish()
        mSyncCommand.countDown()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    inner class GUI: IGUI {
        override fun refresh() {
            TODO("Not yet implemented")
        }

        override fun start() {
            TODO("Not yet implemented")
        }

        override fun printPlayer(field: Fields?) {
            runOnUi{
                whoAmI.text = when (field) {
                    Fields.PLAYER_ONE -> "<<<<<< Вы слева"
                    else -> "Вы справа >>>>>>"
                }
                mSyncCommand.countDown()
            }
        }

        override fun drawBots(selector: Selector?) {
            mSyncCommand.countDown()
            TODO("Not yet implemented")
        }

        override fun drawWait() {
            mSyncCommand.countDown()
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
                        true -> getDrawable(R.drawable.attacker_active)
                        false -> getDrawable(R.drawable.attacker)
                    }
                    val armyTextView: Array<TextView>
                    val armyUnits: Array<Array<Unit>>

                    when (field) {
                        true -> {
                            armyUnits = armyOne!!
                            armyTextView = leftPlayerArmy
                        }
                        false -> {
                            armyUnits = armyTwo!!
                            armyTextView = rightPlayerArmy
                        }
                    }

                    for (i in 0..1) {
                        for (j in 0..2) {
                            if (!armyUnits[i][j].isAlive) continue
                            armyTextView[i * 3 + j].background = when(armyUnits[i][j].isActive) {
                                true -> getDrawable(R.drawable.attacking_unit_active)
                                false -> getDrawable(R.drawable.attacking_unit)
                            }
                        }
                    }
                }
                ActionTypes.DEFENSE -> {
                    attacker.background = getDrawable(R.drawable.defender)
                }
                ActionTypes.HEALING -> {
                    attacker.background = getDrawable(R.drawable.attacker)
                    defender.background = when (activeDef) {
                        true -> getDrawable(R.drawable.healing_active)
                        false -> getDrawable(R.drawable.healing)
                    }
                }
                else -> {
                    attacker.background = getDrawable(R.drawable.attacker)
                    if (defenderAlive) {
                        defender.background = when (activeDef) {
                            true -> getDrawable(R.drawable.attacking_unit_active)
                            false -> getDrawable(R.drawable.attacking_unit)
                        }
                    }
                    else {
                        defender.background = getDrawable(R.drawable.attacking_unit_dead)
                    }
                }
            }
        }

        private fun resetBorders() {
            for (i in 0..5) {
                leftPlayerArmy[i].setBackgroundResource(android.R.color.transparent)
                rightPlayerArmy[i].setBackgroundResource(android.R.color.transparent)
            }
        }

        private fun updateRound(board: Board?) {
            roundInfo.text = "Раунд: ${board!!.curNumRound} / 10"
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
                    Fields.PLAYER_ONE -> leftPlayerArmy
                    else -> rightPlayerArmy
                }
                val arr2 = when (answer.attacker.F()) {
                    Fields.PLAYER_TWO -> leftPlayerArmy
                    else -> rightPlayerArmy
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
                        leftPlayerArmy[i * 3 + j].setTextColor(armyOne[i][j].color)
                        leftPlayerArmy[i * 3 + j].text =
                            GetASCIIUnit.getASCII(armyOne[i][j].actionType) + "\n     HP:" +
                                    armyOne[i][j].currentHP
                        if (!armyOne[i][j].isAlive) {
                            leftPlayerArmy[i * 3 + j].background =
                                getDrawable(R.drawable.dead_unit)
                        }
                        if (armyOne[i][j].isActive) {
                            leftPlayerArmy[i * 3 + j].setBackgroundColor(
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
                        rightPlayerArmy[i * 3 + j].setTextColor(armyTwo[i][j].color)
                        rightPlayerArmy[i * 3 + j].text =
                            GetASCIIUnit.getASCII(armyTwo[i][j].actionType) + "\n     HP:" +
                                    armyTwo[i][j].currentHP
                        if (!armyTwo[i][j].isAlive) {
                            rightPlayerArmy[i * 3 + j].background =
                                getDrawable(R.drawable.dead_unit)
                        }
                        if (armyTwo[i][j].isActive) {
                            rightPlayerArmy[i * 3 + j].setBackgroundColor(
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
                mSyncCommand.countDown()
            }
        }

        override fun stop() {
            mSyncCommand.countDown()
        }

        override fun clear() {
            mSyncCommand.countDown()
        }

        override fun endGame(data: Data?) {
            mSyncCommand.countDown()
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
                        roundInfo.text = msg
                        Log.d("CONTINUE", mContinue.isEnabled.toString())
                        mContinue.isEnabled = true
                        Log.d("CONTINUE", mContinue.isEnabled.toString())
                        mSyncCommand.countDown()
                    }
                }
            }
        }

    }
}