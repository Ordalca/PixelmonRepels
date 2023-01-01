package me.ordalca.pixelmonrepels.init;

import me.ordalca.pixelmonrepels.ModFile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber(modid = "pixelmonrepels", value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ItemInit.registerItemLayers();
    }
}
