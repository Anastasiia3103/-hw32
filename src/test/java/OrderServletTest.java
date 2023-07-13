import hw32.Order;
import hw32.OrderServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class OrderServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private OrderServlet orderServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoGetWithOrderId() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/orders/1");

        OrderServlet orderServlet = mock(OrderServlet.class);

        Order mockOrder = null;
        try {
            Date orderDate = new SimpleDateFormat("yyyy-MM-dd").parse("2023-07-13");
            mockOrder = new Order(1, orderDate, 10.99, new ArrayList<>());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        doReturn(mockOrder).when(orderServlet).getOrderById(1);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doGet(request, response);

        writer.flush();

        String responseContent = stringWriter.toString();

        assertEquals("Expected response", responseContent);
    }

    @Test
    void testDoGetWithoutOrderId() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/orders");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doGet(request, response);

        writer.flush();
        String responseContent = stringWriter.toString();

        assertNotNull(responseContent);
    }

    @Test
    void testDoPost() throws IOException, ServletException {
        when(request.getParameter("date")).thenReturn("2023-07-13");
        when(request.getParameter("cost")).thenReturn("10.99");
        when(request.getParameterValues("products")).thenReturn(new String[]{"Product A", "Product B"});

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doPost(request, response);

        writer.flush();
        String responseContent = stringWriter.toString();

        // Update the expected response content based on your servlet's logic
        String expectedResponse = "Expected response";

        assertEquals(expectedResponse, responseContent);
    }

    @Test
    void testDoPut() throws IOException, ServletException {

        when(request.getRequestURI()).thenReturn("/orders/1");
        when(request.getParameter("date")).thenReturn("2023-07-14");
        when(request.getParameter("cost")).thenReturn("15.99");
        when(request.getParameterValues("products")).thenReturn(new String[]{"Product X", "Product Y"});

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doPut(request, response);

        writer.flush();
        String responseContent = stringWriter.toString();

        assertEquals("Expected response", responseContent);
    }


    @Test
    void testDoDelete() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/orders/1");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doDelete(request, response);

        writer.flush();
        String responseContent = stringWriter.toString();

        String expectedResponse = "Expected response";

        assertEquals(expectedResponse, responseContent);
    }
}

