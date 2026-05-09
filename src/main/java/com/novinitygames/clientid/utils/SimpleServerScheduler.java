package com.novinitygames.clientid.utils;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleServerScheduler {
    private static final Map<Long, List<Runnable>> tasks = new HashMap<>();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            long tick = server.getTicks();
            List<Runnable> runnables = tasks.remove(tick);
            if (runnables != null) {
                for (Runnable runnable : runnables) {
                    server.execute(runnable);
                }
            }
        });
    }

    public static void schedule(MinecraftServer server, Runnable task, long delayTicks) {
        long targetTick = server.getTicks() + Math.max(0, delayTicks);
        tasks.computeIfAbsent(targetTick, k -> new ArrayList<>()).add(task);
    }
}
