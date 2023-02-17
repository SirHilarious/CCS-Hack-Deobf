//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.extensibility;

import org.spongepowered.asm.mixin.*;

public interface IEnvironmentTokenProvider
{
    public static final int DEFAULT_PRIORITY = 1000;
    
    int getPriority();
    
    Integer getToken(final String p0, final MixinEnvironment p1);
}
