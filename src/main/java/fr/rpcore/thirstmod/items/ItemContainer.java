/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.items;

import fr.rpcore.thirstmod.ThirstMod;
import fr.rpcore.thirstmod.common.content.Drink;
import fr.rpcore.thirstmod.common.thirstlogic.ThirstStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;

public class ItemContainer
        extends Item {

    public ItemContainer(String unlocalisedName) {
        setTranslationKey(unlocalisedName);
        setRegistryName("thirstmod", unlocalisedName);

    }

    public Drink getDrinkFromMetadata(int metadata) {
        return null;
    }

    public int getMetadataForDrink(Drink drink) {
        return 0;
    }

    public boolean hasEffect(ItemStack stack) {
        return getDrinkFromMetadata(stack.getMetadata()).shiny;
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    public void onDrinkItem(EntityPlayer player, ItemStack stack) {
        if ((!player.world.isRemote) && (player != null)) {
            ThirstStats stats = ThirstMod.getProxy().getStatsByUUID(player.getUniqueID());
            Drink drink = getDrinkFromMetadata(stack.getMetadata());
            stats.addStats(drink.thirstReplenish, drink.saturationReplenish);
            stats.attemptToPoison(drink.poisonChance);
            player.addStat(StatList.getObjectUseStats(this));
        }
    }
}
