import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HighchartsExportServerLoadTest {

    private static final String HCE_URL = "http://your-highcharts-export-server-url/export";
    private static CloseableHttpClient httpClient;

    @BeforeClass
    public static void setUp() {
        httpClient = HttpClients.createDefault();
    }

    // Single load test request to Highcharts Export Server
    @Test
    public void testLoadRequest() throws IOException {
        HttpPost request = new HttpPost(HCE_URL);
        
        // Example body; adjust as needed for your specific Highcharts Export Server
        String jsonBody = "{ \"infile\": \"...\" }";  // Replace with actual JSON input

        // Add request body
        request.setEntity(new org.apache.http.entity.StringEntity(jsonBody));

        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        // Log the response
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println("Response: " + responseBody);

        // Basic assertion: check if response code is 200 OK
        assertEquals(200, statusCode);
    }

    // Load test with multiple parallel requests
    @Test
    public void testConcurrentLoad() throws Exception {
        int numberOfRequests = 10;  // Number of parallel requests
        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = "{ \"infile\": \"...\" }";  // Replace with actual JSON input

        // Use CountDownLatch to wait for all threads to finish
        CountDownLatch latch = new CountDownLatch(numberOfRequests);

        for (int i = 0; i < numberOfRequests; i++) {
            // Create a new thread for each request
            new Thread(() -> {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(HCE_URL))
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .timeout(Duration.ofSeconds(10))  // Timeout for each request
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // Check if the response code is 200 OK
                    assertEquals(200, response.statusCode());
                    assertTrue(response.body().length() > 0);  // Ensure response body is not empty
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();  // Decrease the latch count when done
                }
            }).start();
        }

        // Wait for all requests to finish
        latch.await(30, TimeUnit.SECONDS);  // Wait up to 30 seconds for all threads to complete
    }
}
