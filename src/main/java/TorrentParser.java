

public class TorrentParser {
    private String trackerPath;

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
    }

    public String parseTorrent() {

        System.out.println(this.trackerPath);
        return this.trackerPath;
    }
}
