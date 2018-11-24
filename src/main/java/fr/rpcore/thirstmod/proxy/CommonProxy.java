/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.proxy;

import fr.rpcore.thirstmod.common.content.Drink;
import fr.rpcore.thirstmod.common.thirstlogic.ThirstStats;
import fr.rpcore.thirstmod.items.ItemDrink;
import fr.rpcore.thirstmod.items.ItemFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.UUID;

public class CommonProxy {
    public HashMap<UUID, ThirstStats> loadedPlayers = new HashMap();

    public static final ItemDrink DRINKS = new ItemDrink("drink_item");
    public static final Item FILTER = new ItemFilter("filter");

    public void preInit() {
        Drink.registerDrink(new Drink("Fresh Water", 7, 2.0F, 1171189));
        Drink.registerDrink(new Drink("Milk", 5, 1.8f, 0xF0E8DF));
        Drink.registerDrink(new Drink("Chocolate Milk", 7, 2.0f, 0x6E440D));
    }

    public void init() {
    }

    public ThirstStats getStatsByUUID(UUID uuid) {
        ThirstStats stats = (ThirstStats) this.loadedPlayers.get(uuid);
        if (stats == null) {
            System.out.println("Error: Attempted to access non-existent player with UUID: " + uuid);
            return null;
        }
        return stats;
    }

    public void registerPlayer(EntityPlayer player, ThirstStats stats) {
        UUID uuid = player.getUniqueID();
        if (this.loadedPlayers.containsKey(uuid)) {
            return;
        }
        this.loadedPlayers.put(uuid, stats);
    }
}
