package com.loqor.core;

import com.loqor.VieuxJeu;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class VJSoundEvents {

	public static final SoundEvent PUNCH_GAME_WIN = register("punch_game_win");
	
	private static final SoundEvent register(String name) {
		return SoundEvent.of(Identifier.of(VieuxJeu.MOD_ID, name));
	}
}
