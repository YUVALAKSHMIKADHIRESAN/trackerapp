<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Details</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <h1>Employee Details</h1>
    <p>Name: ${employeeName}</p>

    <h2>Daily Work Hours</h2>
    <form action="EmployeeDetailsServlet" method="post">
        <input type="hidden" name="employeeName" value="${employeeName}">
        <label for="dailyDate">Select Date:</label>
        <input type="date" id="dailyDate" name="dailyDate" required>
        <button type="submit">Show Daily Chart</button>
    </form>
    <div class="chart-container">
        <canvas id="dailyChart"></canvas>
    </div>

    <h2>Weekly Work Hours</h2>
    <form action="EmployeeDetailsServlet" method="post">
        <input type="hidden" name="employeeName" value="${employeeName}">
        <label for="weeklyDate">Select Week:</label>
        <input type="week" id="weeklyDate" name="weeklyDate" required>
        <button type="submit">Show Weekly Chart</button>
    </form>
    <div class="chart-container">
        <canvas id="weeklyChart"></canvas>
    </div>

    <h2>Monthly Work Hours</h2>
    <form action="EmployeeDetailsServlet" method="post">
        <input type="hidden" name="employeeName" value="${employeeName}">
        <label for="monthlyDate">Select Month:</label>
        <input type="month" id="monthlyDate" name="monthlyDate" required>
        <button type="submit">Show Monthly Chart</button>
    </form>
    <div class="chart-container">
        <canvas id="monthlyChart"></canvas>
    </div>

    <script>
        function renderPieChart(chartId, data, label) {
            var ctx = document.getElementById(chartId).getContext('2d');
            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        label: label,
                        data: Object.values(data),
                        backgroundColor: [
                            '#FF6384', '#36A2EB', '#FFCE56', 
                            '#4BC0C0', '#9966FF', '#FF9F40'
                        ],
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                }
            });
        }

        function renderBarChart(chartId, data, label) {
            var ctx = document.getElementById(chartId).getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        label: label,
                        data: Object.values(data),
                        backgroundColor: '#36A2EB',
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Hours'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Days'
                            }
                        }
                    }
                }
            });
        }

        var dailyData = JSON.parse('${dailyData}');
        var weeklyData = JSON.parse('${weeklyData}');
        var monthlyData = JSON.parse('${monthlyData}');

        renderPieChart('dailyChart', dailyData, 'Daily Work Hours by Task');
        renderBarChart('weeklyChart', weeklyData, 'Weekly Work Hours');
        renderBarChart('monthlyChart', monthlyData, 'Monthly Work Hours');
    </script>
    <form action="AdminHome.jsp" method="post">
        <input type="submit" value="Back" class="back-button">
    </form>
    </div>
</body>
</html>
