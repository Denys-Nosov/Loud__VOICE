package loudvoice.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final RoleRepo roleRepository;
    private final UserRepository userRepository;


    @Transactional
    @Override
    public void AddDelStudent(Long id){
        String RoleName = "STUDENT";
        Optional<Role> role = roleRepository.findById(id);
        role.ifPresent(role1 -> roleRepository.AddOrDelStudent(RoleName,id));
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(user1 -> roleRepository.AddDelStudent(true,id));
    }

    @Transactional
    @Override
    public void DelStudent(Long id){
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(user1 -> roleRepository.AddDelStudent(false,id));
        String RoleName = "USER";
        Optional<Role> role = roleRepository.findById(id);
        role.ifPresent(role1 -> roleRepository.AddOrDelStudent(RoleName,id));
    }
}
