//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.tools.obfuscation.service;

import java.util.*;

public interface IObfuscationService
{
    Set<String> getSupportedOptions();
    
    Collection<ObfuscationTypeDescriptor> getObfuscationTypes();
}
