package servlet.reader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javabean.Base;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class Book
 */
@WebServlet("/reader/book")
public class Book extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json; charset=utf8");
		// 接收参数
		String limit = req.getParameter("limit");
		String page = req.getParameter("page");
		String condition = (String) req.getParameter("condition");
		String conditionValue = (String) req.getParameter("conditionValue");
		String where = ""; // 无限制条件
		if (page == null) {
			page = "1";
		}
		if (limit == null) {
			limit = "10";
		}
		// 准备查询
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement countPstmt = null;
		ResultSet resultSet = null;
		ResultSet countSet = null;
		String sql = "";
		String countSql = "";
		// 准备返回参数
		int code = 1;
		String msg = "error";
		int count = 0;

		JSONObject jsonData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonResult = new JSONObject();
		// 进行查询
		try {
			connection = (Connection) Base.getConnection();
			sql = "select * from books ";
			if (condition != null && conditionValue != null && !condition.equals("") && !conditionValue.equals("")) {
				where = " where " + condition + " like '%" + conditionValue + "%' ";
				sql += where;
			}
			sql += " limit ?,?";// 1 10 (1-1)*10
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, (Integer.parseInt(page) - 1) * Integer.parseInt(limit));
			pstmt.setInt(2, Integer.parseInt(limit));
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				jsonData.put("id", resultSet.getString("id"));
				jsonData.put("name", resultSet.getString("name"));
				jsonData.put("author", resultSet.getString("author"));
				jsonData.put("library_id", resultSet.getString("library_id"));
				jsonData.put("sort_id", resultSet.getString("sort_id"));
				jsonData.put("position", resultSet.getString("position"));
				jsonData.put("status", resultSet.getString("status"));
				jsonData.put("description", resultSet.getString("description"));
				jsonArray.add(jsonData);
			}
			countSql = "select count(*) as count from books ";
			countSql += where;
			countPstmt = connection.prepareStatement(countSql);
			countSet = countPstmt.executeQuery();
			if (countSet.next()) {
				count = countSet.getInt("count");
			}
			if (!jsonArray.isEmpty()) {
				code = 0;
				msg = "查询成功";
			}

		} catch (ClassNotFoundException e) {
			msg = "class没找到";
		} catch (SQLException e) {
			msg = "sql错误";
		} finally {
			try {
				Base.closeResource(null, pstmt, resultSet);
				Base.closeResource(connection, countPstmt, countSet);
			} catch (SQLException e) {
				msg = "关闭资源失败";
			}

		}
		// 返回数据
		jsonResult.put("code", code);
		jsonResult.put("count", count);
		jsonResult.put("msg", msg);
		jsonResult.put("data", jsonArray.toArray());
		PrintWriter out = resp.getWriter();
		out.print(jsonResult.toString());
	}

}
