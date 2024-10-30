import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        String str = new String(data, StandardCharsets.UTF_8);
        System.out.println(str);
        return this.trackerPath;
    }
}
