package com.example.heroesandroid.gui

import android.os.Build
import com.example.heroesandroid.heroes.auxiliaryclasses.ActionTypes

object GetASCIIUnit {
    private var map = mapOf(
        ActionTypes.CLOSE_COMBAT to "        /  \\\n" +
                                    "        ||||\n" +
                                    "        ||||\n" +
                                    "        ||||\n" +
                                    "       |00|\n" +
                                    " ========\n" +
                                    "         ||\n" +
                                    "         ||",
        ActionTypes.HEALING to "        |+|\n" +
                               "        |+|\n" +
                               "        |+|\n" +
                               "+++++++++\n" +
                               "        |0|\n" +
                               "        |+|\n" +
                               "        |+|\n" +
                               "        |+|",
        ActionTypes.AREA_DAMAGE to "         ||\n" +
                                   "      _\\  /_\n" +
                                   "        /  \\\n" +
                                   "         ||\n" +
                                   "         ||\n" +
                                   "         ||\n" +
                                   "         ()\n" +
                                   "         ()",
        ActionTypes.RANGE_COMBAT to "    (\n" +
                                    "      \\\n" +
                                    "        )\n" +
                                    "##_____\\\n" +
                                    "##         /\n" +
                                    "        )\n" +
                                    "      /\n" +
                                    "    ("
    )

    fun getASCII(action: ActionTypes): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return map.getOrDefault(action, "")
        }
        return ""
    }
}