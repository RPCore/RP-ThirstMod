/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.proxy;

import fr.rpcore.thirstmod.common.thirstlogic.ThirstStats;
import fr.rpcore.thirstmod.items.ItemDrink;
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
