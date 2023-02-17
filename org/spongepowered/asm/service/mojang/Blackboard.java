//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.service.mojang;

import org.spongepowered.asm.service.*;
import net.minecraft.launchwrapper.*;

public class Blackboard implements IGlobalPropertyService
{
    public final <T> T getProperty(final String key) {
        return Launch.blackboard.get(key);
    }
    
    public final void setProperty(final String key, final Object value) {
        Launch.blackboard.put(key, value);
    }
    
    public final <T> T getProperty(final String key, final T defaultValue) {
        final Object value = Launch.blackboard.get(key);
        return (T)((value != null) ? value : defaultValue);
    }
    
    public final String getPropertyString(final String key, final String defaultValue) {
        final Object value = Launch.blackboard.get(key);
        return (value != null) ? value.toString() : defaultValue;
    }
}
