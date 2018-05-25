if (!("WebSocket" in window)) {
    alert('NEMA websoketa');
    // $('#tekst, #send').fadeOut("fast");
    // $('Oh no, you need a browser that supports WebSockets. How about <a href="http://www.google.com/chrome">Google/a>?')
    //         .appendTo('#msgs');
} else {
    var host = "ws://localhost:8080/ChatAppWeb/wsep";
    try {
        socket = new WebSocket(host);
        // message('connect. Socket Status: ' + socket.readyState + "\n");

        socket.onopen = function() {
            alert('OTVORIO SOKET')
            // message('onopen. Socket Status: ' + socket.readyState
            //         + ' (open)\n');
        }

        socket.onmessage = function(msg) {
            // message('onmessage. Received: ' + msg.data + "\n");
            //parseMessage();   //bice switch case neki za sve
            alert('primio poruku sa servera : ' + msg);
        }

        socket.onclose = function() {
            // message('onclose. Socket Status: ' + socket.readyState
            //         + ' (Closed)\n');
            socket = null;
        }

    } catch (exception) {
        // message('Error' + exception + "\n");
    }
}

