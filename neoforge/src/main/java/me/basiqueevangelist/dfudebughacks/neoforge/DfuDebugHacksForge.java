package me.basiqueevangelist.dfudebughacks.neoforge;


import me.basiqueevangelist.dfudebughacks.DfuDebugHacks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(DfuDebugHacks.MOD_ID)
public class DfuDebugHacksForge {

    public DfuDebugHacksForge(IEventBus eventBus) {
        DfuDebugHacks.init();
    }
}