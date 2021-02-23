var stompClient = null;
let log = document.getElementById("log");
window.addEventListener('load', connect);
 var host = 'https://heroes.khryniewicki.com.pl';

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}

function connect() {
    var contextPath = window.location.protocol + "//" + window.location.host + '/websocket-example';
    console.log(contextPath);

    var socket = new SockJS(contextPath);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        setConnected(true);
        stompClient.subscribe('/topic/hero/1', function (coordinates) {
            receiveHeroCoordinates1(JSON.parse(coordinates.body));
        });
        stompClient.subscribe('/topic/hero/2', function (coordinates) {
            receiveHeroCoordinates2(JSON.parse(coordinates.body));
        });
        stompClient.subscribe('/topic/spell/1', function (spells) {
            receiveSpellDTO1(JSON.parse(spells.body));
        });
        stompClient.subscribe('/topic/spell/2', function (spells) {
            receiveSpellDTO2(JSON.parse(spells.body));
        });

        stompClient.subscribe('/topic/room', function (message) {
            receiveSpellDTO2(JSON.parse(message.body));
        });
    });
    log.innerText = "Connected"
}


function clearmap() {
    $.get(host + '/empty-room', function (data) {

        $('#textFromRestController').text(data);
        setTimeout(function () {
            $('#textFromRestController').text("");
        }, 30 * 1000);

    });
}


function kickoff() {

    stompClient.send("/app/room", {}, JSON.stringify({
        'content': 'FORCE DISCONNECTION FROM SERVER',
        'sessionID': '',
        'status': 'ConnectionStatus.DISCONNECTED'
    }));
    console.log("Players kicked off")
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    log.innerText = ("Disconnected");
}

function initRoom() {
    $.ajax({
        type: 'GET',
        crossDomain: true,
        dataType: 'jsonp',
        url: host + '/init-map',
        success: function () {
            console.log('room initiated')
        },
        error: function () {
            console.log('error')
        }
    })
}

function stopRoom() {
    $.ajax({
        type: 'GET',
        crossDomain: true,
        dataType: 'jsonp',
        url: host + '/stop-map',
        success: function () {
            console.log('room initiated')
        }
    })
}

function badgeTurnON(badge) {
    badge.innerText = "ON";
    badge.classList.remove("badge-danger");
    badge.classList.add("badge-success");
}

function badgeTurnOFF(badge) {
    badge.innerText = "OFF";
    badge.classList.remove("badge-success");
    badge.classList.add("badge-danger");
}

function receiveHeroCoordinates1(HeroDTO) {
    let badge1 = document.getElementById("badge_hero1");

    if (HeroDTO) {
        let description1 = document.getElementById('hero1');
        badgeTurnON(badge1);
        description1.innerText = HeroDTO.name + " [x: " + HeroDTO.positionX + ", y: " + HeroDTO.positionY + "]";
    } else
        badgeTurnOFF(badge1)
}

function receiveHeroCoordinates2(HeroDTO) {
    let badge2 = document.getElementById("badge_hero2");
    if (HeroDTO) {
        let elementById = document.getElementById('hero2');
        badgeTurnON(badge2);
        elementById.innerText = HeroDTO.name + " [x: " + HeroDTO.positionX + ", y: " + HeroDTO.positionY + "]";
    }
    badgeTurnOFF(badge2)
}

function receiveSpellDTO1(SpellDTO) {
    if (SpellDTO) {
        let elementById = document.getElementById('spell1');
        elementById.innerText = SpellDTO.name + " [TargetX: " + SpellDTO.targetSpellX + ", TargetY: " + SpellDTO.targetSpellY + "]";
    }
}

function receiveSpellDTO2(SpellDTO) {
    if (SpellDTO) {
        let elementById = document.getElementById('spell2');
        elementById.innerText = SpellDTO.name + " [TargetX: " + SpellDTO.targetSpellX + ", TargetY: " + SpellDTO.targetSpellY + "]";
    }
}






