var stompClient = null;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    if (connected) {$("#conversation").show();}
    else {$("#conversation").hide();}
    $("#greetings").html("");}
function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/hello.user', function (greeting) {showGreeting(JSON.parse(greeting.body).content);});
        stompClient.subscribe('/topic/color', function(greeting){ setNewColor(JSON.parse(greeting.body).content);});});}
function showGreeting(message) {$("#greetings").append("<tr><td>" + message + "</td></tr>");}
function setNewColor(colorString) {document.getElementById("form").style.color = colorString;}
$(function () {connect();});
