## Android simple get/post request via Volley

### URL

Use `10.0.2.2` instead of `localhost` IF the server program is running on the same host as the Android Enmulator

### AndroidManifest.xml

- `<uses-permission android:name="android.permission.INTERNET" />` before `<application>`
- `android:usesCleartextTraffic="true"` inside `<application` before `<activity>` if using HTTP

### Example helper method for GET:
```
private void getRequest() {
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this);

    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // String response can be converted to JSONObject via
                // JSONObject object = new JSONObject(response);
                tvResponse.setText("Response is: "+ response);
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvResponse.setText("That didn't work!" + error.toString());
            }
    });

    // Add the request to the RequestQueue.
    queue.add(stringRequest);
    Toast.makeText(getApplicationContext(),method+" request sent!",Toast.LENGTH_SHORT).show();
}
```

#### works with controllers like:
```
@GetMapping("trivia/all")
List<Trivia> getAllTrivias(){
    return triviaRepository.findAll();
}

@GetMapping("trivia/{id}")
Trivia getByQuestion(@PathVariable Long id){

    Optional<Trivia> optinalEntity =  triviaRepository.findById(id);
    Trivia trivia = optinalEntity.get();

    return trivia;
}
```

### Example helper method for POST:
```
private void postRequest() {
    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

    // Convert input to JSONObject
    JSONObject body = null;
    try{
        // etRequest should contain a JSON object string as your POST body
        // similar to what you would have in POSTMAN-body field
        // and the fields should match with the object structure of @RequestBody on sb
        body = new JSONObject(etRequest.getText().toString());
    } catch (Exception e){
        e.printStackTrace();
    }

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                tvResponse.setText(response.toString());
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }
    );
    queue.add(request); // send request
}
```

#### works with controllers like:
```
@PostMapping("trivia/post")
Trivia postTriviaByPath(@RequestBody Trivia newTrivia){
    triviaRepository.save(newTrivia);
    return newTrivia;
}
```
