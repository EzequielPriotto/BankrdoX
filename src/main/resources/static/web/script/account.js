Vue.createApp({
    data() {
        return {
            idURL: "",
            dataBase:[],
            dataBaseUser: [],
            userNameInput: "",
            avatarSelect: "./assets/avatares/avatar1.png",
            userSettings: {},
            mailFooterInput:"",
            notificaciones: []

        }
    },

    created() {
        const params = new Proxy(new URLSearchParams(window.location.search), {
            get: (searchParams, prop) => searchParams.get(prop),
          });
          this.idURL = params.id; 

            axios.get(`http://localhost:8080/api/clients/current`)
            .then(repuesta => {
                this.dataBaseUser = repuesta.data
                console.log(this.dataBaseUser)
                this.dataBase = this.dataBaseUser.accounts.filter(account => account.id == this.idURL)[0];
                console.log(this.dataBase)
                this.transactions = this.dataBase.transactions.sort((x, y) => new Intl.Collator().compare(y.date, x.date))
                console.log(this.transactions)

                let userSettings = JSON.parse(localStorage.getItem("userSettings"));

                if (!userSettings) {
                    this.userNameInput = this.dataBaseUser.firstName + " "  + this.dataBaseUser.lastName
                    this.userSettings = 
                        {
                            avatar: this.avatarSelect,
                            userName: this.userNameInput,
                         
                        }
                    
                    localStorage.setItem("userSettings", JSON.stringify(this.userSettings));

                }
                else {
                    this.userSettings = userSettings;
                  
                }
            })
    },

    methods: {
        getDateCreate(dateInput) {
            const months = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
            const date = new Date(dateInput);
            return months[date.getMonth()].toLowerCase() + " " + date.getDate() + ", " + date.getFullYear()+ " " + date.getHours()+ ":" + date.getMinutes() + `${date.getHours() < 12 ? " am" : " pm"}`
        },
        generateIdCifrada (id){ 
            let idEncriptada = CryptoJS.AES.encrypt(id.toString(), "ASD");     
            let idDesencriptada = CryptoJS.AES.decrypt(idEncriptada, "ASD");
           
           return idEncriptada.salt.toString()
        },
        enviarMail(){
            Swal.fire(
                'Mail send',
                `Soon you will receive more news to the ${this.mailFooterInput} address`,
                'success'
              ).then(result => result.isConfirmed ? location.reload() : "")
        },
        signOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "http://localhost:8080/web/login.html")
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
