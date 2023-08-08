package loudvoice.registration;


import lombok.Data;
import loudvoice.user.Role;

import java.util.List;

@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles;
}
