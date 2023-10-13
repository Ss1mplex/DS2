import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AggregationServer {
    public static JSONObject weatherData = new JSONObject();
    public static String lastTimeBody;
    public static int lamportClock = 0; // 初始化Lamport时钟为0
    public static long dataExpirationTime = 30 * 1000; // 数据过期时间（30秒）

    public static void parseToWeatherdata(String body) {
        body = body.replaceAll("[\\{\\}\"]", "");
        String[] data = body.split(",");
        for (String line : data) {
            String[] subline = line.split(":");
            weatherData.put(subline[0], subline[1]);
        }
        System.out.println(weatherData);
    }

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        // active and check every period
        executor.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            if (lastTimeBody != null) {
                long dataTimestamp = Long.parseLong(lastTimeBody.split("\"timestamp\":")[1].split(",")[0]);
                if (currentTime - dataTimestamp > dataExpirationTime) {
                    // clear the data
                    weatherData = new JSONObject();
                    lastTimeBody = null;
                    System.out.println("Expired data has been purged.");
                }
            }
        }, 0, 10, TimeUnit.SECONDS); // check every 10s

        try (ServerSocket serverSocket = new ServerSocket(4567)) {
            while (true) {
                System.out.println("Aggregation server is working");
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    // Read the request line
                    String requestLine = reader.readLine();
                    System.out.println("Received: " + requestLine);
                    if (requestLine.contains("PUT")) {
                        // Increment Lamport clock for each event
                        lamportClock++;

                        // Read headers and find Content-Length
                        String headerLine;
                        int contentLength = 0;
                        while (!(headerLine = reader.readLine()).isEmpty()) {
                            if (headerLine.startsWith("Content-Length:")) {
                                contentLength = Integer.parseInt(headerLine.split(": ")[1]);
                            }
                        }

                        // Read body
                        char[] bodyChars = new char[contentLength];
                        reader.read(bodyChars, 0, contentLength);
                        String body = new String(bodyChars);
                        System.out.println("Body: " + body);

                        // First time or newer Lamport clock
                        if (lastTimeBody == null || lamportClock > Integer.parseInt(body.split("\"lamport\":")[1].split(",")[0])) {
                            lastTimeBody = body;
                            parseToWeatherdata(body);
                            // Send response
                            writer.write("201 - HTTP_CREATED\r\n");
                        } else {
                            // Send response indicating the data is outdated
                            writer.write("202 - Data outdated\r\n");
                        }

                        // Send HTTP response
                        writer.write("HTTP/1.1 200 OK\r\n");
                        writer.write("Content-Type: text/plain\r\n");
                        writer.write("\r\n");
                        writer.write("Data Received");
                        writer.flush();
                    } else if (requestLine.contains("GET")) {
                        if (weatherData.isEmpty() == true) {
                            writer.write("205 - no data");
                        } else {
                            writer.write(weatherData.toString());
                        }
                    } else {
                        // Any request other than GET or PUT
                        writer.write("400 - invalid request\r\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
