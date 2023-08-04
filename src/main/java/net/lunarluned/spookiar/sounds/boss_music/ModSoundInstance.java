package net.lunarluned.spookiar.sounds.boss_music;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class ModSoundInstance extends AbstractSoundInstance {

	protected ModSoundInstance(SoundEvent sound, SoundSource category, RandomSource random) {
		super(sound, category, random);
	}
}
