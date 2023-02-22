package uk.co.tmdavies.prisoncore.objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import uk.co.tmdavies.prisoncore.managers.ChatManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

}
