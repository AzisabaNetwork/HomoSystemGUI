package jp.azisaba.main.homogui.gui.eco;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.gui.ClickableGUI;
import jp.azisaba.main.homogui.gui.ClickableGUIController;
import jp.azisaba.main.homogui.utils.ItemHelper;

public class TicketGUI extends ClickableGUI {

	@Override
	public void onClick(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		ItemMeta meta = item.getItemMeta();

		Inventory numberInv;

		if (meta.getDisplayName().equals(ChatColor.RED + "購入")) {
			numberInv = ClickableGUIController.getGUI(NumberGUIForBuy.class).getInventory(p);
		} else if (meta.getDisplayName().equals(ChatColor.GREEN + "売却")) {
			numberInv = ClickableGUIController.getGUI(NumberGUIForSell.class).getInventory(p);
			if (numberInv == null) {
				return;
			}
		} else {
			return;
		}

		p.openInventory(numberInv);
	}

	@Override
	public boolean cancelEvent(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		return true;
	}

	private Inventory inv = null;

	@Override
	public Inventory getInventory(Player p, Object... objects) {
		if (inv == null) {
			inv = Bukkit.createInventory(null, getInvSize(), getInvTitle());

			ItemStack sell = ItemHelper.createItem(Material.PAPER, ChatColor.GREEN + "売却", clickToContinue());
			ItemStack buy = ItemHelper.createItem(Material.PAPER, ChatColor.RED + "購入", clickToContinue());

			inv.setItem(12, buy);
			inv.setItem(14, sell);
		}

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
		return ChatColor.RED + "Ticket";
	}

	private int getInvSize() {
		return 9 * 3;
	}

	private String clickToContinue() {
		return ChatColor.YELLOW + "クリックで継続...";
	}
}
