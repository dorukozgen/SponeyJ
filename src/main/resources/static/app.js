var stompClient = null;
var started = false;
var atSong = false;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#userinfo").html("");
}

function connect() {
    var socket = new SockJS('/websocket-example');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/check', function (data) {
            const json = JSON.parse(data.body);
            if (json.success) {
                window.location.pathname = '/static/index.html';
            } else {
                alertCheck(json.message);
            }
        });
        stompClient.subscribe('/topic/start', function (data) {
            const json = JSON.parse(data.body);
            if (json.success) {
                if (!started) {
                    started = true;
                    showStop();
                } else {
                    showStart();
                    started = false;
                }
            } else {
                alertStart(json.message);
            }
        });
        stompClient.subscribe('/topic/update', function (data) {
            const { threadCount, totalStreams, successfulStreams, failedStreams, earning } = JSON.parse(data.body);
            update(threadCount, totalStreams, successfulStreams, failedStreams, earning);
        });
        stompClient.subscribe('/topic/expired', function (data) {
            const json = JSON.parse(data.body);
            if (json.expired) {
                window.location.pathname = json.path;
            }
        });

        // stompClient.subscribe('/topic/hwid', function (data) {
        // ...
        // });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function update(thread_count, total, success, fail, earning) {
    $("#currentThreadCount").text(thread_count);
    $("#totalStreams").text(total);
    $("#totalSuccess").text(success);
    $("#totalFail").text(fail);
    $("#totalEarning").text(earning + "$");
}

function showStop() {
    $('#statusView').removeClass('d-none');
    $('#startForm').addClass('d-none');
    $('#songsForm').addClass('d-none');
    $('#mainLogo').addClass('started');

    $('#startBtn').addClass('d-none');
    $('#stopBtn').removeClass('d-none');
    $('#stopBtn').prop('disabled', false);
}

function showStart() {
    $('#statusView').addClass('d-none');
    $('#startForm').removeClass('d-none');
    $('#songsForm').removeClass('d-none');
    $('#mainLogo').removeClass('started');

    $('#stopBtn').addClass('d-none');
    $('#startBtn').removeClass('d-none');
    $('#startBtn').prop('disabled', false);
}

function sendStart() {
    const alert = $('#startHelp');
    if (!alert.hasClass('d-none')) {
        alert.addClass('d-none');
    }

    if (started) {
        $('#stopBtn').prop('disabled', true);
    } else {
        $('#startBtn').prop('disabled', true);
    }

    const songsArray = $(".song_item span").map(function() { return $(this).text(); }).get();

    stompClient.send(
        "/app/start",
        {},
        JSON.stringify(
            {
                'songs': songsArray,
                'threadCount': $("#threadCount").val(),
                'useAccounts': $('#useAccounts')[0].checked,
                'useProxy': $('#useProxy')[0].checked,
                'proxyType': $('#proxyType').find(":selected").val()
            }
        )
    );
};

function alertStart(message) {
    // put message in in startHelp div with jquery
   $('#startHelp').text(message);

    const alert = $('#startHelp');
    if (alert.hasClass('d-none')) {
        alert.removeClass('d-none');
    }

    $('#startBtn').prop('disabled', false);
}

function sendCheck() {
    $('#checkBtn').prop('disabled', true);
    stompClient.send("/app/check", {}, JSON.stringify({ 'key': $("#key").val() }));
}

function alertCheck(message) {
    const alert = $('#keyHelp');
    alert.text(message);
    if (alert.hasClass('d-none')) {
        alert.removeClass('d-none');
    }
    $('#checkBtn').prop('disabled', false);
}

function addKeyword(keyword) {
    const keywordItem = $("<div class='song_item'><span class='inline-block mr-2'>" + keyword + "</span><button class='inline-block' onclick='removeKeyword(this)'>X</button></div>");
    $("#songContainer").append(keywordItem);
}

function removeKeyword(button) {
    const keywordItem = $(button).closest(".song_item");
    keywordItem.remove();
}

function songEntered() {
    if (atSong) {
        const keyword = $("#songUrl").val().trim();
        if (keyword !== "") {
            addKeyword(keyword);
            $("#songUrl").val("");
        }
    }
}

$(function () {
    connect();

    $("form").on('submit', function (e) {
        e.preventDefault();
        if (e.target.id === 'checkForm') {
                sendCheck();
        } else if (e.target.id === 'startForm') {
            if (atSong) return;
            const songs = $(".song_item span").map(function() { return $(this).text(); }).get();
            if (!songs.length) {
                alertStart('Please add at least one song');
            } else {
                sendStart();
            }
        }
    });
    $("#key").keyup(function (e) {
        const alert = $('#keyHelp');
        if (!alert.hasClass('d-none')) {
            alert.addClass('d-none');
        }
    });

    // click button
    $('#stopBtn').click(function() {
        sendStart();
    });

    // const keywordContainer = $("#songContainer");
    // const keywordInput = $("#songUrl");

     // keywordInput.on("keypress", function(e) {
     //   if (e.key === "Enter") {
     //       e.preventDefault();
     //       const keyword = keywordInput.val().trim();
     //       if (keyword !== "") {
     //           addKeyword(keyword);
     //           keywordInput.val("");
      //      }
      //  }
    //});

    $("#songUrl").on("focus", function(event) {
        atSong = true;
    });

    $("#songUrl").on("blur", function(event) {
        atSong = false;
    });

    $(document).on("click", ".remove-button", function() {
        $(this).closest(".keyword-item").remove();
    });



    // $( "#connect" ).click(function() { connect(); });
    // $( "#disconnect" ).click(function() { disconnect(); });
    // $( "#send" ).click(function() { sendName(); });
});