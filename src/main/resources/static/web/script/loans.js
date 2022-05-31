Vue.createApp({
    data() {
        return {
            dataBase: [],
            mailFooterInput: "",
            loans: [],
            notificaciones:[]
        }
    },

    created() {
        axios.get(`http://localhost:8080/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.loans = this.dataBase.loans.sort((x, y) => x.id - y.id)
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
       

    },
    computed: {

    }







})
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