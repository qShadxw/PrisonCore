package uk.co.tmdavies.prisoncore;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.prisoncore.commands.CoreCommand;
import uk.co.tmdavies.prisoncore.listeners.*;
import uk.co.tmdavies.prisoncore.objects.Config;
import uk.co.tmdavies.prisoncore.objects.Logger;
import uk.co.tmdavies.prisoncore.objects.Profile;

import java.util.HashMap;

public final class PrisonCore extends JavaPlugin {

    public static HashMap<Player, Profile> playerProfiles;
    public static boolean papiEnabled;
    public static Permission perms;
    public static Logger logger;
    public static Config itemCache;

    @Override
    public void onLoad() {
        logger = new Logger();
        saveDefaultConfig();
        playerProfiles = new HashMap<>();
        itemCache = new Config(PrisonCore.class, "itemcache.yml", false, true);
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

        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        assert rsp != null; // Maybe properly handle this later (if vault doesn't exist)
        perms = rsp.getProvider();

        papiEnabled = (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null);
    }

}
