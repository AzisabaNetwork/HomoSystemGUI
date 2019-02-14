package jp.azisaba.main.homogui.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.HomoGUI;
import jp.azisaba.main.homogui.JSONMessage;
import jp.azisaba.main.homogui.gui.MainGUI;
import jp.azisaba.main.homogui.gui.ServerSelectGUI;
import jp.azisaba.main.homogui.gui.TicketGUI;
import net.md_5.bungee.api.ChatColor;

public class MainGUIListener implements Listener {

	@SuppressWarnings("unused")
	private HomoGUI plugin;

	public MainGUIListener(HomoGUI plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onClickGUI(InventoryClickEvent e) {
		if (e.getInventory() == null || !(e.getWhoClicked() instanceof Player)) {
			return;
		}

		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();

		if (inv == null) {
			return;
		}

		@SuppressWarnings("deprecation")
		String invTitle = inv.getTitle();
		if (!invTitle.equals(MainGUI.getInvTitle())) {
			return;
		}

		e.setCancelled(true);

		if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		ItemStack item = e.getCurrentItem();
		ItemMeta meta = item.getItemMeta();

		if (meta.getDisplayName().equals(MainGUI.getCAItem().getItemMeta().getDisplayName())) {
			p.closeInventory();
			p.performCommand("ca");
		} else if (meta.getDisplayName().equals(MainGUI.getTicketItem().getItemMeta().getDisplayName())) {
			Inventory ticketInv = TicketGUI.getInv();
			p.openInventory(ticketInv);
		} else if (meta.getDisplayName().equals(MainGUI.getVoteItem().getItemMeta().getDisplayName())) {
			sendVoteURL(p);
			p.closeInventory();
		} else if (meta.getDisplayName().equals(MainGUI.getServerSelector().getItemMeta().getDisplayName())) {
			p.openInventory(ServerSelectGUI.getInv());
		}
	}

	private String voteUrl = "https://minecraft.jp/servers/azisaba.net/vote";

	public void sendVoteURL(Player p) {
		JSONMessage msg = JSONMessage.create().bar(40).newline();
		msg.then(ChatColor.GREEN + "ありがとうございます！ 投票リンクは");
		msg.then(ChatColor.RED + "こちら").openURL(voteUrl);
		msg.then(ChatColor.GREEN + "です！").newline();
		msg.then(ChatColor.YELLOW + voteUrl).openURL(voteUrl);
		msg.bar(40);
		msg.send(p);
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
	}
}
