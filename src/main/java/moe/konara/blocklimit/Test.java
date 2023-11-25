package moe.konara.blocklimit;

import com.google.gson.Gson;

public class Test {
    public static void main(String[] args){
        BlockList config = new Gson().fromJson("{\n" +
                "  \"LimitedBlocks\": [\n" +
                "    \"RIGHT\",\n" +
                "    \"minecraft:grass_block\"\n" +
                "  ]\n" +
                "}",BlockList.class);
        System.out.println(config.ByPassPermission);
    }
}
