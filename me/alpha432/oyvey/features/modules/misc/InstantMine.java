//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.util.math.*;
import me.alpha432.oyvey.features.setting.*;
import java.awt.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.network.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.features.modules.player.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.world.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;

public class InstantMine extends Module
{
    private BlockPos renderBlock;
    private BlockPos lastBlock;
    private boolean packetCancel;
    private final Timer breakTimer;
    private EnumFacing direction;
    private boolean broke;
    private final Setting<Integer> delay;
    private final Setting<Boolean> picOnly;
    
    public InstantMine() {
        super("InstantMine", "Instantly mine blocks", Category.MISC, true, false, false);
        this.packetCancel = false;
        this.breakTimer = new Timer();
        this.broke = false;
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)65, (T)0, (T)500));
        this.picOnly = (Setting<Boolean>)this.register(new Setting("PicOnly", (T)true));
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.renderBlock != null) {
            final Color color = new Color(93, 2, 198, 150);
            RenderUtil.drawBoxESP(this.renderBlock, color, false, color, 1.2f, true, true, 120, false);
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            final CPacketPlayerDigging digPacket = (CPacketPlayerDigging)event.getPacket();
            if (digPacket.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK && this.packetCancel) {
                event.setCanceled(true);
            }
        }
    }
    
    @Override
    public void onTick() {
        if (this.renderBlock == null || !this.breakTimer.passedMs(this.delay.getValue())) {
            try {
                InstantMine.mc.playerController.blockHitDelay = 0;
            }
            catch (Exception ex) {}
            return;
        }
        this.breakTimer.reset();
        if (this.picOnly.getValue() && InstantMine.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.DIAMOND_PICKAXE) {
            return;
        }
        InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.renderBlock, this.direction));
    }
    
    @SubscribeEvent
    public void onBlockEvent(final BlockEvent event) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && Speedmine.mc.playerController.curBlockDamageMP > 0.1f) {
            Speedmine.mc.playerController.isHittingBlock = true;
        }
        if (event.getStage() == 4 && BlockUtil.canBreak(event.pos)) {
            Speedmine.mc.playerController.isHittingBlock = false;
            if (this.canBreak(event.pos)) {
                if (this.lastBlock == null || event.pos.x != this.lastBlock.x || event.pos.y != this.lastBlock.y || event.pos.z != this.lastBlock.z) {
                    this.packetCancel = false;
                    InstantMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                    this.packetCancel = true;
                }
                else {
                    this.packetCancel = true;
                }
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                this.renderBlock = event.pos;
                this.lastBlock = event.pos;
                this.direction = event.facing;
                event.setCanceled(true);
            }
        }
    }
    
    private boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = InstantMine.mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, (World)InstantMine.mc.world, pos) != -1.0f;
    }
}
