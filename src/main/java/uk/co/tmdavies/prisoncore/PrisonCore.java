package uk.co.tmdavies.prisoncore;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.prisoncore.commands.CoreCommand;
import uk.co.tmdavies.prisoncore.commands.CustomEnchantCommand;
import uk.co.tmdavies.prisoncore.commands.GangCommand;
import uk.co.tmdavies.prisoncore.econ.Econ;
import uk.co.tmdavies.prisoncore.listeners.*;
import uk.co.tmdavies.prisoncore.objects.Config;
import uk.co.tmdavies.prisoncore.objects.Gang;
import uk.co.tmdavies.prisoncore.objects.Logger;
import uk.co.tmdavies.prisoncore.objects.Profile;

import java.util.HashMap;

public final class PrisonCore extends JavaPlugin {

    public static HashMap<Player, Profile> playerProfiles;
    public static HashMap<OfflinePlayer, Gang> gangCache;
    public static boolean papiEnabled;
    public static Permission perms;
    public static Economy econ = null;
    public static Logger logger;
    public static Config itemConfig;
    public static Config profileConfig;
    public static Config gangConfig;


    @Override
    public void onLoad() {
        logger = new Logger();
        saveDefaultConfig();
        playerProfiles = new HashMap<>();
        gangCache = new HashMap<>();
        itemConfig = new Config(PrisonCore.class, "itemcache.yml", false, true);
        profileConfig = new Config(PrisonCore.class, "profiles.yml", false, true);
        gangConfig = new Config(PrisonCore.class, "gangs.yml", false, true);
    }

    @Override
    public void onEnable() {
        // Commands
        new CoreCommand(this);
        new CustomEnchantCommand(this);
        new GangCommand(this);
        // Listeners
        new ChatListener(this);
        new JoinListener(this);
        new ItemListener(this);
        new BlockListener(this);
        new InteractListener(this);

        // Block Absorber Runnable
        new Econ(this).runTaskTimerAsynchronously(this, 0L, (getConfig().getInt("absorb-able-blocks-interval") * 20L));

        if (!setupEconomy() ) {
            logger.error(Logger.Reason.ECONOMY, "Vault was not found. Please install Vault before using this plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupPermissions();

        papiEnabled = (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null);

        logger.startUp();

        // Do this last
        if (Bukkit.getOnlinePlayers().isEmpty()) return;

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (playerProfiles.containsKey(player)) continue;

            playerProfiles.put(player, new Profile(player));

        }

    }

    @Override
    public void onDisable() {

        if (!gangCache.isEmpty())
            for (Gang gang : gangCache.values()) gang.save();

        if (Bukkit.getOnlinePlayers().isEmpty()) return;

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!playerProfiles.containsKey(player)) continue;

            playerProfiles.get(player).saveData();
            playerProfiles.remove(player);

        }

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
