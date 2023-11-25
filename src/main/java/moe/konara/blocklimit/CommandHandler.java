package moe.konara.blocklimit;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandHandler implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("blocklimit") && args.length >= 1) {
            if (args[0].equalsIgnoreCase("add")) {
                if(args.length == 1 && sender instanceof Player){
                    String holding = ((Player) sender).getInventory().getItemInMainHand().getType().getKey().toString();
                    if(!holding.isEmpty()) {
                        if (BlockLimit.CONFIG.add(holding))
                            sender.sendMessage("已添加" + holding);
                        else
                            sender.sendMessage("该物品已经存在于列表中,或读取配置文件失败");
                    }else {
                        sender.sendMessage("What's empty Item?");
                    }
                } else if (args.length == 2) {
                    if(!args[1].isEmpty()) {
                        if (BlockLimit.CONFIG.add(args[1]))
                            sender.sendMessage("已添加" + args[1]);
                        else
                            sender.sendMessage("该物品已经存在于列表中,或读取配置文件失败");
                    }else {
                        sender.sendMessage("What's empty Item?");
                    }
                }else {
                    sender.sendMessage("Cannot apply parameters more than 1 after add.");
                }
            }else if(args[0].equalsIgnoreCase("remove")){
                if (args.length == 2) {
                    if(!args[1].isEmpty()) {
                        if (BlockLimit.CONFIG.remove(args[1]))
                            sender.sendMessage("已删除"+args[1]);
                        else
                            sender.sendMessage("该物品不存在于列表中,或读取配置文件失败");
                    }else {
                        sender.sendMessage("What's empty Item?");
                    }
                }else{
                    sender.sendMessage("Please offer 1 parameter after remove.");
                }
            }else if(args[0].equalsIgnoreCase("set")){
                if(args.length==4){
                    BlockLimit.CONFIG.load();
                    BlockObject block = BlockLimit.CONFIG.get(args[1]);
                    if(block!=null){
                        if(args[2].equals("range")){
                            block.Range= Integer.valueOf(args[3]);
                            BlockLimit.CONFIG.save();
                            sender.sendMessage("已修改"+block.ID+".Range为"+block.Range);
                        } else if (args[2].equals("count")) {
                            block.Count= Integer.valueOf(args[3]);
                            BlockLimit.CONFIG.save();
                            sender.sendMessage("已修改"+block.ID+".Count为"+block.Count);
                        }else{
                            sender.sendMessage("不存在键值" + args[2]);
                        }
                    }else{
                        sender.sendMessage("列表中无" + args[1]);
                    }
                }else{
                    sender.sendMessage("Please offer 3 parameter after set.");
                }
            }else if(args[0].equalsIgnoreCase("reload")){
                if(BlockLimit.CONFIG.load()){
                    sender.sendMessage("重载成功");
                }else {
                    sender.sendMessage("重载失败");
                }

            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            return Arrays.asList(new String[]{"add", "set", "remove", "reload"});
        }else
        if(args.length == 2 && args[0].equalsIgnoreCase("add")) {
            return Collections.singletonList("选填[ItemID]");
        }else
        if(args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            return BlockLimit.CONFIG.getList();
        }else
        if(args.length == 2 && args[0].equalsIgnoreCase("set")) {
            return BlockLimit.CONFIG.getList();
        }else
        if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
            return Arrays.asList(new String[]{"range", "count"});
        }else
        if(args.length == 4 && args[0].equalsIgnoreCase("set")) {
            return Collections.singletonList("<value>");
        }
        return new ArrayList<>();
    }
}
