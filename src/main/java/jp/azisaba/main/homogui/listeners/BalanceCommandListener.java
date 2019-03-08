package jp.azisaba.main.homogui.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import jp.azisaba.main.homogui.HomoGUI;
import net.md_5.bungee.api.ChatColor;

public class BalanceCommandListener implements Listener {

	private HomoGUI plugin;
	private List<String> labels = Arrays.asList("money", "balance", "bal", "baltop", "balancetop");

	public BalanceCommandListener(HomoGUI plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {

		Player p = e.getPlayer();

		String cmd = e.getMessage();
		String label;
		if (cmd.contains(" ")) {
			label = cmd.substring(1, cmd.indexOf(" "));
		} else {
			label = cmd.substring(1);
		}

		if (labels.contains(label.toLowerCase())) {
			new BukkitRunnable() {
				public void run() {
					p.sendMessage(
							ChatColor.GRAY + "所持金や総資金、ランキングの確認は" + ChatColor.RED + "/g" + ChatColor.GRAY + "を推奨しています");
				}
			}.runTaskLater(plugin, 1);
		}
	}
}
