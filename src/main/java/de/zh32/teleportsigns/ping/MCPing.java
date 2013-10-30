package de.zh32.teleportsigns.ping;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
public final class MCPing {
    @Setter
    private InetSocketAddress address;
    @Setter
    private int timeout = 2500;
    private int pingVersion = -1;
    private int protocolVersion = -1;
    private String gameVersion;
    private String motd;
    private int playersOnline = -1;
    private int maxPlayers = -1;

    public boolean fetchData() {
        Socket socket = new Socket();  
        try {
            socket.setSoTimeout(this.timeout);

            socket.connect(address,this.getTimeout());
            OutputStream outputStream;
            DataOutputStream dataOutputStream;
            InputStream inputStream;
            InputStreamReader inputStreamReader;
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);

            inputStream = socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-16BE"));

            dataOutputStream.write(new byte[]{
                    (byte) 0xFE,
                    (byte) 0x01,
                    (byte) 0xFA
                    
            });
            
            dataOutputStream.write("MC|PingHost".getBytes("UTF-16BE"));
            dataOutputStream.writeShort( (address.getHostString().length() * 2) + 7);
            dataOutputStream.write(74);
            dataOutputStream.writeShort(address.getHostString().length());
            dataOutputStream.write(address.getHostString().getBytes("UTF-16BE"));
            dataOutputStream.writeInt(address.getPort());
            
            int packetId = inputStream.read();

            if (packetId == -1) {
                    throw new IOException("Premature end of stream.");
            }

            if (packetId != 0xFF) {
                    throw new IOException("Invalid packet ID (" + packetId + ").");
            }

            int length = inputStreamReader.read();

            if (length == -1) {
                    throw new IOException("Premature end of stream.");
            }

            if (length == 0) {
                    throw new IOException("Invalid string length.");
            }

            char[] chars = new char[length];

            if (inputStreamReader.read(chars,0,length) != length) {
                    throw new IOException("Premature end of stream.");
            }

            String string = new String(chars);

            if (string.startsWith("§")) {
                    String[] data = string.split("\0");

                    this.pingVersion = Integer.parseInt(data[0].substring(1));
                    this.protocolVersion = Integer.parseInt(data[1]);
                    this.gameVersion = data[2];
                    this.motd = data[3];
                    this.playersOnline = Integer.parseInt(data[4]);
                    this.maxPlayers = Integer.parseInt(data[5]);
            } else {
                    String[] data = string.split("§");

                    this.motd = data[0];
                    this.playersOnline = Integer.parseInt(data[1]);
                    this.maxPlayers = Integer.parseInt(data[2]);
            }
        } catch (Exception exception) {
            if (!(exception instanceof ConnectException))
                Bukkit.getLogger().log(Level.SEVERE, "[TeleportSigns] Error fetching data from server " + address.toString());
            return false;
        }
        finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException ex) {}
        }
        return true;
    }
}
