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
public class PostServiceTest {

    private static Logger logger = LogManager.getLogger(PostServiceTest.class);

    @Autowired
    private FillDatabase fillDatabase;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

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
    public void testCountByTitle() {
        // Correct data
        assertEquals(2, postService.getListByTitle("qui est esse").size());
        assertEquals(4, postService.getListByTitle("dolorem eum magni eos aperiam quia").size());

        // Incorrect data
        assertNotEquals(3, postService.getListByTitle("eveniet quod temporibus").size());
        assertNotEquals(6, postService.getListByTitle("dolorem eum magni eos aperiam quia").size());
    }

    @Test
    public void testCountContainsTitleWord() {
        // Correct data
        assertEquals(5, postService.getListLikeTitle("myWord").size());

        // Incorrect data
        assertNotEquals(7, postService.getListLikeTitle("eum et est occaecati").size());
    }

    @Test
    public void testDeletePost() {
        // Correct data
        assertNotNull(commentService.getById(81));
        assertNotNull(commentService.getById(82));
        assertNotNull(commentService.getById(83));
        assertNotNull(commentService.getById(84));
        assertNotNull(commentService.getById(85));

        assertTrue(postService.deleteById(17L));

        assertNull(commentService.getById(81));
        assertNull(commentService.getById(82));
        assertNull(commentService.getById(83));
        assertNull(commentService.getById(84));
        assertNull(commentService.getById(85));

        // Incorrect data
        assertNotNull(commentService.getById(31));
        assertNotNull(commentService.getById(32));
        assertNotNull(commentService.getById(33));
        assertNotNull(commentService.getById(34));
        assertNotNull(commentService.getById(35));

        assertTrue(postService.deleteById(4L));

        assertNotNull(commentService.getById(31));
        assertNotNull(commentService.getById(32));
        assertNotNull(commentService.getById(33));
        assertNotNull(commentService.getById(34));
        assertNotNull(commentService.getById(35));
    }

    @Test
    public void testDeleteOwnPost() {
        UserDTO currentUser = userService.getByUserName("Bret");
        PostDTO currentPost;

        // Correct data
        assertNotNull(commentService.getById(26));
        assertNotNull(commentService.getById(27));
        assertNotNull(commentService.getById(28));
        assertNotNull(commentService.getById(29));
        assertNotNull(commentService.getById(30));

        currentPost = postService.findById(6);
        assertTrue(postService.deleteOwnById(currentUser.getUsername(), currentPost.getId()));

        assertNull(postService.findById(currentPost.getId()));
        assertNull(commentService.getById(26));
        assertNull(commentService.getById(27));
        assertNull(commentService.getById(28));
        assertNull(commentService.getById(29));
        assertNull(commentService.getById(30));

        // Incorrect data
        assertNotNull(commentService.getById(56));
        assertNotNull(commentService.getById(57));
        assertNotNull(commentService.getById(58));
        assertNotNull(commentService.getById(59));
        assertNotNull(commentService.getById(60));

        currentPost = postService.findById(12);
        assertFalse(postService.deleteOwnById(currentUser.getUsername(), currentPost.getId()));

        assertNotNull(postService.findById(currentPost.getId()));

        assertNotNull(commentService.getById(56));
        assertNotNull(commentService.getById(57));
        assertNotNull(commentService.getById(58));
        assertNotNull(commentService.getById(59));
        assertNotNull(commentService.getById(60));
    }
}
