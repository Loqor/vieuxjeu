package com.loqor.core.blockentities;

import org.jetbrains.annotations.Nullable;

import gay.lemmaeof.terrifictickets.TerrificTickets;
import gay.lemmaeof.terrifictickets.component.PasscardComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.Pair;

public interface TakesTokens {
		
	public static final String NBT_KEY_ACTIVE = "Active";
	public static final String NBT_KEY_CURRENT_TOKENS = "CurrentTokens";
	
	public void markDirty();
	
	public boolean isActive();
	public void setActive(boolean active);
	public int getNeededTokens();
	public int getCurrentTokens();
	public void setCurrentTokens(int tokens);
	
	/**
	 * Used to feed tokens or a passcard with tokens into machine
	 * @param itemStack  
	 * @param tokenFeeder (usually a player) that gave tokens, is nullable
	 * @return if itemStack was accepted
	 */
	public default boolean feedTokens(ItemStack itemStack, @Nullable LivingEntity tokenFeeder) {
		if (this.isActive()) return false;
		
		if (itemStack.isOf(TerrificTickets.TOKEN)) {
			itemStack.decrementUnlessCreative(getTokensRequiredLeft(), tokenFeeder);
			this.setCurrentTokens(this.getCurrentTokens() + itemStack.getCount());
		
		} else if (itemStack.isOf(TerrificTickets.PASSCARD)) {
			final PasscardComponent contents = itemStack.get(TerrificTickets.PASSCARD_COMPONENT);
			if (contents == null) return false;
			final Pair<PasscardComponent, Integer> response = contents.removeTokens(getTokensRequiredLeft());
			
			this.setCurrentTokens(this.getCurrentTokens() + response.getRight());
			if (tokenFeeder == null || !tokenFeeder.isInCreativeMode()) {
				itemStack.set(TerrificTickets.PASSCARD_COMPONENT, response.getLeft());
			}
		
		} else return false;
		
		if (this.getCurrentTokens() >= this.getNeededTokens()) this.activate();

		return true;
	}
	
	public default void activate() {
		this.setActive(true);
		this.setCurrentTokens(0);
		this.markDirty();
	}
	
	public default void deactivate() {
		this.setActive(false);
		this.markDirty();
	}
		
	public default int getTokensRequiredLeft() {
		return this.getNeededTokens() - this.getCurrentTokens();
	}
	
	public default void iWriteNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		nbt.putBoolean(NBT_KEY_ACTIVE, this.isActive());
		if (this.getNeededTokens() > 1) nbt.putInt(NBT_KEY_CURRENT_TOKENS, this.getCurrentTokens());
	}
	
	public default void iReadNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		this.setActive(nbt.getBoolean(NBT_KEY_ACTIVE));
		this.setCurrentTokens(nbt.getInt(NBT_KEY_CURRENT_TOKENS));
	}
	
}
