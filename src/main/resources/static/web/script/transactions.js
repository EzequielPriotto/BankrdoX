Vue.createApp({
    data() {
        return {
            dataBase: [],
            mailFooterInput: "",
            transactions: [],
            accounts: [],
            accountsSend: [],
            notificaciones: [],
            typeForm: 0,
            accountSelect: "",
            accountSelectNumber: "",
            accountDestiny: "",
            amount: 0,
            isActiveDrop: false,
            isActiveDrop2: false,
            transferencia:{}
        }
    },

    created() {
        axios.get(`http://localhost:8080/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.accounts = this.dataBase.accounts.sort((x, y) => x.id - y.id)
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
                .then(response => window.location.href = "http://localhost:8080/web/login.html")
        },
        verifyTransfer() {
            axios.post("/api/clients/current/transactions/verify", `amount=${this.amount}&description=Transfer&accountSNumber=${this.accountSelectNumber}&accountRNumber=${this.accountDestiny}`)
                .then(response => {
                    if(response.data == "data correct"){
                        let container = document.querySelector(".confirmBody")
                        container.classList.toggle("active")
                    }
                })
                .catch(error => console.log(error))
        }, 
        sendTransfer() {
            axios.post("/api/clients/current/transactions", `amount=${this.amount}&description=Transfer&accountSNumber=${this.accountSelectNumber}&accountRNumber=${this.accountDestiny}`)
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
            if (this.dataBase.accounts != undefined) {
                this.accountSelect = this.dataBase.accounts.filter(account => account.number == this.accountSelectNumber)

                this.accountsSend = this.dataBase.accounts.filter(account => account.number != this.accountSelectNumber)
                    .filter(account => this.accountSelect[0].accountType == account.accountType)

                if (this.accountsSend.length > 0 && this.typeForm != 2) {
                    this.accountDestiny = this.accountsSend[0].number
                }

            }

        },
        

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