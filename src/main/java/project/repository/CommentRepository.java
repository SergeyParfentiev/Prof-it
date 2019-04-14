package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.dto.CommentDTO;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentDTO, Long> {

    @Query(value = "SELECT * FROM comment c JOIN post p ON p.id = c.post_id JOIN user u ON u.id = p.user_id " +
            "WHERE u.id = :id", nativeQuery = true)
    List<CommentDTO> getListByUserId(@Param("id") long userId);

    @Query(value = "SELECT SUM(ROUND((LENGTH(c.body)- LENGTH( REPLACE ( c.body, :word, ''))) / LENGTH(:word))) AS count " +
            "FROM comment c", nativeQuery = true)
    Long getWordCountByBodyWord(@Param("word") String bodyWord);
}
