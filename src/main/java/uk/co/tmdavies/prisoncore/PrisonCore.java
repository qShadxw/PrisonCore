package uk.co.tmdavies.prisoncore;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.prisoncore.commands.CoreCommand;
import uk.co.tmdavies.prisoncore.econ.Econ;
import uk.co.tmdavies.prisoncore.listeners.*;
import uk.co.tmdavies.prisoncore.objects.Config;
import uk.co.tmdavies.prisoncore.objects.Logger;
import uk.co.tmdavies.prisoncore.objects.Profile;

import java.util.HashMap;

public final class PrisonCore extends JavaPlugin {

    public static HashMap<Player, Profile> playerProfiles;
    public static boolean papiEnabled;
    public static Permission perms;
    public static Economy econ = null;
    public static Logger logger;
    public static Config itemCache;
    public static Config profileCache;

    @Override
    public void onLoad() {
        logger = new Logger();
        saveDefaultConfig();
        playerProfiles = new HashMap<>();
        itemCache = new Config(PrisonCore.class, "itemcache.yml", false, true);
        profileCache = new Config(PrisonCore.class, "profiles.yml", false, true);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        new CoreCommand(this);
        new ChatListener(this);
        new JoinListener(this);
        new ItemListener(this);
        new BlockListener(this);
        new InteractListener(this);
        new Econ(this);

        if (!setupEconomy() ) {
            Bukkit.getServer().getConsoleSender().sendMessage("Missing Dependency Vault.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupPermissions();


        papiEnabled = (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) { return false; }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) { return false; }
        econ = rsp.getProvider();
        return true;
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        assert rsp != null;
        perms = rsp.getProvider();
    }

}
