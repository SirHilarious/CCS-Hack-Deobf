//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.*;
import me.alpha432.oyvey.features.modules.render.*;
import java.awt.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RenderItem.class })
public class MixinRenderItem
{
    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderModel(final int a1) {
        if (EnchantColour.INSTANCE != null && EnchantColour.INSTANCE.isEnabled()) {
            return new Color((int)EnchantColour.INSTANCE.getR().getValue(), (int)EnchantColour.INSTANCE.getG().getValue(), (int)EnchantColour.INSTANCE.getB().getValue()).getRGB();
        }
        return a1;
    }
}
