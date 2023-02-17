//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.*;

public class ParameterNode
{
    public String name;
    public int access;
    
    public ParameterNode(final String name, final int access) {
        this.name = name;
        this.access = access;
    }
    
    public void accept(final MethodVisitor mv) {
        mv.visitParameter(this.name, this.access);
    }
}
