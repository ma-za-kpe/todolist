import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class TaskTest {

    @Before
    public void setUp() {
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
    }

    @After
    public void tearDown() {
        try(Connection con = DB.sql2o.open()) {
            String deleteTasksQuery = "DELETE FROM tasks *;";
            String deleteCategoriesQuery = "DELETE FROM categories *;";
            con.createQuery(deleteTasksQuery).executeUpdate();
            con.createQuery(deleteCategoriesQuery).executeUpdate();
        }
    }

    @Test
    public void equals_returnsTrueIfDescriptionsAretheSame() {
        Task firstTask = new Task("Mow the lawn",1);
        Task secondTask = new Task("Mow the lawn", 1);
        assertTrue(firstTask.equals(secondTask));
    }

    @Test
    public void save_returnsTrueIfDescriptionsAretheSame() {
        Task myTask = new Task("Mow the lawn", 1);
        myTask.save();
        assertTrue(Task.all().get(0).equals(myTask));
    }

    @Test
    public void all_returnsAllInstancesOfTask_true() {
        Task firstTask = new Task("Mow the lawn",1);
        firstTask.save();
        Task secondTask = new Task("Buy groceries", 2);
        secondTask.save();
        assertEquals(true, Task.all().get(0).equals(firstTask));
        assertEquals(true, Task.all().get(1).equals(secondTask));
    }

    @Test
    public void save_assignsIdToObject() {
        Task myTask = new Task("Mow the lawn", 1);
        myTask.save();
        Task savedTask = Task.all().get(0);
        assertEquals(myTask.getId(), savedTask.getId());
    }

    @Test
    public void getId_tasksInstantiateWithAnID() {
        Task myTask = new Task("Mow the lawn", 1);
        myTask.save();
        assertTrue(myTask.getId() > 0);
    }

    @Test
    public void find_returnsTaskWithSameId_secondTask() {
        Task firstTask = new Task("Mow the lawn", 1);
        firstTask.save();
        Task secondTask = new Task("Buy groceries", 2);
        secondTask.save();
        assertEquals(Task.find(secondTask.getId()), secondTask);
    }

    //update data
    @Test
    public void update_updatesTaskDescription_true() {
        Task myTask = new Task("Mow the lawn", 1);
        myTask.save();
        myTask.update("Take a nap");
        assertEquals("Take a nap", Task.find(myTask.getId()).getDescription());
    }

    //delete task
    @Test
    public void delete_deletesTask_true() {
        Task myTask = new Task("Mow the lawn", 1);
        myTask.save();
        int myTaskId = myTask.getId();
        myTask.delete();
        assertEquals(null, Task.find(myTaskId));
    }

}