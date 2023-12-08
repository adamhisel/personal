import React, { useState } from "react";
import ReactDOM from "react-dom";
import "bootstrap/dist/css/bootstrap.css";
import Demos from "./demos.js";
import Account from "./account.js";
import Navbar from "./navbar.js";
import Authors from "./authors.js";
import "./index.css";

const App = () => {
  const [showDemos, setShowDemos] = useState(false);
  const [showAccount, setShowAccount] = useState(false);
  const [showAuthors, setShowAuthors] = useState(false);

  return (
    <div>
      <Navbar
        setShowDemos={setShowDemos}
        setShowAccount={setShowAccount}
        setShowAuthors={setShowAuthors}
      />
      {showDemos && <Demos />}
      {showAccount && <Account />}
      {showAuthors && <Authors />}
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