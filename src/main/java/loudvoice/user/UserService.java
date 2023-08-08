package loudvoice.user;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loudvoice.registration.RegistrationRequest;
import loudvoice.registration.token.VerificationTokenService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenService verificationTokenService;



    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<User> user = new ArrayList<>();
        for (User r :users){
            if (!r.isStudent()){
                user.add(r);
            }
        }
        return user;
    }


    @Override
    public List<User> getStudents(){
        String Role = "STUDENT";
        List<User> users = userRepository.findAll();
        List<User> students = new ArrayList<>();

        for (User t : users){
            if (t.isStudent()){
                students.add(t);
            }
        }
        return students;
    }

    @Override
    public User registerUser(RegistrationRequest regisration) {
        var userrr = new User(regisration.getFirstName(),regisration.getLastName(),
                regisration.getEmail(), passwordEncoder.encode(regisration.getPassword()),
                Arrays.asList(new Role("USER")));
        userRepository.save(userrr);
        return userrr;
    }

    @Override
    public User saveAdmin(RegistrationRequest registration){
        User admin = new User("admin","admin",
                "example@gmail.com" ,passwordEncoder.encode("ADMIN2"),
                Arrays.asList(new Role("ADMIN")));
        userRepository.save(admin);
        return admin;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
    @Override
    public User findByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateUser(Long id, String firstName, String lastName, String email) {
        userRepository.update(firstName,lastName,email,id);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<User> theUser = userRepository.findById(id);
        if (theUser.get().getId() != 1){
            theUser.ifPresent(user -> verificationTokenService.deleteUserToken(user.getId()));
            userRepository.deleteById(id);
        }
    }
}
