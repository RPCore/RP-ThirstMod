/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.common.thirstlogic;

import fr.rpcore.thirstmod.ThirstMod;
import fr.rpcore.thirstmod.client.gui.GuiThirstBar;
import fr.rpcore.thirstmod.common.content.Drink;
import fr.rpcore.thirstmod.common.content.DrinkItem;
import fr.rpcore.thirstmod.common.content.ExternalDrink;
import fr.rpcore.thirstmod.network.NetworkManager;
import fr.rpcore.thirstmod.network.PacketMovementSpeed;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.*;

public class EventHook {
    private static EventHook instance = new EventHook();

    public static EventHook getInstance() {
        return instance;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
        GuiThirstBar.onRenderGameOverlayEvent(event);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote) {
            ThirstStats stats = ThirstMod.getProxy().getStatsByUUID(event.player.getUniqueID());
            if (stats != null) {
                stats.update(event.player);
            }
        } else {
            NetworkManager.getNetworkWrapper().sendToServer(new PacketMovementSpeed(event.player, ThirstMod.getClientProxy().clientStats));
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        final Item items[] = {
                ThirstMod.getProxy().DRINKS,
                ThirstMod.getProxy().FILTER,
        };

        event.getRegistry().registerAll(items);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        final Item items[] = {
                ThirstMod.getProxy().DRINKS,
                ThirstMod.getProxy().FILTER,
        };
        for (Item item : items) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
        for (int i = 0; i < Drink.ALL_DRINKS.size(); i++) {
            ModelLoader.setCustomModelResourceLocation(items[0], i, new ModelResourceLocation(items[0].getRegistryName(), "inventory"));
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent attack) {
        if (!attack.getEntityPlayer().world.isRemote) {
            ThirstStats stats = ThirstMod.getProxy().getStatsByUUID(attack.getEntityPlayer().getUniqueID());
            stats.addExhaustion(0.5F);
        }
        attack.setResult(Event.Result.DEFAULT);
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent hurt) {
        if ((hurt.getEntity() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) hurt.getEntity();
            if (!player.world.isRemote) {
                ThirstStats stats = ThirstMod.getProxy().getStatsByUUID(player.getUniqueID());
                stats.addExhaustion(0.4F);
            }
        }
        hurt.setResult(Event.Result.DEFAULT);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if ((player != null) &&
                (!player.world.isRemote)) {
            ThirstStats stats = ThirstMod.getProxy().getStatsByUUID(player.getUniqueID());
            stats.addExhaustion(0.03F);
        }
        event.setResult(Event.Result.DEFAULT);
    }

    public void playedCloned(PlayerEvent.Clone event) {
        if ((!event.getEntityPlayer().world.isRemote) &&
                (event.isWasDeath())) {
            ThirstStats stats = ThirstMod.getProxy().getStatsByUUID(event.getEntityPlayer().getUniqueID());
            stats.resetStats();
        }
    }

    @SubscribeEvent
    public void onLoadPlayerData(PlayerEvent.LoadFromFile event) {
        if (!event.getEntityPlayer().world.isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            File saveFile = event.getPlayerFile("thirstmod");
            if (!saveFile.exists()) {
                ThirstMod.getProxy().registerPlayer(player, new ThirstStats());
            } else {
                try {
                    FileReader reader = new FileReader(saveFile);
                    ThirstStats stats = (ThirstStats) ThirstMod.gsonInstance.fromJson(reader, ThirstStats.class);
                    if (stats == null) {
                        ThirstMod.getProxy().registerPlayer(player, new ThirstStats());
                    } else {
                        ThirstMod.getProxy().registerPlayer(player, stats);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public void onSavePlayerData(PlayerEvent.SaveToFile event) {
        if (!event.getEntityPlayer().world.isRemote) {
            ThirstStats stats = ThirstMod.getProxy().getStatsByUUID(event.getEntityPlayer().getUniqueID());
            File saveFile = new File(event.getPlayerDirectory(), event.getPlayerUUID() + ".thirstmod");
            try {
                String write = ThirstMod.gsonInstance.toJson(stats);
                saveFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
                writer.write(write);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if ((!event.getEntity().world.isRemote) && ((event.getEntityLiving() instanceof EntityPlayer))) {
            ItemStack eventItem = event.getItem();

            eventItem.setCount(eventItem.getCount() + 1);
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            for (DrinkItem drinkItem : ExternalDrink.EXTERNAL_DRINKS) {
                Item item = Item.getByNameOrId(drinkItem.name);
                if ((eventItem.getItem().equals(item)) && ((drinkItem.metadata == -1) || (eventItem.getMetadata() == drinkItem.metadata))) {
                    ThirstStats stats = ThirstMod.getProxy().getStatsByUUID(player.getUniqueID());
                    stats.addStats(drinkItem.thirstReplenish, drinkItem.saturationReplenish);
                    stats.attemptToPoison(drinkItem.poisonChance);
                }
            }
            eventItem.setCount(eventItem.getCount() - 1);
        }
    }
}
