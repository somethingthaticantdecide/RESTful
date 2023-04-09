package edu.school21.restful.services;

import edu.school21.restful.exceptions.NotFoundException;
import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.models.roles.Role;
import edu.school21.restful.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService implements UserDetailsService {
    private final UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public User addNewUser(UserDto userDto) {
        User user = userRepository.findUserByFirstname(userDto.getFirstname());
        if (user == null) {
            user = new User(userDto);
            userRepository.save(user);
        }
        return user;
    }

    public void updateUser(User user, UserDto userDto) {
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setRoles(Role.valueOf(userDto.getRoles()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByFirstname(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public Page<User> getStudentByCourse(Long id, Pageable pageable) {
        List<User> students = userRepository.findStudentsByCourse(id, pageable);
        return new PageImpl<>(students, pageable, students.size());
    }

    public Page<User> getTeachersByCourse(Long id, Pageable pageable) {
        List<User> teachers = userRepository.findTeachersByCourse(id, pageable);
        return new PageImpl<>(teachers, pageable, teachers.size());
    }
}
