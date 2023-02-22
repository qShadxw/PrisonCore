package uk.co.tmdavies.prisoncore.objects;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Config {


    private final String path;
    private final Plugin plugin;
    private final File file;
    private YamlConfiguration ymlFile;

    public Config(Class<? extends JavaPlugin> pluginClass, String path) {

        this.plugin = JavaPlugin.getPlugin(pluginClass);
        this.path = this.plugin.getDataFolder() + "/" + path;
        this.file = new File(this.path);

    }

    public Config(Class<? extends JavaPlugin> pluginClass, String path, boolean override) {

        this.plugin = JavaPlugin.getPlugin(pluginClass);
        this.path = (!override ? this.plugin.getDataFolder() + "/" : "./plugins/") + path;
        this.file = new File(this.path);

    }

    public Config(Class<? extends JavaPlugin> pluginClass, String path, boolean override, boolean preload) {

        this.plugin = JavaPlugin.getPlugin(pluginClass);
        this.path = (!override ? this.plugin.getDataFolder() + "/" : "./plugins/") + path;
        this.file = new File(this.path);

        if (preload) {
            boolean fileExists = this.create();
            this.load();
        }

    }

    public boolean create() {

        if (!plugin.getDataFolder().exists()) {
            boolean generalDir = plugin.getDataFolder().mkdirs();
        }

        boolean fileDir = file.getParentFile().mkdirs();

        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch(IOException e) {
                PrisonCore.logger.error(Logger.Reason.CONFIG, "&cError creating file: " + this.path);
                e.printStackTrace();
            }
        }

        return true;

    }

    public boolean exists() {
        return this.file.exists();
    }

    public String getPath() {
        return this.path;
    }

    public void save() {
        try {
            this.ymlFile.save(this.file);
        } catch (IOException e) {
            PrisonCore.logger.error(Logger.Reason.CONFIG, "&cError saving file: " + this.path);
            e.printStackTrace();
        }
    }

    public void load() {
        this.ymlFile = YamlConfiguration.loadConfiguration(this.file);
    }

    public void reload() {

        save();
        load();

    }

    public boolean delete() {

        return this.file.delete();

    }

    public void set(String path, Object value) {
        this.ymlFile.set(path, value);
        reload();
    }

    public void add(String path, Object value) {
        if (!this.contains(path)) {
            this.ymlFile.set(path, value);
        }
    }

    public Object get(String path) {
        return this.ymlFile.get(path);
    }

    public File getFile() {

        return this.file;

    }

    public Object getByDefault(String path, Object value) {
        this.add(path, value);
        return this.get(path);
    }

    public boolean contains(String path) {
        return this.ymlFile.contains(path);
    }

    public Set<String> getKeys(String path) {
        return this.getKeys(path, false);
    }

    public Set<String> getKeys(String path, boolean deep) {
        try {
            return Objects.requireNonNull(this.ymlFile.getConfigurationSection(path)).getKeys(deep);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean getBoolean(String path) {
        return this.ymlFile.getBoolean(path);
    }

    public boolean getBooleanByDefault(String path, boolean value) {
        this.add(path, value);
        return this.getBoolean(path);
    }

    public double getDouble(String path) {
        return this.ymlFile.getDouble(path);
    }

    public Double getDoubleByDefault(String path, Double value) {
        this.add(path, value);
        return this.getDouble(path);
    }

    public float getFloat(String path) {
        return Float.parseFloat(Objects.requireNonNull(this.ymlFile.getString(path)));
    }

    public Float getFloatByDefault(String path, Float value) {
        this.add(path, value);
        return this.getFloat(path);
    }

    public int getInt(String path) {
        return this.ymlFile.getInt(path);
    }

    public int getIntByDefault(String path, Integer value) {
        this.add(path, value);
        return this.getInt(path);
    }

    public String getString(String path) {
        return this.ymlFile.getString(path);
    }

    public String getStringByDefault(String path, String value) {
        this.add(path, value);
        return this.getString(path);
    }

    public List<String> getStringList(String path) {

        return getYML().getStringList(path);

    }

    public List<String> getColouredStringList(String path) {

        List<String> toReturn = new ArrayList<>();

        getStringList(path).forEach(string -> toReturn.add(Utils.Colour(string)));

        return toReturn;

    }

    public List<?> getList(String path) {

        return this.ymlFile.getList(path);

    }

    public String getColouredString(String path) {
        return Utils.Colour(this.getString(path));
    }

    public ItemStack getItemStack(String path) {
        return this.ymlFile.getItemStack(path);
    }

    public ItemStack getItemStackByDefault(String path, ItemStack value) {
        this.add(path, value);
        return this.getItemStack(path);
    }

    public YamlConfiguration getYML() {
        return this.ymlFile;
    }

    public void setYML(YamlConfiguration ymlFile) {
        this.ymlFile = ymlFile;
    }


}
