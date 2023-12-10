import React, { useState, useEffect } from "react";

const Account = ( { setIsLoggedIn, setShowFeed, setNewAcctClicked, setSignOutClicked, setShowAccount, setShowLogin, loggedInUserId} ) => {
  const [user, setUser] = useState("");

  const [isUpdateFormValid, setIsUpdateFormValid] = useState("");

  const [firstname, setFirstName] = useState("");
  const [lastname, setLastName] = useState("");
  const [username, setUsername] = useState("");

  const [firstNameValid, setFirstNameValid] = useState(null);
  const [lastNameValid, setLastNameValid] = useState(null);
  const [usernameValid, setUsernameValid] = useState(null);

  const [showUpdateForm, setShowUpdateForm] = useState(false);

  const [firstNameError, setFirstNameError] = useState("");
  const [lastNameError, setLastNameError] = useState("");
  const [usernameError, setUsernameError] = useState("");

  const [updatedUser, setUpdatedUser] = useState({
    firstname: "",
    lastname: "",
    username: "",
  });

  useEffect(() => {
    getUserById(loggedInUserId);
  }, []);

  function getUserById(id) {
    fetch("http://localhost:8081/users/" + id)
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setUser(data);
      });
  }

  const updateUser = async () => {
    const userId = loggedInUserId; // Update this with the actual user ID
    const updatedData = {
      firstname,
      lastname,
      username,
    };

    try {
      const response = await fetch(`http://localhost:8081/users/${userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedData),
      });

      if (response.ok) {
        
        getUserById(userId);
        // Additionally, you might want to set success messages or perform further actions here
      } else {
       
        console.error("Update failed:", response.statusText);
      }
    } catch (error) {
      console.error("Error updating user:", error);
    }
  };

  const handleUpdateClick = (e) => {
    e.preventDefault();
    setShowUpdateForm(true);
  };

  const handleUpdateFormSubmit = async (e) => {
    e.preventDefault();
    setShowUpdateForm(false);
    setFirstName("");
    setLastName("");
    setUsername("");
    setFirstNameValid(null);
    setLastNameValid(null);
    setUsernameValid(null);

    updateUser();
    
  };

  const handleUpdateFormReturn = (e) => {
    e.preventDefault();
    setShowUpdateForm(false);
    setFirstNameValid(null);
    setLastNameValid(null);
    setUsernameValid(null);

  };

  const handleSignOutClick = (e) => {
    e.preventDefault();
    setSignOutClicked(true);
    setIsLoggedIn(false);
    setShowFeed(false);
    setShowAccount(false);
    setNewAcctClicked(false);
    setShowLogin(true);

  }

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

  
    if (val) {
      setUpdatedUser({ firstname, lastname, username});
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
                  <button className="btn btn-danger" onClick={handleSignOutClick}>Sign Out</button>
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
