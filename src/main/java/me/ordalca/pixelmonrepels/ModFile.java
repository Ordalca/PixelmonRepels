package me.ordalca.pixelmonrepels;

import com.pixelmonmod.pixelmon.Pixelmon;
import me.ordalca.pixelmonrepels.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModFile.MOD_ID)
@Mod.EventBusSubscriber(modid = ModFile.MOD_ID)
public class ModFile {

    public static final String MOD_ID = "pixelmonrepels";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static ModFile instance;

    public ModFile() {
        instance = this;

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        ItemInit.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Loaded PixelmonRepels mod");
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        // Logic for when the server is starting here

        // Here is how you register a listener for Pixelmon events
        // Pixelmon has its own event bus for its events, as does TCG
        Pixelmon.EVENT_BUS.register(new PokemonSpawnAttemptListener());

        // So any event listener for those mods need to be registered to those specific event buses
    }

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) {
        // Logic for once the server has started here
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        //Register command logic here
        // Commands don't have to be registered here
        // However, not registering them here can lead to some hybrids/server software not recognising the commands
    }

    @SubscribeEvent
    public static void onServerStopping(FMLServerStoppingEvent event) {
        // Logic for when the server is stopping
    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event) {
        // Logic for when the server is stopped
    }

    public static ModFile getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
