//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.service.mojang;

import org.spongepowered.asm.service.*;
import net.minecraft.launchwrapper.*;
import javax.annotation.*;

class LegacyTransformerHandle implements ILegacyClassTransformer
{
    private final IClassTransformer transformer;
    
    LegacyTransformerHandle(final IClassTransformer transformer) {
        this.transformer = transformer;
    }
    
    public String getName() {
        return this.transformer.getClass().getName();
    }
    
    public boolean isDelegationExcluded() {
        return this.transformer.getClass().getAnnotation(Resource.class) != null;
    }
    
    public byte[] transformClassBytes(final String name, final String transformedName, final byte[] basicClass) {
        return this.transformer.transform(name, transformedName, basicClass);
    }
}
