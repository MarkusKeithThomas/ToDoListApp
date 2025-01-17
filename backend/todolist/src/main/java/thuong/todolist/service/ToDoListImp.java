package thuong.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thuong.todolist.baserespone.BaseRespone;
import thuong.todolist.dto.ToDoListDTO;
import thuong.todolist.entity.ListToDo;
import thuong.todolist.entity.UserEntity;
import thuong.todolist.exception.DeleteException;
import thuong.todolist.exception.InsertException;
import thuong.todolist.repository.ToDoListRepository;
import thuong.todolist.request.ToDoListRequest;
import thuong.todolist.utils.JwtHelper;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoListImp implements ToDoListService{
    @Autowired
    private ToDoListRepository toDoListRepository;
    @Autowired
    private FileServiceImp fileServiceImp;
    @Autowired
    private JwtHelper jwtHelper;


    @Override
    public BaseRespone getAllToDoListByUserId(String authToken) {
        String email = jwtHelper.decryptEmail(authToken);
        BaseRespone baseRespone = new BaseRespone();
        List<ToDoListDTO> list = toDoListRepository.findByUserEntity_Email(email).stream().map(data ->{
            ToDoListDTO toDoListDTO = new ToDoListDTO();
            toDoListDTO.setId(data.getId());
            toDoListDTO.setNameToDoList(data.getNameToDo());
            toDoListDTO.setDescription(data.getDescription());
            toDoListDTO.setImage("http://localhost:8080/image/"+(data.getImageName()));
            toDoListDTO.setDateCreate(data.getDateCreate());
            return toDoListDTO ;
        }).toList();
        baseRespone.setCode(200);
        baseRespone.setMessage("Success");
        baseRespone.setData(list);
        return baseRespone;
    }

    @Override
    public BaseRespone insertToDoList(ToDoListRequest toDoListRequest, String authorizationHeader) {
        BaseRespone baseRespone = new BaseRespone();
        try{
            fileServiceImp.uploadFile(toDoListRequest.getFile());
            ListToDo listToDo = new ListToDo();
            listToDo.setImageName(toDoListRequest.getFile().getOriginalFilename());
            listToDo.setNameToDo(toDoListRequest.getNameToDoList());
            listToDo.setDescription(toDoListRequest.getDescription());
            listToDo.setDateCreate(toDoListRequest.getDateCreate());

            UserEntity userEntity = new UserEntity();
            userEntity.setId(jwtHelper.decryptUserId(authorizationHeader));

            listToDo.setUserEntity(userEntity);

            toDoListRepository.save(listToDo);
            baseRespone.setCode(200);
            baseRespone.setMessage("Success");
            baseRespone.setData(true);
        } catch (Exception e){
            baseRespone.setCode(501);
            baseRespone.setMessage("Failed");
            baseRespone.setData(e);
            throw new InsertException("InsertToDoList Error "+e.getMessage());
        }
        return baseRespone;

    }

    @Override
    public BaseRespone deleteToDoList(int idToDoList) {
        BaseRespone baseRespone = new BaseRespone();
        try{
            Optional<ListToDo> listToDo = toDoListRepository.findById(idToDoList);
            if (listToDo.isPresent()){
                toDoListRepository.deleteById(idToDoList);
                baseRespone.setCode(200);
                baseRespone.setMessage("Xóa thành công.");
                baseRespone.setData(true);
            } else {
                baseRespone.setCode(404);
                baseRespone.setMessage("Không tìm thấy công việc với ID: " + idToDoList);
                baseRespone.setData(false);
            }
        } catch (Exception e){
            throw new DeleteException("DeleteToDoList Error "+e.getMessage());
        }
        return baseRespone;
    }
}
