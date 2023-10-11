fetch("./index.html")
    .then(Response => Response.json)
    .then(myDevices => myDevices(devices));

function myDevices(devices) {

}
