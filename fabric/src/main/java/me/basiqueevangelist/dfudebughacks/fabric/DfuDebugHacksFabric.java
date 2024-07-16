package me.basiqueevangelist.dfudebughacks.fabric;

import me.basiqueevangelist.dfudebughacks.DfuDebugHacks;
import net.fabricmc.api.ModInitializer;

public class DfuDebugHacksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DfuDebugHacks.init();
    }
}
