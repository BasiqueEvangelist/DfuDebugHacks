package me.basiqueevangelist.dfudebughacks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.FieldEncoder;
import me.basiqueevangelist.dfudebughacks.CodecNaming;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// TODO: neoforge support.
@Mixin(value = FieldEncoder.class, remap = false)
public class FieldEncoderMixin {
    @Shadow @Final private Encoder<?> elementCodec;

    @Shadow @Final private String name;

    @ModifyExpressionValue(method = "encode", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Encoder;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"))
    private <T> DataResult<T> addCodecContext(DataResult<T> original) {
        if (elementCodec instanceof Codec<?> codec) {
            String codecName = CodecNaming.getName(codec);

            if (codecName != null) {
                return original.mapError(err -> "Error in field " + name + " of " + codecName + ": " + err);
            }
        }

        return original;
    }
}
