package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesSearchServlet
 */
@WebServlet("/employees/search")
public class EmployeesSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        List<Employee> employees = em.createNamedQuery("searchEmployeesCode", Employee.class)
                                     .setFirstResult(15 * (page - 1))
                                     .setMaxResults(15)
                                     .setParameter("code", "%"+code+"%")
                                     .setParameter("name", "%"+name+"%")
                                     .getResultList();


        long employees_name_count = (long)em.createNamedQuery("getEmployeesNameCount", Long.class)
                                        .setParameter("code", "%"+code+"%")
                                        .setParameter("name", "%"+name+"%")
                                        .getSingleResult();

        em.close();

        request.setAttribute("employees", employees);
        request.setAttribute("employees_name_count", employees_name_count);
        request.setAttribute("page", page);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/Search.jsp");
        rd.forward(request, response);
    }
}
