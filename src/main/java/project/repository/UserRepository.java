package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.dto.UserDTO;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, Long> {

    @Query(value = "SELECT * FROM user u JOIN post p ON u.id = p.user_id JOIN comment c ON p.id = c.post_id " +
            "WHERE p.id = :id", nativeQuery = true)
    List<UserDTO> getListByCommentId(@Param("id") long id);

    UserDTO findByUsername(String username);
}
