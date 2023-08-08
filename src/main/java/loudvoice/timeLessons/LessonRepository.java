package loudvoice.timeLessons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface LessonRepository extends JpaRepository<timeLessons,Long> {


    @Modifying
    @Query(value = "update timeLessons u set u.isEnabled =:isEnable," + "u.Uid =:Uid where u.id =:id")
    void addDateId(boolean isEnable, String Uid, Long id);

    @Modifying
    @Query(value = "update timeLessons u set u.isEnabled =:isEnable," + "u.Uid =:Uid where u.id =:id")
    void DeleteDateId(boolean isEnable, String Uid, Long id);
}
