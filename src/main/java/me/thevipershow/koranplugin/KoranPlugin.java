package me.thevipershow.koranplugin;

import co.aikar.commands.BukkitCommandManager;
import java.util.Locale;
import me.thevipershow.koranplugin.data.DataManager;
import me.thevipershow.koranplugin.structure.LANG;
import org.bukkit.plugin.java.JavaPlugin;

public final class KoranPlugin extends JavaPlugin {

    private DataManager dataManager;
    private BukkitCommandManager commandManager = new BukkitCommandManager(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        dataManager = DataManager.getInstance(this);
        getConfig().getStringList("koran.load-default")
                .forEach(str -> {
                    LANG lang = LANG.valueOf(str.toUpperCase(Locale.ROOT));
                    // ahah bruh we ignoring exceptions;
                    // imagine if the plugin was that bad that I
                    // had to handle them.
                    dataManager.downloadKoran(lang);
                    dataManager.loadKoran(lang);
                });

    }
}
