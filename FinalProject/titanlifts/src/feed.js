import React, { useState, useEffect } from "react";


const Feed = () => {

    return (
        <div className="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
      {/* Adjust the column classes based on screen sizes to achieve responsiveness */}
      <div className="card mb-3 w-100">
        {/* 'w-100' class makes the card width 100% */}
        <img src="" className="card-img-top" alt="Card" />
        <div className="card-body">
          <h5 className="card-title"></h5>
          <p className="card-text">Category: </p>
          <p className="card-text">
            Main Muscles:&nbsp;
            {/* Add content for the Main Muscles section */}
          </p>
        </div>
      </div>
    </div>
      );




}

export default Feed;


