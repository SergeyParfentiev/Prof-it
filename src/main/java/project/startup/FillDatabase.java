package project.startup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.dto.CommentDTO;
import project.dto.PostDTO;
import project.dto.UserDTO;
import project.service.CommentService;
import project.service.PostService;
import project.service.TruncateDatabaseService;
import project.service.UserService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class FillDatabase {

    private Logger logger = LogManager.getLogger(FillDatabase.class);

    private final TruncateDatabaseService truncateDatabaseService;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private Gson gson;

    @Autowired
    public FillDatabase(TruncateDatabaseService truncateDatabaseService, UserService userService, PostService postService, CommentService commentService) {
        this.truncateDatabaseService = truncateDatabaseService;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        gson = new Gson();
    }

    public void run() {
        try {
            truncateDatabase();

            fillUserDataFromString(fetchContent("https://jsonplaceholder.typicode.com/users"));
            fillPostDataFromString(fetchContent("https://jsonplaceholder.typicode.com/posts"));
            fillCommentDataFromString(fetchContent("https://jsonplaceholder.typicode.com/comments"));

        } catch (Exception e) {
            logger.error("Throw exception when fill database. Exception: '{}'", e);
        }
    }

    public void truncateDatabase() throws Exception {
        truncateDatabaseService.truncate();
    }

    public void fillUserDataFromString(String content) {
        userService.save(getListFromString(content, UserDTO.class));
    }

    public void fillPostDataFromString(String content) {
        postService.save(getListFromString(content, PostDTO.class));
    }

    public void fillCommentDataFromString(String content) {
        commentService.save(getListFromString(content, CommentDTO.class));
    }

    public <T> List<T> getListFromString(String content, Class<T> t) {
        return gson.fromJson(content, TypeToken.getParameterized(ArrayList.class, t).getType());
    }

    private String fetchContent(String uri) throws IOException {
        final int OK = 200;
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int responseCode = connection.getResponseCode();
        if (responseCode == OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        }

        return "";
    }

    public String loadFromFile(String fileName) {
        StringBuilder fileData = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File(fileName)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileData.append(line);
            }

        } catch (Exception e) {
            logger.error("Throw exception when read from file. Exception: '{}'", e);
        }

        return fileData.toString();
    }
}
