import React, { useState, useEffect } from "react";
import "./demos.css";

const Demos = () => {
  const [query, setQuery] = useState("");
  const [lifts, setLifts] = useState([]);
  const [isLiftFormValid, setIsLiftFormValid] = useState(true);

  const [nameValid, setNameValid] = useState(true);
  const [categoryValid, setCategoryValid] = useState(true);
  const [musclesValid, setMusclesValid] = useState(true);
  const [imgLinkValid, setImgLinkValid] = useState(true);

  const [name, setName] = useState("");
  const [category, setCategory] = useState("");
  const [muscles, setMuscles] = useState("");
  const [imgLink, setImgLink] = useState("");

  const [showAddLiftForm, setShowAddLiftForm] = useState(false);
  const [showDemos, setShowDemos] = useState(true);

  const [nameError, setNameError] = useState("");
  const [categoryError, setCategoryError] = useState("");
  const [musclesError, setMusclesError] = useState("");
  const [imgLinkError, setImgLinkError] = useState("");

  const [lift, setLift] = useState({
    name: "",
    category: "",
    muscles: "",
    imgLink: ""
  });

  const [idCounter, setIdCounter] = useState(0);

  const incrementCounter = () => {
    setIdCounter(lifts.length + 1);
  };




  useEffect(() => {
    getLiftMethod();
  }, []);

  const handleChange = (e) => {
    setQuery(e.target.value);
  };

  const handleAddLiftClick = (e) => {

    e.preventDefault();
    setShowAddLiftForm(true);
    setShowDemos(false);
  };

  const handleLiftFormSubmit = (e) => {
    e.preventDefault();
    
      setShowAddLiftForm(false);
      setShowDemos(true);

      
  
  };

  const handleLiftReturn = (e) => {
    e.preventDefault();

    setShowAddLiftForm(false);
    setShowDemos(true);
  };

  const checkImageValidity = (url) => {
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.onload = () => resolve();
      img.onerror = () => reject();
      img.src = url;
    });
  }

  const isValid = async () => {
    let val = true;

    if (name.trim() === "") {
      setNameValid(false);
      setNameError("Must type a lift name");
      val = false;
    } else {
      setNameValid(true);
    }

    if (category.trim() === "") {
      setCategoryValid(false);
      setCategoryError("Must select a category");
      val = false;
    } else {
      setCategoryValid(true);
    }

    if (muscles.trim() === "") {
      setMusclesValid(false);
      setMusclesError("Must type at least one main muscle");
      val = false;
    } else {
      setMusclesValid(true);
    }

    if (imgLink.trim() === "") {
      setImgLinkValid(false);
      setImgLinkError("Must provide an image link");
      val = false;
    }else{ 
      try {
        await checkImageValidity(imgLink);
        setImgLinkValid(true);
      } catch (error) {
        setImgLinkValid(false);
        setImgLinkError("Image link is not correct");
        val = false;
      }
    }
  
  
    if (val) {
      setLift({ name, category, muscles, imgLink });
      setIsLiftFormValid(true);
      setNameValid(true);
      setCategoryValid(true);
      setMusclesValid(true);
      setImgLinkValid(true);
      console.log(name);
      console.log(category);
      console.log(muscles);
      console.log(imgLink);
    }
    return val;
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
              <span key={index}>
                {muscle}
                {index !== lift.muscles.length - 1 ? ", " : ""}
              </span>
            ))}
          </p>
        </div>
      </div>
    </div>
  ));

  function postLiftMethod() {

    const array = muscles.split(',').map(item => item.trim());

    fetch("http://localhost:8081/addLift", {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({
        id: idCounter,
        name: name,
        category: category,
        muscles: array,
        imglink: imgLink
      })
    })
    .then(response => response.json())
    .then(data => {
      console.log(data);
      getLiftMethod();
    })
    .catch((error) => {
      console.error("Error fetching lifts:", error);
    });

  };

  function getLiftMethod() {
    fetch("http://localhost:8081/listLifts")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        if (Array.isArray(data)) {
          setLifts(data);
        } else {
          console.error("Invalid data format received for lifts:", data);
        }
      })
      .catch((error) => {
        console.error("Error fetching lifts:", error);
      });
  }

  return (
    <div className="app-container">
      {showDemos && (
        <div className="container" id="demos">
          <h1 className="text-center">Lift Demos</h1>
          <div className="d-flex justify-content-between">
            <div className="search-bar-container">
              <input
                className="searchBar form-control"
                type="text"
                placeholder="Search by lift name or category..."
                value={query}
                onChange={handleChange}
              />
            </div>
            <div>
              <button
                className="btn btn-primary subButton"
                onClick={handleAddLiftClick}
              >
                Add Lift
              </button>
            </div>
          </div>
          <div className="container mt-4">
            {/* Adding margin-top for space */}
            <div className="row">{listItems}</div>
          </div>
        </div>
      )}

      {showAddLiftForm && (
        <div className="container" id="validation">
          <div className="row">
            <div className="col-8">
              <h1>Add Lift</h1>
              <form
                className="row g-3"
                id="checkout-form"
                onSubmit={(e) => {
                  e.preventDefault();
                  isValid().then((isValidResult) => {
                    if (isValidResult) {
                      incrementCounter();
                      handleLiftFormSubmit(e);
                      postLiftMethod();
                    }
                  });
                }}
              >
                {/* Name */}
                <div className="col-md-6">
                  <label htmlFor="inputName" className="form-label">
                    Lift Name
                  </label>
                  <input
                    type="text"
                    className={`form-control ${!nameValid ? "is-invalid" : ""}`}
                    id="inputName"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  />

                  {!nameValid && (
                    <div className="invalid-feedback">{nameError}</div>
                  )}
                </div>
                {/*Category*/}
                <div className="col-md-6">
                  <label htmlFor="categoryDropdown" className="form-label">
                    Category
                  </label>
                  <select
                    id="categoryDropdown"
                    className={`form-select ${
                      !categoryValid ? "is-invalid" : ""
                    }`}
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                  >
                    <option value="">Select Category</option>
                    <option value="Legs">Legs</option>
                    <option value="Push">Push</option>
                    <option value="Pull">Pull</option>
                    <option value="Core">Core</option>
                  </select>

                  {!categoryValid && (
                    <div className="invalid-feedback">{categoryError}</div>
                  )}
                </div>
                <div className="col-md-6">
                  <label htmlFor="inputName" className="form-label">
                    Muscles Worked (separate with commas)
                  </label>
                  <input
                    type="text"
                    className={`form-control ${
                      !musclesValid ? "is-invalid" : ""
                    }`}
                    id="inputName"
                    value={muscles}
                    onChange={(e) => setMuscles(e.target.value)}
                  />

                  {!musclesValid && (
                    <div className="invalid-feedback">{musclesError}</div>
                  )}
                </div>
                <div>
                  1.{" "}
                  <a href="https://weighttraining.guide/" target="_blank">
                    Open this link
                  </a>{" "}
                  <br></br>
                  2. Use the search bar to find your workout<br></br>
                  3. Click the workout and double tap the image<br></br>
                  4. Click "Copy Image Link" <br></br>
                  5. Paste the link in the text box below
                </div>
                <div className="col-md-6">
                  <label htmlFor="inputName" className="form-label">
                    Image Link
                  </label>
                  <input
                    type="text"
                    className={`form-control ${
                      !imgLinkValid ? "is-invalid" : ""
                    }`}
                    id="inputName"
                    value={imgLink}
                    onChange={(e) => setImgLink(e.target.value)}
                  />

                  {!imgLinkValid && (
                    <div className="invalid-feedback">{imgLinkError}</div>
                  )}
                </div>
                <div className="col-12">
                  <button type="submit" className="btn btn-success">
                    <i className="bi-bag-check"></i> Submit
                  </button>

                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={handleLiftReturn}
                  >
                    <i className="bi bi-arrow-left-circle"></i> Return
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Demos;
