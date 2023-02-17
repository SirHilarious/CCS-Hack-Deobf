//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.features.modules.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.features.modules.movement.*;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    public MixinEntityPlayer(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn);
    }
    
    @Inject(method = { "travel" }, at = { @At("HEAD") }, cancellable = true)
    public void travel(final float strafe, final float vertical, final float forward, final CallbackInfo info) {
        final EntityPlayer us = (EntityPlayer)Minecraft.getMinecraft().player;
        if (!(us instanceof EntityPlayerSP)) {
            return;
        }
        final PlayerTravelEvent event = new PlayerTravelEvent(strafe, vertical, forward);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            this.move(MoverType.SELF, us.motionX, us.motionY, us.motionZ);
            info.cancel();
        }
    }
    
    @Inject(method = { "getCooldownPeriod" }, at = { @At("HEAD") }, cancellable = true)
    private void getCooldownPeriodHook(final CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (TpsSync.getInstance().isOn() && (boolean)TpsSync.getInstance().attack.getValue()) {
            callbackInfoReturnable.setReturnValue((Object)(float)(1.0 / EntityPlayer.class.cast(this).getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue() * 20.0 * OyVey.serverManager.getTpsFactor()));
        }
    }
    
    @Inject(method = { "isEntityInsideOpaqueBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void isEntityInsideOpaqueBlockHook(final CallbackInfoReturnable<Boolean> info) {
        if (PacketFly.getInstance().isOn()) {
            info.setReturnValue((Object)false);
        }
    }
}
