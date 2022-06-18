Vue.createApp({
    data() {
        return {
            dataBase: [],
            dataBaseUser: [],
            mailFooterInput: "",
            cards: [],
            notificacionesCortadas:[],
            notificaciones: [],
            creditArray: [],
            debitArray: [],
            isCredit: true,


        }
    },

    created() {
        axios.get(`http://localhost:8080/api/clients/current`)
            .then(repuesta => {
                this.dataBaseUser = repuesta.data
                this.cards = repuesta.data.cards.sort((x, y) => x.id - y.id)
                this.creditArray = this.cards.filter(card => card.cardType === "CREDIT")
                this.debitArray = this.cards.filter(card => card.cardType === "DEBIT")
                this.cardsFocus = this.creditArray
                this.notificaciones = this.dataBaseUser.notifications.sort((x,y)=>y.id - x.id)
                this.notificaciones.forEach(notificacion => {
                    if(this.notificacionesCortadas.length < 3){
                        this.notificacionesCortadas.push(notificacion)
                    }
                })
                desableLoad();

            })


    },

    methods: {

        enviarMail() {
            Swal.fire(
                'Mail send',
                `Soon you will receive more news to the ${this.mailFooterInput} address`,
                'success'
            ).then(result => result.isConfirmed ? location.reload() : "")
        },
        getDateCreate(dateInput) {
            const date = new Date(dateInput);
            let months = date.getMonth() < 10 ? "0" + date.getMonth() : date.getMonth()
            let year = date.getFullYear().toString().slice(-2)
            return months + "/" + year
        },

        separarNombre(texto, parte) {
            let newText = texto.split(" ");
            return newText[parte];
        },
        changeFocus(id) {

            let firstCardF = document.querySelector("#id0").firstChild;
            let firstCardB = document.querySelector("#id0").lastChild;

            let secondCardF = document.querySelector("#id1");
            let secondCardB = document.querySelector("#id1");
            if (secondCardF != null) {
                secondCardF = document.querySelector("#id1").firstChild;
                secondCardB = document.querySelector("#id1").lastChild;
            }

            let thirdCardF = document.querySelector("#id2");
            let thirdCardB = document.querySelector("#id2");

            if (thirdCardF != null) {
                thirdCardF = document.querySelector("#id2").firstChild;
                thirdCardB = document.querySelector("#id2").lastChild;
            }

            switch (id) {
                case "id0":

                    if (firstCardF.style.left == "0.9%") {

                        firstCardF.style.animationPlayState = "paused";
                        firstCardB.style.animationPlayState = "paused";

                        firstCardF.style.left = "45%";
                        firstCardB.style.left = "46%";
                        setTimeout(() => firstCardB.style.left = "61.2%", 1100)
                    }
                    else {
                        firstCardF.style.left = "0.9%";
                        firstCardB.style.left = "0.2%";

                        setTimeout(() => {
                            firstCardF.style.animationPlayState = "running";
                            firstCardB.style.animationPlayState = "running";
                        }, 1100)

                    }

                    if (secondCardF != null) {
                        secondCardF.style.left = "12.9%";
                        secondCardB.style.left = "12.2%";
                        secondCardF.style.animationPlayState = "running";
                        secondCardB.style.animationPlayState = "running";

                    }
                    if (thirdCardF != null) {
                        thirdCardF.style.left = "24.9%";
                        thirdCardB.style.left = "24.2%";
                        thirdCardF.style.animationPlayState = "running";
                        thirdCardB.style.animationPlayState = "running";
                    }

                    break;
                case "id1":
                    firstCardF.style.left = "0.9%";
                    firstCardB.style.left = "0.2%";
                    firstCardF.style.animationPlayState = "running";
                    firstCardB.style.animationPlayState = "running";


                    if (secondCardF.style.left == "12.9%") {
                        secondCardF.style.animationPlayState = "paused";
                        secondCardB.style.animationPlayState = "paused";
                        secondCardF.style.left = "45%";
                        secondCardB.style.left = "46%";
                        setTimeout(() => secondCardB.style.left = "61.2%", 1100)
                    }
                    else {
                        secondCardF.style.left = "12.9%";
                        secondCardB.style.left = "12.2%";
                        setTimeout(() => {
                            secondCardF.style.animationPlayState = "running";
                            secondCardB.style.animationPlayState = "running";
                        }, 1100)
                    }

                    if (thirdCardB != null) {

                        thirdCardF.style.left = "24.9%";
                        thirdCardB.style.left = "24.2%";
                        thirdCardF.style.animationPlayState = "running";
                        thirdCardB.style.animationPlayState = "running";
                    }

                    break;
                case "id2":

                    firstCardF.style.left = "0.9%";
                    firstCardB.style.left = "0.2%";
                    firstCardF.style.animationPlayState = "running";
                    firstCardB.style.animationPlayState = "running";

                    secondCardF.style.left = "12.9%";
                    secondCardB.style.left = "12.2%";
                    secondCardF.style.animationPlayState = "running";
                    secondCardB.style.animationPlayState = "running";

                    
                    if (thirdCardF.style.left == "24.9%") {
                        thirdCardF.style.animationPlayState = "paused";
                        thirdCardB.style.animationPlayState = "paused";
                        thirdCardF.style.left = "45%";
                        thirdCardB.style.left = "46%";
                        setTimeout(() => thirdCardB.style.left = "61.2%", 1100)
                    }
                    else {
                        thirdCardF.style.left = "24.9%";
                        thirdCardB.style.left = "24.2%";
                        setTimeout(() => {
                            thirdCardF.style.animationPlayState = "running";
                            thirdCardB.style.animationPlayState = "running";
                        }, 1100)
                    }

                    break;
            }
        },

        changeFocus2(id) {

            let firstCardF = document.querySelector("#id010").firstChild;
            let firstCardB = document.querySelector("#id010").lastChild;


            let secondCardF = document.querySelector("#id110");
            let secondCardB = document.querySelector("#id110");
            if (secondCardF != null) {
                secondCardF = document.querySelector("#id110").firstChild;
                secondCardB = document.querySelector("#id110").lastChild;
            }

            let thirdCardF = document.querySelector("#id210");
            let thirdCardB = document.querySelector("#id210");

            if (thirdCardF != null) {
                thirdCardF = document.querySelector("#id210").firstChild;
                thirdCardB = document.querySelector("#id210").lastChild;
            }

            switch (id) {
                case "id010":

                    if (firstCardF.style.left == "0.9%") {

                        firstCardF.style.animationPlayState = "paused";
                        firstCardB.style.animationPlayState = "paused";

                        firstCardF.style.left = "45%";
                        firstCardB.style.left = "46%";
                        setTimeout(() => firstCardB.style.left = "61.2%", 1100)
                    }
                    else {
                        firstCardF.style.left = "0.9%";
                        firstCardB.style.left = "0.2%";

                        setTimeout(() => {
                            firstCardF.style.animationPlayState = "running";
                            firstCardB.style.animationPlayState = "running";
                        }, 1100)

                    }

                    if (secondCardF != null) {
                        secondCardF.style.left = "12.9%";
                        secondCardB.style.left = "12.2%";
                        secondCardF.style.animationPlayState = "running";
                        secondCardB.style.animationPlayState = "running";

                    }
                    if (thirdCardF != null) {
                        thirdCardF.style.left = "19.9%";
                        thirdCardB.style.left = "19.2%";
                        thirdCardF.style.animationPlayState = "running";
                        thirdCardB.style.animationPlayState = "running";
                    }

                    break;
                case "id110":
                    firstCardF.style.left = "0.9%";
                    firstCardB.style.left = "0.2%";
                    firstCardF.style.animationPlayState = "running";
                    firstCardB.style.animationPlayState = "running";


                    if (secondCardF.style.left == "12.9%") {
                        secondCardF.style.animationPlayState = "paused";
                        secondCardB.style.animationPlayState = "paused";
                        secondCardF.style.left = "45%";
                        secondCardB.style.left = "46%";
                        setTimeout(() => secondCardB.style.left = "61.2%", 1100)
                    }
                    else {
                        secondCardF.style.left = "12.9%";
                        secondCardB.style.left = "12.2%";
                        setTimeout(() => {
                            secondCardF.style.animationPlayState = "running";
                            secondCardB.style.animationPlayState = "running";
                        }, 1100)
                    }

                    if (thirdCardB != null) {

                        thirdCardF.style.left = "19.9%";
                        thirdCardB.style.left = "19.2%";
                        thirdCardF.style.animationPlayState = "running";
                        thirdCardB.style.animationPlayState = "running";
                    }

                    break;
                case "id210":

                    firstCardF.style.left = "0.9%";
                    firstCardB.style.left = "0.2%";
                    firstCardF.style.animationPlayState = "running";
                    firstCardB.style.animationPlayState = "running";

                    secondCardF.style.left = "12.9%";
                    secondCardB.style.left = "12.9%";
                    secondCardF.style.animationPlayState = "running";
                    secondCardB.style.animationPlayState = "running";

                    
                    if (thirdCardF.style.left == "19.9%") {
                        thirdCardF.style.animationPlayState = "paused";
                        thirdCardB.style.animationPlayState = "paused";
                        thirdCardF.style.left = "45%";
                        thirdCardB.style.left = "46%";
                        setTimeout(() => thirdCardB.style.left = "61.2%", 1100)
                    }
                    else {
                        thirdCardF.style.left = "19.9%";
                        thirdCardB.style.left = "19.2%";
                        setTimeout(() => {
                            thirdCardF.style.animationPlayState = "running";
                            thirdCardB.style.animationPlayState = "running";
                        }, 1100)
                    }

                    break;
            }
        },
        
        signOut() {
            axios.post('/api/logout')
                .then(response => window.location.href = "http://localhost:8080/web/login.html")
        },
        getDateNotification(dateTrans){
            const date = new Date(dateTrans)
            let dateNow = new Date()
            let year = "";
            let month = "";
            let hours = "";
            let minutes = "";

            if(date.getFullYear() != dateNow.getFullYear()){
                year = parseInt(date.getFullYear()) - parseInt(dateNow.getFullYear()) 
                return year + " years ago"
            }
            if(date.getMonth() != dateNow.getMonth()){
                month = parseInt(date.getMonth()) - parseInt(dateNow.getMonth()) 
                return month + " months ago"
            }
            if(date.getHours() != dateNow.getHours()){
                hours = parseInt(date.getHours()) - parseInt(dateNow.getHours()) 
                if(hours < 0){
                    hours = hours * -1
                }
                 return hours + " hours ago"
            }
            if(date.getMinutes() != dateNow.getMinutes()){
                minutes = parseInt(date.getMinutes()) - parseInt(dateNow.getMinutes()) 
                if(minutes < 0){
                    minutes = minutes * -1
                }
                 return minutes + " minutes ago"
            }

           

        },

    },
    computed: {
       
    }


})
    .mount('#app')


function desableLoad() {
    $('.loader').toggleClass('active');

}

// =====SLIDEBAR LOGICA====

$('.btnNav').click(function () {
    $(this).toggleClass("click");
    $('.sidebar').toggleClass("show");
    $('.contenedorS').toggleClass("show");
    $('html').toggleClass("show");
});

$('.sidebar ul li a').click(function () {
    var id = $(this).attr('id');
    $('nav ul li ul.item-show-' + id).toggleClass("show");
    $('nav ul li #' + id + ' span').toggleClass("rotate");

});

