public class Task {


    public String getTitle() {
        return title;
    }

    public String getShortd() {
        return shortd;
    }

    public String getLongd() {
        return longd;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortd(String shortd) {
        this.shortd = shortd;
    }

    public void setLongd(String longd) {
        this.longd = longd;
    }

    private String title="";
    private String shortd = "";
    private String longd="";

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag ="";



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    private String date = "";
    protected long id = 0;
    public long getId() { return(id);
    }
    public void setId(long id) { this.id=id;
    }

    public String toString() { return(getTitle());
    }
}
