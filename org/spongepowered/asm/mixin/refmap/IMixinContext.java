//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.refmap;

import org.spongepowered.asm.mixin.extensibility.*;
import org.spongepowered.asm.mixin.transformer.ext.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.lib.tree.*;
import org.spongepowered.asm.mixin.injection.struct.*;

public interface IMixinContext
{
    IMixinInfo getMixin();
    
    Extensions getExtensions();
    
    String getClassName();
    
    String getClassRef();
    
    String getTargetClassRef();
    
    IReferenceMapper getReferenceMapper();
    
    boolean getOption(final MixinEnvironment.Option p0);
    
    int getPriority();
    
    Target getTargetMethod(final MethodNode p0);
}
