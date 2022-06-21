
Vue.createApp({
    data() {
        return {
            dataBase: [],
            accountsArray: [],
            accountFocus: [],

            listadoComprasOriginal: [],
            listadoComprasCortado: [],
            listadoComprasFiltrado: [],
            listadoTypes: ['All', 'Food', 'Games', 'Services', 'Outfit', 'Others'],
            typeCompraInput: "All",

            indexFocus: 0,
            listadoComprasFocus: [],
            listAccountSecundary: [],


            estadisticas: {
                food: 0,
                games: 0,
                services: 0,
                outfit: 0,
                others: 0,
            },
            mailFooterInput: "",
            loans: [],

            gastoTotal: 0,
            cards: [],
            cardsCortado: [],
            accountFocusCVU: {},
            accountTypeCreate: "DOLAR",
            notificacionesCortadas: [],
            notificaciones: [
            ],

        }
    },

    created() {
        axios.get('/api/clients/current')
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.accountsArray = this.dataBase.accounts.filter(account => account.active)
                this.accountsArray = this.accountsArray.sort((x, y) => x.id - y.id)

                this.accountFocus = this.accountsArray[0]

                this.dataBase.accounts.forEach(cuenta => cuenta.transactions.forEach(compra => this.listadoComprasOriginal.push(compra)));
                this.listadoComprasOriginal = this.listadoComprasOriginal.sort((x, y) => Intl.Collator('en').compare(y.date, x.date))
                this.generarEstadisticas("chartdiv")
                this.generarEstadisticas("chartdiv3")

                this.listadoComprasOriginal.forEach(compra => this.listadoComprasCortado.length < 5 ? this.listadoComprasCortado.push(compra) : "")
                this.listadoComprasFiltrado = this.listadoComprasCortado;

                this.cards = repuesta.data.cards.sort((x, y) => x.id - y.id)
                this.cards.forEach(card => this.cardsCortado.push(card))

                this.notificaciones = this.dataBase.notifications.sort((x, y) => y.id - x.id)
                this.notificaciones.forEach(notificacion => {
                    if (this.notificacionesCortadas.length < 3) {
                        this.notificacionesCortadas.push(notificacion)
                    }
                })
                disableLoad();

            })
    },




    methods: {
        modificarData() {

            let dps = [
                { y: this.inputPrueba, label: "Google" },
                { y: 7.31, label: "Bing" },
                { y: 7.06, label: "Baidu" },
                { y: 4.91, label: "Yahoo" },
                { y: 1.26, label: "Others" }
            ]
            chart.options.data[0].dataPoints = dps;
            chart.render();
        },
        getDateCreate(dateInput) {
            const months = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
            const date = new Date(dateInput);
            return months[date.getMonth()].toLowerCase() + " " + date.getDate() + ", " + date.getFullYear()
        },
        getDateCard(dateInput) {
            let date = new Date(dateInput);
            // let month = date.getMonth()+1 < 10? "0" + (date.getMonth()+1) : date.getMonth()+1
            let month = (date.getMonth() + 1).toString().padStart(2, "0")
            let years = date.getFullYear().toString().slice(-2)
            return month + "/" + years
        },
        generarEstadisticas(id) {

            this.listadoComprasOriginal.forEach(compra => {
                let tipo = compra.category
                switch (tipo) {

                    case "Food":
                        this.estadisticas.food += compra.amount;
                        break;

                    case "Games":
                        this.estadisticas.games += compra.amount;
                        break;

                    case "Outfit":
                        this.estadisticas.outfit += compra.amount;
                        break;

                    case "Others":
                        this.estadisticas.others += compra.amount;
                        break;

                    case "Services":
                        this.estadisticas.services += compra.amount;
                        break;

                }
                this.gastoTotal += parseInt(compra.amount);

            })

            let food = this.estadisticas.food
            let games = this.estadisticas.games
            let services = this.estadisticas.services
            let others = this.estadisticas.others
            let outfit = this.estadisticas.outfit
            if (document.getElementById("accounts")) {

                am5.ready(function () {
                    // Create root element
                    // https://www.amcharts.com/docs/v5/getting-started/#Root_element
                    let root = am5.Root.new(`${id}`);

                    let myTheme = am5.Theme.new(root);

                    myTheme.rule("Label").setAll({
                        fill: am5.color(0xffffff),
                        fontSize: ".75rem"

                    });

                    // Set themes
                    // https://www.amcharts.com/docs/v5/concepts/themes/
                    root.setThemes([
                        am5themes_Animated.new(root),
                        myTheme
                    ]);

                    // Create chart
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/
                    let chart = root.container.children.push(am5xy.XYChart.new(root, {
                        panX: false,
                        panY: false,
                        wheelX: "none",
                        wheelY: "none",
                    }));

                    // Add cursor
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
                    let cursor = chart.set("cursor", am5xy.XYCursor.new(root, {}));
                    cursor.lineY.set("visible", false);

                    // Create axes
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
                    let xRenderer = am5xy.AxisRendererX.new(root, { minGridDistance: 30 });

                    let xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
                        maxDeviation: 0,
                        categoryField: "name",
                        renderer: xRenderer,
                        tooltip: am5.Tooltip.new(root, {})
                    }));

                    xRenderer.grid.template.set("visible", false);

                    let yRenderer = am5xy.AxisRendererY.new(root, {});
                    let yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
                        maxDeviation: 0,
                        min: 0,
                        extraMax: 0.1,
                        renderer: yRenderer
                    }));

                    yRenderer.grid.template.setAll({
                        strokeDasharray: [2, 2]
                    });

                    // Create series
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
                    let series = chart.series.push(am5xy.ColumnSeries.new(root, {
                        name: "Series 1",
                        xAxis: xAxis,
                        yAxis: yAxis,
                        valueYField: "value",
                        sequencedInterpolation: true,
                        categoryXField: "name",
                        tooltip: am5.Tooltip.new(root, { dy: -25, labelText: "{valueY}" })
                    }));


                    series.columns.template.setAll({
                        cornerRadiusTL: 5,
                        cornerRadiusTR: 5
                    });

                    series.columns.template.adapters.add("fill", (fill, target) => {
                        return am5.color("#fc5203");
                    });

                    series.columns.template.adapters.add("stroke", (stroke, target) => {
                        return am5.color("#fc5203")
                    });


                    // Set data
                    let data = [
                        {
                            name: "Food",
                            value: food,
                        },
                        {
                            name: "Games",
                            value: games,

                        },
                        {
                            name: "Services",
                            value: services,

                        },
                        {
                            name: "Outfit",
                            value: outfit,

                        }
                        , {
                            name: "Others",
                            value: others,

                        }
                    ];

                    series.bullets.push(function () {
                        return am5.Bullet.new(root, {
                            locationY: 1,
                            sprite: am5.Picture.new(root, {
                                templateField: "bulletSettings",
                                width: 50,
                                height: 50,
                                centerX: am5.p50,
                                centerY: am5.p50,
                                shadowColor: am5.color(0x000000),
                                shadowBlur: 4,
                                shadowOffsetX: 4,
                                shadowOffsetY: 4,
                                shadowOpacity: 0.6
                            })
                        });
                    });

                    xAxis.data.setAll(data);
                    series.data.setAll(data);

                    // Make stuff animate on load
                    // https://www.amcharts.com/docs/v5/concepts/animations/
                    series.appear(1000);
                    chart.appear(1000, 100);

                });


            }
        },
        enviarMail() {
            Swal.fire(
                'Mail send',
                `Soon you will receive more news to the ${this.mailFooterInput} address`,
                'success'
            ).then(result => result.isConfirmed ? location.reload() : "")
        },
        modificarListadoTransAll(filtroInput) {

            this.typeCompraInput = filtroInput
            const filtro = () => {
                let array = this.listadoComprasOriginal;
                if (this.typeCompraInput === "All") {
                    return this.listadoComprasCortado;
                }
                else {
                    return array.filter(compra => compra.category === this.typeCompraInput)
                }

            }

            this.listadoComprasFiltrado = filtro();


        },
        signOut() {
            Swal.fire({
                title: 'Are you sure?',
                text: "Your session will be closed and you will be redirected to the home page.",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, I am sure!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/logout')
                        .then(response => window.location.href = "/web/index.html")
                }
            })

        },
        redireccionarCard(lugar) {
            Swal.fire({
                title: 'Are you sure?',
                text: "You will be redirected to another tab!",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, I am sure!'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = `./${lugar}`;
                }
            })

        },
        linkearAccount(accountNumber) {
            console.log(accountNumber)

            this.accountsArray.forEach(account => {
                if (account.number === accountNumber) {
                    window.location.href = `/web/account.html?id=${account.id}`
                }
            })
            setTimeout(()=> Swal.fire(
                'Oh wow!',
                'You are trying to access a disabled account.!',
                'warning'
            ), 100)

        },
        addAccount() {
            axios.post("/api/clients/current/accounts/", `accountType=${this.accountTypeCreate}`)
                .then(
                    window.location.reload()
                )
                .catch(error => alert(error.request.message))
        },
        openOff() {
            let contenedor = document.querySelector(".contenedorS");
            let bodyOff = document.querySelector(".contenedorCreate");
            let html = document.querySelector("html");
            contenedor.classList.add("active");
            setTimeout(() => bodyOff.classList.add("active"), 500)
            html.classList.add("load")
        },
        closeOff() {
            let contenedor = document.querySelector(".contenedorS");
            let bodyOff = document.querySelector(".contenedorCreate");
            let html = document.querySelector("html");
            bodyOff.classList.remove("active")
            setTimeout(() => contenedor.classList.remove("active"), 500)
            html.classList.remove("load")
        },
        openOffCVU(account) {
            let contenedor = document.querySelector(".contenedorSCVU");
            let bodyOff = document.querySelector(".contenedorCreateCVU");
            let html = document.querySelector("html");
            contenedor.classList.add("active");
            setTimeout(() => bodyOff.classList.add("active"), 500)
            html.classList.add("load")
            this.accountFocusCVU = account
        },
        closeOffCVU() {
            let contenedor = document.querySelector(".contenedorSCVU");
            let bodyOff = document.querySelector(".contenedorCreateCVU");
            let html = document.querySelector("html");
            bodyOff.classList.remove("active")
            setTimeout(() => contenedor.classList.remove("active"), 500)
            html.classList.remove("load")
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
        selectAccountType(accountType) {
            let accounts = document.querySelectorAll(".accountType");
            accounts.forEach(account => account.classList.remove("active"))
            let account = document.querySelector("#" + accountType)
            account.classList.add("active")
            this.accountTypeCreate = accountType;
        },
        cancelAccount() {
            Swal.fire({
                title: 'Are you sure?',
                text: `Are you sure you want to cancel the account ${this.accountFocusCVU.number}?`,
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, I am sure!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("/api/clients/current/accounts/disable/", `accountNumber=${this.accountFocusCVU.number}`)
                        .then(response => response.data != "" ? alert("ups") : window.location.reload())
                        .catch(error => {
                            console.log(error.response.data)
                            if (error.response.data == "The client have only one account") {
                                Swal.fire(
                                    'Oh wow!',
                                    'It seems that this is the only account you have.!',
                                    'warning'
                                )
                            }
                            else if (error.response.data == "Account have money") {
                                Swal.fire({
                                    title: 'The account has money inside!',
                                    text: `Do you want to transfer your entire balance to another account and then delete the ${this.accountFocusCVU.number} account ?`,
                                    icon: 'question',
                                    showCancelButton: true,
                                    confirmButtonColor: '#3085d6',
                                    cancelButtonColor: '#d33',
                                    confirmButtonText: 'Yes, do it!'
                                }).then(result => {
                                    let cuentaDestino = this.accountsArray.filter(account => account != this.accountFocusCVU)
                                    cuentaDestino = cuentaDestino[0]
                                    axios.post("/api/clients/current/transactions", `amount=${this.accountFocusCVU.balance}&category=Transfer&description=Transfer from ${this.accountFocusCVU.number} to ${cuentaDestino.number}&accountSNumber=${this.accountFocusCVU.number}&accountRNumber=${cuentaDestino.number}`)
                                        .then(response => {
                                            axios.post("/api/clients/current/accounts/disable/", `accountNumber=${this.accountFocusCVU.number}`)
                                                .then(response => response.data != "" ? alert("ups") : window.location.reload())
                                                .catch(error => {
                                                    if (error.response.data == "The client have only one account") {
                                                        Swal.fire(
                                                            'Oh wow!',
                                                            'It seems that this is the only account you have.!',
                                                            'warning'
                                                        )
                                                    }
                                                })
                                        })
                                        .catch(response=>{
                                            
                                        })

                                })
                            }
                            
                        })
                }
            })
        }




    },
    computed: {

    }







})
    .directive('dragscroll', VueDragscroll)
    .mount('#app')



function disableLoad() {
    $('.loader').toggleClass('active');

}
// =====SLIDEBAR LOGICA====
$('.containerPromo').click(function () {
    $('.imgPromo').addClass("clicked");
})

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
