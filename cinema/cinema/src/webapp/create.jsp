<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Create user</title>
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
                    $("#selectCity").html(jqXHR.responseText);
                },
                error: function(data, textStatus, jqXHR){
               }
            });
        }
    </script>

    <div class="col-sm-2"></div>
    <h1>New user</h1>
    <form class="form-horizontal" method='post' id="form1" name="form1" action='/chapter_009/create'>
        <div class="form-group">
            <label class="control-label col-sm-2" for="name">Name:</label>
            <div class="col-sm-10">
                <input type='input' class="form-control" name='name' id='name' value=''/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="login">Login:</label>
            <div class="col-sm-10">
                <input type='input' class="form-control" name='login' id='login' value=''/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="pass">Password:</label>
            <div class="col-sm-10">
                <input type='input' class="form-control" name='pass' id='pass' value=''/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="email">Email:</label>
            <div class="col-sm-10">
                <input type='input' class="form-control" name='email' id='email' value=''/>
            </div>
        </div>
        <div class="form-group">
        <label class="control-label col-sm-2" for="role">Role</label>
        <div class="col-sm-10">
            <select class="form-control" id="role" name="role">
                <c:forEach items="${requestScope['roles']}"  var="rol">
                    <option value="${rol}">${rol}</option>
                </c:forEach>
            </select>
        </div>
        </br>
        </br>
        <label class="control-label col-sm-2" for="country">Country</label>
        <div class="col-sm-10">
            <select id="country" class="form-control" onchange="printCites();">
                <c:forEach items="${requestScope['countres']}"  var="cnt">
                    <option value="${cnt}">${cnt}</option>
                </c:forEach>
            </select>
        </div>
        </br>
        </br>
        <div id="selectCity"></div>
        </div>
        </br>
        </br>
        <script>printCites();</script>
        <div class="col-sm-2"></div>
        <input type='reset' class="btn btn-default" name='but1' onclick="return buttonReset();" value='Reset'/>
        <input type='submit' class="btn btn-default" name='but2' onclick="return validate();" value='Submit'/>
    </form>
    </body>
</html>
