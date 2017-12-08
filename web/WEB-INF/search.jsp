<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <title>Search</title>
  </head>
  <body>
  <table>
    <thead>
    <tr>
      <td>学号</td>
      <td>姓名</td>
      <td>性别</td>
      <td>年龄</td>
      <td>院系</td>
    </tr>
    </thead>
  <jsp:useBean id="list" scope="request" type="java.util.ArrayList<com.Student>"/>
  <c:forEach items="${list}" var="item">
    <tr>
      <td>${item.id}</td>
      <td>${item.name}</td>
      <td>${item.gender}</td>
      <td>${item.age}</td>
      <td>${item.department}</td>
    </tr>
  </c:forEach>
    </table>
  </body>
</html>
