package me.basiqueevangelist.dfudebughacks.neoforge.asm;

import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.llamalad7.mixinextras.utils.ClassGenUtils;
import cpw.mods.cl.ModuleClassLoader;
import net.auoeke.reflect.Reflect;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.instrument.UnmodifiableClassException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;

public class DfuDebugHacksPlugin implements IMixinConfigPlugin {
    public static URLClassLoader FUNNY;

    @Override
    public void onLoad(String mixinPackage) {
        LoggerFactory.getLogger("DfuDebugHacks/NeoForge").info("Injecting into NeoForge to enable DFU modification");

        var instrumentation = Reflect.instrument()
            .valueOrGet(() -> {
                throw new IllegalStateException("Instrumentation not present. DfuDebugHacks cannot work.");
            });

        var passthrough = new PassthroughTransformer();


        FUNNY = new URLClassLoader(new URL[] {getUrlFor(LocalRef.class)}, ClassLoader.getPlatformClassLoader());
        var moduleCl = (ModuleClassLoader) IMixinConfigPlugin.class.getClassLoader();

        moduleCl.setFallbackClassLoader(FUNNY);

        instrumentation.addTransformer(passthrough, false);

        var genUtilsTx = new ClassGenUtilsTransformer();
        instrumentation.addTransformer(genUtilsTx, true);
        try {
            instrumentation.retransformClasses(ClassGenUtils.class);
        } catch (UnmodifiableClassException e) {
            throw new RuntimeException(e);
        }
        instrumentation.removeTransformer(genUtilsTx);
    }

    private URL getUrlFor(Class<?> klass) {
        try {
            var url = DfuDebugHacksPlugin.class
                .getClassLoader()
                .getResource(klass.getName().replace('.', '/') + ".class");
            var rootUrl = new URL(url
                .toString()
                .replace(klass.getName().replace('.', '/') + ".class", ""));

            var conn = url.openConnection();
            if (conn instanceof JarURLConnection jarURLConn)
                rootUrl = jarURLConn.getJarFileURL();

            return rootUrl;
        } catch (Throwable e) {
            throw new RuntimeException(e);
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
