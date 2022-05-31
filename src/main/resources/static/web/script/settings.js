Vue.createApp({
    data() {
        return {
            dataBase: [],
            userNameInput: "",
            avatarSelect: "",
            mailFooterInput:"",
            notificaciones: [
                {
                        avatar: "./assets/avatares/avatar3.png",
                        name: "Pepito",
                        message: "accepted your transfer",
                        date: 2
                },
                {
                        avatar: "./assets/avatares/avatar5.png",
                        name: "Pepita",
                        message: "Send $1600 you",
                        date: 6
                },
                {
                        avatar: "./assets/avatares/avatar2.png",
                        name: "Edu",
                        message: "Send $42.6 you",
                        date: 7
                }
            ],
        }
    },

    created() {




        axios.get(`http://localhost:8080/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.userNameInput = this.dataBase.userName;
                this.avatarSelect = this.dataBase.avatar;

              
            })
    },

    methods: {
        cambiarAvatar(avatar) {
            switch (avatar) {
                case 1:
                    this.avatarSelect = "./assets/avatares/avatar1.png";
                    break;
                case 2:
                    this.avatarSelect = "./assets/avatares/avatar2.png";

                    break;
                case 3:
                    this.avatarSelect = "./assets/avatares/avatar3.png";

                    break;
                case 4:
                    this.avatarSelect = "./assets/avatares/avatar4.png";

                    break;
                case 5:
                    this.avatarSelect = "./assets/avatares/avatar5.png";

                    break;
                case 6:
                    this.avatarSelect = "./assets/avatares/avatar6.png";

                    break;
                case 7:
                    this.avatarSelect = "./assets/avatares/avatar7.png";

                    break;
                case 8:
                    this.avatarSelect = "./assets/avatares/avatar8.png";
                    break;
            }
        },
        cambiarUserName(){
        axios.patch('/api/clients/current',
        `avatar=${this.avatarSelect}&userName=${this.userNameInput}`,
        {headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(location.reload())
            
           

        },
        
        enviarMail(){
            // Swal.fire(
            //     'Mail send',
            //     `Soon you will receive more news to the ${this.mailFooterInput} address`,
            //     'success'
            //   ).then(result => result.isConfirmed ? location.reload() : "")
        },

    },
}).mount('#app')







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

