//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.*;
import me.alpha432.oyvey.features.modules.misc.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.lang.reflect.*;
import org.spongepowered.asm.mixin.injection.*;
import io.netty.channel.*;

@Mixin({ NetworkManager.class })
public class MixinNetworkManager
{
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onSendPacketPre(final Packet<?> packet, final CallbackInfo info) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null && Inspector.getInstance().isOn() && (boolean)Inspector.getInstance().send.getValue()) {
            System.out.println("------- New Send -------");
            System.out.println(packet.getClass());
            try {
                for (Class clazz = packet.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                    for (final Field field : clazz.getDeclaredFields()) {
                        if (field != null) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            System.out.println(StringUtils.stripControlCodes("      " + field.getType().getSimpleName() + " " + field.getName() + " = " + field.get(packet)));
                            field.setAccessible(false);
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        final PacketEvent.Send event = new PacketEvent.Send(0, (Packet)packet);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void onChannelReadPre(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo info) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null && Inspector.getInstance().isOn() && (boolean)Inspector.getInstance().recv.getValue()) {
            System.out.println("------- New Recv -------");
            System.out.println(packet.getClass());
            try {
                for (Class clazz = packet.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                    for (final Field field : clazz.getDeclaredFields()) {
                        if (field != null) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            System.out.println(StringUtils.stripControlCodes("      " + field.getType().getSimpleName() + " " + field.getName() + " = " + field.get(packet)));
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        final PacketEvent.Receive event = new PacketEvent.Receive(0, (Packet)packet);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }
}
