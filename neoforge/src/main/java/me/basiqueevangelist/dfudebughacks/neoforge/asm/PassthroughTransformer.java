package me.basiqueevangelist.dfudebughacks.neoforge.asm;

import cpw.mods.modlauncher.ClassTransformer;
import cpw.mods.modlauncher.TransformingClassLoader;

import java.lang.instrument.ClassFileTransformer;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;

public class PassthroughTransformer implements ClassFileTransformer {
    private final MethodHandle transformHandle;
    private final ClassTransformer transformer;

    public PassthroughTransformer() {
        try {
            var cl = (TransformingClassLoader) PassthroughTransformer.class.getClassLoader();

            Field transformerField = TransformingClassLoader.class.getDeclaredField("classTransformer");
            transformerField.setAccessible(true);
            this.transformer = (ClassTransformer) transformerField.get(cl);

            var transformMethod = ClassTransformer.class.getDeclaredMethod("transform", byte[].class, String.class, String.class);
            transformMethod.setAccessible(true);
            this.transformHandle = MethodHandles.lookup().unreflect(transformMethod);

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className.startsWith("com/mojang/serialization/")) {
            try {
                return (byte[]) transformHandle.invoke(transformer, classfileBuffer, className, "dfudebughacks:instrumentation_passthrough");
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
