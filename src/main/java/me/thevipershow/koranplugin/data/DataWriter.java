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
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import me.thevipershow.koranplugin.structure.LANG;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class DataWriter {
    private static DataWriter instance = null;
    private final JavaPlugin plugin;

    private DataWriter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static synchronized DataWriter getInstance(JavaPlugin plugin) {
        if (instance == null)
            instance = new DataWriter(plugin);
        return instance;
    }

    public CompletableFuture<Void> writeFromURL(LANG lang) {
        File toWrite = DataManager.langToFile(lang, plugin);
        if (!toWrite.exists())
            toWrite.mkdirs();
        return CompletableFuture.runAsync(()->{
            try {
                FileUtils.copyURLToFile(new URL(lang.getUrl()), toWrite, 3,3);
            } catch (IOException e) {
                plugin.getLogger().warning("Something has went wrong when reading Koran '" + lang.getAbbrev() + "'");
                e.printStackTrace();
            }
        });
    }
}