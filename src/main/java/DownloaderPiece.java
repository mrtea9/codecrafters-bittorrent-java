import java.net.Socket;

public class DownloaderPiece {
    private Torrent torrent;
    private String fileToCreate;
    private int pieceIndex;
    private Peer peer;
    private PeerConnection peerConnection;

    public DownloaderPiece(Torrent torrent,String fileToCreate,int pieceIndex){
        this.torrent = torrent;
        this.fileToCreate = fileToCreate;
        this.pieceIndex = pieceIndex;
        this.peer = new Peer(torrent);
        this.peer.performGetRequest();

        this.peerConnection = new PeerConnection(this.peer.peersList.getFirst(), torrent);
        this.peerConnection.creatingConnection();
    }
}
