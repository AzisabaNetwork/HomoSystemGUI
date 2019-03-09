package jp.azisaba.main.homogui.gui.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import jp.azisaba.main.homogui.HomoGUI;
import jp.azisaba.main.homogui.ServerItem;
import jp.azisaba.main.homogui.ServerSelectConfig;
import jp.azisaba.main.homogui.gui.ClickableGUI;

public class ServerSelectGUI extends ClickableGUI {

	private Inventory inv = null;

	@Override
	public void onClick(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		ServerItem serverItem = null;
		for (ServerItem search : ServerSelectConfig.getItems().values()) {
			if (!search.getItem().equals(item)) {
				continue;
			}
			serverItem = search;
			break;
		}

		if (serverItem == null) {
			return;
		}

		String server = serverItem.getServerName();
		boolean closeInv = serverItem.isCloseInv();

		sendPlayer(p, server);

		if (closeInv) {
			p.closeInventory();
		}
		return;
	}

	@Override
	public boolean cancelEvent(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		return true;
	}

	@Override
	public Inventory getInventory(Player p, Object... objects) {

		if (inv == null) {
			inv = Bukkit.createInventory(null, getInvSize(), getInvTitle());
			HashMap<Integer, ServerItem> items = ServerSelectConfig.getItems();
			for (int place : items.keySet()) {
				inv.setItem(place, items.get(place).getItem());
			}
		}

		return this.inv;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isSameInventory(Inventory inv) {

		boolean sameName = inv.getTitle().equals(getInvTitle());
		boolean sameSize = inv.getSize() == getInvSize();

		return sameName && sameSize;
	}

	private static String getInvTitle() {
		return ChatColor.RED + "Server Selector";
	}

	private int getInvSize() {
		return ServerSelectConfig.getInventorySize();
	}

	private void sendPlayer(Player player, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(HomoGUI.getInstance(), "BungeeCord", out.toByteArray());
	}
}
