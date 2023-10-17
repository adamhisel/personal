// Fetch the products.json file
fetch("./products.json")
    .then(response => response.json())
    .then(devices => getProducts(devices));



function getProducts(devices) {
    featProduct("Pixel Watch", devices);

    /*var container = document.getElementById("productId");
    for (var i = 0; i < devices.products.length; i++) {
        var product = devices.products[i];
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
        container.appendChild(product);
    }*/
}

function featProduct(feat, devices) {
    var featname = document.getElementById("featname");
    var featimg= document.getElementById("featimg"); 
    var info = document.getElementById("featinfo");
    for(var i = 0; i < devices.products.length; i++){
        if(feat === devices.products[i].name){
            featname.textContent = devices.products[i].name;
            featimg.src = devices.products[i].url;

            if(devices.products[i].storage != "" && devices.products[i].size != ""){
                info.innerHTML = `<strong>Type:</strong> ${devices.products[i].type}<br><strong>Brand:</strong> ${devices.products[i].brand}<br><strong>Release Year:</strong> ${devices.products[i].year}<br><strong>Price:</strong> ${devices.products[i].price}<br><strong>Storage Capacity:</strong> ${devices.products[i].storage}<br><strong>Size:</strong> ${devices.products[i].size}<br><strong>Battery Capacity:</strong> ${devices.products[i].battery}`;
            }
            else{
                info.innerHTML = `<strong>Type:</strong> ${devices.products[i].type}<br><strong>Brand:</strong> ${devices.products[i].brand}<br><strong>Release Year:</strong> ${devices.products[i].year}<br><strong>Price:</strong> ${devices.products[i].price}<br><strong>Battery Capacity:</strong> ${devices.products[i].battery}`;
            }
        }
        
    }

}
