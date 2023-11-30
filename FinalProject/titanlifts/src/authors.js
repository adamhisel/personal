import React from "react";
import Adam from "./images/adam.jpg";
import Adomale from "./images/adomale.JPG";

const Authors = () => {
  return (
    <div className="app-container">
      <div className="text-center">
        <h1 className="p-3 mb-3">Authors</h1>
      </div>
      <div className="p-3 mb-3 rounded bg-body-secondary">
        <div className="row">
          <div className="col-md-6 mb-4">
            <div className="d-flex flex-column align-items-center">
              <img
                src={Adomale}
                width="250"
                height="250"
                className="mb-3 rounded-circle"
                alt="Adomale Kiobel"
              />
              <h3>Adomale Kiobel</h3>
              <p>amkiobel@iastate.edu</p>
              <a href="https://jamdrate.github.io/personal/Index.html" target="_blank">About Me</a>
            </div>
          </div>

          <div className="col-md-6 mb-4">
            <div className="d-flex flex-column align-items-center">
              <img
                src={Adam}
                width="250"
                height="250"
                className="mb-3 rounded-circle"
                alt="Adam Hisel"
              />
              <h3>Adam Hisel</h3>
              <p>ahisel@iastate.edu</p>
              <a href="https://adamhisel.github.io/personal/" target="_blank">About Me</a>
            </div>
          </div>
        </div>
        <div className="text-center">
          <h2 className="p-3 mb-3">
            SE/ComS319 Construction of User Interfaces, Fall 2023
          </h2>
          <h2>Dr. Abraham N. Aldaco Gastelum </h2>
          <h2>Email: aaldaco@iastate.edu</h2>
          <h2 className="p-3 mb-3">November 29th, 2023</h2>
        </div>
      </div>
    </div>
  );
};

export default Authors;