//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.launch;

public class MixinInitialisationError extends Error
{
    private static final long serialVersionUID = 1L;
    
    public MixinInitialisationError() {
    }
    
    public MixinInitialisationError(final String message) {
        super(message);
    }
    
    public MixinInitialisationError(final Throwable cause) {
        super(cause);
    }
    
    public MixinInitialisationError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
