
Vue.createApp({
  data() {
      return {
        emailInput: "",
        passwordInput: "",
        error: "",
        isError:false,
        registerFirstName:"",
        registerLastName:"",
        registerEmail:"",
        registerPassword:"",
        registerUserName:"",
      }
  },

  created() {


  },

  methods: {
    singIn(){
      axios.post('/api/login',`email=${this.emailInput}&password=${this.passwordInput}`,{
      headers:{'content-type':'application/x-www-form-urlencoded'}})
      .then(response =>window.location.href =  response.request.responseURL)
      .catch(error => {
        this.error = error.response.status == 900 ? "Incorrect Email or Password": ""
        console.log()
        this.isError = true;
      })
     },
     singUp(){
      axios.post('/api/clients',
      `firstName=${this.registerFirstName}&lastName=${this.registerLastName}&email=${this.registerEmail}&password=${this.registerPassword}&userName=${this.registerUserName}`,
      {headers:{'content-type':'application/x-www-form-urlencoded'}})
      .then(response => {
        axios.post('/api/login',`email=${this.registerEmail}&password=${this.registerPassword}`,{
          headers:{'content-type':'application/x-www-form-urlencoded'}})
          .then(response => window.location.href = "http://localhost:8080/web/accounts.html")
      })
      .catch(error=>{
        console.log(error)
        let mensaje = error.response.data;
        alert(mensaje)
      })
     }
  },
}).mount('#app')




const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".contenedor");

sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});