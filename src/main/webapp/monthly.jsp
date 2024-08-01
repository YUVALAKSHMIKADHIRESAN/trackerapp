<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Monthly Progress</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <style>
        #monthlyProgressChart {
            max-width: 600px;  
            max-height: 400px; 
        }
    </style>
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
    
    <h1>Select a Month to View Work Hours</h1>
    <form id="monthlyForm" onsubmit="fetchMonthlyProgress(event)">
        <input type="hidden" id="id" name="id" value="<%= id %>">
        <input type="month" id="month" name="month" required>
        <input type="submit" value="Show Monthly Progress">
    </form>

    <canvas id="monthlyProgressChart"></canvas>

    <script>
        function fetchMonthlyProgress(event) {
            event.preventDefault();

            var id = document.getElementById('id').value;
            var month = document.getElementById('month').value;

            fetch('MonthlyProgressServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'id': id,
                    'month': month
                })
            })
            .then(response => response.json())
            .then(data => {
                var ctx = document.getElementById('monthlyProgressChart').getContext('2d');
                new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: data.labels,
                        datasets: [{
                            label: 'Work Hours',
                            data: data.values,
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
                        },
                        plugins: {
                            legend: {
                                display: false,
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
            })
        }
    </script>
    <form action="assosiateHome.jsp" method="post">
        <input type="submit" value="Back" class="back-button">
    </form>
    </div>
</body>
</html>
