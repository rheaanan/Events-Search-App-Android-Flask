// Copyright 2017 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

'use strict';

// [START gae_node_request_example]
const express = require('express');

const app = express();

// app.get('/getData',(req, res) =>{

// 	axios.get('https://app.ticketmaster.com/discovery/v2/suggest?apikey=9y0GiDttpsVbJMpqziz7C4Z02gfnjOfN&keyword=laker')
// 	  .then(response => {
// 	    console.log(response.data);
// 	    console.log(response.data.explanation);
// 	    res.json(response.data)
// 	  })
// 	  .catch(error => {
// 	    console.log(error);
// 	});
	
// })
var SpotifyWebApi = require('spotify-web-api-node');


var spotifyApi = new SpotifyWebApi({
	clientId: '3479eeb787254bd7b6c53b7eac535bae',
	clientSecret: '6ab133aab1d24008815c83467099d22b',
	redirectUri: 'http://www.example.com/callback'
  });
var spot_event_obj = {}
var func_map = {'Date':'date', 'EventName':'event_name','EventId':'event_id', 'Category': 'category', 'Venue':'venue'}
var func_map2 = {'Date':'date', 'Artist':'artist', 'Venue':'venue', 'Genres':'genre','Price':'price_ranges','Ticket':'ticket_status' ,'Buy': 'buy_ticket','Seatmap':'seatmap'}
var func_map3 = {'Address':'address', 'City':'city', 'Phone':'phone_number', 'Open':'open_hours','General':'general_rule','Child':'child_rule','Lat':'lat','Lng':'lng' }
var func_map4 = {'Name':'name', 'Followers':'followers', 'Popularity':'popularity', 'Check':'check' }


var myFuncs3 = {
  address: function (event) { try{var text = event['address']['line1']; return text} catch(err){var text = 'N/A'; console.log(err); return text}},
  //artist: function (event) { try{var text=""; var names = [];  var attr = event['_embedded']['attractions']; for (var i=0;i<attr.length;i++){ var artist_obj ={};artist_obj[attr[i]["name"]]=attr[i]["url"] ; names.push(artist_obj)};return names} catch(err){console.log(err);  return []}},
  city: function (event) { try{var text= event['city']['name']+", "+event['state']['name'];return text} catch(err){var text = 'N/A'; console.log(err); return text}},
  phone_number: function (event) { try{var text = event['boxOfficeInfo']['phoneNumberDetail']; return text} catch(err){var text = 'N/A'; console.log(err); return text}},
  open_hours: function (event) { try{var text = event['boxOfficeInfo']['openHoursDetail']; return text} catch(err){var text = 'N/A'; console.log(err); return text}},
  general_rule: function (event) { try{var text = event['generalInfo']['generalRule']; return text} catch(err){var text = 'N/A'; console.log(err); return text}},
  child_rule: function (event) { try{var text = event['generalInfo']['childRule']; return text} catch(err){var text = 'N/A'; console.log(err); return text}},
  lat: function (event) { try{var text = event['location']['latitude']; return text} catch(err){var text = 'N/A'; console.log(err); return text}},
  lng: function (event) { try{var text = event['location']['longitude']; return text} catch(err){var text = 'N/A'; console.log(err); return text}}
};


