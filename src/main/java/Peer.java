


public class Peer {
    private final Torrent torrent;

    public Peer(Torrent torrent) {
        this.torrent = torrent;
    }

    public String discoverPeers() {
        String full_address = this.torrent.announce;

        return full_address;
    }
}
