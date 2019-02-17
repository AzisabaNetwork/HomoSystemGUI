package jp.azisaba.main.homogui.gui;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.gui.TicketConfirmGUI.ConfirmType;
import jp.azisaba.main.homogui.utils.ItemHelper;
import jp.azisaba.main.homos.Homos;
import jp.azisaba.main.homos.database.TicketManager;

public class NumberGUI {

	public static Inventory getInv(Player p, ConfirmType type) {
		Inventory inv = Bukkit.createInventory(null, 9 * 3, getInvTitle() + " - " + type.toString());

		String desc = ChatColor.YELLOW + "チケット1枚あたり" + ChatColor.GREEN + ": " + ChatColor.RED;
		BigDecimal value = BigDecimal.ZERO;
		if (type == ConfirmType.BUY) {
			value = new BigDecimal(Homos.getTicketValueManager().getCurrentTicketValue());
		} else if (type == ConfirmType.SELL) {
			BigInteger bigNum = BigInteger.valueOf(1);
			value = TicketManager.valueOfTicketsToConvertMoney(p.getUniqueId(), null, bigNum);
		}

		desc += value.toString();

		ItemStack result = ItemHelper.createItem(Material.WRITABLE_BOOK, ChatColor.GOLD + "0", desc,
				ChatColor.GRAY + "10%手数料として引かれています");

		ItemStack backSpace = ItemHelper.createSkull(ItemHelper.ARROW_LEFT, ChatColor.GOLD + "バックスペース",
				ChatColor.GRAY + "");

		ItemStack enter = ItemHelper.createItem(Material.PAPER, ChatColor.GOLD + "エンター", ChatColor.GRAY + "");
		ItemStack cancel = ItemHelper.createItem(Material.BONE_MEAL, ChatColor.GOLD + "キャンセル", ChatColor.GRAY + "");
		ItemStack zero = ItemHelper.createSkull(ItemHelper.ZERO, ChatColor.GOLD + "0", ChatColor.GRAY + "");
		ItemStack one = ItemHelper.createSkull(ItemHelper.ONE, ChatColor.GOLD + "1", ChatColor.GRAY + "");
		ItemStack two = ItemHelper.createSkull(ItemHelper.TWO, ChatColor.GOLD + "2", ChatColor.GRAY + "");
		ItemStack three = ItemHelper.createSkull(ItemHelper.THREE, ChatColor.GOLD + "3", ChatColor.GRAY + "");
		ItemStack four = ItemHelper.createSkull(ItemHelper.FOUR, ChatColor.GOLD + "4", ChatColor.GRAY + "");
		ItemStack five = ItemHelper.createSkull(ItemHelper.FIVE, ChatColor.GOLD + "5", ChatColor.GRAY + "");
		ItemStack six = ItemHelper.createSkull(ItemHelper.SIX, ChatColor.GOLD + "6", ChatColor.GRAY + "");
		ItemStack seven = ItemHelper.createSkull(ItemHelper.SEVEN, ChatColor.GOLD + "7", ChatColor.GRAY + "");
		ItemStack eight = ItemHelper.createSkull(ItemHelper.EIGHT, ChatColor.GOLD + "8", ChatColor.GRAY + "");
		ItemStack nine = ItemHelper.createSkull(ItemHelper.NINE, ChatColor.GOLD + "9", ChatColor.GRAY + "");
		ItemStack max = new ItemStack(Material.GOLD_INGOT);
		ItemMeta maxMeta = max.getItemMeta();
		maxMeta.setDisplayName(ChatColor.GOLD + "最大値を指定する");
		max.setItemMeta(maxMeta);

		inv.setItem(0, result);
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

	public static String getInvTitle() {
		return ChatColor.RED + "Enter Number";
	}

}
