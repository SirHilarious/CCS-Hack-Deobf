//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;

public class EventModelPlayerRender extends EventStage
{
    public float limbSwingAmount;
    public float scaleFactor;
    public ModelBase modelBase;
    public float limbSwing;
    public float netHeadYaw;
    public float ageInTicks;
    public float headPitch;
    public Entity entity;
    
    public EventModelPlayerRender(final ModelBase var1, final Entity var2, final float var3, final float var4, final float var5, final float var6, final float var7, final float var8) {
        this.modelBase = var1;
        this.entity = var2;
        this.limbSwing = var3;
        this.limbSwingAmount = var4;
        this.ageInTicks = var5;
        this.netHeadYaw = var6;
        this.headPitch = var7;
        this.scaleFactor = var8;
    }
}
