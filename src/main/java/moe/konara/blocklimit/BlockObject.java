package moe.konara.blocklimit;

public class BlockObject {
    public String ID;
    public Integer Range;
    public Integer Count;
    public BlockObject(String block){
        this.ID=block;
        this.Range=1;
        this.Count=1;
    }
}
