/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.proxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ThirstTab {

    public static final CreativeTabs THIRST_TAB = new CreativeTabs("thirstmod") {
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Item.getByNameOrId("thirstmod:drink_item"));
        }
    };

}
