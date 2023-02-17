//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;

public class RenderEntityModelEvent extends EventStage
{
    public ModelBase modelBase;
    public RenderLivingBase renderer;
    public Entity entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float age;
    public float headYaw;
    public float headPitch;
    public float scale;
    
    public RenderEntityModelEvent(final RenderLivingBase renderer, final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final int stage) {
        this.modelBase = modelBase;
        this.renderer = renderer;
        this.entity = entityIn;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.age = ageInTicks;
        this.headYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
        this.setStage(stage);
    }
    
    public ModelBase getModelBase() {
        return this.modelBase;
    }
    
    public void setModelBase(final ModelBase modelBase) {
        this.modelBase = modelBase;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
    
    public float getLimbSwing() {
        return this.limbSwing;
    }
    
    public void setLimbSwing(final float limbSwing) {
        this.limbSwing = limbSwing;
    }
    
    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }
    
    public void setLimbSwingAmount(final float limbSwingAmount) {
        this.limbSwingAmount = limbSwingAmount;
    }
    
    public float getAge() {
        return this.age;
    }
    
    public void setAge(final float age) {
        this.age = age;
    }
    
    public float getHeadYaw() {
        return this.headYaw;
    }
    
    public void setHeadYaw(final float headYaw) {
        this.headYaw = headYaw;
    }
    
    public float getHeadPitch() {
        return this.headPitch;
    }
    
    public void setHeadPitch(final float headPitch) {
        this.headPitch = headPitch;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public void setScale(final float scale) {
        this.scale = scale;
    }
}
