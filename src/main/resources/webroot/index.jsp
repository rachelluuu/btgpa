<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>BCA GPA Calculator By Kenneth Rhee</title>
        <meta name="description" content="GPA Calculator" />
        <meta name="keywords" content="gpa" />
        <meta name="author" content="Kenneth Rhee" />
        <link rel="icon" type="image/png" href="icon.png" />
        <link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>

        <h1>BCA <strong>GPA</strong> CALCULATOR</h1>
        <h2>Developed By Kenneth Rhee</h2>
        <nav class="buttons">
            <a class="current" href="/">HOME</a>
            <a href="/about">ABOUT THIS APP</a>
        </nav>
	${error}
	<div class="alert">This app will be back up mid September. Stay tuned! - Kenneth</div>
      <form class="form-signin" role="form" name="login" action="/gpa" method="POST">
        <input name="username" type="username" class="form-control" placeholder="PowerSchool Username" required autofocus>
        <input name="password" type="password" class="form-control" placeholder="PowerSchool Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">CALCULATE</button>
      </form>

    </body>
</html>