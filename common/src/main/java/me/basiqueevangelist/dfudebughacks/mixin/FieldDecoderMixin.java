package me.basiqueevangelist.dfudebughacks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.codecs.FieldDecoder;
import me.basiqueevangelist.dfudebughacks.CodecNaming;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// TODO: neoforge support.
@Mixin(value = FieldDecoder.class, remap = false)
public class FieldDecoderMixin {
    @Shadow @Final private Decoder<?> elementCodec;

    @Shadow @Final protected String name;

    @ModifyExpressionValue(method = "decode", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"))
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
