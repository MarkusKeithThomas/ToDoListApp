package thuong.todolist.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity(name = "list")
public class ListToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String nameToDo;
    @Column(name = "description")
    private String description;
    @Column(name = "image")
    private String imageName;
    @Column(name = "create_date")
    private LocalDate dateCreate;

    @ManyToOne
    @JoinColumn(name ="id_user")
    private UserEntity userEntity;
}
