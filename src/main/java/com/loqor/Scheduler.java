package com.loqor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.server.MinecraftServer;

/**
 * This is used to schedule events in the future by ticks
 * It does NOT persist on restart
 * As a counter-measure it will run all queued events when the server stops
 * 
 * @Bug1312 Does Fabric API not have this? Am I just being stoopid?
 */
public final class Scheduler {
    private static final Map<Runnable, Integer> tasks = new HashMap<>(); // <Lambda, RemainingTicks>
    private static int lastTick;
    
    public static final void scheduleTask(Runnable lambda, int ticks) {
        tasks.put(lambda, ticks);
    }

    public static final void endServerTick(MinecraftServer server) {
        Iterator<Map.Entry<Runnable, Integer>> iterator = tasks.entrySet().iterator();
        while (iterator.hasNext() && server.getTicks() != lastTick) {
        	lastTick = server.getTicks();
        	
            Map.Entry<Runnable, Integer> entry = iterator.next();
            int remainingTicks = entry.getValue() - 1;
            
            if (remainingTicks <= 0) {
                entry.getKey().run();
                iterator.remove();
            } else entry.setValue(remainingTicks);
        }
    }
    
    public static final void serverStopping(MinecraftServer server) {
        Iterator<Map.Entry<Runnable, Integer>> iterator = tasks.entrySet().iterator();
        while (iterator.hasNext()) {       	
            Map.Entry<Runnable, Integer> entry = iterator.next();
            entry.getKey().run();
            iterator.remove();
        }
    }

}
