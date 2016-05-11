<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<link rel="stylesheet" href="style.css" />
		<title>Testing color changes</title>
	</head>

	<body>
		<?php
		if(!isset($_POST['choice'])){
			header('Location:error.php');
		}
		
		$results = fopen("results/".$_POST['sciper'].".txt", "a") or die("Fatality.");
		$txt = "Image Set: ".$_POST['set'].", Chosen: ".$_POST['choice']."\nEnd\n";
		fwrite($results, $txt);
		fclose($results);
		?>
		
		<div id="mainwrapper">
			<h2>Success!</h2>
			
			<p>
			You have succesfully completed the task!
			<br />
			<br />
			<img src="content/pouce.png" alt="poucevert" id="poucevert"/> 
			</p>
		</div>
	</body>
</html>	