<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <title>留言版</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
  <link href="https://cdn.bootcss.com/bootstrap/4.0.0-beta.2/css/bootstrap.css" rel="stylesheet">
  <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
  <script src="https://cdn.bootcss.com/bootstrap/4.0.0-beta.2/js/bootstrap.js"></script>
  <%--<!--<script src="js/jquery-3.2.1.js"></script>-->--%>
  <%--<!--<script src="js/bootstrap.js"></script>-->--%>
  <style>
    /*
 * Specific styles of signin component
 */
    /*
     * General styles
     */
    body, html {
      height: 100%;
      background: linear-gradient(rgb(112, 162, 158), rgb(95, 124, 177)) no-repeat;
    }

    /*.container, .card-container {*/
      /*margin: auto;*/
      /*height: 100%;*/
      /*display: flex;*/
      /*justify-content: center;*/
      /*align-items: center;*/
    /*}*/

    /*
     * Card component
     */
    .card {
      background-color: #F7F7F7;
      padding: 2rem;
      border-radius: 1rem;
      box-shadow: 0 0 1rem 0.2rem rgba(56, 81, 79, 0.3);
      transition: box-shadow 0.5s ease-out;
    }

    .card:hover {
      box-shadow: 0 0 2rem 1rem rgba(59, 67, 79, 0.4);
      transition: box-shadow 0.5s ease-out;
    }

    /*
     * Form styles
     */
    .form-signin #username_input {
      direction: ltr;
      height: 2rem;
      font-size: 1rem;
      text-align: center;
    }

    .form-signin input[type=text],
    .form-signin button {
      width: 100%;
      display: block;
      margin-bottom: 0.5rem;
      z-index: 1;
      position: relative;
      box-sizing: border-box;
    }

    .form-signin .form-control:focus {
      border-color: rgb(104, 145, 162);
      outline: 0;
      box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgb(104, 145, 162);
    }

    .btn.btn-primary {
      background-color: rgb(104, 145, 162);
      padding: 0.5rem 1rem;
      border-radius: 0.2rem;
      border: none;
      transition: all 0.4s;
    }

    .btn.btn-primary:hover,
    .btn.btn-primary:active,
    .btn.btn-primary:focus {
      background-color: rgb(71, 164, 216);
    }


  </style>
</head>

<body data-gr-c-s-loaded="true">
<div class="container">
  <div class="card-container">
    <div class="card">
      <h4 class="card-header w-100">留言板</h4>

      <div class="card-body">
        <form action="list.do" id="make_note" method="post" class="form-group">
          <textarea class="form-control" placeholder="写点什么吧😘" name="textarea" rows="3" spellcheck="true"></textarea>
          <br>
          <button formaction="logout.do" formmethod="get" class="btn btn-outline-danger float-left">登出</button>
          <button type="submit" class="btn btn-primary float-right">┏ (゜ω゜)=☞</button>
        </form>

        <div class="clearfix"></div>
        <hr>
        <div class="list-group">
          <jsp:useBean id="note_list" scope="request" class="java.util.ArrayList" type="java.util.ArrayList<model.Note>"/>
          <c:forEach items="${note_list}" var="note">
            <a href="#" class="list-group-item list-group-item-action flex-column align-items-start">
              <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1">@${note.author.username}</h5>
                <small>${note.formatTime()}</small>
              </div>
              <p class="mb-1">${note.body}</p>
            </a>
          </c:forEach>
        </div>
      </div>
      <!--<span class="text-danger float-right">4个</span>-->
    </div>
  </div>

</div>

</body>
</html>
