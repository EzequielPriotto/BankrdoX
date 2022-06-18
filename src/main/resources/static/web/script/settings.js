Vue.createApp({
    data() {
        return {
            dataBase: [],
            userNameInput: "",
            avatarSelect: "",
            mailFooterInput:"",
            notificacionesCortadas:[],
            notificaciones: [
            ],
        }
    },

    created() {




        axios.get(`http://localhost:8080/api/clients/current`)
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.userNameInput = this.dataBase.userName;
                this.avatarSelect = this.dataBase.avatar;
                this.notificaciones = this.dataBase.notifications.sort((x,y)=>y.id - x.id)
                this.notificaciones.forEach(notificacion => {
                    if(this.notificacionesCortadas.length < 3){
                        this.notificacionesCortadas.push(notificacion)
                    }
                })
              
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

