package jp.azisaba.main.homogui;

import org.bukkit.inventory.ItemStack;

public class ServerItem {

	private ItemStack item;
	private String serverName;
	private boolean closeInv;

	protected ServerItem(ItemStack item, String serverName, boolean closeInv) {
		this.item = item;
		this.serverName = serverName;
		this.closeInv = closeInv;
	}

	public ItemStack getItem() {
		return item;
	}

	public String getServerName() {
		return serverName;
	}

	public boolean isCloseInv() {
		return closeInv;
	}
}
