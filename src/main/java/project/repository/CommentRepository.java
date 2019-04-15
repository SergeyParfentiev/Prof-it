package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.dto.CommentDTO;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentDTO, Long> {

    @Query(value = "SELECT * FROM comment c WHERE c.email = :email", nativeQuery = true)
    List<CommentDTO> getListByEmail(@Param("email") String email);

    @Query(value = "SELECT SUM(ROUND((LENGTH(c.body)- LENGTH( REPLACE ( c.body, :word, ''))) / LENGTH(:word))) AS count " +
            "FROM comment c", nativeQuery = true)
    Long getWordCountByBodyWord(@Param("word") String bodyWord);

    @Query("SELECT c FROM CommentDTO c WHERE c.id = :id AND c.email = :email")
    CommentDTO getByIdAndEmail(@Param("id") long id, @Param("email") String email);
}
