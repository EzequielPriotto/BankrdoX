Vue.createApp({
  data() {
    return {
      dataBase: [],
      dataBaseAccount: [],
      json1: {},
      firstname: "",
      lastname: "",
      email: "",
      listadoCuentas: [],
      clientAux: {},
      numberInput: "VIN",
      balanceInput: 0.0

    }
  },
  created() {
    axios.get('/api/clients')
      .then(repuesta => {
        this.dataBase = repuesta.data;
      })
      .catch(error => {
        console.log(error);
      })
    axios.get('/api/accounts')
      .then(repuesta => {
        this.dataBaseAccount = repuesta.data;
      })
      .catch(error => {
        console.log(error);
      })
  },
  methods: {


    addClient() {
      if (this.firstname != "" && this.lastname != "" && this.email != "") {

        let userAux = {
          firstName: this.firstname,
          lastName: this.lastname,
          email: this.email
        }

        axios.post('/rest/clients', userAux)
          .then(repuesta => location.reload())
          .catch(error => console.log(error))
      }
    },
    changeClient(user) {
      Swal.fire({
        title: `Valores para modifica al usuario ${user.firstName} ${user.lastName}`,
        html:
          `<input id="nombreInput" class="swal2-input" value="${user.firstName}">` +
          `<input id="apellidoInput" class="swal2-input" value="${user.lastName}">` +
          `<input id="emailInput" class="swal2-input" value="${user.email}">`,
        focusConfirm: false,
        preConfirm: () => {

          let nombreInput = document.getElementById('nombreInput').value;
          let apellidoInput = document.getElementById('apellidoInput').value;
          let emailInput = document.getElementById('emailInput').value;

          let userAux = {
            firstName: "",
            lastName: "",
            email: ""
          }

          userAux.firstName = nombreInput
          userAux.lastName = apellidoInput
          userAux.email = emailInput


          axios.patch(`/rest/clients/${user.id}`, userAux)
            .then(repuesta => location.reload())
            .catch(error => console.log(error))
        }
      })
    },
    deletedClient(user) {
      Swal.fire({
        title: 'Estas seguro de borrar este cliente?',
        text: "No podras volver atras!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si, estoy seguro!'
      }).then((result) => {
        if (result.isConfirmed) {
          user.accounts.forEach(cuenta => cuenta.transactions.forEach(trans => axios.delete(trans.link)));

          setTimeout(borrarAccounts, 10);
          setTimeout(borrarCliente, 10);

          let url = `/rest/clients/` + user.id

          function borrarCliente() {
            axios.delete(url)
            Swal.fire({
              title: 'Cliente Borrado!',
              text: "",
              icon: 'success',
              confirmButtonColor: '#3085d6',
              confirmButtonText: 'ok'
            }).then(confirmado => confirmado.isConfirmed ? location.reload() : "")
          }
          function borrarAccounts() {
            user.accounts.forEach(cuenta => axios.delete(cuenta.link));
          }
        }
      })

    },
    openModalAccount(user) {
      this.clientAux = user;
    },


    editBalanceAccount(cuenta) {
      let value = cuenta.balance
      Swal.fire({
        title: 'Ingrese el nuevo monto',
        input: 'text',
        inputValue: value,
        showCancelButton: true,

      }).then(value => {
        if (value) {

          let balance = parseFloat(value.value)
          axios.patch(cuenta.link, {
            "balance": balance
          })
            .then(Swal.fire({
              title: `Saldo cambiado, su nuevo saldo es: ${value.value}`,
              text: "",
              icon: 'success',
              confirmButtonColor: '#3085d6',
              confirmButtonText: 'ok'
            }).then(result => console.log(result.isConfirmed ? location.reload() : ""))
            )
        }
      })

    },
    deletedAccount(cuenta) {
      console.log(this.clientAux.accounts)

      if (this.clientAux.accounts.includes(cuenta)) {


        Swal.fire({
          title: 'Estas seguro de borrar este cliente?',
          text: "No podras volver atras!",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Si, estoy seguro!'
        }).then(result => {
          if (result.isConfirmed) {
            cuenta.transactions.forEach(trans => axios.delete(trans.link));
            setTimeout(borrarAccount, 10);

            function borrarAccount() {
              axios.delete(cuenta.link)
                .then(location.reload())
                .catch(error => console.log(error))
            }
          }
        })

      }


    },

  },

}).mount('#app')