var myFuncs2 = {
  date: function (event) { try{var text = event['dates']['start']['localDate']; return text} catch(err){var text = 'N/A'; console.log(err); return text}},
  //artist: function (event) { try{var text=""; var names = [];  var attr = event['_embedded']['attractions']; for (var i=0;i<attr.length;i++){ var artist_obj ={};artist_obj[attr[i]["name"]]=attr[i]["url"] ; names.push(artist_obj)};return names} catch(err){console.log(err);  return []}},
  artist: function (event) { try{var text=""; var names = [];  var attr = event['_embedded']['attractions']; for (var i=0;i<attr.length;i++){ names.push(attr[i]["name"])};return names} catch(err){console.log(err);  return []}},
  venue: function (event) { try{var names = []; names.push(event["_embedded"]["venues"][0]['name']); names.push(event["_embedded"]["venues"][0]['id']); return names}catch(err){console.log(err); return []}},
  genre: function (event) { try{var genre_parts = ['subGenre','genre','segment','subType','type']; var names =[]; for (var i=0;i<genre_parts.length;i++){try{if(event['classifications'][0][genre_parts[i]]['name']!== "Undefined"){names.push(event['classifications'][0][genre_parts[i]]['name']);}}catch(err){console.log(err); }};text=names.join(" | "); return text}catch(err){var text="N/A"; console.log(err); return text}},
  price_ranges: function (event) { try{var text = event['priceRanges'][0]['min'] + "-"+event['priceRanges'][0]['max']+" USD"; return text} catch(err){var text="N/A"; console.log(err);  return text}},
  ticket_status: function (event) { try{var text = event['dates']["status"]["code"]; return text}catch(err){var text="N/A"; return text}},
  buy_ticket: function (event) { try{var text = event['url']; return text} catch(err){var text="N/A"; return text}},
  seatmap: function (event) { try{var text = event["seatmap"]["staticUrl"]; return text}catch(err){var text="N/A"; return text}}
};

var myFuncs = {
  date: function (event) { try{var text = event['dates']['start']['localDate']; return text } catch(err){var text = 'N/A'; console.log(err); return text}},
  event_name: function (event) { try{var text = event['name']; return text}catch(err){var text="N/A"; return text} },
  event_id: function (event) { try{var text = event['id']; return text}catch(err){var text="N/A"; return text} },
  category: function (event) { try{var genre_parts = ['subGenre','genre','segment','subType','type']; var names =[]; for (var i=0;i<genre_parts.length;i++){try{if(event['classifications'][0][genre_parts[i]]['name']!== "Undefined"){names.push(event['classifications'][0][genre_parts[i]]['name']);}}catch(err){console.log(err); }};text=names.join(" | "); return text}catch(err){var text="N/A"; console.log(err); return text}},
  venue: function (event) { try{var text = event["_embedded"]["venues"][0]['name']; return text} catch(err){var text="N/A";console.log(err);  return text} }
};

var myFuncs4 = {
	name: function (event) { try{var text = event['artists']['items'][0]['name']; return text } catch(err){var text = 'N/A'; console.log(err); return text}},
	followers: function (event) { try{var text = event['artists']['items'][0]['followers']['total']; return text}catch(err){console.log(err);var text="N/A"; return text} },
	popularity: function (event) { try{var text = event['artists']['items'][0]['popularity']; return text}catch(err){console.log(err);var text="N/A"; return text} },
	check: function (event) { try{var text = event['artists']['items'][0]['external_urls']; return text}catch(err){console.log(err);var text="N/A"; return text} },

  };

function get_from_json(attr, event){
    var funcToRun = func_map[attr];
    return myFuncs[funcToRun](event);
}

function get_from_json2(attr, event){
    var funcToRun = func_map2[attr];
    return myFuncs2[funcToRun](event);
}
function get_from_json3(attr, event){
    var funcToRun = func_map3[attr];
    return myFuncs3[funcToRun](event);
}
function get_from_json4(attr, event){
    var funcToRun = func_map4[attr];
    return myFuncs4[funcToRun](event);
}

app.get('/getAutoComplete',(req, res) =>{
	console.log('getting keyword', req.query.keyword)
	axios.get('https://app.ticketmaster.com/discovery/v2/suggest?apikey=9y0GiDttpsVbJMpqziz7C4Z02gfnjOfN&keyword='+req.query.keyword)
	  .then(response => {
	  	var attractions = response.data['_embedded']['attractions']
	  	var names = []
	  	for (var i = 0; i < attractions.length; i++) {
		   names.push(attractions[i]['name'])
		}  
	  	console.log(names)
	    res.json({'names':names})
	  })
	  .catch(error => {
	    console.log(error);
	});
	
})


