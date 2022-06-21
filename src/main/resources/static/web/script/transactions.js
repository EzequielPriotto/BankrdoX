Vue.createApp({
    data() {
        return {
            dataBase: [],
            mailFooterInput: "",
            transactions: [],
            accounts: [],
            accountsSend: [],
            notificacionesCortadas:[],
            notificaciones: [
            ],
            typeForm: 0,
            accountSelect: "",
            accountSelectNumber: "",
            accountDestiny: "",
            amount: 0,
            isActiveDrop: false,
            isActiveDrop2: false,
            transferencia:{},
            errorAmount:false,
            errorAccountOrigin:false,
            errorAccountDestiny:false,
            dateInputFrom:"",
            dateInputTo:"",

        }
    },

    created() {
        axios.get(`/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.accounts = this.dataBase.accounts.filter(account=> account.active).sort((x, y) => x.id - y.id)
                this.accounts.forEach(account => {
                    account.transactions.forEach(trans => {
                        this.transactions.push(trans)
                    })
                });
                this.transactions = this.transactions.sort((x, y) => new Intl.Collator().compare(y.date, x.date))
                desableLoad();

                this.accountSelect = this.dataBase.accounts[0]
                this.accountSelectNumber = this.accountSelect.number
                this.accountsSend = this.dataBase.accounts.filter(account => account.number != this.accountSelectNumber)
                this.accountsSend = this.accountsSend.filter(account => this.accountSelect.accountType == account.accountType)
                this.notificaciones = this.dataBase.notifications.sort((x,y)=>y.id - x.id)
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
            return months[date.getMonth()].toLowerCase() + " " + dia + ", " + hora + ":" + minutos + `${date.getHours() < 12 ? " am" : " pm"}`
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
        verifyTransfer() {
            axios.post("/api/clients/current/transactions/verify", `amount=${this.amount}&description=Transfer&accountSNumber=${this.accountSelectNumber}&accountRNumber=${this.accountDestiny}`)
                .then(response => {
                    if(response.data == "data correct"){
                        let container = document.querySelector(".confirmBody")
                        container.classList.toggle("active")
                    }
                })
                .catch(error => {
                    this.errorAmount = false;
                    this.errorAccountOrigin = false;
                    this.errorAccountDestiny = false;

                    if(error.request.response == "Amount no available"){
                        this.errorAmount = true;
                    }
                    if(error.request.response == "Missing account origin"){
                        this.errorAccountOrigin = true;
                    }
                    if(error.request.response == "Missing account destiny" || error.request.response == "The account destiny dont exist"){
                        this.errorAccountDestiny = true;
                    }
                   
                })
        }, 
        sendTransfer() {
            axios.post("/api/clients/current/transactions", `amount=${this.amount}&category=Transfer&description=Transfer from ${this.accountSelectNumber} to ${this.accountDestiny}&accountSNumber=${this.accountSelectNumber}&accountRNumber=${this.accountDestiny}`)
                .then(response => {
                    Swal.fire({
                        title: 'Are you sure?',
                        text: "You will not be able to go back on this action!",
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: 'Yes, I am sure!'
                      }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.reload();
                        }
                      })
                })
                .catch(error => console.log(error))
        }, 
        closeConfirm(){
            let container = document.querySelector(".confirmBody")
            container.classList.toggle("active")
        },
        activarForm(tipoForm) {
            this.typeForm = tipoForm;
            let paso1 = document.querySelector(".paso1");
            let paso2 = document.querySelector(".paso2");
            paso1.classList.toggle("active")
            paso2.classList.toggle("active")
            if (this.typeForm == 2) {
                this.accountDestiny = ""
            }
        },
        activateDropdown() {
            this.isActiveDrop = !this.isActiveDrop
        },
        activateDropdown2() {
            this.isActiveDrop2 = !this.isActiveDrop2
        },
        actualizarSelect() {
            
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
        abrirPDF(){
            let container = document.querySelector(".confirmBody2")
            container.classList.toggle("active")
        },
        changeAccount(number){
            let accountsOption = document.querySelectorAll(".accountOption")
            accountsOption.forEach(account=> account.classList.remove("active"))

            let accountFocus = document.querySelector("#id" + number)
            accountFocus.classList.add("active")

            number === "all"? this.accountFocusPDF = "all": this.accountFocusPDF  = number;
        },
        getPDF(){
          let transactionsPdf=[];
          this.accounts.forEach(account=>{
            account.transactions.forEach(transaction=>{
                transactionsPdf.push(transaction);
            })
          })
          transactionsPdf =  transactionsPdf.filter(transaction => transaction.date > this.dateInputFrom && transaction.date < this.dateInputTo)
          let url = "/api/transactions/resume";
          let data = {
            accountNumber: this.accountFocusPDF,
            dateFrom: this.dateInputFrom,
            dateTo: this.dateInputTo
          }
         
          axios.post(url,data,{ "responseType": 'blob' }
            ).then(response=> {
                console.log(response)
                var blob = new Blob([response.data]);
                var link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = `BankrdoX_Resume_${this.dataBase.firstName}_${this.dataBase.lastName}.pdf`;
                link.click();
          })
          .catch(error => console.log(error))
        }
        

    },
    computed: {

    }







}).directive('dragscroll', VueDragscroll).mount('#app')


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