<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="borrow" scope="session" class="javabean.JDBCBean"></jsp:useBean>
	<%
		String user = request.getParameter("userid");
		String book = request.getParameter("bookid");
		String date1 = request.getParameter("date1");
		String date2 = request.getParameter("date2");
		String sql1 = "select * from borrow_card where ID =" + user;
		
		ResultSet rs1 = borrow.executeQuery(sql1);
		if(session.getAttribute("manager")!=null){
			if (rs1.next()) {
				String sql2 = "select * from books where ID =" + book;
				ResultSet rs2 = borrow.executeQuery(sql2);
				
				if(rs2.next()){
					//out.println(psw1 + " " + psw2);
					//String id = session.getAttribute("manager").toString();
					
					int status=Integer.parseInt(rs2.getString("STATUS"));
					
					
					if(status==1){
						String sql = "insert borrow_books(CARD_ID,BOOK_ID,BORROW_DATE,END_DATE)values('" + user + "','" + book
								+ "','" + date1 + "','" + date2 + "');";
						try {
							int i = borrow.executeUpdate(sql);
							if (i == 1) {
								%>
								<script>
									alert('借阅成功！');
									window.location.href = "02borrow.jsp";
								</script>
							
								<%
									String sql3="update books set STATUS=0 where ID="+book;
									borrow.executeUpdate(sql3);
								
							} else {
								%>
								<script>
									alert('借阅未成功！');
									window.location.href = "02borrow.jsp";
								</script>
								<%
									}
										} catch (Exception e) {
								%>
								<script>
									alert('借阅未成功！');
									window.location.href = "02borrow.jsp";
								</script>
								<%
									}
					}else{
						%>
						<script>
							alert('该图书已借出！');
							window.location.href = "02borrow.jsp";
						</script>
						<%
					}
			}else{
				%>
				<script>
					alert('该图书不存在！');
					window.location.href = "02borrow.jsp";
				</script>
				<%
			}
				} else {
				%>
				<script>
					alert('用户不存在！');
					window.location.href = "02borrow.jsp";
				</script>
				<%
			}
		}else{
			%>
			<script>
				alert('请先登录！');
				window.parent.location.href = "../loginManager.html";
			</script>
			<%
		}
	%>


</body>
</html>