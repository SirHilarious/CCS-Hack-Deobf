//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.util.math.*;
import me.alpha432.oyvey.features.modules.client.*;
import java.awt.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Phase extends Module
{
    private Setting<Double> speed;
    private BlockPos renderBlock;
    
    public Phase() {
        super("Phase", "Phases you", Module.Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (T)0.4, (T)0.01, (T)10.0));
    }
    
    public void onToggle() {
        this.renderBlock = null;
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (this.renderBlock != null) {
            RenderUtil.drawBoxESP(this.renderBlock, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(255, 0, 0, 75), true, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(255, 0, 0, 255), 1.2f, true, true, 75, true);
        }
    }
    
    private boolean checkHitBoxes() {
        return !Phase.mc.world.getCollisionBoxes((Entity)Phase.mc.player, Phase.mc.player.getEntityBoundingBox().expand(-0.0625, 0.0, -0.0625)).isEmpty();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (Phase.mc.player.isSneaking() && this.checkHitBoxes()) {
            final float rotationYaw2 = Phase.mc.player.rotationYaw;
            final double cos = Math.cos(Math.toRadians(rotationYaw2 + 90.0f));
            final double sin = Math.sin(Math.toRadians(rotationYaw2 + 90.0f));
            Phase.mc.player.setEntityBoundingBox(Phase.mc.player.getEntityBoundingBox().offset(this.speed.getValue() / 10.0 * cos, 0.0, this.speed.getValue() / 10.0 * sin));
            Phase.mc.player.motionY = 0.0;
            if (Phase.mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP player = Phase.mc.player;
                player.motionY += 4.24399158E-315;
            }
            if (Phase.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP player2 = Phase.mc.player;
                player2.motionY -= 4.24399158E-315;
            }
        }
    }
}
