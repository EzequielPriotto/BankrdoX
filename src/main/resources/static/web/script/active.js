Vue.createApp({
    data() {
        return {
            dataBase:{},
            failed: true,
        }
    },
  
    created() {
        const params = new Proxy(new URLSearchParams(window.location.search), {
            get: (searchParams, prop) => searchParams.get(prop),
          });
          let token = params.token; 
        axios.post("http://localhost:8080/api/activateAccount/" + token)
        .then(response=> {
            this.dataBase = response.data
            this.failed = false
        })
        .catch(error => {
            this.failed = true
        })
    },
  
    methods: {
        login(){
        window.location.href = "http://localhost:8080/web/login.html"
       },
    
    },
  }).mount('#app')
  