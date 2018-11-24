/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.items;

import fr.rpcore.thirstmod.ThirstMod;
import fr.rpcore.thirstmod.common.content.Drink;
import fr.rpcore.thirstmod.common.thirstlogic.ThirstStats;
import fr.rpcore.thirstmod.proxy.CommonProxy;
import fr.rpcore.thirstmod.proxy.ThirstTab;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemDrink
        extends ItemContainer {
    public ItemDrink(String unlocalisedName) {
        super(unlocalisedName);
    }

    public Drink getDrinkFromMetadata(int metadata) {
        return Drink.getDrinkByIndex(metadata);
    }

    public int getMetadataForDrink(Drink drink) {
        return Drink.ALL_DRINKS.indexOf(drink);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < Drink.ALL_DRINKS.size(); i++) {
            if (tab == ThirstTab.THIRST_TAB){
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return getDrinkFromMetadata(stack.getMetadata()).drinkName;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        EntityPlayer player = (entityLiving instanceof EntityPlayer) ? (EntityPlayer) entityLiving : null;
        onDrinkItem(player, stack);
        stack.shrink(1);
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
        return stack;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        ThirstStats stats = world.isRemote ? ThirstMod.getClientProxy().clientStats : ThirstMod.getProxy().getStatsByUUID(player.getUniqueID());
        if ((stats.canDrink()) || (player.capabilities.isCreativeMode) || (getDrinkFromMetadata(itemstack.getMetadata()).alwaysDrinkable)) {
            player.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult(EnumActionResult.FAIL, itemstack);
    }

    public static class BottleColorHandler
            implements IItemColor {
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            ThirstMod.getProxy();
            return tintIndex > 0 ? CommonProxy.DRINKS.getDrinkFromMetadata(stack.getMetadata()).drinkColor : 16777215;
        }
    }
}
