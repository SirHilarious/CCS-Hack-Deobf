//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.throwables;

public class MixinException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public MixinException() {
    }
    
    public MixinException(final String message) {
        super(message);
    }
    
    public MixinException(final Throwable cause) {
        super(cause);
    }
    
    public MixinException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
