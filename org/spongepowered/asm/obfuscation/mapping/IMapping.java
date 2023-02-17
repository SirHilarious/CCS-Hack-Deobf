//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.obfuscation.mapping;

public interface IMapping<TMapping>
{
    Type getType();
    
    TMapping move(final String p0);
    
    TMapping remap(final String p0);
    
    TMapping transform(final String p0);
    
    TMapping copy();
    
    String getName();
    
    String getSimpleName();
    
    String getOwner();
    
    String getDesc();
    
    TMapping getSuper();
    
    String serialise();
    
    public enum Type
    {
        FIELD, 
        METHOD, 
        CLASS, 
        PACKAGE;
    }
}