app.get('/getResults',(req, res) =>{
	//geohash_code = geohash.encode(request.args.get('lat'), request.args.get('lng'), 7)
	var url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=9y0GiDttpsVbJMpqziz7C4Z02gfnjOfN"
	var category_map = {'music':'KZFzniwnSyZfZ7v7nJ', 'sports':'KZFzniwnSyZfZ7v7nE', 'arts':'KZFzniwnSyZfZ7v7na', 'film': 'KZFzniwnSyZfZ7v7nn',
	'misc':'KZFzniwnSyZfZ7v7n1'}
	
	console.log(req.query,category_map[req.query.category])
	var geohash = require('ngeohash');
	var ghash = geohash.encode(req.query.lat, req.query.lng)
    const params = {
        'keyword': req.query.keyword,
        'radius': req.query.distance,
        'unit': req.query.unit,
        'geoPoint': ghash
    }
	if (req.query.category!='all'){
		params['segmentId']=category_map[req.query.category]
	}
    for (const [key, value] of Object.entries(params)) {
  		console.log(key, value);
  		url+="&"+key+"="+value
	}
	console.log('ticketmasterurl', url)        
	axios.get(url)
	  .then(response => {
	  	try{
		  	var header_row = ['Date' ,'Event', 'Category', 'Venue'];
		 	var events = response.data['_embedded']['events']
		  	var ret_array = []
		  	var event_obj = {}
	        for (var i = 0; i < events.length; i++){
	        	event_obj = {}
	            for (var j = 0; j < header_row.length; j++){
	            	if (header_row[j] == 'Event'){
	            		var value = get_from_json('EventName',events[i]);
	            		event_obj['EventName'] = value
	            		var value = get_from_json('EventId',events[i]);
	            		event_obj['EventId'] = value
	            	}

	            	else{
		                var value = get_from_json(header_row[j],events[i]);
		                event_obj[header_row[j]] = value
		            }
	            }
	            console.log(events[i]['name'], event_obj)
	            ret_array.push(event_obj)

	        }
			ret_array.sort(function(a, b){
				console.log('hello',a['Date'],b['Date'])
				var dateA=new Date(a['Date']), dateB=new Date(b['Date'])
				return ( dateA-dateB )
			})
	        res.json({eventsArr: ret_array})
    	} catch(err){
    		console.log("error", err)
    		res.json({eventsArr: []})
    	}
	    
	  })
	  .catch(error => {
	    console.log(error);
	});
	
})

app.get('/getEventDetails',(req, res) => {
	console.log('getting fetch details id call with',req.query.id)
	console.log('calling api with url ',"https://app.ticketmaster.com/discovery/v2/events/"+req.query.id+"?apikey=9y0GiDttpsVbJMpqziz7C4Z02gfnjOfN")
	axios.get("https://app.ticketmaster.com/discovery/v2/events/"+req.query.id+"?apikey=9y0GiDttpsVbJMpqziz7C4Z02gfnjOfN")
	  .then(response => {
	  	try{
	  		var event_obj = {}
	  		var headings = ['Date' ,'Artist', 'Venue', 'Genres', 'Price', 'Ticket', 'Buy', 'Seatmap']
	  		for (var i = 0; i < headings.length; i++){
	  			if(headings[i]=='Artist'){
	  				console.log("calling gettingjson2 with"+headings[i])
	            	var ret_array = get_from_json2(headings[i],response.data);
	            	if (ret_array.length!== 0){
	            		event_obj[headings[i]] = ret_array
	            	}
	            }
	            else if(headings[i]=='Venue'){
	  				console.log("calling gettingjson2 with"+headings[i])
	            	var ret_array = get_from_json2(headings[i],response.data);
	            	if (ret_array.length!== 0){
	            		event_obj[headings[i]] = ret_array
	            	}
	  			}
	  			else{
	  				console.log("calling gettingjson2 with"+headings[i])
	            	var value = get_from_json2(headings[i],response.data);
	            	if (value!=="N/A"){
	            		event_obj[headings[i]] = value
	            	}
	            }


        	}
        	console.log(event_obj)
        	res.json(event_obj)
	  	}catch(err){
	  		console.log("error occured",err)
	  		res.json({})
	  	}
	    
	  })
	  .catch(error => {
	    console.log(error);
	});

})


