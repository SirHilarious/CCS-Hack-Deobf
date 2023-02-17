//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.item.*;
import java.io.*;

public class GappleFinder extends Module
{
    public GappleFinder() {
        super("GappleFinder", "", Category.MISC, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (GappleFinder.mc.world.isRemote) {
            return;
        }
        final World world = (World)GappleFinder.mc.world;
        if (!world.playerEntities.isEmpty()) {
            final EntityPlayer player = world.playerEntities.get(0);
            for (final TileEntity tile : world.loadedTileEntityList) {
                if (tile instanceof TileEntityLockableLoot) {
                    final TileEntityLockableLoot lockable = (TileEntityLockableLoot)tile;
                    if (lockable.getLootTable() == null) {
                        continue;
                    }
                    lockable.fillWithLoot(player);
                    for (int i = 0; i < lockable.getSizeInventory(); ++i) {
                        final ItemStack stack = lockable.getStackInSlot(i);
                        if (stack.getItem() == Items.GOLDEN_APPLE && stack.getItemDamage() == 1) {
                            writeToFile("Dungeon Chest with ench gapple at: " + lockable.getPos().getX() + " " + lockable.getPos().getY() + " " + lockable.getPos().getZ());
                        }
                        if (stack.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0) {
                            writeToFile("Dungeon Chest with Mending Book: " + lockable.getPos().getX() + " " + lockable.getPos().getY() + " " + lockable.getPos().getZ());
                        }
                    }
                }
            }
            for (final Entity entity : world.loadedEntityList) {
                if (entity instanceof EntityMinecartContainer) {
                    final EntityMinecartContainer cart = (EntityMinecartContainer)entity;
                    if (cart.getLootTable() == null) {
                        continue;
                    }
                    cart.addLoot(player);
                    for (int i = 0; i < cart.itemHandler.getSlots(); ++i) {
                        final ItemStack stack = cart.itemHandler.getStackInSlot(i);
                        if (stack.getItem() == Items.GOLDEN_APPLE && stack.getItemDamage() == 1) {
                            writeToFile("Minecart with ench gapple at: " + cart.posX + " " + cart.posY + " " + cart.posZ);
                        }
                        if (stack.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0) {
                            writeToFile("Minecart with Mending at: " + cart.posX + " " + cart.posY + " " + cart.posZ);
                        }
                    }
                }
            }
        }
    }
    
    protected static void writeToFile(final String coords) {
        try (final FileWriter fw = new FileWriter("FoundCoords.txt", true);
             final BufferedWriter bw = new BufferedWriter(fw);
             final PrintWriter out = new PrintWriter(bw)) {
            out.println(coords);
        }
        catch (IOException ex) {}
    }
}
