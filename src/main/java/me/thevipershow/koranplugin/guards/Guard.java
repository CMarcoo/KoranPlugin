/*
 * KoranPlugin - A Minecraft plugin to interact with the Koran
 * Copyright (C) 2020 TheViperShow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.thevipershow.koranplugin.guards;

import me.thevipershow.koranplugin.structure.Koran;
import me.thevipershow.koranplugin.structure.LANG;
import me.thevipershow.koranplugin.structure.Surah;

public final class Guard {
    public static LANG toLang(String s) throws IllegalArgumentException {
        for (LANG value : LANG.values())
            if (value.getAbbrev().equalsIgnoreCase(s))
                return value;
        throw new IllegalArgumentException("Invalid Koran name.");
    }

    public static String searchAya(Koran koran, int a, int b) throws IllegalArgumentException {
        if (koran.getSuwar().size() >= a) {
            Surah surah = koran.getSuwar().get(a - 1);
            if (surah.getAja().size() >= b) {
                return surah.getAja().get(b - 1).getText();
            }
        }
        throw new IllegalArgumentException("The search is out of range.");
    }
}
