fetch("./products.json")
    .then(Response => Response.json())
    .then(myProducts => myDevices(myProducts));

function myDevices(myProducts) {
    var mainContainer = document.getElementById("listofproducts");
    console.log(myProducts);
    for (var i = 0; i < myProducts.products.length; i++) {
        let name = myProducts.products[i].name;
        let brand = myProducts.products[i].brand;
        let year = myProducts.products[i].year;
        let price = myProducts.products[i].price;
        let storge = myProducts.products[i].storge;
        let size = myProducts.products[i].size;
        let battery = myProducts.products[i].battery;
        let url = myProducts.products[i].url;

        let lookupInfo = document.getElementById("pro1");
        lookupInfo.textContent = `${name}`;


    }
}
