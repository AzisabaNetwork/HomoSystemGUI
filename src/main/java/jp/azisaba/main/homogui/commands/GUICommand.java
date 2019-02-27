package jp.azisaba.main.homogui.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jp.azisaba.main.homogui.gui.ClickableGUIController;
import net.md_5.bungee.api.ChatColor;

public class GUICommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーからのみ実行できます。");
			return true;
		}

		Player p = (Player) sender;
		p.openInventory(ClickableGUIController.getMainInv(p));
		return true;
	}
}
