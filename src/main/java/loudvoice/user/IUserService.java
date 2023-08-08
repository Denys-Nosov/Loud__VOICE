package loudvoice.user;


import loudvoice.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();

    List<User> getStudents();

    User registerUser(RegistrationRequest registrationRequest);

    User saveAdmin(RegistrationRequest registrationRequest);

    User findByEmail(String email);

    User findByFirstName(String firstName);

    Optional<User> findById(Long id);

    void updateUser(Long id, String firstName, String lastName, String email);

    void deleteUser(Long id);
}
