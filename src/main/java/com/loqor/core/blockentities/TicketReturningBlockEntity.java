package com.loqor.core.blockentities;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

import org.jetbrains.annotations.Nullable;

import gay.lemmaeof.terrifictickets.TerrificTickets;
import gay.lemmaeof.terrifictickets.component.PasscardComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class TicketReturningBlockEntity extends BlockEntity {

	public TicketReturningBlockEntity(BlockEntityType<? extends TicketReturningBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void giveTickets(int amount) {
		BlockState state = getCachedState();
		if (state == null) return;
		Direction dir = state.getOrEmpty(HORIZONTAL_FACING).orElse(Direction.NORTH);
		ItemDispenserBehavior.spawnItem(world, new ItemStack(TerrificTickets.TICKET, amount), 6, dir, pos.toCenterPos().offset(dir, 0.8F));
	}
	
	public abstract static class RequiresToken extends TicketReturningBlockEntity {
		
		private final int neededTokens;
		private int currentTokens = 0;
		private boolean isActive = false;
		
		public static final String NBT_KEY_ACTIVE = "Active";
		public static final String NBT_KEY_CURRENT_TOKENS = "CurrentTokens";

		public RequiresToken(BlockEntityType<? extends RequiresToken> type, BlockPos pos, BlockState state) {
			this(type, pos, state, 1);
		}
		
		public RequiresToken(BlockEntityType<? extends RequiresToken> type, BlockPos pos, BlockState state, int neededTokens) {
			super(type, pos, state);
			this.neededTokens = Math.max(0, neededTokens);
		}
		
		/**
		 * Used to feed tokens or a passcard with tokens into machine
		 * @param itemStack  
		 * @param tokenFeeder (usually a player) that gave tokens, is nullable
		 * @return if itemStack was accepted
		 */
		public boolean feedTokens(ItemStack itemStack, @Nullable LivingEntity tokenFeeder) {
			if (this.isActive) return false;
			
			if (itemStack.isOf(TerrificTickets.TOKEN)) {
				itemStack.decrementUnlessCreative(getTokensRequiredLeft(), tokenFeeder);
				this.currentTokens += itemStack.getCount();
			
			} else if (itemStack.isOf(TerrificTickets.PASSCARD)) {
				PasscardComponent contents = itemStack.get(TerrificTickets.PASSCARD_COMPONENT);
				if (contents == null) return false;
				Pair<PasscardComponent, Integer> response = contents.removeTokens(getTokensRequiredLeft());
				
				this.currentTokens += response.getRight();
				if (tokenFeeder == null || !tokenFeeder.isInCreativeMode()) {
					itemStack.set(TerrificTickets.PASSCARD_COMPONENT, response.getLeft());
				}
			
			} else return false;
			
			if (this.currentTokens >= this.neededTokens) this.activate();

			return true;
		}
		
		public void activate() {
			this.isActive = true;
			this.currentTokens = 0;
			this.markDirty();
		}
		
		public void deactivate() {
			this.isActive = false;
			this.markDirty();
		}
		
		public boolean isActive() {
			return this.isActive;
		}
		
		public int getTokensRequiredLeft() {
			return this.neededTokens - this.currentTokens;
		}
				
		public int getTokens() {
			return this.currentTokens;
		}
		
		@Override
		protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
			super.writeNbt(nbt, registryLookup);
			nbt.putBoolean(NBT_KEY_ACTIVE, this.isActive);
			if (this.neededTokens > 1) nbt.putInt(NBT_KEY_CURRENT_TOKENS, this.currentTokens);
		}
		
		@Override
		protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
			super.readNbt(nbt, registryLookup);
			this.isActive = nbt.getBoolean(NBT_KEY_ACTIVE);
			this.currentTokens = nbt.getInt(NBT_KEY_CURRENT_TOKENS);
		}
		
	}
	
}
