//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.block.state.*;
import me.alpha432.oyvey.features.modules.player.*;
import net.minecraft.init.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.features.modules.render.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.*;
import java.util.stream.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.util.*;
import java.util.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;

public class AutoMiner extends Module
{
    ArrayList<BlockPos> positions;
    private static final BlockPos[] surroundOffset;
    public BlockPos targetBlock;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private final Timer timer;
    
    public AutoMiner() {
        super("EnemyExcavate", "Packetmines your enemies", Category.COMBAT, true, false, false);
        this.positions = new ArrayList<BlockPos>();
        this.timer = new Timer();
    }
    
    @Override
    public void onEnable() {
        Speedmine.getInstance().enable();
    }
    
    @Override
    public void onTick() {
        if (AutoMiner.mc.getRenderManager() == null || AutoMiner.mc.getRenderManager().options == null) {
            return;
        }
        if (this.currentPos != null) {
            if (!Speedmine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) || Speedmine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                this.currentPos = null;
                this.currentBlockState = null;
            }
            return;
        }
        this.GetPlayersReadyToBeCitied().forEach(pair -> {
            pair.getValue().forEach(o -> {
                if (this.targetBlock != null) {
                    if (AutoMiner.mc.player.getDistance((double)o.x, (double)o.y, (double)o.z) < AutoMiner.mc.player.getDistance((double)this.targetBlock.x, (double)this.targetBlock.y, (double)this.targetBlock.z)) {
                        this.targetBlock = o;
                    }
                }
                else {
                    this.targetBlock = o;
                }
                return;
            });
            if (this.targetBlock != null) {
                Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.targetBlock, EnumFacing.DOWN));
                Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.targetBlock, EnumFacing.DOWN));
                Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.currentPos = this.targetBlock;
                this.currentBlockState = Speedmine.mc.world.getBlockState(this.currentPos);
                Speedmine.getInstance().growAmt = 0.5;
                Speedmine.getInstance().currentPos = this.targetBlock;
                Speedmine.getInstance().currentBlockState = Speedmine.mc.world.getBlockState(this.currentPos);
                Speedmine.getInstance().timer.reset();
                this.targetBlock = null;
                this.timer.reset();
            }
        });
    }
    
    public ArrayList<CityESP.Pair<EntityPlayer, ArrayList<BlockPos>>> GetPlayersReadyToBeCitied() {
        final ArrayList<CityESP.Pair<EntityPlayer, ArrayList<BlockPos>>> players = new ArrayList<CityESP.Pair<EntityPlayer, ArrayList<BlockPos>>>();
        for (final Entity entity : (List)AutoMiner.mc.world.playerEntities.stream().filter(entityPlayer -> !OyVey.friendManager.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
            if (entity == AutoMiner.mc.player) {
                continue;
            }
            if (AutoMiner.mc.player.getDistance(entity) > 3.0f) {
                continue;
            }
            if (EntityUtil.isBurrow(entity)) {
                continue;
            }
            if (!EntityUtil.isInHole(entity)) {
                continue;
            }
            this.positions = new ArrayList<BlockPos>();
            for (int i = 0; i < 4; ++i) {
                final BlockPos o = EntityUtil.GetPositionVectorBlockPos(entity, AutoMiner.surroundOffset[i]);
                if (AutoMiner.mc.world.getBlockState(o).getBlock() == Blocks.OBSIDIAN || AutoMiner.mc.world.getBlockState(o).getBlock() == Blocks.ENDER_CHEST) {
                    boolean passCheck = false;
                    switch (i) {
                        case 0: {
                            passCheck = canPlaceCrystal(o.north(1).down());
                            break;
                        }
                        case 1: {
                            passCheck = canPlaceCrystal(o.east(1).down());
                            break;
                        }
                        case 2: {
                            passCheck = canPlaceCrystal(o.south(1).down());
                            break;
                        }
                        case 3: {
                            passCheck = canPlaceCrystal(o.west(1).down());
                            break;
                        }
                    }
                    if (passCheck) {
                        this.positions.add(o);
                    }
                }
            }
            if (this.positions.isEmpty()) {
                continue;
            }
            players.add(new CityESP.Pair<EntityPlayer, ArrayList<BlockPos>>((EntityPlayer)entity, this.positions));
        }
        return players;
    }
    
    public static boolean canPlaceCrystal(final BlockPos pos) {
        final Block block = AutoMiner.mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            final Block floor = AutoMiner.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            final Block ceil = AutoMiner.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();
            if (floor == Blocks.AIR && ceil == Blocks.AIR && AutoMiner.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    static {
        surroundOffset = new BlockPos[] { new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
    }
}
