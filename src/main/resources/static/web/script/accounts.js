
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
            gastoTotal: 0,
            cards: [],
            cardsCortado: [],
            accountFocusCVU: {},
            accountTypeCreate: "DOLAR",
            

        }
    },

    created() {
        axios.get('http://localhost:8080/api/clients/current')
            .then(repuesta => {
                this.dataBase = repuesta.data
                this.accountsArray = this.dataBase.accounts
                this.accountsArray = this.accountsArray.sort((x, y) => x.id - y.id)
               
                this.accountFocus = this.accountsArray[0]

                this.accountsArray.forEach(cuenta => cuenta.transactions.forEach(compra => this.listadoComprasOriginal.push(compra)));
                this.listadoComprasOriginal = this.listadoComprasOriginal.sort((x, y) =>  Intl.Collator('en').compare(y.date, x.date))
                this.generarEstadisticas("chartdiv")
                this.generarEstadisticas("chartdiv3")
                this.generarEstadisticas2("chartdiv2")

                this.listadoComprasOriginal.forEach(compra => this.listadoComprasCortado.length < 5 ? this.listadoComprasCortado.push(compra) : "")
                this.listadoComprasFiltrado = this.listadoComprasCortado;

                this.cards = repuesta.data.cards.sort((x, y) => x.id - y.id)
                this.cards.forEach(card => this.cardsCortado.length < 3 ? this.cardsCortado.push(card) : "")
               
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
                let tipo = compra.description
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
        generarEstadisticas2() {
            let gastoTotal = this.gastoTotal;
            am5.ready(function () {

                // Create root element
                // https://www.amcharts.com/docs/v5/getting-started/#Root_element
                var root = am5.Root.new("chartdiv2");

                // Set themes
                // https://www.amcharts.com/docs/v5/concepts/themes/
                root.setThemes([
                    am5themes_Animated.new(root)
                ]);

                // Create chart
                // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/
                var chart = root.container.children.push(
                    am5percent.PieChart.new(root, {
                        startAngle: 160,
                        endAngle: 380,

                    })
                );

                // Create series
                // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Series






                var data = [
                    {
                        account: "Account1",
                        expense: 15012,

                    },
                    {
                        account: "Account2",
                        expense: 17823,

                    },
                    {
                        account: "Account3",
                        expense: 9043,

                    }
                ];

                var series1 = chart.series.push(
                    am5percent.PieSeries.new(root, {
                        startAngle: 160,
                        endAngle: 380,
                        valueField: "expense",
                        innerRadius: am5.percent(80),
                        categoryField: "account"
                    })
                );
                series1.ticks.template.set("forceHidden", true);
                series1.labels.template.set("forceHidden", true);
                //   series1.set("fill", am5.color("#00ff00"));

                // Set data
                // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Setting_data
                series1.data.setAll(data);
            })
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
                    return array.filter(compra => compra.description === this.typeCompraInput)
                }

            }

            this.listadoComprasFiltrado = filtro();


        },
        signOut() {
            axios.post('/api/logout')
                .then(response => window.location.href = "http://localhost:8080/web/login.html")
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
                    window.location.href = `http://localhost:8080/web/account.html?id=${account.id}`
                }
            })
        },
        addAccount() {
            axios.post("http://localhost:8080/api/clients/current/accounts/", `accountType=${this.accountTypeCreate}`)
                .then(
                    window.location.reload()
                )
                .catch(error => alert(error.request.message))
        },
        openOff(){
            let contenedor = document.querySelector(".contenedorS");
            let bodyOff = document.querySelector(".contenedorCreate");
            let html = document.querySelector("html");
            contenedor.classList.add("active");
            setTimeout(()=>  bodyOff.classList.add("active"), 500)
            html.classList.add("load")
        },
        closeOff(){
            let contenedor = document.querySelector(".contenedorS");
            let bodyOff = document.querySelector(".contenedorCreate");
            let html = document.querySelector("html");
            bodyOff.classList.remove("active")
            setTimeout(()=> contenedor.classList.remove("active") , 500)
            html.classList.remove("load")
        },
        openOffCVU(account){
            let contenedor = document.querySelector(".contenedorSCVU");
            let bodyOff = document.querySelector(".contenedorCreateCVU");
            let html = document.querySelector("html");
            contenedor.classList.add("active");
            setTimeout(()=>  bodyOff.classList.add("active"), 500)
            html.classList.add("load")
            this.accountFocusCVU = account
        },
        closeOffCVU(){
            let contenedor = document.querySelector(".contenedorSCVU");
            let bodyOff = document.querySelector(".contenedorCreateCVU");
            let html = document.querySelector("html");
            bodyOff.classList.remove("active")
            setTimeout(()=> contenedor.classList.remove("active") , 500)
            html.classList.remove("load")
        },
       


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
