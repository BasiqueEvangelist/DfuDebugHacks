package me.basiqueevangelist.dfudebughacks.neoforge;

import me.basiqueevangelist.dfudebughacks.PlatformService;
import me.basiqueevangelist.dfudebughacks.neoforge.asm.DfuDebugHacksPlugin;
import net.auoeke.reflect.ClassDefiner;

import java.io.IOException;

public class PlatformServiceNeoforge implements PlatformService {
    @Override
    public void blessClass(String className) {
         try (var stream = PlatformServiceNeoforge.class.getClassLoader().getResourceAsStream(className.replace('.', '/') + ".class")) {
             var bytes = stream.readAllBytes();

             ClassDefiner.make()
                 .classFile(bytes)
                 .loader(DfuDebugHacksPlugin.FUNNY)
                 .define();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
    }
}
