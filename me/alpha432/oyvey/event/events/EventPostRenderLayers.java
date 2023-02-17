//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;

public class EventPostRenderLayers extends EventStage
{
    public float limbSwingAmount;
    public ModelBase modelBase;
    public RenderLivingBase renderer;
    public float headPitch;
    public float netHeadYaw;
    public float limbSwing;
    public float partialTicks;
    public EntityLivingBase entity;
    public float scaleIn;
    public float ageInTicks;
    
    public RenderLivingBase getRenderer() {
        return this.renderer;
    }
    
    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }
    
    public float getHeadPitch() {
        return this.headPitch;
    }
    
    public float getNetHeadYaw() {
        return this.netHeadYaw;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public ModelBase getModelBase() {
        return this.modelBase;
    }
    
    public float getAgeInTicks() {
        return this.ageInTicks;
    }
    
    public float getScaleIn() {
        return this.scaleIn;
    }
    
    public float getLimbSwing() {
        return this.limbSwing;
    }
    
    public EventPostRenderLayers(final RenderLivingBase var1, final ModelBase var2, final EntityLivingBase var3, final float var4, final float var5, final float var6, final float var7, final float var8, final float var9, final float var10) {
        this.renderer = var1;
        this.modelBase = var2;
        this.entity = var3;
        this.limbSwing = var4;
        this.limbSwingAmount = var5;
        this.partialTicks = var6;
        this.ageInTicks = var7;
        this.netHeadYaw = var8;
        this.headPitch = var9;
        this.scaleIn = var10;
    }
    
    public EntityLivingBase getEntitylivingbaseIn() {
        return this.entity;
    }
}
