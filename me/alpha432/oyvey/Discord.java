//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey;

import club.minnced.discord.rpc.*;

public class Discord
{
    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static Thread thread;
    
    public static void start() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        Discord.rpc.Discord_Initialize("869950644629409823", handlers, true, "");
        Discord.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        Discord.presence.details = "CCSHACK";
        Discord.presence.state = "by CCSStudios";
        Discord.presence.largeImageKey = "logi_c";
        Discord.presence.largeImageText = "CCSHACK";
        Discord.rpc.Discord_UpdatePresence(Discord.presence);
        (Discord.thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Discord.rpc.Discord_RunCallbacks();
                Discord.presence.largeImageKey = "picture";
                Discord.rpc.Discord_UpdatePresence(Discord.presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException ex) {}
            }
        }, "RPC-Callback-Handler")).start();
    }
    
    public static void stop() {
        if (Discord.thread != null && !Discord.thread.isInterrupted()) {
            Discord.thread.interrupt();
        }
        Discord.rpc.Discord_Shutdown();
    }
    
    static {
        rpc = DiscordRPC.INSTANCE;
        Discord.presence = new DiscordRichPresence();
    }
}
