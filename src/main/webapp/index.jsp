<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>BT GPA Calculator By Code Club</title>
        <meta name="description" content="GPA Calculator" />
        <meta name="keywords" content="gpa" />
        <meta name="author" content="Code Club" />
        <link rel="icon" type="image/png" href="icon.png" />
        <link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>

        <h1>BT <strong>GPA</strong> CALCULATOR</h1>
        <h2>Developed By Bergen Tech Code Club</h2>
        <nav class="buttons">
            <a class="current" href="/">HOME</a>
            <a href="/about">ABOUT THIS APP</a>
        </nav>
	${error}
	<div class="alert"> !! We are aware of the login error and are currently working on fixing it !! </div> 
      <form class="form-signin" role="form" name="login" action="/gpa" method="POST">
        <input name="username" type="username" class="form-control" placeholder="PowerSchool Username" required autofocus>
        <input name="password" type="password" class="form-control" placeholder="PowerSchool Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">CALCULATE</button>
      </form>

    </body>
</html>
