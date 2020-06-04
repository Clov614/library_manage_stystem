package servlet.reader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javabean.Reader;
import net.sf.json.JSONObject;

@WebServlet("/readerLogin")
public class ReaderLogin extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 设置头文件
		response.setContentType("application/json; charset=utf8");
		PrintWriter out = response.getWriter();
		// 获取账号密码
		String user = request.getParameter("user");
		String psw = request.getParameter("psw");
		// 设置响应map
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		Reader reader = new Reader();
		String result = null;
		try {
			result = reader.login(user, psw);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if (result.equals("1")) {
			HttpSession session = request.getSession();
			session.setAttribute("reader", user);
			session.setAttribute("reader_first", "1"); // 登录
			hashMap.put("code", 0);
			hashMap.put("msg", "登录成功");
			hashMap.put("url", request.getContextPath() + "/reader/01main.jsp");
		} else {
			hashMap.put("code", 1);
			hashMap.put("msg", result);
		}
		// response.sendRedirect(request.getContextPath() +"/test.jsp");
		JSONObject json = JSONObject.fromObject(hashMap);
		out.write(json.toString());

	}

}
