<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Title</title>
  <link rel="stylesheet" href="css/bootstrap.css">
  <script src="js/jquery-3.2.1.js"></script>
  <script src="js/bootstrap.js"></script>
</head>
<body>
<!-- TWEET WRAPPER START -->
<div class="twt-wrapper">
  <div class="panel panel-info">
    <div class="panel-heading">
      Twitter Feed Example
    </div>
    <div class="panel-body">
      <form action="list.do" id="make_note" method="post">
        <textarea class="form-control custom-control" placeholder="Enter here for tweet..." rows="3" name="textarea"></textarea>
        <button type="submit" id="send_button" class="btn btn-primary btn-sm pull-right">Send</button>
        <%--<span class="input-group-addon btn btn-primary">Send</span>--%>
      </form>
      <div class="clearfix"></div>
      <hr>
      <ul class="media-list">
        <li class="media">
          <a href="#" class="pull-left"> <img src="img/1.png" alt="" class="img-circle"> </a>
          <div class="media-body">
                              <span class="text-muted pull-right">
                                  <small class="text-muted">30 min ago</small>
                              </span> <strong class="text-success">@ Rexona Kumi</strong>
            <p>
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet,
              <a href="#"># consectetur adipiscing </a>. </p>
          </div>
        </li>
        <jsp:useBean id="note_list" scope="request" class="java.util.ArrayList" type="java.util.ArrayList<model.Note>"/>
        <c:forEach items="${note_list}" var="note">
          <li class="media">
            <a href="#" class="pull-left"> <img src="img/2.png" alt="" class="img-circle"> </a>
            <div class="media-body">
                              <span class="text-muted pull-right">
                                  <small class="text-muted">30 min ago</small>
                              </span> <strong class="text-success">${note.auther}</strong>
              <p> ${note.body}</p>
            </div>
          </li>
        </c:forEach>
        <li class="media">
          <a href="#" class="pull-left"> <img src="img/3.png" alt="" class="img-circle"> </a>
          <div class="media-body">
                              <span class="text-muted pull-right">
                                  <small class="text-muted">30 min ago</small>
                              </span> <strong class="text-success">@ Madonae Jonisyi</strong>
            <p>
              Lorem ipsum dolor <a href="#"># sit amet</a> sit amet, consectetur adipiscing elit. </p>
          </div>
        </li>
      </ul>
      <span class="text-danger">237K users active</span>
    </div>
  </div>
</div>
<!-- TWEET WRAPPER END -->

</body>
</html>
