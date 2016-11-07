package vn.hnb;

/**
 * Created by huynh on 11/7/2016.
 */
public class ImportObject
{
    public String id;
    public String name;
    public String parentId;
    public String level;


    public ImportObject(String id, String name, String parentId, String level)
    {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
    }

    @Override
    public String toString() {
        return "ImportObject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
