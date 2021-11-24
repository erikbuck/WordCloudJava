import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// This class uses the web API, https://quickchart.io/documentation/word-cloud-api/, to generate
// a word cloud as .png file
public class WordCloudMaker {
    // The words to put in the cloud. The size of each word will depend on teh number of times it
    // occurs in the array
    ArrayList<String> words = new ArrayList<String>();

    // Add a word to the array of words that will be in the generated word cloud
    void addWord(String word) {
        words.add(word);
    }

    // Synchronously generate the word cloud as a .png file at the specified path. If path is null, 
    // Paths.get("cloud.png") is used.
    void generateWordCloud(Path path) throws IOException, InterruptedException {
        if(null == path) {
            path = Paths.get("cloud.png");
        }
        // This uses teh documented web API to specify attributes and words for the cloud to generate
        // JSON stands for Javascript Object Notation and it is a very common way of encoding data to
        // be sent or received using web protocols
        String json = "{" +
            "\"format\": \"png\"," +
            "\"width\": 1000," +
            "\"height\": 1000," +
            "\"fontFamily\": \"sans-serif\"," +
            "\"fontScale\": 15," +
            "\"scale\": \"linear\"," +
            "\"text\": \"";
        // Add the words to the JSON string
        for(String word : words) {
            json += word + " ";
        }
        json += "\"}";

        // Build a client to interact with the remote web server
        HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

        // Build an HTTPS POST request with the json and the request body
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://quickchart.io/wordcloud"))
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        // Send the POST and store the data returned in a file at path
        HttpResponse<Path> response = httpClient.send(request, HttpResponse.BodyHandlers.ofFile(path));

        // The following is for debugging only: Report the status from the web remote server
        System.out.println(response.toString());
    }
}
