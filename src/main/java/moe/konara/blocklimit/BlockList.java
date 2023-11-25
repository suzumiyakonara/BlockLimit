package moe.konara.blocklimit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BlockList {
    Boolean ByPassPermission;
    List<BlockObject> LimitedBlocks;
    public void init(){
        ByPassPermission =true;
        LimitedBlocks=new ArrayList<>();
    }

    public BlockObject get(String block){
        for(BlockObject e:LimitedBlocks){
            if(block.equals(e.ID)){
                return e;
            }
        }
        return null;
    }

    public List<String> getList(){
        List<String> list=new ArrayList<>();
        for (BlockObject e:this.LimitedBlocks){
            list.add(e.ID);
        }
        return list;
    }

    public boolean add(String block){
        if(!load()) return false;
        if(get(block)!=null) return false;
        LimitedBlocks.add(new BlockObject(block));
        save();
        return true;
    }
    public boolean remove(String block){
        if(!load())return false;
        Iterator<BlockObject> iterator = LimitedBlocks.iterator();
        while (iterator.hasNext()){
            if(iterator.next().ID.equals(block)){
                iterator.remove();
                save();
                return true;
            }
        }
        return false;
    }

    public boolean load(){
        File configFile = new File(BlockLimit.configFolder.getPath(),"config.json");
        if(!configFile.exists()){
            init();
            save();
            return true;
        }
        String rawConfig = "";
        try(InputStream inputStream = new FileInputStream(configFile)) {
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
            while (scanner.hasNextLine()) {
                rawConfig += scanner.nextLine()+'\n';
            }
            if(!rawConfig.isEmpty())
                rawConfig = rawConfig.substring(0,rawConfig.length()-1);
        }catch (IOException e) {
            BlockLimit.logger.warning("BlockLimit读取文件出错");
            e.printStackTrace();
            return false;
        }
        BlockList config = BlockLimit.gson.fromJson(rawConfig,BlockList.class);
        if(config!=null){
            boolean flag=false;
            if(config.ByPassPermission!=null) {
                this.ByPassPermission = config.ByPassPermission;
            }else{
                flag=true;
                this.ByPassPermission=true;
            }
            if(config.LimitedBlocks!=null)
                this.LimitedBlocks = config.LimitedBlocks;
            else {
                BlockLimit.logger.warning("BlockLimit无法读取配置文件");
                return false;
            }
            if(flag){
                save();
            }
        }else{
            BlockLimit.logger.warning("BlockLimit无法读取配置文件");
            return false;
        }
        return true;
    }
    public void save(){
        File configFile = new File(BlockLimit.configFolder.getPath(),"config.json");
        if(!configFile.exists()){
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                BlockLimit.logger.warning("BlockLimit无法创建配置文件");
                e.printStackTrace();
            }
        }
        try {
            PrintWriter out = new PrintWriter(configFile, StandardCharsets.UTF_8);
            out.print(BlockLimit.gson.toJson(this));
            out.flush();
            out.close();
        } catch (IOException e) {
            BlockLimit.logger.warning("BlockLimit无法保存配置文件");
            e.printStackTrace();
        }
    }
}
