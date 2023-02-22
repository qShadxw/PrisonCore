package uk.co.tmdavies.prisoncore.objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.PrisonCore;
import uk.co.tmdavies.prisoncore.managers.ChatManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {

    private final Player player;
    private List<ChatManager.Channel> activeChannels;
    private HashMap<Enchantment, Integer> currentEnchantments;
    private HashMap<Material, Integer> absorbedBlocks;

    public Profile(Player player) {

        this.player = player;
        this.activeChannels = new ArrayList<>();
        this.currentEnchantments = new HashMap<>();
        this.absorbedBlocks = new HashMap<>();

        this.activeChannels.addAll(List.of(ChatManager.Channel.values()));

        loadData();

    }

    /**
     *
     * Adds the channel to the player's
     * active channels.
     *
     * @param channel Adding Channel.
     */
    public void addActiveChannel(ChatManager.Channel channel) {

        if (this.activeChannels.contains(channel)) return;

        this.activeChannels.add(channel);

    }

    /**
     *
     * Removes the channel from the player's
     * active channels.
     *
     * @param channel Removing Channel.
     */
    public void removeActiveChannel(ChatManager.Channel channel) {

        if (!this.activeChannels.contains(channel)) return;

        this.activeChannels.remove(channel);

    }

    /**
     *
     * Returns all the player's active channels.
     *
     * @return Active Channels List.
     */
    public List<ChatManager.Channel> getActiveChannels() {

        return this.activeChannels;

    }

    /**
     *
     * Adds an enchantment to the player.
     *
     * @param enchantment Enchantment.
     * @param level Integer.
     */
    public void addEnchantment(Enchantment enchantment, int level) {

        if (this.currentEnchantments.containsKey(enchantment)) return;

        this.currentEnchantments.put(enchantment, level);

    }

    /**
     *
     * Removes an enchantment from the player.
     *
     * @param enchantment Enchantment.
     */
    public void removeEnchantment(Enchantment enchantment) {

        if (!this.currentEnchantments.containsKey(enchantment)) return;

        this.currentEnchantments.remove(enchantment);

    }

    /**
     *
     * Edit a current enchantment the player has.
     *
     * @param enchantment Enchantment.
     * @param level Integer.
     */
    public void changeEnchantment(Enchantment enchantment, int level) {

        if (!this.currentEnchantments.containsKey(enchantment)) return;

        this.currentEnchantments.replace(enchantment, level);

    }

    /**
     *
     * Returns if the player has an enchantment.
     *
     * @param enchantment Enchantment.
     * @return Boolean.
     */
    public boolean hasEnchantment(Enchantment enchantment) {

        return this.currentEnchantments.containsKey(enchantment);

    }

    /**
     *
     * Returns the enchantments the player current has.
     *
     * @return HashMap< Enchantment, Integer >.
     */
    public HashMap<Enchantment, Integer> getCurrentEnchantments() {

        return this.currentEnchantments;

    }



    /**
     *
     * Returns the absorbed Blocks the player current has.
     *
     * @return HashMap< Material, Integer >.
     */
    public HashMap<Material, Integer> getAbsorbedBlocks() {

        return this.absorbedBlocks;

    }

    /**
     *
     * Adds an absorbed Block to the player.
     *
     * @param material material.
     * @param amount Integer.
     */
    public void addAbsorbedBlock(Material material, int amount) {

        if (this.absorbedBlocks.containsKey(material)) {
            this.absorbedBlocks.replace(material, this.absorbedBlocks.get(material) + amount);
            return;
        }

        this.absorbedBlocks.put(material, amount);

    }

    /**
     *
     * Saves data which is stored in ram.
     *
     */
    public void saveData() {

        List<String> toSave = new ArrayList<>();
        for (ChatManager.Channel channel : this.activeChannels)
            toSave.add(String.valueOf(channel.getId()));
        PrisonCore.profileCache.set("Profiles." + this.player.getUniqueId().toString() + ".Channels", toSave);

        toSave.clear();

        for (Map.Entry entry : this.currentEnchantments.entrySet())
            toSave.add(((Enchantment) entry.getKey()).getKey().getKey() + ":" + entry.getValue());
        PrisonCore.profileCache.set("Profiles." + this.player.getUniqueId().toString() + ".Enchantments", toSave);

        toSave.clear();

        for (Map.Entry entry : this.absorbedBlocks.entrySet())
            toSave.add(((Material) entry.getKey()).getKey().getKey() + ":" + entry.getValue());
        PrisonCore.profileCache.set("Profiles." + this.player.getUniqueId().toString() + ".AbsorbsBlocks", toSave);

        toSave.clear();

    }

    /**
     *
     * Loads data from the cache file
     * if there is any.
     *
     */
    public void loadData() {

        if (PrisonCore.profileCache.get("Profiles." + this.player.getUniqueId().toString()) == null) return;

        for (String string : PrisonCore.profileCache.getStringList("Profiles." + this.player.getUniqueId().toString() + ".Channels")) {

            this.activeChannels.add(ChatManager.Channel.getById(Integer.parseInt(string)));

        }

        for (String string : PrisonCore.profileCache.getStringList("Profiles." + this.player.getUniqueId().toString() + ".Enchantments")) {

            String[] enchantmentSplit = string.split(";");
            Enchantment enchantment = null;

            for (Enchantment enchant : Enchantment.values())
                if (enchant.getKey().getKey().equals(enchantmentSplit[0])) enchantment = enchant;

            if (enchantment == null) continue;

            int level = Integer.parseInt(enchantmentSplit[1]);

            this.currentEnchantments.put(enchantment, level);

        }

        for (String string : PrisonCore.profileCache.getStringList("Profiles." + this.player.getUniqueId().toString() + ".AbsorbsBlocks")) {

            String[] blocksSplit = string.split(";");
            Material material = null;

            for (Material mat : Material.values())
                if (mat.getKey().getKey().equals(blocksSplit[0])) material = mat;

            if (material == null) continue;

            int amount = Integer.parseInt(blocksSplit[1]);

            this.absorbedBlocks.put(material, amount);

        }

    }

}
