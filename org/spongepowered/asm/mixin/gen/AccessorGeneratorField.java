//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.tree.*;
import org.spongepowered.asm.lib.*;

public abstract class AccessorGeneratorField extends AccessorGenerator
{
    protected final FieldNode targetField;
    protected final Type targetType;
    protected final boolean isInstanceField;
    
    public AccessorGeneratorField(final AccessorInfo info) {
        super(info);
        this.targetField = info.getTargetField();
        this.targetType = info.getTargetFieldType();
        this.isInstanceField = ((this.targetField.access & 0x8) == 0x0);
    }
}
