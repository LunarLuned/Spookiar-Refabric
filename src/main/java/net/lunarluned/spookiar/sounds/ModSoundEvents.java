package net.lunarluned.spookiar.sounds;

import net.lunarluned.spookiar.Spookiar;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

@SuppressWarnings("all")
public class ModSoundEvents {

	// Musics

	public static final SoundEvent ENTITY_PRIME_SPIRIT_MUSIC = registerSoundEvent("entity.prime_spirit.music");
    public static final SoundEvent ENTITY_PRIME_SPIRIT_MUSIC_END = registerSoundEvent("entity.prime_spirit.music_end");
    public static final SoundEvent ENTITY_PRIME_SPIRIT_MUSIC_START = registerSoundEvent("entity.prime_spirit.music_start");


    // Item Sounds

    public static final SoundEvent SCULK_SAC_FILL = registerSoundEvent("item.sculk_sac.fill");
    public static final SoundEvent SCULK_SAC_EMPTY = registerSoundEvent("item.sculk_sac.empty");

    // Entity Sounds
    public static final SoundEvent ENTITY_GHOST_MOAN = registerSoundEvent("entity.ghost.moan");
    public static final SoundEvent ENTITY_GHOST_JUMPSCARE = registerSoundEvent("entity.ghost.jumpscare");
    public static final SoundEvent ENTITY_GHOST_RARE_SECRET = registerSoundEvent("entity.ghost.rare_secret");
    public static final SoundEvent ENTITY_GHOST_EMPOWERED = registerSoundEvent("entity.ghost.empowered");
    public static final SoundEvent ENTITY_GHOST_HURT = registerSoundEvent("entity.ghost.hurt");
    public static final SoundEvent ENTITY_GHOST_DEATH = registerSoundEvent("entity.ghost.death");

	public static final SoundEvent ENTITY_PRIME_SPIRIT_JUDGEMENT = registerSoundEvent("entity.prime_spirit.judgement");
	public static final SoundEvent ENTITY_PRIME_SPIRIT_RISE = registerSoundEvent("entity.prime_spirit.rise");
	public static final SoundEvent ENTITY_PRIME_SPIRIT_WEAKEN = registerSoundEvent("entity.prime_spirit.weaken");
	public static final SoundEvent ENTITY_PRIME_SPIRIT_PATHETIC = registerSoundEvent("entity.prime_spirit.pathetic");
	public static final SoundEvent ENTITY_PRIME_SPIRIT_WEAK = registerSoundEvent("entity.prime_spirit.weak");
	public static final SoundEvent ENTITY_PRIME_SPIRIT_DIE = registerSoundEvent("entity.prime_spirit.die");


	public static final SoundEvent ENTITY_PRIME_SPIRIT_DEATH_SPEECH = registerSoundEvent("entity.prime_spirit.death_speech");
	public static final SoundEvent ENTITY_PRIME_SPIRIT_DEATH = registerSoundEvent("entity.prime_spirit.death");

    // Block Sounds
    public static final SoundEvent BLOCK_GRIMSTEEL_BREAK = registerSoundEvent("block.grimsteel.break");
    public static final SoundEvent BLOCK_GRIMSTEEL_STEP = registerSoundEvent("block.grimsteel.step");
    public static final SoundEvent BLOCK_GRIMSTEEL_PLACE = registerSoundEvent("block.grimsteel.place");
    public static final SoundEvent BLOCK_GRIMSTEEL_HIT = registerSoundEvent("block.grimsteel.hit");
    public static final SoundEvent BLOCK_GRIMSTEEL_FALL = registerSoundEvent("block.grimsteel.fall");

    // Misc Sounds

    public static final SoundEvent BLOCK_GRAVESTONE_LAY = registerSoundEvent("block.gravestone.lay");

    public static final SoundEvent BLOCK_GRAVESTONE_COLLECT = registerSoundEvent("block.gravestone.collect");




    public static final SoundType GRIMSTEEL = new SoundType(1f, 1f,
            ModSoundEvents.BLOCK_GRIMSTEEL_BREAK, ModSoundEvents.BLOCK_GRIMSTEEL_STEP, ModSoundEvents.BLOCK_GRIMSTEEL_PLACE,
            ModSoundEvents.BLOCK_GRIMSTEEL_HIT, ModSoundEvents.BLOCK_GRIMSTEEL_FALL);


    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(Spookiar.MOD_ID, name);
        SoundEvent se = SoundEvent.createVariableRangeEvent(id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, se);
    }

    public static void registerSounds() {
        System.out.println("Registering Sounds for " + Spookiar.MOD_ID);
    }
}
