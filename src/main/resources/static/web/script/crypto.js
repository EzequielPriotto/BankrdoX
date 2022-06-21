Vue.createApp({
    data() {
        return {
            dataBase: [],
            notificacionesCortadas:[],
            notificaciones: [],
            cards: [],
            accountsCrypto: [],
            accountsUsd: [],
            fee: 0.5,
            origin:"CRYPTO-DOLLAR",
            accountCrypto: "",
            accountUsd:"",
            inputCrypto: 0,
            inputUsd: 0,
            errorAmountOrigin:false,
            errorAmountDestiny:false,
            errorAccountOrigin:false,
            errorAccountDestiny:false,

        }
    },

    created() {
        axios.get(`/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.cards = this.dataBase.cards;
                this.accountsCrypto = this.dataBase.accounts.filter(account => account.accountType == "CRYPTO")
                this.accountsUsd = this.dataBase.accounts.filter(account => account.accountType == "DOLAR")
                this.notificaciones = this.dataBase.notifications.sort((x,y)=>y.id - x.id)
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
        signOut() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/login.html")
        },
        selectCrypto(account) {
            this.accountCrypto = account.number

            let accountsCrypto = document.querySelectorAll(".accountFrom");
            accountsCrypto.forEach(account => {
                account.classList.remove("active")
            })
            let accountCrypto = document.querySelector(`#${account.number}`)
            accountCrypto.classList.add("active")

        },
        selectUsd(account) {
            this.accountUsd = account.number

            let accountsUsd = document.querySelectorAll(".accountDestiny");
            accountsUsd.forEach(account => {
                account.classList.remove("active")
            })
            let accountUsd = document.querySelector(`#${account.number}`)
            accountUsd.classList.add("active")

        },
        changeOrigin(){
            console.log( this.origin)
            if(this.origin == "CRYPTO-DOLLAR"){
              this.origin = "DOLLAR-CRYPTO" 
            }
            else {
                this.origin = "CRYPTO-DOLLAR"
            }
        },
        makeSwap(){
            let amount;
            if(this.origin == "CRYPTO-DOLLAR"){
                amount = this.inputCrypto
              }
              else {
                amount = this.inputUsd
              }
            axios.post("/api/clients/current/swap",`accountCryptoNumber=${this.accountCrypto}&accountDollarNumber=${this.accountUsd}&amount=${amount}&toFrom=${this.origin}`)
            .then(response=> window.location.href="/web/transfers.html")
            .catch(error => {
                this.errorAmountOrigin = false;
                this.errorAmountDestiny = false;
                this.errorAccountOrigin = false;
                this.errorAccountDestiny = false;

                if(error.response.data == "Amount no valid"){
                    this.errorAmountOrigin = true;
                    this.errorAmountDestiny = true;
                }
                if( error.response.data == "The dollars balance no are available"){
                    this.errorAmountDestiny = true;
                }
                if(error.response.data == "The crypto balance no are available"  ){
                    this.errorAmountOrigin = true;
                }
                else if(error.response.data == "No exist the crypto account"){
                    this.errorAccountOrigin = true;
                }
                else if(error.response.data == "No exist the dollar account" ){
                    this.errorAccountDestiny = true;
                }
               
                else{
                    console.log(error.response.data)
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
        updateAmount(){
            if(this.origin == "CRYPTO-DOLLAR"){
                this.inputUsd = this.inputCrypto * 20532;
            }
            else {
                this.inputCrypto = this.inputUsd / 20532;
            }
           
          
        }
    }







}).directive('dragscroll', VueDragscroll)
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