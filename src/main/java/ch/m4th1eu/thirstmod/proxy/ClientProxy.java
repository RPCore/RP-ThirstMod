/*
 * Class By M4TH1EU
 */

package ch.m4th1eu.thirstmod.proxy;

import ch.m4th1eu.thirstmod.common.thirstlogic.ThirstStats;
import ch.m4th1eu.thirstmod.items.ItemDrink;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

public class ClientProxy
        extends CommonProxy {
    public ThirstStats clientStats = new ThirstStats();

    public void preInit() {
        super.preInit();
    }

    public void init() {
        super.init();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemDrink.BottleColorHandler(), new Item[]{DRINKS});
    }
}
