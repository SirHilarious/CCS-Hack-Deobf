//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.service;

public interface ILegacyClassTransformer extends ITransformer
{
    String getName();
    
    boolean isDelegationExcluded();
    
    byte[] transformClassBytes(final String p0, final String p1, final byte[] p2);
}
