<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<link rel="stylesheet" href="style.css" />
		<title>Testing color changes: Test</title>
	</head>

	<body>
	
		<?php 					
		if(!isset($_POST['sets']) || !isset($_POST['sciper'])){header('Location:error.php');}
		
		$sets = $_POST['sets'];
		$set = $sets[0];
		$sets = array_slice($sets, 1);
		$results = fopen("results/".$_POST['sciper'].".txt", "a") or die("Fatality: results/".$_POST['sciper'].".txt");
		switch($_POST['from']) {
			case "first":
			$txt = "Sciper ".$_POST['sciper']."\n";
			break;
			
			case "next":
			$txt = "Image Set: ".$_POST['set'].", Chosen: ".$_POST['choice']."\n";
			break;
		}
		fwrite($results, $txt);
		fclose($results);
		?>
		<div id="mainwrapper">
			<fieldset>
				<legend> Choose the best image! </legend>
				<?php 
					$numbers=array(1, 2, 3, 4);
					shuffle($numbers);
				?>


				<form method="post" action=<?php
				if(sizeof($sets)==0){
					echo '"page2.php"';
				}
				else{
					echo '"imagetest.php"';
				}
				?> id="images">
					<label for="A"> 
						<div id="1">
							<img src="content/rand<?php echo $set;?>/<?php echo $numbers[0]?>.jpg" width="550" >  <br />
							<input type="radio" name="choice" value="<?php echo $numbers[0]?>" id="A" required/> Image A
						</div>
					</label>
					<label for="B">
						<div id="2">
							<img src="content/rand<?php echo $set;?>/<?php echo $numbers[1]?>.jpg" width="550"> <br />
							<input type="radio" name="choice" value="<?php echo $numbers[1]?>" id="B" /> Image B 
						</div>
					</label>
					<label for="C">
						<div id="3">
							<img src="content/rand<?php echo $set;?>/<?php echo $numbers[2]?>.jpg" width="550"> <br />
							<input type="radio" name="choice" value="<?php echo $numbers[2]?>" id="C" /> Image C 
						</div>
					</label>
					<label for="D">
						<div id="4">
							<img src="content/rand<?php echo $set;?>/<?php echo $numbers[3]?>.jpg" width="550"> <br />
							<input type="radio" name="choice" value="<?php echo $numbers[3]?>" id="D" /> Image D
						</div> <br />
					</label>
					<input type="submit" value="Next" />
					
					<!-- Hidden inputs -->
					<input type="hidden" value="next" name="from" />
					<input type="hidden" value="<?php echo $_POST['sciper']?>" name="sciper" />
					<?php 
					echo '<input type="hidden" value="'.$set.'" name="set" />';
					?>
					<?php 
					foreach($sets as $value){
					  echo '<input type="hidden" name="sets[]" value="'. $value. '"/>';
					}
					?>
				</form>
			</fieldset>


		

		</div>
	</body>

</html>