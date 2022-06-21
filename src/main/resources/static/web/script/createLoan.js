const APP = Vue.createApp({
    data() {
        return {
            loans: [],
            dataBaseUser: [],
            mailFooterInput:"",
            notificacionesCortadas:[],
            notificaciones: [
            ],
            formulario:{
                nombre:"",
                apellido:"",
                typeCard:"CREDIT",
                levelCard:"SILVER",
            },
            loanFocus:{},
            paymentSelect:0,
            amountLoan:0,
            dataOk:false,
            confirm:false,
            fullName: "",
            firma:false,
            fullName: "",
            accounts:[],
            accountDestiny:"",
            paymentPreview:0,
            loansAcceptes:[]

        }
    },

    created() {
            axios.get(`/api/loans`)
            .then(repuesta => {
                this.loans = repuesta.data
            })
            axios.get(`/api/clients/current/`)
            .then(repuesta => {
                this.dataBaseUser = repuesta.data
                this.accounts = this.dataBaseUser.accounts.filter(account => account.accountType != "CRYPTO")
                let ids =  this.dataBaseUser.loans.map(loan =>loan.idLoan)
                this.loansAcceptes = this.loans.filter(loan => !ids.includes(loan.id))
                this.notificaciones = this.dataBaseUser.notifications.sort((x,y)=>y.id - x.id)
                this.notificaciones.forEach(notificacion => {
                    if(this.notificacionesCortadas.length < 3){
                        this.notificacionesCortadas.push(notificacion)
                    }
                })
            })

            
    },

    methods: {
    
        enviarMail(){
            Swal.fire(
                'Mail send',
                `Soon you will receive more news to the ${this.mailFooterInput} address`,
                'success'
              ).then(result => result.isConfirmed ? location.reload() : "")
        },
        getDateCreate(dateInput) {
            const months = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
            const date = new Date(dateInput);
            return months[date.getMonth()]+ " "  + date.getFullYear()
        },
        signOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "/web/login.html")
        },
        confirmarFirma(){
            if(!signaturePad.isEmpty() ){
                this.firma = !this.firma;
            }
            else{
                this.firma = false 
            }
        },
        borrarFirma(){
            signaturePad.clear();
        },
        createPayment(){
            let newLoan ={
                idLoan: this.loanFocus.id,
                amount: this.amountLoan,
                payments: this.paymentSelect,
                numberAccount: this.accountDestiny
            }
            console.log(newLoan)
            axios.post('/api/clients/current/loans/',newLoan)
                .then(response =>window.location.href="/web/loans.html")
                .catch(error => console.log(error))
        },
        selectTypeLoan(loan){
            let loans =  document.querySelectorAll('.loanType');
            let loanItem = document.getElementById(`id${loan.id}`);
            this.loanFocus = loan;
            this.paymentSelect = "";
            loans.forEach(item => {
                item.classList.remove('active')
            })
            let payments =  document.querySelectorAll('.payment');
            if(payments.length > 0){
                payments.forEach(item => {
                    item.classList.remove('active')
                })
            }
            loanItem.classList.add('active')
            
        },
        selectAccount(account){
            let accounts =  document.querySelectorAll('.account');
            let accountItem = document.getElementById(`id${account.number}`);
            this.accountDestiny = account.number;
            accounts.forEach(item => {
                item.classList.remove('active')
            })
           
            accountItem.classList.add('active')
            
        },
        selectPayment(payment){
            let payments =  document.querySelectorAll('.payment');
            let paymentItem = document.getElementById(`id${payment}`);
            this.paymentSelect = payment;
            payments.forEach(item => {
                item.classList.remove('active')
            })
            paymentItem.classList.add('active')
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
       comprobarData(){
         if(this.paymentSelect != "" && this.loanFocus.name != "" && this.amountLoan > 0 && this.amountLoan < this.loanFocus.maxAmount && this.accountDestiny != ""){
            this.dataOk = true;
         }
         else{
            this.dataOk = false;
         }
         
         if(this.firma  && this.fullName.toLowerCase().includes(this.dataBaseUser.firstName.toLowerCase()) && this.fullName.toLowerCase().includes(this.dataBaseUser.lastName.toLowerCase()) &&  this.dataOk){
            this.confirm = true;
         }
         else{
            this.confirm = false;
         }
         if((this.amountLoan * this.loanFocus.fee) != NaN){
             console.log(this.paymentPreview)
             console.log(this.selectPayment)
             this.paymentPreview = (this.amountLoan * this.loanFocus.fee)  / parseInt(this.paymentSelect)
             } 
         else{
             this.paymentPreview = 0
             console.log(this.paymentPreview)
         } 
       }
    }







}).mount('#app')









let canvas = document.querySelector('canvas')
const signaturePad = new SignaturePad(canvas, {
    minWidth: 1,
    maxWidth: 2,
    penColor: "rgb(0, 0, 0)"
});

// Returns signature image as data URL (see https://mdn.io/todataurl for the list of possible parameters)
signaturePad.toDataURL(); // save image as PNG
signaturePad.toDataURL("image/jpeg"); // save image as JPEG
signaturePad.toDataURL("image/jpeg", 0.5); // save image as JPEG with 0.5 image quality
signaturePad.toDataURL("image/svg+xml"); // save image as SVG

// Returns signature image as an array of point groups
const data = signaturePad.toData();

// Draws signature image from an array of point groups
signaturePad.fromData(data);

// Draws signature image from an array of point groups, without clearing your existing image (clear defaults to true if not provided)
signaturePad.fromData(data, { clear: false });

// Clears the canvas


// Returns true if canvas is empty, otherwise returns false
signaturePad.isEmpty();

// Unbinds all event handlers
signaturePad.off();

// Rebinds all event handlers
signaturePad.on();


function download(dataURL, filename) {
    if (navigator.userAgent.indexOf("Safari") > -1 && navigator.userAgent.indexOf("Chrome") === -1) {
      window.open(dataURL);
    } else {
      var blob = dataURLToBlob(dataURL);
      var url = window.URL.createObjectURL(blob);
  
      var a = document.createElement("a");
      a.style = "display: none";
      a.href = url;
      a.download = filename;
  
      document.body.appendChild(a);
      a.click();
  
      window.URL.revokeObjectURL(url);
    }
  }

function pruebaAshe(elemento){
        console.log(id)
       
}

function guardar(){
    if (signaturePad.isEmpty()) {
        alert("Please provide a signature first.");
      } else {
        var dataURL = signaturePad.toDataURL();
        download(dataURL, "signature.png");
      }
}

$('.card__container').click(function () {
    var id = $(this).attr("id")
    console.log(id)
    $('.card__front').toggleClass("desactive");

});


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

