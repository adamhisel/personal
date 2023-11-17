import React, { useState, useEffect } from "react";
import "./demos.css";

const Lift = () => {
  const [query, setQuery] = useState("");
  const [lifts, setLifts] = useState([]);
  
  // Fetch lifts from MongoDB when the component mounts
  useEffect(() => {
    getLiftMethod();
  }, []);

  const handleChange = (e) => {
    setQuery(e.target.value);
  };

  const filteredLifts = lifts.filter((lift) =>
    lift.name.toLowerCase().includes(query.toLowerCase())
  );

  const listItems = filteredLifts.map((lift) => (
    <div className="col-md-4" key={lift._id}>
      <div className="card mb-3">
        <img src={lift.imglink} className="card-img-top" alt={lift.name} />
        <div className="card-body">
          <h5 className="card-title">{lift.name}</h5>
          <p className="card-text">Category: {lift.category}</p>
          <p className="card-text">
            Main Muscles:&nbsp; 
            {lift.muscles.map((muscle, index) => (
              <span key={index}>{muscle}{index !== lift.muscles.length - 1 ? ', ' : ''}</span>
            ))}
          </p>
        </div>
      </div>
    </div>
  ));


  function getLiftMethod() {
    fetch("http://localhost:8081/listLifts")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setLifts(data); 
      })
      .catch((error) => {
        console.error("Error fetching lifts:", error);
      });
  }


  return (
    <div className="app-container">
      <h1 className="text-center">Lift Demos</h1>
      <div className="search-bar-container">
        <input
          className="searchBar form-control" 
          type="text"
          placeholder="Search lifts..."
          value={query}
          onChange={handleChange}
        />
      </div>
      <div className="container mt-4"> {/* Adding margin-top for space */}
        <div className="row">{listItems}</div>
      </div>
    </div>
  );
};

export default Lift;