//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.command;

import me.alpha432.oyvey.features.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.util.text.*;
import java.util.regex.*;

public abstract class Command extends Feature
{
    protected String name;
    protected String[] commands;
    
    public Command(final String name) {
        super(name);
        this.name = name;
        this.commands = new String[] { "" };
    }
    
    public Command(final String name, final String[] commands) {
        super(name);
        this.name = name;
        this.commands = commands;
    }
    
    public static void sendMessage(final String message) {
        sendSilentMessage(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.GRAY + message);
    }
    
    public static void sendTempMessage(final String message) {
        sendTempSilentMessage(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.GRAY + message, -1337);
    }
    
    public static void sendTempMessageID(final String message, final int id) {
        sendTempSilentMessage(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.GRAY + message, id);
    }
    
    public static void sendTempSilentMessage(final String message, final int id) {
        if (nullCheck()) {
            return;
        }
        Util.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)new ChatMessage(message), id);
    }
    
    public static void sendSilentMessage(final String message) {
        if (nullCheck()) {
            return;
        }
        Command.mc.player.sendMessage((ITextComponent)new ChatMessage(message));
    }
    
    public static String getCommandPrefix() {
        return OyVey.commandManager.getPrefix();
    }
    
    public abstract void execute(final String[] p0);
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public String[] getCommands() {
        return this.commands;
    }
    
    public static class ChatMessage extends TextComponentBase
    {
        private final String text;
        
        public ChatMessage(final String text) {
            final Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            final Matcher matcher = pattern.matcher(text);
            final StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                final String replacement = matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, replacement);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }
        
        public String getUnformattedComponentText() {
            return this.text;
        }
        
        public ITextComponent createCopy() {
            return null;
        }
        
        public ITextComponent shallowCopy() {
            return (ITextComponent)new ChatMessage(this.text);
        }
    }
}
