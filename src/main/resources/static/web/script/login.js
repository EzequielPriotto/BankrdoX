

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
        console.log(error.response)
        this.isError = true;
      })
     },
     singUp(){
      const loader = document.querySelector(".loader");
      const astro = document.querySelector(".astronauta");
      const mail = document.querySelector(".mailSend");
      const error = document.querySelector(".error");
      loader.classList.add("active")
      astro.classList.add("active")
      axios.post('/api/clients',
     `firstName=${this.registerFirstName}&lastName=${this.registerLastName}&email=${this.registerEmail}&password=${this.registerPassword}&userName=${this.registerUserName}`,
     {headers:{'content-type':'application/x-www-form-urlencoded'}})
     .then(response => {
          astro.classList.remove("active")
          mail.classList.add("active")
     })
     .catch(errorC=>{
      astro.classList.remove("active")
      error.classList.add("active")
     })
     },
     login(){
      axios.post('/api/login',`email=${this.registerEmail}&password=${this.registerPassword}`,{
        headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(response =>window.location.href =  response.request.responseURL)
        .catch(error => {
          this.error = error.response.status == 900 ? "Incorrect Email or Password": ""
          console.log(error.response)
          this.isError = true;
        })
     },
     tryAgain(){
      const loader2 = document.querySelector(".loader");
      const error2 = document.querySelector(".error");

      loader2.classList.remove("active")
      error2.classList.remove("active")
     },
     redirect(){
      window.location.href= "http://localhost:8080/web/index.html"
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