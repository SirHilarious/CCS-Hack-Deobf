//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.entity.player.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import org.lwjgl.util.glu.*;

public class PenisESP extends Module
{
    private float pspin;
    private float pcumsize;
    private float pamount;
    
    public PenisESP() {
        super("PenisESP", "Makes u have many big pp", Module.Category.RENDER, true, false, false);
    }
    
    public void onEnable() {
        this.pspin = 0.0f;
        this.pcumsize = 0.0f;
        this.pamount = 0.0f;
    }
    
    public void onRender3D(final Render3DEvent event) {
        for (final Object o : PenisESP.mc.world.loadedEntityList) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)o;
                final double n = player.lastTickPosX + (player.posX - player.lastTickPosX) * PenisESP.mc.timer.renderPartialTicks;
                PenisESP.mc.getRenderManager();
                final double x = n - PenisESP.mc.renderManager.renderPosX;
                final double n2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * PenisESP.mc.timer.renderPartialTicks;
                PenisESP.mc.getRenderManager();
                final double y = n2 - PenisESP.mc.renderManager.renderPosY;
                final double n3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * PenisESP.mc.timer.renderPartialTicks;
                PenisESP.mc.getRenderManager();
                final double z = n3 - PenisESP.mc.renderManager.renderPosZ;
                GL11.glPushMatrix();
                RenderHelper.disableStandardItemLighting();
                this.esp(player, x, y, z);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
            ++this.pamount;
            if (this.pamount > 25.0f) {
                ++this.pspin;
                if (this.pspin > 50.0f) {
                    this.pspin = -50.0f;
                }
                else if (this.pspin < -50.0f) {
                    this.pspin = 50.0f;
                }
                this.pamount = 0.0f;
            }
            ++this.pcumsize;
            if (this.pcumsize > 180.0f) {
                this.pcumsize = -180.0f;
            }
            else {
                if (this.pcumsize >= -180.0f) {
                    continue;
                }
                this.pcumsize = 180.0f;
            }
        }
    }
    
    public void esp(final EntityPlayer player, final double x, final double y, final double z) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-player.rotationYaw, 0.0f, player.height, 0.0f);
        GL11.glTranslated(-x, -y, -z);
        GL11.glTranslated(x, y + player.height / 2.0f - 0.22499999403953552, z);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glRotated((double)((player.isSneaking() ? 35 : 0) + this.pspin), (double)(1.0f + this.pspin), 0.0, (double)this.pcumsize);
        GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
        final Cylinder shaft = new Cylinder();
        shaft.setDrawStyle(100013);
        shaft.draw(0.1f, 0.11f, 0.4f, 25, 20);
        GL11.glColor4f(1.38f, 0.85f, 1.38f, 1.0f);
        GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
        GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
        final Sphere right = new Sphere();
        right.setDrawStyle(100013);
        right.draw(0.14f, 10, 20);
        GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
        final Sphere left = new Sphere();
        left.setDrawStyle(100013);
        left.draw(0.14f, 10, 20);
        GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);
        final Sphere tip = new Sphere();
        tip.setDrawStyle(100013);
        tip.draw(0.13f, 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }
}
