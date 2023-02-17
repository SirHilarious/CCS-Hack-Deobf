//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.client.entity.*;
import me.alpha432.oyvey.features.modules.movement.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ EntityLivingBase.class })
public abstract class MixinEntityLivingBase extends Entity
{
    public MixinEntityLivingBase(final World worldIn) {
        super(worldIn);
    }
    
    @Inject(method = { "isElytraFlying" }, at = { @At("HEAD") }, cancellable = true)
    private void isElytraFlying(final CallbackInfoReturnable<Boolean> var1) {
        final Entity var2 = this;
        if (Util.mc.player != null && var2 instanceof EntityPlayerSP) {
            final ElytraFly2 elytraFly2 = ElytraFly2.getINSTANCE();
            if (elytraFly2 != null && elytraFly2.isOn()) {
                var1.setReturnValue((Object)false);
            }
        }
    }
}
