Vue.createApp({
    data() {
        return {
            dataBase: [],
            dataBaseUser: [],
            mailFooterInput: "",
            cards: [],
            notificaciones: [],
            creditArray: [],
            debitArray: [],

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

        signOut() {
            axios.post('/api/logout')
                .then(response => window.location.href = "http://localhost:8080/web/login.html")
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
                        firstCardB.style.left = "1.5%";

                        setTimeout(() => {
                            firstCardF.style.animationPlayState = "running";
                            firstCardB.style.animationPlayState = "running";
                        }, 1100)

                    }

                    if (secondCardF != null) {
                        secondCardF.style.left = "12.9%";
                        secondCardB.style.left = "13.5%";
                        secondCardF.style.animationPlayState = "running";
                        secondCardB.style.animationPlayState = "running";

                    }
                    if (thirdCardF != null) {
                        thirdCardF.style.left = "19.9%";
                        thirdCardB.style.left = "20.5%";
                        thirdCardF.style.animationPlayState = "running";
                        thirdCardB.style.animationPlayState = "running";
                    }

                    break;
                case "id1":
                    firstCardF.style.left = "0.9%";
                    firstCardB.style.left = "1.5%";
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
                        secondCardB.style.left = "13.5%";
                        setTimeout(() => {
                            secondCardF.style.animationPlayState = "running";
                            secondCardB.style.animationPlayState = "running";
                        }, 1100)
                    }

                    if (thirdCardB != null) {

                        thirdCardF.style.left = "19.9%";
                        thirdCardB.style.left = "20.5%";
                        thirdCardF.style.animationPlayState = "running";
                        thirdCardB.style.animationPlayState = "running";
                    }

                    break;
                case "id2":

                    firstCardF.style.left = "0.9%";
                    firstCardB.style.left = "1.5%";
                    firstCardF.style.animationPlayState = "running";
                    firstCardB.style.animationPlayState = "running";

                    secondCardF.style.left = "12.9%";
                    secondCardB.style.left = "13.5%";
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
                        thirdCardB.style.left = "20.5%";
                        setTimeout(() => {
                            thirdCardF.style.animationPlayState = "running";
                            thirdCardB.style.animationPlayState = "running";
                        }, 1100)
                    }

                    break;
            }
        }

    },
    computed: {

    }







})
    .mount('#app')

$('.card__container').click(function () {
    var id = $(this).attr("id")
    console.log(id)
    // $('.card__back').toggleClass("active");
    // $('.card__front').toggleClass("active");
    // $('.card__back').toggleClass("desactive");
    $('.card__front').toggleClass("desactive");

});


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

