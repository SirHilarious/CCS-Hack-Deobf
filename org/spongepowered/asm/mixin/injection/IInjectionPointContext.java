//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.injection;

import org.spongepowered.asm.mixin.refmap.*;
import org.spongepowered.asm.lib.tree.*;

public interface IInjectionPointContext
{
    IMixinContext getContext();
    
    MethodNode getMethod();
    
    AnnotationNode getAnnotation();
}
