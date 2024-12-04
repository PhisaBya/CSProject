import org.junit.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class HighchartsExportServerLoadTest {

    private static final String HCE_URL = "https://your-highcharts-export-server-url/export";  // Replace with your server URL
    private static final int NUM_REQUESTS = 100;  // Number of requests to send

    @Test
    public void testLoadWithMultipleRequests() throws Exception {
        // Create a thread pool to send requests concurrently
        ExecutorService executor = Executors.newFixedThreadPool(10);  // Adjust the pool size as needed

        // Create an HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        // Submit tasks for 100 requests
        for (int i = 0; i < NUM_REQUESTS; i++) {
            executor.submit(() -> {
                try {
                    // Define the request body (adjust as needed)
                    String jsonBody = "{\n" +
                          "  \"infile\": \"{\\\"chart\\\":{\\\"type\\\":\\\"line\\\"},\\\"title\\\":{\\\"text\\\":\\\"Simple Line Chart\\\"},\\\"xAxis\\\":{\\\"categories\\\":[\\\"Jan\\\",\\\"Feb\\\",\\\"Mar\\\",\\\"Apr\\\",\\\"May\\\",\\\"Jun\\\",\\\"Jul\\\",\\\"Aug\\\",\\\"Sep\\\",\\\"Oct\\\",\\\"Nov\\\",\\\"Dec\\\"]},\\\"yAxis\\\":{\\\"title\\\":{\\\"text\\\":\\\"Value\\\"}},\\\"series\\\":[{\\\"name\\\":\\\"2019\\\",\\\"data\\\":[29.9,71.5,106.4,129.2,144.0,176.0,135.6,148.5,216.4,194.1,95.6,54.4}]}},\n" +
                          "  \"type\": \"image/png\"\n" +
                          "}"; // Replace with actual JSON input

                    // Build the HTTP POST request
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(HCE_URL))
                            .timeout(Duration.ofSeconds(10))
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    // Send the request and get the response
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // Check the response status code and assert success (status code 200)
                    int statusCode = response.statusCode();
                    assertEquals(200, statusCode);  // Adjust as needed based on expected status code

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor after submitting all tasks
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);  // Adjust the timeout if necessary
    }
}
