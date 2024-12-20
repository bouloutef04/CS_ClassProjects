<!DOCTYPE html>

<%
    String resultA = (String) session.getAttribute("resultA");
  
    if (resultA == null) 
    {
      resultA = " ";
    }
%>

<html lang="en">
    <head>
        <title>Authentication</title>
        <meta charset="utf-8">
        <style>
            <!--
                .center {margin: 0; position: relative; left: 40%;}
                body { background-color: black; color:lime; font-family: verdana, arial, sans-serif; font-size: 1.4em;  }
                h1 { color:lime; font-size: 1.3em; text-align: center;}
                h2 { color:orange; font-size: 1.2em; text-align: center;}
                h3 {color:red; font-size: 1.1em; text-align: center;}
                h4 {color:white; font-size: 0.6em; text-align: center;}
                h5 {color:black; font-size: 0.7em; text-align: center;}
                form {background-color: grey; color: blue; font-size: 0.6em;}
                input [type=radio] {background-color: grey; font-size: 0.6em; color:white; display: block; margin-left: auto; margin-right: auto;}
                button[type="execute"] {background-color: darkslategrey; color:lime; font-weight:bold; text-align: center;  justify-content: center;}
                button[type="clear"] {background-color: darkslategrey; color:yellow; font-weight:bold; text-align: center;  justify-content: center;}
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
        <h4>You are connected to the Project 4 Enterprise System database as a <span style="color:red">accountant-level</span> user</h4>
        <h4>Please enter any SQL query or update command in the box below.</h4>
        <br><br>
        <form action="AccountantCommand" method="post">
            <input type="radio" id="max" name="qOptions" value="Max">
            <label for="Max">Get The Maximum Status Value Of All Suppliers (Returns a maximum value)</label>
            <br><br>
            <input type="radio" id="weight" name="qOptions" value="Weight">
            <label for="Weight">Get The Total Weight Of All Parts (Returns a sum)</label>
            <br><br>
            <input type="radio" id="shipments" name="qOptions" value="Shipments">
            <label for="Shipments">Get The Total Number Of Shipments</label>
            <br><br>
            <input type="radio" id="workers" name="qOptions" value="Workers">
            <label for="Workers">Get The Name And Number Of Workers Of The Job With The Most Workers (Returns two values)</label>
            <br><br>
            <input type="radio" id="suppliers" name="qOptions" value="Suppliers">
            <label for="Suppliers">List The Name And Status Of Every Supplier (Returns a list of supplier names with status)</label>
            <br><br>
            <button type="execute">Execute Command</button>
            <button type="clear">Clear Results</button>
        </form>
        
        <br>
        <h4>All execution results will appear below this line.</h4>
        <hr>
        <h4>Execution Results:</h4>
        <br><br>
       <div class="center">
            <h5>
                <%=resultA%>
            </h5>
        </div>

    </body>
</html>