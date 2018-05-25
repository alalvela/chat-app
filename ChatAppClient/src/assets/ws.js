port = 8080;


if (!("WebSocket" in window)) {
    alert('NEMA websoketa');
} else {
    // var host = "ws://localhost:8080/ChatAppWeb/wsep";
    var host = "ws://localhost:" + port + "/ChatAppWeb/wsep";

    try {
        socket = new WebSocket(host);

        socket.onopen = function() {
            alert('OTVORIO SOKET')
            window.vueinstance.loginRedirect();
        }

        socket.onmessage = function(msg) {
            // poruka = msg.data
            // parseMessage(msg);
            window.vueinstance.testMethod(msg.data);
        }

        socket.onclose = function() {
            socket = null;
        }

    } catch (exception) {
    }
}