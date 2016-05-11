<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<link rel="stylesheet" href="style.css" />
		<title>Testing color changes: Login</title>
	</head>

	<body>
	<div id="mainwrapper">
		<?php
			/*$nbrounds = 20;
			$sets = range(1, $nbrounds);*/
			$sets = range(0, 4);
			shuffle($sets);
		?>
		
		<h1> Instructions </h1>
		<p class="welcome">Hello, thanks for participating in this experiment! </p>
		
		<p> You will be asked to evaluate various images, were color was changed from the original image using different methods.
		Just chose the image most <em>visually</em> pleasing, and click next. <br />
		Make sure to be in a place with enough white light and that your screen luminosity isn't too low.</p>
		
		<p>Please enter your sciper.<br />
			<form method="post" action="imagetest.php">
				<label for="sciper"> Sciper </label>
				<input type="number" min="0" max="999999" name="sciper" id="sciper" required autofocus /> <br /> <br />
				Click <input type="submit" value="next" class="link"> to continue.
				
				<!-- Hidden inputs -->
				<?php 
				foreach($sets as $value){
				  echo '<input type="hidden" name="sets[]" value="'.$value.'"/>';
				}
				?>
				<input type="hidden" value="first" name="from" />
				<?php 
					echo '<input type="hidden" value="'.$sets[0].'" name="set" />';
				?>
				
			</form>
			
		</p>
		
	</div>
	</body>
</html>	