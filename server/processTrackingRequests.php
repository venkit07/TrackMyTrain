<?php

require 'parse/autoload.php';
use Parse\ParseClient;
use Parse\ParseQuery;
use Parse\ParsePush;
use Parse\ParseInstallation;
 
ParseClient::initialize('VllO7fW0b79l4aKhs6TQjjxqzrLDRxXbg4PCE9fa', 'uDjO7k0T6lCASYFGH3z9W9DVqMtYZskGHgG7ADGx', 'MO0Bmb78uzCKlG8sO7XN8y62DDJAOaf8ewi642j3');

$query = new ParseQuery("Reminder");
$results = $query->find();
foreach ($results as $row) {
	if(!$row->{'status'}){
		$stringResult = curl("http://runningstatus.in/status/".$row->{'query'});
		$result = processResult($stringResult, $row->{'trainNumber'}, $row->{'stationCode'}, $row->{'deviceId'}, $row->{'isOnBoard'});
		if($result){
			$row->set("status", 1);
			$row->save();
		}
	}
}

function processResult($stringResult, $trainNumber, $stationCode, $deviceId, $isOnBoard){
	$result = getBetween($stringResult, "<tbody>", "</tbody>");
	$dom = new DOMDocument();
	$dom->loadHTML($result);
	$domx = new DOMXPath($dom);
	$entries = $domx->evaluate("//tr");
	$success = false;
	foreach ($entries as $entry) {
		$resultString = $entry->nodeValue;
		if(strstr($resultString, $stationCode) && strstr($resultString, "Departed")){
			if($isOnBoard){
				echo $prev.'<br/>';
				$stationCode = getBetween($prev, "(", ")");
			}
			$query = ParseInstallation::query();
			$query->equalTo("channels", $deviceId);
			$success = true;
			echo $stationCode;
			ParsePush::send(array(
			  "where" => $query,
			  "data" => array(
			    "alert" => "Train ". $trainNumber ." has departed station ".$stationCode
			  )
			));
			break;
		}else{
			$prev = $resultString;
		}
	}
	return $success;
}

function curl($url){
    $agent = 'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)';
    $curl = curl_init($url);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($curl, CURLOPT_USERAGENT, $agent);
	curl_setopt($curl, CURLOPT_TIMEOUT, 4000);
    $response = curl_exec($curl);
    curl_close($curl);
    return $response;
}

function getBetween($content,$start,$end){
    $r = explode($start, $content);
    if (isset($r[1])){
        $r = explode($end, $r[1]);
        return $r[0];
    }
    return '';
}
?>