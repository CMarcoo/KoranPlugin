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

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.thevipershow.koranplugin.structure.LANG;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class DataReader {
    public static DataReader instance = null;

    private final JavaPlugin plugin;

    public DataReader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static DataReader getInstance(JavaPlugin plugin) {
        return instance != null ? instance : (instance = new DataReader(plugin));
    }

    @SuppressWarnings("UnstableApiUsage")
    private String readFromFile(LANG lang) throws IOException {
        return FileUtils.readFileToString(DataManager.langToFile(lang, plugin), StandardCharsets.UTF_8);
    }

    public CompletableFuture<String> readData(LANG lang) {
        return CompletableFuture.supplyAsync(
                ()->{
                    try {
                        return readFromFile(lang);
                    } catch (IOException e) {
                        plugin.getLogger().warning("Something went wrong when reading from Koran '" + lang.getAbbrev() + "'.");
                        e.printStackTrace();
                    }
                    return null;
                }
        );
    }
}
