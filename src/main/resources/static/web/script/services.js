Vue.createApp({
    data() {
        return {
            dataBase: [],
            mailFooterInput: "",
            notificacionesCortadas:[],
            notificaciones: [],
        }
    },

    created() {
        axios.get(`http://localhost:8080/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
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
                .then(response => window.location.href = "http://localhost:8080/web/login.html")
        },
        redireccionar() {
            window.location.href = "http://localhost:8080/web/create-loan.html"
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