package jp.azisaba.main.homogui.gui.pata;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.HomoGUI;
import jp.azisaba.main.homogui.gui.ClickableGUI;
import jp.azisaba.main.homogui.tickets.DataManager;
import jp.azisaba.main.homogui.utils.ItemHelper;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class TicketConfirmGUIForBuy extends ClickableGUI {

	@Override
	public void onClick(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		ItemMeta meta = item.getItemMeta();

		if (meta.getDisplayName().equals(ChatColor.RED + "キャンセル")) {
			p.closeInventory();
		} else if (meta.getDisplayName().equals(ChatColor.GREEN + "確定")) {

			String signMsg = inv.getItem(4).getItemMeta().getDisplayName();
			long num = Long.parseLong(ChatColor.stripColor(signMsg.substring(0, signMsg.indexOf("チケット"))));

			new Thread() {
				public void run() {

					Economy econ = HomoGUI.getEconomy();
					if (econ == null) {
						sendTitle(p, false);
						return;
					}

					BigInteger bigIntNum = BigInteger.valueOf(num);

					BigDecimal money = new BigDecimal(DataManager.getTicketValue()).multiply(new BigDecimal(bigIntNum));

					EconomyResponse r = econ.withdrawPlayer(p, money.doubleValue());
					if (!r.transactionSuccess()) {
						Bukkit.broadcastMessage(r.errorMessage);
						sendTitle(p, false);
						return;
					}

					boolean success = false;
					try {
						success = DataManager.addTicket(p, bigIntNum);
					} catch (Exception e) {
						e.printStackTrace();
						success = false;
					}

					if (!success) {
						r = econ.depositPlayer(p, money.doubleValue());
						if (!r.transactionSuccess()) {
							Bukkit.broadcastMessage(r.errorMessage);
						}
						sendTitle(p, false);
						return;
					} else {
						sendTitle(p, true);
					}
					return;
				}
			}.start();

			p.sendTitle("", ChatColor.RED + "" + ChatColor.BOLD + "処理を実行中...", 0, 20 * 3, 20);
			p.closeInventory();
		}
	}

	private void sendTitle(Player p, boolean success) {

		if (success) {
			String sub = ChatColor.DARK_GREEN + "購入に成功しました！";

			p.sendTitle("", sub, 0, 40, 10);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
		} else {
			p.sendTitle("", ChatColor.RED + "取引に失敗しました。", 0, 40, 10);
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
		}
	}

	@Override
	public boolean cancelEvent(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		return true;
	}

	@Override
	public Inventory getInventory(Player p, Object... objects) {

		long tickets = 0;
		if (objects.length <= 0) {
			return null;
		} else {
			if (objects[0] instanceof Long) {
				tickets = (Long) objects[0];
			}
		}

		if (tickets <= 0) {
			throw new IllegalArgumentException("Ticket amount must be positive number.");
		}

		Inventory inv = Bukkit.createInventory(null, getInvSize(), getInvTitle());

		ItemStack ok = getConfirmItem();
		ItemStack no = getCancelItem();

		ItemStack sign = ItemHelper.createItem(Material.SIGN, ChatColor.YELLOW + "" + tickets + "チケットを購入");

		inv.setItem(0, no);
		inv.setItem(1, no);
		inv.setItem(2, no);
		inv.setItem(3, no);

		inv.setItem(4, sign);

		inv.setItem(5, ok);
		inv.setItem(6, ok);
		inv.setItem(7, ok);
		inv.setItem(8, ok);
		return inv;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isSameInventory(Inventory inv) {

		boolean sameTitle = getInvTitle().equals(inv.getTitle());
		boolean sameSize = getInvSize() == inv.getSize();

		return sameTitle && sameSize;
	}

	private String getInvTitle() {
		return ChatColor.RED + "Confirm - " + ChatColor.RED + "Buy";
	}

	private int getInvSize() {
		return 9 * 1;
	}

	private static ItemStack getConfirmItem() {
		ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "確定");
		item.setItemMeta(meta);
		return item;
	}

	private static ItemStack getCancelItem() {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "キャンセル");
		item.setItemMeta(meta);
		return item;
	}
}
