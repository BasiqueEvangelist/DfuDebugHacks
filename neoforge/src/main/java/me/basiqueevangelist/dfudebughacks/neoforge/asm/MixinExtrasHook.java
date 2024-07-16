package me.basiqueevangelist.dfudebughacks.neoforge.asm;

import me.basiqueevangelist.dfudebughacks.HookMethod;
import net.auoeke.reflect.ClassDefiner;

public class MixinExtrasHook {
    @HookMethod
    public static void defineClass(String name, byte[] bytes) {
        ClassDefiner.make()
            .name(name)
            .classFile(bytes)
            .loader(DfuDebugHacksPlugin.FUNNY)
            .define();
    }
}
