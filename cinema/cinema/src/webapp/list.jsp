<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Users list </title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <h2>
        <script>
            function idToName(type, data, id) {
                $.ajax({
                    url:"/chapter_009/ajax",
                    method:'POST',
                    contentType: 'application/json',
                    data:JSON.stringify({type:type, data:data}),
                    success: function(data, textStatus, jqXHR){
                        console.log('success');
                        $('td[id=' + id+ ']').html(jqXHR.responseText);
                    },
                    error: function(data, textStatus, jqXHR){
                        console.log('error');
                    }
                });
            }
        </script>

        <h2> Login user: <c:out value='${sessionScope.fuser.login}' /></h2>
        <form method='get' action='/chapter_009/login'>
            <input type='hidden' name='action' value='logout'>
            <input type='submit' class="btn btn-default" value='Logout' />
        </form>
        <c:if test = "${sessionScope.fuser.role_id == 9999}">
            <form method='get' action='/chapter_009/create'>
                <input type='submit' class="btn btn-default" value='New user' />
            </form>
        </c:if>
        <h2>User's table</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Login</th>
                    <th>Email</th>
                    <th>City</th>
                    <th>Role</th>
                    <th>Date creation</th>
                </tr>
            </thead>
            <c:forEach items="${requestScope['usrs']}"  var="usr">
                <tr>
                    <td>${usr.id}</td>
                    <td>${usr.name}</td>
                    <td>${usr.login}</td>
                    <td>${usr.email}</td>
                    <td id="${usr.id}"><script>idToName('city', ${usr.city_id}, ${usr.id});</script></td>
                    <td id="r${usr.id}"><script>idToName('role', ${usr.role_id}, "r" + ${usr.id});</script></td>
                    <td>${usr.res}</td>
                    <td>
                        <c:if test = "${sessionScope.fuser.role_id == 9999 or sessionScope.fuser.login == usr.login}">
                            <form method='get' action='/chapter_009/edit'>
                                <input type='hidden' name='action' value='update'>
                                <input type='hidden' name='id' value='${usr.id}'/>
                                <input type='submit' value='Edit' />
                            </form>
                        </c:if>
                    </td>
                    <td>
                        <c:if test = "${sessionScope.fuser.role_id == 9999}">
                            <form method='post' action='/chapter_009/list'>
                                <input type='hidden' name='action' value='delete'>
                                <input type='hidden' name='id' value='${usr.id}'/>
                                <input type='submit' value='Delete'/>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
