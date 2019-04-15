package project;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import project.config.AppConfig;
import project.dto.CommentDTO;
import project.dto.PostDTO;
import project.dto.UserDTO;
import project.service.CommentService;
import project.service.PostService;
import project.service.UserService;
import project.startup.FillDatabase;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AppConfig.class)
@WebAppConfiguration
public class CommentServiceTest {

    private static Logger logger = LogManager.getLogger(CommentServiceTest.class);

    @Autowired
    private FillDatabase fillDatabase;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Before
    public void init() {
        try {
            fillDatabase.truncateDatabase();

            fillDatabase.fillUserDataFromString(fillDatabase.loadFromFile("src/test/resources/data/user_data.txt"));
            fillDatabase.fillPostDataFromString(fillDatabase.loadFromFile("src/test/resources/data/post_data.txt"));
            fillDatabase.fillCommentDataFromString(fillDatabase.loadFromFile("src/test/resources/data/comment_data.txt"));

        } catch (Exception e) {
            logger.error("Throw exception when fill database. Exception: '{}'", e);
        }
    }

    @Test
    public void testCountByUser() {
        // Correct data
        assertEquals(1, commentService.getList("Bret").size());

        // Incorrect data
        assertNotEquals(30, commentService.getList("wrongUser").size());
    }

    @Test
    public void testSpecificWordCountInBodies() {
        // Correct data
        assertEquals(21, commentService.getWordCount("myWord"));

        // Incorrect data
        assertNotEquals(8, commentService.getWordCount("dolorem"));
    }

    @Test
    public void testCreateOwnComment() {
        UserDTO currentUser = userService.getByUserName("Antonette");

        // Correct data
        PostDTO currentPost = postService.findById(13);
        CommentDTO ownComment = commentService.createOwnComment(currentUser.getUsername(), currentPost.getId(),
                "testName", "testBody");
        assertNotNull(ownComment);
        assertEquals(currentPost.getId(), ownComment.getPostId());
        assertEquals(currentUser.getEmail(), ownComment.getEmail());
        assertEquals(currentUser.getId(), currentPost.getUserId());

        // Incorrect data
        currentPost = postService.findById(4);
        ownComment = commentService.createOwnComment("wrongUser", currentPost.getId(),
                "testName2", "testBody2");
        assertNull(ownComment);
    }

    @Test
    public void testEditOwnComment() {
        UserDTO currentUser = userService.getByUserName("Bret");

        // Correct data
        String newName = "newName";
        String newBody = "newBody";
        CommentDTO editedComment = commentService.editOwnComment(currentUser.getUsername(), 2L, newName, newBody);

        assertNotNull(editedComment);
        assertEquals(editedComment.getName(), newName);
        assertEquals(editedComment.getBody(), newBody);

        // Incorrect data
        long commentId = 65;
        assertNull(commentService.editOwnComment(currentUser.getUsername(), commentId, newName, newBody));
        CommentDTO notEditedComment = commentService.getById(commentId);

        assertNotEquals(notEditedComment.getName(), newName);
        assertNotEquals(notEditedComment.getBody(), newBody);
    }

    @Test
    public void testDeleteOwnComment() {
        UserDTO currentUser = userService.getByUserName("Antonette");

        // Correct data
        long commentId = 60;
        assertTrue(commentService.deleteOwnComment(currentUser.getUsername(), commentId));
        assertNull(commentService.getById(commentId));

        // Incorrect data
        commentId = 23;
        assertFalse(commentService.deleteOwnComment(currentUser.getUsername(), commentId));
        assertNotNull(commentService.getById(commentId));
    }
}
