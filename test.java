import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HighchartsLoadTest {
    private static final String EXPORT_SERVER_URL = "http://localhost:8080";
    private static final int NUM_THREADS = 10;  // Number of concurrent users
    private static final int NUM_REQUESTS_PER_THREAD = 100;  // Requests per thread

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < NUM_REQUESTS_PER_THREAD; j++) {
                    sendExportRequest();
                }
            });
        }

        executorService.shutdown();
    }

    private static void sendExportRequest() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(EXPORT_SERVER_URL);

            // Example payload for Highcharts export
            String payload = """
                {
                    "infile": {
                        "chart": {
                            "type": "bar"
                        },
                        "title": {
                            "text": "Load Test Chart"
                        },
                        "xAxis": {
                            "categories": ["Apples", "Bananas", "Oranges"]
                        },
                        "series": [
                            {
                                "name": "John",
                                "data": [5, 3, 4]
                            },
                            {
                                "name": "Jane",
                                "data": [2, 2, 3]
                            },
                            {
                                "name": "Joe",
                                "data": [3, 4, 4]
                            }
                        ]
                    },
                    "type": "image/png"
                }
            """;

            StringEntity entity = new StringEntity(payload);
            postRequest.setEntity(entity);
            postRequest.setHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(postRequest);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                System.out.println("Request successful");
            } else {
                System.out.println("Failed with status code: " + statusCode);
            }
        } catch (Exception e) {
            System.err.println("Error during request: " + e.getMessage());
        }
    }
}
