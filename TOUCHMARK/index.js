fetch("./products.json")
    .then(Response => Response.json())
    .then(myProducts => myDevices(myProducts));

function myDevices(myProducts) {
    showSamsung("iPhone 14", myProducts);

    
 
}

function showSamsung(name, myProducts){

    var nameprod = document.getElementById("test");
    for(var i = 0; i < myProducts.products.length; i++){
        if(name === myProducts.products[i].name){
            nameprod.textContent = myProducts.products[i].name;

        }
        
    }
}