package loudvoice.user;

import jakarta.transaction.Transactional;

public interface IRoleService {


    @Transactional
    void AddDelStudent(Long id);

    @Transactional
    void DelStudent(Long id);
}
