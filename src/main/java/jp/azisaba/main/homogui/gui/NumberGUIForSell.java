package jp.azisaba.main.homogui.gui;

import java.math.BigInteger;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.tickets.DataManager;
import jp.azisaba.main.homogui.utils.ItemHelper;
import jp.azisaba.main.homos.database.PlayerDataManager;
import jp.azisaba.main.homos.database.TicketManager;

public class NumberGUIForSell extends ClickableGUI {

	@Override
	public boolean cancelEvent(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isSameInventory(Inventory inv) {
		boolean sameTitle = getInvTitle().equals(inv.getTitle());
		boolean sameSize = getInvSize() == inv.getSize();

		return sameTitle && sameSize;
	}

	private ItemStack backSpace, enter, cancel, zero, one, two, three, four, five, six, seven, eight, nine, max;

	@Override
	public Inventory getInventory(Player p, Object... objects) {
		Inventory inv = Bukkit.createInventory(null, getInvSize(), getInvTitle());

		inv.setItem(0, getResultItem(p));
		inv.setItem(1, backSpace);
		inv.setItem(4, zero);
		inv.setItem(5, one);
		inv.setItem(6, two);
		inv.setItem(7, three);
		inv.setItem(8, four);
		inv.setItem(9, enter);
		inv.setItem(10, cancel);
		inv.setItem(13, five);
		inv.setItem(14, six);
		inv.setItem(15, seven);
		inv.setItem(16, eight);
		inv.setItem(17, nine);
		inv.setItem(26, max);

		return inv;
	}

	@Override
	public void onRegister() {
		backSpace = ItemHelper.createSkull(ItemHelper.ARROW_LEFT, ChatColor.GOLD + "バックスペース", ChatColor.GRAY + "");
		enter = ItemHelper.createItem(Material.PAPER, ChatColor.GOLD + "エンター", ChatColor.GRAY + "");
		cancel = ItemHelper.createItem(Material.BONE_MEAL, ChatColor.GOLD + "キャンセル", ChatColor.GRAY + "");
		zero = ItemHelper.createSkull(ItemHelper.ZERO, ChatColor.GOLD + "0", ChatColor.GRAY + "");
		one = ItemHelper.createSkull(ItemHelper.ONE, ChatColor.GOLD + "1", ChatColor.GRAY + "");
		two = ItemHelper.createSkull(ItemHelper.TWO, ChatColor.GOLD + "2", ChatColor.GRAY + "");
		three = ItemHelper.createSkull(ItemHelper.THREE, ChatColor.GOLD + "3", ChatColor.GRAY + "");
		four = ItemHelper.createSkull(ItemHelper.FOUR, ChatColor.GOLD + "4", ChatColor.GRAY + "");
		five = ItemHelper.createSkull(ItemHelper.FIVE, ChatColor.GOLD + "5", ChatColor.GRAY + "");
		six = ItemHelper.createSkull(ItemHelper.SIX, ChatColor.GOLD + "6", ChatColor.GRAY + "");
		seven = ItemHelper.createSkull(ItemHelper.SEVEN, ChatColor.GOLD + "7", ChatColor.GRAY + "");
		eight = ItemHelper.createSkull(ItemHelper.EIGHT, ChatColor.GOLD + "8", ChatColor.GRAY + "");
		nine = ItemHelper.createSkull(ItemHelper.NINE, ChatColor.GOLD + "9", ChatColor.GRAY + "");
		max = ItemHelper.createItem(Material.GOLD_INGOT, ChatColor.GOLD + "最大値を指定する");
	}

	private ItemStack getResultItem(Player p) {
		String desc = ChatColor.YELLOW + "チケット1枚あたり" + ChatColor.GREEN + ": " + ChatColor.RED;
		BigInteger value = BigInteger.ZERO;

		BigInteger bigNum = BigInteger.valueOf(1);
		value = TicketManager.valueOfTicketsToConvertMoney(p.getUniqueId(), null, bigNum);

		desc += value.toString();

		String line2 = ChatColor.GRAY + "10%手数料として引かれています";
		return ItemHelper.createItem(Material.WRITABLE_BOOK, ChatColor.GOLD + "0", desc, line2);
	}

	private int getInvSize() {
		return 9 * 3;
	}

	private String getInvTitle() {
		return ChatColor.RED + "Enter Number for Sell";
	}

	private HashMap<Player, Long> lastClicked = new HashMap<>();

	@Override
	public void onClick(Player p, Inventory inv, ItemStack item, InventoryAction action) {

		if (lastClicked.containsKey(p) && lastClicked.get(p) + 50 >= System.currentTimeMillis()) {
			return;
		}

		lastClicked.put(p, System.currentTimeMillis());

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
		} else if (strip.equals("最大値を指定する")) {

			long num;

			BigInteger tickets = PlayerDataManager.getPlayerData(p).getTickets();

			if (tickets.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
				num = Long.MAX_VALUE;
			} else {
				num = tickets.longValue();
			}

			setBookString(inv, ChatColor.GOLD + "" + num);

			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1.2f);
		} else if (strip.equals("エンター")) {

			ItemStack book = inv.getItem(0);
			String current = ChatColor.stripColor(book.getItemMeta().getDisplayName());

			boolean canInput = false;
			try {
				long value = Long.parseLong(current);
				canInput = true;

				if (value <= 0) {
					canInput = false;
				}
			} catch (Exception ex) {
				canInput = false;
			}

			if (!canInput) {
				setBookString(inv, ChatColor.RED + "数字を入力してから押してください。");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				return;
			}

			long tickets = Long.parseLong(current);

			if (DataManager.getPlayerData(p).getTickets().compareTo(BigInteger.valueOf(tickets)) < 0) {
				setBookString(inv, ChatColor.RED + "十分なチケットがありません！");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				return;
			}

			clickEnter(inv, tickets, p);
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

	private String getNumberTooLarge() {
		return ChatColor.RED + "数が大きすぎます！";
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

	private void clickEnter(Inventory inv, long tickets, Player p) {
		Inventory confirmInv = ClickableGUIController.getGUI(TicketConfirmGUIForSell.class).getInventory(p, tickets);
		p.openInventory(confirmInv);
	}

	private void setBookString(Inventory inv, String str) {
		ItemStack item = inv.getItem(0);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(str);
		item.setItemMeta(meta);
		inv.setItem(0, item);
	}
}
