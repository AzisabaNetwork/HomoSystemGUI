package jp.azisaba.main.homogui.listeners;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import jp.azisaba.main.homogui.tickets.DataManager;
import jp.azisaba.main.homogui.utils.Advancement;
import jp.azisaba.main.homos.database.TicketManager;
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
			long num = Long.parseLong(ChatColor.stripColor(signMsg.substring(0, signMsg.indexOf("チケット"))));

			ConfirmType type = getType(invTitle);

			boolean success = false;

			Advancement failAdv = new Advancement(Material.BARRIER, ChatColor.RED + "取引に失敗しました。");

			if (type == ConfirmType.BUY) {
				success = buyTicket(p, num);
			} else if (type == ConfirmType.SELL) {
				success = sellTicket(p, num);
			} else {
				p.closeInventory();
				p.sendMessage(ChatColor.RED + "実行に失敗しました。");
				return;
			}

			if (success) {
				Advancement adv;

				if (type == ConfirmType.BUY) {
					adv = new Advancement(Material.PAPER, ChatColor.GREEN + "購入に成功しました！");
				} else if (type == ConfirmType.SELL) {
					adv = new Advancement(Material.GOLD_INGOT, ChatColor.GREEN + "売却に成功しました！");
				} else {
					adv = new Advancement(Material.PAPER, ChatColor.GREEN + "取引に成功しました！");
				}

				adv.sendAndDelete(p);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

				failAdv.unload(20);
			} else {
				failAdv.sendAndDelete(p);
				p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
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

	private boolean buyTicket(Player p, long num) {
		Economy econ = HomoGUI.getEconomy();
		if (econ == null) {
			return false;
		}

		BigInteger bigIntNum = BigInteger.valueOf(num);

		BigDecimal money = new BigDecimal(DataManager.getTicketValue()).multiply(new BigDecimal(bigIntNum));

		EconomyResponse r = econ.withdrawPlayer(p, money.doubleValue());
		if (!r.transactionSuccess()) {
			Bukkit.broadcastMessage(r.errorMessage);
			return false;
		}

		DataManager.addTicket(p, bigIntNum);
		return true;
	}

	private boolean sellTicket(Player p, long num) {
		Economy econ = HomoGUI.getEconomy();
		if (econ == null) {
			return false;
		}

		BigInteger bigNum = BigInteger.valueOf(num);

		BigDecimal value = TicketManager.valueOfTicketsToConvertMoney(p.getUniqueId(), null, bigNum);

		EconomyResponse r = econ.depositPlayer(p, value.doubleValue());
		if (!r.transactionSuccess()) {
			Bukkit.broadcastMessage(r.errorMessage);
			return false;
		}

		DataManager.removeTicket(p, bigNum);
		return true;
	}
}
