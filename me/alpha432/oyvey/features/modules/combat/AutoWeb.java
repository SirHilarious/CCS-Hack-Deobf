//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.features.setting.*;
import java.util.*;
import net.minecraft.block.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.features.modules.client.*;
import java.awt.*;
import me.alpha432.oyvey.util.*;

public class AutoWeb extends Module
{
    private EntityPlayer target;
    private Setting<Boolean> rotate;
    private Setting<Boolean> predict;
    private Setting<Boolean> lowerbody;
    private Setting<Boolean> upperBody;
    private Setting<Boolean> render;
    public Setting<Boolean> box;
    private Setting<Float> range;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    public Setting<Boolean> outline;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private List<Vec3d> placeTargets;
    
    public AutoWeb() {
        super("AutoWeb", "Traps other players in webs", Category.COMBAT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
        this.predict = (Setting<Boolean>)this.register(new Setting("Resolve", (T)true));
        this.lowerbody = (Setting<Boolean>)this.register(new Setting("LowerBody", (T)true));
        this.upperBody = (Setting<Boolean>)this.register(new Setting("UpperBody", (T)true));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)true));
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)4.0f, (T)1.0f, (T)6.0f));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true));
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            return;
        }
        this.placeTargets = new ArrayList<Vec3d>();
        this.isSneaking = AutoWeb.mc.player.isSneaking();
        this.target = null;
        this.lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
    }
    
    @Override
    public void onTick() {
        if (InventoryUtil.findHotbarBlock(BlockWeb.class) != -1) {
            this.lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
            this.target = null;
            this.getTarget();
            AutoWeb.mc.addScheduledTask(() -> this.doTrap());
        }
        else {
            Command.sendMessage("No webs in hotbar");
            this.disable();
        }
    }
    
    private void doTrap() {
        if (this.target != null) {
            this.placeTargets.forEach(vec3d -> {
                if (this.canPlace(new BlockPos(vec3d))) {
                    AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(InventoryUtil.findHotbarBlock(BlockWeb.class)));
                    this.isSneaking = BlockUtil.placeBlock(new BlockPos(vec3d), EnumHand.MAIN_HAND, this.rotate.getValue(), true, this.isSneaking);
                    AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.lastHotbarSlot));
                    EntityUtil.stopSneaking(this.isSneaking);
                }
            });
        }
    }
    
    private boolean canPlace(final BlockPos pos) {
        return AutoWeb.mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }
    
    private void getTarget() {
        this.target = null;
        this.placeTargets = new ArrayList<Vec3d>();
        AutoWeb.mc.world.playerEntities.forEach(e -> {
            if (e != null && ((EntityPlayer)e).getHealth() > 0.0f && e != AutoWeb.mc.player && AutoWeb.mc.player.getDistance((Entity)e) <= this.range.getValue() && !OyVey.friendManager.isFriend(((EntityPlayer)e).getName())) {
                this.target = (EntityPlayer)e;
            }
            return;
        });
        if (this.target != null) {
            this.placeTargets = this.getPlacements();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.placeTargets != null && this.render.getValue() && this.target != null) {
            final BlockPos pos2;
            Color rainbow = null;
            Color rainbow2 = null;
            final boolean secondC;
            this.placeTargets.forEach(pos -> {
                pos2 = new BlockPos(pos);
                if (ClickGui.getInstance().rainbow.getValue()) {
                    rainbow = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue());
                }
                else {
                    // new(java.awt.Color.class)
                    new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 75);
                }
                this.outline.getValue();
                if (ClickGui.getInstance().rainbow.getValue()) {
                    rainbow2 = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue());
                }
                else {
                    // new(java.awt.Color.class)
                    new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255);
                }
                RenderUtil.drawBoxESP(pos2, rainbow, secondC, rainbow2, this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
            });
        }
    }
    
    @Override
    public void onDisable() {
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }
    
    private List<Vec3d> getPlacements() {
        final ArrayList<Vec3d> list = new ArrayList<Vec3d>();
        final Vec3d baseVec = this.target.getPositionVector();
        if (this.lowerbody.getValue()) {
            if (this.predict.getValue()) {
                list.add(baseVec.add(0.0 + this.target.motionX, 0.0, 0.0 + this.target.motionZ));
            }
            list.add(baseVec.add(0.0, 0.0, 0.0));
        }
        if (this.upperBody.getValue()) {
            if (this.predict.getValue()) {
                list.add(baseVec.add(0.0 + this.target.motionX, 1.0, 0.0 + this.target.motionZ));
            }
            list.add(baseVec.add(0.0, 1.0, 0.0));
        }
        return list;
    }
}
