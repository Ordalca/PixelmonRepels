package me.ordalca.pixelmonrepels.init;

import com.pixelmonmod.api.pokemon.requirement.impl.FormRequirement;
import com.pixelmonmod.api.pokemon.requirement.impl.SpeciesRequirement;
import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.items.LureItem;
import me.ordalca.pixelmonrepels.ModFile;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.Level;

import java.util.Locale;
import java.util.Optional;

public class RepelItem extends LureItem {
    public RepelItem.RepelStrength repelstrength;

    public RepelItem(LureItem.LureType type, LureItem.LureStrength strength) {
        super(type, strength);
    }
    public RepelItem(LureItem.LureType type, RepelItem.RepelStrength strength) {
        super(type, null);
        this.repelstrength = strength;
    }

    public static enum RepelStrength {
        BAN(0.0F),
        REPEL(0.5F),
        WEAK(1.5F),
        STRONG(3.0F);

        public float multiplier;

        private RepelStrength(float multiplier) {
            this.multiplier = multiplier;
        }
    }

    @Override
    public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
        if (!(spawnInfo instanceof SpawnInfoPokemon)) {
            return 1.0F;
        } else {
            SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;
            RegistryValue<Species> specSpecies = spawnInfoPokemon.getPokemonSpec().getValue(SpeciesRequirement.class).orElse(null);
            if (spawnInfoPokemon.getPokemonSpec() != null && specSpecies != null && specSpecies.isInitialized()) {
                Species species = spawnInfoPokemon.getSpecies();
                Optional<String> formReq = spawnInfoPokemon.getPokemonSpec().getValue(FormRequirement.class);
                Stats form = (Stats)formReq.map(species::getForm).orElse(species.getDefaultForm());
                if (this.type.type != null && form.getTypes().contains(this.type.type)) {
                    if (this.strength != null) {
                        return this.strength.multiplier;
                    } else {
                        return this.repelstrength.multiplier;
                    }
                } else {
                    if (this.type == LureType.SHINY) {
                        if (spawnInfoPokemon.spawnSpecificShinyRate != null && spawnInfoPokemon.spawnSpecificShinyRate == 1.0F || spawnInfo.set.setSpecificShinyRate != null && spawnInfo.set.setSpecificShinyRate == 1.0F) {
                            return this.strength.multiplier;
                        }
                    }
                    return 1.0F;
                }
            } else {
                return 1.0F;
            }
        }
    }
    public String getTooltipText() {
        String translatedType = I18n.get("type." + this.type.name().toLowerCase(Locale.ROOT), new Object[0]);
        if (this.strength != null) {
            return super.getTooltipText();
        } else {
            return I18n.get("repel." + this.repelstrength.name().toLowerCase(Locale.ROOT) + ".normal.tooltip", new Object[]{translatedType});
        }
    }
}
