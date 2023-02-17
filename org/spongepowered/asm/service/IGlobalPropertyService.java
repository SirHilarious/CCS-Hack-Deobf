//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.service;

public interface IGlobalPropertyService
{
     <T> T getProperty(final String p0);
    
    void setProperty(final String p0, final Object p1);
    
     <T> T getProperty(final String p0, final T p1);
    
    String getPropertyString(final String p0, final String p1);
}
