
fetch('products.json')
.then(response => response.json())
.then(myProducts => compareProducts(myProducts))
.catch(error => {
    console.error("Error fetching data:", error);
});

function compareProducts(myProducts){
    

    const button1 = document.getElementById('button1');
    const searchInput1 = document.getElementById('search-input1');

   
    const button2 = document.getElementById('button2');
    const searchInput2 = document.getElementById('search-input2');



    button1.addEventListener('click', function (e) {

        e.preventDefault();

        var searchVal = searchInput1.value.toLowerCase();
        var productImage = findProductImage(myProducts, searchVal); 

        displayImage(productImage, "image1");
        findProductInfo(myProducts, searchVal, "info1", "productName1");
    });

    button2.addEventListener('click', function (e) {

        e.preventDefault();

        var searchVal = searchInput2.value.toLowerCase();
        var productImage = findProductImage(myProducts, searchVal); 

        displayImage(productImage, "image2");
        findProductInfo(myProducts, searchVal, "info2", "productName2");
    });

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
            var lineBreak = document.createElement("br");

            info.textContent = `Type: ${myProducts.products[i].type} Brand: ${myProducts.products[i].brand}
            Release Year: ${myProducts.products[i].year} Price: ${myProducts.products[i].price} Storage Capacity: ${myProducts.products[i].storage} Size: ${myProducts.products[i].size} Battery Capacity: ${myProducts.products[i].battery}`;
            break;
        }
    }
}


function displayImage(url, imageId){
    var image = document.getElementById(`${imageId}`);
    image.src = `${url}`;
    image.width = 250;
    image.height = 350;
}