import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class ContentServer {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 4567;
        String dataFilePath = "data.txt";

        if (args.length > 0) {
            serverAddress = args[0];
        }
        if (args.length > 1) {
            serverPort = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            dataFilePath = args[2];
        }
        LamportClock lamport = new LamportClock();
        lamport.tick();
        int lamportClock = lamport.getValue();



        try {
            BufferedReader dataReader = new BufferedReader(new FileReader(dataFilePath));
            String jsonData = convertToJSON(dataReader);

            System.out.println("Request Sent:");
            System.out.println(jsonData);

            Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // add lamportclock
            String putRequest = createPutRequest("data.txt", jsonData, lamportClock);
            out.println(putRequest);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Response Received:");
                System.out.println(response);
            }

            dataReader.close();
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertToJSON(BufferedReader dataReader) {
        JSONObject json = new JSONObject();
        try {
            String line;
            while ((line = dataReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    json.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    private static String createPutRequest(String path, String jsonData, int lamportClock) {
        StringBuilder request = new StringBuilder();
        request.append("PUT data.txt HTTP/1.1\r\n");
        request.append("User-Agent: ATOMClient/1/0\r\n");
        request.append("Content-Type: application/json\r\n");
        request.append("Content-Length: ").append(jsonData.length()).append("\r\n");
      //add lamport clock in PUT
        request.append("Lamport-Clock: ").append(lamportClock).append("\r\n\r\n");
        request.append(jsonData);

        System.out.println("Request Sent: " + request);

        return request.toString();
    }
}
