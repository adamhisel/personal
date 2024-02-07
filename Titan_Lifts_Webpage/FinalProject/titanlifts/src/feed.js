import React, { useState, useEffect } from "react";

const Feed = ({ loggedInUserId }) => {
  const [userWorkouts, setUserWorkouts] = useState([]);
  const [allWorkouts, setAllWorkouts] = useState([]);
  const [currentUser, setCurrentUser] = useState([]);

  const [dayValid, setDayValid] = useState(true);
  const [monthValid, setMonthValid] = useState(true);
  const [yearValid, setYearValid] = useState(true);

  const [categoryValid, setCategoryValid] = useState(true);

  const [liftsValid, setLiftsValid] = useState(true);
  const [liftNameValid, setLiftNameValid] = useState(true);
  const [liftSetsValid, setLiftSetsValid] = useState(true);
  const [liftRepsValid, setLiftRepsValid] = useState(true);

  const [selectedWorkoutCategoryValid, setSelectedWorkoutCategoryValid] =
    useState(true);

  const [day, setDay] = useState("");
  const [month, setMonth] = useState("");
  const [year, setYear] = useState("");

  const [date, setDate] = useState("");
  const [category, setCategory] = useState("");
  const [lifts, setLifts] = useState([
    { liftName: "", sets: 0, reps: [] },
    { liftName: "", sets: 0, reps: [] },
    { liftName: "", sets: 0, reps: [] },
  ]);

  const [showAddWorkoutForm, setShowAddWorkoutForm] = useState(false);
  const [showFeed, setShowFeed] = useState(true);
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [editClicked, setEditClicked] = useState(false);
  const [updateClicked, setUpdateClicked] = useState(false);
  const [deleteClicked, setDeleteClicked] = useState(false);

  const [dateError, setDateError] = useState("");

  const [categoryError, setCategoryError] = useState("");
  const [liftNameError, setLiftNameError] = useState("");
  const [liftSetsError, setLiftSetsError] = useState("");
  const [liftRepsError, setLiftRepsError] = useState("");

  const [workId, setWorkId] = useState(0);
  const [workoutToDelete, setWorkoutToDelete] = useState(0);
  const [selectedWorkoutCategory, setSelectedWorkoutCategory] = useState("");

  const [idCounter, setIdCounter] = useState(0);

  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  const monthOptions = months.map((month, index) => (
    <option key={index} value={month}>
      {month}
    </option>
  ));

  const days = Array.from({ length: 31 }, (_, i) => i + 1);

  const dayOptions = days.map((day) => (
    <option key={day} value={day}>
      {day}
    </option>
  ));

  const currentYear = new Date().getFullYear();
  const years = Array.from(
    { length: currentYear - 1999 },
    (_, i) => currentYear - i
  );

  const yearOptions = years.map((year) => (
    <option key={year} value={year}>
      {year}
    </option>
  ));

  useEffect(() => {
    getUserData();
    getAllWorkoutsMethod();
    getUserWorkouts();
  }, [allWorkouts, loggedInUserId]);

  // useEffect(() => {
  //   getUserWorkouts();
  // }, [allWorkouts, loggedInUserId]);

  const validateForm = () => {
    let isValid = true;

    if (day === "" || month === "" || year === "") {
      setDayValid(false);
      setMonthValid(false);
      setYearValid(false);
      setDateError("Must choose a value");
      isValid = false;
    } else {
      setDayValid(true);
      setMonthValid(true);
      setYearValid(true);
    }

    if (category === "") {
      setCategoryValid(false);
      setCategoryError("Must choose a category");
      isValid = false;
    } else {
      setCategoryValid(true);
    }

    const liftsValidations = lifts.map((lift, index) => {
      let liftValid = true;
      if (lift.liftName.trim() === "") {
        liftValid = false;
        setLiftNameValid(false);
        setLiftNameError("Type in a lift name");
      } else {
        setLiftNameValid(true);
      }
      if (lift.sets <= 0 || isNaN(lift.sets)) {
        liftValid = false;
        setLiftSetsValid(false);
        setLiftSetsError("Sets value is empty or not a number");
      } else {
        setLiftSetsValid(true);
      }
      const repsValid = lift.reps.every((rep) => !isNaN(rep));
      if (!repsValid) {
        liftValid = false;
        setLiftRepsValid(false);
        setLiftRepsError("Reps value is not a number");
      } else {
        setLiftRepsValid(true);
      }
      return liftValid;
    });

    if (liftsValidations.some((liftValid) => !liftValid)) {
      setLiftsValid(false);
      isValid = false;
    } else {
      setLiftsValid(true);
    }

    return isValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const isFormValid = validateForm();
    if (isFormValid) {
      try {
        
        
        await addNewWorkout();
        getUserWorkouts(loggedInUserId);

        setDay("");
        setMonth("");
        setYear("");
        setCategory("");
        setLifts([
          { liftName: "", sets: 0, reps: [] },
          { liftName: "", sets: 0, reps: [] },
          { liftName: "", sets: 0, reps: [] },
        ]);

        setShowAddWorkoutForm(false);
        setShowFeed(true);
      } catch (error) {
        console.error("Error adding workout:", error);
      }
    } else {
      console.log("Form has errors. Cannot submit.");
    }
  };

  // const handleAddLift = () => {
  //   const newLift = { liftName: "", sets: 0, reps: [] };
  //   setLifts([...lifts, newLift]);
  // };

  const getUserWorkouts = () => {
    const filteredWorkouts = allWorkouts.filter(
      (workout) => workout.user_id === loggedInUserId
    );
    setUserWorkouts(filteredWorkouts);
  };

  function getAllWorkoutsMethod() {
    fetch("http://localhost:8081/listWorkouts")
      .then((response) => response.json())
      .then((data) => {
        if (Array.isArray(data)) {
          setAllWorkouts(data);
          setIdCounter(data.length + 1);
        } else {
          console.error("Invalid data format received for lifts:", data);
        }
      })
      .catch((error) => {
        console.error("Error fetching lifts:", error);
      });
  }

  const addNewWorkout = async () => {
    try {

      const workoutDate = month + " " + day + ", " + year; 

      const response = await fetch("http://localhost:8081/addWorkout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          id: idCounter,
          user_id: loggedInUserId,
          date: workoutDate,
          category: category,
          lifts: lifts,
        }),
      });
      if (response.ok) {
        console.log("Workout added successfully");
      } else {
        console.error("Failed to add workout");
      }
    } catch (error) {
      console.error("Error adding workout:", error);
    }
  };

  const updateWorkoutCategory = async (workoutId, updatedCategory) => {
    try {
      const response = await fetch(
        `http://localhost:8081/workouts/${workoutId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ category: updatedCategory }),
        }
      );
      if (response.ok) {
        console.log("Category updated successfully");

        const updatedUserWorkouts = userWorkouts.map((workout) => {
          if (workout.id === workoutId) {
            return { ...workout, category: updatedCategory };
          }
          return workout;
        });
        setUserWorkouts(updatedUserWorkouts);
      } else {
        console.error("Failed to update category");
      }
    } catch (error) {
      console.error("Error updating category:", error);
    }
  };

  const deleteWorkout = async (workoutId) => {
    try {
      const response = await fetch(
        `http://localhost:8081/workouts/${workoutId}`,
        {
          method: "DELETE",
        }
      );
      if (response.ok) {
        // Handle success
        console.log("Workout deleted successfully");
      } else {
        // Handle error
        console.error("Failed to delete workout");
      }
    } catch (error) {
      console.error("Error deleting workout:", error);
    }
  };

  const getUserData = async () => {
    try {
      const response = await fetch(
        `http://localhost:8081/users/${loggedInUserId}`
      );
      if (response.ok) {
        const userData = await response.json();
        setCurrentUser(userData);
      } else {
        console.error("Failed to fetch user data");
      }
    } catch (error) {
      console.error("Error fetching user data:", error);
    }
  };

  const renderUserWorkouts = () => {
    return (
      <div className="row row-cols-1 g-4 pb-6">
        {userWorkouts.map((workout, index) => (
          <div key={index} className="col">
            <div className="card w-100 position-relative">
              {/* Edit button */}
              <button
                onClick={() => handleEditClick(index)} // Pass the workout index to handleEditClick function
                className="btn btn-link position-absolute top-0 end-0 m-3"
              >
                {editClicked === index ? "Close" : "Edit"}
              </button>

              <div className="card-body">
                <h5 className="card-title">Date: {workout.date}</h5>
                <h6 className="card-subtitle mb-2 text-muted">
                  Category: {workout.category}
                </h6>
                <div className="row row-cols-1 row-cols-md-3 g-4 pb-3">
                  {workout.lifts.map((lift, liftIndex) => (
                    <div key={liftIndex} className="col">
                      <div className="card">
                        <div className="card-body">
                          <h6 className="card-title">
                            Lift Name: {lift.liftName}
                          </h6>
                          <p className="card-text">Sets: {lift.sets}</p>
                          <p className="card-text">
                            Reps:{" "}
                            {lift.reps.length > 0 ? lift.reps.join(", ") : "-"}
                          </p>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>

                {editClicked === index && (
                  <div className="d-grid gap-2">
                    <button
                      onClick={() => handleUpdateClick(workout.id)} // Changed this line
                      className="btn btn-primary btn-lg mx-auto"
                    >
                      Update Workout
                    </button>

                    <button
                      onClick={() => handleDeleteWorkout(workout.id)}
                      className="btn btn-danger btn-lg mx-auto"
                    >
                      Delete Workout
                    </button>

                    {showDeleteConfirmation &&
                      workoutToDelete === workout.id && (
                        <div className="col-md-6">
                          <h4>Confirm Delete</h4>
                          <p>Are you sure you want to delete this workout?</p>
                          <button
                            onClick={handleConfirmDelete} // Confirm deletion
                            className="btn btn-danger btn-sm mx-auto"
                          >
                            Confirm
                          </button>
                          <button
                            onClick={handleCancelDelete} // Cancel deletion
                            className="btn btn-secondary btn-sm mx-auto"
                          >
                            Cancel
                          </button>
                        </div>
                      )}

                    {updateClicked && ( // Moved this line inside the parent div
                      <div className="col-md-6">
                        <h4>Update Category</h4>
                        <form onSubmit={handleUpdateSubmit}>
                          <select
                            id="categoryDropdown"
                            className={`form-select ${
                              !selectedWorkoutCategoryValid ? "is-invalid" : ""
                            }`}
                            value={selectedWorkoutCategory}
                            onChange={(e) =>
                              setSelectedWorkoutCategory(e.target.value)
                            }
                          >
                            <option value="">Select Category</option>
                            <option value="Legs">Legs</option>
                            <option value="Push">Push</option>
                            <option value="Pull">Pull</option>
                            <option value="Core">Core</option>
                          </select>

                          {!categoryValid && (
                            <div className="invalid-feedback">
                              {categoryError}
                            </div>
                          )}
                          <br />
                          <button
                            type="submit"
                            className="btn btn-primary btn-sm mx-auto"
                          >
                            Update
                          </button>

                          <button
                            onClick={() => handleUpdateReturn}
                            className="btn btn-secondary btn-sm mx-auto"
                          >
                            Cancel
                          </button>
                        </form>
                      </div>
                    )}
                  </div>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    );
  };

  // () => updateWorkout(workout.id, [])

  const handleEditClick = (index) => {
    setEditClicked((prevState) => (prevState === index ? false : index));
  };

  const handleUpdateClick = (workoutId) => {
    setWorkId(workoutId);
    setShowDeleteConfirmation(false);
    setUpdateClicked(true);
  };

  const handleUpdateSubmit = () => {
    let valid = true;
    if (selectedWorkoutCategory === "") {
      setSelectedWorkoutCategoryValid(false);
      setCategoryError("Must choose a category");
      valid = false;
    } else {
      setSelectedWorkoutCategoryValid(true);

      updateWorkoutCategory(workId, selectedWorkoutCategory);

      setUpdateClicked(false);
    }
    return valid;
  };

  const handleUpdateReturn = () => {
    updateClicked(false);
  };

  const handleDeleteWorkout = (workoutId) => {
    setWorkoutToDelete(workoutId);
    setUpdateClicked(false);
    setShowDeleteConfirmation(true);
  };

  const handleConfirmDelete = () => {
    deleteWorkout(workoutToDelete);
    setShowDeleteConfirmation(false);
  };

  const handleCancelDelete = () => {
    setShowDeleteConfirmation(false);
  };
  const handleLiftNameChange = (index, value) => {
    const updatedLifts = [...lifts];
    updatedLifts[index].liftName = value;
    setLifts(updatedLifts);
  };

  const handleSetsChange = (index, value) => {
    const updatedLifts = [...lifts];
    updatedLifts[index].sets = value;
    updatedLifts[index].reps = Array.from({ length: value }, () => 0);
    setLifts(updatedLifts);
  };

  const handleRepsChange = (liftIndex, repIndex, value) => {
    const updatedLifts = [...lifts];
    updatedLifts[liftIndex].reps[repIndex] = value;
    setLifts(updatedLifts);
  };

  const handleAddCard = () => {
    setLifts([
      ...lifts,
      {
        liftName: "",
        sets: 0,
        reps: [],
      },
    ]);
  };

  const handleRemoveCard = (index) => {
    const updatedLifts = [...lifts];
    updatedLifts.splice(index, 1);
    setLifts(updatedLifts);
  };

  const handleAddClicked = () => {
    setShowAddWorkoutForm(true);
    setShowFeed(false);
  };

  const handleReturn = () => {
    setShowAddWorkoutForm(false);
    setShowFeed(true);
  };

  return (
    <div style={{ width: "100vw" }}>
      {showFeed && (
        <div>
          <h1 className="text-center py-4">
            {currentUser
              ? `${currentUser.firstname}'s Workout Feed`
              : "Loading..."}
          </h1>

          {currentUser && renderUserWorkouts()}

          <div className="text-center py-4">
            <button
              type="button"
              className="btn btn-primary btn-lg mt-3 px-5 py-3 fs-4"
              onClick={handleAddClicked}
            >
              Post A Workout
            </button>
          </div>
        </div>
      )}
      {showAddWorkoutForm && (
        <div className="container" id="validation">
          <div className="row">
            <div className="col-8 py-4">
              <h1>Add Workout</h1>
              <form
                className="row g-3"
                id="checkout-form"
                onSubmit={handleSubmit}
              >
                <div className="row">
                  <h4 className="pt-4">Date</h4>

                  {/* Month */}
                  <div className="col-md-4">
                    <label htmlFor="inputName" className="form-label">
                      Month:
                    </label>
                    <select
                      className={`form-select ${
                        !monthValid ? "is-invalid" : ""
                      }`}
                      id="inputName"
                      value={month}
                      onChange={(e) => setMonth(e.target.value)}
                    >
                      <option value="">Select a month</option>
                      {monthOptions}
                    </select>

                    {!monthValid && (
                      <div className="invalid-feedback">{dateError}</div>
                    )}
                  </div>

                  {/* Day */}
                  <div className="col-md-4">
                    <label htmlFor="inputName" className="form-label">
                      Day:
                    </label>
                    <select
                      className={`form-select ${!dayValid ? "is-invalid" : ""}`}
                      id="inputDay"
                      value={day}
                      onChange={(e) => setDay(e.target.value)}
                    >
                      <option value="">Select a day</option>
                      {dayOptions}
                    </select>

                    {!dayValid && (
                      <div className="invalid-feedback">{dateError}</div>
                    )}
                  </div>

                  {/* Year */}
                  <div className="col-md-4">
                    <label htmlFor="inputName" className="form-label">
                      Year:
                    </label>
                    <select
                      className={`form-select ${
                        !yearValid ? "is-invalid" : ""
                      }`}
                      id="inputYear"
                      value={year}
                      onChange={(e) => setYear(e.target.value)}
                    >
                      <option value="">Select a year</option>
                      {yearOptions}
                    </select>

                    {!yearValid && (
                      <div className="invalid-feedback">{dateError}</div>
                    )}
                  </div>
                </div>

                {/*Category*/}
                <div className="col-md-6">
                  <h4>Category</h4>
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

                <div>
                  <div className="row row-cols-4 g-4">
                    {lifts.map((lift, index) => (
                      <div key={index} className="col">
                        <div className="card">
                          <div className="card-body">
                            <h6>Lift Name</h6>
                            <input
                              type="text"
                              className="form-control mb-2"
                              placeholder="Lift Name"
                              value={lift.liftName}
                              onChange={(e) =>
                                handleLiftNameChange(index, e.target.value)
                              }
                            />

                            {!liftNameValid && (
                              <div className="invalid-feedback">
                                {liftNameError}
                              </div>
                            )}
                            <h6>Sets</h6>
                            <input
                              type="number"
                              className="form-control mb-2"
                              placeholder="Sets"
                              value={lift.sets}
                              onChange={(e) =>
                                handleSetsChange(index, e.target.value)
                              }
                            />

                            {!liftSetsValid && (
                              <div className="invalid-feedback">
                                {liftSetsError}
                              </div>
                            )}
                            <div className="d-flex flex-wrap">
                              {Array.from(
                                { length: lift.sets },
                                (_, repIndex) => (
                                  <div key={repIndex} className="mb-2 me-2">
                                    <h6>Set {repIndex + 1} Reps</h6>
                                    <input
                                      type="number"
                                      className="form-control"
                                      style={{ width: "70px" }}
                                      value={lift.reps[repIndex]}
                                      onChange={(e) =>
                                        handleRepsChange(
                                          index,
                                          repIndex,
                                          e.target.value
                                        )
                                      }
                                    />
                                    {!liftRepsValid && (
                                      <div className="invalid-feedback">
                                        {liftRepsError}
                                      </div>
                                    )}
                                  </div>
                                )
                              )}
                            </div>
                            <button
                              type="button"
                              className="btn btn-danger"
                              onClick={() => handleRemoveCard(index)}
                            >
                              Remove Lift
                            </button>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                  <button
                    type="button"
                    className="btn btn-success mt-3"
                    onClick={handleAddCard}
                  >
                    Add New Lift
                  </button>
                </div>
                <div className="row">
                  <div className="col-md-6 py-4">
                    <button type="submit" className="btn btn-primary w-100">
                      <i className="bi-bag-check"></i> Post Workout
                    </button>
                  </div>

                  <div className="col-md-6 py-4">
                    <button
                      type="button"
                      className="btn btn-secondary w-100"
                      onClick={handleReturn}
                    >
                      <i className="bi bi-arrow-left-circle"></i> Cancel Post
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
      ;
    </div>
  );
};

export default Feed;
