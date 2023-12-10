import React, { useState, useEffect } from "react";
import myImage from "./images/TitanLiftsLogo.png";
import "./login.css"; 

const Login = ( { setIsLoggedIn, setShowFeed, setNewAcctClicked, setShowLogin, setLoggedInUserId} ) => {
  const [allUsers, setAllUsers] = useState([]);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    getUserMethod();
  }, []);

  const confirmLogin = (e) => {
    e.preventDefault();
    const userExists = allUsers.find(
        (user) => user.username === username && user.password === password
      );
  
      if (userExists) {
        setIsLoggedIn(true);
        setShowFeed(true);
        setSuccess("Login successful!");

        setLoggedInUserId(userExists.id);
      } else {
        setError("Invalid username or password");
      }
  };

  
  function getUserMethod() {
    fetch("http://localhost:8081/listUsers")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        if (Array.isArray(data)) {
          setAllUsers(data);
        } else {
          console.error("Invalid data format received for lifts:", data);
        }
      })
      .catch((error) => {
        console.error("Error fetching lifts:", error);
      });
  }



  const createAccount = () => {
    setShowLogin(false);
    setNewAcctClicked(true);
    console.log("Create Account clicked");
  };

  return (
    <div className="login-page">
    <div className="login-container">
      <img src={myImage} width="80%" alt="Logo" className="pb-2"></img>
      <h4 className="pb-2">Login</h4>
      <form className="login-form" onSubmit= {confirmLogin}>
        <div>
          <input
            type="text"
            id="username"
            name="username"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <input
            type="password"
            id="password"
            name="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <br />
          <span id="error" className="login-error">
            {error}
          </span>
          <span id="success" className="login-success">
            {success}
          </span>
        </div>
        <div className="pb-3">
          <button type="submit">Enter</button>
        </div>
        <div>
          <button
            type="button"
            className="create-account-btn"
            onClick={createAccount}
          >
            Create Account
          </button>
        </div>
      </form>
    </div>
    </div>
  );
};

export default Login;
