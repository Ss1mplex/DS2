import org.json.JSONObject;

import java.io.*;
import java.net.*;

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

        try {

            BufferedReader dataReader = new BufferedReader(new FileReader(dataFilePath));
            String jsonData = convertToJSON(dataReader);


            System.out.println("Request Sent:");
            System.out.println(jsonData);

            Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


            String putRequest = createPutRequest("data.txt", jsonData);
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

    private static String createPutRequest(String path, String jsonData) {
        StringBuilder request = new StringBuilder();
        request.append("PUT ").append(path).append(" HTTP/1.1\n");
        request.append("User-Agent: ATOMClient/1/0\n");
        request.append("Content-Type: application/json\n");
        request.append("Content-Length: ").append(jsonData.length()).append("\n\n");
        request.append(jsonData);


        System.out.println("Request Sent: " + request.toString());

        return request.toString();
    }


}
