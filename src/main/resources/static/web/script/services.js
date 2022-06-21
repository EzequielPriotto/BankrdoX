Vue.createApp({
    data() {
        return {
            dataBase: [],
            mailFooterInput: "",
            notificacionesCortadas:[],
            notificaciones: [],
            cards:[],
        }
    },

    created() {
        axios.get(`/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
                 this.cards= this.dataBase.cards.filter(card=> card.active)
                this.notificaciones = this.dataBase.notifications.sort((x,y)=>y.id - x.id)
                this.notificaciones.forEach(notificacion => {
                    if(this.notificacionesCortadas.length < 3){
                        this.notificacionesCortadas.push(notificacion)
                    }})
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
        redireccionar() {
            window.location.href = "/web/create-loan.html"
        },
        getDateNotification(dateTrans) {
            const date = new Date(dateTrans)
            let dateNow = new Date()
            let year = "";
            let month = "";
            let hours = "";
            let minutes = "";

            if (date.getFullYear() != dateNow.getFullYear()) {
                year = parseInt(date.getFullYear()) - parseInt(dateNow.getFullYear())
                return year + " years ago"
            }
            if (date.getMonth() != dateNow.getMonth()) {
                month = parseInt(date.getMonth()) - parseInt(dateNow.getMonth())
                return month + " months ago"
            }
            if (date.getHours() != dateNow.getHours()) {
                hours = parseInt(date.getHours()) - parseInt(dateNow.getHours())
                if (hours < 0) {
                    hours = hours * -1
                }
                return hours + " hours ago"
            }
            if (date.getMinutes() != dateNow.getMinutes()) {
                minutes = parseInt(date.getMinutes()) - parseInt(dateNow.getMinutes())
                if (minutes < 0) {
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