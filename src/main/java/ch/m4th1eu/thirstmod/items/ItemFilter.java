/*
 * Class By M4TH1EU
 */

package ch.m4th1eu.thirstmod.items;

import ch.m4th1eu.thirstmod.Constants;
import ch.m4th1eu.thirstmod.proxy.ThirstTab;
import net.minecraft.item.Item;

public class ItemFilter extends Item {

    public ItemFilter(String name) {
        setTranslationKey(name);
        setRegistryName(Constants.MOD_ID, name);
        setNoRepair();
        setCreativeTab(ThirstTab.THIRST_TAB);
        setMaxStackSize(1);
    }
}

