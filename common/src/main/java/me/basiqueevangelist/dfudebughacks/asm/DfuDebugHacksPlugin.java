package me.basiqueevangelist.dfudebughacks.asm;

import net.auoeke.reflect.Reflect;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class DfuDebugHacksPlugin implements IMixinConfigPlugin {
    public static final boolean CODEC_NAMING = Boolean.getBoolean("dfudebughacks.codecNaming");

    @Override
    public void onLoad(String mixinPackage) {
        LoggerFactory.getLogger("DfuDebugHacks").info("Installing instrumentation transformer for codec naming");

        var instrumentation = Reflect.instrument()
            .valueOrGet(() -> {
                throw new IllegalStateException("Instrumentation not present. DfuDebugHacks cannot work.");
            });

        if (CODEC_NAMING) {
            instrumentation.addTransformer(new CodecNamingTransformer());
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
