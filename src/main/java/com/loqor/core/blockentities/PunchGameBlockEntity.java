package com.loqor.core.blockentities;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.loqor.core.VJBlockEntityTypes;
import com.loqor.core.VJEntityTypes;
import com.loqor.core.entities.PunchGameEntity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PunchGameBlockEntity extends TicketReturningBlockEntity.RequiresToken {
	
	@Nullable
	private UUID entityUUID;
	private int currentValue = 0;
	
	public long currentValueTick = 0;
	public int previousValue = 0;
	
	public static final long DELAY = 20 * 3;
	public static final String NBT_KEY_CONNECTED_ENTITY = "ConnectedEntity";
	public static final String NBT_KEY_SCORE = "Value";
	public static final String NBT_KEY_PREVIOUS_VALUE = "PreviousValue";
	public static final String NBT_KEY_SCORE_TICK = "ScoreTick";
	
	public PunchGameBlockEntity(BlockPos pos, BlockState state) {
		super(VJBlockEntityTypes.PUNCH_GAME, pos, state, 10);
	}
	
	public int getScore() {
		return this.currentValue;
	}
	
	public void setScore(int score) {
		this.previousValue = this.currentValue;
		this.currentValue = score;
		this.currentValueTick = this.world.getTime();
	}
	
	@Override
	public void activate() {
		super.activate();
		
		if (!this.world.isClient()) {
			PunchGameEntity entity = VJEntityTypes.PUNCH_GAME.create(this.world);
			final float offset = 2.0F - 2/16F - entity.getHeight(); // 2 blocks up, 2 pixels down
			entity.setPosition(this.pos.toBottomCenterPos().offset(Direction.UP, offset));
			entity.blockPos = this.pos;
			if (this.world.spawnEntity(entity)) this.entityUUID = entity.getUuid();
			else this.deactivate();
		}
		
		this.setScore(0);
				
		if (!this.world.isClient()) ((ServerWorld) this.world).getChunkManager().markForUpdate(this.pos);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		
		if (!this.world.isClient() && this.entityUUID != null) {			
			MinecraftServer server = world.getServer();
			final UUID uuidBeforeRemoval = this.entityUUID;
			// This is used so that crit particles will get created but the entity will still die instantly
			// Remember kiddos, never forget to do your null checks on @Nullable's. Mods exist
			if (server != null) server.execute(() -> {					
				Entity entity = ((ServerWorld) this.world).getEntity(uuidBeforeRemoval);
				if (entity != null) entity.discard();
			});
		}
		
		this.entityUUID = null;

		if (!this.world.isClient()) ((ServerWorld) this.world).getChunkManager().markForUpdate(this.pos);
	}
	
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt(WrapperLookup registryLookup) {
	    return createNbt(registryLookup);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		if (this.entityUUID != null) nbt.putUuid(NBT_KEY_CONNECTED_ENTITY, entityUUID);
		nbt.putInt(NBT_KEY_SCORE, this.currentValue);
		nbt.putLong(NBT_KEY_SCORE_TICK, this.currentValueTick);
		nbt.putInt(NBT_KEY_PREVIOUS_VALUE, this.previousValue);
	}
	
	@Override
	protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		if (nbt.containsUuid(NBT_KEY_CONNECTED_ENTITY)) this.entityUUID = nbt.getUuid(NBT_KEY_CONNECTED_ENTITY);
		this.currentValue = nbt.getInt(NBT_KEY_SCORE);
		this.currentValueTick = nbt.getLong(NBT_KEY_SCORE_TICK);
		this.previousValue = nbt.getInt(NBT_KEY_PREVIOUS_VALUE);
	}
	
}
