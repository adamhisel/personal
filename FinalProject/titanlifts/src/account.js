import React, { useState, useEffect } from "react";

const Account = () => {
  const [user, setUser] = useState("");

  const [isUpdateFormValid, setIsUpdateFormValid] = useState("");

  const [firstname, setFirstName] = useState("");
  const [lastname, setLastName] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");

  const [firstNameValid, setFirstNameValid] = useState(null);
  const [lastNameValid, setLastNameValid] = useState(null);
  const [usernameValid, setUsernameValid] = useState(null);
  const [emailValid, setEmailValid] = useState(null);

  const [showUpdateForm, setShowUpdateForm] = useState(false);

  const [firstNameError, setFirstNameError] = useState("");
  const [lastNameError, setLastNameError] = useState("");
  const [usernameError, setUsernameError] = useState("");
  const [emailError, setEmailError] = useState("");

  const [updatedUser, setUpdatedUser] = useState({
    firstname: "",
    lastname: "",
    username: "",
    email: ""
  });

  useEffect(() => {
    getUserById(1);
  }, []);

  function getUserById(id) {
    fetch("http://localhost:8081/users/" + id)
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setUser(data);
      });
  }

  const handleUpdateClick = (e) => {
    e.preventDefault();
    setShowUpdateForm(true);
  };

  const handleUpdateFormSubmit = (e) => {
    e.preventDefault();
    setShowUpdateForm(false);
    setFirstName("");
    setLastName("");
    setUsername("");
    setEmail("");
    setFirstNameValid(null);
    setLastNameValid(null);
    setUsernameValid(null);
    setEmailValid(null);
  };

  const handleUpdateFormReturn = (e) => {
    e.preventDefault();
    setShowUpdateForm(false);
    setFirstNameValid(null);
    setLastNameValid(null);
    setUsernameValid(null);
    setEmailValid(null);
  };

  const isValid = () => {
    let val = true;
  
    if (firstname.trim() === "") {
      setFirstNameValid(false);
      setFirstNameError("Must type a first name");
      val = false;
    } else {
      setFirstNameValid(true);
    }
  
    if (lastname.trim() === "") {
      setLastNameValid(false);
      setLastNameError("Must type a last name");
      val = false;
    } else {
      setLastNameValid(true);
    }
  
    if (username.trim() === "") {
      setUsernameValid(false);
      setUsernameError("Must type a username");
      val = false;
    } else {
      setUsernameValid(true);
    }
  
    if (!email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
      setEmailValid(false);
      setEmailError("Must provide a valid email like abcd@something.com");
      val = false;
    } else {
      setEmailValid(true);
    }
  
    if (val) {
      setUpdatedUser({ firstname, lastname, username, email });
      setIsUpdateFormValid(true);
    }
    
    return val;
  };

  const form = (
    <div className="card mb-3 h-100">
      <div className="card-body">
        <h1 className="card-title">Update Information</h1>
        <form  onSubmit={(e) => {
                  e.preventDefault();
                  if(isValid()){
                    
                    handleUpdateFormSubmit(e);
                    //updateUser()
                  
                  }
                }}>
          <div className="mb-3">
            <label htmlFor="firstName">First Name</label>
            <input
              type="text"
              className={`form-control ${firstNameValid === false ? "is-invalid" : ""}`}
              id="firstName"
              value={firstname}
              onChange={(e) => setFirstName(e.target.value)}
            />
            {!firstNameValid && (
                <div className="invalid-feedback">{firstNameError}</div>
              )}
          </div>
          <div className="mb-3">
            <label htmlFor="lastname">Last Name</label>
            <input
              type="text"
              className={`form-control ${lastNameValid === false ? "is-invalid" : ""}`}
              id="lastName"
              value={lastname}
              onChange={(e) => setLastName(e.target.value)}
              />
    
              {!lastNameValid && (
                <div className="invalid-feedback">{lastNameError}</div>
              )}
          </div>
          <div className="mb-3">
            <label htmlFor="username">User Name</label>
            <input
              type="text"
              className={`form-control ${usernameValid === false ? "is-invalid" : ""}`}
              id="userName"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              />
    
              {!usernameValid && (
                <div className="invalid-feedback">{usernameError}</div>
              )}
          </div>
          <div className="mb-3">
            <label htmlFor="email">Email</label>
            <input
              type="text"
              className={`form-control ${emailValid === false ? "is-invalid" : ""}`}
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              />
    
              {!emailValid && (
                <div className="invalid-feedback">{emailError}</div>
              )}
          </div>
          <button type="submit" className="btn btn-primary">
            Save Changes
          </button>
          <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={handleUpdateFormReturn}
                  >
                    <i className="bi bi-arrow-left-circle"></i> Return
                  </button>
        </form>
      </div>
    </div>
  );

  return (
    <div className="container py-4">
      <div className="row">
        <div className="col-md-6 ">
          <div className="card mb-3 h-100">
            <div className="card-body">
              <h1 className="card-title">Personal Information</h1>
              <p className="card-text">
                <strong>Name:</strong> {user.firstname} {user.lastname}
              </p>
              <p className="card-text">
                <strong>Username:</strong> {user.username}
              </p>
              <p className="card-text">
                <strong>Email:</strong> {user.email}
              </p>
            </div>
          </div>
        </div>
        <div className="col-md-6">
          {showUpdateForm ? (
            form
          ) : (
            <div className="card mb-3 h-100">
              <div className="card-body">
                <h1 className="card-title">Settings</h1>
                <p className="card-text">
                  <button className="btn btn-primary" onClick={handleUpdateClick}>
                    Update Information
                  </button>
                </p>
                <p className="card-text">
                  <button className="btn btn-danger">Sign Out</button>
                </p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Account;
