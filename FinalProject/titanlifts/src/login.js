import React from "react";
import ReactDOM from "react-dom";
import "bootstrap/dist/css/bootstrap.css";
import Lift from "./demos.js";
import App from "./App.js";

// login

function confirmLogin() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    if (username === 'username' && password === 'password') {
        alert("Login successful");
        return true;
    }
    else if ((username.trim() === '') || (password.trim() === '')) {
        alert("You cannot leave username and/or password blank");
        return false;
    } else {
        alert("Incorrect");
        return false;
    }
}

function createAccount() {
    var username = document.getElementById("username");
    var password = document.getElementById("password");




}