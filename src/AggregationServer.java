import org.json.*;
import java.io.*;
import java.net.*;

public class AggregationServer {
    public static void main(String[] args) {
        int port = 4567;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Aggregation Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new AggregationServerThread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



class AggregationServerThread extends Thread {
    private LamportClock lamportClock;
    private Socket clientSocket;
    private JSONObject aggregatedData;

    public AggregationServerThread(Socket socket) {
        this.clientSocket = socket;
        this.aggregatedData = new JSONObject();
        this.lamportClock = new LamportClock();
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            StringBuilder request = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                lamportClock.tick();
                request.append(inputLine).append("\n");
                if (inputLine.isEmpty() || inputLine.trim().isEmpty()) {
                    break;
                }
            }

            if (request.toString().startsWith("PUT")) {
                String jsonData = extractJsonData(request.toString());
                updateAggregatedData(jsonData);
                out.println("HTTP/1.1 200 OK");
            } else if (request.toString().startsWith("GET")) {
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println();
                out.println(aggregatedData.toString());
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractJsonData(String httpRequest) {
        int startIndex = httpRequest.indexOf("{");
        int endIndex = httpRequest.lastIndexOf("}");
        if (startIndex != -1 && endIndex != -1) {
            String jsonData = httpRequest.substring(startIndex, endIndex + 1);
            return jsonData;
        } else {
            return "{}";
        }
    }

    private void updateAggregatedData(String jsonData) {
        try {
            System.out.println("Received data: " + jsonData);
            JSONObject json = new JSONObject(jsonData);

            String id = json.getString("id");

            if (aggregatedData.has(id)) {
                JSONObject existingData = aggregatedData.getJSONObject(id);


                existingData.put("name", json.getString("name"));
                existingData.put("state", json.getString("state"));
                existingData.put("time_zone", json.getString("time_zone"));
                existingData.put("lat", json.getDouble("lat"));
                existingData.put("lon", json.getDouble("lon"));
                existingData.put("local_date_time", json.getString("local_date_time"));
                existingData.put("local_date_time_full", json.getString("local_date_time_full"));
                existingData.put("air_temp", json.getDouble("air_temp"));
                existingData.put("apparent_t", json.getDouble("apparent_t"));
                existingData.put("cloud", json.getString("cloud"));
                existingData.put("dewpt", json.getDouble("dewpt"));
                existingData.put("press", json.getDouble("press"));
                existingData.put("rel_hum", json.getInt("rel_hum"));
                existingData.put("wind_dir", json.getString("wind_dir"));
                existingData.put("wind_spd_kmh", json.getInt("wind_spd_kmh"));
                existingData.put("wind_spd_kt", json.getInt("wind_spd_kt"));
            } else {
                aggregatedData.put(id, json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
