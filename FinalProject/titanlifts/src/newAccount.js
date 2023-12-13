import React, { useState, useEffect } from "react";
import myImage from "./images/TitanLiftsLogo.png";
import "./login.css";

const CreateAccount = ({
  setIsLoggedIn,
  setShowFeed,
  setShowLogin,
  setNewAcctClicked,
  setLoggedInUserId,
}) => {
  const [allUsers, setAllUsers] = useState([]);
  const [username, setUsername] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [idCounter, setIdCounter] = useState(0);

  useEffect(() => {
    getUserMethod();
  }, []);

  const handleBackClick = (e) => {
    e.preventDefault();
    setNewAcctClicked(false);
    setShowLogin(true);
  };

  const getUserMethod = () => {
    fetch("http://localhost:8081/listUsers")
      .then((response) => response.json())
      .then((data) => {
        if (Array.isArray(data)) {
          setAllUsers(data);
          setIdCounter(data.length + 1);

          const mostRecentUser = data.reduce((prev, current) =>
            prev.id > current.id ? prev : current
          );

          setLoggedInUserId(mostRecentUser.id);
        } else {
          console.error("Invalid data format received for users:", data);
        }
      })
      .catch((error) => {
        console.error("Error fetching users:", error);
      });
  };

  const confirmCreateAccount = (e) => {
    e.preventDefault();
    postUserMethod();
  };

  const postUserMethod = () => {
    fetch("http://localhost:8081/addUser", {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({
        id: idCounter,
        username: username.trim(),
        firstname: firstName.trim(),
        lastname: lastName.trim(),
        password: password.trim(),
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        setSuccess("Account Creation Successful");
        getUserMethod();
        setIsLoggedIn(true);
        setShowFeed(true);
      })
      .catch((error) => {
        console.error("Error posting user:", error);
      });
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <img src={myImage} width="80%" alt="Logo" className="pb-2" />
        <h4 className="pb-2">Create Account</h4>
        <form className="login-form" onSubmit={confirmCreateAccount}>
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
              type="text"
              id="firstName"
              name="firstName"
              placeholder="First Name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              required
            />
          </div>
          <div>
            <input
              type="text"
              id="lastName"
              name="lastName"
              placeholder="Last Name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
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
            <button type="submit">Create Account</button>
          </div>
          <div className="pb-3">
            <button onClick={handleBackClick}>Back</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateAccount;
