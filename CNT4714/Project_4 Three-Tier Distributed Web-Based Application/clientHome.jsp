<!DOCTYPE html>

<%
    String result = (String) session.getAttribute("result");
  
    if (result == null) 
    {
      result = " ";
    }
%>


<html lang="en">
    <head>
        <title>ClientHome</title>
        <meta charset="utf-8">
        <style>
            <!--
                .center {margin: 0; position: relative; left: 40%;}
                body { background-color: black; color:lime; font-family: verdana, arial, sans-serif; font-size: 1.4em;  }
               h1 { color:red; font-size: 1.3em; text-align: center;}
                h2 { color:cyan; font-size: 1.2em; text-align: center;}
                h3 {color:red; font-size: 1.1em; text-align: center;}
                h4 {color:white; font-size: 0.6em; text-align: center;}
                h5 {color:black; font-size: 0.7em; text-align: center;}
                .bottom {margin-left: auto; margin-right: auto;}
                textarea {background-color: blue; font-size: 0.7em; color:white; display: block; margin-left: auto; margin-right: auto;}
                button[type="execute"] {background-color: rgb(150, 150, 21); color:lime; font-weight:bold; text-align: center;  justify-content: center;}
                input[type="reset"] {background-color: rgb(150, 150, 21); color:red; font-weight:bold; text-align: center; justify-content: center;}
                button[type="clear"] {background-color: rgb(150, 150, 21); color:yellow; font-weight:bold; text-align: center;  justify-content: center;}
                span {color: red;}
                #servlet {color:purple;}
                #jsp {color:cyan;}
            -->
        </style>
        <style>
            input[type="textBox"] {size: 50rem;}
        </style>
    </head>
    <body>
        
        <h1>Welcome to the Fall 2024 Project 4 Enterprise System</h1>
        <h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
        <br><br>
        <hr>
    </body>

    <body>
        <h4>You are connected to the Project 4 Enterprise System database as a <span style="color:red">client-level</span> user</h4>
        <h4>Please enter any SQL query or update command in the box below.</h4>
        <br><br>
        
        <form action="ClientCommand" method="post">
            <textarea id="commands" name="commands" rows="10" cols="50"></textarea>
            <br><br>
            <div class="center">
            <button action="" method="get" type="execute">Execute Command</button>
            <input type="reset" value="Reset Form">
            <button type="clear" value="clear">Clear Results</button>
            </div>
        </form>
        
        <br><br>
        <h4>All execution results will appear below this line.</h4>
        <hr>
        <h4>Execution Results:</h4>
        <br><br>
        <div class="center">
            <h5>
                <%=result%>
            </h5>
        </div>
    </body>
</html>