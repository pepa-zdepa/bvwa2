<?php

$dbName = "../db";
$db = new PDO("sqlite:$dbName");
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$statement = $db->prepare("SELECT * FROM users where nickname = ?;");
$dbRes = $statement->execute([$_POST["user"]]);
$result = $statement->fetch();

if($result) {
    header('Content-type: application/json');

    if (password_verify($_POST["password"], $result['password'])) {
        echo json_encode(array('id' => $result["id"], 'role' => $result["role"], 'nickname' => $result["nickname"]));
    } else {
        header("HTTP/1.1 401 Unauthorized");
    }
} else {
    header("HTTP/1.1 401 Unauthorized");
}

?>
