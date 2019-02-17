package jp.azisaba.main.homogui.gui;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;

import jp.azisaba.main.homogui.HomoGUI;
import jp.azisaba.main.homogui.tickets.DataManager;
import jp.azisaba.main.homogui.utils.ItemHelper;
import jp.azisaba.main.homos.classes.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class MainGUI {

	public static String getInvTitle() {
		return ChatColor.YELLOW + "Main Menu";
	}

	public static Inventory getInv(Player player) {
		Inventory inv = Bukkit.createInventory(null, 9 * 3, getInvTitle());

		ItemStack ticket = getTicketItem();
		ItemStack ca = getCAItem();
		ItemStack head = getPlayerSkull(player);
		ItemStack vote = getVoteItem();
		ItemStack server = getServerSelector();
		ItemStack moneyRank = getMoneyRankingItem();
		ItemMeta moneyRankMeta = moneyRank.getItemMeta();

		new Thread(new Runnable() {

			private Player p = player;

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

							if (entry.getValue().compareTo(userMoney) == 0) {
								containOpener = true;

								lore.add(ChatColor.AQUA + StringUtils.repeat("-", 30));
								lore.add(ChatColor.DARK_BLUE + "YOU > " + ChatColor.LIGHT_PURPLE + rank + "位 "
										+ ChatColor.YELLOW + p.getName() + ChatColor.GREEN + ": " + ChatColor.RED
										+ entry.getValue());
								break;
							}
						}

						count++;
						continue;
					}

					String prefix = "";
					if (entry.getKey().equals(p.getName())) {
						containOpener = true;
						prefix = ChatColor.DARK_BLUE + "YOU > ";
					}

					lore.add(prefix + ChatColor.LIGHT_PURPLE + rank + "位 " + ChatColor.YELLOW + entry.getKey()
							+ ChatColor.GREEN + ": " + ChatColor.RED
							+ entry.getValue());
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
				// 4. 昇順
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});

		return entryList;
	}

	public static ItemStack getTicketItem() {
		ItemStack ticket = new ItemStack(Material.PAPER);
		ItemMeta ticketMeta = ticket.getItemMeta();
		ticketMeta.setDisplayName(ChatColor.YELLOW + "チケット売買");
		ticketMeta.setLore(Arrays.asList(ChatColor.GREEN + "チケットの売買が行えます"));
		ticket.setItemMeta(ticketMeta);

		return ticket;
	}

	public static ItemStack getCAItem() {
		ItemStack ca = new ItemStack(Material.ANVIL);
		ItemMeta caMeta = ca.getItemMeta();
		caMeta.setDisplayName(ChatColor.RED + "オークションメニューを開く");
		ca.setItemMeta(caMeta);

		return ca;
	}

	public static ItemStack getMoneyRankingItem() {
		ItemStack moneyRank = new ItemStack(Material.GOLD_INGOT);
		ItemMeta moneyRankMeta = moneyRank.getItemMeta();
		moneyRankMeta.setDisplayName(ChatColor.GREEN + "総資金ランキング");
		moneyRankMeta.setLore(Arrays.asList(ChatColor.RED + "取得中..."));
		moneyRank.setItemMeta(moneyRankMeta);

		return moneyRank;
	}

	public static ItemStack getVoteItem() {
		ItemStack vote = new ItemStack(Material.DIAMOND);
		ItemMeta meta = vote.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "アジ鯖に投票！");
		meta.setLore(Arrays.asList(ChatColor.RED + "クリックで投票リンクを表示！"));
		vote.setItemMeta(meta);

		return vote;
	}

	public static ItemStack getServerSelector() {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "サーバー選択");
		meta.setLore(Arrays.asList(ChatColor.RED + "ほかのサーバーに移動することができます"));
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack getPlayerSkull(Player p) {
		ItemStack skull = ItemHelper.createSkull(p);
		ItemMeta meta = skull.getItemMeta();

		BigInteger tickets = DataManager.getPlayerData(p).getTickets();
		BigDecimal money = BigDecimal.valueOf(HomoGUI.getEconomy().getBalance(p));

		String ticketStr = ChatColor.RED + "チケット" + ChatColor.GREEN + ": " + ChatColor.YELLOW + tickets.toString();
		String moneyStr = ChatColor.RED + "所持金" + ChatColor.GREEN + ": " + ChatColor.YELLOW + money.toPlainString();

		meta.setDisplayName(ChatColor.YELLOW + p.getName() + ChatColor.RED + "の情報");
		meta.setLore(Arrays.asList(ticketStr, moneyStr));

		skull.setItemMeta(meta);
		return skull;
	}
}
