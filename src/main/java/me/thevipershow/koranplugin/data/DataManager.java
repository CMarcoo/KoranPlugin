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

package me.thevipershow.koranplugin.data;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import me.thevipershow.koranplugin.structure.Koran;
import me.thevipershow.koranplugin.structure.LANG;
import org.bukkit.plugin.java.JavaPlugin;

public final class DataManager {
    private final HashSet<Koran> koranHashSet = new HashSet<>();
    private final JavaPlugin plugin;
    private static DataManager instance = null;
    private final DataWriter dataWriter;
    private final DataReader dataReader;

    private DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        dataReader = DataReader.getInstance(plugin);
        dataWriter = DataWriter.getInstance(plugin);
    }

    public static DataManager getInstance(JavaPlugin plugin) {
        return instance != null ? instance : (instance = new DataManager(plugin));
    }

    public boolean addKoran(Koran koran) {
        return koranHashSet.add(koran);
    }

    public Optional<Koran> getKoran(LANG lang) {
        return koranHashSet.stream()
                .filter(koran -> koran.getLanguage() == lang)
                .findFirst();
    }

    public static File langToFile(LANG lang, JavaPlugin plugin) {
        return new File(plugin.getDataFolder(), lang.getAbbrev() + "_koran.json");
    }

    public HashSet<Koran> getKoranHashSet() {
        return koranHashSet;
    }
}
