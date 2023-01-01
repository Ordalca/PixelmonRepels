package me.ordalca.pixelmonrepels;

import com.pixelmonmod.pixelmon.api.events.spawning.CreateSpawnerEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.RarityTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.listener.RepelHandler;
import com.pixelmonmod.pixelmon.spawning.PlayerTrackingSpawner;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;

public class PokemonSpawnAttemptListener {
    @SubscribeEvent
    public void addRepelTweak(CreateSpawnerEvent event) {
        ModFile.getLogger().log(Level.DEBUG, "spawner created "+event.spawner.name);
        if (event.spawner instanceof PlayerTrackingSpawner) {
            PlayerTrackingSpawner ptSpawner = (PlayerTrackingSpawner) event.spawner;
            ModFile.getLogger().log(Level.DEBUG, "Adding repel tweak to spawner "+ptSpawner.name);
            ptSpawner.rarityTweaks.add(new RepelRarityTweak());
            ModFile.getLogger().log(Level.DEBUG, "Tweaks:"+ptSpawner.rarityTweaks);
        }
    }

    @SubscribeEvent
    public void adjustSpawnLevel(SpawnEvent event) {
        if (!(event.spawner instanceof  PlayerTrackingSpawner)) return;
        if (!(event.action instanceof  SpawnActionPokemon)) return;

        PlayerTrackingSpawner spawner = (PlayerTrackingSpawner) event.spawner;
        ServerPlayerEntity player = spawner.getTrackedPlayer();

        if (!hasRepel(player)) return;

        int spawnLevel = getWildLevel(event);
        int leadLevel = getPartyLevel(player);

        SpawnActionPokemon action = (SpawnActionPokemon)event.action;
        Pokemon pokemon = action.pokemon;
        int maxLevel = ((SpawnInfoPokemon)(action.spawnInfo)).maxLevel;
        if (maxLevel < leadLevel) {
            event.setCanceled(true);
        } else if (spawnLevel != -1 && spawnLevel < leadLevel) {
            String message = "Adjusting level for "+pokemon.getDisplayName();
            String currentLevel = ": "+pokemon.getPokemonLevel()+"("+maxLevel+")";
            ModFile.getLogger().log(Level.DEBUG, message+currentLevel+"->"+leadLevel);
            action.pokemon.setLevel(leadLevel);
        }
    }

    private boolean hasRepel(ServerPlayerEntity player) {
        return player != null && (RepelHandler.hasRepel(player));
    }
    private int getWildLevel(SpawnEvent event) {
        if (!(event.action instanceof SpawnActionPokemon))
            return -1;
        PixelmonEntity pixelmonEntity = ((SpawnActionPokemon)event.action).getOrCreateEntity();
        if (pixelmonEntity == null)
            return -1;

        Pokemon pokemon = pixelmonEntity.getPokemon();
        return pokemon.getPokemonLevel();
    }
    private int getPartyLevel(ServerPlayerEntity player) {
        if (player == null) return -1;

        PlayerPartyStorage party = StorageProxy.getParty(player);
        if (party.getTeam().size() > 0) {
            return party.getTeam().get(0).getPokemonLevel();
        }
        return -1;
    }

    class RepelRarityTweak implements RarityTweak {
        @Override
        public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
            if (!(spawner instanceof  PlayerTrackingSpawner)) return 1.0F;
            if (!(spawnInfo instanceof SpawnInfoPokemon)) return 1.0F;

            PlayerTrackingSpawner ptSpawner = (PlayerTrackingSpawner)spawner;
            ServerPlayerEntity player = ptSpawner.getTrackedPlayer();
            if (!hasRepel(player)) return 1.0F;

            PlayerPartyStorage party = StorageProxy.getParty(player);
            if (party.getTeam().size() > 0) {
                int partyLevel = party.getTeam().get(0).getPokemonLevel();
                SpawnInfoPokemon pokeSpawn = (SpawnInfoPokemon) spawnInfo;
                return (pokeSpawn.maxLevel >= partyLevel) ? 1.0F : 0.0F;
            }
            return 1.0F;
        }
    }
}
