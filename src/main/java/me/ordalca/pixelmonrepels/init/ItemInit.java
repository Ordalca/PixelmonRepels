package me.ordalca.pixelmonrepels.init;
import com.pixelmonmod.pixelmon.items.LureItem;

import me.ordalca.pixelmonrepels.ModFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;


public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ModFile.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
    private static RepelItem addToList(RepelItem entry, @Nullable List<RepelItem> list) {
        if (list != null && !list.contains(entry)) {
            list.add(entry);
        }
        return entry;
    }

    static {
        for (LureItem.LureType type : LureItem.LureType.values()) {
            if (type.type != null) {
                RepelItem weak_repel = new RepelItem(type, RepelItem.RepelStrength.REPEL);
                RepelItem strong_repel = new RepelItem(type, RepelItem.RepelStrength.BAN);

                ITEMS.register("repel_" + type.name().toLowerCase(Locale.ROOT) + "_weak", () -> addToList(weak_repel, RepelItems.weakRepels));
                ITEMS.register("repel_" + type.name().toLowerCase(Locale.ROOT) + "_strong", () -> addToList(strong_repel, RepelItems.strongRepels));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerItemLayers() {
        IItemColor color = (stack, tint) -> {
            if (tint != 0) {
                return 0xFFFFFF;
            }

            LureItem lure = (LureItem) stack.getItem();
            return lure.type.type.getColor();
        };

        for (Item item : RepelItems.weakRepels) {
            Minecraft.getInstance().getItemColors().register(color, new IItemProvider[] { item });
        }
        for (Item item : RepelItems.strongRepels) {
            Minecraft.getInstance().getItemColors().register(color, new IItemProvider[] { item });
        }
    }
}