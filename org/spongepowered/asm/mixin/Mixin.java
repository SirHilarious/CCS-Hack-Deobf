//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
public @interface Mixin {
    Class<?>[] value() default {};
    
    String[] targets() default {};
    
    int priority() default 1000;
    
    boolean remap() default true;
}
