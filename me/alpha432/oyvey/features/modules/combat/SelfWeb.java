//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.features.command.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.util.math.*;
import me.alpha432.oyvey.features.modules.client.*;
import java.awt.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.util.*;

public class SelfWeb extends Module
{
    private final Setting<Boolean> autoOff;
    private final Setting<Boolean> render;
    public Setting<Boolean> box;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    public Setting<Boolean> outline;
    private BlockPos playerPos;
    private int webSlot;
    private int oldSlot;
    
    public SelfWeb() {
        super("SelfWeb", "Automatically places a web inside your player", Category.COMBAT, true, false, false);
        this.autoOff = (Setting<Boolean>)this.register(new Setting("Auto Disable", (T)true));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)true));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true));
        this.webSlot = -1;
        this.oldSlot = -1;
    }
    
    @Override
    public void onEnable() {
        this.webSlot = InventoryUtil.findHotbarBlock(Blocks.WEB);
        this.oldSlot = SelfWeb.mc.player.inventory.currentItem;
        if (this.webSlot == -1) {
            Command.sendMessage("No Webs In Hotbar");
            this.disable();
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.playerPos != null && this.render.getValue()) {
            RenderUtil.drawBoxESP(new BlockPos((Vec3i)this.playerPos), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 75), this.outline.getValue(), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
        }
    }
    
    @Override
    public void onUpdate() {
        this.webSlot = InventoryUtil.findHotbarBlock(Blocks.WEB);
        this.oldSlot = SelfWeb.mc.player.inventory.currentItem;
        if (this.webSlot == -1) {
            Command.sendMessage("No Webs In Hotbar");
            this.disable();
            return;
        }
        this.playerPos = this.getPos();
        if (this.playerPos != null) {
            SelfWeb.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.webSlot));
            BlockUtil.placeBlock(this.playerPos, EnumHand.MAIN_HAND, true, true, SelfWeb.mc.player.isSneaking());
            SelfWeb.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlot));
        }
        if (this.autoOff.getValue()) {
            this.disable();
        }
    }
    
    private BlockPos getPos() {
        final BlockPos blockPos = new BlockPos(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY, SelfWeb.mc.player.posZ);
        if (SelfWeb.mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
            return blockPos;
        }
        return null;
    }
}
