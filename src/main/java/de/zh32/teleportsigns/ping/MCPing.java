package de.zh32.teleportsigns.ping;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
 
public final class MCPing {
    @Setter
    @Getter
    private InetSocketAddress address;
    @Setter
    @Getter
    private int timeout = 1500;
    @Getter
    private int pingVersion = -1;
    @Getter
    private int protocolVersion = -1;
    @Getter
    private String gameVersion;
    @Getter
    private String motd;
    @Getter
    private int playersOnline = -1;
    @Getter
    private int maxPlayers = -1;

    public boolean fetchData() {
        try {
            Socket socket = new Socket();
            OutputStream outputStream;
            DataOutputStream dataOutputStream;
            InputStream inputStream;
            InputStreamReader inputStreamReader;

            socket.setSoTimeout(this.timeout);

            socket.connect(address,this.getTimeout());

            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);

            inputStream = socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-16BE"));

            dataOutputStream.write(new byte[]{
                    (byte) 0xFE,
                    (byte) 0x01
            });

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
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Bukkit.getLogger().log(Level.SEVERE, null, ex);
            }
            dataOutputStream.close();
            outputStream.close();

            inputStreamReader.close();
            inputStream.close();
            socket.close();
        } catch (SocketException exception) {
                return false;
        } catch (IOException exception) {
                return false;
        }

        return true;
}
}
