<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
            integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"
            integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI"
            crossorigin="anonymous"></script>
    <title>Accident</title>
</head>
<body>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <div class="form-row">
                    <div class="col-md-8 my-auto">
                        <h4 style="margin-bottom: 0">Автонарушения</h4>
                    </div>
                </div>
            </div>
            <div class="card-body pt-0 pb-0">
                <a href="<c:url value='/create'/>" class="btn btn-primary">Добавить инцидент</a>
                <table id="table" class="table table-striped">
                    <thead>
                    <tr>
                        <th scope="col" style="width:2%">№</th>
                        <th scope="col" style="width:15%">Название</th>
                        <th scope="col" style="width:10%">Тип</th>
                        <th scope="col" style="width:10%">Статьи</th>
                        <th scope="col" style="width:38%">Описание</th>
                        <th scope="col" style="width:23%">Адрес</th>
                        <th scope="col" style="width:2%"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${accidents}" var="accident">
                        <tr>
                            <td><c:out value="${accident.id}"/></td>
                            <td><c:out value="${accident.name}"/></td>
                            <td><c:out value="${accident.type.name}"/></td>
                            <td>
                                <c:forEach items="${accident.rules}" var="rule">
                                    <c:out value="${rule.name}"/><br>
                                </c:forEach>
                            </td>
                            <td><c:out value="${accident.text}"/></td>
                            <td><c:out value="${accident.address}"/></td>
                            <td>
                                <a href='<c:url value="/update?id=${accident.id}"/>'>
                                    <i class="fa fa-edit mr-3"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>