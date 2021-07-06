package mx.ucol.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mx.ucol.models.Todo;
import mx.ucol.helpers.DBConnection;
import mx.ucol.helpers.JSON;

public class TodosHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                getHandler(exchange);
                break;
            case "POST":
                postHandler(exchange);
                break;
            case "PUT":
                putHandler(exchange);
                break;
            case "DELETE":
                deleteHandler(exchange);
                break;
            default:
                notSupportedHandler(exchange);
                break;
        }
    }

    private void getHandler(HttpExchange exchange) throws IOException {

        /*
         * Supported endpoints for the GET handler: GET /todos Get all the ToDo entries
         * rom the DB instance
         *
         * GET /todos/:id Get the ToDo entry with the :id from the DB instance
         *
         * To get the connection to the DB use the following Connection connection =
         * DBConnection.getInstance();
         *
         * Then you can use the connection variable to create statements.
         */

        OutputStream output = exchange.getResponseBody();

        Todo todo = new Todo();
        todo.setId(1);
        todo.setTitle("Title Example");
        todo.setCompleted(false);

        String json = JSON.objectToJson(todo);
        byte[] response = json.getBytes();

        exchange.sendResponseHeaders(200, response.length);
        output.write(response);
        output.close();
    }

    private void postHandler(HttpExchange exchange) throws IOException {

        /*
         * Supported endpoints for the POST handler: POST /todos Creates a new ToDo
         * entry
         *
         * You can use getBodyContent to read a JSON from the HTTP request: String
         * jsonBody = getBodyContent(exchange.getRequestBody())
         *
         * Try to convert the jsonBody variable to a Todo object to ensure the JSON is
         * well-formed.
         */

        OutputStream output = exchange.getResponseBody();

        //Get the request body
        InputStream input = exchange.getRequestBody();
        //Get the JSON body of the resonse
        String bodyContent = getBodyContent(input);

//        System.out.println(bodyContent);
        Todo bodyContentTodo  = JSON.jsonToObject(bodyContent);

        String bodyContentTodoTitle = bodyContentTodo.getTitle();
        boolean bodyContentTodoCompleted = bodyContentTodo.getCompleted();

        System.out.println(bodyContentTodo.getTitle());



        // Create the ToDos table if not exists
        Connection connection = DBConnection.getInstance();
        String sql = "INSERT INTO todos (title, completed) VALUES(\""+ bodyContentTodoTitle + "\",\" "+ bodyContentTodoCompleted +"\")";

        byte[] response;

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            response = "TO-DO ADDED SUCCESFULLY".getBytes();
        } catch (SQLException e) {
            response = "ERROR WHEN ADDING TO-DO".getBytes();
            System.err.println("Error on TodosHandler.java executeUpdate: " + e.getMessage());
        }


//        Todo todo = new Todo();
//        todo.setId(1);
//        todo.setTitle("Title Example 2");
//        todo.setCompleted(false);
//


//        String json = JSON.objectToJson(todo);
//        byte[] response = json.getBytes();


        exchange.sendResponseHeaders(200,response.length);
        output.write(response);
        output.close();
    }

    private void putHandler(HttpExchange exchange) throws IOException {

        /*
         * Supported endpoints for the PUT handler POST /todos/:id Update the details of
         * ToDo netry with :id if exists
         */

        OutputStream output = exchange.getResponseBody();
        byte[] response = "PUT Handler".getBytes();

        exchange.sendResponseHeaders(200, response.length);
        output.write(response);
        output.close();
    }

    private void deleteHandler(HttpExchange exchange) throws IOException {

        /*
         * Supported endpoints for the DELETE handler DELETE /todos/:id Remove ToDo
         * entry with :id if exists
         */

        OutputStream output = exchange.getResponseBody();
        byte[] response = "DELETE Handler".getBytes();

        exchange.sendResponseHeaders(200, response.length);
        output.write(response);
        output.close();
    }

    private void notSupportedHandler(HttpExchange exchange) throws IOException {
        OutputStream output = exchange.getResponseBody();
        byte[] response = "Not supported".getBytes();

        exchange.sendResponseHeaders(200, response.length);
        output.write(response);
        output.close();
    }

    private String getBodyContent(InputStream input) throws UnsupportedEncodingException, IOException {
        InputStreamReader streamReader = new InputStreamReader(input, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(streamReader);

        int buffer;
        StringBuilder builder = new StringBuilder();

        while ((buffer = bufferedReader.read()) != -1) {
            builder.append((char) buffer);
        }

        bufferedReader.close();
        streamReader.close();

        return builder.toString();
    }
}
