package me.thevipershow.koranplugin;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import me.thevipershow.koranplugin.commands.KoranCommand;
import me.thevipershow.koranplugin.data.DataManager;
import me.thevipershow.koranplugin.structure.LANG;
import org.bukkit.plugin.java.JavaPlugin;

public final class KoranPlugin extends JavaPlugin {

    private DataManager dataManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        dataManager = DataManager.getInstance(this);
        dataManager.makeDataFile();
        getConfig().getStringList("koran.load-default")
                .forEach(str -> {
                    LANG lang = LANG.valueOf(str.toUpperCase(Locale.ROOT));
                    try {
                        dataManager.downloadKoran(lang);
                        dataManager.loadKoran(lang);
                    } catch (RuntimeException e) {
                    }
                });
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions()
                .registerStaticCompletion("available",
                        Arrays.stream(LANG.values())
                                .map(LANG::getAbbrev)
                                .collect(Collectors.toList())
                );

        commandManager.registerCommand(KoranCommand.getInstance(this));
    }
}
