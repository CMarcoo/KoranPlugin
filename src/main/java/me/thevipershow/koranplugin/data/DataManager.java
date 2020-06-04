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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import me.thevipershow.koranplugin.factory.GSONKoranDeserializer;
import me.thevipershow.koranplugin.structure.Koran;
import me.thevipershow.koranplugin.structure.LANG;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class DataManager {
    private final CopyOnWriteArrayList<Koran> koranConcurrentList = new CopyOnWriteArrayList<>();
    private final JavaPlugin plugin;
    private static DataManager instance = null;
    private final DataWriter dataWriter;
    private final DataReader dataReader;
    private final GSONKoranDeserializer gsonKoranDeserializer;

    private DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        dataReader = DataReader.getInstance(plugin);
        dataWriter = DataWriter.getInstance(plugin);
        gsonKoranDeserializer = GSONKoranDeserializer.getInstance();
    }

    public static DataManager getInstance(JavaPlugin plugin) {
        return instance != null ? instance : (instance = new DataManager(plugin));
    }

    public boolean addKoran(Koran koran) {
        return koranConcurrentList.add(koran);
    }

    public Optional<Koran> getKoran(LANG lang) {
        return koranConcurrentList.stream()
                .filter(koran -> koran.getLanguage() == lang)
                .findAny();
    }

    public void makeDataFile() {
        File dataFile = new File(plugin.getDataFolder(), "data");
        if (!dataFile.exists())
            dataFile.mkdirs();
    }

    public String getLoadedKoran() {
        File dataFile = new File(plugin.getDataFolder(), "data");
        if (!dataFile.exists())
            return "none";
        return Arrays.stream(Objects.requireNonNull(dataFile.listFiles()))
                .map(file -> FilenameUtils.removeExtension(file.getName())).collect(Collectors.joining(", "));
    }

    public static File langToFile(LANG lang, JavaPlugin plugin) {
        return new File(plugin.getDataFolder(), "data" + File.separatorChar + lang.getAbbrev().toLowerCase(Locale.ROOT) + "_koran.json");
    }

    public List<Koran> getKoranConcurrentList() {
        return koranConcurrentList;
    }

    private boolean noDirFilesMatch(LANG lang) {
        File pluginDir = plugin.getDataFolder();
        if (!pluginDir.exists())
            return false;
        return Arrays.stream(Objects.requireNonNull(pluginDir.listFiles()))
                .noneMatch(file -> FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase(lang.getAbbrev()));
    }

    public CompletableFuture<Void> downloadKoran(LANG lang) throws RuntimeException {
        if (noDirFilesMatch(lang)) {
            try {
                return dataWriter.writeFromURL(lang);
            } catch (IOException yoException) {
                plugin.getLogger().warning("Something has gone wrong while downloading the resource.");
                yoException.printStackTrace();
            }
        }
        throw new RuntimeException("That koran has already been downloaded before.");
    }

    public void loadKoran(LANG lang) throws RuntimeException {
        if (koranConcurrentList.stream().noneMatch(koran -> koran.getLanguage() == lang)) {
            if (langToFile(lang, plugin).exists()) {
                dataReader.readData(lang).thenAcceptAsync(json -> {
                    if (json == null) {
                        throw new RuntimeException("The data couldn't be read correctly.");
                    } else {
                        gsonKoranDeserializer.createKoran(json, lang).thenAcceptAsync(koran -> {
                            if (koran == null)
                                throw new RuntimeException("Something has went wrong, Koran was null.");
                            else
                                this.koranConcurrentList.add(koran);
                        });
                    }
                });
            } else {
                throw new RuntimeException("That Koran has never been downloaded before.");
            }
        } else {
            throw new RuntimeException("That Koran has already been loaded.");
        }
    }
}
