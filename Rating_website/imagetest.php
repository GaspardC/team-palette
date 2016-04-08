<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<link rel="stylesheet" href="style.css" />
		<title>Testing color changes: Test</title>
	</head>

	<body>
	
		<?php 
		if(isset($_POST['sciper']) && isset($_POST['login'])){
			$granted = 0;
			$logins = fopen('logins.txt', 'r+');
			while(($line = fgets($logins)) !== false && $granted==0){
				$tscip = preg_replace('#^.*sciper:([0-9]{1,3})_.*$#', '$1' ,$line);
				$tscip=preg_replace('/[^0-9]+/', '', $tscip);
				$tlog = preg_replace('#^.*_login:([0-9]{1,8}).*$#', '$1' ,$line);
				$tlog=preg_replace('/[^0-9]+/', '', $tlog);
				
				//echo '\''.$tscip .'\' _ \''. $tlog.'\' au lieu de \''.$_POST['sciper'].'\' _ \''.$_POST['login'].'\'<br />';
				
				if($tscip==$_POST['sciper'] && $tlog==$_POST['login']){
					$granted = 1;
				}
			}
			
			fclose($logins);
			if($granted==0){
				header('Location:index.php?g=0');
			}
		}
		
		if(!isset($_POST['sets'])){header('Location:error.php');}
		
		$sets = $_POST['sets'];
		$set = $sets[0];
		$sets = array_slice($sets, 1);
		$results = fopen("results/".$_POST['login'].".txt", "a") or die("Fatality.");
		switch($_POST['from']) {
			case "first":
			$txt = "Subject ".$_POST['login'].", Sciper ".$_POST['sciper']."\n";
			break;
			
			case "next":
			$txt = "Image Set: ".$set.", Chosen: ".$_POST['choice']."\n";
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
				if(empty($sets)){
					echo '"page2.php"';
				}
				else{
					echo '"imagetest.php"';
				}
				?> id="images">
					<label for="A"> 
						<div id="1">
							<img src="content/rand<?php echo $set;?>/<?php echo $numbers[0]?>.jpg">  <br />
							<input type="radio" name="choice" value="<?php echo $numbers[0]?>" id="A" required/> Image A
						</div>
					</label>
					<label for="B">
						<div id="2">
							<img src="content/rand<?php echo $set;?>/<?php echo $numbers[1]?>.jpg"> <br />
							<input type="radio" name="choice" value="<?php echo $numbers[1]?>" id="B" /> Image B 
						</div>
					</label>
					<label for="C">
						<div id="3">
							<img src="content/rand<?php echo $set;?>/<?php echo $numbers[2]?>.jpg"> <br />
							<input type="radio" name="choice" value="<?php echo $numbers[2]?>" id="C" /> Image C 
						</div>
					</label>
					<label for="D">
						<div id="4">
							<img src="content/rand<?php echo $set;?>/<?php echo $numbers[3]?>.jpg"> <br />
							<input type="radio" name="choice" value="<?php echo $numbers[3]?>" id="D" /> Image D
						</div> <br />
					</label>
					<input type="submit" value="Next" />
					
					<!-- Hidden inputs -->
					<input type="hidden" value="next" name="from" />
					<input type="hidden" value="<?php echo $_POST['login']?>" name="login"/>
					<input type="hidden" value="<?php echo $_POST['sciper']?>" name="sciper" />
					<?php if(empty($sets)){
					echo '<input type="hidden" value="'.$set.'" name="set" />';}?>
				
					
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