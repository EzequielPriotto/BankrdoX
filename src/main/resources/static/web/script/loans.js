Vue.createApp({
    data() {
        return {
            dataBase: [],
            mailFooterInput: "",
            loans: [],
            notificacionesCortadas:[],
            notificaciones: [],
            loansActives: [],
        }
    },

    created() {
        axios.get(`http://localhost:8080/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.loansActives = this.dataBase.loans.sort((x, y) => x.id - y.id)
                this.notificaciones = this.dataBase.notifications.sort((x,y)=>y.id - x.id)
                this.notificaciones.forEach(notificacion => {
                    if(this.notificacionesCortadas.length < 3){
                        this.notificacionesCortadas.push(notificacion)
                    }
                })
                desableLoad();
            })
        axios.get(`http://localhost:8080/api/loans`)
            .then(repuesta => {
                this.loans = repuesta.data
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
        redireccionar() {
            window.location.href = "http://localhost:8080/web/create-loan.html"
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