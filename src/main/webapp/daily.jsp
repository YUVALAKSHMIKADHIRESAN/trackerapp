<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Daily Work Hours</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <%
        HttpSession sess = request.getSession(false);
        String id = (String) sess.getAttribute("uid");
        if (id == null) {
            response.sendRedirect("associateHome.jsp");
        }
    %>
    
    <h1>Select a Date to View Work Hours</h1>
    <form id="workHoursForm">
        <input type="hidden" id="id" name="id" value="<%= id %>">
        <input type="date" id="date" name="date" placeholder="Enter Date" required>
        <input type="button" value="Show Work Hours" onclick="fetchWorkHours()">
    </form>

    <div class="chart-container">
        <canvas id="workHoursChart" width="50px" height="50px"></canvas>
    </div>
    
    <script>
        function fetchWorkHours() {
            var id = document.getElementById('id').value;
            var date = document.getElementById('date').value;
            
            fetch('DailyWorkHoursServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'id': id,
                    'date': date
                })
            })
            .then(response => response.json())
            .then(data => {
                var ctx = document.getElementById('workHoursChart').getContext('2d');
                new Chart(ctx, {
                    type: 'pie',
                    data: {
                        labels: data.labels,
                        datasets: [{
                            data: data.values,
                            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56','#4BC0C0','#F7464A'], 
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: {
                                position: 'top',
                            },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        let label = context.label || '';
                                        if (label) {
                                            label += ': ';
                                        }
                                        if (context.parsed !== null) {
                                            label += context.parsed + ' hours';
                                        }
                                        return label;
                                    }
                                }
                            }
                        }
                    }
                });
            });
        }
    </script>

    <form action="assosiateHome.jsp" method="post">
        <input type="submit" value="Back to Home" class="back-button">
    </form>
    </div>
</body>
</html>
