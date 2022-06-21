Vue.createApp({
    data() {
        return {
            dataBase: {},
            failed: true,
            token: "",
            passwordError:false,
            email:"",
            password:"",
            passwordConfirm:"",
        }
    },

    created() {

        const params = new Proxy(new URLSearchParams(window.location.search), {
            get: (searchParams, prop) => searchParams.get(prop),
        });

        this.token = params.token;

        axios.post("/api/verifyToken/" + this.token)
            .then(response => {
                this.dataBase = response.data
                this.failed = false

            })
            .catch(error => {
                this.failed = true
                console.log(error)
            })
    },

    methods: {

        changePassword() {
            this.passwordError = false;
            if (this.password == this.passwordConfirm) {
                axios.patch("/api/clients/changePassword", `token=${this.token}&email=${this.email}&newPassword=${this.password}`)
                    .then(response => {

                    })
                    .catch(error => console.log(error))
            }
            else{
                this.passwordError = true;
            }
        },

    },
}).mount('#app')
