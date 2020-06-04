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

package me.thevipershow.koranplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import me.thevipershow.koranplugin.data.DataManager;
import me.thevipershow.koranplugin.guards.Guard;
import me.thevipershow.koranplugin.structure.Koran;
import me.thevipershow.koranplugin.structure.LANG;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

@CommandAlias("koran|koran-plugin|krn")
@Description("The main command of Koran Plugin")
public final class KoranCommand extends BaseCommand {
    private final static String PREFIX = "&8[&eKoranPlugin&8]&f: ";
    private static KoranCommand instance = null;
    private final DataManager dataManager;
    private final JavaPlugin plugin;

    private KoranCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataManager = DataManager.getInstance(plugin);
    }

    public static KoranCommand getInstance(JavaPlugin plugin) {
        return instance != null ? instance : (instance = new KoranCommand(plugin));
    }

    private void sendMessage(CommandSender sender, String s) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes('&', PREFIX + s)
        );
    }

    @Subcommand("help")
    @CommandAlias("h")
    @CommandPermission("koran.commands.help")
    public void withoutArg(CommandSender sender) {
        sendMessage(sender, "&7This commands allows to download a Koran");
        sendMessage(sender, "&7/&ekoran download &8[&7koran&8]");
        sendMessage(sender, "&7This commands allows to load a Koran");
        sendMessage(sender, "&7/&ekoran load &8[&7koran&8]");
        sendMessage(sender, "&7This commands allows to list every downloaded koran");
        sendMessage(sender, "&7/&ekoran downloaded");
        sendMessage(sender, "&7This commands allows to read a \"āya\" from a Koran");
        sendMessage(sender, "&7/&ekoran read &8[&7koran&8] &8[&7sūre&8] &8[&7āya&8]");
    }

    @Subcommand("download")
    @Syntax("<koran> &8- &7Download a Koran.")
    @CommandCompletion("@available")
    @CommandPermission("koran.commands.download")
    public void onDownload(CommandSender sender, String arg) {
        try {
            sendMessage(sender, "&7Starting download. . .");
            long then = System.currentTimeMillis();
            dataManager.downloadKoran(LANG.valueOf(arg.toUpperCase(Locale.ROOT)))
                    .thenAcceptAsync((empty) -> {
                        long now = (System.currentTimeMillis() - then) / 1_000_000;
                        sendMessage(sender, "&7Download has completed in &e" + now + " &7ms");
                    });
        } catch (IllegalArgumentException e) {
            sendMessage(sender, "&7The name of the Koran was invalid.");
        } catch (RuntimeException e) {
            sendMessage(sender, "&7" + e.getMessage());
        }
    }

    @Subcommand("available")
    @Syntax("&8- &7Get all available Korans.")
    @CommandCompletion("@available")
    @CommandPermission("koran.commands.available")
    public void onAvailable(CommandSender sender) {
        sendMessage(sender, "&e" + Arrays.stream(LANG.values()).map(LANG::getAbbrev).collect(Collectors.joining("&7, &e")));
    }

    @Subcommand("downloaded")
    @Syntax("&8- &7Get all the downloaded Korans.")
    @CommandPermission("koran.commands.downloaded")
    public void onDownloaded(CommandSender sender) {
        sendMessage(sender, "&e" + dataManager.getLoadedKoran());
    }

    @Subcommand("read")
    @Syntax("&8[&7Koran&8] &8[&7sūre&8] &8[&7āya&8] &8- &7Make a research in the Koran.")
    @CommandPermission("koran.commands.read")
    @CommandCompletion("@available @range:1-114 @range:1-125")
    public void onRead(CommandSender sender, String koran, Integer surah, Integer aya) {
        try {
            LANG lang = Guard.toLang(koran);
            Optional<Koran> koranOptional = dataManager.getKoran(lang);
            if (koranOptional.isPresent()) {
                Koran k = koranOptional.get();
                String s = Guard.searchAya(k, surah, aya);
                sendMessage(sender, "&7" + s);
            } else {
                sendMessage(sender, "&7Not found.");
            }
        } catch (IllegalArgumentException e) {
            sendMessage(sender, e.getMessage());
        }
    }

    @Subcommand("load")
    @Syntax("&8[&7Koran&8] &8- &7Load a Koran into the plugin.")
    @CommandPermission("koran.commands.load")
    @CommandCompletion("@available")
    public void onLoad(CommandSender sender, String koran) {
        try {
            sendMessage(sender, "&7Loading Koran . . . ");
            LANG lang = Guard.toLang(koran);
            dataManager.loadKoran(lang);
            sendMessage(sender, "&7Successfully loaded Koran.");
        } catch (RuntimeException e) {
            sendMessage(sender, "&7" + e.getMessage());
        }
    }

    @Subcommand("loaded")
    @Syntax("&8- &7Look at all the loaded Korans.")
    @CommandPermission("koran.commands.loaded")
    public void onLoaded(CommandSender sender) {
        sendMessage(sender, "&e" + dataManager.getKoranConcurrentList().stream().map(koran -> koran.getLanguage().getAbbrev()).collect(Collectors.joining("&7, &e")));
    }
}
