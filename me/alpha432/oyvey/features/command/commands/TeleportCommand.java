//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.command.commands;

import me.alpha432.oyvey.features.command.*;
import com.mojang.realmsclient.gui.*;

public class TeleportCommand extends Command
{
    public TeleportCommand() {
        super("tp", new String[] { "<x> <y> <z>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length != 4) {
            Command.sendMessage(ChatFormatting.DARK_RED + " Invalid Position");
            return;
        }
        final int x = Integer.parseInt(commands[0]);
        final int y = Integer.parseInt(commands[1]);
        final int z = Integer.parseInt(commands[2]);
        Command.sendMessage("Teleported to " + ChatFormatting.GRAY + x + ", " + y + ", " + z);
        TeleportCommand.mc.player.setPosition((double)x, (double)y, (double)z);
    }
}
