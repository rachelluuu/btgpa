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
	        <a href="/">HOME</a>
	        <a href="/about">ABOUT THIS APP</a>
	        <a href="/">LOGOUT</a>
	    </nav>
	    <div class="alert-success">The <b>BT GPA</b> app has been updated to reflect PowerSchool's year grades.</div>
	    <table>
	        <tr><th>Period</th><th>GPA</th></tr>
	        <tr><td>Marking Period 1</td><td>${mp1GPA}</td></tr>
	        <tr><td>Marking Period 2</td><td>${mp2GPA}</td></tr>
	        <tr><td>Marking Period 3</td><td>${mp3GPA}</td></tr>
	        <tr><td>Marking Period 4</td><td>${mp4GPA}</td></tr>
	        <tr><td>Year GPA</td><td>${yearGPA}</td></tr>
	    </table>
	</body>
</html>
