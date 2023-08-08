package loudvoice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepo extends JpaRepository<Role,Long> {

    @Modifying
    @Query(value = "update Role u set u.name =:name where u.id =:id")
    void AddOrDelStudent(String name, Long id);

    @Modifying
    @Query(value = "update User u set u.isStudent =:isStudent where u.id =:id")
    void AddDelStudent(boolean isStudent, Long id);
}
