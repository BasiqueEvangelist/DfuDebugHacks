package me.basiqueevangelist.dfudebughacks.asm;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class CodecNamingTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (loader != CodecNamingTransformer.class.getClassLoader()) return null;

        ClassReader cr = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);

        var cv = new CVisitor(cw);

        cr.accept(cv, 0);

        if (!cv.changed) return null;

        byte[] bytes = cw.toByteArray();

//        try {
//            var dumpPath = Path.of("class-dumps/" + className + ".class");
//            dumpPath.getParent().toFile().mkdirs();
//            Files.write(dumpPath, bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return bytes;
    }

    private static class CVisitor extends ClassVisitor {
        private boolean changed = false;

        public CVisitor(ClassVisitor delegate) {
            super(Opcodes.ASM9, delegate);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            return new MVisitor(super.visitMethod(access, name, descriptor, signature, exceptions));
        }

        private class MVisitor extends MethodVisitor {
            public MVisitor(MethodVisitor delegate) {
                super(Opcodes.ASM9, delegate);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                if (opcode == Opcodes.PUTSTATIC &&
                    (descriptor.equals("Lcom/mojang/serialization/Codec;")
                    || descriptor.equals("Lcom/mojang/serialization/codecs/PrimitiveCodec;"))) {
                    mv.visitLdcInsn(owner + "#" + name);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "me/basiqueevangelist/dfudebughacks/CodecNaming", "nameCodec", "(Lcom/mojang/serialization/Codec;Ljava/lang/String;)Lcom/mojang/serialization/Codec;", false);
                    changed = true;
                }

                super.visitFieldInsn(opcode, owner, name, descriptor);
            }
        }
    }


}
