package jp.azisaba.main.homogui.gui;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;

import jp.azisaba.main.homogui.HomoGUI;
import jp.azisaba.main.homogui.tickets.DataManager;
import jp.azisaba.main.homogui.utils.ItemHelper;
import jp.azisaba.main.homos.JSONMessage;
import jp.azisaba.main.homos.classes.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class MainGUI extends ClickableGUI {

	@Override
	public void onClick(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		ItemMeta meta = item.getItemMeta();

		if (meta.getDisplayName().equals(caDisplay)) {
			p.closeInventory();
			p.performCommand("ca");
		} else if (meta.getDisplayName().equals(ticketDisplay)) {
			Inventory ticketInv = ClickableGUIController.getGUI(TicketGUI.class).getInventory(p);
			p.openInventory(ticketInv);
		} else if (meta.getDisplayName().equals(voteDisplay)) {
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

	private final String ticketDisplay = ChatColor.YELLOW + "チケット売買";
	private final String caDisplay = ChatColor.RED + "販売/オークション";
	private final String voteDisplay = ChatColor.AQUA + "アジ鯖に投票！";
	private final String serverDisplay = ChatColor.GREEN + "サーバー選択";

	@Override
	public Inventory getInventory(Player p, Object... objects) {
		Inventory inv = Bukkit.createInventory(null, getInvSize(), getInvTitle());

		ItemStack ticket = ItemHelper.createItem(Material.PAPER, ticketDisplay, ChatColor.GREEN + "チケットの売買が行えます");
		ItemStack ca = ItemHelper.createItem(Material.ANVIL, caDisplay);
		ItemStack head = getPlayerSkull(p);
		ItemStack vote = ItemHelper.createItem(Material.DIAMOND, voteDisplay, ChatColor.RED + "クリックで投票リンクを表示！");
		ItemStack server = ItemHelper.createItem(Material.NETHER_STAR, serverDisplay,
				ChatColor.RED + "ほかのサーバーに移動することができます");
		ItemStack moneyRank = ItemHelper.createItem(Material.GOLD_INGOT, ChatColor.GREEN + "総資金ランキング",
				ChatColor.RED + "取得中...");
		ItemMeta moneyRankMeta = moneyRank.getItemMeta();

		new Thread(new Runnable() {

			private Player player = p;

			@Override
			public void run() {

				Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
				UserMap map = ess.getUserMap();

				BigDecimal userMoney = map.getUser(p.getUniqueId()).getMoney();

				List<Entry<String, BigDecimal>> moneyMap = sortedMoneyList();

				List<String> lore = new ArrayList<>();

				boolean containOpener = false;
				BigDecimal before = BigDecimal.ZERO;
				int rank = 0;
				int count = 1;
				for (Entry<String, BigDecimal> entry : moneyMap) {

					entry.setValue(entry.getValue().setScale(1, BigDecimal.ROUND_DOWN));

					if (entry.getValue().compareTo(before) != 0) {
						rank = count;
					}
					before = entry.getValue();

					if (count >= 15 + 1) {

						if (!containOpener) {

							if (userMoney.compareTo(BigDecimal.ZERO) <= 0) {
								lore.add(ChatColor.AQUA + StringUtils.repeat("-", 30));
								lore.add(ChatColor.DARK_BLUE + "YOU > " + ChatColor.GRAY + "所持金が0円のため対象外");
								break;
							}

							if (entry.getKey().equals(player.getName())) {
								containOpener = true;

								lore.add(ChatColor.AQUA + StringUtils.repeat("-", 30));
								lore.add(ChatColor.DARK_BLUE + "YOU > " + ChatColor.LIGHT_PURPLE + rank + "位 "
										+ ChatColor.YELLOW + player.getName() + ChatColor.GREEN + ": " + ChatColor.RED
										+ entry.getValue());
								break;
							}
						}

						count++;
						continue;
					}

					String prefix = "";
					if (entry.getKey().equals(player.getName())) {
						containOpener = true;
						prefix = ChatColor.DARK_BLUE + "YOU > ";
					}

					lore.add(prefix + ChatColor.LIGHT_PURPLE + rank + "位 " + ChatColor.YELLOW + entry.getKey()
							+ ChatColor.GREEN + ": " + ChatColor.RED
							+ entry.getValue().toString());
					count++;
				}

				moneyRankMeta.setLore(lore);
				moneyRank.setItemMeta(moneyRankMeta);

				inv.setItem(14, moneyRank);
			}
		}).start();

		inv.setItem(10, ticket);
		inv.setItem(12, ca);
		inv.setItem(14, moneyRank);
		inv.setItem(16, head);
		inv.setItem(22, vote);
		inv.setItem(26, server);

		return inv;
	}

	private synchronized static List<Entry<String, BigDecimal>> sortedMoneyList() {

		HashMap<String, BigDecimal> moneyMap = new HashMap<>();

		Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
		UserMap map = ess.getUserMap();

		for (UUID uuid : map.getAllUniqueUsers()) {
			User user = ess.getUser(uuid);

			String name = user.getName();
			BigDecimal money = user.getMoney();

			PlayerData pd = DataManager.getPlayerData(user.getConfigUUID());
			BigInteger ticketMoney = BigInteger.ZERO;
			if (pd != null) {
				ticketMoney = pd.getMoney();
			}

			if (ticketMoney.compareTo(BigInteger.ZERO) < 0) {
				ticketMoney = BigInteger.ZERO;
			}

			moneyMap.put(name, money.add(new BigDecimal(ticketMoney)));
		}

		List<Entry<String, BigDecimal>> entryList = new ArrayList<Entry<String, BigDecimal>>(moneyMap.entrySet());

		Collections.sort(entryList, new Comparator<Entry<String, BigDecimal>>() {
			public int compare(Entry<String, BigDecimal> obj1, Entry<String, BigDecimal> obj2) {
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});

		return entryList;
	}

	public static ItemStack getPlayerSkull(Player p) {

		BigInteger tickets = DataManager.getPlayerData(p).getTickets();
		BigDecimal money = BigDecimal.valueOf(HomoGUI.getEconomy().getBalance(p));

		String displayName = ChatColor.YELLOW + p.getName() + ChatColor.RED + "の情報";

		String ticketStr = ChatColor.RED + "チケット" + ChatColor.GREEN + ": " + ChatColor.YELLOW + tickets.toString();
		String moneyStr = ChatColor.RED + "所持金" + ChatColor.GREEN + ": " + ChatColor.YELLOW + money.toPlainString();

		ItemStack skull = ItemHelper.createSkull(p, displayName, ticketStr, moneyStr);
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
		return 9 * 3;
	}
}
