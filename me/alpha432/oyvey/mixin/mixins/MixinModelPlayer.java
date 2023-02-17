//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.features.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.model.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Mixin({ ModelPlayer.class })
public class MixinModelPlayer
{
    @Inject(method = { "setRotationAngles" }, at = { @At("RETURN") })
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn, final CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && entityIn instanceof EntityPlayer) {
            Skeleton.addEntity((EntityPlayer)entityIn, (ModelPlayer)ModelPlayer.class.cast(this));
        }
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void renderPre(final Entity var1, final float var2, final float var3, final float var4, final float var5, final float var6, final float var7, final CallbackInfo var8) {
        final EventModelPlayerRender var9 = new EventModelPlayerRender((ModelBase)ModelPlayer.class.cast(this), var1, var2, var3, var4, var5, var6, var7);
        var9.setStage(0);
        MinecraftForge.EVENT_BUS.post((Event)var9);
        if (var9.isCanceled()) {
            var8.cancel();
        }
    }
    
    @Inject(method = { "render" }, at = { @At("RETURN") })
    private void renderPost(final Entity var1, final float var2, final float var3, final float var4, final float var5, final float var6, final float var7, final CallbackInfo var8) {
        final EventModelPlayerRender var9 = new EventModelPlayerRender((ModelBase)ModelPlayer.class.cast(this), var1, var2, var3, var4, var5, var6, var7);
        var9.setStage(1);
        MinecraftForge.EVENT_BUS.post((Event)var9);
    }
}
