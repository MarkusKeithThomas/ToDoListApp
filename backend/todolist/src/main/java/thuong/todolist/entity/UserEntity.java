package thuong.todolist.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "pass_word")
    private String password;
    @OneToMany(mappedBy = "userEntity")
    private List<ListToDo> list;

}
