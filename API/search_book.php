<?php //search book by title and return the first book suggestion and return ratings, author, picture, number of ratings, list of ratings.  Takes a string as input

	//get the query string and make our api request to goodreads with the secret api key
	$query = $_GET['query'];
	$xml = file_get_contents('https://www.goodreads.com/search/index.xml?key=2E0T8ZFM9tqVVY9OaY5A&q='.urlencode($query));

	//converting the response to json
    $xml = str_replace(array("\n", "\r", "\t"), '', $xml);
    $xml = trim(str_replace('"', "'", $xml));
    $simple_xml = simplexml_load_string($xml);
    $json = json_encode($simple_xml);
    $json = json_decode($json);

    //select first book
    $book = $json->search->results->work[0];

    //move information around into a more readable and simple state
    $book->title = $book->best_book->title;
    $book->image_url = $book->best_book->image_url;
    $book->small_image_url = $book->best_book->small_image_url;
    $book->author_id = $book->best_book->author->id;
    $book->author_name = $book->best_book->author->name;

    //remove unneeded json entries
    unset($book->best_book);

	header('Content-Type: application/json');
	//echo json_encode($search_request);
	echo json_encode($book);
?>
