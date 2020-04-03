<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Update user</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <body>
    <script>
        function validate() {
            var isEmpty = false;
            var list = document.forms.form1.querySelectorAll('input[type=input]');
            for (var item of list) {
                if (document.getElementById(item.name).value == '') {
                    isEmpty = true;
                    alert("Field " + item.name + " non filled");
                }
            }
            if (isEmpty) {
                return false;
            }
        }

        function buttonReset() {
            $('#form1').trigger("reset");
            printCites();
            return false;
        }

        function printCites() {
            var id = $("#country :selected").val();
            $.ajax({
                type: "POST",
                url: "/chapter_009/ajax",
                contentType: 'application/json',
                data:JSON.stringify({type:"selectcity", data:id}),
                success: function(data, textStatus, jqXHR){
                    console.log('success');
                    $("#selectCity").html(jqXHR.responseText);
                },
                error: function(data, textStatus, jqXHR){
                    console.log('error');
                }
            });
        }
    </script>

        <c:set var="usr" value="${requestScope['find']}"/>
        <div class="col-sm-2"></div>
        <h1>Edit user</h1>
        <form method='post' class="form-horizontal" name="form1" id="form1" action='/chapter_009/edit'>
            <input type='hidden' name='id' value='${usr.id}'/>
            <div class="form-group">
                <label class="control-label col-sm-2" for="name">Name:</label>
                <div class="col-sm-10">
                    <input type='input' class="form-control" name='name' id='name' value='${usr.name}'/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="login">Login:</label>
                <div class="col-sm-10">
                    <input type='input' class="form-control" name='login' id='login' value='${usr.login}'/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="pass">Password:</label>
                <div class="col-sm-10">
                    <input type='input' class="form-control" name='pass' id='pass' value='${usr.pass}'/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="email">Email:</label>
                <div class="col-sm-10">
                    <input type='input' class="form-control" name='email' id='email' value='${usr.email}'/>
                </div>
            </div>
            <div class="form-group">
                <c:choose>
                    <c:when test="${sessionScope.fuser.role_id == 9999}">
                    <label class="control-label col-sm-2" for="role">Role</label>
                    <div class="col-sm-10">
                        <select class="form-control" name="role" id="role">
                                <c:forEach items="${requestScope['roles']}"  var="rol">
                                    <option value="${rol}" <c:if test = "${lor eq rol}"> selected="selected" </c:if> >${rol}</option>
                                </c:forEach>
                        </select>
                    </div>
                    </c:when>
                    <c:otherwise>
                        <label class="control-label col-sm-2" for="role">Role</label>
                        <div class="col-sm-10">
                           <c:out value='${requestScope.lor}' />
                            </br>
                        </div>
                        <input type='hidden' name='role' value='${requestScope.lor}'/>
                    </c:otherwise>
                </c:choose>
                </br>
                </br>
                <label class="control-label col-sm-2" for="country">Country</label>
                <div class="col-sm-10">
                    <select id="country" class="form-control" onchange="printCites();">
                    <c:forEach items="${requestScope['countres']}"  var="cnt">
                        <option value="${cnt}" <c:if test = "${country eq cnt}"> selected="selected" </c:if> >${cnt}</option>
                    </c:forEach>
                    </select>
                </div>
                </br>
                </br>
                <div id="selectCity">
                    <label class="control-label col-sm-2" for="city">Cites</label>
                    <div class="col-sm-10">
                        <select id="city" class="form-control" name='cname'>
                            <c:forEach items="${requestScope['cites']}"  var="cit">
                                <option value="${cit}" <c:if test = "${city eq cit}"> selected="selected" </c:if> >${cit}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                </br>
            </div>
            <input type='hidden' name='res' value='${usr.res}'/>
            <div class="col-sm-2"></div>
            <input type='reset' class="btn btn-default" name='but1' onclick="return buttonReset();" value='Reset'/>
            <input type='submit' class="btn btn-default" name='but2' onclick="return validate();" value='Submit'/>
        </form>
    </body>
</html>
