import React from "react";
import myImage from "./images/TitanLiftsLogo.png";
import "./Navbar.css";

const Navbar = ({ setShowDemos, setShowAccount, setShowAuthors }) => {
  const handleDemosClick = () => {
    setShowDemos(true);
    setShowAuthors(false);
    setShowAccount(false);
  };

  const handleAccountClick = () => {
    setShowDemos(false);
    setShowAuthors(false);
    setShowAccount(true);
  };

  const handleAuthorsClick = () => {
    setShowAccount(false);
    setShowDemos(false);
    setShowAuthors(true);
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark nav-bg-color">
      <div className="container">
        <a className="navbar-brand" href="#" /*onClick={handleAccountClick}*/>
          <img src={myImage} width="80%" alt="Logo"></img>
        </a>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <div className="d-flex justify-content-center w-100">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <a className="nav-link nav-text-color" href="#">
                  Personal Feed
                </a>
              </li>
              <li className="nav-item">
                <a
                  className="nav-link nav-text-color"
                  href="#"
                  onClick={handleDemosClick}
                >
                  Demos
                </a>
              </li>
              <li className="nav-item">
                <a
                  className="nav-link nav-text-color"
                  href="#"
                  onClick={handleAccountClick}
                >
                  Account
                </a>
              </li>
              <li>
                <a
                  className="nav-link nav-text-color"
                  href="#"
                  onClick={handleAuthorsClick}
                >
                  Authors
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
