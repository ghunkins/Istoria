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
    $book->id = $book->best_book->id;
    $book->title = $book->best_book->title;
    $book->image_url = $book->best_book->image_url;
    $book->small_image_url = $book->best_book->small_image_url;
    $book->author_id = $book->best_book->author->id;
    $book->author_name = $book->best_book->author->name;

    //remove unneeded json entries
    unset($book->best_book);

    //get reviews from the embedded URL in another API call to get book details
    $xml = file_get_contents('https://www.goodreads.com/book/show/'.$book->id.'.xml?key=2E0T8ZFM9tqVVY9OaY5A&text_only=true');
    // $xml = str_replace(array("\n", "\r", "\t"), '', $xml);
    // $xml = trim(str_replace('"', "'", $xml));
    // $simple_xml = simplexml_load_string($xml);
    // $json = json_encode($simple_xml);
    // $json = json_decode($json);

    //this is really ugly and shouldn't exist, curse you GoodReads
    $dom = new DOMDocument;
    $dom->loadXML($xml);
    $widget = $dom->getElementsByTagName('reviews_widget')[0]->nodeValue;

    //here we extract the link for the widget embedd with slightly less 
    $startsAt = strpos($widget, "src=\"") + strlen("src=\"");
    $endsAt = strpos($widget, "\" width=", $startsAt);
    $result = substr($widget, $startsAt, $endsAt - $startsAt);

    //get the reviews page and extract the text of reviews from it
    $html = file_get_contents($result);

    $dom = new DOMDocument;
    $dom->loadHTML($html);
    $finder = new DomXPath($dom);
    $classname = "gr_review_container";
    $nodes = $finder->query("//*[contains(concat(' ', normalize-space(@class), ' '), ' $classname ')]");

    $reviews = array();

    //filter through the reviews and put them into our return object
    foreach($nodes as $href) {
        $review = array();
        //echo $href->childNodes->item(0)->nodeValue;  //ONLY PROBABLY WHITESPACE
        array_push($review, trim($href->childNodes->item(1)->nodeValue));  //USERNAMES PLUS WHITESPACE
        //echo $href->childNodes->item(2)->nodeValue;  //ONLY WHITESPACE
        //echo $href->childNodes->item(3)->nodeValue;  //ONLY A DOT
        //echo $href->childNodes->item(4)->nodeValue;  //ONLY WHITESPACE
        //echo $href->childNodes->item(5)->nodeValue;  //STARS
        //echo $href->childNodes->item(6)->nodeValue;  //ONLY WHITESPACE
        //echo $href->childNodes->item(7)->nodeValue;  //ONLY DOTS
        //echo $href->childNodes->item(8)->nodeValue;  //ONLY WHITESPACE
        //echo $href->childNodes->item(9)->nodeValue;  //DATES AND WHITESPACE
        //echo $href->childNodes->item(10)->nodeValue;  //ONLY WHITESPACES
        //echo $href->childNodes->item(11)->nodeValue;  //ACTUAL REVIEWS

        array_push($reviews, $review);
    }

    echo $reviews[0][0];

	header('Content-Type: application/json');
	//echo json_encode($search_request);
	echo json_encode($book);
?>
