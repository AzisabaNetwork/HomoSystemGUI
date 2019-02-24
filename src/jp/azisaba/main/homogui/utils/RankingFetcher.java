package jp.azisaba.main.homogui.utils;

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
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;

import jp.azisaba.main.homos.classes.PlayerData;
import jp.azisaba.main.homos.database.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;

public class RankingFetcher {

	public synchronized static List<Entry<String, BigDecimal>> getData() {

		HashMap<String, BigDecimal> moneyMap = new HashMap<>();

		Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
		UserMap map = ess.getUserMap();

		List<UUID> uuidList = new ArrayList<UUID>(map.getAllUniqueUsers());
		List<PlayerData> playerDataList = PlayerDataManager.getPlayerDataListByUUIDList(uuidList);

		for (PlayerData data : playerDataList) {
			User user = ess.getUser(data.getUuid());

			String name = user.getName();
			BigDecimal money = user.getMoney();

			BigInteger ticketMoney = data.getMoney();

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

	public static List<String> getDataByString(Player player, int lines) {

		Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
		UserMap map = ess.getUserMap();

		BigDecimal userMoney = map.getUser(player.getUniqueId()).getMoney();

		List<Entry<String, BigDecimal>> moneyMap = getData();

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

			if (count >= lines + 1) {

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

		return lore;
	}
}
