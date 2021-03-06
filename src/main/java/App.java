import static spark.Spark.staticFileLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import static spark.Spark.*;


/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String layout = "templates/layout.vtl";

        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
        }

        port(port);

        get("/addTodoForm", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("categories", Category.all());
            model.put("template", "templates/addTodo.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

            //post: process new post form
        post("/addform", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();

                Integer i = Integer.parseInt(request.queryParams("categoryId"));
                Category category = Category.find(i);
                String description = request.queryParams("todoItem");
                Task newTask = new Task(description, category.getId());
                newTask.save();
                model.put("category", category);
                model.put("template", "templates/success.vtl");

            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //add a new category
        post("/categories", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String name = request.queryParams("name");
            Category newCategory = new Category(name);
            newCategory.save();
            model.put("template", "templates/success.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //get category form
        get("/categories/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("template", "templates/category-form.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //get all category
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("categories", Category.all());
            model.put("template", "templates/index.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //get category by id
        get("/categories/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Category category = Category.find(Integer.parseInt(request.params(":id")));
            model.put("category", category);
            model.put("template", "templates/categoryTodo.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //get the updated task page
        get("/categories/:category_id/tasks/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Category category = Category.find(Integer.parseInt(request.params(":category_id")));
            Task task = Task.find(Integer.parseInt(request.params(":id")));
            model.put("category", category);
            model.put("task", task);
            model.put("template", "templates/task.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //update task by id
        post("/categories/:category_id/tasks/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Task task = Task.find(Integer.parseInt(request.params("id")));
            String description = request.queryParams("description");
            Category category = Category.find(task.getCategoryId());
            task.update(description);
            String url = String.format("/categories/%d/tasks/%d", category.getId(), task.getId());
            response.redirect(url);
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //delete a task
        post("/categories/:category_id/tasks/:id/delete", (request, response) -> {
            HashMap<String, Object> model = new HashMap<String, Object>();
            Task task = Task.find(Integer.parseInt(request.params("id")));
            Category category = Category.find(task.getCategoryId());
            task.delete();
            model.put("category", category);
            model.put("template", "templates/category.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //get the updated category page
        get("/categoriedit/:category_id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Category category = Category.find(Integer.parseInt(request.params(":category_id")));
            model.put("category", category);
            model.put("template", "templates/categoryEdit.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //update category by id
        post("/categories/:category_id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Category category = Category.find(Integer.parseInt(request.params("category_id")));
            String name = request.queryParams("name");
            category.update(name);
            String url = String.format("/categories/%d", category.getId());
            response.redirect(url);
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        //delete a category
        post("/categories/:category_id/delete", (request, response) -> {
            HashMap<String, Object> model = new HashMap<String, Object>();
            Category category = Category.find(Integer.parseInt(request.params("category_id")));
            category.delete();
            model.put("category", category);
            model.put("template", "templates/index.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

    }
}
