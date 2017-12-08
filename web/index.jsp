<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>添加</title>
  </head>
  <body>
  <form action="index.do" method="post">
    <label for="student_id">学号</label>
    <input type="number" id="student_id" name="student_id">
    <br/>
    <label for="student_name">姓名</label>
    <input type="text" id="student_name" name="student_name">
    <br/>
    <label for="student_gender">性别</label>
    <select name="student_gender" id="student_gender">
      <option value="1">男</option>
      <option value="2">女</option>
    </select>
    <br/>
    <label for="student_age">年龄</label>
    <input type="number" id="student_age" name="student_age">
    <br/>
    <label for="student_department">院系</label>
    <input type="text" id="student_department" name="student_department">
    <br/>
    <button type="submit">添加</button>
    <button type="submit" formaction="delete.do" formmethod="post">根据学号删除</button>
    <button type="submit" formaction="search.do" formmethod="post">搜索院系信息</button>
  </form>
  </body>
</html>
