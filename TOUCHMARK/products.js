
fetch("./data.json")
    .then(response => response.json())
    .then(devices => getProducts(devices));



function getProducts(devices) {
    featProduct("iPhone 15", devices, "1");
    featProduct("iPad Pro", devices, "2");
   
    putPhones(devices);
    createDivider("Tablets")
    putTablets(devices);
    createDivider("Smartwatches")
    putSmartwatch(devices);
    createDivider("Wireless Headphones")
    putWirelessHeadphones(devices);

}

function featProduct(feat, devices, id) {
    var featname = document.getElementById(`featname${id}`);
    var featimg= document.getElementById(`featimg${id}`); 
    var info = document.getElementById(`featinfo${id}`);
    for(var i = 0; i < devices.products.length; i++){
        if(feat === devices.products[i].name){
            featname.textContent = devices.products[i].name;
            featimg.src = devices.products[i].url;

            if(devices.products[i].storage != "" && devices.products[i].size != ""){
                info.innerHTML = `<strong>Type:</strong> ${devices.products[i].type}<br><strong>Brand:</strong> ${devices.products[i].brand}<br><strong>Release Year:</strong> ${devices.products[i].year}<br><strong>Price:</strong> ${devices.products[i].price}<br><strong>Storage:</strong> ${devices.products[i].storage}<br><strong>Size:</strong> ${devices.products[i].size}<br><strong>Battery:</strong> ${devices.products[i].battery}`;
            }
            else{
                info.innerHTML = `<strong>Type:</strong> ${devices.products[i].type}<br><strong>Brand:</strong> ${devices.products[i].brand}<br><strong>Release Year:</strong> ${devices.products[i].year}<br><strong>Price:</strong> ${devices.products[i].price}<br><strong>Battery:</strong> ${devices.products[i].battery}`;
            }
        }
        
    }

}

function createDivider(name){
    var divider = document.createElement('div');
    var divName = document.createElement("h2");
    divName.textContent = name;
    divider.appendChild(divName);
}

function putPhones(myProducts){
    var container = document.querySelector('.container4');

    var count = 0;
    for(var i = 0; i < myProducts.products.length; i++){

        if(myProducts.products[i].type === "Smartphone"){
            if (count % 3 === 0) {
                var row = document.createElement('div');
                row.classList.add('row');
                container.appendChild(row);
            }
            count += 1;

            var prodContainer = document.createElement('div');
            prodContainer.classList.add('item');

            var name = document.createElement("h2");
            name.textContent = myProducts.products[i].name;

            var info = document.createElement("p");

            if(myProducts.products[i].storage != "" && myProducts.products[i].size != ""){
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Storage Capacity:</strong> ${myProducts.products[i].storage}<br><strong>Size:</strong> ${myProducts.products[i].size}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }
            else{
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }

            var img = document.createElement("img");
            img.src = myProducts.products[i].url;
            img.width = 300;


            prodContainer.appendChild(name);
            prodContainer.appendChild(info);
            prodContainer.appendChild(img);


            const currentRow = container.lastChild;
            currentRow.appendChild(prodContainer);
    }
    }
}

function putTablets(myProducts){
    var container = document.querySelector('.container4');
    var count = 0;
    for(var i = 0; i < myProducts.products.length; i++){

        if(myProducts.products[i].type === "Tablet"){
            if (count % 3 === 0) {
                var row = document.createElement('div');
                row.classList.add('row');
                container.appendChild(row);
            }
            count += 1;
        
            var prodContainer = document.createElement('div');
            prodContainer.classList.add('item');

            var name = document.createElement("h2");
            name.textContent = myProducts.products[i].name;

            var info = document.createElement("p");

            if(myProducts.products[i].storage != "" && myProducts.products[i].size != ""){
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Storage Capacity:</strong> ${myProducts.products[i].storage}<br><strong>Size:</strong> ${myProducts.products[i].size}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }
            else{
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }

            var img = document.createElement("img");
            img.src = myProducts.products[i].url;
            img.width = 300;


            prodContainer.appendChild(name);
            prodContainer.appendChild(info);
            prodContainer.appendChild(img);


            const currentRow = container.lastChild;
            currentRow.appendChild(prodContainer);
    }
    }
}

function putSmartwatch(myProducts){
    var container = document.querySelector('.container4');
    var count = 0;
    for(var i = 0; i < myProducts.products.length; i++){

       if(myProducts.products[i].type === "Smartwatch"){
            if (count % 3 === 0) {
                var row = document.createElement('div');
                row.classList.add('row');
                container.appendChild(row);
            }
            count += 1;
        
            var prodContainer = document.createElement('div');
            prodContainer.classList.add('item');

            var name = document.createElement("h2");
            name.textContent = myProducts.products[i].name;

            var info = document.createElement("p");

            if(myProducts.products[i].storage != "" && myProducts.products[i].size != ""){
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Storage Capacity:</strong> ${myProducts.products[i].storage}<br><strong>Size:</strong> ${myProducts.products[i].size}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }
            else{
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }

            var img = document.createElement("img");
            img.src = myProducts.products[i].url;
            img.width = 300;


            prodContainer.appendChild(name);
            prodContainer.appendChild(info);
            prodContainer.appendChild(img);


            const currentRow = container.lastChild;
            currentRow.appendChild(prodContainer);
        }
    }
}

function putWirelessHeadphones(myProducts){
    var container = document.querySelector('.container4');
    var count = 0;
    for(var i = 0; i < myProducts.products.length; i++){

        if(myProducts.products[i].type === "Wireless Headphones"){
            if (count % 3 === 0) {
                var row = document.createElement('div');
                row.classList.add('row');
                container.appendChild(row);
            }
            count += 1;
        
            var prodContainer = document.createElement('div');
            prodContainer.classList.add('item');

            var name = document.createElement("h2");
            name.textContent = myProducts.products[i].name;

            var info = document.createElement("p");

            if(myProducts.products[i].storage != "" && myProducts.products[i].size != ""){
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Storage Capacity:</strong> ${myProducts.products[i].storage}<br><strong>Size:</strong> ${myProducts.products[i].size}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }
            else{
                info.innerHTML = `<strong>Type:</strong> ${myProducts.products[i].type}<br><strong>Brand:</strong> ${myProducts.products[i].brand}<br><strong>Release Year:</strong> ${myProducts.products[i].year}<br><strong>Price:</strong> ${myProducts.products[i].price}<br><strong>Battery Capacity:</strong> ${myProducts.products[i].battery}`;
            }

            var img = document.createElement("img");
            img.src = myProducts.products[i].url;
            img.width = 300;


            prodContainer.appendChild(name);
            prodContainer.appendChild(info);
            prodContainer.appendChild(img);


            const currentRow = container.lastChild;
            currentRow.appendChild(prodContainer);
    }
    }
}
