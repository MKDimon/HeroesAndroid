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
import com.example.heroesandroid.gui.GUI
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

        gui = GUI(this)
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

}