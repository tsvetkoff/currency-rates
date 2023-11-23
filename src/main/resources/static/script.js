"use strict";

document.addEventListener('DOMContentLoaded', () => {

    function update() {
        fetch("/api/rates/current")
            .then(response => response.json())
            .then(rates => updateTable(rates));
    }

    function updateTable(rates) {
        const banks = [];
        rates.forEach(r => {
            if (banks.find(b => b.id === r.bank) === undefined) {
                const bank = {};
                bank.id = r.bank;
                bank.name = r.bankName;
                banks.push(bank);
            }
        });
        banks.sort((a, b) => a.name.localeCompare(b.name));

        const thead = createTHead(banks);
        const tbody = createTBody(rates, banks);

        const table = document.querySelector("table.rates");
        table.innerHTML = "";
        table.appendChild(thead);
        table.appendChild(tbody);
    }
    
    function createTHead(banks) {
        const tr = document.createElement("tr");
        tr.appendChild(document.createElement("th"));

        banks.forEach(bank => {
            const th = document.createElement("th");
            th.setAttribute("scope", "col");
            th.setAttribute("data-bank-id", bank.id);
            th.classList.add("text-center");
            th.innerText = bank.name;
            tr.appendChild(th);
        });

        const thead = document.createElement("thead");
        thead.appendChild(tr);
        return thead;
    }
    
    function createTBody(rates, banks) {
        const ratesByCurrencyAndBank = {};
        rates.forEach(r => {
            if (ratesByCurrencyAndBank[r.currency] === undefined) {
                ratesByCurrencyAndBank[r.currency] = {};
            }
            ratesByCurrencyAndBank[r.currency][r.bank] = r;
        });
    
        const tbody = document.createElement("tbody");
        for (let currency in ratesByCurrencyAndBank) {
          const th = document.createElement("th");
          th.setAttribute("scope", "row");
          th.classList.add("text-center");
          th.classList.add("align-middle");
          th.innerText = currency;

          const tr = document.createElement("tr");
          tr.appendChild(th);

          const ratesByBank = ratesByCurrencyAndBank[currency];
          banks.forEach(b => {
            const td = document.createElement("td");
            td.setAttribute("role", "button");
            const rate = ratesByBank[b.id];
            if (rate !== undefined) {
                td.classList.add("text-center");
                td.innerHTML = rate.purchase + '<br>' + rate.sale;
            }
            tr.appendChild(td);
          })

          tbody.appendChild(tr);
        }
        return tbody;
    }

    function createStockChart(currency, bankName, rateHistory) {
        const chart = document.querySelector("div.chart");
        if (rateHistory.length === 0) {
            chart.hidden = true;
            return
        }

        rateHistory.map(r => {
            r.timestamp = Date.parse(r.date);
            return r;
        }).sort((a, b) => a.timestamp - b.timestamp);

        const now = Date.now();

        const saleData = rateHistory.map(r => [r.timestamp, r.sale]);
        saleData.push([now, saleData[saleData.length - 1][1]]);

        const purchaseData = rateHistory.map(r => [r.timestamp, r.purchase]);
        purchaseData.push([now, purchaseData[purchaseData.length - 1][1]]);

        chart.hidden = false;
        Highcharts.stockChart(chart, {
            chart: {
                type: 'spline',
                zoomType: 'x'
            },
            rangeSelector: {
                selected: 1,
                buttons: [
                    {
                        type: 'day',
                        count: 1,
                        text: 'День'
                    },
                    {
                        type: 'week',
                        count: 1,
                        text: 'Нед'
                    },
                    {
                        type: 'month',
                        count: 1,
                        text: '1мес'
                    },
                    {
                        type: 'month',
                        count: 3,
                        text: '3мес'
                    },
                    {
                        type: 'month',
                        count: 6,
                        text: '6мес'
                    },
                    {
                        type: 'ytd',
                        text: 'YTD'
                    },
                    {
                        type: 'year',
                        count: 1,
                        text: 'Год'
                    },
                    {
                        type: 'all',
                        text: 'Все'
                    }
                ],
                buttonSpacing: 5
            },
            title: {
                text: `Курс ${currency} для банка ${bankName}`
            },
            time: {
                useUTC: false
            },
            xAxis: {
                ordinal: false
            },
            yAxis: {
                labels: {
                    enabled: false
                }
            },
            legend: {
                enabled: true
            },
            credits: {
                enabled: false
            },
            tooltip: {
                enabled: false
            },
            plotOptions: {
                series: {
                    showInNavigator: true,
                    marker: {
                        enabled: true
                    },
                    dataLabels: {
                        enabled: true
                    }
                }
            },
            series: [
                {
                    name: 'Покупка',
                    data: purchaseData
                },
                {
                    name: 'Продажа',
                    data: saleData
                }
            ]
        });
    }

    const table = document.querySelector("table.rates");
    table.addEventListener("click", e => {
        const target = e.target;
        if (target.tagName != 'TD') {
            return;
        }

        const currency = target.parentElement.children[0].innerText;

        const bankCell = table.querySelector("thead tr").children[target.cellIndex];
        const bank = bankCell.getAttribute("data-bank-id");
        const bankName = bankCell.innerText;

        fetch(`/api/rates?bank=${bank}&currency=${currency}`)
                    .then(response => response.json())
                    .then(rateHistory => createStockChart(currency, bankName, rateHistory));
    });

    update();
    setInterval(update, 10000);

});
