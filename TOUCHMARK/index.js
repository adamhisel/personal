fetch("./data.json")
    .then(Response => Response.json())
    .then(myProducts => myDevices(myProducts));

function myDevices(myProducts) {
    headerImages("iPhone 14", "Galaxy Z Flip5", myProducts);
    showProduct("iPhone 15", myProducts, "1");
    showProduct("Apple Watch Series 9", myProducts, "2");
    showProduct("Galaxy Buds2 Pro", myProducts, "3");
    showProduct("Galaxy Tab S9 Ultra", myProducts, "4");
}

function headerImages(name1, name2, myProducts){
    var prod1 = document.getElementById("left");
    var prod2 = document.getElementById("right");
    for(var i = 0; i < myProducts.products.length; i++){
        if(name1 === myProducts.products[i].name){
            prod1.src= myProducts.products[i].url;
            prod1.alt= myProducts.products[i].name;
        }
        if(name2 === myProducts.products[i].name){
            prod2.src= myProducts.products[i].url;
            prod2.alt= myProducts.products[i].name;
        }
        
        
    }
}

function showProduct(name, myProducts, nameId){

    var nameprod = document.getElementById(`featName${nameId}`);
    var priceprod = document.getElementById(`featPrice${nameId}`);
    var imgprod = document.getElementById(`featImg${nameId}`);
    var infoprod = document.getElementById(`featInfo${nameId}`);
    for(var i = 0; i < myProducts.products.length; i++){
        if(name === myProducts.products[i].name){
            nameprod.textContent = myProducts.products[i].name;
            priceprod.textContent = myProducts.products[i].price;
            imgprod.src = myProducts.products[i].url;
            imgprod.alt = myProducts.products[i].name;
            if(name != "Galaxy Tab S9 Ultra"){
                imgprod.width = 300; 
                imgprod.height = 400; 
            }
            if(myProducts.products[i].storage != ""){
                infoprod.innerHTML = `<strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Year:</strong> ${myProducts.products[i].year}<br><strong>Storage:</strong> ${myProducts.products[i].storage}`;
            }
            else{
                infoprod.innerHTML = `<strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Year:</strong> ${myProducts.products[i].year}<br><strong>`;
            }
            
        }
        
    }
}
