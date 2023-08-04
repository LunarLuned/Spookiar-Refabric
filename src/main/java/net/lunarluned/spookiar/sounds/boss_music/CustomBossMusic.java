package net.lunarluned.spookiar.sounds.boss_music;

import net.lunarluned.spookiar.sounds.ModSoundEvents;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class CustomBossMusic {
	private static final ResourceLocation PRIME_SPIRIT_MUSIC = ModSoundInstances.PRIME_SPIRIT_MUSIC;
	private static final SoundEvent PRIME_SPIRIT_MUSIC_EVENT = ModSoundEvents.ENTITY_PRIME_SPIRIT_MUSIC;
	private static final SoundSource PRIME_SPIRIT_MUSIC_CATEGORY = SoundSource.MUSIC;

	public static SoundInstance createBossMusicInstance() {
		return new ModSoundInstance(PRIME_SPIRIT_MUSIC_EVENT, PRIME_SPIRIT_MUSIC_CATEGORY, RandomSource.create());
	}
}
