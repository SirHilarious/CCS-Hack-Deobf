//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.util;

public final class Counter
{
    public int value;
    
    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj.getClass() == Counter.class && ((Counter)obj).value == this.value;
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
}
