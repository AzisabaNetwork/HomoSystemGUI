package jp.azisaba.main.homogui.listeners;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import org.bukkit.ChatColor;
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
import jp.azisaba.main.homogui.gui.NumberGUI;
import jp.azisaba.main.homogui.gui.TicketConfirmGUI;
import jp.azisaba.main.homogui.gui.TicketConfirmGUI.ConfirmType;
import jp.azisaba.main.homogui.tickets.DataManager;

public class NumberGUIListener implements Listener {

	private HashMap<Player, Long> clicked = new HashMap<>();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory() == null || !(e.getWhoClicked() instanceof Player)) {
			return;
		}

		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();

		@SuppressWarnings("deprecation")
		String invTitle = inv.getTitle();
		if (!invTitle.startsWith(NumberGUI.getInvTitle())) {
			return;
		}

		e.setCancelled(true);

		if (clicked.containsKey(p) && clicked.get(p) + 50 >= System.currentTimeMillis()) {
			return;
		}

		clicked.put(p, System.currentTimeMillis());

		if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		ItemStack item = e.getCurrentItem();
		ItemMeta meta = item.getItemMeta();

		if (!meta.hasDisplayName()) {
			return;
		}

		String name = meta.getDisplayName();
		String strip = ChatColor.stripColor(name);

		boolean isNumber;
		try {
			Integer.parseInt(strip);
			isNumber = true;
		} catch (Exception ex) {
			isNumber = false;
		}

		if (strip.length() == 1 && isNumber && item.getType() == Material.PLAYER_HEAD) {
			int num = Integer.parseInt(strip);
			clickedNumber(inv, p, num);
		} else if (strip.equals("バックスペース")) {
			clickedBack(inv, p);
		} else if (strip.equals("キャンセル")) {
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
		} else if (strip.equals("エンター")) {

			ItemStack book = inv.getItem(0);
			String current = ChatColor.stripColor(book.getItemMeta().getDisplayName());

			boolean canInput = false;
			try {
				Integer.parseInt(current);
				canInput = true;
			} catch (Exception ex) {
				canInput = false;
			}

			if (canInput) {
				long num = Long.parseLong(current);
				if (num <= 0) {
					canInput = false;
				}
			}

			if (!canInput) {
				setBookString(inv, ChatColor.RED + "数字を入力してから押してください。");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				return;
			}

			int tickets = Integer.parseInt(current);

			String str = invTitle;
			str = str.substring(str.indexOf("-") + 1, str.length()).trim();
			ConfirmType type = ConfirmType.valueOf(str);

			if (type == ConfirmType.BUY) {

				BigDecimal value = new BigDecimal(DataManager.getTicketValue().multiply(BigInteger.valueOf(tickets)));
				if (!HomoGUI.getEconomy().has(p, value.doubleValue())) {
					setBookString(inv, ChatColor.RED + "十分なお金がありません！");
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					return;
				}
			} else if (type == ConfirmType.SELL) {
				if (DataManager.getPlayerData(p).getTickets().compareTo(BigInteger.valueOf(tickets)) < 0) {
					setBookString(inv, ChatColor.RED + "十分なチケットがありません！");
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					return;
				}
			} else {
				return;
			}

			clickEnter(inv, tickets, p, type);
		}
	}

	private void clickedNumber(Inventory inv, Player p, int num) {
		ItemStack book = inv.getItem(0);
		String newMsg = "";

		String currentItemName = book.getItemMeta().getDisplayName();

		if (currentItemName.equals(getNumberTooLarge())) {
			currentItemName = "0";
		}

		try {
			newMsg = ChatColor.GOLD + ""
					+ Integer.parseInt(ChatColor.stripColor(currentItemName) + num);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
		} catch (NumberFormatException ex) {
			newMsg = getNumberTooLarge();
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		}

		ItemMeta bookMeta = book.getItemMeta();
		bookMeta.setDisplayName(newMsg);
		book.setItemMeta(bookMeta);
		inv.setItem(0, book);
	}

	private void clickedBack(Inventory inv, Player p) {
		ItemStack book = inv.getItem(0);
		String current = ChatColor.stripColor(book.getItemMeta().getDisplayName());

		boolean isNumber = false;
		try {
			Integer.parseInt(current);
			isNumber = true;
		} catch (Exception e) {
			isNumber = false;
		}

		if (!isNumber || current.equals(ChatColor.stripColor(getNumberTooLarge()))
				|| (current.length() <= 1 && !current.equals("0"))) {
			current = "0";
		} else if (!current.equals("0")) {
			current = current.substring(0, current.length() - 1);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
		}

		ItemMeta bookMeta = book.getItemMeta();
		bookMeta.setDisplayName(ChatColor.GOLD + current);
		book.setItemMeta(bookMeta);

		inv.setItem(0, book);
	}

	private void clickEnter(Inventory inv, int num, Player p, ConfirmType type) {
		Inventory confirmInv = TicketConfirmGUI.getInv(type, num);
		p.openInventory(confirmInv);
	}

	private String getNumberTooLarge() {
		return ChatColor.RED + "数が大きすぎます！";
	}

	private void setBookString(Inventory inv, String str) {
		ItemStack item = inv.getItem(0);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(str);
		item.setItemMeta(meta);
		inv.setItem(0, item);
	}
}
