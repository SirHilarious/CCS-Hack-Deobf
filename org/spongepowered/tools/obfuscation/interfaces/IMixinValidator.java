//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.tools.obfuscation.interfaces;

import javax.lang.model.element.*;
import java.util.*;
import org.spongepowered.tools.obfuscation.mirror.*;

public interface IMixinValidator
{
    boolean validate(final ValidationPass p0, final TypeElement p1, final AnnotationHandle p2, final Collection<TypeHandle> p3);
    
    public enum ValidationPass
    {
        EARLY, 
        LATE, 
        FINAL;
    }
}
