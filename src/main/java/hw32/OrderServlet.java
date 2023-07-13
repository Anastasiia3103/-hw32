package hw32;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {
    private List<Order> orders = new ArrayList<>();
    private int nextId = 1;
    private Date orderDate;

    public void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String orderIdString = requestUri.substring(requestUri.lastIndexOf('/') + 1);

        if (orderIdString.isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write(orders.toString());
        } else {
            int orderId = Integer.parseInt(orderIdString);
            Order order = getOrderById(orderId);
            if (order != null) {
                response.setContentType("application/json");
                response.getWriter().write(order.toString());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = nextId++;
        String date = request.getParameter("date");
        double cost = Double.parseDouble(request.getParameter("cost"));
        String[] productNames = request.getParameterValues("product");
        List<Product> products = new ArrayList<>();
        for (String productName : productNames) {
            products.add(new Product(productName));
        }
        Order order = new Order(orderId, orderDate, cost, products);
        orders.add(order);

        response.setContentType("application/json");
        response.getWriter().write(order.toString());
    }

    public void doPut (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String orderIdString = requestUri.substring(requestUri.lastIndexOf('/') + 1);

        int orderId = Integer.parseInt(orderIdString);
        Order order = getOrderById(orderId);
        if (order != null) {

            String date = request.getParameter("date");
            double cost = Double.parseDouble(request.getParameter("cost"));
            String[] productNames = request.getParameterValues("product");
            List<Product> products = new ArrayList<>();
            for (String productName : productNames) {
                products.add(new Product(productName));
            }
            order.setDate(orderDate);
            order.setCost(cost);
            order.setProducts(products);

            response.setContentType("application/json");
            response.getWriter().write(order.toString());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
        }
    }

    public void doDelete (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String orderIdString = requestUri.substring(requestUri.lastIndexOf('/') + 1);

        int orderId = Integer.parseInt(orderIdString);
        boolean removed = orders.removeIf(order -> order.getId() == orderId);
        if (removed) {
            response.getWriter().write("Order with ID " + orderId + " deleted successfully");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
        }
    }

    public Order getOrderById (int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        return null;
    }
}
