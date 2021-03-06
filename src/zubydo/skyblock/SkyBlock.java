package zubydo.skyblock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import zubydo.skyblock.Listeners.PlayerJoin;
import zubydo.skyblock.Listeners.PlayerMove;
import zubydo.skyblock.Listeners.PlayerQuit;
import zubydo.skyblock.Listeners.PlayerRespawn;
import zubydo.skyblock.commands.IslandCommand;
import zubydo.skyblock.island.IslandManager;
import zubydo.skyblock.tpa.TpaManager;
import zubydo.skyblock.world.SkyBlockGen;

public class SkyBlock extends JavaPlugin {
	

	String worldName = "world_skyblock";
	public World world;
	public WorldEditPlugin worldedit;
	private static SkyBlock skyBlock;
	
	

	@Override
	public void onEnable() {
		
		skyBlock = this;
		this.worldedit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (worldedit == null) {
			sendMessage("ERROR, You must have worldedit on your server!");

		} else {
			makeWorld();
			new IslandManager();
			new TpaManager();
			registerPermissions();
			registerCommands();
			registerListeners();
		}
	}

	private void registerPermissions() {
		PluginManager pm = Bukkit.getPluginManager();
		Permission p = new Permission("skyblock.admin");
		p.setDefault(PermissionDefault.OP);
		pm.addPermission(p);
	}

	@Override
	public void onDisable() {
		IslandManager.getIslandManager().onDisable();
	}

	private void registerCommands() {
		getCommand("cw").setExecutor(new IslandCommand());
	}

	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new PlayerMove(), this);
		pm.registerEvents(new PlayerRespawn(), this);
	}

	private void makeWorld() {
		if (Bukkit.getWorld(worldName) == null) {
			sendMessage("Generating world: '" + worldName + "'");
			WorldCreator wc = new WorldCreator(worldName);
			wc.generateStructures(false);
			wc.generator(new SkyBlockGen());
			Bukkit.getServer().createWorld(wc);
		}
		sendMessage("Loaded world: '" + worldName + "'");
		world = Bukkit.getWorld(worldName);
		world.setDifficulty(Difficulty.NORMAL);
	}

	public static SkyBlock getSkyBlock() {
		return skyBlock;
	}

	public void sendMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "(CrystalWars) " + ChatColor.AQUA + message);
	}
}