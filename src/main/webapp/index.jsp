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
	    <script>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
			})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');		
			ga('create', 'UA-89956086-1', 'auto');
			ga('send', 'pageview');
		</script>
    </head>
    <body>
        <h1>BT <strong>GPA</strong> CALCULATOR</h1>
        <h2>Developed By Bergen Tech Code Club</h2>
        <nav class="buttons">
            <a class="current" href="/">HOME</a>
            <a href="/about">ABOUT THIS APP</a>
        </nav>
	${error}
	<!-- div class="alert"> !! We are aware of the login error and are currently working on fixing it !! </div>  -->
      <form class="form-signin" role="form" name="login" action="/gpa" method="POST">
        <input name="username" class="form-control" placeholder="PowerSchool Username" required autofocus>
        <input name="password" type="password" class="form-control" placeholder="PowerSchool Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">CALCULATE</button>
      </form>
    </body>
</html>
