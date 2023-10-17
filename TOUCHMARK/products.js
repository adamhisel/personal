// Fetch the products.json file
fetch("./products.json")
    .then(response => response.json())
    .then(devices => {
        // Process the retrieved data with getProducts function
        getProducts(devices);
        // Assuming you have a function named featProduct to process iPhone 15
        featProduct(iPhone15);
    })
    .catch(error => {
        console.error("Error fetching data", error);
    })

function getProducts(devices) {
    var container = document.getElementById("productId");
    // Get every product from JSON file and store it as container
    for (var i = 0; i < devices.products.length; i++) {
        var product = devices.products[i];
        // Create an HTML element to display the product information
        var productInfo = document.createElement("div");
        productInfo.innerHTML = `
            <p>Name: ${product.name}</p>
            <p>Type: ${product.type}</p>
            <p>Brand: ${product.brand}</p>
            <p>Year: ${product.year}</p>
            <p>Price: ${product.price}</p>
            <p>Storage: ${product.storage}</p>
            <p>Size: ${product.size}</p>
            <p>Battery: ${product.battery}</p>
            <a href ="${product.url}" target="_blank">Product link</a>
            `;
        //Append the information to the container
        container.appendChild(product);
    }
}

function featProduct(iPhone15) {
    var feat = document.getElementById("3");
    var show = document.createElement("col"); //create a div element

    //set the content of the 'show' div using innerHTML
    show.innerHTML = `
    <p>${iPhone15.name}</p>
    <p>Type: ${iPhone15.type}</p>
    <p>Brand: ${iPhone15.brand}</p>
    <p>Year: ${iPhone15.year}</p>
    <p>Price: ${iPhone15.price}</p>
    <p>Storage: ${iPhone15.storage}</p>
    <p>Size: ${iPhone15.size}</p>
    <p>Battery: ${iPhone15.battery}</p>
    <a href="${iPhone15.url}" target="_blank">Product Link</a>`; 

    //Append to 'show' div to the 'feat' element
    feat.appendChild(show);

}