app.get("/spotify", (req, res) => {
  
	console.log("spot",req.query.artist);
	var spotifyApi = new SpotifyWebApi({
	  clientId: "3479eeb787254bd7b6c53b7eac535bae",
	  clientSecret: "6ab133aab1d24008815c83467099d22b",
	});
  
	spotifyApi.clientCredentialsGrant().then(
	  function(data) {
		console.log('The access token expires in ' + data.body['expires_in']);
		console.log('The access token is ' + data.body['access_token']);
	
		// Save the access token so that it's used in future calls
		spotifyApi.setAccessToken(data.body['access_token']);
		
		spotifyApi.searchArtists(req.query.artist).then(
		  function (data) {
			try{
				var spot_event_obj = {}
				var headings = ['Name','Followers','Popularity','Check']
				for (var i = 0; i < headings.length; i++){
					console.log("calling gettingjson3 with"+headings[i])
				  var value = get_from_json4(headings[i],data.body);
				  if (value!=="N/A"){
					  spot_event_obj[headings[i]] = value
				  }
			  }
			  console.log("from get spot",spot_event_obj)
			  res.json(spot_event_obj)
			}catch(err){
				console.log("error occured",err)
			}
		  },
		  function (err) {
			console.error(err);
		  }
		);
	  },
	  function(err) {
		console.log('Something went wrong when retrieving an access token', err);
	  }
	);
  
  
  });
		
	

app.get('/getVenueDetails',(req, res) => {
	console.log('getting fetch details id call with',req.query.id)
	var url= "https://app.ticketmaster.com/discovery/v2/venues/"+req.query.id+".json?apikey=9y0GiDttpsVbJMpqziz7C4Z02gfnjOfN"
	console.log('calling api with url ',url)
	axios.get(url)
	  .then(response => {
	  	try{
	  	    var event_obj = {}
	  		var headings = ['Address','City','Phone','Open', 'General', 'Child','Lat','Lng']
	  		for (var i = 0; i < headings.length; i++){
	  			
  				console.log("calling gettingjson3 with"+headings[i])
            	var value = get_from_json3(headings[i],response.data);
            	if (value!=="N/A"){
            		event_obj[headings[i]] = value
            	}
	        }
        	console.log(event_obj)
       	res.json(event_obj)
	  	}catch(err){
	  		console.log("error occured",err)
	  		res.json({})
	  	}
	    
	  })
	  .catch(error => {
	    console.log(error);
	});

})

app.get('/getLocation',(req, res) => {
	console.log('getting loc call with',req.query.location)
	axios.get("https://maps.googleapis.com/maps/api/geocode/json?address="+req.query.location+"&key=AIzaSyC1IQkjNRBcenezJ8ukQEZXrXZe1t3I9jQ")
	  .then(response => {

	    var returned_data1 = response.data['results'][0]['geometry']['location'];
    	var latitude = returned_data1['lat'];
    	var longitude = returned_data1['lng'];
    	res.json({'latitude':latitude,
    		'longitude':longitude
    	})
	  })
	  .catch(error => {
	    console.log(error);
	});

})


app.listen(3000, (req, res) =>{
	console.log('Express API  is running at port 3000')

})

app.get('/', (req, res) => {
  res.status(200).send('Hello, world! Rhea').end();
});

const axios = require('axios');
// Start the server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`App listening on port ${PORT}`);
  console.log('Press Ctrl+C to quit.');
});
// [END gae_node_request_example]

const path = require('path');
app.use(express.static(path.join(__dirname,'/dist/check-ticket')));

app.use('/*', (req, res) => {
	res.sendFile(path.join(__dirname+'/dist/check-ticket/index.html'));
})
module.exports = app;
