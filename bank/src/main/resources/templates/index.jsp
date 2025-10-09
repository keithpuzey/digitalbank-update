<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Demo Server</title>
<style>
    /* Body styling */
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        margin: 0;
        padding: 0;
        background: linear-gradient(135deg, #e3f2fd, #bbdefb);
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 100vh;
        color: #333;
    }

    /* Main container */
    .container {
        text-align: center;
        background-color: #ffffff;
        padding: 50px 30px;
        border-radius: 15px;
        box-shadow: 0 15px 30px rgba(0,0,0,0.1);
        max-width: 900px;
        width: 95%;
        position: relative;
    }

    h1 {
        font-size: 2.5em;
        margin-bottom: 30px;
        color: #0D47A1;
        text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
    }

    /* Buttons container */
    .buttons {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 20px;
        margin-bottom: 20px;
    }

    /* Individual buttons */
    .buttons a {
        display: inline-block;
        padding: 15px 25px;
        font-size: 1.1em;
        text-decoration: none;
        color: #ffffff;
        background: linear-gradient(135deg, #1E88E5, #1976D2);
        border-radius: 50px;
        transition: all 0.3s ease;
        box-shadow: 0 6px 15px rgba(30,136,229,0.25);
    }

    .buttons a:hover {
        background: linear-gradient(135deg, #1565C0, #0D47A1);
        transform: translateY(-3px);
        box-shadow: 0 10px 20px rgba(30,136,229,0.35);
    }

    .buttons a:active {
        transform: translateY(0);
        box-shadow: 0 5px 15px rgba(30,136,229,0.25);
    }

    /* Tomcat version */
    .tomcat-version {
        position: absolute;
        bottom: 15px;
        right: 20px;
        font-size: 0.9em;
        color: #555;
    }

    /* Mobile adjustments */
    @media (max-width: 600px) {
        h1 {
            font-size: 2em;
        }

        .buttons a {
            width: 100%;
            text-align: center;
        }
    }
</style>
</head>
<body>
    <div class="container">
        <h1>Welcome to the Demo Server</h1>

        <!-- First row of buttons -->
        <div class="buttons">
            <a href="/bank">Digital Banking</a>
            <a href="/query.html">Database Demo</a>
            <a href="https://blazedemo.com/" target="_blank">Travel Website</a>
            <a href="https://www.demoblaze.com/index.html" target="_blank">Product Store</a>
        </div>

        <!-- Second row of buttons -->
        <div class="buttons">
            <a href="/nestedtableexample1.html">Financial Statement</a>
            <a href="/nestedtableexample.html">Advanced Portfolio Dashboard</a>
            <a href="/guicardexample.html">Financial Dashboard</a>
        </div>
		<!-- Third row of buttons -->
		<div class="buttons">
			<a href="/flow-demo.html">Process Flow Demo</a>
		    <a href="/sv-user-profile.html">Virtual Service SQL Query Directory</a>
		    <a href="/sv-profile.html">Virtual Service SQL Query Profile</a>
		</div>
		
        <!-- Tomcat version -->
        <div class="tomcat-version">
             <%= application.getServerInfo() %>
        </div>
    </div>
</body>
</html>