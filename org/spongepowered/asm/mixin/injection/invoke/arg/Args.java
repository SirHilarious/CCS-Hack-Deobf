//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.injection.invoke.arg;

public abstract class Args
{
    protected final Object[] values;
    
    protected Args(final Object[] values) {
        this.values = values;
    }
    
    public int size() {
        return this.values.length;
    }
    
    public <T> T get(final int index) {
        return (T)this.values[index];
    }
    
    public abstract <T> void set(final int p0, final T p1);
    
    public abstract void setAll(final Object... p0);
}