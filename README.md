# Amphithere Mod (Forge 1.20.1)

An add-on for [Ice and Fire](https://www.curseforge.com/minecraft/mc-mods/ice-and-fire-dragons) that expands its
amphitheres. This is a port of the 1.12.2 **I&F Amphithere Mod** by Iqury and the I&F Amphithere Mod team, made with
their permission and under the original's MIT license. It needs Ice and Fire for 1.20.1 (and Citadel, which Ice and Fire needs anyway).

## Features
- **27 variants**: Ice and Fire's 5 plus rare, gem, skeleton and special ones (Rainbow, Shivaxi, Black Eagle, Iqury,
  Crafty), each with its own eyes, and some with glowing markings. Wild amphitheres roll from all of them.
- **Wing patterns**: 1 in 10 wild amphitheres has a coloured wing pattern.
- **Male and female**: shown in the inventory and with markings; only a male and a female can breed.
- **Breeding rules and eggs**: two colours can mix into a third (Blue + Yellow = Green, and so on). Breeding lays an
  egg that hatches after it stays warm (on leaves in a warm biome) for a while.
- **Shivaxi and Black Eagle**: rare (1 in 1000) strong amphitheres. The Shivaxi drops its own feathers.
- **Inventory**: the owner shift-right-clicks a tamed amphithere to open it. Slots for a beak and four armor pieces
  (head, wings, body, tail), its health, stamina and gender, and a button to switch wander / sit / follow.
- **Armor and beaks**: copper, iron, gold, diamond and silver armor, and beaks in those plus dragonbone and fire, ice
  and lightning dragonbone. Elemental beaks set on fire, freeze or chain lightning (when the Ice and Fire build has
  chain lightning), and do extra damage to the opposite dragons. Silver beaks hurt undead more.
- **Stamina**: tamed amphitheres ridden by a player use stamina to fly and flap, and regain it gliding, diving and on
  the ground. At zero they fall and can't take off until they recover. A bar shows it while riding.
- **Food**: cocoa beans heal 10% and give back some stamina (and aren't wasted at full health); the XXL Chocolate
  Cookie gives a long Health Boost; the Bloody Cookie makes an amphithere permanently immune to COTH (needs End-Parasitize
  and Convert All).
- **Crystal Feather**: soul binds your amphithere to you: if one of you dies, both do. It stores the amphithere inside
  the feather and releases or summons it.
- **Flight animation**: a reworked wing beat with folding wings, body bob, dive pose, banking and tail follow-through.
- **Fixes**: tamed amphitheres no longer attack a wild one their owner is taming, defend themselves while sitting, follow
  the owner after the flute, recover after falling out of the sky, and circle without stuttering. Leads stretch further.
  Skeleton variants make skeleton sounds; lightning and fire can turn an amphithere into a skeleton.
- **Third person camera** while riding, using Ice and Fire's dragon view key.

Almost everything can be turned off or tuned in `config/amphitheremod-common.toml` (camera options are in
`amphitheremod-client.toml`).

## Compatibility
- **Dynamic Trees**: amphitheres pass through its leaves (and optionally side branches) like normal leaves.
- **End-Parasitize and Convert All**: the Bloody Cookie and its COTH immunity.
- **SimpleHats** (in place of 1.12.2's ClassyHats): the amphithere inventory gets a hat slot, and the hat is drawn on
  the amphithere's head and follows its neck and head.
- **Simple Difficulty Reforged** (in place of 1.12.2's PotionCore): a full silver set (beak and all four armor pieces)
  gives its Cure effect, which clears bad effects. The effect list is in the config (`silverSetBonusEffects`), so any
  mod's cure effect can be used.
- **First Aid** and **Quality Equipment**: nothing needed. In 1.12.2 they treated the amphithere armor as player armor
  (First Aid tooltips, Quality reforging and stats), so the add-on had to switch them off for it. Here the armor and
  beaks aren't player armor items, so neither mod touches them.
- The wind talisman support isn't ported, since the mod that adds it has no 1.20.1 version.

## Building
`./gradlew build`. The jar is in `build/libs`. It compiles against Ice and Fire 2.1.13 from CurseForge. To build and run
against another Ice and Fire build (a fork, for example), put its jar at `libs/iceandfire-1.jar`.

## Credits
- **Iqury and the I&F Amphithere Mod team**: the original 1.12.2 mod, its art, sounds and design. Ported with permission.
- **Alexthe666, Raptorfarian and the Ice and Fire contributors**: Ice and Fire and its amphithere.

## License
MIT, the same license as the original 1.12.2 mod (see `LICENSE`).
