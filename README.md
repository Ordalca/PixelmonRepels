# PixelmonRepels  [![GitHub](https://img.shields.io/github/license/Pixelmon-Development/API)](https://www.gnu.org/licenses/lgpl-3.0.html)

This project modifies the behavior of the Repel effect to account for the lead Pokemon's level.

1) If the spawning Pokemon's maximum spawn level is below the lead Pokemon's, set the rarity to 0.
   1) This prevents spawn attempts of weaker Pokemon from occurring, leaving slots open for stronger Pokemon.
2) If the Pokemon selected to be spawned has a range containing the lead Pokemon's level, raises the level to match the lead Pokemon's if lower.

This project also adds type-based repels crafted from Repels and type Lures.
1) Strong Type Repels block all normal spawns matching that type.
2) Weak Type Repels reduce the rarity of all spawns matching that type by half.