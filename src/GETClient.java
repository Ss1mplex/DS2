import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GETClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 4567;

        if (args.length > 0) {
            serverAddress = args[0];
        }
        if (args.length > 1) {
            serverPort = Integer.parseInt(args[1]);
        }

        Socket socket = null;

        try {
            socket = new Socket(serverAddress, serverPort);

            while (true) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String getRequest = "GET /data.txt HTTP/1.1";
                out.println(getRequest);
                out.println("User-Agent: GETClient/1.0");
                out.println();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
