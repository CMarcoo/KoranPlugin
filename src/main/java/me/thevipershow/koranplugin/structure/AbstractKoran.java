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

package me.thevipershow.koranplugin.structure;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractKoran implements Koran {
    private final List<Surah> suwar;

    public AbstractKoran(List<Surah> suwar) {
        this.suwar = suwar;
    }

    public AbstractKoran(Surah... suwar) {
        this.suwar = Arrays.asList(suwar);
    }

    @Override
    public List<Surah> getSuwar() {
        return this.suwar;
    }

    @Override
    public int countOccurrences(String word) {
        return suwar.parallelStream()
                .mapToInt(suwar -> suwar.countOccurrences(word))
                .sum();
    }
}
