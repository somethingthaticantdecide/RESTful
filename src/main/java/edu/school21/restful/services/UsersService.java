package edu.school21.restful.services;

import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.models.roles.Role;
import edu.school21.restful.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User addNewUser(UserDto userDto) {
        User user = new User();
        setFields(user, userDto);
        userRepository.save(user);
        return user;
    }

    public void updateUser(User user, UserDto userDto) {
        setFields(user, userDto);
        userRepository.save(user);
    }

    private void setFields(User user, UserDto userDto) {
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setRoles(Role.valueOf(userDto.getRoles()));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
