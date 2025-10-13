package clinica.medtech.service;

import clinica.medtech.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class UserService {

    private static final List<User> USER_LIST = new ArrayList<>();

    public void register(User user) {
        user.setStatus("online");
        USER_LIST.add(user);
    }

    public User login(User user) {
        var userIndex = IntStream.range(0, USER_LIST.size())
                .filter(i -> USER_LIST.get(i).getEmail().equals(user.getEmail()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Usuario incorrecto"));

        var connUser = USER_LIST.get(userIndex);

        if (!connUser.getPassword().equals(user.getPassword())) throw new RuntimeException("Contraseña incorrecta");

        connUser.setStatus("online");

        return connUser;
    }

    public void logout(String email) {
        var userIndex = IntStream.range(0, USER_LIST.size())
                .filter(i -> USER_LIST.get(i).getEmail().equals(email))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Usuario incorrecto"));

        USER_LIST.get(userIndex).setStatus("offline");

    }

    public List<User> findAll() {
        return USER_LIST;
    }
}
