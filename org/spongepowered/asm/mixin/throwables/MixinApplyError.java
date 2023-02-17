//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.throwables;

public class MixinApplyError extends Error
{
    private static final long serialVersionUID = 1L;
    
    public MixinApplyError(final String message) {
        super(message);
    }
    
    public MixinApplyError(final Throwable cause) {
        super(cause);
    }
    
    public MixinApplyError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
