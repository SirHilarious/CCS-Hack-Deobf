//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.block.*;

public class HoleManager extends Feature
{
    private static final BlockPos[] surroundOffset;
    private final List<BlockPos> midSafety;
    private List<BlockPos> holes;
    
    public HoleManager() {
        this.midSafety = new ArrayList<BlockPos>();
        this.holes = new ArrayList<BlockPos>();
    }
    
    public void update() {
        if (!fullNullCheck()) {
            this.holes = this.calcHoles();
        }
    }
    
    public List<BlockPos> getHoles() {
        return this.holes;
    }
    
    public List<BlockPos> getMidSafety() {
        return this.midSafety;
    }
    
    public List<BlockPos> getSortedHoles() {
        this.holes.sort(Comparator.comparingDouble(hole -> HoleManager.mc.player.getDistanceSq(hole)));
        return this.getHoles();
    }
    
    public List<BlockPos> calcHoles() {
        final ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        this.midSafety.clear();
        final List<BlockPos> positions = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)HoleManager.mc.player), 6.0f, 6, false, true, 0);
        for (final BlockPos pos : positions) {
            if (HoleManager.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && HoleManager.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                if (!HoleManager.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                    continue;
                }
                boolean isSafe = true;
                boolean midSafe = true;
                for (final BlockPos offset : HoleManager.surroundOffset) {
                    final Block block = HoleManager.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                    if (BlockUtil.isBlockUnSolid(block)) {
                        midSafe = false;
                    }
                    if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST) {
                        if (block != Blocks.ANVIL) {
                            isSafe = false;
                        }
                    }
                }
                if (isSafe) {
                    safeSpots.add(pos);
                }
                if (!midSafe) {
                    continue;
                }
                this.midSafety.add(pos);
            }
        }
        return safeSpots;
    }
    
    public boolean isSafe(final BlockPos pos) {
        boolean isSafe = true;
        for (final BlockPos offset : HoleManager.surroundOffset) {
            final Block block = HoleManager.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
            if (block != Blocks.BEDROCK) {
                isSafe = false;
                break;
            }
        }
        return isSafe;
    }
    
    static {
        surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0.0, true));
    }
}
