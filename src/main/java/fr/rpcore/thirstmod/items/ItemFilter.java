/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.items;

import fr.rpcore.thirstmod.Constants;
import fr.rpcore.thirstmod.proxy.ThirstTab;
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

