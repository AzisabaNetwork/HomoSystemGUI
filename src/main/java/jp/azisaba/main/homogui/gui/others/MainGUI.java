package jp.azisaba.main.homogui.gui.others;

import java.math.BigInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.HomoGUI;
import jp.azisaba.main.homogui.gui.ClickableGUI;
import jp.azisaba.main.homogui.gui.ClickableGUIController;
import jp.azisaba.main.homogui.gui.ServerSelectGUI;
import jp.azisaba.main.homogui.tickets.DataManager;
import jp.azisaba.main.homogui.utils.ItemHelper;
import me.rayzr522.jsonmessage.JSONMessage;
import net.md_5.bungee.api.ChatColor;

public class MainGUI extends ClickableGUI {

	@Override
	public void onClick(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		ItemMeta meta = item.getItemMeta();

		if (meta.getDisplayName().equals(voteDisplay)) {
			sendVoteURL(p);
			p.closeInventory();
		} else if (meta.getDisplayName().equals(serverDisplay)) {
			p.openInventory(ClickableGUIController.getGUI(ServerSelectGUI.class).getInventory(p));
		}
	}

	@Override
	public boolean cancelEvent(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isSameInventory(Inventory inv) {

		boolean sameName = inv.getTitle().equals(getInvTitle());
		boolean sameSize = inv.getSize() == getInvSize();

		return sameName && sameSize;
	}

	private final String voteDisplay = ChatColor.AQUA + "アジ鯖に投票！";
	private final String serverDisplay = ChatColor.GREEN + "サーバー選択";

	@Override
	public Inventory getInventory(Player p, Object... objects) {
		Inventory inv = Bukkit.createInventory(null, getInvSize(), getInvTitle());

		ItemStack head = getPlayerSkull(p);
		ItemStack vote = ItemHelper.createItem(Material.DIAMOND, voteDisplay, ChatColor.RED + "クリックで投票リンクを表示！");
		ItemStack server = ItemHelper.createItem(Material.NETHER_STAR, serverDisplay,
				ChatColor.RED + "ほかのサーバーに移動することができます");

		inv.setItem(2, vote);
		inv.setItem(4, head);
		inv.setItem(6, server);

		return inv;
	}

	public static ItemStack getPlayerSkull(Player p) {

		BigInteger tickets = DataManager.getPlayerData(p).getTickets();
		String displayName = ChatColor.YELLOW + p.getName() + ChatColor.RED + "の情報";
		String ticketStr = ChatColor.RED + "チケット" + ChatColor.GREEN + ": " + ChatColor.YELLOW + tickets.toString();
		ItemStack skull = ItemHelper.createSkull(p, displayName, ticketStr);
		return skull;
	}

	public void sendVoteURL(Player p) {
		JSONMessage msg = JSONMessage.create().bar(40).newline();
		msg.then(ChatColor.GREEN + "ありがとうございます！ 投票リンクは");
		msg.then(ChatColor.RED + "こちら").openURL(HomoGUI.voteUrl);
		msg.then(ChatColor.GREEN + "です！").newline();
		msg.then(ChatColor.YELLOW + HomoGUI.voteUrl).openURL(HomoGUI.voteUrl);
		msg.bar(40);
		msg.send(p);
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
	}

	private String getInvTitle() {
		return ChatColor.YELLOW + "Main Menu";
	}

	private int getInvSize() {
		return 9 * 1;
	}
}
