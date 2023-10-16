fetch('reviews.json')
.then(response => response.json())
.then(myReviews => insertReviews(myReviews))
.catch(error => {
    console.error("Error fetching data:", error);
});

function insertReviews(myReviews){
    for(var i = 0; i < myReviews.reviews.length; i++){
        var device = myReviews.reviews[i].device;
        var nameOfUser = myReviews.reviews[i].nameOfUser;
        var stars = myReviews.reviews[i].stars;
        var description = myReviews.reviews[i].description;

        var count = i+1
        var p = "review" + count + "Header";


        var header = document.getElementById("review" + count + "Header");
        var para = document.getElementById("review" + count + "Name");
        var img = document.getElementById("review" + count + "img");
        var desc = document.getElementById("review" + count + "desc");

        header.textContent = device;
        para.textContent = nameOfUser;
        img.src = stars;
        desc.textContent = description;
    }
}