package moe.konara.blocklimit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class BlockLimit extends JavaPlugin implements Listener {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Logger logger;
    public static File configFolder;
    public static BlockList CONFIG = new BlockList();
    @Override
    public void onEnable() {
        this.logger = getServer().getLogger();
        getServer().getPluginManager().registerEvents(this, this);
        configFolder = new File("./plugins/BlockLimit");
        if(!configFolder.exists()){
            configFolder.mkdirs();
        }
        CONFIG.load();
        logger.info("BlockLimit已加载配置数:"+CONFIG.LimitedBlocks.size());
        Objects.requireNonNull(Bukkit.getPluginCommand("blocklimit")).setExecutor(new CommandHandler());
        Objects.requireNonNull(Bukkit.getPluginCommand("blocklimit")).setTabCompleter(new CommandHandler());
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event){
        BlockObject block = CONFIG.get(event.getBlock().getType().getKey().toString());
        if(block!=null){
            if(!(CONFIG.ByPassPermission && event.getPlayer().hasPermission("blocklimit.bypass"))) {
                List<v3> list = new ArrayList<>();
                Location loc = event.getBlock().getLocation();
                World world = event.getBlock().getWorld();
                int flag=0;
                for(int x = -block.Range; x <= block.Range; x++){
                    for(int z = -block.Range; z <= block.Range; z++){
                        for(int y = -block.Range; y<= block.Range; y++){
                            Location temp = loc.clone();
                            if(world.getBlockAt(temp.add(x,y,z)).getType().getKey().toString().equals(block.ID)){
                                if(!temp.equals(loc)) {
                                    flag++;
                                    list.add(new v3(temp.getBlockX(), temp.getBlockY(), temp.getBlockZ()));
                                }
                            }
                            if(flag >= block.Count)break;
                        }
                        if(flag >= block.Count)break;
                    }
                    if(flag >= block.Count)break;
                }
                if(flag >= block.Count){
                    event.getPlayer().sendMessage(block.ID+"存在于"+list);
                    event.getPlayer().sendMessage("方块" + block.ID + "在半径为" + block.Range+"的范围内只能存在" + block.Count + "个");
                    event.setCancelled(true);
                }
            }
        }
    }
}
