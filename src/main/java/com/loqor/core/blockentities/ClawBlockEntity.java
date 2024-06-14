package com.loqor.core.blockentities;

import java.util.List;
import java.util.stream.Collectors;

import com.loqor.core.VJBlockEntityTypes;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.tick.TickPriority;

public class ClawBlockEntity extends TicketReturningBlockEntity.RequiresToken implements SidedInventory {
	private final DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
	public ItemStack collectedItem = ItemStack.EMPTY;
	public long collectedItemTick = 0;

	// 5s at normal tick rate
	public static final long WIN_DELAY = 20 * 5;

	private static final String NBT_KEY_COLLECTED_ITEM = "CollectedItem";
	private static final String NBT_KEY_COLLECTED_ITEM_TICK = "CollectedItemTick"; 

	public ClawBlockEntity(BlockPos pos, BlockState state) {
		super(VJBlockEntityTypes.CLAW, pos, state);
	}

	@Override
	public void activate() {
		super.activate();

		// TODO: Play sound

		if (!this.world.isClient()) {
			this.collectedItem = this.removeRandomItem();
			this.collectedItemTick = world.getTime();

			world.scheduleBlockTick(pos, getCachedState().getBlock(), (int) PunchGameBlockEntity.WIN_DELAY, TickPriority.NORMAL);

			((ServerWorld) this.world).getChunkManager().markForUpdate(this.pos);
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();

		this.collectedItem = ItemStack.EMPTY;
		this.collectedItemTick = 0;

		if (!this.world.isClient()) ((ServerWorld) this.world).getChunkManager().markForUpdate(this.pos);
	}

	private ItemStack removeRandomItem() {
		if (!this.isEmpty()) {
			final Random random = this.world.random;
			if (random.nextFloat() <= 0.6f) {
				final List<ItemStack> stacks = this.items.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
				if (!stacks.isEmpty()) {
					final ItemStack stack = stacks.get(random.nextInt(stacks.size()));
					final int slot = this.items.indexOf(stack);
					if (slot != -1) return this.removeStack(slot, 1);
				}
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);

		Inventories.writeNbt(nbt, this.items, true, registryLookup);

		if (!this.collectedItem.isEmpty()) nbt.put(NBT_KEY_COLLECTED_ITEM, this.collectedItem.encode(registryLookup));

		nbt.putLong(NBT_KEY_COLLECTED_ITEM_TICK, this.collectedItemTick);
	}

	@Override
	protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);

		this.items.clear();
		Inventories.readNbt(nbt, this.items, registryLookup);

		if (nbt.contains(NBT_KEY_COLLECTED_ITEM, NbtElement.COMPOUND_TYPE))
			this.collectedItem = ItemStack.fromNbt(registryLookup, nbt.get(NBT_KEY_COLLECTED_ITEM)).orElse(ItemStack.EMPTY);
		else 
			this.collectedItem = ItemStack.EMPTY;

		this.collectedItemTick = nbt.getLong(NBT_KEY_COLLECTED_ITEM_TICK);
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
	public int size() {
		return 27;
	}

	@Override
	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.items.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack itemStack = Inventories.splitStack(this.items, slot, amount);
		if (!itemStack.isEmpty()) this.markDirty();
		return itemStack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.items, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.items.set(slot, stack);
		stack.capCount(this.getMaxCount(stack));
		this.markDirty();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	private static final int[] ALL_SLOTS;
	static {
		int[] array = new int[27];
		for (int i = 0; i <= 26; i++) {
			array[i] = i;
		}
		ALL_SLOTS = array; // I ain' typing allat
	}

	@Override
	public int[] getAvailableSlots(Direction var1) {
		return ALL_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return this.isValid(slot, stack);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}
}
