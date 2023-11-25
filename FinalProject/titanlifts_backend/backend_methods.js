const { MongoClient } = require("mongodb");
const url = "mongodb://127.0.0.1:27017";
const dbName = "titanlifts";
const client = new MongoClient(url);
const db = client.db(dbName);

var express = require("express");
var cors = require("cors");
var app = express();
var fs = require("fs");
var bodyParser = require("body-parser");
const exp = require("constants");
app.use(cors());
app.use(bodyParser.json());
const port = "8081";
const host = "localhost";

app.listen(port, () => {
  console.log("App listening at http://%s:%s", host, port);
});

//Lifts

app.get("/listLifts", async (req, res) => {
  await client.connect();
  console.log("Node connected successfully to GET MongoDB");
  const query = {};
  const results = await db.collection("lifts").find(query).limit(100).toArray();
  console.log(results);
  res.status(200);
  res.send(results);
});


app.get("/lifts/:id", async (req, res) => {
    const liftid = Number(req.params.id);
    console.log("Lift to find :", liftid);
    await client.connect();
    console.log("Node connected successfully to GET-id MongoDB");
    const query = { id: liftid };
    const results = await db.collection("lifts").findOne(query);
    console.log("Results :", results);
    if (!results) res.send("Not Found").status(404);
    else res.send(results).status(200);
  });

  app.post("/addLift", async (req, res) =>{
    await client.connect();
    const keys = Object.keys(req.body);
    const values = Object.values(req.body);
    const id = values[0];
    const name = values[1];
    const category = values[2];
    const muscles = values[3];
    const imglink = values[4];
  
    console.log(id, name);
    const newDocument = {
  
      "id" : id,
      "name" : name,
      "category" : category,
      "muscles" : muscles,
      "imglink" : imglink
  
    };
  
    const results = await db.collection("lifts").insertOne(newDocument);
    res.status(200);
    res.send(results);
  
  })


//Users

app.get("/listUsers", async (req, res) => {
  await client.connect();
  console.log("Node connected successfully to GET MongoDB");
  const query = {};
  const results = await db.collection("users").find(query).limit(100).toArray();
  console.log(results);
  res.status(200);
  res.send(results);
});

app.get("/users/:id", async (req, res) => {
    const userid = Number(req.params.id);
    console.log("Lift to find :", userid);
    await client.connect();
    console.log("Node connected successfully to GET-id MongoDB");
    const query = { id: userid };
    const results = await db.collection("users").findOne(query);
    console.log("Results :", results);
    if (!results) res.send("Not Found").status(404);
    else res.send(results).status(200);
  });

  app.post("/addUser", async (req, res) =>{
    await client.connect();
    const keys = Object.keys(req.body);
    const values = Object.values(req.body);
    const id = values[0];
    const username = values[1];
    const firstname = values[2];
    const lastname = values[3];
    const password = values[4];
    const subscribed = values[5];
    const cardNumber = values[6];
    const expMonth = values[7];
    const expYear = values[8];
  
    console.log(id, username);
    const newDocument = {
  
      "id" : id,
      "username" : username,
      "firstname" : firstname,
      "lastname" : lastname,
      "password" : password,
      "subscribed" : subscribed,
      "cardNumber" : cardNumber,
      "expMonth" : expMonth,
      "expYear" : expYear
    };
  
    const results = await db.collection("users").insertOne(newDocument);
    res.status(200);
    res.send(results);
  
  });

//Workouts

app.get("/listWorkouts", async (req, res) => {
  await client.connect();
  console.log("Node connected successfully to GET MongoDB");
  const query = {};
  const results = await db.collection("workouts").find(query).limit(100).toArray();
  console.log(results);
  res.status(200);
  res.send(results);
});

app.get("/workouts/:id", async (req, res) => {
    const workoutid = Number(req.params.id);
    console.log("Lift to find :", workoutid);
    await client.connect();
    console.log("Node connected successfully to GET-id MongoDB");
    const query = { id: workoutid };
    const results = await db.collection("workouts").findOne(query);
    console.log("Results :", results);
    if (!results) res.send("Not Found").status(404);
    else res.send(results).status(200);
  });

  app.post("/addWorkout", async (req, res) =>{
    await client.connect();
    const keys = Object.keys(req.body);
    const values = Object.values(req.body);
    const id = values[0];
    const date = values[1];
    const category = values[2];
    const lifts = values[3];
  
    console.log(id, date);
    const newDocument = {
  
      "id" : id,
      "date" : date,
      "category" : category,
      "lifts" : lifts,
    };
  
    const results = await db.collection("workouts").insertOne(newDocument);
    res.status(200);
    res.send(results);
  
  })
