/*
 * Class By M4TH1EU
 */

package ch.m4th1eu.thirstmod;

import ch.m4th1eu.thirstmod.common.content.ExternalDrink;
import ch.m4th1eu.thirstmod.common.thirstlogic.EventHook;
import ch.m4th1eu.thirstmod.proxy.ClientProxy;
import ch.m4th1eu.thirstmod.proxy.CommonProxy;
import com.google.gson.Gson;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "thirstmod", version = "0.0.2", acceptedMinecraftVersions = "[1.12.2]")
public class ThirstMod {
    @Mod.Instance("thirstmod")
    private static ThirstMod instance;
    @SidedProxy(clientSide = "ch.m4th1eu.thirstmod.proxy.ClientProxy", serverSide = "ch.m4th1eu.thirstmod.proxy.CommonProxy", modId = "thirstmod")
    private static CommonProxy commonProxy;


    public static ThirstMod getInstance() {
        return instance;
    }

    public static CommonProxy getProxy() {
        return commonProxy;
    }

    @SideOnly(Side.CLIENT)
    public static ClientProxy getClientProxy() {
        return (ClientProxy) commonProxy;
    }

    public static Gson gsonInstance = new Gson();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ExternalDrink.load();
        MinecraftForge.EVENT_BUS.register(EventHook.getInstance());
        getProxy().preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        getProxy().init();
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        getProxy().loadedPlayers.clear();
    }
}
