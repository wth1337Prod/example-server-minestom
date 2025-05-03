package org.wth;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.utils.entity.EntityFinder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.kyori.adventure.text.Component;

public class TpCommand extends Command {

    private final ArgumentEntity targetArg =
            ArgumentType.Entity("target")
                    .onlyPlayers(true)
                    .singleEntity(true);

    public TpCommand() {
        super("tp", "teleport");

        addSyntax(this::teleportToPlayer, targetArg);

        var xArg = ArgumentType.Double("x");
        var yArg = ArgumentType.Double("y");
        var zArg = ArgumentType.Double("z");
        addSyntax(this::teleportToCoordinates, xArg, yArg, zArg);
    }

    /** Регистрация команды в менеджере */
    public static void register() {
        MinecraftServer.getCommandManager().register(new TpCommand());
    }

    /** /tp <игрок> */
    private void teleportToPlayer(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("§cТолько игроки могут телепортироваться"));
            return;
        }
        EntityFinder finder = context.get(targetArg);
        Entity found = finder.findFirstEntity(sender);
        if (!(found instanceof Player targetPlayer)) {
            sender.sendMessage(Component.text("§cИгрок не найден"));
            return;
        }
        player.teleport(targetPlayer.getPosition());
        player.sendMessage(Component.text("§aТелепортировано к игроку " + targetPlayer.getUsername()));
    }

    /** /tp <x> <y> <z> */
    private void teleportToCoordinates(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("§cТолько игроки могут телепортироваться"));
            return;
        }
        double x = context.get("x");
        double y = context.get("y");
        double z = context.get("z");
        Pos pos = new Pos(x, y, z);
        player.teleport(pos);
        player.sendMessage(Component.text(
                String.format("§aТелепортировано на координаты (%.1f, %.1f, %.1f)", x, y, z)
        ));
    }
}
