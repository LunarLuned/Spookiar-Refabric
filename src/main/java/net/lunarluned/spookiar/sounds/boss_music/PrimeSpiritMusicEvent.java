package net.lunarluned.spookiar.sounds.boss_music;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;

@Environment(EnvType.CLIENT)
public class PrimeSpiritMusicEvent {
	private static final SoundInstance BOSS_MUSIC = CustomBossMusic.createBossMusicInstance();

	public static void playBossMusic() {
		Minecraft.getInstance().getSoundManager().play(BOSS_MUSIC);
	}

	public static void stopBossMusic() {
		Minecraft.getInstance().getSoundManager().stop(BOSS_MUSIC);
	}
}
