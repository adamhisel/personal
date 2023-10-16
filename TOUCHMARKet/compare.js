
fetch('products.json')
.then(response => response.json())
.then(myProducts => compareProducts(myProducts))
.catch(error => {
    console.error("Error fetching data:", error);
});

function compareProducts(myProducts){
    
    const form1 = document.getElementById("form1");
    const form2 = document.getElementById("form2");
    const button1 = document.getElementById("button1");
    const searchInput1 = document.getElementById("search-input1");

    const suggestions1 = document.getElementById("suggestions1");
    const suggestions2 = document.getElementById("suggestions2");
   
    const button2 = document.getElementById("button2");
    const searchInput2 = document.getElementById("search-input2");

    const nameArr = [];

    makeNameArray(myProducts, nameArr);
    

    searchInput1.addEventListener("input", function (event) {
        showSuggestions(event.target.value, nameArr, suggestions1);
    });
    searchInput2.addEventListener("input", function (event) {
        showSuggestions(event.target.value, nameArr, suggestions2);
    });

    suggestions1.addEventListener("click", function (event) {
        if (event.target.tagName === "LI") {
          searchInput1.value = event.target.textContent;
          suggestions1.style.display = "none";
        }
    });
    suggestions2.addEventListener("click", function (event) {
        if (event.target.tagName === "LI") {
          searchInput2.value = event.target.textContent;
          suggestions2.style.display = "none";
        }
    });


    button1.addEventListener('click', function (e) {

        e.preventDefault();

        var searchVal = searchInput1.value.toLowerCase();
        if(searchVal == ""){
            return;
        }
        var productImage = findProductImage(myProducts, searchVal); 

        displayImage(productImage, "image1");
        findProductInfo(myProducts, searchVal, "info1", "productName1");
    });

    button2.addEventListener('click', function (e) {

        e.preventDefault();

        var searchVal = searchInput2.value.toLowerCase();
        if(searchVal == ""){
            return;
        }
        var productImage = findProductImage(myProducts, searchVal); 

        displayImage(productImage, "image2");
        findProductInfo(myProducts, searchVal, "info2", "productName2");
    });

}

function makeNameArray(myProducts, nameArr){
    for(var i = 0; i < myProducts.products.length; i++){
        var name = myProducts.products[i].name;
        nameArr[i] = name;
    }
}


function findProductImage(myProducts, value){
    var url = "";
    for (var i = 0; i< myProducts.products.length; i++){
        let name = myProducts.products[i].name;
        if(value === name.toLowerCase()){
            url = myProducts.products[i].url;
            break;
        }
    }

    return url;
}

function findProductInfo(myProducts, value, infoId, productNameId){
    for (var i = 0; i< myProducts.products.length; i++){
        let name = myProducts.products[i].name;
        if(value === name.toLowerCase()){
            var header = document.getElementById(`${productNameId}`);
            header.textContent = myProducts.products[i].name;
            var info = document.getElementById(`${infoId}`);
            if(myProducts.products[i].storage != "" && myProducts.products[i].size != ""){
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Storage Capacity:</strong> ${myProducts.products[i].storage}<br><strong>Size:</strong> ${myProducts.products[i].size}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }
            else{
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }
            break;
        }
    }
}


function displayImage(url, imageId){
    var image = document.getElementById(`${imageId}`);
    image.src = `${url}`;
    image.width = 350;
    image.height = 410;
}



function showSuggestions(searchInput, nameArr, suggestions) {
  const searchQuery = searchInput.toLowerCase();
  const filteredSuggestions = nameArr.filter(suggestion =>
    suggestion.toLowerCase().includes(searchQuery)
  );

  suggestions.innerHTML = "";

  if (searchQuery !== "") {
    filteredSuggestions.forEach(suggestion => {
      const listItem = document.createElement("li");
      listItem.textContent = suggestion;
      suggestions.appendChild(listItem);
    });
    suggestions.style.display = "block";
  } else {
    suggestions.style.display = "none";
  }
}
