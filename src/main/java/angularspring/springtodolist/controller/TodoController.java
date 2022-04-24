package angularspring.springtodolist.controller;

import angularspring.springtodolist.dao.ToDoDao;
import angularspring.springtodolist.model.Todo;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/todos")
@RestController
public class TodoController {

    @Autowired
    private ToDoDao dao;

    @GetMapping
    public List<Todo> list() {
        return dao.findAll();
    }

    @PostMapping
    public Todo create(@RequestBody Todo toBeCreated) {
        toBeCreated.setStatus("NEW");
        return dao.save(toBeCreated);
    }

    @GetMapping("/{id}")
    public Todo get(@PathVariable Integer id) {
        return dao.findById(id)
                .orElse(null);
    }

    @PutMapping
    public Todo update(@RequestBody Todo todo) {
        return dao.save(todo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        dao.deleteById(id);
    }

    @GetMapping("/count/{status}")
    public int countByStatus(@PathVariable String status) {
        return dao.countByStatus(status);
    }

    @GetMapping("/search/{status}")
    public List<Todo> searchByStatus(@PathVariable String status) {
        return dao.findByStatus(status);
    }

    @GetMapping("/upcoming/list")
    public List<Todo> topTodos() {
        return dao.findTop3ByStatusInOrderByDueDate(Arrays.asList("NEW", "IN_PROGRESS"));
    }

    @GetMapping("/order/{direction}")
    public List<Todo> orderTodos(@PathVariable String direction) {
        /*if(direction.equals("asc")) {
            return dao.findAllByOrderByDueDateAsc();
        } else {
            return dao.findAllByOrderByDueDateDesc();
        }*/
        Sort sort = direction.equals("asc") ? Sort.by("dueDate").ascending() :
                Sort.by("dueDate").descending();
        return dao.findAllBy(PageRequest.of(0,Integer.MAX_VALUE, sort))
                .getContent();
    }

}
