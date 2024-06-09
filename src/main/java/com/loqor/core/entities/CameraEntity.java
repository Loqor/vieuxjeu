package com.loqor.core.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class CameraEntity extends Entity {

    private double prevX;
    private double prevY;
    private double prevZ;

    public CameraEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public CameraEntity() {
        super(EntityType.BAT, null); // cameras are actually bats
    }

    public void setPosition(double x, double y, double z, World world) {
        prevX = x;
        prevY = y;
        prevZ = z;
        super.setPosition(x, y, z);
        this.setWorld(world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

}
