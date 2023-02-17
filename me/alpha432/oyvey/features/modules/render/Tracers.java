//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.client.entity.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import me.alpha432.oyvey.util.*;
import java.util.function.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.*;
import me.alpha432.oyvey.*;
import java.awt.*;
import net.minecraft.entity.monster.*;
import java.util.*;

public class Tracers extends Module
{
    private HueCycler cycler;
    private Setting<Integer> range;
    private Setting<Float> width;
    private Setting<Float> opacity;
    private Setting<Boolean> players;
    private Setting<Boolean> mobs;
    private Setting<Boolean> animals;
    private Setting<Boolean> spine;
    private int colour;
    private float r;
    private float g;
    private float b;
    private EntityPlayerSP entityPlayerSP;
    
    public Tracers() {
        super("Tracers", "Draws a line to other players", Module.Category.RENDER, true, false, false);
        this.range = (Setting<Integer>)this.register(new Setting("Range", (T)30, (T)1, (T)150));
        this.width = (Setting<Float>)this.register(new Setting("Width", (T)1.2f, (T)0.1f, (T)2.0f));
        this.opacity = (Setting<Float>)this.register(new Setting("Opacity", (T)1.0f, (T)0.0f, (T)1.0f));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", (T)true));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Monsters", (T)false));
        this.animals = (Setting<Boolean>)this.register(new Setting("Animals", (T)false));
        this.spine = (Setting<Boolean>)this.register(new Setting("Spine", (T)false));
        this.cycler = new HueCycler(3600);
    }
    
    public void onRender3D(final Render3DEvent event) {
        this.entityPlayerSP = (EntityPlayerSP)((Tracers.mc.getRenderViewEntity() == null) ? Tracers.mc.player : Tracers.mc.getRenderViewEntity());
        GlStateManager.pushMatrix();
        final Minecraft mc = Tracers.mc;
        Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> !EntityUtil.isFakeLocalPlayer(entity)).filter(entity -> (entity instanceof EntityPlayer) ? (this.players.getValue() && this.entityPlayerSP != entity) : (EntityUtil.isPassive(entity) ? this.animals.getValue() : ((boolean)this.mobs.getValue()))).filter(entity -> this.entityPlayerSP.getDistance(entity) < this.range.getValue()).forEach(entity -> {
            this.colour = this.getColour(entity);
            if (this.colour == Integer.MIN_VALUE) {
                this.colour = this.cycler.current();
            }
            this.r = (this.colour >>> 16 & 0xFF) / 255.0f;
            this.g = (this.colour >>> 8 & 0xFF) / 255.0f;
            this.b = (this.colour & 0xFF) / 255.0f;
            this.drawLineToEntity(entity, this.r, this.g, this.b, this.opacity.getValue());
            return;
        });
        GlStateManager.popMatrix();
    }
    
    private void drawRainbowToEntity(final Entity entity, final float opacity) {
        final Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-(float)Math.toRadians(Tracers.mc.player.rotationPitch)).rotateYaw(-(float)Math.toRadians(Tracers.mc.player.rotationYaw));
        final double[] xyz = this.interpolate(entity);
        final double posx = xyz[0];
        final double posy = xyz[1];
        final double posz = xyz[2];
        final double posx2 = eyes.x;
        final double posy2 = eyes.y + this.entityPlayerSP.getEyeHeight();
        final double posz2 = eyes.z;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        this.cycler.reset();
        this.cycler.setNext(opacity);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        Tracers.mc.entityRenderer.orientCamera(Tracers.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        this.cycler.setNext(opacity);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.enableLighting();
    }
    
    private int getColour(final Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (OyVey.friendManager.isFriend(entity.getName())) {
                return new Color(0.27f, 0.7f, 0.92f).getRGB();
            }
            final float distance = this.entityPlayerSP.getDistance(entity);
            if (distance <= 32.0f) {
                return new Color(1.0f - distance / 32.0f / 2.0f, distance / 32.0f, 0.0f).getRGB();
            }
            return new Color(0.0f, 0.9f, 0.0f).getRGB();
        }
        else {
            if (EntityUtil.isPassive(entity)) {
                return new Color(0, 255, 0).getRGB();
            }
            return new Color(255, 0, 0).getRGB();
        }
    }
    
    public double interpolate(final double now, final double then) {
        return then + (now - then) * Tracers.mc.getRenderPartialTicks();
    }
    
    public double[] interpolate(final Entity entity) {
        final double posX = this.interpolate(entity.posX, entity.lastTickPosX) - Tracers.mc.getRenderManager().renderPosX;
        final double posY = this.interpolate(entity.posY, entity.lastTickPosY) - Tracers.mc.getRenderManager().renderPosY;
        final double posZ = this.interpolate(entity.posZ, entity.lastTickPosZ) - Tracers.mc.getRenderManager().renderPosZ;
        return new double[] { posX, posY, posZ };
    }
    
    public void drawLineToEntity(final Entity e, final float red, final float green, final float blue, final float opacity) {
        final double[] xyz = this.interpolate(e);
        this.drawLine(xyz[0], xyz[1], xyz[2], e.height, red, green, blue, opacity);
    }
    
    public void drawLine(final double posx, final double posy, final double posz, final double up, final float red, final float green, final float blue, final float opacity) {
        final Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-(float)Math.toRadians(Tracers.mc.player.rotationPitch)).rotateYaw(-(float)Math.toRadians(Tracers.mc.player.rotationYaw));
        this.drawLineFromPosToPos(eyes.x, eyes.y + this.entityPlayerSP.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue, opacity);
    }
    
    public void drawLineFromPosToPos(final double posx, final double posy, final double posz, final double posx2, final double posy2, final double posz2, final double up, final float red, final float green, final float blue, final float opacity) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth((float)this.width.getValue());
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, opacity);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLoadIdentity();
        final boolean bobbing = Tracers.mc.gameSettings.viewBobbing;
        Tracers.mc.gameSettings.viewBobbing = false;
        Tracers.mc.entityRenderer.orientCamera(Tracers.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        if (this.spine.getValue()) {
            GL11.glVertex3d(posx2, posy2, posz2);
            GL11.glVertex3d(posx2, posy2 + up, posz2);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glColor3d(1.0, 1.0, 1.0);
        Tracers.mc.gameSettings.viewBobbing = bobbing;
    }
    
    private EntityType getType(final Entity entity) {
        if (EntityUtil.isDrivenByPlayer(entity) || EntityUtil.isFakeLocalPlayer(entity)) {
            return EntityType.INVALID;
        }
        if (EntityUtil.isPlayer(entity)) {
            return EntityType.PLAYER;
        }
        if (EntityUtil.isPassive(entity)) {
            return EntityType.ANIMAL;
        }
        if (!EntityUtil.isPassive(entity) || entity instanceof EntitySpider) {
            return EntityType.HOSTILE;
        }
        return EntityType.HOSTILE;
    }
    
    private enum EntityType
    {
        PLAYER, 
        HOSTILE, 
        ANIMAL, 
        INVALID;
    }
    
    private class EntityRelations implements Comparable<EntityRelations>
    {
        private final Entity entity;
        private final EntityType entityType;
        
        public EntityRelations(final Entity entity) {
            Objects.requireNonNull(entity);
            this.entity = entity;
            this.entityType = Tracers.this.getType(entity);
        }
        
        public Entity getEntity() {
            return this.entity;
        }
        
        public EntityType getEntityType() {
            return this.entityType;
        }
        
        public Color getColor() {
            switch (this.entityType) {
                case PLAYER: {
                    return Color.YELLOW;
                }
                case HOSTILE: {
                    return Color.RED;
                }
                default: {
                    return Color.GREEN;
                }
            }
        }
        
        public float getDepth() {
            switch (this.entityType) {
                case PLAYER: {
                    return 15.0f;
                }
                case HOSTILE: {
                    return 10.0f;
                }
                case ANIMAL: {
                    return 5.0f;
                }
                default: {
                    return 0.0f;
                }
            }
        }
        
        public boolean isOptionEnabled() {
            switch (this.entityType) {
                case PLAYER: {
                    return Tracers.this.players.getValue();
                }
                case HOSTILE: {
                    return Tracers.this.mobs.getValue();
                }
                default: {
                    return Tracers.this.animals.getValue();
                }
            }
        }
        
        @Override
        public int compareTo(final EntityRelations o) {
            return this.getEntityType().compareTo(o.getEntityType());
        }
    }
}
