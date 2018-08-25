<?php
/* persional music controller */

if (isset($_POST['action'])) {
	
    $action = $_POST['action'];
	
    if ($action === "start") {
		exec("start.vbs");
		exec("play.vbs");
	} else if ($action === "last") {
        exec("last.vbs");
    } else if ($action === "next") {
        exec("next.vbs");
    } else if ($action === "play") {
        exec("play.vbs");
    } else if ($action === "volume_up") {
		exec("volume_up.vbs");
	} else if ($action === "volume_down") {
		exec("volume_down.vbs");
	}
}

echo "{}";
