package es.uca.iw.eslada.api;

public class DataUsageRecord {
    private int megaBytes;
    private String date;

    public int getMegaBytes() {
        return megaBytes;
    }

    public void setMegaBytes(int megaBytes) {
        this.megaBytes = megaBytes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
