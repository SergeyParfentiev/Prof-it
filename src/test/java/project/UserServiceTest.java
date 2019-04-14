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
import project.service.UserService;
import project.startup.FillDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@SpringApplicationConfiguration(AppConfig.class)
@WebAppConfiguration
public class UserServiceTest {

    private static Logger logger = LogManager.getLogger(UserServiceTest.class);

    @Autowired
    private FillDatabase fillDatabase;

    @Autowired
    private UserService userService;

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
    public void testCountByCommentId() {
        // Correct data
        assertEquals(5, userService.getListByCommentId(1).size());

        // Incorrect data
        assertNotEquals(3, userService.getListByCommentId(3).size());
    }
}
