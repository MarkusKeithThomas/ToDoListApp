package thuong.todolist.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thuong.todolist.entity.ListToDo;

import java.util.List;

@Repository
public interface ToDoListRepository extends JpaRepository<ListToDo,Integer> {
    List<ListToDo> findByUserEntity_Email(String email);
}
