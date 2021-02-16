<?php
	require_once("dbconnect.php");
	
	$userID = "";
	$country = "";
	$orders = "";
	$bought = "";
	
	if(isset($_POST['userid'])){
		$sql="SELECT UserID, Country FROM users WHERE UserID = '$_POST[userid]'";
		$result = mysqli_query($conn,$sql);
		
		if (!$result) {
			die('Error: ' . mysqli_error($conn));
			} else {
			while ($row = mysqli_fetch_array($result)) {
				$userID = $row["UserID"];
				$country = $row["Country"];
			}
		}
		
		$sql="SELECT COUNT(UserID) FROM orders WHERE UserID = '$_POST[userid]'";
		$result = mysqli_query($conn,$sql);
		
		if (!$result) {
			die('Error: ' . mysqli_error($conn));
			} else {
			while ($row = mysqli_fetch_array($result)) {
				$orders = $row["COUNT(UserID)"];
			}
		}
		
		$sql="SELECT SUM(Quantity) FROM trans WHERE OrderID IN (SELECT OrderID FROM orders WHERE UserID = '$_POST[userid]')";
		$result = mysqli_query($conn,$sql);
		
		if (!$result) {
			die('Error: ' . mysqli_error($conn));
			} else {
			while ($row = mysqli_fetch_array($result)) {
				$bought = $row["SUM(Quantity)"];
			}
		}
	}
	
	function stars($ISBN,$conn){
		$stars = "";
		$num = 0;
		$sql2="SELECT AVG(Rating) FROM bookratings WHERE ISBN = '$ISBN';";
		$result2 = mysqli_query($conn,$sql2);
		if (!$result2) {
			die('Error: ' . mysqli_error($conn));
			} else {
			while ($row2 = mysqli_fetch_array($result2)) {
				$num = (int) $row2["AVG(Rating)"];
			}
		}
		
		while ($num > 0) {
			$stars = $stars . "*";
			$num = $num - 1;
		}
		return $stars;
	}
	
	function quantity($ISBN,$conn){
		$num = 0;
		$sql2="SELECT Quantity FROM trans WHERE ISBN = '$ISBN';";
		$result2 = mysqli_query($conn,$sql2);
		if (!$result2) {
			die('Error: ' . mysqli_error($conn));
			} else {
			while ($row2 = mysqli_fetch_array($result2)) {
				$num = (int) $row2["Quantity"];
			}
		}
		
		if ($num > 1){
			return $num;
		}
		
		return 1;
	}
?>
<html>
	<head>
		<title>Amazon - Order History</title>
	</head>
	<body>
		<div id="header" style="width:710px">
			<img src="amazon_topbanner.png"></div>
			<div id="body" style="width:710px">
				<table style="width:100%">
					<tr>
						<td>
							<table>
								<tr>
									<td>
									<p style="font-size: 18px;">User Details:</p>
									</td>
								</tr>
								<tr>
									<td>
									UserID:
									<?php echo $userID ?>
									</td>
								</tr>
								<tr>
									<td>
									Country:
									<?php echo $country ?>
									</td>
								</tr>
							</table>
						</td>
						<td style="text-align:right">
							<table style="float:right">
								<tr>
									<td style="text-align:right">
										<p style="font-size: 18px;">Orders Summary:</p>
									</td>
								</tr>
								<tr>
									<td style="text-align:right">Orders Placed:
										<?php echo $orders ?>
									</td>
								</tr>
								<tr>
									<td style="text-align:right">Books bought:
										<?php echo $bought ?>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<hr>
				<table style="width:100%;" cellspacing="0">
					<?php
						if(isset($_POST['userid'])){
							$sql="SELECT Year,OrderID,Totalpay FROM orders WHERE UserID = '$_POST[userid]' ORDER BY Year DESC;";
							$result = mysqli_query($conn,$sql);
							
							if (!$result) {
								die('Error: ' . mysqli_error($conn));
								} else {
								while ($row = mysqli_fetch_array($result)) {
									echo "<tr>";
									echo "<td style='background-color:#BABABA'>";
									echo "Order Placed <br>";
									echo $row["Year"];
									echo "</td>";
									echo "<td style='background-color:#BABABA'>";
									echo "Total <br>";
									echo "£". $row["Totalpay"];
									echo "</td>";
									echo "<td style='background-color:#BABABA'>";
									
									//discount
									$sql3="SELECT Refund FROM refund WHERE OrderID = '$row[OrderID]'";
									$result3 = mysqli_query($conn,$sql3);
									if (!$result3) {
										die('Error: ' . mysqli_error($conn));
										} else {
										while ($row3 = mysqli_fetch_array($result3)) {
											if ((float) $row3["Refund"] < 0) {
												echo "Discount <br>";
												echo "£", (((float) $row3["Refund"]) * -1);
												} else if ((float) $row3["Refund"] > 0) {
												echo "Refund <br>";
												echo "£", $row3["Refund"];
											}
										}
									}
									
									echo "</td>";
									echo "<td style='background-color:#BABABA'>";
									echo "OrderID <br>";
									echo $row["OrderID"];
									echo "</td>";
									echo "</tr>";
									
									
									$sql2="SELECT * FROM books WHERE ISBN IN (SELECT ISBN FROM trans WHERE OrderID = '$row[OrderID]') ORDER BY Genre ASC;";
									$result2 = mysqli_query($conn,$sql2);
									if (!$result2) {
										die('Error: ' . mysqli_error($conn));
										} else {
										while ($row2 = mysqli_fetch_array($result2)) {
											echo "<tr>";
											echo "<td>";
											echo "<img src='$row2[ImageURL]'>";//img
											echo "</td>";
											echo "<td>";
											$num = quantity($row2["ISBN"],$conn);
											if ($num > 1) {
												echo $num . " of ";
											}
											echo $row2["Title"],"<br>";//title
											echo $row2["ISBN"],"<br>";//isbn
											echo "£". $row2["Unitprice"],"<br>";//unit price
											echo stars($row2["ISBN"],$conn);//stars
											echo "</td>";
											echo "<td>";
											echo $row2["Genre"];//genre
											echo "</td>";
											echo "<td>";
											echo "£". ((double) quantity($row2["ISBN"],$conn)) * $row2["Unitprice"];//total price
											echo "</td>";
											echo "</tr>";
										}
									}
									echo "<tr>";
									echo "<td>";
									echo "&nbsp;";
									echo "</td>";
									echo "</tr>";
									
								}
							}
						}
					?>
				</table>
			</div>
			<div id="footer" style="width:710px">
				<img src="amazon_bottombanner.png">
				<br>
				<hr>
				<form action="amazon.php" method="post">
					<table>
						<tr>
							<td>
								<b>Query</b> UserID:
							</td>
							<td>
								<input type="text" id="userid" name="userid">
							</td>
							<td>
								<input type="submit" id="submit" name="submit">
							</td>
						</tr>
					</table>
				</form>
			</div>
	</body>
<?php mysqli_close($conn); ?>
</html>