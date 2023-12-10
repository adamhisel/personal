import React, { useState, useEffect } from "react";

const Feed = ({ loggedInUserId }) => {
  const [workouts, setWorkouts] = useState([]);
  const [isWorkoutFormValid, setIsWorkoutFormValid] = useState(true);

  const [dayValid, setDayValid] = useState(true);
  const [monthValid, setMonthValid] = useState(true);
  const [yearValid, setYearValid] = useState(true);

  const [categoryValid, setCategoryValid] = useState(true);

  const [liftsValid, setLiftsValid] = useState(true);
  const [liftNameValid, setLiftNameValid] = useState(true);
  const [liftSetsValid, setLiftSetsValid] = useState(true);
  const [liftRepsValid, setLiftRepsValid] = useState(true);

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

  const [showAddWorkoutForm, setShowAddWorkoutForm] = useState(true);
  const [showFeed, setShowFeed] = useState(true);

  const [dateError, setNameError] = useState("");
  const [categoryError, setCategoryError] = useState("");
  const [liftNameError, setLiftNameError] = useState("");
  const [liftSetsError, setLiftSetsError] = useState("");
  const [lifRepsError, setLiftRepsError] = useState("");

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
    fetchWorkouts(loggedInUserId);
  }, [loggedInUserId]);

  const [newWorkout, setNewWorkout] = useState({
    date: "",
    category: "",
    lifts: [],
  });

  const validateForm = () => {
    let isValid = true;
  
    if (day === "" || month === "" || year === "") {
      setDayValid(false);
      setMonthValid(false);
      setYearValid(false);
      isValid = false;
    } else {
      setDayValid(true);
      setMonthValid(true);
      setYearValid(true);
    }
  
    if (category === "") {
      setCategoryValid(false);
      isValid = false;
    } else {
      setCategoryValid(true);
    }
  
    const liftsValidations = lifts.map((lift, index) => {
      let liftValid = true;
      if (lift.liftName.trim() === "") {
        liftValid = false;
        setLiftNameValid(false);
      } else {
        setLiftNameValid(true);
      }
      if (lift.sets <= 0 || isNaN(lift.sets)) {
        liftValid = false;
        setLiftSetsValid(false);
      } else {
        setLiftSetsValid(true);
      }
      const repsValid = lift.reps.every((rep) => !isNaN(rep));
      if (!repsValid) {
        liftValid = false;
        setLiftRepsValid(false);
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
      const formattedDate = `${year}-${month}-${day}`;
      const formattedLifts = lifts.map(({ liftName, sets, reps }) => ({
        liftName,
        sets,
        reps,
      }));

      const newWorkoutData = {
        date: formattedDate,
        category,
        lifts: formattedLifts,
      };

      try {
        await addNewWorkout(newWorkoutData);
        fetchWorkouts(loggedInUserId); // Fetch updated workouts after adding a new one

        // Clear form values after submitting
        setDay("");
        setMonth("");
        setYear("");
        setCategory("");
        setLifts([
          { liftName: "", sets: 0, reps: [] },
          { liftName: "", sets: 0, reps: [] },
          { liftName: "", sets: 0, reps: [] },
        ]);
      } catch (error) {
        console.error("Error adding workout:", error);
      }
    } else {
      console.log("Form has errors. Cannot submit.");
    }
  };
  
  const handleAddLift = () => {
    const newLift = { liftName: "", sets: 0, reps: [] };
    setLifts([...lifts, newLift]);
  };

  const fetchWorkouts = async (loggedInUserId) => {
    try {
      const response = await fetch(
        `http://localhost:8081/workouts/${loggedInUserId}`
      );
      if (response.ok) {
        const data = await response.json();
        setWorkouts(data);
      } else {
        console.error("Error fetching workouts:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching workouts:", error);
    }
  };

  const addNewWorkout = async () => {
    try {
      const response = await fetch("http://localhost:8081/addWorkout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newWorkout),
      });
      if (response.ok) {
       
        console.log("Workout added successfully");
      } else {
        // Handle error
        console.error("Failed to add workout");
      }
    } catch (error) {
      console.error("Error adding workout:", error);
    }
  };

  const updateWorkout = async (workoutId, updatedData) => {
    try {
      const response = await fetch(
        `http://localhost:8081/workouts/${workoutId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(updatedData),
        }
      );
      if (response.ok) {
        // Handle success
        console.log("Workout updated successfully");
      } else {
        // Handle error
        console.error("Failed to update workout");
      }
    } catch (error) {
      console.error("Error updating workout:", error);
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

  return (
    <div style={{ width: "100vw" }}>
      <h2>Workouts Feed</h2>
      {/* Button to add a new workout */}
      <button onClick={addNewWorkout}>Add New Workout</button>
      {/* List of workouts */}
      <ul>
        {/* {workouts.map((workout, index) => (
          <li key={index}>
            
            <div>
              <strong>Date: </strong>
              {workout.date}
            </div>
            <div>
              <strong>Category: </strong>
              {workout.category}
            </div>
           
            <button onClick={() => updateWorkout(workout.id, [])}>
              Update Workout
            </button>
            
            <button onClick={() => deleteWorkout(workout.id)}>
              Delete Workout
            </button>
          </li>
        ))} */}
      </ul>
      {showAddWorkoutForm && (
        <div className="container" id="validation">
          <div className="row">
            <div className="col-8">
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
                      <div className="invalid-feedback">
                        Please select a month.
                      </div>
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
                      <div className="invalid-feedback">
                        Please select a day.
                      </div>
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
                      <div className="invalid-feedback">
                        Please select a year.
                      </div>
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
                                  </div>
                                )
                              )}
                            </div>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                  <button type = "button"
                    className="btn btn-primary mt-3"
                    onClick={handleAddCard}
                  >
                    Add New Card
                  </button>
                </div>
                <div className="col-12">
                  <button type="submit" className="btn btn-success">
                    <i className="bi-bag-check"></i> Submit
                  </button>
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
