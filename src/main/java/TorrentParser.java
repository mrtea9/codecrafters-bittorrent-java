import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Arrays;

public class TorrentParser {
    private String trackerPath;

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
    }

    public String parseTorrent() {
        Path path = Paths.get(trackerPath);
        byte[] data;

        try {
            data = Files.readAllBytes(path);
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return "error";
        }

        System.out.println(Arrays.toString(data));
        return this.trackerPath;
    }
}
