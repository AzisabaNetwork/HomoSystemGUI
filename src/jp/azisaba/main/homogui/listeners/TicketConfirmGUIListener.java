package jp.azisaba.main.homogui.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.HomoGUI;
import jp.azisaba.main.homogui.gui.TicketConfirmGUI;
import jp.azisaba.main.homogui.gui.TicketConfirmGUI.ConfirmType;
import jp.azisaba.main.homogui.tickets.TicketManager;
import jp.azisaba.main.homos.classes.PlayerData;
import jp.azisaba.main.homos.database.SQLDataManager;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class TicketConfirmGUIListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory() == null || !(e.getWhoClicked() instanceof Player) || e.getCurrentItem() == null
				|| e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();

		@SuppressWarnings("deprecation")
		String invTitle = inv.getTitle();
		if (!invTitle.startsWith(TicketConfirmGUI.getInvTitle(ConfirmType.BUY).substring(0,
				TicketConfirmGUI.getInvTitle(ConfirmType.BUY).lastIndexOf("-") + 1))) {
			return;
		}

		e.setCancelled(true);

		if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		ItemStack item = e.getCurrentItem();
		ItemMeta meta = item.getItemMeta();

		if (meta.getDisplayName().equals(ChatColor.RED + "キャンセル")) {
			p.closeInventory();
		} else if (meta.getDisplayName().equals(ChatColor.GREEN + "確定")) {

			String signMsg = inv.getItem(4).getItemMeta().getDisplayName();
			int num = Integer.parseInt(ChatColor.stripColor(signMsg.substring(0, signMsg.indexOf("チケット"))));

			ConfirmType type = getType(invTitle);

			if (type == ConfirmType.BUY) {
				boolean success = buyTicket(p, num);

				if (!success) {
					p.sendMessage(ChatColor.RED + "失敗しました。");
				} else {
					p.sendMessage(ChatColor.GREEN + "取引に成功しました。");
				}
			} else if (type == ConfirmType.SELL) {
				boolean success = sellTicket(p, num);

				if (!success) {
					p.sendMessage(ChatColor.RED + "失敗しました。");
				} else {
					p.sendMessage(ChatColor.GREEN + "取引に成功しました。");
				}
			} else {
				p.closeInventory();
				p.sendMessage(ChatColor.RED + "実行に失敗しました。");
				return;
			}

			p.closeInventory();
		}
	}

	private static ConfirmType getType(String name) {
		if (name.endsWith("Buy")) {
			return ConfirmType.BUY;
		} else if (name.endsWith("Sell")) {
			return ConfirmType.SELL;
		}

		return null;
	}

	private boolean buyTicket(Player p, int num) {
		Economy econ = HomoGUI.getEconomy();
		if (econ == null) {
			return false;
		}

		EconomyResponse r = econ.withdrawPlayer(p, TicketManager.getTicketValue() * num);
		if (!r.transactionSuccess()) {
			return false;
		}

		TicketManager.addTicket(p, num);
		return true;
	}

	private boolean sellTicket(Player p, int num) {
		Economy econ = HomoGUI.getEconomy();
		if (econ == null) {
			return false;
		}

		double value = SQLDataManager.valueOfTicketsToConvertMoney(p.getUniqueId(), null, num);

		EconomyResponse r = econ.depositPlayer(p, value);
		if (!r.transactionSuccess()) {
			return false;
		}

		TicketManager.removeTicket(p, num);
		return true;
	}
}
