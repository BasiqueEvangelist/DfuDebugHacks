package me.basiqueevangelist.dfudebughacks;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;

public class DfuDebugHacks {

    public static final String MOD_ID = "dfudebughacks";

    public static void init() {
        PlatformService.INSTANCE.blessClass("me.basiqueevangelist.dfudebughacks.StackTraceSupplier");
        PlatformService.INSTANCE.blessClass("me.basiqueevangelist.dfudebughacks.CodecNaming");


        CompoundTag tag = new CompoundTag();
        tag.putString("id", "minecraft:air");
        tag.putString("count", "lol");
        ItemStack.CODEC.parse(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY).createSerializationContext(NbtOps.INSTANCE), tag).getOrThrow();
    }
}