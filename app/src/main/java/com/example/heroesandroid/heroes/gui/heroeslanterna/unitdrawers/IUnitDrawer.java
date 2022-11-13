package com.example.heroesandroid.heroes.gui.heroeslanterna.unitdrawers;

import com.googlecode.lanterna.graphics.TextImage;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.mathutils.Pair;

/**
 * Интерфейс для группы классов, рисующих юнитов.
 */
public interface IUnitDrawer {
    /**
     * Формирует TextImage, вспомогательную конструкцию Лантерны для более удобного расположения элементов.
     *
     * @param isGeneral boolean параметр, указывающий, является ли переданный на отрисовку юнит генералом или нет.
     *                  Юниты-генералы на поле боя отмечаются особыми цветами некоторых элементов.
     * @return TextImage, который можно удобно поместить в терминал при помощи одной команды.
     */
    TextImage formTextImage(final boolean isGeneral);

    /**
     * Рисует юнита на поле боя. По сути, размещает сформированный TextImage на терминале.
     *
     * @param tw            экземпляр класса LanternaWrapper для обращения непосредственно к Screen
     * @param topLeftCorner координаты верхнего левого угла для размещения рисунка юнита.
     * @param isGeneral     boolean параметр, указывающий, является ли переданный на отрисовку юнит генералом или нет.
     */
    void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner,
              final boolean isGeneral);
}
