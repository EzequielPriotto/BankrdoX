Vue.createApp({
    data() {
        return {
            idURL: "",
            dataBase: [],
            dataBaseUser: [],
            userNameInput: "",
            avatarSelect: "./assets/avatares/avatar1.png",
            userSettings: {},
            mailFooterInput: "",
            notificaciones: [
            ],
            notificacionesCortadas:[],
            amount: 0,
            number: "",
            transactions:[],
            errorAccount:false,
            errorAmount:false,


        }
    },

    created() {
        const params = new Proxy(new URLSearchParams(window.location.search), {
            get: (searchParams, prop) => searchParams.get(prop),
        });
        this.idURL = params.id;

        axios.get(`/api/clients/current`)
            .then(repuesta => {
                this.dataBaseUser = repuesta.data
                console.log(this.dataBaseUser)
                this.dataBase = this.dataBaseUser.accounts.filter(account => account.id == this.idURL)[0];
                console.log(this.dataBase)
                this.transactions = this.dataBase.transactions.sort((x, y) => new Intl.Collator().compare(y.date, x.date))
                console.log(this.transactions)

                this.notificaciones = this.dataBaseUser.notifications.sort((x,y)=>y.id - x.id)
                this.notificaciones.forEach(notificacion => {
                    if(this.notificacionesCortadas.length < 3){
                        this.notificacionesCortadas.push(notificacion)
                    }
                })
            })
    },

    methods: {
        getDateCreate(dateInput) {
            const months = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
            const date = new Date(dateInput);
            const dia = date.getDate() < 10 ? "0" + date.getDate() : date.getDate()
            const hora = date.getHours() < 10 ? "0" + date.getHours() : date.getHours()
            const minutos = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()
            return months[date.getMonth()].toLowerCase() + " " + dia + ", " + date.getFullYear() + " " + hora + ":" + minutos + `${date.getHours() < 12 ? " am" : " pm"}`
        },
        generateIdCifrada(id) {
            let idEncriptada = CryptoJS.AES.encrypt(id.toString(), "ASD");
            let idDesencriptada = CryptoJS.AES.decrypt(idEncriptada, "ASD");

            return idEncriptada.salt.toString()
        },
        enviarMail() {
            Swal.fire(
                'Mail send',
                `Soon you will receive more news to the ${this.mailFooterInput} address`,
                'success'
            ).then(result => result.isConfirmed ? location.reload() : "")
        },
        signOut() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/login.html")
        },
        sendTransfer() {
            axios.post("/api/clients/current/transactions", `amount=${this.amount}&category=Transfer&description=Transfer from ${this.dataBase.number} to ${this.number}&accountSNumber=${this.dataBase.number}&accountRNumber=${this.number}`)
                .then(response => console.log(response))
                .catch(error => {
                    this.errorAmount = false;
                    this.errorAccount = false;

                    if(error.request.response == "Amount no available"){
                        this.errorAmount = true;
                    }
                    if(error.request.response == "Missing account destiny" || error.request.response == "The account destiny dont exist"){
                        this.errorAccount = true;
                    }
                })
        },
        verify(){
            axios.post("/api/clients/current/transactions/verify", `amount=${this.amount}&description=Transfer&accountSNumber=${this.dataBase.number}&accountRNumber=${this.number}`)
            .then(response => this.sendTransfer())
            .catch(error => {
                this.errorAmount = false;
                this.errorAccount = false;

                if(error.request.response == "Amount no available"){
                    this.errorAmount = true;
                }
                if(error.request.response == "Missing account destiny" || error.request.response == "The account destiny dont exist"){
                    this.errorAccount = true;
                }
            })
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







}).directive('dragscroll', VueDragscroll)
    .mount('#app')




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
