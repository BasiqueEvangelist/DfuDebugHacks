package me.basiqueevangelist.dfudebughacks.neoforge.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;

public class ClassGenUtilsTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className.equals("com/llamalad7/mixinextras/utils/ClassGenUtils")) {
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);

            for (var m : node.methods) {
                if (m.name.equals("lambda$static$0")) {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/basiqueevangelist/dfudebughacks/neoforge/asm/MixinExtrasHook", "defineClass", "(Ljava/lang/String;[B)V", false));

                    m.instructions.insert(list);
                } else if (m.name.equals("lambda$static$1")) {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/basiqueevangelist/dfudebughacks/neoforge/asm/MixinExtrasHook", "defineClass", "(Ljava/lang/String;[B)V", false));

                    m.instructions.insert(list);
                }
            }

            ClassWriter wr = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            node.accept(wr);

            byte[] bytes = wr.toByteArray();

            if (false) {
                try {
                    Files.write(Path.of("ClassGenUtils.class"), bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return bytes;
        }

        return null;
    }
}
