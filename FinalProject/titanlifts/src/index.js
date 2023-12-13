import React, { useState , useEffect} from "react";
import ReactDOM from "react-dom";
import "bootstrap/dist/css/bootstrap.css";
import Demos from "./demos.js";
import Account from "./account.js";
import Navbar from "./navbar.js";
import Authors from "./authors.js";
import Feed from "./feed.js";
import Login from "./login.js";
import CreateAccount from "./newAccount.js";
import "./index.css";

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showFeed, setShowFeed] = useState(false);
  const [showDemos, setShowDemos] = useState(false);
  const [showAccount, setShowAccount] = useState(false);
  const [showAuthors, setShowAuthors] = useState(false);
  const [showLogin, setShowLogin] = useState(false);
  const [newAcctClicked, setNewAcctClicked] = useState(false);
  const [signOutClicked, setSignOutClicked] = useState(false);
  const [loggedInUserId, setLoggedInUserId] = useState(0);



  useEffect(() => {
    if (signOutClicked) {
      setIsLoggedIn(false);
      setShowFeed(false);
      setShowAccount(false);
      setNewAcctClicked(false);
      setShowLogin(true); // Ensure the login page is shown after sign out
      setSignOutClicked(false);
    }
  }, [signOutClicked]);

  useEffect(() => {
    if (newAcctClicked) {
      setShowLogin(false);
    }
  }, [newAcctClicked]);

  return (
    <div>
      {isLoggedIn && (
        <Navbar
          setShowFeed={setShowFeed}
          setShowDemos={setShowDemos}
          setShowAccount={setShowAccount}
          setShowAuthors={setShowAuthors}
        />
      )}
      <div className={isLoggedIn ? "content-wrapper" : ""}>
        {isLoggedIn ? (
          <>
            {showFeed && <Feed loggedInUserId={loggedInUserId} />}
            {showDemos && <Demos />}
            {showAccount && (
              <Account
                setIsLoggedIn={setIsLoggedIn}
                setShowFeed={setShowFeed}
                setNewAcctClicked={setNewAcctClicked}
                setSignOutClicked={setSignOutClicked}
                setShowAccount={setShowAccount}
                setShowLogin={setShowLogin}
                loggedInUserId = {loggedInUserId}
              />
            )}
            {showAuthors && <Authors />}
          </>
        ) : signOutClicked ? (
          <Login
            setIsLoggedIn={setIsLoggedIn}
            setShowFeed={setShowFeed}
            setNewAcctClicked={setNewAcctClicked}
            setShowLogin={setShowLogin}
            setLoggedInUserId={setLoggedInUserId}
          />
        ) : newAcctClicked ? (
          <CreateAccount
            setIsLoggedIn={setIsLoggedIn}
            setShowFeed={setShowFeed}
            setShowLogin={setShowLogin}
            setNewAcctClicked={setNewAcctClicked}
            setLoggedInUserId={setLoggedInUserId}
          />
        ) : showLogin ? (
          <Login
            setIsLoggedIn={setIsLoggedIn}
            setShowFeed={setShowFeed}
            setNewAcctClicked={setNewAcctClicked}
            setShowLogin={setShowLogin}
            setLoggedInUserId={setLoggedInUserId}
          />
        ) : (
          <Login
            setIsLoggedIn={setIsLoggedIn}
            setShowFeed={setShowFeed}
            setNewAcctClicked={setNewAcctClicked}
            setShowLogin={setShowLogin}
            setLoggedInUserId={setLoggedInUserId}
          />
        )}
      </div>
    </div>
  );
};

const root = document.getElementById("root");

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  root
);
