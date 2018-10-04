var devEnabled = false;

$(document).ready(function () {
    $("#refreshButton").click(function () {
        console.log("Refresh button was clicked.");
        loadStocks();
    });

    setupEnvironment();
    loadStocks();
});

function setupEnvironment() {
    if (document.location.search === "") {
        return;
    }
    var queries = {};
    $.each(document.location.search.substr(1).split('&'), function (c, q) {
        var i = q.split('=');
        console.log("i: " + i);
        console.log("q: " + q);
        queries[i[0].toString()] = i[1].toString();
    });
    if (queries.dev !== undefined) {
        console.log("DEV mode is enabled.");
        devEnabled = true;
    }
}

function loadStocks() {
    console.log("loadStocks()");
    $("#stocksList").empty();
    $("#refreshButton").attr("disabled", "disabled");

    if (devEnabled) {
        setTimeout(function () {
            $("#refreshButton").removeAttr("disabled");
            handleStockResponse(DUMMY_STOCKS_DATA);
        }, 1000);

        return;
    }
    $.ajax({
        url: "/api/stocks",
        type: "GET",
        dataType: "json"
    })
        .done(function (stocks) {
            handleStockResponse(stocks);
        })
        .fail(function (xhr, status, errorThrown) {
            alert("Loading stocks failed!");
            console.log("Error: " + errorThrown);
            console.log("Status: " + status);
            console.dir(xhr);
        })
        .always(function (xhr, status) {
            $("#refreshButton").removeAttr("disabled");
        });
}

function handleStockResponse(stocks) {
    console.log("Successfully retrieved " + stocks.length + " stocks.");

    $.each(stocks, function (index, stock) {
        var htmlName = $("<b>").text(stock.name);
        var htmlAmount = $("<span>").text(" ... " + formatAmount(stock.currentPrice));
        $("<li>").append(htmlName, htmlAmount).appendTo("#stocksList");
    });

}

function formatAmount(amount) {
    var value;
    if (amount.precision === 0) {
        value = amount.value
    } else {
        value = amount.value / Math.pow(10, amount.precision)
    }
    return value.toFixed(2) + " " + amount.currency
}
